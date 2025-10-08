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
    2,    4,    4,    4,    4,    4,    4,    4,    5,    5,
    6,   16,   16,   14,   14,   14,   14,    7,    8,    8,
    8,    8,   18,   18,   18,   19,   19,   19,   19,   19,
   17,   17,   17,   17,   17,   20,   20,   20,   20,   20,
   21,   21,   21,   12,   12,   22,   22,   23,   23,   24,
   24,   24,   24,   24,   24,   13,   15,   15,   26,   26,
   26,   27,   27,    9,    9,    9,    9,    9,    9,    9,
    9,    9,   28,   28,   28,   28,   31,   31,   31,   31,
   31,   31,   29,   29,   30,   30,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   10,   32,   32,   11,   11,
   11,   11,   25,   25,   25,   25,   33,   33,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    3,    2,    2,    1,    3,    3,
    3,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    6,    1,    3,    1,    3,    3,    2,    3,    3,    2,
    2,    3,    1,    3,    3,    1,    2,    1,    2,    1,
    3,    3,    3,    3,    1,    3,    3,    3,    3,    1,
    1,    1,    1,    4,    4,    1,    3,    3,    3,    1,
    4,    4,    4,    4,    1,    4,    1,    3,    3,    3,
    2,    0,    1,    6,    5,    6,    4,    6,    7,    7,
    7,    4,    3,    3,    3,    3,    1,    1,    1,    1,
    1,    1,    1,    3,    2,    2,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    2,    3,    4,    4,
    4,    4,   10,   10,   10,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    3,    0,   12,   13,   14,   15,   16,
   17,   18,    0,    0,    0,    0,   38,   36,   40,    0,
    0,   52,    0,    0,   53,    0,   50,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   33,    7,   22,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   56,    0,   65,   89,   90,   91,   92,   88,   87,    4,
   93,    0,    0,   39,   37,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   11,    0,   19,    9,   10,    0,    0,    0,    0,    0,
   32,    0,   26,    0,    0,   23,    0,    0,    0,    0,
   55,    0,    0,   82,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   46,    0,   48,    0,   77,
  111,  110,  112,  109,   66,   73,    0,   67,    0,    0,
    0,    0,    0,    0,    0,   54,   35,   34,    0,    0,
    0,   57,   59,   58,   94,    0,    0,    0,    0,   75,
    0,    0,    0,   71,   20,    0,    0,    0,    0,    0,
   64,   63,   62,   61,    0,    5,   76,    0,    0,   78,
   74,    0,   68,    2,   70,   69,    0,    0,    0,    0,
    0,    0,    4,    4,    0,   95,   81,   80,   79,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   21,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    0,  105,   98,   99,  100,  101,
  102,  103,  104,    0,   97,    0,    0,    0,    0,    0,
  107,    0,    0,    0,  117,  118,    0,    0,    0,  108,
  116,  114,  115,  113,
};
final static short yydgoto[] = {                          2,
    4,   71,  113,  225,   94,   91,   16,   17,   18,   19,
   20,   21,   22,   23,  137,   24,   59,   47,   35,   36,
   37,   60,   61,   62,   63,  138,  139,   38,   72,  179,
   73,  226,  244,
};
final static short yysindex[] = {                      -264,
 -275,    0,    0,  380, -210, -142, -253, -182, -145, -191,
 -172,  314,    0,    0, -163,    0,    0,    0,    0,    0,
    0,    0,  -44, -102,  630,  464,    0,    0,    0, -260,
  319,    0, -149,  653,    0, -111,    0,  500,  677,  683,
  677, -144,   19,  -94,  -70,  630,  -89,    0,    0,    0,
  356,  -69, -149,  -89,  677,  -50, -188,   -7,  -61,  -31,
    0,  -39,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   21,  677,    0,    0,  779, -233,  677,  700,  704,
  708,  725,  729,  567,   27, -205, -110, -239,  138,  -10,
    0,   33,    0,    0,    0,   26,   58, -154,   -2,  732,
    0,  -89,    0, -149,  -61,    0,  677,  677,   40,  630,
    0,  -27, -248,    0,  -61,  567,  582,  -61, -210, -111,
 -210, -111, -210,  -61, -210,    0, -210,    0,   64,    0,
    0,    0,    0,    0,    0,    0,   13,    0, -193, -146,
 -149,   55,   70,   71, -250,    0,    0,    0, -237, -215,
   54,    0,    0,    0,    0,   57,   84, -192, -137,    0,
  -10,   68,  -11,    0,    0,   81,   95,   97,  109,  -97,
    0,    0,    0,    0, -254,    0,    0,  599,  117,    0,
    0,   28,    0,    0,    0,    0,  107,  108,  120,  126,
  127, -200,    0,    0, -210,    0,    0,    0,    0,  414,
 -232,  121,  122,  124,  125,  128, -228, -231,  424,    0,
  614,  614,  614,  614,  614,  614,  614,  614,  624,  130,
  144,  145,  159,    0,  111,    0,    0,    0,    0,    0,
    0,    0,    0, -210,    0,  652,  789,  652,  789,  439,
    0, -172,    0,  137,    0,    0,  146,  160,  161,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -9,    0,    0,    0,  399,    0,    0,    0,    0,    0,
    0,    0,    0,  753,    0,    0,    0,    0,    0,    0,
    0,    0,  -68,    0,    0,  110,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -107,    0,    0,    0,
    0,    0, -105,  295,    0,    0,    0,    0,  139,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -32,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  310,    0,  746,  347,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  510,    0,    0,  525,  147,  184,
  221,  258,  542,  557,   36,    0,   73,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -130,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  454,    0,    0,    0,    0,
  -32,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   45,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  362,    0,    0,    0,    0,    0,    0,
    0,  763,  101,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  265,   -3, -121,   -4,    0,  409,    0,    0,    0,    0,
    0,   53,    0,  363,    0,    8,   65,    4,   -5,  123,
  158,  408,  346,    0,    0,  296,    0,  433,  -34,  306,
  436,  860,  100,
};
final static int YYTABLESIZE=1079;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         15,
   14,  193,   39,   85,    1,  169,   48,    5,    6,   74,
   75,    7,    8,   33,   10,    3,  133,   48,  171,  170,
   11,   15,  116,  211,  220,    6,   54,  218,    7,    8,
   53,   10,   33,   15,   12,   40,  194,   11,   33,   79,
  173,   79,   80,  155,   80,   48,   33,   33,   33,  129,
  134,   12,  172,   33,  102,  206,  117,  212,   32,  104,
  221,  219,   33,   79,   44,  178,   80,  107,  163,  207,
   34,  208,  209,   79,  174,  164,   80,   32,   25,   15,
   33,  157,  159,   32,  131,   33,   33,   33,   33,   33,
   33,   32,   32,   32,  148,   34,   25,   45,   32,  141,
  108,  144,  240,   86,   88,   89,   41,   32,  156,  145,
   42,   15,   15,   26,   33,   33,   46,   33,  180,  105,
  178,  181,   50,   43,   49,   32,   11,   27,   28,   29,
   32,   32,   32,   32,   32,   32,   56,  115,   24,   30,
   52,  165,  118,  196,   90,  124,   31,   53,   30,   30,
   30,   30,   30,   30,   30,   30,   24,   24,  191,   32,
   32,   30,   32,   27,   27,   27,   27,  192,   82,   83,
   55,  149,  150,   15,   96,   30,   27,   27,   25,  132,
   30,   27,   27,   56,   30,   97,  103,   51,   51,   51,
   51,   51,   51,   51,   51,   15,   14,  100,   98,   50,
   51,  120,  122,  156,  156,   51,   51,   51,   51,   51,
   51,   51,   51,   51,   51,   51,   51,   79,  106,   51,
   80,   51,   51,   51,   50,   27,   28,   29,  153,   72,
  243,  246,  243,  246,  112,  156,   72,   30,   51,  126,
  128,  154,   52,   33,  185,   33,   22,   22,   22,   22,
   22,   22,   22,   22,  109,  110,  136,  186,  111,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,  114,
   22,   22,   22,  198,  110,  130,  199,  146,   32,  142,
   32,   47,   47,   47,   47,   47,   47,   47,   47,  161,
   96,   50,  162,   96,   47,   92,   93,   90,  151,   47,
   47,   47,   47,   47,   47,   47,   47,   47,   47,   47,
   47,  143,  160,   47,  166,   47,   47,   47,   49,   49,
   49,   49,   49,   49,   49,   49,  247,  248,  249,  167,
  168,   49,  177,  175,  176,  187,   49,   49,   49,   49,
   49,   49,   49,   49,   49,   49,   49,   49,  184,  188,
   49,  189,   49,   49,   49,   45,   45,   45,   45,   45,
   45,   45,   45,  190,   53,  197,  201,  202,   45,   53,
   53,   53,   53,   45,   45,   45,   45,   45,   45,  203,
  118,   45,   45,   45,   45,  204,  205,   45,  241,   45,
   45,   45,   42,   42,   42,   42,   42,   42,   42,   42,
  213,  214,   60,  215,  216,   42,   79,  217,  236,   80,
   42,   42,   42,   42,   42,   42,  251,  135,   42,   42,
   42,   42,  237,  238,   42,  252,   42,   42,   42,   41,
   41,   41,   41,   41,   41,   41,   41,  239,  200,  253,
  254,   95,   41,   99,  140,  152,  183,   41,   41,   41,
   41,   41,   41,   77,  182,   41,   41,   41,   41,   81,
    0,   41,    0,   41,   41,   41,   44,   44,   44,   44,
   44,   44,   44,   44,    0,    0,    0,    0,    0,   44,
    0,    0,    0,    0,   44,   44,   44,   44,   44,   44,
    0,    0,   44,   44,   44,   44,    0,    0,   44,    0,
   44,   44,   44,   43,   43,   43,   43,   43,   43,   43,
   43,    0,    0,    0,    0,    0,   43,    0,    0,    0,
    0,   43,   43,   43,   43,   43,   43,    0,    0,   43,
   43,   43,   43,    0,    0,   43,    0,   43,   43,   43,
   31,   31,   31,   31,   31,   31,   31,   31,    0,    0,
    0,    0,    0,   31,    0,   29,   29,   29,   29,   29,
   29,   29,   29,    0,   76,    0,    0,   31,   29,    0,
    0,    0,   31,   27,   28,   29,   31,   11,   27,   28,
   29,    0,   29,    0,    0,   30,    0,   29,    0,    0,
   30,   29,   28,   28,   28,   28,   28,   28,   28,   28,
    0,  101,    0,    0,    0,   28,    0,  106,  106,  106,
  106,  106,  106,  106,  106,   27,   28,   29,    0,   28,
  106,    0,    0,    0,   28,    5,    6,   30,   28,    7,
    8,    9,   10,    0,  106,    0,    0,    0,   11,  106,
    0,    0,    0,  106,    8,    8,    8,    8,    8,    8,
    8,    8,   12,    0,    0,    0,    0,    8,    0,    5,
    6,   13,    0,    7,    8,    9,   10,    0,    0,  222,
    6,    8,   11,    7,    8,    0,   10,    0,    0,    0,
    8,    0,   11,    0,    5,    6,   12,    0,    7,    8,
    0,   10,    0,    0,    0,  210,   12,   11,    0,    6,
    6,    0,    0,    6,    6,  223,    6,    0,    0,    5,
    6,   12,    6,    7,    8,    9,   10,    0,    0,    0,
  250,    0,   11,    0,    0,    0,    6,    0,   64,   65,
   66,   67,    0,    0,    0,    6,   12,   68,   69,    0,
    0,    0,   25,    0,   70,    5,    6,    0,    0,    7,
    8,    9,   10,    0,    0,   85,   85,    0,   11,   85,
   85,   85,   85,    0,    0,    0,    0,    0,   85,    0,
   84,   84,   12,    0,   84,   84,   84,   84,    0,   84,
   70,    0,   85,   84,    0,    0,    0,   86,   86,   85,
   85,   86,   86,   86,   86,    0,    0,   84,    0,    0,
   86,    0,   83,   83,   84,   84,   83,   83,   83,   83,
    0,    0,    5,    6,   86,   83,    7,    8,    9,   10,
    0,   86,   86,    0,    0,   11,    0,  158,    6,   83,
    0,    7,    8,    9,   10,    0,   83,   83,    0,   12,
   11,    0,    0,    0,  195,    6,    0,   70,    7,    8,
    9,   10,    0,    0,   12,    0,    0,   11,    0,    5,
    6,    0,   70,    7,    8,    0,   10,    0,    0,  234,
    6,   12,   11,    7,    8,    5,   10,    0,    0,   70,
    0,    0,   11,    0,    0,    0,   12,   57,   11,   27,
   28,   29,    0,    0,  224,    0,   12,    5,   78,    0,
    0,   30,    0,    0,  224,    0,    0,    0,   58,   57,
  242,   27,   28,   29,    0,    0,    0,   64,   65,   66,
   67,   79,    5,   30,   80,    0,   68,   69,   87,    0,
   58,    0,    0,    0,    0,   11,   27,   28,   29,    0,
    0,   11,   27,   28,   29,  119,    0,    0,   30,  121,
    0,    0,    0,  123,   30,    0,    0,    0,   11,   27,
   28,   29,   11,   27,   28,   29,   11,   27,   28,   29,
  125,   30,    0,    0,  127,   30,    0,  147,    0,   30,
    0,    0,    0,   11,   27,   28,   29,   11,   27,   28,
   29,   27,   28,   29,    0,    0,   30,    0,    0,    0,
   30,    0,    0,   30,   25,   25,   25,   25,    0,    0,
    0,   24,   24,   24,   24,    0,    0,   25,   25,    0,
    0,    0,   25,   25,   24,   24,   22,    0,    0,   24,
    0,   22,   22,   22,   22,    0,    0,    0,   22,    0,
    0,    0,  117,   64,   65,   66,   67,  245,   27,   28,
   29,    0,   68,   69,    0,    0,    0,   25,    0,    0,
   30,  227,  228,  229,  230,  231,  232,  233,  235,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    4,  256,  256,   38,  269,  256,   12,  256,  257,  270,
  271,  260,  261,    6,  263,  291,  256,   23,  256,  270,
  269,   26,  256,  256,  256,  257,   23,  256,  260,  261,
   23,  263,   25,   38,  283,  289,  291,  269,   31,  279,
  256,  279,  282,  292,  282,   51,   39,   40,   41,   84,
  290,  283,  290,   46,   51,  256,  290,  290,    6,   52,
  292,  290,   55,  279,  256,  258,  282,  256,  262,  270,
    6,  193,  194,  279,  290,  269,  282,   25,  289,   84,
   73,  116,  117,   31,  290,   78,   79,   80,   81,   82,
   83,   39,   40,   41,  100,   31,  289,  289,   46,   92,
  289,  256,  224,   39,   40,   41,  289,   55,  113,  264,
  256,  116,  117,  256,  107,  108,  289,  110,  256,   55,
  258,  259,  269,  269,  288,   73,  269,  270,  271,  272,
   78,   79,   80,   81,   82,   83,  286,   73,  269,  282,
  287,  288,   78,  178,  289,   81,  289,  140,  256,  257,
  258,  259,  260,  261,  262,  263,  287,  288,  256,  107,
  108,  269,  110,  269,  270,  271,  272,  265,  280,  281,
  273,  107,  108,  178,  269,  283,  282,  283,  289,  290,
  288,  287,  288,  286,  292,  256,  256,  256,  257,  258,
  259,  260,  261,  262,  263,  200,  200,  287,  269,  269,
  269,   79,   80,  208,  209,  274,  275,  276,  277,  278,
  279,  280,  281,  282,  283,  284,  285,  279,  269,  288,
  282,  290,  291,  292,  269,  270,  271,  272,  256,  262,
  236,  237,  238,  239,  274,  240,  269,  282,  283,   82,
   83,  269,  287,  236,  256,  238,  256,  257,  258,  259,
  260,  261,  262,  263,  262,  287,  267,  269,  290,  269,
  270,  271,  272,  273,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  286,  287,  288,  259,
  290,  291,  292,  256,  287,  259,  259,  290,  236,  264,
  238,  256,  257,  258,  259,  260,  261,  262,  263,  287,
  256,  269,  290,  259,  269,  287,  288,  289,  269,  274,
  275,  276,  277,  278,  279,  280,  281,  282,  283,  284,
  285,  264,  259,  288,  270,  290,  291,  292,  256,  257,
  258,  259,  260,  261,  262,  263,  237,  238,  239,  270,
  270,  269,  259,  290,  288,  265,  274,  275,  276,  277,
  278,  279,  280,  281,  282,  283,  284,  285,  291,  265,
  288,  265,  290,  291,  292,  256,  257,  258,  259,  260,
  261,  262,  263,  265,  274,  259,  270,  270,  269,  279,
  280,  281,  282,  274,  275,  276,  277,  278,  279,  270,
  290,  282,  283,  284,  285,  270,  270,  288,  288,  290,
  291,  292,  256,  257,  258,  259,  260,  261,  262,  263,
  290,  290,  274,  290,  290,  269,  279,  290,  289,  282,
  274,  275,  276,  277,  278,  279,  290,  290,  282,  283,
  284,  285,  289,  289,  288,  290,  290,  291,  292,  256,
  257,  258,  259,  260,  261,  262,  263,  289,  184,  290,
  290,   43,  269,   46,   92,  110,  161,  274,  275,  276,
  277,  278,  279,   31,  159,  282,  283,  284,  285,   34,
   -1,  288,   -1,  290,  291,  292,  256,  257,  258,  259,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,
   -1,   -1,   -1,   -1,  274,  275,  276,  277,  278,  279,
   -1,   -1,  282,  283,  284,  285,   -1,   -1,  288,   -1,
  290,  291,  292,  256,  257,  258,  259,  260,  261,  262,
  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,   -1,   -1,
   -1,  274,  275,  276,  277,  278,  279,   -1,   -1,  282,
  283,  284,  285,   -1,   -1,  288,   -1,  290,  291,  292,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,   -1,
   -1,   -1,   -1,  269,   -1,  256,  257,  258,  259,  260,
  261,  262,  263,   -1,  256,   -1,   -1,  283,  269,   -1,
   -1,   -1,  288,  270,  271,  272,  292,  269,  270,  271,
  272,   -1,  283,   -1,   -1,  282,   -1,  288,   -1,   -1,
  282,  292,  256,  257,  258,  259,  260,  261,  262,  263,
   -1,  256,   -1,   -1,   -1,  269,   -1,  256,  257,  258,
  259,  260,  261,  262,  263,  270,  271,  272,   -1,  283,
  269,   -1,   -1,   -1,  288,  256,  257,  282,  292,  260,
  261,  262,  263,   -1,  283,   -1,   -1,   -1,  269,  288,
   -1,   -1,   -1,  292,  256,  257,  258,  259,  260,  261,
  262,  263,  283,   -1,   -1,   -1,   -1,  269,   -1,  256,
  257,  292,   -1,  260,  261,  262,  263,   -1,   -1,  256,
  257,  283,  269,  260,  261,   -1,  263,   -1,   -1,   -1,
  292,   -1,  269,   -1,  256,  257,  283,   -1,  260,  261,
   -1,  263,   -1,   -1,   -1,  292,  283,  269,   -1,  256,
  257,   -1,   -1,  260,  261,  292,  263,   -1,   -1,  256,
  257,  283,  269,  260,  261,  262,  263,   -1,   -1,   -1,
  292,   -1,  269,   -1,   -1,   -1,  283,   -1,  275,  276,
  277,  278,   -1,   -1,   -1,  292,  283,  284,  285,   -1,
   -1,   -1,  289,   -1,  291,  256,  257,   -1,   -1,  260,
  261,  262,  263,   -1,   -1,  256,  257,   -1,  269,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,
  256,  257,  283,   -1,  260,  261,  262,  263,   -1,  290,
  291,   -1,  283,  269,   -1,   -1,   -1,  256,  257,  290,
  291,  260,  261,  262,  263,   -1,   -1,  283,   -1,   -1,
  269,   -1,  256,  257,  290,  291,  260,  261,  262,  263,
   -1,   -1,  256,  257,  283,  269,  260,  261,  262,  263,
   -1,  290,  291,   -1,   -1,  269,   -1,  256,  257,  283,
   -1,  260,  261,  262,  263,   -1,  290,  291,   -1,  283,
  269,   -1,   -1,   -1,  256,  257,   -1,  291,  260,  261,
  262,  263,   -1,   -1,  283,   -1,   -1,  269,   -1,  256,
  257,   -1,  291,  260,  261,   -1,  263,   -1,   -1,  256,
  257,  283,  269,  260,  261,  256,  263,   -1,   -1,  291,
   -1,   -1,  269,   -1,   -1,   -1,  283,  268,  269,  270,
  271,  272,   -1,   -1,  291,   -1,  283,  256,  256,   -1,
   -1,  282,   -1,   -1,  291,   -1,   -1,   -1,  289,  268,
  269,  270,  271,  272,   -1,   -1,   -1,  275,  276,  277,
  278,  279,  256,  282,  282,   -1,  284,  285,  256,   -1,
  289,   -1,   -1,   -1,   -1,  269,  270,  271,  272,   -1,
   -1,  269,  270,  271,  272,  256,   -1,   -1,  282,  256,
   -1,   -1,   -1,  256,  282,   -1,   -1,   -1,  269,  270,
  271,  272,  269,  270,  271,  272,  269,  270,  271,  272,
  256,  282,   -1,   -1,  256,  282,   -1,  256,   -1,  282,
   -1,   -1,   -1,  269,  270,  271,  272,  269,  270,  271,
  272,  270,  271,  272,   -1,   -1,  282,   -1,   -1,   -1,
  282,   -1,   -1,  282,  269,  270,  271,  272,   -1,   -1,
   -1,  269,  270,  271,  272,   -1,   -1,  282,  283,   -1,
   -1,   -1,  287,  288,  282,  283,  274,   -1,   -1,  287,
   -1,  279,  280,  281,  282,   -1,   -1,   -1,  286,   -1,
   -1,   -1,  290,  275,  276,  277,  278,  269,  270,  271,
  272,   -1,  284,  285,   -1,   -1,   -1,  289,   -1,   -1,
  282,  212,  213,  214,  215,  216,  217,  218,  219,
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
"sentencia : INT ID decl_var",
"sentencia : INT ID decl_func",
"sentencia : INT error decl_func",
"sentencia_ejec : asign_simple",
"sentencia_ejec : asign_multiple",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion",
"sentencia_ejec : return_sent",
"decl_var : PUNTOYCOMA",
"decl_var : COMA lista_ids PUNTOYCOMA",
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
"param_formal : sem_pasaje_opt ID",
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
"bloque_for : FOR error ID FROM CTEINT TO CTEINT error rama_for",
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

