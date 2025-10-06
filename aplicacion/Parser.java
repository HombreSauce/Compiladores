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






//#line 4 ".\gramatica.y"
	package aplicacion;
	import aplicacion.AnalizadorLexico;
	import aplicacion.Token;
	import datos.TablaSimbolos;
//#line 22 "Parser.java"




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
public final static short FOR=263;
public final static short FROM=264;
public final static short TO=265;
public final static short LAMBDA=266;
public final static short CV=267;
public final static short TRUNC=268;
public final static short ID=269;
public final static short CTEINT=270;
public final static short CTEFLOAT=271;
public final static short CTESTR=272;
public final static short ASIGN=273;
public final static short FLECHA=274;
public final static short IGUAL=275;
public final static short DISTINTO=276;
public final static short MENORIGUAL=277;
public final static short MAYORIGUAL=278;
public final static short MAS=279;
public final static short MUL=280;
public final static short DIV=281;
public final static short MENOS=282;
public final static short IGUALUNICO=283;
public final static short MAYOR=284;
public final static short MENOR=285;
public final static short PUNTO=286;
public final static short COMA=287;
public final static short PUNTOYCOMA=288;
public final static short PARENTINIC=289;
public final static short PARENTFIN=290;
public final static short LLAVEINIC=291;
public final static short LLAVEFIN=292;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    2,    2,    5,    5,    5,
    4,    4,    4,    4,    4,    4,    4,   15,   15,    6,
    6,    8,    9,   17,   17,   18,   18,   18,   16,   16,
   16,   19,   19,   19,   20,   20,   20,   13,   21,   21,
   22,   23,   23,   23,   14,    7,    7,   25,   26,   26,
   10,   27,   30,   30,   30,   30,   30,   30,   28,   28,
   29,   29,   11,   31,   31,   12,   24,   32,   32,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    2,    1,    3,    1,    3,    6,
    2,    2,    1,    1,    1,    2,    1,    1,    3,    1,
    3,    3,    3,    1,    3,    1,    1,    1,    3,    3,
    1,    3,    3,    1,    1,    1,    1,    4,    1,    3,
    3,    1,    4,    1,    5,    1,    3,    3,    0,    1,
    8,    3,    1,    1,    1,    1,    1,    1,    1,    3,
    0,    2,   10,    1,    3,    5,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    1,    3,    6,    0,    0,    0,   13,   14,   15,    0,
   17,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   11,   12,   16,    0,    0,   27,   26,   28,   36,    0,
    0,   37,    0,   34,    0,    0,    0,    0,    8,    0,
    7,    0,    0,    0,    0,    0,   39,    0,   44,    0,
   24,   18,    0,    0,   19,   55,   56,   57,   58,    0,
    0,   54,   53,    0,    0,    0,    0,    0,    0,    0,
    0,   50,    0,   46,    0,    0,    0,    0,    0,   38,
    0,    0,    0,    0,    0,   32,   33,    4,   59,    0,
   66,   45,    9,    0,    0,    0,    0,    0,    0,   40,
   41,   25,    0,    0,    0,   47,    2,   48,    0,   43,
    0,   60,    5,   62,    0,    0,    0,    4,   51,   10,
    0,    0,    4,   64,    0,    0,    0,   63,    0,   65,
   68,   69,    0,   67,
};
final static short yydgoto[] = {                          2,
    4,   12,  113,  123,   51,   14,   83,   15,   16,   17,
   18,   19,   39,   21,   40,   55,   60,   42,   43,   44,
   56,   57,   58,   59,   84,   85,   45,  100,  115,   74,
  135,  143,
};
final static short yysindex[] = {                      -261,
 -275,    0,    0, -250, -271, -264, -258, -236, -248, -242,
    0,    0,    0, -206, -249, -230,    0,    0,    0, -221,
    0, -272, -167, -167, -167, -181, -193, -171, -156, -184,
    0,    0,    0, -167, -173,    0,    0,    0,    0, -161,
  -52,    0, -160,    0, -158, -247, -219, -184,    0, -133,
    0, -111, -125,  -92, -189, -196,    0,  -93,    0, -100,
    0,    0, -161, -189,    0,    0,    0,    0,    0, -167,
 -167,    0,    0, -167, -167, -167, -207,  -90,  -87, -120,
 -161,    0, -164,    0,  -60,  -66, -167,  -57, -171,    0,
  -56, -156, -160, -160, -189,    0,    0,    0,    0,  -43,
    0,    0,    0, -133,  -73,  -50,  -44, -218,  -62,    0,
    0,    0, -233, -207,  -30,    0,    0,    0,  -39,    0,
  -53,    0,    0,    0,  -49, -240,  -48,    0,    0,    0,
 -174, -223,    0,    0,  -47,  -46, -212,    0,  -35,    0,
    0,    0,  -45,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -136,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -195,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -119,
    0,    0, -102,    0,    0,    0,    0,    0,    0,  -22,
    0,    0,    0,    0,  -28,    0,    0,    0,    0,  -41,
    0,    0, -209,  -40,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -109,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -85,  -68,  -38,    0,    0,    0,    0,  -15,
    0,    0,    0,  -22,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  132,    0,  -63,    5,    0,  202,    0,    0,    0,    0,
    0,    0,   -2,    0,   -4,  -19,    0,  -26,  114,  120,
    0,  162,    0,    0,  149,    0,    0,  140,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=254;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   34,   20,   61,   41,   46,   47,    5,    1,   13,    6,
    7,    8,    9,   35,   64,    3,    5,   23,   10,    6,
    7,    8,    9,    5,   24,   63,    6,    7,   10,    9,
   25,   70,   26,    5,   71,   10,    6,    7,   31,    9,
   27,   11,   78,   81,    5,   10,   28,    6,    7,    5,
    9,  130,    6,    7,   95,    9,   10,   32,  122,   70,
   70,   10,   71,   71,  132,  112,   33,  108,  136,  137,
   79,  120,   22,   21,   20,   52,   29,   21,   21,  140,
   30,   99,    5,   98,   62,    6,    7,   20,    9,   70,
   89,   20,   71,   90,   10,   65,   53,   10,   36,   37,
   38,   10,   36,   37,   38,   48,   49,   50,   22,   22,
   20,   20,  142,   36,   37,   38,  133,   54,   99,   75,
   76,   22,  104,   20,   35,  105,   22,   22,   20,   20,
   13,   77,   22,   82,   20,  134,   18,   18,   18,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   18,   18,
   18,   18,   86,   18,   35,   35,   35,   35,   35,   35,
   35,   35,   35,   87,   35,   35,   30,  103,   35,   88,
   35,   31,   31,   31,   31,   31,   31,   20,   20,   31,
   91,   31,   31,   93,   94,   31,   92,   31,   29,   29,
   29,   29,   29,   29,   96,   97,   29,  101,   29,   29,
  102,  106,   29,  107,   29,   30,   30,   30,   30,   30,
   30,  109,  111,   30,  114,   30,   30,  117,  118,   30,
  119,   30,   66,   67,   68,   69,   70,  121,  125,   71,
  127,   72,   73,  141,   36,   37,   38,  128,  129,   49,
  138,  131,  139,   61,  144,   42,   23,   22,  126,   80,
  110,   52,  116,  124,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
  273,    4,   29,   23,   24,   25,  257,  269,    4,  260,
  261,  262,  263,  286,   34,  291,  257,  289,  269,  260,
  261,  262,  263,  257,  289,   30,  260,  261,  269,  263,
  289,  279,  269,  257,  282,  269,  260,  261,  288,  263,
  289,  292,  290,   48,  257,  269,  289,  260,  261,  257,
  263,  292,  260,  261,   74,  263,  269,  288,  292,  279,
  279,  269,  282,  282,  128,   92,  288,   87,  292,  133,
  290,  290,   77,  283,   77,  269,  283,  287,  288,  292,
  287,   77,  257,  291,  269,  260,  261,  283,  263,  279,
  287,  287,  282,  290,  269,  269,  268,  269,  270,  271,
  272,  269,  270,  271,  272,  287,  288,  289,  113,  114,
  113,  114,  139,  270,  271,  272,  291,  289,  114,  280,
  281,  126,  287,  126,  286,  290,  131,  132,  131,  132,
  126,  290,  137,  267,  137,  131,  273,  274,  275,  276,
  277,  278,  279,  280,  281,  282,  283,  284,  285,  286,
  287,  288,  264,  290,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  289,  284,  285,  287,  288,  288,  262,
  290,  274,  275,  276,  277,  278,  279,  287,  288,  282,
  274,  284,  285,   70,   71,  288,  287,  290,  274,  275,
  276,  277,  278,  279,   75,   76,  282,  288,  284,  285,
  288,  262,  288,  270,  290,  274,  275,  276,  277,  278,
  279,  269,  269,  282,  258,  284,  285,  291,  269,  288,
  265,  290,  275,  276,  277,  278,  279,  290,  259,  282,
  270,  284,  285,  269,  270,  271,  272,  291,  288,  262,
  288,  290,  289,  259,  290,  274,  288,  288,  117,   48,
   89,  290,  104,  114,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=292;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"IF","ELSE","ENDIF","PRINT","RETURN","INT","FOR","FROM","TO",
"LAMBDA","CV","TRUNC","ID","CTEINT","CTEFLOAT","CTESTR","ASIGN","FLECHA",
"IGUAL","DISTINTO","MENORIGUAL","MAYORIGUAL","MAS","MUL","DIV","MENOS",
"IGUALUNICO","MAYOR","MENOR","PUNTO","COMA","PUNTOYCOMA","PARENTINIC",
"PARENTFIN","LLAVEINIC","LLAVEFIN",
};
final static String yyrule[] = {
"$accept : prog",
"prog : ID LLAVEINIC bloque LLAVEFIN",
"bloque :",
"bloque : bloque sentencia",
"bloque_ejec :",
"bloque_ejec : bloque_ejec sentencia_ejec",
"sentencia : sentencia_ejec",
"sentencia : INT ID decl_func",
"decl_func : PUNTOYCOMA",
"decl_func : COMA lista_ids PUNTOYCOMA",
"decl_func : PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
"sentencia_ejec : asign_simple PUNTOYCOMA",
"sentencia_ejec : asign_multiple PUNTOYCOMA",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion PUNTOYCOMA",
"sentencia_ejec : return_sent",
"var_ref : ID",
"var_ref : var_ref PUNTO ID",
"lista_ids : var_ref",
"lista_ids : lista_ids COMA var_ref",
"asign_simple : var_ref ASIGN expresion",
"asign_multiple : lista_ids IGUALUNICO lista_ctes",
"lista_ctes : cte",
"lista_ctes : lista_ctes COMA cte",
"cte : CTEFLOAT",
"cte : CTEINT",
"cte : CTESTR",
"expresion : expresion MAS termino",
"expresion : expresion MENOS termino",
"expresion : termino",
"termino : termino MUL factor",
"termino : termino DIV factor",
"termino : factor",
"factor : var_ref",
"factor : llamada_funcion",
"factor : cte",
"llamada_funcion : ID PARENTINIC lista_params_reales PARENTFIN",
"lista_params_reales : param_real_map",
"lista_params_reales : lista_params_reales COMA param_real_map",
"param_real_map : parametro_real FLECHA ID",
"parametro_real : expresion",
"parametro_real : TRUNC PARENTINIC expresion PARENTFIN",
"parametro_real : lambda_expr",
"return_sent : RETURN PARENTINIC expresion PARENTFIN PUNTOYCOMA",
"lista_params_formales : param_formal",
"lista_params_formales : lista_params_formales COMA param_formal",
"param_formal : sem_pasaje_opt INT ID",
"sem_pasaje_opt :",
"sem_pasaje_opt : CV",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF PUNTOYCOMA",
"condicion : expresion relop expresion",
"relop : MENOR",
"relop : MAYOR",
"relop : IGUAL",
"relop : DISTINTO",
"relop : MENORIGUAL",
"relop : MAYORIGUAL",
"rama_if : sentencia_ejec",
"rama_if : LLAVEINIC bloque_ejec LLAVEFIN",
"opt_else :",
"opt_else : ELSE rama_if",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for PUNTOYCOMA",
"rama_for : sentencia_ejec",
"rama_for : LLAVEINIC bloque_ejec LLAVEFIN",
"print_sent : PRINT PARENTINIC expresion PARENTFIN PUNTOYCOMA",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN PARENTINIC argumento PARENTFIN",
"argumento : ID",
"argumento : cte",
};

//#line 228 ".\gramatica.y"

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;

public static void main (String [] args) {
	TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
    System.out.println("Iniciando compilacion...");
    lex = new AnalizadorLexico (args[0]);
    par = new Parser (false);
    par.run();
	tablaSimbolos.mostrarTabla();
    System.out.println("Fin compilacion");
}

int yylex (){
        Token token = null;
        if ((token = lex.getToken()) != null) { 
            yylval = new ParserVal(token.getEntradaTS());
            System.out.println("Token ID: " + token.getIDToken() + ". ");
			if(token != null) {
				token.mostrarToken();
			}
            return token.getIDToken();
        } else {
            return 0; // Indica que no hay más tokens
        }
}

void yyerror (String mensaje){
    System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
}
//#line 406 "Parser.java"
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
case 22:
//#line 107 ".\gramatica.y"
{System.out.println("Esto es una asign_simple");}
break;
//#line 559 "Parser.java"
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
