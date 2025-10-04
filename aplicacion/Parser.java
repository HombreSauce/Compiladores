package aplicacion;
//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";










public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short ELSE=258;
public final static short ENDIF=259;
public final static short PRINT=260;
public final static short RETURN=261;
public final static short INT=262;
public final static short DFLOAT=263;
public final static short FOR=264;
public final static short FROM=265;
public final static short TO=266;
public final static short LAMBDA=267;
public final static short CV=268;
public final static short TRUNC=269;
public final static short ID=270;
public final static short CTE=271;
public final static short CADENA=272;
public final static short ASIGN=273;
public final static short FLECHA=274;
public final static short IGUAL=275;
public final static short DISTINTO=276;
public final static short MENORIGUAL=277;
public final static short MAYORIGUAL=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    2,    2,    2,    5,    4,
    4,    4,    4,    4,    4,    4,    7,   16,   16,   15,
   15,    8,    9,   18,   18,   19,   17,   17,   17,   20,
   20,   20,   21,   21,   21,   13,   22,   22,   23,   23,
   24,   25,   25,   25,   14,    6,   27,   27,   29,   30,
   30,   28,   31,   31,   32,   32,   10,   33,   36,   36,
   36,   36,   36,   36,   34,   34,   35,   35,   11,   37,
   37,   12,   12,   26,   38,   38,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    2,    1,    1,    1,    2,    2,
    2,    1,    1,    1,    2,    1,    2,    1,    3,    1,
    3,    3,    3,    1,    3,    1,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    4,    0,    1,    1,    3,
    3,    1,    4,    1,    5,    8,    1,    3,    3,    0,
    1,    1,    0,    2,    1,    1,    8,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    0,    2,   10,    1,
    3,    5,    5,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    1,    3,    7,    6,    8,    0,    0,    0,   12,   13,
   14,    0,   16,    0,    0,    0,    0,    0,    0,    0,
   20,    0,    0,    0,    9,   10,   11,   15,    0,    0,
    0,   26,   34,   33,    0,   35,    0,   32,    0,    0,
    0,    0,    0,    0,   19,    0,    0,    0,    0,    0,
   39,    0,   44,    0,   21,    0,   24,    0,   61,   62,
   63,   64,    0,    0,   59,   60,    0,    0,    0,    0,
    0,    0,    0,   51,    0,   47,    0,    0,    0,    0,
   36,    0,    0,    0,    0,    0,    0,   30,   31,    4,
   65,    0,   72,   73,   45,    0,    0,    0,    0,    0,
    0,   40,   41,   25,    0,    0,    0,   48,   53,   49,
    0,   43,    0,   66,    5,   68,    0,    0,    0,    0,
    4,   57,   46,    0,   56,   55,   54,    0,    0,    4,
   70,    0,    0,    0,   69,    0,   71,   75,   76,    0,
   74,
};
final static short yydgoto[] = {                          2,
    4,   12,  115,  125,   14,   15,   16,   17,   18,   19,
   20,   21,   43,   23,   24,   44,   58,   66,   46,   47,
   48,   59,   60,   61,   62,   63,   85,  128,   86,   87,
  129,  137,   49,  102,  117,   77,  142,  150,
};
final static short yysindex[] = {                      -256,
  -75,    0,    0, -102,   20,   35,   43, -175,   57,   13,
    0,    0,    0,    0,    0,   49,   50,   51,    0,    0,
    0,   52,    0,    2, -166, -202, -171, -202,   18,   60,
    0, -156, -155,  -11,    0,    0,    0,    0, -154, -153,
 -202,    0,    0,    0,  -21,    0,   14,    0,   76,   78,
   41,   46, -148, -144,    0,   82, -139,   45,   83,   85,
    0, -150,    0,   79,    0,   87,    0,   45,    0,    0,
    0,    0, -202, -202,    0,    0, -202, -202, -202, -118,
   67,   68,   74,    0,   32,    0, -130, -137, -202, -135,
    0,  -11, -134, -153,   14,   14,   45,    0,    0,    0,
    0, -121,    0,    0,    0, -148,   17, -125, -117,   53,
  113,    0,    0,    0,  -87, -118, -103,    0,    0,    0,
 -108,    0,   42,    0,    0,    0,  105,   44, -190,  125,
    0,    0,    0, -154,    0,    0,    0, -113,  -85,    0,
    0,  108,  131,  -70,    0, -192,    0,    0,    0,  137,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    5,    0,    0,    0,    6,  121,
    0,    0,    0,  140,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -34,    0,    0,    0,
    0,    0,  -80,    0,    0,    0,    0,  -96,    0,  145,
    0,    0,    0,  -14,    0,  133,    0,  134,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -28,   -8,  154,    0,    0,    0,
    0,  -61,    0,    0,    0,  -80,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   80,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    0, -104,   12,   70,    0,    0,    0,    0,    0,
    0,    0,   81,    0,    4,   73,   16,    0,  -32,   29,
   28,    0,    0,  109,    0,    0,    0,    0,   97,    0,
    0,    0,    0,   88,    0,    0,    0,    0,
};
final static int YYTABLESIZE=272;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         18,
   18,   18,   18,   18,  100,   18,   29,   67,   29,  140,
   29,   30,   27,    1,   27,   13,   27,   18,   18,   18,
   18,   73,   11,   74,   29,   29,  139,   29,   57,   18,
   27,   27,   28,   27,   28,  144,   28,  124,   75,  143,
   76,   45,   51,   52,   18,   39,   18,    3,   20,   18,
   28,   28,   34,   28,  147,   78,   68,   53,   33,   26,
   79,  114,   40,   33,   18,   20,    5,   10,   42,    6,
    7,  134,  107,    9,   27,  106,   25,  148,  149,   10,
   31,   82,   28,   73,   22,   74,   83,   73,   73,   74,
   74,  101,   97,  122,   29,   73,   32,   74,   10,   42,
   50,   95,   96,   39,  110,   98,   99,   35,   36,   37,
   38,   65,   41,   54,   55,   64,   80,   42,   81,   84,
   88,   89,   90,   91,   33,  103,  104,  101,   92,   93,
   94,  108,  105,  109,  111,  113,  116,   30,    5,  119,
  135,    6,    7,    5,  120,    9,    6,    7,  121,  141,
    9,   10,   25,  123,    5,  127,   10,    6,    7,    8,
   22,    9,  130,  132,  131,  138,  145,   10,  133,    5,
  146,    5,    6,    7,    6,    7,    9,  151,    9,   17,
   37,   50,   10,   42,   10,   38,    5,   25,   25,    6,
    7,   23,   22,    9,   58,   22,   22,   67,  136,   10,
  112,   25,  118,  126,   52,    0,   31,    0,    0,   22,
   25,   25,    0,    0,    0,    0,   25,    0,   22,   22,
    0,    0,    0,    0,   22,    0,    0,    0,    0,    0,
    0,    0,    0,   18,   18,   18,   18,   18,   18,    0,
   29,   29,   29,   29,    0,   29,   27,   27,   27,   27,
    0,   27,    0,   69,   70,   71,   72,   56,   10,   42,
    0,    0,    0,    0,    0,    0,   28,   28,   28,   28,
    0,   28,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,  123,   47,   41,   40,   43,  123,
   45,    8,   41,  270,   43,    4,   45,   59,   60,   61,
   62,   43,  125,   45,   59,   60,  131,   62,   40,   44,
   59,   60,   41,   62,   43,  140,   45,  125,   60,  125,
   62,   26,   27,   28,   59,   44,   61,  123,   44,   44,
   59,   60,   40,   62,  125,   42,   41,   40,   46,   40,
   47,   94,   61,   46,   59,   61,  257,  270,  271,  260,
  261,  262,   41,  264,   40,   44,    4,  270,  271,  270,
    8,   41,   40,   43,    4,   45,   41,   43,   43,   45,
   45,   80,   77,   41,  270,   43,   40,   45,  270,  271,
  272,   73,   74,   44,   89,   78,   79,   59,   59,   59,
   59,   39,  279,  270,  270,  270,   41,  271,   41,  268,
  265,   40,  262,   41,   46,   59,   59,  116,   44,  280,
   44,  262,   59,  271,  270,  270,  258,  134,  257,  123,
  129,  260,  261,  257,  270,  264,  260,  261,  266,  138,
  264,  270,   80,   41,  257,  259,  270,  260,  261,  262,
   80,  264,  271,   59,  123,   41,   59,  270,  125,  257,
   40,  257,  260,  261,  260,  261,  264,   41,  264,   59,
   41,  262,  270,  280,  270,   41,  257,  115,  116,  260,
  261,   59,   59,  264,   41,  115,  116,  259,  129,  270,
   92,  129,  106,  116,  125,   -1,  134,   -1,   -1,  129,
  138,  139,   -1,   -1,   -1,   -1,  144,   -1,  138,  139,
   -1,   -1,   -1,   -1,  144,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  275,  276,  277,  278,  279,  280,   -1,
  275,  276,  277,  278,   -1,  280,  275,  276,  277,  278,
   -1,  280,   -1,  275,  276,  277,  278,  269,  270,  271,
   -1,   -1,   -1,   -1,   -1,   -1,  275,  276,  277,  278,
   -1,  280,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=280;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","ELSE","ENDIF","PRINT","RETURN","INT",
