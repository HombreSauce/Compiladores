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
   26,   26,    8,    8,    8,    8,    8,    8,    8,    8,
    8,   27,   27,   27,   27,   30,   30,   30,   30,   30,
   30,   28,   28,   29,   29,    9,    9,    9,    9,    9,
    9,    9,    9,    9,   31,   31,   10,   10,   10,   10,
   24,   24,   24,   24,   32,   32,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    3,    2,    2,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    6,
    1,    3,    1,    3,    3,    2,    3,    3,    2,    2,
    3,    1,    3,    3,    1,    2,    1,    2,    1,    3,
    3,    3,    3,    1,    3,    3,    3,    3,    1,    1,
    1,    1,    4,    4,    1,    3,    3,    3,    1,    4,
    4,    4,    4,    1,    4,    1,    3,    3,    3,    3,
    0,    1,    6,    5,    6,    4,    6,    7,    7,    7,
    4,    3,    3,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    3,    2,    2,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    2,    3,    4,    4,    4,    4,
   10,   10,   10,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    3,    0,   11,   12,   13,   14,   15,
   16,   17,    0,    0,    0,    0,   37,   35,   39,    0,
    0,   51,    0,    0,   52,    0,   49,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   32,    7,   21,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   55,    0,   64,   88,   89,   90,   91,   87,   86,    4,
   92,    0,    0,   38,   36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   18,    0,   10,    9,    0,    0,    0,    0,    0,   31,
    0,   25,    0,    0,   22,    0,    0,    0,    0,   54,
    0,    0,   81,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   45,    0,   47,    0,   76,  109,
  108,  110,  107,   65,    0,    0,   72,    0,   66,    0,
    0,    0,    0,    0,   53,   34,   33,    0,    0,    0,
   56,   58,   57,   93,    0,    0,    0,    0,   74,   19,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   63,
   62,   61,   60,    0,    5,   75,    0,    0,   77,   73,
    0,   67,    2,   70,   69,   68,    0,    0,    0,    0,
    0,    0,    4,    4,    0,   94,   80,   79,   78,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   20,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    4,    0,   97,   98,   99,  100,  101,  102,
  103,    0,   96,    0,    0,    0,    0,    0,  105,    0,
    0,    0,  115,  116,    0,    0,    0,  106,  114,  112,
  113,  111,
};
final static short yydgoto[] = {                          2,
    4,   71,  112,   15,   93,   16,   17,   18,   19,   20,
   21,   22,   23,  138,   24,   59,   47,   35,   36,   37,
   60,   61,   62,   63,  139,  140,   38,   72,  178,   73,
  225,  242,
};
final static short yysindex[] = {                      -221,
 -259,    0,    0,  -61, -206,  642, -233, -201, -242, -228,
 -187,  297,    0,    0, -170,    0,    0,    0,    0,    0,
    0,    0,  738, -167,  615,  449,    0,    0,    0, -265,
 -134,    0, -196,  643,    0,    4,    0,  459, -125,  302,
 -125,  -68,  -68, -141, -139,  615, -155,    0,    0,    0,
  339,  -76, -196, -155, -125,  -90, -226,  -97, -214, -148,
    0, -113,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -55, -125,    0,    0,  770, -247, -125,  474,  667,
  673,  690,  694,  548,  -50,  121,   16, -253,  158,  -40,
    0,  -20,    0,    0,  -15,    7, -198, -101,  697,    0,
 -155,    0, -196, -214,    0, -125, -125,   18,  615,    0,
  -62, -245,    0, -214,  548,  558, -214, -206,    4, -206,
    4, -206, -214, -206,    0, -206,    0,   33,    0,    0,
    0,    0,    0,    0, -189, -196,    0,  -84,    0, -129,
   38,   50,   53, -199,    0,    0,    0, -220,  -92,   34,
    0,    0,    0,    0,   40,   70, -237,  -24,    0,    0,
  -20,   51,   74,  -51,   80,   92,   94,   95, -234,    0,
    0,    0,    0, -249,    0,    0,  573,  102,    0,    0,
  -23,    0,    0,    0,    0,    0,  103,  109,  110,  112,
  124, -192,    0,    0, -206,    0,    0,    0,    0,  363,
  105,  106,  107,  108,  111,  120, -246,  393,  403,    0,
  590,  590,  590,  590,  590,  590,  590,  605,  113,  127,
  128,  142,    0,  131,    0,    0,    0,    0,    0,    0,
    0, -206,    0,  621,  780,  621,  780,  421,    0, -187,
    0,  143,    0,    0,  144,  145,  148,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -18,    0,    0,    0,  -46,    0,    0,    0,    0,    0,
    0,    0,    0,  744,    0,    0,    0,    0,    0,    0,
    0,    0, -107,    0,    0,   93,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  278,    0,    0,    0,
    0,    0,  711,  293,    0,    0,    0,    0,  165,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -74,    0,    0,    0,    0,    0,    0,    0,    0,
  330,    0,  718,  345,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  491,    0,    0,  501,  130,  167,  204,
  241,  516,  533,   19,    0,   56,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -128,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  431,    0,    0,    0,    0,    0,
  -74,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  382,    0,    0,    0,    0,    0,    0,    0,  754,
   84,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  249,   -3, -108, -104,  404,    0,    0,    0,    0,    0,
   14,    0,  364,    0,   -6,   85,   28,  -10,  242,  244,
  407,  347,    0,    0,  307,    0,  438,  -34,  312,  437,
  851,   54,
};
final static int YYTABLESIZE=1069;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         33,
   14,   48,  132,   85,   74,   75,  193,  155,  115,  217,
    5,    6,   48,   42,    7,    8,   53,   10,   33,   32,
  177,  191,   39,   11,   33,   79,   43,   44,   80,  106,
  192,    3,   33,   33,   33,  170,  133,   12,   32,   33,
   48,  194,  116,  218,   32,  103,  154,    1,   33,  128,
   54,   25,   32,   32,   32,   40,  168,  143,   79,   32,
   45,   80,  107,  206,   79,  144,   33,   80,   32,  171,
  169,   33,   33,   33,   33,   33,   33,  207,  101,   50,
  156,  158,   25,  136,  208,  209,   32,   41,  147,   56,
   34,   32,   32,   32,   32,   32,   32,   52,  160,   33,
   33,   46,   33,  155,  155,   55,  224,  224,  224,  224,
  224,  224,  224,  224,  238,   34,   96,   49,   56,   32,
   32,   76,   32,   86,   88,   89,  163,   95,   53,   97,
    5,   99,  164,  155,   11,   27,   28,   29,  109,  104,
   23,  110,  196,   11,   27,   28,   29,   30,   50,   50,
   50,   50,   50,   50,   50,   50,   30,  114,   23,   23,
  111,   50,  117,  172,  108,  123,   50,   50,   50,   50,
   50,   50,   50,   50,   50,   50,   50,   50,  105,  102,
   50,   71,   50,   50,   50,  109,   79,   71,  145,   80,
  148,  149,   50,  152,    5,    6,   14,  173,    7,    8,
    9,   10,  161,  113,  185,  162,  153,   11,  129,    8,
    8,    8,    8,    8,    8,    8,    8,  186,   90,   91,
   92,   12,    8,  241,  244,  241,  244,   33,   50,   33,
   13,  179,  198,  177,  180,  199,    8,   21,   21,   21,
   21,   21,   21,   21,   21,    8,  137,   32,  141,   32,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
  142,   21,   21,   21,   46,   46,   46,   46,   46,   46,
   46,   46,   95,   82,   83,   95,  150,   46,  245,  246,
  247,  159,   46,   46,   46,   46,   46,   46,   46,   46,
   46,   46,   46,   46,   25,  131,   46,  165,   46,   46,
   46,   48,   48,   48,   48,   48,   48,   48,   48,  166,
  119,  121,  167,  174,   48,  125,  127,  175,  176,   48,
   48,   48,   48,   48,   48,   48,   48,   48,   48,   48,
   48,  183,  184,   48,  187,   48,   48,   48,   44,   44,
   44,   44,   44,   44,   44,   44,  188,   52,  189,  190,
  197,   44,   52,   52,   52,   52,   44,   44,   44,   44,
   44,   44,  201,  116,   44,   44,   44,   44,  202,  203,
   44,  204,   44,   44,   44,   41,   41,   41,   41,   41,
   41,   41,   41,  205,  211,  212,  213,  214,   41,   79,
  215,  234,   80,   41,   41,   41,   41,   41,   41,  216,
  130,   41,   41,   41,   41,  235,  236,   41,  239,   41,
   41,   41,   40,   40,   40,   40,   40,   40,   40,   40,
  237,  200,  249,  250,  251,   40,   79,  252,   59,   80,
   40,   40,   40,   40,   40,   40,   94,  134,   40,   40,
   40,   40,   98,  135,   40,  151,   40,   40,   40,   43,
   43,   43,   43,   43,   43,   43,   43,  182,   77,  181,
   81,    0,   43,    0,    0,    0,    0,   43,   43,   43,
   43,   43,   43,    0,    0,   43,   43,   43,   43,    0,
    0,   43,    0,   43,   43,   43,   42,   42,   42,   42,
   42,   42,   42,   42,    0,    0,    0,    0,    0,   42,
    0,    0,    0,    0,   42,   42,   42,   42,   42,   42,
    0,    0,   42,   42,   42,   42,    0,    0,   42,    0,
   42,   42,   42,   29,   29,   29,   29,   29,   29,   29,
   29,    0,    0,    0,    0,    0,   29,    0,   30,   30,
   30,   30,   30,   30,   30,   30,    0,   87,    0,    0,
   29,   30,    0,    0,    0,   29,   27,   28,   29,   29,
   11,   27,   28,   29,    0,   30,    0,    0,   30,    0,
   30,    0,    0,   30,   30,   28,   28,   28,   28,   28,
   28,   28,   28,    0,  100,    0,    0,    0,   28,    0,
   27,   27,   27,   27,   27,   27,   27,   27,   27,   28,
   29,    0,   28,   27,    0,    0,    0,   28,    5,    6,
   30,   28,    7,    8,    9,   10,    0,   27,    0,    0,
    0,   11,   27,    0,    0,    0,   27,  104,  104,  104,
  104,  104,  104,  104,  104,   12,    0,    0,  219,    6,
  104,    0,    7,    8,  210,   10,    0,    0,  221,    6,
    0,   11,    7,    8,  104,   10,    0,    0,    0,  104,
    0,   11,    0,  104,    0,   12,    5,    6,    0,    0,
    7,    8,    0,   10,  220,   12,    6,    6,    0,   11,
    6,    6,    0,    6,  222,    0,    0,    0,    0,    6,
    0,    0,    0,   12,    5,    6,    0,    0,    7,    8,
    9,   10,  248,    6,    5,    6,    0,   11,    7,    8,
    9,   10,    6,   64,   65,   66,   67,   11,    0,  118,
    0,   12,   68,   69,    0,    0,    0,   25,    0,   70,
    0,   12,   11,   27,   28,   29,   84,   84,   84,   70,
   84,   84,   84,   84,    0,   30,   83,   83,    0,   84,
   83,   83,   83,   83,    0,    0,    0,    0,    0,   83,
    0,   85,   85,   84,    0,   85,   85,   85,   85,    0,
   84,   84,    0,   83,   85,    0,    0,    0,   82,   82,
   83,   83,   82,   82,   82,   82,    0,    0,   85,    0,
    0,   82,    0,    5,    6,   85,   85,    7,    8,    9,
   10,    0,    0,  157,    6,   82,   11,    7,    8,    9,
   10,    0,   82,   82,    0,    0,   11,    0,  195,    6,
   12,    0,    7,    8,    9,   10,    0,    0,   70,    0,
   12,   11,    0,    0,    0,    5,    6,    0,   70,    7,
    8,    0,   10,    0,    0,   12,    0,    0,   11,    0,
  232,    6,    0,   70,    7,    8,    0,   10,    0,    0,
    5,    0,   12,   11,    0,    0,    5,    0,    0,    0,
  223,    0,   57,   11,   27,   28,   29,   12,   57,  240,
   27,   28,   29,    0,    0,  223,   30,   26,   78,    0,
    0,    0,   30,   58,    0,    0,    0,    0,    0,   58,
   11,   27,   28,   29,    0,    0,    0,   64,   65,   66,
   67,   79,  120,   30,   80,    0,   68,   69,  122,    0,
   31,    0,    0,    0,    0,   11,   27,   28,   29,    0,
    0,   11,   27,   28,   29,  124,    0,    0,   30,  126,
    0,    0,  146,    0,   30,    0,    0,    0,   11,   27,
   28,   29,   11,   27,   28,   29,   27,   28,   29,    0,
    0,   30,    0,    0,    0,   30,    0,    0,   30,   26,
   26,   26,   26,    0,    0,    0,   24,   24,   24,   24,
    0,    0,   26,   26,    0,    0,    0,   26,   26,   24,
   24,    0,    0,    0,   24,   24,   50,   27,   28,   29,
    0,    0,   23,   23,   23,   23,    0,    0,    0,   30,
   51,    0,    0,    0,   52,   23,   23,   21,    0,    0,
   23,    0,   21,   21,   21,   21,    0,    0,    0,   21,
    0,    0,    0,  115,   64,   65,   66,   67,  243,   27,
   28,   29,    0,   68,   69,    0,    0,    0,   25,    0,
    0,   30,  226,  227,  228,  229,  230,  231,  233,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          6,
    4,   12,  256,   38,  270,  271,  256,  112,  256,  256,
  256,  257,   23,  256,  260,  261,   23,  263,   25,    6,
  258,  256,  256,  269,   31,  279,  269,  256,  282,  256,
  265,  291,   39,   40,   41,  256,  290,  283,   25,   46,
   51,  291,  290,  290,   31,   52,  292,  269,   55,   84,
   23,  289,   39,   40,   41,  289,  256,  256,  279,   46,
  289,  282,  289,  256,  279,  264,   73,  282,   55,  290,
  270,   78,   79,   80,   81,   82,   83,  270,   51,  269,
  115,  116,  289,   90,  193,  194,   73,  289,   99,  286,
    6,   78,   79,   80,   81,   82,   83,  287,  288,  106,
  107,  289,  109,  208,  209,  273,  211,  212,  213,  214,
  215,  216,  217,  218,  223,   31,  256,  288,  286,  106,
  107,  256,  109,   39,   40,   41,  256,  269,  135,  269,
  256,  287,  262,  238,  269,  270,  271,  272,  287,   55,
  269,  290,  177,  269,  270,  271,  272,  282,  256,  257,
  258,  259,  260,  261,  262,  263,  282,   73,  287,  288,
  274,  269,   78,  256,  262,   81,  274,  275,  276,  277,
  278,  279,  280,  281,  282,  283,  284,  285,  269,  256,
  288,  256,  290,  291,  292,  287,  279,  262,  290,  282,
  106,  107,  269,  256,  256,  257,  200,  290,  260,  261,
  262,  263,  287,  259,  256,  290,  269,  269,  259,  256,
  257,  258,  259,  260,  261,  262,  263,  269,  287,  288,
  289,  283,  269,  234,  235,  236,  237,  234,  269,  236,
  292,  256,  256,  258,  259,  259,  283,  256,  257,  258,
  259,  260,  261,  262,  263,  292,  267,  234,  264,  236,
  269,  270,  271,  272,  273,  274,  275,  276,  277,  278,
  279,  280,  281,  282,  283,  284,  285,  286,  287,  288,
  264,  290,  291,  292,  256,  257,  258,  259,  260,  261,
  262,  263,  256,  280,  281,  259,  269,  269,  235,  236,
  237,  259,  274,  275,  276,  277,  278,  279,  280,  281,
  282,  283,  284,  285,  289,  290,  288,  270,  290,  291,
  292,  256,  257,  258,  259,  260,  261,  262,  263,  270,
   79,   80,  270,  290,  269,   82,   83,  288,  259,  274,
  275,  276,  277,  278,  279,  280,  281,  282,  283,  284,
  285,  291,  269,  288,  265,  290,  291,  292,  256,  257,
  258,  259,  260,  261,  262,  263,  265,  274,  265,  265,
  259,  269,  279,  280,  281,  282,  274,  275,  276,  277,
  278,  279,  270,  290,  282,  283,  284,  285,  270,  270,
  288,  270,  290,  291,  292,  256,  257,  258,  259,  260,
  261,  262,  263,  270,  290,  290,  290,  290,  269,  279,
  290,  289,  282,  274,  275,  276,  277,  278,  279,  290,
  290,  282,  283,  284,  285,  289,  289,  288,  288,  290,
  291,  292,  256,  257,  258,  259,  260,  261,  262,  263,
  289,  183,  290,  290,  290,  269,  279,  290,  274,  282,
  274,  275,  276,  277,  278,  279,   43,  290,  282,  283,
  284,  285,   46,   90,  288,  109,  290,  291,  292,  256,
  257,  258,  259,  260,  261,  262,  263,  161,   31,  158,
   34,   -1,  269,   -1,   -1,   -1,   -1,  274,  275,  276,
  277,  278,  279,   -1,   -1,  282,  283,  284,  285,   -1,
   -1,  288,   -1,  290,  291,  292,  256,  257,  258,  259,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,
   -1,   -1,   -1,   -1,  274,  275,  276,  277,  278,  279,
   -1,   -1,  282,  283,  284,  285,   -1,   -1,  288,   -1,
  290,  291,  292,  256,  257,  258,  259,  260,  261,  262,
  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,  256,  257,
  258,  259,  260,  261,  262,  263,   -1,  256,   -1,   -1,
  283,  269,   -1,   -1,   -1,  288,  270,  271,  272,  292,
  269,  270,  271,  272,   -1,  283,   -1,   -1,  282,   -1,
  288,   -1,   -1,  282,  292,  256,  257,  258,  259,  260,
  261,  262,  263,   -1,  256,   -1,   -1,   -1,  269,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,  270,  271,
  272,   -1,  283,  269,   -1,   -1,   -1,  288,  256,  257,
  282,  292,  260,  261,  262,  263,   -1,  283,   -1,   -1,
   -1,  269,  288,   -1,   -1,   -1,  292,  256,  257,  258,
  259,  260,  261,  262,  263,  283,   -1,   -1,  256,  257,
  269,   -1,  260,  261,  292,  263,   -1,   -1,  256,  257,
   -1,  269,  260,  261,  283,  263,   -1,   -1,   -1,  288,
   -1,  269,   -1,  292,   -1,  283,  256,  257,   -1,   -1,
  260,  261,   -1,  263,  292,  283,  256,  257,   -1,  269,
  260,  261,   -1,  263,  292,   -1,   -1,   -1,   -1,  269,
   -1,   -1,   -1,  283,  256,  257,   -1,   -1,  260,  261,
  262,  263,  292,  283,  256,  257,   -1,  269,  260,  261,
  262,  263,  292,  275,  276,  277,  278,  269,   -1,  256,
   -1,  283,  284,  285,   -1,   -1,   -1,  289,   -1,  291,
   -1,  283,  269,  270,  271,  272,  256,  257,  290,  291,
  260,  261,  262,  263,   -1,  282,  256,  257,   -1,  269,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,
   -1,  256,  257,  283,   -1,  260,  261,  262,  263,   -1,
  290,  291,   -1,  283,  269,   -1,   -1,   -1,  256,  257,
  290,  291,  260,  261,  262,  263,   -1,   -1,  283,   -1,
   -1,  269,   -1,  256,  257,  290,  291,  260,  261,  262,
  263,   -1,   -1,  256,  257,  283,  269,  260,  261,  262,
  263,   -1,  290,  291,   -1,   -1,  269,   -1,  256,  257,
  283,   -1,  260,  261,  262,  263,   -1,   -1,  291,   -1,
  283,  269,   -1,   -1,   -1,  256,  257,   -1,  291,  260,
  261,   -1,  263,   -1,   -1,  283,   -1,   -1,  269,   -1,
  256,  257,   -1,  291,  260,  261,   -1,  263,   -1,   -1,
  256,   -1,  283,  269,   -1,   -1,  256,   -1,   -1,   -1,
  291,   -1,  268,  269,  270,  271,  272,  283,  268,  269,
  270,  271,  272,   -1,   -1,  291,  282,  256,  256,   -1,
   -1,   -1,  282,  289,   -1,   -1,   -1,   -1,   -1,  289,
  269,  270,  271,  272,   -1,   -1,   -1,  275,  276,  277,
  278,  279,  256,  282,  282,   -1,  284,  285,  256,   -1,
  289,   -1,   -1,   -1,   -1,  269,  270,  271,  272,   -1,
   -1,  269,  270,  271,  272,  256,   -1,   -1,  282,  256,
   -1,   -1,  256,   -1,  282,   -1,   -1,   -1,  269,  270,
  271,  272,  269,  270,  271,  272,  270,  271,  272,   -1,
   -1,  282,   -1,   -1,   -1,  282,   -1,   -1,  282,  269,
  270,  271,  272,   -1,   -1,   -1,  269,  270,  271,  272,
   -1,   -1,  282,  283,   -1,   -1,   -1,  287,  288,  282,
  283,   -1,   -1,   -1,  287,  288,  269,  270,  271,  272,
   -1,   -1,  269,  270,  271,  272,   -1,   -1,   -1,  282,
  283,   -1,   -1,   -1,  287,  282,  283,  274,   -1,   -1,
  287,   -1,  279,  280,  281,  282,   -1,   -1,   -1,  286,
   -1,   -1,   -1,  290,  275,  276,  277,  278,  269,  270,
  271,  272,   -1,  284,  285,   -1,   -1,   -1,  289,   -1,
   -1,  282,  212,  213,  214,  215,  216,  217,  218,
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
"bloque_if : IF condicion PARENTFIN rama_if ENDIF",
"bloque_if : IF PARENTINIC condicion error rama_if ENDIF",
"bloque_if : IF condicion rama_if ENDIF",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if error",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if opt_else error",
"bloque_if : IF PARENTINIC condicion PARENTFIN error opt_else ENDIF",
"bloque_if : IF error rama_if ENDIF",
"condicion : expresion relop expresion",
"condicion : expresion error expresion",
"condicion : error relop expresion",
"condicion : expresion relop error",
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

