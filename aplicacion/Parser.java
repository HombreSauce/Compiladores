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
	import datos.TablaIdentificadorToken;
	import datos.TablaPalabraReservada;
	import java.io.*;
	import java.nio.charset.StandardCharsets;
	import java.nio.file.*;

//#line 29 "Parser.java"




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
    0,    1,    1,    3,    3,    3,    2,    2,    2,    4,
    4,    4,    4,    4,    4,    4,    5,    5,    5,   15,
   15,   13,   13,   13,   13,    6,    7,    7,    7,    7,
   17,   17,   17,   18,   18,   18,   18,   18,   16,   16,
   16,   19,   19,   19,   20,   20,   20,   11,   21,   21,
   22,   23,   23,   23,   12,   14,   14,   25,   26,   26,
    8,    8,   27,   30,   30,   30,   30,   30,   30,   28,
   28,   29,    9,   31,   31,   10,   24,   32,   32,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    3,    2,    2,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    3,    6,    1,
    3,    1,    3,    3,    2,    3,    3,    2,    2,    3,
    1,    3,    3,    1,    2,    1,    2,    1,    3,    3,
    1,    3,    3,    1,    1,    1,    1,    4,    1,    3,
    3,    1,    4,    1,    4,    1,    3,    3,    0,    1,
    6,    7,    3,    1,    1,    1,    1,    1,    1,    1,
    3,    2,    9,    1,    3,    4,   11,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    3,    0,   10,   11,   12,   13,   14,   15,
   16,    0,    0,    0,    0,    0,    0,    0,    0,   36,
   34,   38,    0,    0,   31,    7,   20,    0,    0,    0,
    0,    0,    0,   46,    0,    0,   47,    0,   44,    0,
    0,    0,    0,   17,    0,    9,    0,    0,    0,    0,
    0,   49,    0,   54,   37,   35,    0,   30,    0,   24,
    0,    0,   21,   66,   67,   68,   69,    0,    0,   65,
   64,    0,    0,    0,    0,   76,   55,    0,    0,   60,
    0,   56,    0,    0,    0,    0,    0,   48,    0,   33,
   32,    0,    0,    0,   42,   43,    4,   70,    0,   18,
    0,    0,    0,    0,    0,    0,   50,   51,    0,    0,
   61,    0,   57,    2,   58,    0,   53,    0,   71,    0,
   72,   62,    0,    0,    4,    5,   19,    0,    0,    4,
   74,   73,    0,    0,    0,   75,    0,   78,   79,    0,
   77,
};
final static short yydgoto[] = {                          2,
    4,   13,  119,  130,   56,   15,   16,   17,   18,   19,
   44,   21,   22,   91,   45,   60,   34,   47,   48,   49,
   61,   62,   63,   64,   92,   93,   50,  109,  122,   82,
  142,  150,
};
final static short yysindex[] = {                      -250,
 -262,    0,    0,   77, -229, -217, -200, -177, -176, -169,
   61,    0,    0, -240,    0,    0,    0,    0,    0,    0,
    0, -160, -265, -204, -204, -204, -181, -143, -168,    0,
    0,    0, -259, -155,    0,    0,    0, -255, -236, -131,
 -155, -204,  -93,    0, -131,  202,    0, -129,    0, -220,
 -245, -203,  -91,    0,  -76,    0,  -71,  -95,  -67, -221,
 -216,    0,  -78,    0,    0,    0, -246,    0, -155,    0,
 -131, -221,    0,    0,    0,    0,    0, -204, -204,    0,
    0, -204, -204, -204,  145,    0,    0, -256, -131,    0,
 -199,    0,  -53,  -60, -204,  -57, -168,    0,  -46,    0,
    0, -129, -129, -221,    0,    0,    0,    0, -105,    0,
  -76,  -66,  -45,  -51, -149,  -64,    0,    0,   84,  145,
    0,  -32,    0,    0,    0,  -40,    0,  -62,    0,  -56,
    0,    0,  101,  -59,    0,    0,    0,  152,  118,    0,
    0,    0,  -48,  128,  -49,    0,  193,    0,    0,  -44,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -113,
    0,    0,    0,  111,    0,    0,    0,    0,    0,    0,
    0,    0,  189,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -219,    0,    0,    0,    0,    0,  162,
 -206,    0,    0,    0,  -77,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,  -17,    0,    0,    0,    0,  -26,
    0,    0,    0,    0,    0,    0,    0,    0, -164,    0,
  169,   67,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -241,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -5,   31,  -31,    0,    0,    0,    0,    0,    0,
  -17,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  135,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  126,    0,  -55,    5,    0,    0,    0,    0,    0,    0,
   -2,    0,  207,    0,   -4,  -20,  -15,   -8,  109,  106,
    0,  164,    0,    0,  151,    0,    0,  143,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=487;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         23,
   68,   20,   35,   46,   51,   52,   41,   42,   14,  100,
   65,   66,   37,   35,   30,   31,   32,   40,    1,   70,
   43,   72,   69,   30,   31,   32,   33,   22,    3,   35,
   39,  110,   37,   78,   71,   33,   79,   28,   28,   28,
   28,   28,   28,   28,   86,   22,   22,   36,   89,   28,
   29,   29,   29,   29,   29,   29,   29,   78,  101,   24,
   79,  104,   29,   28,   10,   30,   31,   32,   28,   85,
   97,   25,   28,   98,  115,   78,   29,   33,   79,  139,
   23,   29,   20,   40,  144,   29,   87,  111,   26,  108,
  112,   27,   27,   27,   27,   27,   27,   27,   27,   58,
   10,   30,   31,   32,   27,   53,   54,   55,   37,   30,
   31,   32,   28,   33,   23,   23,   20,   20,   27,   29,
   59,   33,   38,   27,  108,   57,   39,   27,   23,   78,
   20,   67,   79,   23,   23,   20,   20,   14,  149,   23,
  127,   20,  141,   20,   20,   20,   20,   20,   20,   20,
   83,   84,  120,  121,   43,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   20,   20,   20,   20,   73,   20,   37,   20,   45,
   45,   45,   45,   45,   45,   45,  102,  103,  105,  106,
   90,   45,   94,   95,   96,   99,   45,   45,   45,   45,
   45,   45,   45,   45,   45,   45,   45,   45,  113,  114,
   45,  116,   45,  126,   45,   41,   41,   41,   41,   41,
   41,   41,  118,  125,  124,  128,  132,   41,  135,  134,
  138,  136,   41,   41,   41,   41,   41,   41,  145,  147,
   41,   41,   41,   41,   59,  151,   41,   52,   41,  133,
   41,   39,   39,   39,   39,   39,   39,   39,   63,   88,
  117,  123,  131,   39,    0,    0,    0,    0,   39,   39,
   39,   39,   39,   39,    0,    0,   39,   39,   39,   39,
    0,    0,   39,    0,   39,    0,   39,   40,   40,   40,
   40,   40,   40,   40,    0,    0,    0,    0,    0,   40,
    0,    0,    0,    0,   40,   40,   40,   40,   40,   40,
    0,    0,   40,   40,   40,   40,    0,    0,   40,    0,
   40,    0,   40,   26,   26,   26,   26,   26,   26,   26,
   30,   31,   32,    5,    0,   26,    6,    7,    8,    9,
    5,    0,   33,    6,    7,   10,    9,    0,    0,   26,
    0,    0,   10,    0,   26,    0,    0,    5,   26,   11,
    6,    7,    8,    9,    0,    0,   11,    8,   12,   10,
    8,    8,    8,    8,    5,  129,    0,    6,    7,    8,
    9,    0,    0,   11,    5,    0,   10,    6,    7,    0,
    9,    6,  137,    8,    6,    6,   10,    6,    0,    0,
   11,    5,    8,    6,    6,    7,    0,    9,    5,  143,
   11,    6,    7,   10,    9,    0,    0,    6,    0,  146,
   10,    0,    0,    0,    0,    0,    6,   11,    0,    0,
   25,   25,   25,   25,   11,  107,    0,   23,   23,   23,
   23,    0,  140,   25,   25,    0,    0,    0,   25,   25,
   23,   23,    0,    0,    0,   23,   23,   22,   22,   22,
   22,  148,   30,   31,   32,    0,    0,    0,    0,    0,
   22,   22,    0,    0,   33,   22,   74,   75,   76,   77,
   78,    0,    0,   79,    0,   80,   81,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
  256,    4,   11,   24,   25,   26,   22,  273,    4,  256,
  270,  271,  269,   22,  270,  271,  272,   22,  269,  256,
  286,   42,   38,  270,  271,  272,  282,  269,  291,   38,
  287,  288,  269,  279,   39,  282,  282,  257,  258,  259,
  260,  261,  262,  263,  290,  287,  288,  288,   53,  269,
  257,  258,  259,  260,  261,  262,  263,  279,   67,  289,
  282,   82,  269,  283,  269,  270,  271,  272,  288,  290,
  287,  289,  292,  290,   95,  279,  283,  282,  282,  135,
   85,  288,   85,   88,  140,  292,  290,  287,  289,   85,
  290,  269,  257,  258,  259,  260,  261,  262,  263,  268,
  269,  270,  271,  272,  269,  287,  288,  289,  269,  270,
  271,  272,  289,  282,  119,  120,  119,  120,  283,  289,
  289,  282,  283,  288,  120,  269,  287,  292,  133,  279,
  133,  287,  282,  138,  139,  138,  139,  133,  147,  144,
  290,  144,  138,  257,  258,  259,  260,  261,  262,  263,
  280,  281,  258,  259,  286,  269,  270,  271,  272,  273,
  274,  275,  276,  277,  278,  279,  280,  281,  282,  283,
  284,  285,  286,  287,  288,  269,  290,  269,  292,  257,
  258,  259,  260,  261,  262,  263,   78,   79,   83,   84,
  267,  269,  264,  289,  262,  274,  274,  275,  276,  277,
  278,  279,  280,  281,  282,  283,  284,  285,  262,  270,
  288,  269,  290,  265,  292,  257,  258,  259,  260,  261,
  262,  263,  269,  269,  291,  290,  259,  269,  291,  270,
  290,  288,  274,  275,  276,  277,  278,  279,  287,  289,
  282,  283,  284,  285,  262,  290,  288,  274,  290,  124,
  292,  257,  258,  259,  260,  261,  262,  263,  290,   53,
   97,  111,  120,  269,   -1,   -1,   -1,   -1,  274,  275,
  276,  277,  278,  279,   -1,   -1,  282,  283,  284,  285,
   -1,   -1,  288,   -1,  290,   -1,  292,  257,  258,  259,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,
   -1,   -1,   -1,   -1,  274,  275,  276,  277,  278,  279,
   -1,   -1,  282,  283,  284,  285,   -1,   -1,  288,   -1,
  290,   -1,  292,  257,  258,  259,  260,  261,  262,  263,
  270,  271,  272,  257,   -1,  269,  260,  261,  262,  263,
  257,   -1,  282,  260,  261,  269,  263,   -1,   -1,  283,
   -1,   -1,  269,   -1,  288,   -1,   -1,  257,  292,  283,
  260,  261,  262,  263,   -1,   -1,  283,  257,  292,  269,
  260,  261,  262,  263,  257,  292,   -1,  260,  261,  269,
  263,   -1,   -1,  283,  257,   -1,  269,  260,  261,   -1,
  263,  257,  292,  283,  260,  261,  269,  263,   -1,   -1,
  283,  257,  292,  269,  260,  261,   -1,  263,  257,  292,
  283,  260,  261,  269,  263,   -1,   -1,  283,   -1,  292,
  269,   -1,   -1,   -1,   -1,   -1,  292,  283,   -1,   -1,
  269,  270,  271,  272,  283,  291,   -1,  269,  270,  271,
  272,   -1,  291,  282,  283,   -1,   -1,   -1,  287,  288,
  282,  283,   -1,   -1,   -1,  287,  288,  269,  270,  271,
  272,  269,  270,  271,  272,   -1,   -1,   -1,   -1,   -1,
  282,  283,   -1,   -1,  282,  287,  275,  276,  277,  278,
  279,   -1,   -1,  282,   -1,  284,  285,
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
"bloque_ejec : bloque_ejec sentencia_ejec PUNTOYCOMA",
"bloque_ejec : bloque_ejec sentencia_ejec",
"sentencia : sentencia_ejec PUNTOYCOMA",
"sentencia : sentencia_ejec",
"sentencia : INT ID decl_func",
"sentencia_ejec : asign_simple",
"sentencia_ejec : asign_multiple",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion",
"sentencia_ejec : return_sent",
"decl_func : PUNTOYCOMA",
"decl_func : COMA lista_ids PUNTOYCOMA",
"decl_func : PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
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
"return_sent : RETURN PARENTINIC expresion PARENTFIN",
"lista_params_formales : param_formal",
"lista_params_formales : lista_params_formales COMA param_formal",
"param_formal : sem_pasaje_opt INT ID",
"sem_pasaje_opt :",
"sem_pasaje_opt : CV",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if ENDIF",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF",
"condicion : expresion relop expresion",
"relop : MENOR",
"relop : MAYOR",
"relop : IGUAL",
"relop : DISTINTO",
"relop : MENORIGUAL",
"relop : MAYORIGUAL",
"rama_if : sentencia_ejec",
"rama_if : LLAVEINIC bloque_ejec LLAVEFIN",
"opt_else : ELSE rama_if",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for",
"rama_for : sentencia_ejec",
"rama_for : LLAVEINIC bloque_ejec LLAVEFIN",
"print_sent : PRINT PARENTINIC expresion PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN COMA PARENTINIC argumento PARENTFIN",
"argumento : ID",
"argumento : cte",
};

