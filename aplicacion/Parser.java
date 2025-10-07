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



package aplicacion;



//#line 4 "gramatica.y"
	package aplicacion;
	import aplicacion.AnalizadorLexico;
	import aplicacion.Token;
	import datos.TablaSimbolos;
	import datos.TablaIdentificadorToken;
	import datos.TablaPalabraReservada;
	import java.io.*;
	import java.nio.charset.StandardCharsets;
	import java.nio.file.*;

//#line 28 "Parser.java"




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
    0,    0,    0,    0,    1,    1,    3,    3,    2,    2,
    2,    5,    5,    5,    5,    5,    4,    4,    4,    4,
    4,    4,    4,    4,    4,   15,   15,    6,    6,    6,
    6,    8,    9,   17,   17,   17,   18,   18,   18,   16,
   16,   16,   19,   19,   19,   20,   20,   20,   13,   13,
   21,   21,   21,   21,   21,   22,   22,   22,   23,   23,
   23,   23,   23,   14,   14,    7,    7,    7,    7,   25,
   25,   25,   25,   25,   26,   26,   10,   10,   10,   10,
   10,   27,   30,   30,   30,   30,   30,   30,   28,   28,
   29,   29,   11,   11,   11,   11,   11,   11,   11,   11,
   11,   31,   31,   31,   12,   12,   12,   24,   24,   24,
   24,   24,   24,   32,   32,   32,
};
final static short yylen[] = {                            2,
    4,    4,    4,    4,    0,    2,    0,    2,    1,    3,
    3,    1,    3,    6,    2,    4,    2,    2,    1,    1,
    1,    2,    1,    3,    3,    1,    3,    1,    3,    3,
    3,    3,    3,    1,    3,    3,    1,    1,    1,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    4,    4,
    1,    3,    3,    3,    3,    3,    2,    3,    1,    4,
    1,    3,    5,    5,    6,    1,    3,    3,    3,    3,
    4,    3,    4,    3,    0,    1,    8,    9,    3,    5,
    8,    3,    1,    1,    1,    1,    1,    1,    1,    3,
    0,    2,   10,   11,    3,    9,    4,    5,    6,    7,
    8,    1,    3,    3,    5,    6,    4,   10,    5,    6,
    8,    9,   11,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    5,    5,    5,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    2,    6,    9,    0,    0,
    0,   19,   20,   21,    0,   23,    0,    3,    4,    1,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   17,   18,    0,   22,    0,    0,   79,
   38,   37,   39,   47,    0,    0,   48,    0,   45,    0,
    0,    0,    0,   11,    0,    0,   12,    0,   10,   95,
    0,    0,    0,    0,    0,    0,    0,   51,    0,   61,
   26,    0,    0,   34,   30,    0,   24,   25,    0,   27,
   85,   86,   87,   88,    0,    0,   84,   83,    0,    0,
    0,    0,    0,  107,    0,    0,   15,    0,    0,   76,
    0,   66,    0,   97,    0,    0,   50,    0,    0,    0,
    0,    0,   49,   57,    0,    0,    0,    0,    0,   43,
   44,   80,    7,   89,    0,    0,  105,    0,   64,    0,
   13,    0,    0,    0,    0,    0,   98,    0,    0,   62,
    0,    0,   55,   54,   53,   52,   58,   56,   36,   35,
    0,    0,    0,  106,   65,   16,   69,   68,   67,    5,
   74,   72,    0,   70,   99,    0,    0,    0,   60,    0,
    0,   90,    8,   92,    0,    0,    0,   73,   71,  100,
    0,    0,   63,  109,    0,    7,   81,    0,   77,   14,
  101,    0,    0,  110,    0,   78,   96,    0,  102,    0,
    0,    0,    0,    0,    0,   93,  111,    0,    0,  104,
  103,   94,  112,    0,  114,  115,    0,  116,    0,  108,
  113,
};
final static short yydgoto[] = {                          3,
    7,   17,  161,   18,   69,   19,  111,   20,   21,   22,
   23,   24,   54,   26,   55,   76,   83,   57,   58,   59,
   77,   78,   79,   80,  112,  113,   60,  135,  163,   99,
  210,  227,
};
final static short yysindex[] = {                       -26,
 -274, -240,    0,    0,    0,    0, -221, -208, -242, -233,
 -251, -231,   15, -186, -153,    0,    0,    0,   -2, -226,
 -223,    0,    0,    0, -110,    0,    7,    0,    0,    0,
 -102,   59,   67,   59,  -95, -149,  -57,   42,  -80,  -11,
  105,   43,  -28,    0,    0,   -9,    0,   59,   32,    0,
    0,    0,    0,    0,    9,   84,    0, -129,    0, -211,
   22,   66,   68,    0,   34,  -11,    0,   53,    0,    0,
   38,  -69,   13, -173,   82,  -40, -243,    0, -126,    0,
    0,    9,   37,    0,    0,    9,    0,    0,  -40,    0,
    0,    0,    0,    0,   59,   59,    0,    0,   59,   59,
   59,   58, -142,    0, -103,  -85,    0, -156,    9,    0,
 -224,    0,  -21,    0,   69,  -19,    0,   75,   59,   86,
   46,   36,    0,    0,   63, -127, -129, -129,  -40,    0,
    0,    0,    0,    0,   95,   94,    0,   96,    0, -146,
    0,   62,   53,   81,   64,   65,    0,   97, -222,    0,
 -192, -210,    0,    0,    0,    0,    0,    0,    0,    0,
 -175, -142,  108,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   83,    0,    0,   98,  -18,   93,    0,   99,
 -234,    0,    0,    0,  100,  -56, -188,    0,    0,    0,
  102, -180,    0,    0,  101,    0,    0,  104,    0,    0,
    0,  106, -135,    0, -232,    0,    0,  131,    0,  -52,
  103, -121,  107, -168,  109,    0,    0,  110,   71,    0,
    0,    0,    0,  111,    0,    0, -178,    0,  112,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -62,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -115,    0,    0,  -29,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   21,    0,    0,
    0,    0,    0,    0,    0,  -77,    0,    0,    0,    0,
    0, -106,  116,    0,    0,  -54,    0,    0,  -48,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -82,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -12,   12, -143,    0,
    0,    0,    0,    0,  115,    0,    0,    0,    0,    0,
    0,    0,   21,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -155,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    2,    0,   61,  -94,    0,  329,    0,    0,    0,    0,
    0,    0,   -4,    0,   -7,  -22,    0,  -35,  179,  278,
    0,  259,    0,    0,  253,    0,    0,  236,    0,    0,
    0,    0,
};
final static int YYTABLESIZE=404;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         27,
   27,   27,   25,   25,   25,   84,    8,    9,  134,   56,
   62,   63,  121,   29,   10,    5,    4,   11,   12,   13,
   14,  195,   31,  211,   10,   89,   15,   11,   12,   43,
   14,  142,   82,  176,   86,   10,   15,   33,   11,   12,
   13,   14,  177,  122,  102,  180,  123,   15,   10,   30,
    6,   11,   12,   13,   14,   32,  196,   34,  109,  212,
   15,   44,  143,  178,   45,  144,  183,  134,   10,   37,
   16,   11,   12,   13,   14,  202,  129,  229,  103,  181,
   15,   10,  118,   28,   11,   12,   95,   14,   10,   96,
  160,   11,   12,   15,   14,   27,  151,  179,   25,  140,
   15,    7,   38,  200,    7,    7,   65,    7,  209,  203,
  183,  230,   82,    7,   10,  119,  182,   11,   12,  183,
   14,   10,   81,  221,   11,   12,   15,   14,  159,  124,
   42,  141,   82,   15,  218,   39,    7,   66,   67,   68,
   46,  166,   51,   52,   53,   46,   82,  125,  133,   31,
  100,  101,  136,   27,   27,  208,   25,   25,   46,   46,
   46,   46,   46,   46,   46,   46,   46,  219,   46,   46,
  138,  187,   46,   28,   46,   73,   31,   47,   59,   27,
   31,   31,   25,  226,  137,   50,  115,   74,   15,   51,
   52,   53,   64,   26,  116,   27,   59,   27,   25,  198,
   25,   29,  139,  215,   28,   28,   27,   32,   75,   25,
   26,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   26,   26,   26,   26,   42,   26,   29,    1,
   70,  199,   29,   29,  145,  216,  148,  191,   95,   32,
  146,   96,    2,   40,   42,   42,   42,   42,   42,   42,
  149,  192,   42,   40,   42,   42,  205,   81,   42,   87,
   42,   40,   40,   40,   40,   40,   40,   41,  214,   40,
   35,   40,   40,  127,  128,   40,   75,   40,   88,   48,
   41,   28,   75,   36,   42,   41,   41,   41,   41,   41,
   41,  155,   49,   41,   49,   41,   41,   71,   85,   41,
   90,   41,  117,   74,   15,   51,   52,   53,   28,  104,
   72,   81,   28,   74,   15,   51,   52,   53,  157,  110,
  173,  107,   61,  126,   75,  114,  224,   15,   51,   52,
   53,  158,  153,  174,   75,   15,   51,   52,   53,  225,
   51,   52,   53,  120,   95,  132,   95,   96,  167,   96,
  171,  168,  162,  172,  152,  105,  147,  106,   91,   92,
   93,   94,   95,  185,  150,   96,  186,   97,   98,  188,
   91,  170,  189,   91,   51,   52,   53,  130,  131,  154,
  156,  164,  193,  165,  175,  190,  213,  197,  194,  201,
  204,  206,  217,  207,  108,  169,  222,  184,  220,  223,
  228,  231,    0,   33,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          7,
    8,    9,    7,    8,    9,   41,    5,    6,  103,   32,
   33,   34,  256,  256,  257,  256,  291,  260,  261,  262,
  263,  256,  256,  256,  257,   48,  269,  260,  261,  256,
  263,  256,   40,  256,   42,  257,  269,  289,  260,  261,
  262,  263,  265,  287,  256,  256,  290,  269,  257,  292,
  291,  260,  261,  262,  263,  289,  291,  289,   66,  292,
  269,  288,  287,  256,  288,  290,  161,  162,  257,  256,
  292,  260,  261,  262,  263,  256,   99,  256,  290,  290,
  269,  257,  256,  292,  260,  261,  279,  263,  257,  282,
  126,  260,  261,  269,  263,  103,  119,  290,  103,  256,
  269,  257,  289,  292,  260,  261,  256,  263,  203,  290,
  205,  290,  256,  269,  257,  289,  292,  260,  261,  214,
  263,  257,  269,  292,  260,  261,  269,  263,  256,  256,
  287,  288,  140,  269,  256,  289,  292,  287,  288,  289,
  256,  288,  270,  271,  272,  256,  290,  274,  291,  256,
  280,  281,  256,  161,  162,  291,  161,  162,  274,  275,
  276,  277,  278,  279,  280,  281,  282,  289,  284,  285,
  256,  170,  288,  256,  290,  256,  283,  288,  256,  187,
  287,  288,  187,  219,  288,  288,  256,  268,  269,  270,
  271,  272,  288,  256,  264,  203,  274,  205,  203,  256,
  205,  256,  288,  256,  287,  288,  214,  256,  289,  214,
  273,  274,  275,  276,  277,  278,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  288,  256,  290,  283,  256,
  288,  288,  287,  288,  256,  288,  256,  256,  279,  288,
  262,  282,  269,  256,  274,  275,  276,  277,  278,  279,
  270,  270,  282,  256,  284,  285,  196,  269,  288,  288,
  290,  274,  275,  276,  277,  278,  279,  256,  208,  282,
  256,  284,  285,   95,   96,  288,  256,  290,  288,  273,
  283,  256,  262,  269,  287,  274,  275,  276,  277,  278,
  279,  256,  286,  282,  286,  284,  285,  256,  256,  288,
  269,  290,  290,  268,  269,  270,  271,  272,  283,  288,
  269,  269,  287,  268,  269,  270,  271,  272,  256,  267,
  256,  288,  256,  287,  289,  288,  256,  269,  270,  271,
  272,  269,  287,  269,  289,  269,  270,  271,  272,  269,
  270,  271,  272,  262,  279,  288,  279,  282,  287,  282,
  287,  290,  258,  290,  269,  290,  288,  290,  275,  276,
  277,  278,  279,  256,  290,  282,  259,  284,  285,  287,
  256,  291,  290,  259,  270,  271,  272,  100,  101,  121,
  122,  288,  290,  288,  288,  288,  256,  288,  290,  288,
  290,  288,  290,  288,   66,  143,  288,  162,  292,  290,
  290,  290,   -1,  288,
};
}
final static short YYFINAL=3;
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
"prog : error LLAVEINIC bloque LLAVEFIN",
"prog : ID error bloque LLAVEFIN",
"prog : ID LLAVEINIC bloque error",
"bloque :",
"bloque : bloque sentencia",
"bloque_ejec :",
"bloque_ejec : bloque_ejec sentencia_ejec",
"sentencia : sentencia_ejec",
"sentencia : INT ID decl_func",
"sentencia : INT error PUNTOYCOMA",
"decl_func : PUNTOYCOMA",
"decl_func : COMA lista_ids PUNTOYCOMA",
"decl_func : PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
"decl_func : error PUNTOYCOMA",
"decl_func : COMA lista_ids error PUNTOYCOMA",
"sentencia_ejec : asign_simple PUNTOYCOMA",
"sentencia_ejec : asign_multiple PUNTOYCOMA",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion PUNTOYCOMA",
"sentencia_ejec : return_sent",
"sentencia_ejec : asign_simple error PUNTOYCOMA",
"sentencia_ejec : llamada_funcion error PUNTOYCOMA",
"var_ref : ID",
"var_ref : var_ref PUNTO ID",
"lista_ids : var_ref",
"lista_ids : lista_ids COMA var_ref",
"lista_ids : lista_ids COMA error",
"lista_ids : lista_ids error var_ref",
"asign_simple : var_ref ASIGN expresion",
"asign_multiple : lista_ids IGUALUNICO lista_ctes",
"lista_ctes : cte",
"lista_ctes : lista_ctes COMA cte",
"lista_ctes : lista_ctes COMA error",
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
"llamada_funcion : ID PARENTINIC error PARENTFIN",
"lista_params_reales : param_real_map",
"lista_params_reales : lista_params_reales COMA param_real_map",
"lista_params_reales : lista_params_reales COMA error",
"lista_params_reales : lista_params_reales error param_real_map",
"lista_params_reales : lista_params_reales error COMA",
"param_real_map : parametro_real FLECHA ID",
"param_real_map : parametro_real error",
"param_real_map : parametro_real FLECHA error",
"parametro_real : expresion",
"parametro_real : TRUNC PARENTINIC expresion PARENTFIN",
"parametro_real : lambda_expr",
"parametro_real : TRUNC error PARENTFIN",
"parametro_real : TRUNC PARENTINIC expresion error PARENTFIN",
"return_sent : RETURN PARENTINIC expresion PARENTFIN PUNTOYCOMA",
"return_sent : RETURN PARENTINIC expresion PARENTFIN error PUNTOYCOMA",
"lista_params_formales : param_formal",
"lista_params_formales : lista_params_formales COMA param_formal",
"lista_params_formales : lista_params_formales error PARENTFIN",
"lista_params_formales : lista_params_formales error COMA",
"param_formal : sem_pasaje_opt INT ID",
"param_formal : sem_pasaje_opt INT error PARENTFIN",
"param_formal : sem_pasaje_opt error PARENTFIN",
"param_formal : sem_pasaje_opt INT error COMA",
"param_formal : sem_pasaje_opt error COMA",
"sem_pasaje_opt :",
"sem_pasaje_opt : CV",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF PUNTOYCOMA",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF error PUNTOYCOMA",
"bloque_if : IF error PUNTOYCOMA",
"bloque_if : IF PARENTINIC condicion error PUNTOYCOMA",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else error PUNTOYCOMA",
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
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for error PUNTOYCOMA",
"bloque_for : FOR error PUNTOYCOMA",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT error PUNTOYCOMA",
"bloque_for : FOR PARENTINIC error PUNTOYCOMA",
"bloque_for : FOR PARENTINIC ID error PUNTOYCOMA",
"bloque_for : FOR PARENTINIC ID FROM error PUNTOYCOMA",
"bloque_for : FOR PARENTINIC ID FROM CTEINT error PUNTOYCOMA",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO error PUNTOYCOMA",
"rama_for : sentencia_ejec",
"rama_for : LLAVEINIC bloque_ejec LLAVEFIN",
"rama_for : LLAVEINIC error LLAVEFIN",
"print_sent : PRINT PARENTINIC expresion PARENTFIN PUNTOYCOMA",
"print_sent : PRINT PARENTINIC expresion PARENTFIN error PUNTOYCOMA",
"print_sent : PRINT PARENTINIC error PUNTOYCOMA",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC INT ID error PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN error PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec error PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN error PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN PARENTINIC argumento error PARENTFIN",
"argumento : ID",
"argumento : cte",
"argumento : error PARENTFIN",
};

