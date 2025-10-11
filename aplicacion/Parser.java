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



//#line 4 "gramaticaDeCero.y"
	import aplicacion.AnalizadorLexico;
	import aplicacion.Token;
	import datos.TablaSimbolos;
	import datos.EntradaTablaSimbolos;
	import datos.TablaIdentificadorToken;
	import datos.TablaPalabraReservada;
    import salida.Generador_out;
	import salida.Tokens_out;
	import salida.TS_out;
	import salida.SintaxStruct_out;
	import salida.Errors_out;
	import static salida.Generador_out.*;
	import java.io.*;
	import java.nio.charset.StandardCharsets;
	import java.nio.file.*;
//#line 33 "Parser.java"




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
    0,    0,    0,    0,    1,    1,    3,    3,    3,    3,
    5,    2,    2,    2,    2,    2,    4,    4,    4,    4,
    4,    4,    6,    6,   15,   15,   15,   15,   15,    7,
    7,    7,    7,    7,    8,    8,   19,   17,   17,   17,
   20,   20,   20,   21,   21,   18,   18,   18,    9,   10,
   10,   10,   24,   24,   24,   24,   23,   23,   23,   23,
   22,   22,   16,   16,   16,   16,   16,   16,   16,   26,
   26,   26,   26,   26,   26,   26,   26,   27,   27,   25,
   25,   25,   25,   25,   11,   11,   11,   31,   31,   31,
   31,   31,   31,   31,   31,   31,   31,   31,   28,   28,
   28,   32,   32,   32,   32,   32,   32,   29,   29,   29,
   29,   30,   30,   30,   30,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   33,   33,   33,   13,   13,
   13,   13,   14,   34,   34,   35,   35,   36,   36,   36,
   36,   36,   36,   37,   37,   37,   37,   38,   38,
};
final static short yylen[] = {                            2,
    4,    4,    3,    4,    0,    2,    3,    2,    1,    2,
    1,    1,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    2,    1,    3,    3,    2,    2,    5,
    4,    5,    5,    5,    9,    1,    7,    1,    3,    3,
    3,    3,    2,    0,    1,    5,    4,    0,    3,    3,
    2,    3,    1,    3,    3,    2,    1,    3,    3,    2,
    1,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    3,    1,    1,    1,
    2,    1,    2,    1,    6,    8,    1,    5,    5,    4,
    7,    7,    6,    3,    5,    6,    8,    5,    3,    3,
    3,    1,    1,    1,    1,    1,    1,    2,    3,    2,
    0,    2,    3,    2,    0,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    1,    3,    2,    4,    4,
    4,    3,    4,    1,    3,    3,    3,    1,    4,    3,
    4,    3,    1,   10,    9,    9,    8,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    5,    5,    5,    0,    0,    0,    0,
    0,   11,    0,    0,    0,    4,    6,    0,    0,   12,
   13,   16,   17,   18,   19,   20,   21,   22,   36,    0,
    0,   87,    2,    1,    0,   82,   80,   84,    0,    0,
    0,    0,    0,    0,   79,    0,   70,    0,    0,    0,
    0,    0,    0,    0,    0,   53,   14,    0,    0,    0,
    0,    0,    0,   61,    0,    0,    0,  104,  105,  106,
  107,    0,    0,    0,    0,  103,  102,    0,   83,   81,
    0,    0,    0,  110,    0,    0,  108,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   94,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  134,    0,
  143,    0,   56,   29,    0,   28,    0,    0,    0,   45,
    0,   38,    0,    0,   23,    0,   62,   52,    0,   59,
    0,    0,    0,   73,   76,    0,    0,    0,    0,    0,
  109,    0,    8,    0,    0,    0,    0,    0,    0,   77,
   72,   71,   75,   74,    0,    0,   90,    0,    0,    0,
  131,  130,  129,    0,    0,    0,    0,    0,    0,    0,
    0,  133,    0,   55,   54,    0,    0,    0,    0,    0,
    0,    0,   43,    0,   27,   26,    0,   98,    0,    0,
   89,    7,    0,   88,    0,  114,    0,  112,   95,    0,
    0,    0,    0,    0,    0,    0,    0,  135,  137,  136,
   34,   33,   32,   30,    0,   40,   39,    5,   42,   41,
    0,   96,    0,   85,    0,    0,   93,  113,    0,    0,
    0,    0,    0,    0,  141,  139,    0,    5,    0,    0,
    0,   92,   91,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   37,   97,   86,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  126,  124,  117,  118,  119,  120,  121,  122,  123,
  125,  116,    0,    0,  148,  149,    0,    0,    0,   35,
  128,    0,    0,    0,  147,    0,    0,  127,  146,    0,
  145,    0,  144,   46,
};
final static short yydgoto[] = {                          3,
    7,   17,   85,  272,   19,   20,   21,   22,   23,   24,
   25,   26,   27,   28,   61,   43,  121,  270,   29,  122,
  123,   30,   31,   55,   45,   46,   47,   48,   49,  160,
   32,   78,  273,  108,  109,  110,  111,  287,
};
final static short yysindex[] = {                      -178,
 -278, -251,    0,    0,    0,    0, -240, -127, -108, -145,
 -230,    0, -228, -233,  519,    0,    0, -226, -231,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -139,
 -130,    0,    0,    0,  801,    0,    0,    0, -239,  677,
  290, -140,  812, -139,    0, -245,    0, -242, -177,  681,
  719, -209, -155, -172,  385,    0,    0,  -96, -199, -116,
 -109,  681, -129,    0,  284, -128, -124,    0,    0,    0,
    0,  723,  621,  621,  723,    0,    0,  681,    0,    0,
  785, -124,  565,    0,  366, -120,    0,  728,  746,  750,
  621,  755,  773,  576,  -46,  581,    0,   -8,  -57,   28,
   64, -106,  -29, -201, -223,  -19, -163,   15,    0,  -24,
    0,  336,    0,    0,  681,    0,  681,  777, -116,    0,
  125,    0, -253,  -76,    0, -163,    0,    0,  385,    0,
 -124,  -14, -245,    0,    0, -245, -163,  576,  -97,   29,
    0,  -32,    0,  -14, -245,  -14, -245,   -8, -163,    0,
    0,    0,    0,    0,   80,  581,    0,  411,   16,   65,
    0,    0,    0,   70,  111,  119, -185,  681,  681,  127,
 -172,    0,  -58,    0,    0,  238,  342,   94,  402,  159,
 -184,  120,    0,  -50,    0,    0,   86,    0,  101,  581,
    0,    0,  581,    0,  151,    0,  478,    0,    0,  152,
  153,  160,  166,  -38,  208,  267,  142,    0,    0,    0,
    0,    0,    0,    0,  156,    0,    0,    0,    0,    0,
  581,    0,  581,    0,  157,  189,    0,    0,  182,  183,
  184,  191,  197, -148,    0,    0,  594,    0,  -93,  192,
  209,    0,    0, -244,  193,  195,  198,  199,  207, -237,
  -31,  -68,  -15,    0,    0,    0,  611,  611,  611,  611,
  611,  611,  611,  611,  -60,  -55,  628,  214,  215,  190,
  486,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  628,  229,    0,    0,  231,  628,  681,    0,
    0,  496,  233,  628,    0,  235,  286,    0,    0,  243,
    0,  251,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  481,  121,
    0,    0,    0,  640,    0,    0,    0,  426,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -18,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  689,    0,   37,    0,  121,    0,    0,
    0,    0,    0,    0,  301,    0,    0,    0,  317, -119,
  445,    0,    0,    0,    0,    0,   54,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,  121,    0,    0,  -53,    0,    0,    0,    0,
    0,    0,    0,  121,    0,  260,    0,    0,    0,    0,
  353,    0,    0,    0,    0,    0,  274,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -119,    0,
    0,    0,    0,    0,    0,  377,    0,    0,  390,    0,
  126,    0,   73,    0,    0,  109,  523,  121,  121,    0,
    0,  328,    0,  145,  181,  217,  253,  536,  552,    0,
    0,    0,    0,    0,    0,  260,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  462,    0,
 -119,    0,    0,    0,    0,    0,    0,    0,    0,  260,
    0,    0,  260,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  277,  278,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  260,    0,  260,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  268,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  275,    0,    0,
};
final static short yygindex[] = {                         0,
    2,    0, -158,  691,  -72,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -48,  450,    0,    0,  391,
    0,   14,    0,  506,  469,   99,  570,  535,  -44, -146,
    0,  540,  840,    0,  416,    0,    0,  -39,
};
final static int YYTABLESIZE=1105;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        197,
   78,   99,  101,   95,    5,  107,    8,    9,   12,  195,
   91,  257,    4,  126,   10,  183,   10,   11,  264,   11,
   13,   12,   13,   44,   58,   50,   14,   52,   14,  137,
   79,   80,  168,  170,   92,   93,   63,   59,  140,    6,
   15,  149,   15,  225,   67,  258,  226,   94,   41,  155,
  184,   16,  265,   82,  166,   54,  117,   60,   51,  102,
   53,   57,  167,   82,   82,  169,  176,   82,  177,  179,
  203,  216,   66,  118,  240,   82,  241,    1,  252,  131,
   96,   97,  120,   98,  204,   82,   82,   82,   82,  119,
    2,   82,  266,  187,  189,  105,   64,   36,   37,   38,
  103,   82,   82,   82,   82,   82,   82,  249,   69,   39,
   35,   10,  292,  104,   11,   88,  106,   13,   89,  205,
  206,  250,  107,   14,   36,   37,   38,  130,   82,   10,
   82,   82,   11,   62,   12,   13,   39,   15,   64,  127,
   64,   14,   44,   40,   65,   41,   63,   87,   10,   44,
  120,   11,   65,   12,   13,   15,   66,  164,  188,   10,
   14,   63,   11,   10,   33,   13,   11,  143,   12,   13,
  133,   14,  114,  136,   15,   14,  115,  124,  125,  185,
   64,   82,   82,   34,   82,   15,  145,  147,   10,   15,
  116,   11,  186,   41,   13,  281,   10,  209,  254,   11,
   14,   10,   13,    9,   11,  219,    9,   13,   14,    9,
  210,  156,  157,   14,   15,    9,   68,  233,  220,  239,
  267,   88,   15,  268,   89,   10,  234,   15,   11,    9,
  271,   13,  161,  283,  165,    9,  284,   14,    9,  253,
  297,   10,   12,  293,   11,  269,   12,   13,  296,  173,
   57,   15,   67,   14,  300,  192,   78,   78,   78,   78,
   78,   78,   78,   78,   57,   73,   74,   15,   57,   78,
   72,   73,   74,   75,   78,   78,   78,   78,   78,   78,
   78,   78,   78,   78,   78,   78,  190,  191,   78,   78,
   78,   78,   78,   63,   63,   63,   63,   63,   63,   63,
   51,  171,   82,  198,  172,   63,   72,   73,   74,   75,
   63,   63,   63,   63,   63,   63,   25,  162,   63,   63,
   63,   63,   60,  199,   63,   63,   63,   63,   63,   66,
   66,   66,   66,   66,   66,   66,   60,  193,  194,  200,
   60,   66,   88,  221,  222,   89,   66,   66,   66,   66,
   66,   66,  132,  163,   66,   66,   66,   66,  223,  224,
   66,   66,   66,   66,   66,   69,   69,   69,   69,   69,
   69,   69,   72,   73,   74,   75,   49,   69,  111,  111,
  201,  213,   69,   69,   69,   69,   69,   69,  202,   50,
   69,   69,   69,   69,   58,  207,   69,   69,   69,   69,
   69,   65,   65,   65,   65,   65,   65,   65,   58,  227,
  218,  181,   58,   65,  182,  242,  229,  230,   65,   65,
   65,   65,   65,   65,  231,   15,   65,   65,   65,   65,
  232,  237,   65,   65,   65,   65,   65,   64,   64,   64,
   64,   64,   64,   64,   24,  181,  238,  243,  215,   64,
  255,  244,  245,  246,   64,   64,   64,   64,   64,   64,
  247,   31,   64,   64,   64,   64,  248,  256,   64,   64,
   64,   64,   64,   68,   68,   68,   68,   68,   68,   68,
    3,  290,  259,   56,  260,   68,   88,  261,  262,   89,
   68,   68,   68,   68,   68,   68,  263,  235,   68,   68,
   68,   68,  288,  289,   68,   68,   68,   68,   68,   67,
   67,   67,   67,   67,   67,   67,   88,  294,  115,   89,
  295,   67,  299,  113,  301,  211,   67,   67,   67,   67,
   67,   67,  303,   56,   67,   67,   67,   67,  304,  128,
   67,   67,   67,   67,   67,   88,   10,  138,   89,   11,
  142,  140,   13,   36,   37,   38,  236,   51,   14,   48,
   51,   51,   51,   51,   88,   39,   47,   89,  180,   51,
  129,  217,   15,   25,   83,  302,   25,   25,   25,   25,
  175,   84,   90,   51,   10,   25,  208,   10,   51,   51,
   10,  174,   51,    0,    0,    0,   10,  113,    0,   25,
    0,    0,    0,   25,   25,   36,   37,   38,   25,  132,
   10,    0,  132,  132,  132,  132,   10,   39,    0,   10,
   88,  132,   10,   89,    0,   11,    0,    0,   13,  212,
    0,    0,    0,   49,   14,  132,   49,   49,   49,   49,
  132,  132,  134,  135,  132,   49,   50,    0,   15,   50,
   50,   50,   50,    0,   36,   37,   38,  141,   50,   49,
  150,  152,  154,    0,   49,   49,   39,   10,   49,    0,
   11,  112,   50,   13,    0,    0,    0,   50,   50,   14,
   88,   50,   15,   89,    0,   15,   15,   15,   15,  214,
    0,    0,    0,   15,   15,    0,    0,   18,   18,   18,
   42,   24,  196,    0,   24,   24,   24,   24,   15,    0,
    0,    0,    0,   24,    0,    0,    0,   15,   31,    0,
    0,   31,   31,   31,   31,    0,    0,   24,    0,    0,
   31,   86,    0,    0,   10,  286,   24,   11,   42,    0,
   13,    0,   10,    0,   31,   11,   14,    0,   13,    0,
    0,  286,   10,   31,   14,   11,  286,    0,   13,    0,
   15,    0,  286,    0,   14,    0,    0,    0,   15,  228,
    0,    0,    0,   42,    0,  142,    0,  291,   15,  100,
  100,  100,  100,    0,   42,  100,  159,  298,   36,   37,
   38,  100,  101,  101,  101,  101,    0,    0,  101,    0,
   39,    0,    0,    0,  101,  100,    0,    0,   99,   99,
   99,   99,  100,  100,   99,    0,    0,    0,  101,    0,
   99,   10,    0,    0,   11,  101,  101,   13,   42,   42,
    0,    0,   10,   14,   99,   11,    0,   10,   13,    0,
   11,   99,   99,   13,   14,    0,  159,   15,   86,   14,
   10,    0,    0,   11,  139,   41,   13,    0,   15,    0,
    0,    0,   14,   15,    0,    0,   41,   10,    0,    0,
   11,  158,    0,   13,    0,    0,   15,    0,    0,   14,
  159,    0,    0,  159,  251,    0,    0,  142,    0,   64,
   36,   37,   38,   15,    0,   61,  285,   36,   37,   38,
    0,  271,   39,    0,    0,    0,    0,    0,   61,   39,
    0,  159,   61,  159,   61,   61,   61,   61,   61,   61,
   61,   61,   61,   61,   61,   61,   61,   86,    0,   18,
    0,    0,   81,    0,    0,    0,   98,    0,    0,    0,
    0,   86,  142,   18,   78,   64,   36,   37,   38,   64,
   36,   37,   38,    0,    0,    0,  142,   57,   39,    0,
    0,   86,   39,   78,   78,   78,   78,   78,   78,   78,
   78,   57,   78,   78,  100,   57,    0,    0,  132,    0,
    0,    0,  142,  144,    0,    0,    0,   64,   36,   37,
   38,   64,   36,   37,   38,    0,   64,   36,   37,   38,
   39,  146,    0,    0,   39,  148,    0,    0,    0,   39,
  151,    0,    0,    0,   64,   36,   37,   38,   64,   36,
   37,   38,    0,   64,   36,   37,   38,   39,  153,    0,
    0,   39,  178,    0,    0,    0,   39,    0,    0,    0,
    0,   64,   36,   37,   38,   64,   36,   37,   38,    0,
    0,    0,    0,    0,   39,    0,    0,    0,   39,   68,
   69,   70,   71,   72,   73,   74,   75,    0,   76,   77,
    0,    0,    0,    0,  138,   68,   69,   70,   71,   72,
   73,   74,   75,    0,   76,   77,   68,   69,   70,   71,
   88,    0,    0,   89,    0,   76,   77,  274,  275,  276,
  277,  278,  279,  280,  282,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        158,
    0,   50,   51,   48,  256,   54,    5,    6,  262,  156,
  256,  256,  291,   62,  257,  269,  257,  260,  256,  260,
  263,  262,  263,   10,  256,  256,  269,  256,  269,   78,
  270,  271,  256,  106,  280,  281,    0,  269,   83,  291,
  283,   90,  283,  190,   31,  290,  193,  290,  291,   94,
  123,  292,  290,   40,  256,  289,  256,  289,  289,  269,
  289,  288,  264,   50,   51,  289,  115,   54,  117,  118,
  256,  256,    0,  273,  221,   62,  223,  256,  237,   66,
  258,  259,  267,  256,  270,   72,   73,   74,   75,  289,
  269,   78,  251,  138,  139,  268,  269,  270,  271,  272,
  256,   88,   89,   90,   91,   92,   93,  256,    0,  282,
  256,  257,  271,  269,  260,  279,  289,  263,  282,  168,
  169,  270,  171,  269,  270,  271,  272,  256,  115,  257,
  117,  118,  260,  273,  262,  263,  282,  283,  269,  269,
  269,  269,  262,  289,    0,  291,  286,  288,  257,  269,
  267,  260,  283,  262,  263,  283,  287,  264,  256,  257,
  269,  286,  260,  257,  292,  263,  260,  288,  262,  263,
   72,  269,  269,   75,  283,  269,  273,  287,  288,  256,
    0,  168,  169,  292,  171,  283,   88,   89,  257,  283,
  287,  260,  269,  291,  263,  256,  257,  256,  292,  260,
  269,  257,  263,  257,  260,  256,  260,  263,  269,  263,
  269,  258,  259,  269,  283,  269,    0,  256,  269,  218,
  289,  279,  283,  292,  282,  257,  265,  283,  260,  283,
  291,  263,  290,  289,  264,  289,  292,  269,  292,  238,
  289,  257,  262,  283,  260,  261,  262,  263,  288,  274,
  269,  283,    0,  269,  294,  288,  256,  257,  258,  259,
  260,  261,  262,  263,  283,  280,  281,  283,  287,  269,
  279,  280,  281,  282,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  258,  259,  288,  289,
  290,  291,  292,  257,  258,  259,  260,  261,  262,  263,
    0,  287,  289,  288,  290,  269,  279,  280,  281,  282,
  274,  275,  276,  277,  278,  279,    0,  290,  282,  283,
  284,  285,  269,  259,  288,  289,  290,  291,  292,  257,
  258,  259,  260,  261,  262,  263,  283,  258,  259,  270,
  287,  269,  279,  258,  259,  282,  274,  275,  276,  277,
  278,  279,    0,  290,  282,  283,  284,  285,  258,  259,
  288,  289,  290,  291,  292,  257,  258,  259,  260,  261,
  262,  263,  279,  280,  281,  282,    0,  269,  258,  259,
  270,  288,  274,  275,  276,  277,  278,  279,  270,    0,
  282,  283,  284,  285,  269,  269,  288,  289,  290,  291,
  292,  257,  258,  259,  260,  261,  262,  263,  283,  259,
  291,  287,  287,  269,  290,  259,  265,  265,  274,  275,
  276,  277,  278,  279,  265,    0,  282,  283,  284,  285,
  265,  290,  288,  289,  290,  291,  292,  257,  258,  259,
  260,  261,  262,  263,    0,  287,  291,  259,  290,  269,
  259,  270,  270,  270,  274,  275,  276,  277,  278,  279,
  270,    0,  282,  283,  284,  285,  270,  259,  288,  289,
  290,  291,  292,  257,  258,  259,  260,  261,  262,  263,
    0,  292,  290,   15,  290,  269,  279,  290,  290,  282,
  274,  275,  276,  277,  278,  279,  290,  290,  282,  283,
  284,  285,  289,  289,  288,  289,  290,  291,  292,  257,
  258,  259,  260,  261,  262,  263,  279,  289,  259,  282,
  290,  269,  290,   55,  290,  288,  274,  275,  276,  277,
  278,  279,  290,   65,  282,  283,  284,  285,  288,  256,
  288,  289,  290,  291,  292,  279,  257,  274,  282,  260,
  274,  274,  263,  270,  271,  272,  290,  257,  269,  292,
  260,  261,  262,  263,  279,  282,  292,  282,  119,  269,
   65,  181,  283,  257,   40,  290,  260,  261,  262,  263,
  112,  292,   43,  283,  257,  269,  171,  260,  288,  289,
  263,  256,  292,   -1,   -1,   -1,  269,  129,   -1,  283,
   -1,   -1,   -1,  287,  288,  270,  271,  272,  292,  257,
  283,   -1,  260,  261,  262,  263,  289,  282,   -1,  292,
  279,  269,  257,  282,   -1,  260,   -1,   -1,  263,  288,
   -1,   -1,   -1,  257,  269,  283,  260,  261,  262,  263,
  288,  289,   73,   74,  292,  269,  257,   -1,  283,  260,
  261,  262,  263,   -1,  270,  271,  272,  292,  269,  283,
   91,   92,   93,   -1,  288,  289,  282,  257,  292,   -1,
  260,  287,  283,  263,   -1,   -1,   -1,  288,  289,  269,
  279,  292,  257,  282,   -1,  260,  261,  262,  263,  288,
   -1,   -1,   -1,  283,  269,   -1,   -1,    7,    8,    9,
   10,  257,  292,   -1,  260,  261,  262,  263,  283,   -1,
   -1,   -1,   -1,  269,   -1,   -1,   -1,  292,  257,   -1,
   -1,  260,  261,  262,  263,   -1,   -1,  283,   -1,   -1,
  269,   41,   -1,   -1,  257,  267,  292,  260,   48,   -1,
  263,   -1,  257,   -1,  283,  260,  269,   -1,  263,   -1,
   -1,  283,  257,  292,  269,  260,  288,   -1,  263,   -1,
  283,   -1,  294,   -1,  269,   -1,   -1,   -1,  283,  292,
   -1,   -1,   -1,   83,   -1,   85,   -1,  292,  283,  257,
  258,  259,  260,   -1,   94,  263,   96,  292,  270,  271,
  272,  269,  257,  258,  259,  260,   -1,   -1,  263,   -1,
  282,   -1,   -1,   -1,  269,  283,   -1,   -1,  257,  258,
  259,  260,  290,  291,  263,   -1,   -1,   -1,  283,   -1,
  269,  257,   -1,   -1,  260,  290,  291,  263,  138,  139,
   -1,   -1,  257,  269,  283,  260,   -1,  257,  263,   -1,
  260,  290,  291,  263,  269,   -1,  156,  283,  158,  269,
  257,   -1,   -1,  260,  290,  291,  263,   -1,  283,   -1,
   -1,   -1,  269,  283,   -1,   -1,  291,  257,   -1,   -1,
  260,  291,   -1,  263,   -1,   -1,  283,   -1,   -1,  269,
  190,   -1,   -1,  193,  291,   -1,   -1,  197,   -1,  269,
  270,  271,  272,  283,   -1,  256,  269,  270,  271,  272,
   -1,  291,  282,   -1,   -1,   -1,   -1,   -1,  269,  282,
   -1,  221,  273,  223,  275,  276,  277,  278,  279,  280,
  281,  282,  283,  284,  285,  286,  287,  237,   -1,  239,
   -1,   -1,  256,   -1,   -1,   -1,  256,   -1,   -1,   -1,
   -1,  251,  252,  253,  256,  269,  270,  271,  272,  269,
  270,  271,  272,   -1,   -1,   -1,  266,  269,  282,   -1,
   -1,  271,  282,  275,  276,  277,  278,  279,  280,  281,
  282,  283,  284,  285,  256,  287,   -1,   -1,  256,   -1,
   -1,   -1,  292,  256,   -1,   -1,   -1,  269,  270,  271,
  272,  269,  270,  271,  272,   -1,  269,  270,  271,  272,
  282,  256,   -1,   -1,  282,  256,   -1,   -1,   -1,  282,
  256,   -1,   -1,   -1,  269,  270,  271,  272,  269,  270,
  271,  272,   -1,  269,  270,  271,  272,  282,  256,   -1,
   -1,  282,  256,   -1,   -1,   -1,  282,   -1,   -1,   -1,
   -1,  269,  270,  271,  272,  269,  270,  271,  272,   -1,
   -1,   -1,   -1,   -1,  282,   -1,   -1,   -1,  282,  275,
  276,  277,  278,  279,  280,  281,  282,   -1,  284,  285,
   -1,   -1,   -1,   -1,  290,  275,  276,  277,  278,  279,
  280,  281,  282,   -1,  284,  285,  275,  276,  277,  278,
  279,   -1,   -1,  282,   -1,  284,  285,  258,  259,  260,
  261,  262,  263,  264,  265,
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
"prog : ID error bloque LLAVEFIN",
"prog : ID LLAVEINIC bloque",
"prog : error LLAVEINIC bloque LLAVEFIN",
"bloque :",
"bloque : bloque sentencia",
"bloque_ejecutable : bloque_ejecutable sentencia_ejec PUNTOYCOMA",
"bloque_ejecutable : sentencia_ejec PUNTOYCOMA",
"bloque_ejecutable : sentencia_ejec",
"bloque_ejecutable : bloque_ejecutable sentencia_ejec",
"tipo : INT",
"sentencia : declaracion_variable",
"sentencia : declaracion_con_asignacion",
"sentencia : sentencia_ejec PUNTOYCOMA",
"sentencia : sentencia_ejec",
"sentencia : declaracion_funcion",
"sentencia_ejec : asign_simple",
"sentencia_ejec : asign_multiple",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : sentencia_print",
"sentencia_ejec : llamada_funcion",
"declaracion_variable : tipo lista_ids PUNTOYCOMA",
"declaracion_variable : tipo lista_ids",
"lista_ids : ID",
"lista_ids : lista_ids COMA ID",
"lista_ids : lista_ids COMA error",
"lista_ids : error COMA",
"lista_ids : error ID",
"declaracion_con_asignacion : tipo ID ASIGN expresion PUNTOYCOMA",
"declaracion_con_asignacion : tipo ID ASIGN expresion",
"declaracion_con_asignacion : tipo ID ASIGN error PUNTOYCOMA",
"declaracion_con_asignacion : tipo ID error expresion PUNTOYCOMA",
"declaracion_con_asignacion : tipo error ASIGN expresion PUNTOYCOMA",
"declaracion_funcion : tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque return_sent LLAVEFIN",
"declaracion_funcion : declaracion_funcion_err",
"declaracion_funcion_err : tipo PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
"lista_params_formales : param_formal",
"lista_params_formales : lista_params_formales COMA param_formal",
"lista_params_formales : lista_params_formales COMA error",
"param_formal : sem_pasaje_opt tipo ID",
"param_formal : sem_pasaje_opt tipo error",
"param_formal : sem_pasaje_opt ID",
"sem_pasaje_opt :",
"sem_pasaje_opt : CV",
"return_sent : RETURN PARENTINIC expresion PARENTFIN PUNTOYCOMA",
"return_sent : RETURN PARENTINIC expresion PARENTFIN",
"return_sent :",
"asign_simple : var_ref ASIGN expresion",
"asign_multiple : lista_vars IGUALUNICO lista_ctes",
"asign_multiple : IGUALUNICO lista_ctes",
"asign_multiple : lista_vars IGUALUNICO error",
"lista_ctes : cte",
"lista_ctes : lista_ctes COMA cte",
"lista_ctes : lista_ctes COMA error",
"lista_ctes : lista_ctes cte",
"lista_vars : var_ref",
"lista_vars : lista_vars COMA var_ref",
"lista_vars : lista_vars COMA error",
"lista_vars : lista_vars var_ref",
"var_ref : ID",
"var_ref : var_ref PUNTO ID",
"expresion : termino",
"expresion : expresion MAS termino",
"expresion : expresion MAS error",
"expresion : error MAS termino",
"expresion : expresion MENOS termino",
"expresion : expresion MENOS error",
"expresion : error MENOS termino",
"termino : factor",
"termino : termino MUL factor",
"termino : termino MUL error",
"termino : error MUL factor",
"termino : termino DIV factor",
"termino : termino DIV error",
"termino : error DIV factor",
"termino : termino error factor",
"factor : var_ref",
"factor : cte",
"cte : CTEFLOAT",
"cte : MENOS CTEFLOAT",
"cte : CTEINT",
"cte : MENOS CTEINT",
"cte : CTESTR",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if ENDIF",
"bloque_if : IF PARENTINIC condicion PARENTFIN rama_if ELSE rama_else ENDIF",
"bloque_if : bloque_if_error",
"bloque_if_error : IF condicion PARENTFIN rama_if ENDIF",
"bloque_if_error : IF PARENTINIC condicion rama_if ENDIF",
"bloque_if_error : IF condicion rama_if ENDIF",
"bloque_if_error : IF condicion PARENTFIN rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF PARENTINIC condicion rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF condicion rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF rama_if ENDIF",
"bloque_if_error : IF rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF PARENTINIC error PARENTFIN rama_if ENDIF",
"bloque_if_error : IF PARENTINIC error PARENTFIN rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF PARENTINIC condicion PARENTFIN error",
"condicion : expresion op_relacion expresion",
"condicion : error op_relacion expresion",
"condicion : expresion op_relacion error",
"op_relacion : MENOR",
"op_relacion : MAYOR",
"op_relacion : IGUAL",
"op_relacion : DISTINTO",
"op_relacion : MENORIGUAL",
"op_relacion : MAYORIGUAL",
"rama_if : sentencia_ejec PUNTOYCOMA",
"rama_if : LLAVEINIC bloque_ejecutable LLAVEFIN",
"rama_if : LLAVEINIC LLAVEFIN",
"rama_if :",
"rama_else : sentencia_ejec PUNTOYCOMA",
"rama_else : LLAVEINIC bloque_ejecutable LLAVEFIN",
"rama_else : LLAVEINIC LLAVEFIN",
"rama_else :",
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
"rama_for : sentencia_ejec",
"rama_for : LLAVEINIC bloque_ejecutable LLAVEFIN",
"rama_for : LLAVEINIC LLAVEFIN",
"sentencia_print : PRINT PARENTINIC expresion PARENTFIN",
"sentencia_print : PRINT PARENTINIC error PARENTFIN",
"sentencia_print : PRINT error expresion PARENTFIN",
"sentencia_print : PRINT PARENTINIC expresion",
"llamada_funcion : ID PARENTINIC lista_params_reales PARENTFIN",
"lista_params_reales : param_real_map",
"lista_params_reales : lista_params_reales COMA param_real_map",
"param_real_map : parametro_real FLECHA ID",
"param_real_map : parametro_real FLECHA error",
"parametro_real : expresion",
"parametro_real : TRUNC PARENTINIC expresion PARENTFIN",
"parametro_real : TRUNC PARENTINIC expresion",
"parametro_real : TRUNC error expresion PARENTFIN",
"parametro_real : TRUNC error expresion",
"parametro_real : lambda_expr",
"lambda_expr : PARENTINIC tipo ID PARENTFIN LLAVEINIC bloque_ejecutable LLAVEFIN PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC tipo ID PARENTFIN bloque_ejecutable LLAVEFIN PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC tipo ID PARENTFIN LLAVEINIC bloque_ejecutable PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC tipo ID PARENTFIN bloque_ejecutable PARENTINIC argumento PARENTFIN",
"argumento : ID",
"argumento : cte",
};