//#line 312 ".\gramatica.y"

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
//#line 825 "Parser.java"
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
{ yyerror("Falta '(' en sentencia if."); }
break;
case 75:
//#line 241 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 76:
//#line 242 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 77:
//#line 243 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 79:
//#line 245 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 80:
//#line 246 ".\gramatica.y"
{ yyerror("Falta bloque del then."); }
break;
case 81:
//#line 247 ".\gramatica.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 83:
//#line 252 ".\gramatica.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 84:
//#line 253 ".\gramatica.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 85:
//#line 254 ".\gramatica.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 95:
//#line 271 ".\gramatica.y"
{ yyerror("Falta bloque del else."); }
break;
case 97:
//#line 277 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 98:
//#line 278 ".\gramatica.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 99:
//#line 279 ".\gramatica.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 100:
//#line 280 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 101:
//#line 281 ".\gramatica.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 102:
//#line 282 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 103:
//#line 283 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 104:
//#line 284 ".\gramatica.y"
{ yyerror("Falta bloque del for."); }
break;
case 108:
//#line 294 ".\gramatica.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 109:
//#line 295 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 110:
//#line 296 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 112:
//#line 302 ".\gramatica.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 113:
//#line 303 ".\gramatica.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 114:
//#line 304 ".\gramatica.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1223 "Parser.java"
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