//#line 271 ".\gramatica.y"

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;
static TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
static int n_var = 0; //para contar variables en asignaciones multiples
static int n_cte = 0; //para contar ctes en asignaciones multiples

// writers de salida
static BufferedWriter wTokens = null;   // para los tokens
static BufferedWriter wTabla  = null;   // para la tabla de símbolos
static BufferedWriter wSint = null;     // para las estructuras sintacticas

// helpers de ruta
static Path rutaLexico(Path fuente, boolean acentos) {
    String base = quitarExt(fuente.getFileName().toString());
    String suf  = acentos ? "-léxico" : "-lexico";
    return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
            .resolve(base + suf + ".txt");
}
static Path rutaTabla(Path fuente, boolean acentos) {
    String base = quitarExt(fuente.getFileName().toString());
    String suf  = acentos ? "-tabla-de-símbolos" : "-tabla-simbolos";
    return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
            .resolve(base + suf + ".txt");
}
static Path rutaSintactico(Path fuente, boolean acentos) {
    String base = quitarExt(fuente.getFileName().toString());
    String suf  = acentos ? "-sintáctico" : "-sintactico";
    return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
            .resolve(base + suf + ".txt");
}
static String quitarExt(String s) {
    int i = s.lastIndexOf('.');
    return (i > 0) ? s.substring(0, i) : s;
}