//#line 319 ".\gramatica.y"

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
//#line 829 "Parser.java"
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
case 11:
//#line 80 ".\gramatica.y"
{yyerror("Falta identificador de función despues de 'int'");}
break;
case 20:
//#line 94 ".\gramatica.y"
{n_var = 0;}
break;
case 24:
//#line 107 ".\gramatica.y"
{n_var++;}
break;
case 25:
//#line 108 ".\gramatica.y"
{n_var++;}
break;
case 26:
//#line 109 ".\gramatica.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 27:
//#line 110 ".\gramatica.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 28:
//#line 116 ".\gramatica.y"
{System.out.println("Asignación válida");}
break;
case 29:
//#line 121 ".\gramatica.y"
{
                    if (n_var == 1 && n_cte == 1) {
                        yyerror("Error: para asignación simple use ':=' en lugar de '='");
                    } else {
                        if (n_var < n_cte) {
                            yyerror("Error: más constantes que variables en la asignación");
                        } else {
                            System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
                        }
                    }
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 30:
//#line 133 ".\gramatica.y"
{ yyerror("Error: falta lista de variables antes del '='"); }
break;
case 31:
//#line 134 ".\gramatica.y"
{ yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 32:
//#line 135 ".\gramatica.y"
{ yyerror("Error: falta lista de constantes después del '='");}
break;
case 33:
//#line 138 ".\gramatica.y"
{n_cte++;}
break;
case 34:
//#line 139 ".\gramatica.y"
{n_cte++;}
break;
case 35:
//#line 140 ".\gramatica.y"
{ yyerror("Error: falta una constante después de coma");}
break;
case 37:
//#line 146 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 38:
//#line 153 ".\gramatica.y"
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
case 39:
//#line 167 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 42:
//#line 181 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 44:
//#line 183 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 47:
//#line 189 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 49:
//#line 191 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 55:
//#line 205 ".\gramatica.y"
{ yyerror("Llamada a función sin nombre");}
break;
case 59:
//#line 214 ".\gramatica.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 62:
//#line 219 ".\gramatica.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 63:
//#line 220 ".\gramatica.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 64:
//#line 221 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 70:
//#line 236 ".\gramatica.y"
{ yyerror("Falta identificador después de 'int' en parámetro formal");}
break;
case 71:
//#line 237 ".\gramatica.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 75:
//#line 247 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 76:
//#line 248 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 77:
//#line 249 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 78:
//#line 250 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 80:
//#line 252 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 81:
//#line 253 ".\gramatica.y"
{ yyerror("Falta bloque del then."); }
break;
case 82:
//#line 254 ".\gramatica.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 84:
//#line 259 ".\gramatica.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 85:
//#line 260 ".\gramatica.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 86:
//#line 261 ".\gramatica.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 96:
//#line 277 ".\gramatica.y"
{ yyerror("Falta bloque del else."); }
break;
case 98:
//#line 283 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 99:
//#line 284 ".\gramatica.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 100:
//#line 285 ".\gramatica.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 101:
//#line 286 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 102:
//#line 287 ".\gramatica.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 103:
//#line 288 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 104:
//#line 289 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 105:
//#line 290 ".\gramatica.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 106:
//#line 291 ".\gramatica.y"
{ yyerror("Falta bloque del for."); }
break;
case 110:
//#line 301 ".\gramatica.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 111:
//#line 302 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 112:
//#line 303 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 114:
//#line 309 ".\gramatica.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 115:
//#line 310 ".\gramatica.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 116:
//#line 311 ".\gramatica.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1239 "Parser.java"
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