//#line 386 "gramaticaDeCero.y"

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;
static TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
static int n_var = 0; //para contar variables en asignaciones multiples
static int n_cte = 0; //para contar ctes en asignaciones multiples

public static Tokens_out TOKENS;
public static TS_out TS;
public static SintaxStruct_out SINT;
public static Errors_out ERR;

public static void logLexError(String mensaje){
    int linea = (lex != null) ? lex.getLineaActual() : -1;
    System.err.println("Error lexico en linea " + linea + ": " + mensaje);
    if (ERR != null) ERR.add(linea, "Error lexico: " + mensaje);
}

public static void logLexWarnAt(int linea, String mensaje){
    System.out.println("Warning en linea " + linea + ": " + mensaje);
    if (ERR != null) ERR.add(linea, "Warning: " + mensaje);
}

public static void main (String [] args) {
    try {
        System.out.println("Iniciando compilacion...");
        TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();

        Path fuente = Paths.get(args[0]);

        TOKENS = new Tokens_out(Generador_out.rutaLexico(fuente, false));
        TS     = new TS_out(Generador_out.rutaTabla(fuente, false));
        SINT   = new SintaxStruct_out(Generador_out.rutaSintactico(fuente, false));
        ERR    = new Errors_out(Generador_out.rutaErrores(fuente, false));

        // correr analisis
        lex = new AnalizadorLexico(args[0]);
        par = new Parser(false);
        par.run();

        TS.dump(tablaSimbolos);

        System.out.println("Fin compilacion");
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        // Cierra en orden; SintaxStruct_out escribe el header y la tabla en close()
        try { if (SINT   != null) SINT.close(); }   catch (Exception ignored) {}
        try { if (TS     != null) TS.close(); }     catch (Exception ignored) {}
        try { if (TOKENS != null) TOKENS.close(); } catch (Exception ignored) {}
        try { if (ERR    != null) ERR.close(); }   catch (Exception ignored) {}
    }
}



