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
    0,    1,    1,    3,    3,    3,    2,    2,    2,    2,
    4,    4,    4,    4,    4,    4,    4,    5,    5,    5,
   15,   15,   13,   13,   13,   13,    6,    7,    7,    7,
    7,   17,   17,   17,   18,   18,   18,   18,   18,   16,
   16,   16,   16,   16,   19,   19,   19,   19,   19,   20,
   20,   20,   11,   11,   21,   21,   22,   22,   23,   23,
   23,   23,   23,   23,   12,   14,   14,   25,   25,   25,
   26,   26,    8,    8,    8,    8,    8,    8,   27,   27,
   27,   27,   27,   30,   30,   30,   30,   30,   30,   28,
   28,   29,   29,    9,    9,    9,    9,    9,    9,    9,
    9,    9,   31,   31,   10,   10,   10,   10,   24,   24,
   24,   24,   32,   32,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    3,    2,    2,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    6,
    1,    3,    1,    3,    3,    2,    3,    3,    2,    2,
    3,    1,    3,    3,    1,    2,    1,    2,    1,    3,
    3,    3,    3,    1,    3,    3,    3,    3,    1,    1,
    1,    1,    4,    4,    1,    3,    3,    3,    1,    4,
    4,    4,    4,    1,    4,    1,    3,    3,    3,    3,
    0,    1,    6,    6,    7,    7,    7,    4,    3,    3,
    2,    2,    0,    1,    1,    1,    1,    1,    1,    1,
    3,    2,    2,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    2,    3,    4,    4,    4,    4,   10,   10,
   10,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    3,    0,   11,   12,   13,   14,   15,
   16,   17,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   37,   35,   39,    0,    0,
   32,    7,   21,    0,    0,    0,    0,    0,    0,    0,
    0,   51,    0,    0,   52,    0,   49,    0,   55,    0,
   64,    4,   90,    0,   86,   87,   88,   89,   85,   84,
    0,    0,    0,    0,    0,    0,    0,    0,   18,    0,
   10,    9,    0,    0,    0,    0,   38,   36,    0,   31,
    0,   25,    0,    0,   22,    0,    0,    0,    0,    0,
    0,    0,    0,   54,    0,    0,   78,    0,    0,    0,
    0,  107,  106,  108,  105,   65,    0,    0,   72,    0,
   66,    0,    0,    0,    0,    0,   53,   34,   33,    0,
    0,    0,    0,    0,    0,    0,    0,   45,    0,   47,
   56,   58,   57,   91,    0,    0,    0,    0,    0,   19,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   63,
   62,   61,   60,    0,    5,    0,    0,   74,   73,    0,
   67,    2,   70,   69,   68,    0,    0,    0,    0,    0,
    0,    4,    4,    0,   92,   77,   76,   75,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   20,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    4,    0,   95,   96,   97,   98,   99,  100,  101,
    0,   94,    0,    0,    0,    0,    0,  103,    0,    0,
    0,  113,  114,    0,    0,    0,  104,  112,  110,  111,
  109,
};
final static short yydgoto[] = {                          2,
    4,   63,  106,  213,   81,   16,   17,   18,   19,   20,
   52,   22,   23,  120,   53,   54,   40,   55,   56,   57,
   58,   59,   60,   61,  121,  122,   72,   64,  167,   73,
  214,  231,
};
final static short yysindex[] = {                      -258,
 -276,    0,    0,  379, -223, -232, -217, -206, -131, -209,
 -174,  313,    0,    0, -226,    0,    0,    0,    0,    0,
    0,    0, -171,  -97, -128,  456,  515, -151,  318, -151,
  -40,  -40, -160,  -81, -128,    0,    0,    0,   -5, -163,
    0,    0,    0,  355,  -78, -147, -163, -151, -116, -203,
  -82,    0, -147, -150,    0,   19,    0, -130,    0, -115,
    0,    0,    0,  -50,    0,    0,    0,    0,    0,    0,
  532,  -49, -151,  137,   12, -231,  174,  -26,    0,  -15,
    0,    0,   23,   25,  -52, -103,    0,    0,  580,    0,
 -163,    0, -147, -150,    0, -151, -151,   34,  549,  556,
  573,  577, -128,    0,  -74, -243,    0, -151, -151,  466,
 -150,    0,    0,    0,    0,    0, -180, -147,    0,  -80,
    0, -252,   51,   52,   54, -201,    0,    0,    0, -212,
 -211,   18, -223,   19, -223,   19, -223,    0, -223,    0,
    0,    0,    0,    0,   50, -150, -150, -235, -123,    0,
  -15,   16,   57,  -64,   74,   75,   77,   78, -192,    0,
    0,    0,    0, -253,    0,  481,   85,    0,    0,  -48,
    0,    0,    0,    0,    0,   88,   89,   91,   93,  103,
 -193,    0,    0, -223,    0,    0,    0,    0,  389,   55,
   86,   87,   99,  105,  106, -244,  400,  417,    0,  498,
  498,  498,  498,  498,  498,  498,  513,  111,  121,  122,
  123,    0,  110,    0,    0,    0,    0,    0,    0,    0,
 -223,    0,  -36,  -32,  -36,  -32,  428,    0, -174,    0,
  124,    0,    0,  127,  128,  136,    0,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -2,    0,    0,    0,  361,    0,    0,    0,    0,    0,
    0,    0,    0,  621,    0,    0,  142,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -227,
    0,    0,    0,    0,    0,  594, -166,    0,    0,    0,
    0,    0, -111,  101,    0,  109,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -152,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  294,    0,  601,  309,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  143,    0,
  145,    0,    0,    0,    0,    0,    0, -132,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  146,  183,  220,  257,   35,    0,   72,    0,
    0,    0,    0,    0,  438,  147,  157,    0,    0,    0,
 -152,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -14,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  346,    0,    0,    0,    0,    0,    0,    0,  620,  100,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
  241,   -3,  -98,   17,  416,    0,    0,    0,    0,    0,
   24,    0,  371,    0,   -4,  -21,   37,   -7,  206,  235,
  415,  348,    0,    0,  303,    0,    0, -108,  306,  392,
  710,   38,
};
final static int YYTABLESIZE=917;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         24,
   14,  149,  182,  153,   41,   71,   74,   76,   77,  154,
    1,  206,    5,    6,    3,   41,    7,    8,   46,   10,
   15,   24,  166,   26,  114,   11,   94,   21,   29,   29,
   29,   29,   29,   29,   29,   29,   41,  183,   28,   12,
   93,   29,   15,  160,  162,  207,   33,   99,  144,   21,
  100,  111,   96,   25,  158,   29,   27,  185,  115,   47,
   29,   42,  195,  180,   29,   25,   99,   99,  159,  100,
  100,   29,  181,  118,  130,  131,  196,  161,  163,   34,
   91,  129,   30,  197,  198,   97,  146,  147,   43,   30,
   30,   30,   30,   30,   30,   30,   30,   43,   36,   37,
   38,   24,   30,   71,    5,   24,   45,  150,   83,   71,
   39,   44,   46,  227,   35,   45,   30,   11,   36,   37,
   38,   30,  145,   89,   31,   30,   15,    5,   99,   21,
   39,  100,  168,   21,  166,  169,   23,   32,   49,   50,
   11,   36,   37,   38,   50,   50,   50,   50,   50,   50,
   50,   50,   95,   39,   23,   23,  103,   50,  105,  104,
   51,   24,   50,   50,   50,   50,   50,   50,   50,   50,
   50,   50,   50,   50,   84,   48,   50,   92,   50,   98,
   50,  142,   15,  103,   24,   14,  127,   85,   49,   21,
   43,  174,   24,   24,  143,   24,   24,   24,   24,   24,
   24,   24,   24,  125,  175,   15,  151,  187,  107,  152,
  188,  126,   21,  145,  145,  230,  233,  230,  233,    5,
   21,   21,   24,   21,   21,   21,   21,   21,   21,   21,
   21,   50,  229,   36,   37,   38,  232,   36,   37,   38,
  110,   93,   43,  145,   93,   39,   78,   79,   80,   39,
   21,  119,   51,   21,   21,   21,   21,   21,   21,   21,
   21,  234,  235,  236,   87,   88,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,  123,   21,  124,   21,
   46,   46,   46,   46,   46,   46,   46,   46,  101,  102,
   25,  113,  132,   46,  134,  136,  172,  164,   46,   46,
   46,   46,   46,   46,   46,   46,   46,   46,   46,   46,
  155,  156,   46,  157,   46,  173,   46,   48,   48,   48,
   48,   48,   48,   48,   48,  138,  140,  165,  176,  177,
   48,  178,  179,  186,  200,   48,   48,   48,   48,   48,
   48,   48,   48,   48,   48,   48,   48,  190,  191,   48,
  192,   48,  193,   48,   44,   44,   44,   44,   44,   44,
   44,   44,  194,   52,   59,  201,  202,   44,   52,   52,
   52,   52,   44,   44,   44,   44,   44,   44,  203,  114,
   44,   44,   44,   44,  204,  205,   44,  228,   44,  223,
   44,   41,   41,   41,   41,   41,   41,   41,   41,  224,
  225,  226,  189,  238,   41,   99,  239,  240,  100,   41,
   41,   41,   41,   41,   41,  241,  112,   41,   41,   41,
   41,   83,   82,   41,   81,   41,   80,   41,   40,   40,
   40,   40,   40,   40,   40,   40,   79,   82,  117,   86,
  141,   40,   99,  171,  170,  100,   40,   40,   40,   40,
   40,   40,  109,  116,   40,   40,   40,   40,    0,    0,
   40,    0,   40,    0,   40,   43,   43,   43,   43,   43,
   43,   43,   43,    0,    0,    0,    0,    0,   43,    0,
    0,    0,    0,   43,   43,   43,   43,   43,   43,    0,
    0,   43,   43,   43,   43,    0,    0,   43,    0,   43,
    0,   43,   42,   42,   42,   42,   42,   42,   42,   42,
    0,    0,    0,    0,    0,   42,    0,    0,    0,    0,
   42,   42,   42,   42,   42,   42,    0,    0,   42,   42,
   42,   42,    0,    0,   42,    0,   42,    0,   42,   28,
   28,   28,   28,   28,   28,   28,   28,    0,    0,    0,
    0,    0,   28,    0,   27,   27,   27,   27,   27,   27,
   27,   27,    0,   75,    0,    0,   28,   27,    0,    0,
    0,   28,   36,   37,   38,   28,   11,   36,   37,   38,
    0,   27,    0,    0,   39,    0,   27,    0,    0,   39,
   27,  102,  102,  102,  102,  102,  102,  102,  102,    0,
   90,    0,    0,    0,  102,    0,    8,    8,    8,    8,
    8,    8,    8,    8,   36,   37,   38,    0,  102,    8,
    0,    0,    0,  102,    5,    6,   39,  102,    7,    8,
    9,   10,    0,    8,    5,    6,    0,   11,    7,    8,
    9,   10,    8,    0,    0,  208,    6,   11,    0,    7,
    8,   12,   10,    0,    0,    0,    0,    0,   11,    0,
   13,   12,  210,    6,    0,    0,    7,    8,    0,   10,
  199,    0,   12,    5,    6,   11,    0,    7,    8,    0,
   10,  209,    0,    6,    6,    0,   11,    6,    6,   12,
    6,    0,    0,    0,    0,    0,    6,    0,  211,    0,
   12,    5,    6,    0,    0,    7,    8,    9,   10,  237,
    6,  148,    6,    0,   11,    7,    8,    9,   10,    6,
    0,    0,    0,    0,   11,    0,  184,    6,   12,    0,
    7,    8,    9,   10,    0,    0,   62,    0,   12,   11,
    0,    0,    0,    5,    6,    0,   62,    7,    8,    0,
   10,    0,    0,   12,    0,    0,   11,    0,  221,    6,
    5,   62,    7,    8,    0,   10,    0,    0,    0,    0,
   12,   11,    0,   11,   36,   37,   38,  108,  212,   65,
   66,   67,   68,    0,    0,   12,   39,    0,   69,   70,
    0,    0,    0,  212,  133,    0,   65,   66,   67,   68,
   99,  135,    0,  100,    0,   69,   70,   11,   36,   37,
   38,    0,    0,    0,   11,   36,   37,   38,  137,    0,
   39,    0,  139,    0,    0,  128,    0,   39,    0,    0,
    0,   11,   36,   37,   38,   11,   36,   37,   38,   36,
   37,   38,    0,    0,   39,    0,    0,    0,   39,    0,
    0,   39,   26,   26,   26,   26,    0,    0,    0,   24,
   24,   24,   24,    0,    0,   26,   26,    0,    0,    0,
   26,   26,   24,   24,    0,    0,    0,   24,   24,   23,
   23,   23,   23,   21,    0,    0,    0,    0,   21,   21,
   21,   21,   23,   23,    0,   21,    0,   23,    0,  113,
  215,  216,  217,  218,  219,  220,  222,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    4,  110,  256,  256,   12,   27,   28,   29,   30,  262,
  269,  256,  256,  257,  291,   23,  260,  261,   23,  263,
    4,   26,  258,  256,  256,  269,   48,    4,  256,  257,
  258,  259,  260,  261,  262,  263,   44,  291,  256,  283,
   45,  269,   26,  256,  256,  290,  256,  279,  292,   26,
  282,   73,  256,  289,  256,  283,  289,  166,  290,   23,
  288,  288,  256,  256,  292,  289,  279,  279,  270,  282,
  282,  289,  265,   78,   96,   97,  270,  290,  290,  289,
   44,   89,  289,  182,  183,  289,  108,  109,  269,  256,
  257,  258,  259,  260,  261,  262,  263,  269,  270,  271,
  272,  106,  269,  256,  256,  110,  287,  288,  269,  262,
  282,  283,  117,  212,  289,  287,  283,  269,  270,  271,
  272,  288,  106,  287,  256,  292,  110,  256,  279,  106,
  282,  282,  256,  110,  258,  259,  269,  269,  286,  268,
  269,  270,  271,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  269,  282,  287,  288,  287,  269,  274,  290,
  289,  166,  274,  275,  276,  277,  278,  279,  280,  281,
  282,  283,  284,  285,  256,  273,  288,  256,  290,  262,
  292,  256,  166,  287,  189,  189,  290,  269,  286,  166,
  269,  256,  197,  198,  269,  200,  201,  202,  203,  204,
  205,  206,  207,  256,  269,  189,  287,  256,  259,  290,
  259,  264,  189,  197,  198,  223,  224,  225,  226,  256,
  197,  198,  227,  200,  201,  202,  203,  204,  205,  206,
  207,  268,  269,  270,  271,  272,  269,  270,  271,  272,
  290,  256,  269,  227,  259,  282,  287,  288,  289,  282,
  227,  267,  289,  256,  257,  258,  259,  260,  261,  262,
  263,  224,  225,  226,  270,  271,  269,  270,  271,  272,
  273,  274,  275,  276,  277,  278,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  288,  264,  290,  264,  292,
  256,  257,  258,  259,  260,  261,  262,  263,  280,  281,
  289,  290,  269,  269,   99,  100,  291,  290,  274,  275,
  276,  277,  278,  279,  280,  281,  282,  283,  284,  285,
  270,  270,  288,  270,  290,  269,  292,  256,  257,  258,
  259,  260,  261,  262,  263,  101,  102,  288,  265,  265,
  269,  265,  265,  259,  290,  274,  275,  276,  277,  278,
  279,  280,  281,  282,  283,  284,  285,  270,  270,  288,
  270,  290,  270,  292,  256,  257,  258,  259,  260,  261,
  262,  263,  270,  274,  274,  290,  290,  269,  279,  280,
  281,  282,  274,  275,  276,  277,  278,  279,  290,  290,
  282,  283,  284,  285,  290,  290,  288,  288,  290,  289,
  292,  256,  257,  258,  259,  260,  261,  262,  263,  289,
  289,  289,  172,  290,  269,  279,  290,  290,  282,  274,
  275,  276,  277,  278,  279,  290,  290,  282,  283,  284,
  285,  290,  290,  288,  290,  290,  290,  292,  256,  257,
  258,  259,  260,  261,  262,  263,  290,   32,   78,   35,
  103,  269,  279,  151,  149,  282,  274,  275,  276,  277,
  278,  279,   71,  290,  282,  283,  284,  285,   -1,   -1,
  288,   -1,  290,   -1,  292,  256,  257,  258,  259,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,
   -1,   -1,   -1,  274,  275,  276,  277,  278,  279,   -1,
   -1,  282,  283,  284,  285,   -1,   -1,  288,   -1,  290,
   -1,  292,  256,  257,  258,  259,  260,  261,  262,  263,
   -1,   -1,   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,
  274,  275,  276,  277,  278,  279,   -1,   -1,  282,  283,
  284,  285,   -1,   -1,  288,   -1,  290,   -1,  292,  256,
  257,  258,  259,  260,  261,  262,  263,   -1,   -1,   -1,
   -1,   -1,  269,   -1,  256,  257,  258,  259,  260,  261,
  262,  263,   -1,  256,   -1,   -1,  283,  269,   -1,   -1,
   -1,  288,  270,  271,  272,  292,  269,  270,  271,  272,
   -1,  283,   -1,   -1,  282,   -1,  288,   -1,   -1,  282,
  292,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
  256,   -1,   -1,   -1,  269,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  270,  271,  272,   -1,  283,  269,
   -1,   -1,   -1,  288,  256,  257,  282,  292,  260,  261,
  262,  263,   -1,  283,  256,  257,   -1,  269,  260,  261,
  262,  263,  292,   -1,   -1,  256,  257,  269,   -1,  260,
  261,  283,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,
  292,  283,  256,  257,   -1,   -1,  260,  261,   -1,  263,
  292,   -1,  283,  256,  257,  269,   -1,  260,  261,   -1,
  263,  292,   -1,  256,  257,   -1,  269,  260,  261,  283,
  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,  292,   -1,
  283,  256,  257,   -1,   -1,  260,  261,  262,  263,  292,
  283,  256,  257,   -1,  269,  260,  261,  262,  263,  292,
   -1,   -1,   -1,   -1,  269,   -1,  256,  257,  283,   -1,
  260,  261,  262,  263,   -1,   -1,  291,   -1,  283,  269,
   -1,   -1,   -1,  256,  257,   -1,  291,  260,  261,   -1,
  263,   -1,   -1,  283,   -1,   -1,  269,   -1,  256,  257,
  256,  291,  260,  261,   -1,  263,   -1,   -1,   -1,   -1,
  283,  269,   -1,  269,  270,  271,  272,  256,  291,  275,
  276,  277,  278,   -1,   -1,  283,  282,   -1,  284,  285,
   -1,   -1,   -1,  291,  256,   -1,  275,  276,  277,  278,
  279,  256,   -1,  282,   -1,  284,  285,  269,  270,  271,
  272,   -1,   -1,   -1,  269,  270,  271,  272,  256,   -1,
  282,   -1,  256,   -1,   -1,  256,   -1,  282,   -1,   -1,
   -1,  269,  270,  271,  272,  269,  270,  271,  272,  270,
  271,  272,   -1,   -1,  282,   -1,   -1,   -1,  282,   -1,
   -1,  282,  269,  270,  271,  272,   -1,   -1,   -1,  269,
  270,  271,  272,   -1,   -1,  282,  283,   -1,   -1,   -1,
  287,  288,  282,  283,   -1,   -1,   -1,  287,  288,  269,
  270,  271,  272,  274,   -1,   -1,   -1,   -1,  279,  280,
  281,  282,  282,  283,   -1,  286,   -1,  287,   -1,  290,
  201,  202,  203,  204,  205,  206,  207,
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
"sentencia : INT error decl_func",
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
"expresion : expresion MAS error",
"expresion : expresion MENOS termino",
"expresion : expresion MENOS error",
"expresion : termino",
"termino : termino MUL factor",
"termino : termino MUL error",
"termino : termino DIV factor",
"termino : termino DIV error",
"termino : factor",
"factor : var_ref",
"factor : llamada_funcion",
"factor : cte",
"llamada_funcion : ID PARENTINIC lista_params_reales PARENTFIN",
"llamada_funcion : error PARENTINIC lista_params_reales PARENTFIN",
"lista_params_reales : param_real_map",
"lista_params_reales : lista_params_reales COMA param_real_map",
"param_real_map : parametro_real FLECHA ID",
"param_real_map : parametro_real FLECHA error",
"parametro_real : expresion",
"parametro_real : TRUNC PARENTINIC expresion PARENTFIN",
"parametro_real : TRUNC PARENTINIC expresion error",
"parametro_real : TRUNC error expresion PARENTFIN",
"parametro_real : TRUNC error expresion error",
"parametro_real : lambda_expr",
"return_sent : RETURN PARENTINIC expresion PARENTFIN",
"lista_params_formales : param_formal",
"lista_params_formales : lista_params_formales COMA param_formal",
"param_formal : sem_pasaje_opt INT ID",
"param_formal : sem_pasaje_opt INT error",
"param_formal : sem_pasaje_opt error ID",
"sem_pasaje_opt :",
"sem_pasaje_opt : CV",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if ENDIF",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if error",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else error",
"bloque_if : IF PARENTINIC condicion PARENTFIN error opt_else ENDIF",
"bloque_if : IF error rama_if ENDIF",
"condicion : expresion relop expresion",
"condicion : expresion error expresion",
"condicion : relop expresion",
"condicion : expresion relop",
"condicion :",
"relop : MENOR",
"relop : MAYOR",
"relop : IGUAL",
"relop : DISTINTO",
"relop : MENORIGUAL",
"relop : MAYORIGUAL",
"rama_if : sentencia",
"rama_if : LLAVEINIC bloque_ejec LLAVEFIN",
"opt_else : ELSE rama_if",
"opt_else : ELSE error",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR error ID FROM CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC error FROM CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID error CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM error TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT error CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO error PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT error rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN error",
"rama_for : sentencia_ejec PUNTOYCOMA",
"rama_for : LLAVEINIC bloque_ejec LLAVEFIN",
"print_sent : PRINT PARENTINIC expresion PARENTFIN",
"print_sent : PRINT PARENTINIC error PARENTFIN",
"print_sent : PRINT error expresion PARENTFIN",
"print_sent : PRINT PARENTINIC expresion error",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN error bloque_ejec LLAVEFIN PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec error PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC INT ID PARENTFIN error bloque_ejec error PARENTINIC argumento PARENTFIN",
"argumento : ID",
"argumento : cte",
};