//para el archivo de tokens
static void escribirHeaderTokens(BufferedWriter w) throws IOException {
    w.write(encabezado("TOKENS DETECTADOS")); w.newLine();
    w.write(String.format("%-6s | %-18s | %-28s | %-4s", "Linea", "Token", "Lexema", "ID")); w.newLine();
    w.write("------ | ------------------ | ---------------------------- | ----"); w.newLine();
}
static String filaToken(Token t, int linea) {
    String[] tl = tipoYLexema(t);
    String tipo   = humanizeTipo(tl[0]);                              
    String lexema = tl[1].replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
    lexema = trunc(lexema, 28);                                       // evita que desborde la columna
    return String.format("%6d | %-18s | %-28s | %4d", linea, tipo, lexema, t.getIDToken());
}
// Mapea los IDs a (tipo, lexema) usando las tablas
static String[] tipoYLexema(Token t) {
    TablaIdentificadorToken tid = TablaIdentificadorToken.getInstancia();
    TablaPalabraReservada  tpr  = TablaPalabraReservada.getInstancia();
    int id = t.getIDToken();

    if (id < 269) {                            // Palabra reservada
        String kw = tpr.getClave(id);          // ej: IF
        return new String[]{ kw, kw.toLowerCase() }; // tipo=IF, lexema="if"
    } else if (id == 269) {
        return new String[]{ "ID",           t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else if (id == 270) {
        return new String[]{ "CONST_INT",    t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else if (id == 271) {
        return new String[]{ "CONST_FLOAT",  t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else if (id == 272) {
        return new String[]{ "CONST_STRING", t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else {
        String raw = tid.getClave(id); // puede ser "(", "ASIGNACION", ";", etc.
        String lex = (t.getEntradaTS()!=null) ? t.getEntradaTS().getLexema() : raw;
        return new String[]{ raw, lex };
    }
}
// Cambia simbolos sueltos para que la columna Token se vea prolija
static String humanizeTipo(String raw) {
    return switch (raw) {
        case "if" -> "IF";
        case "else" -> "ELSE";
        case "endif" -> "ENDIF";
        case "print" -> "PRINT";
        case "return" -> "RETURN";
        case "int" -> "INT";
        case "for" -> "FOR";
        case "from" -> "FROM";
        case "to" -> "TO";
        case "lambda" -> "LAMBDA";
        case "cv" -> "CV";
        case "trunc" -> "TRUNC";
        case ":=" -> "ASIGN";
        case "->" -> "FLECHA";
        case "==" -> "IGUAL";
        case "=!" -> "DISTINTO";
        case "<=" -> "MENORIGUAL";
        case "=>" -> "MAYORIGUAL";
        case "+" -> "MAS";
        case "*" -> "MUL";
        case "/" -> "DIV";
        case "-" -> "MENOS";
        case "=" -> "IGUALUNICO";
        case ">" -> "MAYOR";
        case "<" -> "MENOR";
        case "." -> "PUNTO";
        case "(" -> "PARENTINIC";
        case ")" -> "PARENTFIN";
        case "{" -> "LLAVEINIC";
        case "}" -> "LLAVEFIN";
        case ";" -> "PUNTOYCOMA";
        case "," -> "COMA";
        default -> raw;
    };
}
// Recorta con puntos si el lexema es muy largo
static String trunc(String s, int max) {
    if (s == null) return "";
    if (s.length() <= max) return s;
    if (max <= 1) return s.substring(0, max);
    return s.substring(0, max-1) + "…";
}


public static void main (String [] args) {
    try {
        System.out.println("Iniciando compilacion...");
        TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();

        Path fuente = Paths.get(args[0]);

        // abrir archivos de salida
        wTokens = Files.newBufferedWriter(
                rutaLexico(fuente, false), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        escribirHeaderTokens(wTokens);

        // (opcional) abrimos el de tabla ahora o despues — aca lo abrimos ahora
        wTabla = Files.newBufferedWriter(
                rutaTabla(fuente, false), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

        // correr analisis
        lex = new AnalizadorLexico(args[0]);
        par = new Parser(false);
        par.run();

        // volcar TABLA DE SIMBOLOS a archivo
        wTabla.write(encabezado("TABLA DE SIMBOLOS"));
        wTabla.newLine();
        PrintWriter pw = new PrintWriter(wTabla, true);
        tablaSimbolos.mostrarTabla(pw); 

        System.out.println("Fin compilacion");
        
    } catch (IOException e) {
        throw new RuntimeException(e);
    } finally {
        try { if (wTokens != null) { wTokens.flush(); wTokens.close(); } } catch (IOException ignored) {}
        try { if (wTabla  != null) { wTabla.flush();  wTabla.close();  } } catch (IOException ignored) {}
    }
}

static String encabezado(String titulo) {
    String barra = "=".repeat(Math.max(24, titulo.length() + 8));
    return barra + "\n" + "=== " + titulo + " ===\n" + barra;
}


int yylex (){
    Token token = null;
    if ((token = lex.getToken()) != null) {
        yylval = new ParserVal(token.getEntradaTS());

        // --- escribir al archivo de tokens ---
        if (wTokens != null) {
            try {
				int lineaTok = lex.getLineaActual(); 
                wTokens.write(filaToken(token, lineaTok)); 
                wTokens.newLine();
            } catch (IOException e) {
                throw new RuntimeException("Error escribiendo token", e);
            }
        }
        return token.getIDToken();
    } else {
        return 0; // no hay más tokens
    }
}

void yyerror(String mensaje){
    if ("syntax error".equals(mensaje)) return;  // suprime el genérico
    System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
}
//#line 634 "Parser.java"
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
case 6:
//#line 71 ".\gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 8:
//#line 77 ".\gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 18:
//#line 91 ".\gramatica.y"
{n_var = 0;}
break;
case 22:
//#line 102 ".\gramatica.y"
{n_var++;}
break;
case 23:
//#line 103 ".\gramatica.y"
{n_var++;}
break;
case 24:
//#line 104 ".\gramatica.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 25:
//#line 105 ".\gramatica.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 27:
//#line 116 ".\gramatica.y"
{
					if (n_var < n_cte) {
						yyerror("Error: más constantes que variables en la asignación");
					} else {
						System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
					}
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 28:
//#line 124 ".\gramatica.y"
{ yyerror("Error: falta lista de variables antes del '='"); }
break;
case 29:
//#line 125 ".\gramatica.y"
{ yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 30:
//#line 126 ".\gramatica.y"
{ yyerror("Error: falta lista de constantes después del '='");}
break;
case 31:
//#line 129 ".\gramatica.y"
{n_cte++;}
break;
case 32:
//#line 130 ".\gramatica.y"
{n_cte++;}
break;
case 33:
//#line 131 ".\gramatica.y"
{ yyerror("Error: falta una constante después de coma");}
break;
case 35:
//#line 137 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 36:
//#line 144 ".\gramatica.y"
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
case 37:
//#line 158 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
//#line 884 "Parser.java"
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