//#line 273 "gramatica.y"

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;

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
    System.err.println("Error sintáctico en línea " + lex.getLineaActual() + ": " + mensaje);
}
//#line 683 "Parser.java"
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
case 1:
//#line 58 "gramatica.y"
{System.out.println("")}
break;
case 2:
//#line 59 "gramatica.y"
{ yyerror("Ocurrio algo inesperado al inicio del programa. Sugerencia: Falta el nombre del programa."); }
break;
case 3:
//#line 60 "gramatica.y"
{ yyerror("Ocurrio algo inesperado al inicio del programa. Sugerencia: Falta '{' "); }
break;
case 4:
//#line 61 "gramatica.y"
{ yyerror("Ocurrio algo inesperado al final del programa. Sugerencia: Falta '}'"); }
break;
case 11:
//#line 79 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el nombre de la funcion o variable"); }
break;
case 15:
//#line 85 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 16:
//#line 86 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 24:
//#line 96 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 25:
//#line 97 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 30:
//#line 108 "gramatica.y"
{ yyerror("Ocurrio algo inesperado se esperaba un ID despues de la ',' "); }
break;
case 31:
//#line 109 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ',' "); }
break;
case 36:
//#line 125 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Se esperaba un CTE despues de la ',' "); }
break;
case 50:
//#line 157 "gramatica.y"
{ yyerror("Error en lista de parámetros reales"); }
break;
case 53:
//#line 162 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Se esperaba un parametro despues de la ',' "); }
break;
case 54:
//#line 163 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ',' "); }
break;
case 55:
//#line 164 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ',' "); }
break;
case 57:
//#line 169 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta '->' "); }
break;
case 58:
//#line 170 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta 'ID' "); }
break;
case 62:
//#line 176 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta trunc '(' "); }
break;
case 63:
//#line 177 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ')' del trunc "); }
break;
case 65:
//#line 182 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 68:
//#line 189 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ',' "); }
break;
case 69:
//#line 190 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ',' "); }
break;
case 71:
//#line 194 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el nombre del parámetro"); }
break;
case 72:
//#line 195 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el tipo del parámetro"); }
break;
case 73:
//#line 196 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el nombre del parámetro"); }
break;
case 74:
//#line 197 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el tipo del parámetro"); }
break;
case 78:
//#line 207 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 79:
//#line 208 "gramatica.y"
{ yyerror ("Ocurrio algo inesperado. Sugerencia: Falta if '(' "); }
break;
case 80:
//#line 209 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ')' del if "); }
break;
case 81:
//#line 210 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta 'endif' "); }
break;
case 94:
//#line 235 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 95:
//#line 236 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta for '(' "); }
break;
case 96:
//#line 237 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ')' del for "); }
break;
case 97:
//#line 238 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el control del for (el mitico 'i') "); }
break;
case 98:
//#line 239 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta 'from' "); }
break;
case 99:
//#line 240 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el valor inicial del for "); }
break;
case 100:
//#line 241 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta 'to' "); }
break;
case 101:
//#line 242 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el valor final del for "); }
break;
case 104:
//#line 247 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta el cuerpo del for "); }
break;
case 106:
//#line 253 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ';' "); }
break;
case 107:
//#line 254 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta la expresión a imprimir "); }
break;
case 109:
//#line 260 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ')' "); }
break;
case 110:
//#line 261 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta '{' "); }
break;
case 111:
//#line 262 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta '}' "); }
break;
case 112:
//#line 263 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta '(' "); }
break;
case 113:
//#line 264 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta ')' "); }
break;
case 116:
//#line 269 "gramatica.y"
{ yyerror("Ocurrio algo inesperado. Sugerencia: Falta un argumento "); }
break;
//#line 1024 "Parser.java"
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
