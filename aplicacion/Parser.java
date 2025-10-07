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
	import datos.EntradaTablaSimbolos;
//#line 23 "Parser.java"




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
    6,    6,    6,    8,    9,    9,    9,    9,   17,   17,
   17,   18,   18,   18,   18,   18,   16,   16,   16,   19,
   19,   19,   20,   20,   20,   13,   21,   21,   22,   23,
   23,   23,   14,    7,    7,   25,   26,   26,   10,   27,
   30,   30,   30,   30,   30,   30,   28,   28,   29,   29,
   11,   31,   31,   12,   24,   32,   32,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    2,    1,    3,    1,    3,    6,
    2,    2,    1,    1,    1,    2,    1,    1,    3,    1,
    3,    3,    2,    3,    3,    2,    2,    3,    1,    3,
    3,    1,    2,    1,    2,    1,    3,    3,    1,    3,
    3,    1,    1,    1,    1,    4,    1,    3,    3,    1,
    4,    1,    5,    1,    3,    3,    0,    1,    8,    3,
    1,    1,    1,    1,    1,    1,    1,    3,    0,    2,
   10,    1,    3,    5,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    3,    6,    0,    0,    0,   13,   14,   15,
    0,   17,    0,    0,    0,    0,    0,    0,    0,   34,
   32,   36,    0,    0,   29,   18,    0,    0,    0,    0,
   11,   12,   16,    0,    0,   44,    0,    0,   45,    0,
   42,    0,    0,    0,    0,    8,    0,    7,    0,    0,
    0,    0,    0,   47,    0,   52,   35,   33,    0,   28,
    0,   22,    0,    0,   19,   63,   64,   65,   66,    0,
    0,   62,   61,    0,    0,    0,    0,    0,    0,    0,
    0,   58,    0,   54,    0,    0,    0,    0,    0,   46,
    0,   31,   30,    0,    0,    0,   40,   41,    4,   67,
    0,   74,   53,    9,    0,    0,    0,    0,    0,    0,
   48,   49,    0,    0,    0,   55,    2,   56,    0,   51,
    0,   68,    5,   70,    0,    0,    0,    4,   59,   10,
    0,    0,    4,   72,    0,    0,    0,   71,    0,   73,
   76,   77,    0,   75,
};
final static short yydgoto[] = {                          2,
    4,   13,  123,  133,   58,   15,   93,   16,   17,   18,
   19,   20,   46,   22,   47,   62,   34,   49,   50,   51,
   63,   64,   65,   66,   94,   95,   52,  111,  125,   84,
  145,  153,
};
final static short yysindex[] = {                      -261,
 -265,    0,    0, -248, -222, -218, -192, -233, -166, -158,
  -71,    0,    0,    0, -154, -219, -181,    0,    0,    0,
 -144,    0, -227,  -90,  -90,  -90, -102, -123,  -98,    0,
    0,    0, -253, -232,    0,    0, -172, -231, -118, -232,
    0,    0,    0,  -90,  -86,    0, -118,   20,    0, -190,
    0, -100, -234,  -72,  -63,    0,  -55,    0,  -47,  -70,
  -32, -214, -195,    0,  -40,    0,    0,    0, -168,    0,
 -232,    0, -118, -214,    0,    0,    0,    0,    0,  -90,
  -90,    0,    0,  -90,  -90,  -90, -241,  -43,  -41, -175,
 -118,    0, -179,    0,  -12,  -17,  -90,  -18,  -98,    0,
   -7,    0,    0, -190, -190, -214,    0,    0,    0,    0,
    3,    0,    0,    0,  -55,  -27,    1,    2,  -46,  -22,
    0,    0, -220, -241,   13,    0,    0,    0,    9,    0,
  -11,    0,    0,    0,   -6, -230,   -5,    0,    0,    0,
 -182, -203,    0,    0,   12,   -3, -187,    0,   21,    0,
    0,    0,    4,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -121,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -67,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   18,    0,    0,    0,    0,  -94,   19,
    0,    0,    0,    0,    0,    0,  -53,    0,    0,  -36,
    0,    0,    0,    0,    0,    0,   26,    0,    0,    0,
    0,   27,    0,    0,    0,    0,    0,    0,    0,    0,
   22,    0,  -74,   23,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -163,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -19,   -1,   24,    0,    0,    0,    0,
   49,    0,    0,    0,   26,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  182,    0,  -91,    6,    0,  257,    0,    0,    0,    0,
    0,    0,   -2,    0,   -4,  -20,  -14,   -8,   46,   50,
    0,  214,    0,    0,  200,    0,    0,  192,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=316;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         23,
   40,   21,   35,   48,   53,   54,   35,    1,    5,   14,
   39,    6,    7,    8,    9,    5,   67,   68,    6,    7,
   10,    9,   71,   74,   72,    3,    5,   10,   35,    6,
    7,    8,    9,   73,   11,   27,    5,   36,   10,    6,
    7,   11,    9,   12,   80,   44,  142,   81,   10,  109,
   91,  147,   11,    5,   69,   88,    6,    7,   45,    9,
  103,  140,   11,  106,   80,   10,   24,   81,   41,    5,
   25,  132,    6,    7,    5,    9,  119,    6,    7,   11,
    9,   10,   23,   70,   21,   39,   10,  102,  146,   85,
   86,   99,  110,   36,  100,   11,   26,   30,   31,   32,
   11,   30,   31,   32,  150,   20,   42,  115,  143,   33,
  116,   38,  114,   33,   36,   30,   31,   32,   23,   23,
   21,   21,   28,   20,   20,  104,  105,   33,   37,  110,
   29,   23,   38,   21,  107,  108,   23,   23,   21,   21,
  152,   14,   23,   43,   21,   59,  144,   18,   18,   18,
   18,   18,   18,   18,   18,   18,   18,   18,   18,   18,
   18,   18,   18,   18,   18,   18,   18,   45,   18,   60,
   10,   30,   31,   32,   23,   23,   23,   23,   10,   30,
   31,   32,   75,   33,   55,   56,   57,   23,   23,   87,
   61,   33,   23,   23,   21,   21,   21,   21,   30,   31,
   32,   20,   20,   20,   20,   36,   80,   21,   21,   81,
   33,   92,   21,   21,   20,   20,   96,   89,   97,   20,
   43,   43,   43,   43,   43,   43,   43,   43,   43,   98,
   43,   43,   80,  101,   43,   81,   43,   39,   39,   39,
   39,   39,   39,  130,  112,   39,  113,   39,   39,  117,
  120,   39,  118,   39,   37,   37,   37,   37,   37,   37,
  124,  122,   37,  127,   37,   37,  129,  131,   37,  128,
   37,  135,   38,   38,   38,   38,   38,   38,  137,  138,
   38,  139,   38,   38,  141,  149,   38,   57,   38,  151,
   30,   31,   32,  154,   76,   77,   78,   79,   80,  148,
   50,   81,   33,   82,   83,   26,   27,   69,  136,   25,
   24,   90,  121,   60,  126,  134,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
   15,    4,   11,   24,   25,   26,   15,  269,  257,    4,
   15,  260,  261,  262,  263,  257,  270,  271,  260,  261,
  269,  263,   37,   44,  256,  291,  257,  269,   37,  260,
  261,  262,  263,   38,  283,  269,  257,  269,  269,  260,
  261,  283,  263,  292,  279,  273,  138,  282,  269,  291,
   55,  143,  283,  257,  287,  290,  260,  261,  286,  263,
   69,  292,  283,   84,  279,  269,  289,  282,  288,  257,
  289,  292,  260,  261,  257,  263,   97,  260,  261,  283,
  263,  269,   87,  256,   87,   90,  269,  256,  292,  280,
  281,  287,   87,  269,  290,  283,  289,  270,  271,  272,
  283,  270,  271,  272,  292,  269,  288,  287,  291,  282,
  290,  287,  288,  282,  269,  270,  271,  272,  123,  124,
  123,  124,  289,  287,  288,   80,   81,  282,  283,  124,
  289,  136,  287,  136,   85,   86,  141,  142,  141,  142,
  149,  136,  147,  288,  147,  269,  141,  269,  270,  271,
  272,  273,  274,  275,  276,  277,  278,  279,  280,  281,
  282,  283,  284,  285,  286,  287,  288,  286,  290,  268,
  269,  270,  271,  272,  269,  270,  271,  272,  269,  270,
  271,  272,  269,  282,  287,  288,  289,  282,  283,  290,
  289,  282,  287,  288,  269,  270,  271,  272,  270,  271,
  272,  269,  270,  271,  272,  269,  279,  282,  283,  282,
  282,  267,  287,  288,  282,  283,  264,  290,  289,  287,
  274,  275,  276,  277,  278,  279,  280,  281,  282,  262,
  284,  285,  279,  274,  288,  282,  290,  274,  275,  276,
  277,  278,  279,  290,  288,  282,  288,  284,  285,  262,
  269,  288,  270,  290,  274,  275,  276,  277,  278,  279,
  258,  269,  282,  291,  284,  285,  265,  290,  288,  269,
  290,  259,  274,  275,  276,  277,  278,  279,  270,  291,
  282,  288,  284,  285,  290,  289,  288,  262,  290,  269,
  270,  271,  272,  290,  275,  276,  277,  278,  279,  288,
  274,  282,  282,  284,  285,  288,  288,  259,  127,  288,
  288,   55,   99,  290,  115,  124,
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
"lista_ids : lista_ids COMA error",
"lista_ids : lista_ids var_ref",
"asign_simple : var_ref ASIGN expresion",
"asign_multiple : lista_ids IGUALUNICO lista_ctes",
"asign_multiple : IGUALUNICO lista_ctes",
"asign_multiple : lista_ids lista_ctes",
"asign_multiple : lista_ids IGUALUNICO error",
"lista_ctes : cte",
"lista_ctes : lista_ctes COMA cte",
"lista_ctes : lista_ctes COMA error",
"cte : CTEFLOAT",
"cte : MENOS CTEFLOAT",
"cte : CTEINT",
"cte : MENOS CTEINT",
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

//#line 270 ".\gramatica.y"

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;
static TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
static int n_var = 0; //para contar variables en asignaciones multiples
static int n_cte = 0; //para contar ctes en asignaciones multiples

public static void main (String [] args) {
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
//#line 434 "Parser.java"
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
case 9:
//#line 81 ".\gramatica.y"
{n_var = 0;}
break;
case 20:
//#line 101 ".\gramatica.y"
{n_var++;}
break;
case 21:
//#line 102 ".\gramatica.y"
{n_var++;}
break;
case 22:
//#line 103 ".\gramatica.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 23:
//#line 104 ".\gramatica.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 24:
//#line 110 ".\gramatica.y"
{System.out.println("Esto es una asign_simple");}
break;
case 25:
//#line 115 ".\gramatica.y"
{
					if (n_var < n_cte) {
						yyerror("Error: más constantes que variables en la asignación");
					} else {
						System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
					}
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 26:
//#line 123 ".\gramatica.y"
{ yyerror("Error: falta lista de variables antes del '='"); }
break;
case 27:
//#line 124 ".\gramatica.y"
{ yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 28:
//#line 125 ".\gramatica.y"
{ yyerror("Error: falta lista de constantes después del '='");}
break;
case 29:
//#line 128 ".\gramatica.y"
{n_cte++;}
break;
case 30:
//#line 129 ".\gramatica.y"
{n_cte++;}
break;
case 31:
//#line 130 ".\gramatica.y"
{ yyerror("Error: falta una constante después de coma");}
break;
case 33:
//#line 136 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 34:
//#line 143 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor = entrada.getLexema();
			valor = valor.substring(0, valor.length() - 1); /*nos quedamos con el numero sin el I final*/
			int num = Integer.parseInt(valor);
			int max = 32767;
			/*al ser positivo debemos chequear el maximo*/
			if (num > max) {
				System.err.println("Error léxico: constante entera fuera de rango en línea " + lex.getLineaActual() + ": " + num);
				tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea());
			}

			yyval = val_peek(0);
		}
break;
case 35:
//#line 157 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
//#line 680 "Parser.java"
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