//#line 309 ".\gramatica.y"

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
//#line 790 "Parser.java"
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
case 10:
//#line 79 ".\gramatica.y"
{yyerror("Falta identificador despues de 'int'");}
break;
case 19:
//#line 92 ".\gramatica.y"
{n_var = 0;}
break;
case 23:
//#line 103 ".\gramatica.y"
{n_var++;}
break;
case 24:
//#line 104 ".\gramatica.y"
{n_var++;}
break;
case 25:
//#line 105 ".\gramatica.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 26:
//#line 106 ".\gramatica.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 28:
//#line 117 ".\gramatica.y"
{
					if (n_var < n_cte) {
						yyerror("Error: más constantes que variables en la asignación");
					} else {
						System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
					}
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 29:
//#line 125 ".\gramatica.y"
{ yyerror("Error: falta lista de variables antes del '='"); }
break;
case 30:
//#line 126 ".\gramatica.y"
{ yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 31:
//#line 127 ".\gramatica.y"
{ yyerror("Error: falta lista de constantes después del '='");}
break;
case 32:
//#line 130 ".\gramatica.y"
{n_cte++;}
break;
case 33:
//#line 131 ".\gramatica.y"
{n_cte++;}
break;
case 34:
//#line 132 ".\gramatica.y"
{ yyerror("Error: falta una constante después de coma");}
break;
case 36:
//#line 138 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 37:
//#line 145 ".\gramatica.y"
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
case 38:
//#line 159 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 41:
//#line 173 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 43:
//#line 175 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 46:
//#line 180 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 48:
//#line 182 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 54:
//#line 196 ".\gramatica.y"
{ yyerror("Llamada a función sin nombre");}
break;
case 58:
//#line 205 ".\gramatica.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 61:
//#line 210 ".\gramatica.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 62:
//#line 211 ".\gramatica.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 63:
//#line 212 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 69:
//#line 227 ".\gramatica.y"
{ yyerror("Falta identificador después de 'int' en parámetro formal");}
break;
case 70:
//#line 228 ".\gramatica.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 74:
//#line 240 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 76:
//#line 242 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 77:
//#line 243 ".\gramatica.y"
{ yyerror("Falta bloque del then."); }
break;
case 78:
//#line 244 ".\gramatica.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 80:
//#line 249 ".\gramatica.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 81:
//#line 250 ".\gramatica.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 82:
//#line 251 ".\gramatica.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 83:
//#line 252 ".\gramatica.y"
{ yyerror("Falta condicion en el if."); }
break;
case 93:
//#line 268 ".\gramatica.y"
{ yyerror("Falta bloque del else."); }
break;
case 95:
//#line 274 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 96:
//#line 275 ".\gramatica.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 97:
//#line 276 ".\gramatica.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 98:
//#line 277 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 99:
//#line 278 ".\gramatica.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 100:
//#line 279 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 101:
//#line 280 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 102:
//#line 281 ".\gramatica.y"
{ yyerror("Falta bloque del for."); }
break;
case 106:
//#line 291 ".\gramatica.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 107:
//#line 292 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 108:
//#line 293 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 110:
//#line 299 ".\gramatica.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 111:
//#line 300 ".\gramatica.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 112:
//#line 301 ".\gramatica.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1180 "Parser.java"
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