"DFLOAT","FOR","FROM","TO","LAMBDA","CV","TRUNC","ID","CTE","CADENA","ASIGN",
"FLECHA","IGUAL","DISTINTO","MENORIGUAL","MAYORIGUAL","\":=\"","\"->\"",
};
final static String yyrule[] = {
"$accept : prog",
"prog : ID '{' bloque '}'",
"bloque :",
"bloque : bloque sentencia",
"bloque_ejec :",
"bloque_ejec : bloque_ejec sentencia_ejec",
"sentencia : sentencia_decl",
"sentencia : sentencia_ejec",
"sentencia : funcion",
"sentencia_decl : declaracion ';'",
"sentencia_ejec : asign_simple ';'",
"sentencia_ejec : asign_multiple ';'",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion ';'",
"sentencia_ejec : return_sent",
"declaracion : INT lista_ids",
"var_ref : ID",
"var_ref : ID '.' ID",
"lista_ids : var_ref",
"lista_ids : lista_ids ',' var_ref",
"asign_simple : var_ref \":=\" expresion",
"asign_multiple : lista_ids '=' lista_ctes",
"lista_ctes : cte",
"lista_ctes : lista_ctes ',' cte",
"cte : CTE",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : var_ref",
"factor : llamada_funcion",
"factor : cte",
"llamada_funcion : ID '(' lista_params_reales_opt ')'",
"lista_params_reales_opt :",
"lista_params_reales_opt : lista_params_reales",
"lista_params_reales : param_real_map",
"lista_params_reales : lista_params_reales ',' param_real_map",
"param_real_map : parametro_real \"->\" ID",
"parametro_real : expresion",
"parametro_real : TRUNC '(' expresion ')'",
"parametro_real : lambda_expr",
"return_sent : RETURN '(' expresion ')' ';'",
"funcion : INT ID '(' lista_params_formales ')' '{' cuerpo_funcion '}'",
"lista_params_formales : param_formal",
"lista_params_formales : lista_params_formales ',' param_formal",
"param_formal : sem_pasaje_opt INT ID",
"sem_pasaje_opt :",
"sem_pasaje_opt : CV",
"cuerpo_funcion : cuerpo_funcion_items",
"cuerpo_funcion_items :",
"cuerpo_funcion_items : cuerpo_funcion_items cuerpo_item",
"cuerpo_item : sentencia_decl",
"cuerpo_item : sentencia_ejec",
"bloque_if : IF '(' condicion ')' rama_if opt_else ENDIF ';'",
"condicion : expresion relop expresion",
"relop : '<'",
"relop : '>'",
"relop : IGUAL",
"relop : DISTINTO",
"relop : MENORIGUAL",
"relop : MAYORIGUAL",
"rama_if : sentencia_ejec",
"rama_if : '{' bloque_ejec '}'",
"opt_else :",
"opt_else : ELSE rama_if",
"bloque_for : FOR '(' ID FROM CTE TO CTE ')' rama_for ';'",
"rama_for : sentencia_ejec",
"rama_for : '{' bloque_ejec '}'",
"print_sent : PRINT '(' CADENA ')' ';'",
"print_sent : PRINT '(' expresion ')' ';'",
"lambda_expr : '(' INT ID ')' '{' bloque_ejec '}' '(' argumento ')'",
"argumento : ID",
"argumento : CTE",
};

//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}

private AnalizadorSintactico analizadorSintactico;

    public int yylex() { 
        return analizadorSintactico.yylex();
    }
    public void yyerror(String mensaje) {
        analizadorSintactico.yyerror(mensaje);
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