int yylex (){
    Token token = null;
    if ((token = lex.getToken()) != null) {
        yylval = new ParserVal(token.getEntradaTS());

        // registrar token
        if (TOKENS != null) {
            int lineaTok = lex.getLineaActual();
            TOKENS.append(token, lineaTok);
        }
        return token.getIDToken();
    } else {
        return 0; // no hay más tokens
    }
}

void yyerror(String mensaje){
    if ("syntax error".equals(mensaje)) return;  // suprime el genérico
	int linea = lex.getLineaActual();
    System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
	if (ERR != null) ERR.add(linea, "Error sintactico: " + mensaje);

}
//#line 779 "Parser.java"
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
case 2:
//#line 64 "gramaticaDeCero.y"
{yyerror("Falta '{' en declaración de programa");}
break;
case 3:
//#line 65 "gramaticaDeCero.y"
{yyerror("Falta '}' en declaración de programa");}
break;
case 4:
//#line 66 "gramaticaDeCero.y"
{yyerror("Falta identificador en declaración de programa");}
break;
case 9:
//#line 77 "gramaticaDeCero.y"
{yyerror("Falta ';' al final de la sentencia.");}
break;
case 10:
//#line 78 "gramaticaDeCero.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 15:
//#line 89 "gramaticaDeCero.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 23:
//#line 103 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable"); }
break;
case 24:
//#line 104 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 27:
//#line 110 "gramaticaDeCero.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 28:
//#line 111 "gramaticaDeCero.y"
{yyerror("Identificador invalido");}
break;
case 29:
//#line 112 "gramaticaDeCero.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 30:
//#line 116 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
break;
case 31:
//#line 117 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
break;
case 32:
//#line 118 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, expresión inválida."); }
break;
case 33:
//#line 119 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
break;
case 34:
//#line 120 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
break;
case 35:
//#line 124 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
break;
case 37:
//#line 128 "gramaticaDeCero.y"
{yyerror("Error sintáctico: Falta nombre identificador de función");}
break;
case 40:
//#line 139 "gramaticaDeCero.y"
{yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
break;
case 42:
//#line 145 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de tipo en parámetro formal");}
break;
case 43:
//#line 146 "gramaticaDeCero.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 46:
//#line 153 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Return"); }
break;
case 47:
//#line 154 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 49:
//#line 161 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Asignacion simple"); }
break;
case 50:
//#line 166 "gramaticaDeCero.y"
{ 
                    /*System.out.println("n_var: " + n_var + ", n_cte: " + n_cte);*/
                    if (n_var == 1 && n_cte == 1) {
                        yyerror("Error sintactico: para asignación simple use ':=' en lugar de '='");
                    } else {
                        if (n_var < n_cte) {
                            yyerror("Error sintactico: más constantes que variables en la asignación");
                        } else {
                            /* System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");*/
                            SINT.add(lex.getLineaActual(), "Asignacion multiple");
                        }
                    }
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 51:
//#line 180 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de variables antes del '='"); }
break;
case 52:
//#line 181 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de constantes después del '='");}
break;
case 53:
//#line 186 "gramaticaDeCero.y"
{n_cte++;}
break;
case 54:
//#line 187 "gramaticaDeCero.y"
{n_cte++;}
break;
case 55:
//#line 188 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una constante después de coma");}
break;
case 56:
//#line 189 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
break;
case 57:
//#line 192 "gramaticaDeCero.y"
{n_var++;}
break;
case 58:
//#line 193 "gramaticaDeCero.y"
{n_var++;}
break;
case 59:
//#line 194 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta identificador después de coma");}
break;
case 60:
//#line 195 "gramaticaDeCero.y"
{yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
break;
case 65:
//#line 206 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 66:
//#line 207 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresión."); }
break;
case 68:
//#line 209 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 69:
//#line 210 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresión."); }
break;
case 72:
//#line 217 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 73:
//#line 218 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '*' en expresión."); }
break;
case 75:
//#line 220 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 76:
//#line 221 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresión."); }
break;
case 77:
//#line 222 "gramaticaDeCero.y"
{ yyerror("Falta operador entre factores en expresión."); }
break;
case 81:
//#line 234 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 82:
//#line 241 "gramaticaDeCero.y"
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
case 83:
//#line 255 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 85:
//#line 268 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 86:
//#line 270 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 88:
//#line 276 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 89:
//#line 277 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 90:
//#line 278 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 91:
//#line 280 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 92:
//#line 281 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 93:
//#line 282 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 94:
//#line 284 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 95:
//#line 285 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 96:
//#line 286 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 97:
//#line 287 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 98:
//#line 288 "gramaticaDeCero.y"
{yyerror("Falta bloque del if");}
break;
case 100:
//#line 293 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 101:
//#line 294 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 110:
//#line 309 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 111:
//#line 310 "gramaticaDeCero.y"
{yyerror("Falta bloque del then");}
break;
case 114:
//#line 315 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 115:
//#line 316 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 116:
//#line 321 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 117:
//#line 322 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 118:
//#line 323 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 119:
//#line 324 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 120:
//#line 325 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 121:
//#line 326 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 122:
//#line 327 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 123:
//#line 328 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 124:
//#line 329 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 125:
//#line 330 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 128:
//#line 336 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 129:
//#line 341 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 130:
//#line 342 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 131:
//#line 343 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 132:
//#line 344 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 133:
//#line 351 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 137:
//#line 361 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 139:
//#line 365 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 140:
//#line 366 "gramaticaDeCero.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 141:
//#line 367 "gramaticaDeCero.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 142:
//#line 368 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 144:
//#line 374 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Lambda");}
break;
case 145:
//#line 375 "gramaticaDeCero.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 146:
//#line 376 "gramaticaDeCero.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 147:
//#line 377 "gramaticaDeCero.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1334 "Parser.java"
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
