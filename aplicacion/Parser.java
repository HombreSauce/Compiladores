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
    0,    0,    0,    0,    0,    1,    1,    3,    3,    3,
    3,    5,    2,    2,    2,    2,    2,    4,    4,    4,
    4,    4,    4,    6,    6,    6,   15,   15,   15,    7,
    7,    7,    7,    7,    8,    8,   19,   17,   17,   17,
   20,   20,   20,   21,   21,   18,   18,   18,    9,   10,
   10,   10,   24,   24,   24,   24,   23,   23,   23,   23,
   22,   22,   16,   16,   16,   16,   16,   16,   16,   26,
   26,   26,   26,   26,   26,   26,   27,   27,   25,   25,
   25,   25,   25,   11,   11,   11,   31,   31,   31,   31,
   31,   31,   31,   31,   31,   31,   31,   31,   31,   31,
   28,   28,   28,   28,   32,   32,   32,   32,   32,   32,
   29,   29,   29,   30,   30,   30,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   33,   33,   33,   13,
   13,   13,   13,   14,   34,   34,   35,   35,   36,   36,
   36,   36,   36,   36,   37,   37,   37,   37,   38,   38,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    5,    0,    2,    3,    2,    1,
    2,    1,    1,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    2,    2,    1,    3,    3,    5,
    4,    5,    5,    5,    9,    1,    7,    1,    3,    3,
    3,    3,    2,    0,    1,    5,    4,    0,    3,    3,
    2,    3,    1,    3,    3,    2,    1,    3,    3,    2,
    1,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    1,    1,    1,    2,
    1,    2,    1,    6,    8,    1,    5,    5,    4,    6,
    7,    7,    6,    8,    3,    5,    6,    8,    5,    7,
    3,    3,    3,    3,    1,    1,    1,    1,    1,    1,
    2,    3,    2,    2,    3,    2,    9,    8,    8,    8,
    8,    8,    8,    8,    7,    9,    1,    3,    2,    4,
    4,    3,    3,    4,    1,    3,    3,    3,    1,    4,
    3,    3,    2,    1,   10,    9,    9,    8,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    6,    0,    6,    0,    0,    0,    0,    0,   12,
    0,    0,    0,    2,    7,    0,    0,   13,   14,   17,
   18,   19,   20,   21,   22,   23,   36,    0,    0,   86,
    4,    0,    0,   81,   79,   83,    0,    0,    0,    0,
    0,    0,   78,    0,   70,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,    0,   53,   15,    0,    0,
   26,    0,    0,    0,    0,    0,    0,    0,    5,  107,
  108,  109,  110,    0,    0,    0,    0,  106,  105,    0,
   82,   80,    0,    0,  113,    0,    0,  111,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   95,    0,    0,
  132,    0,    0,    0,    0,    0,    0,    0,  135,    0,
  144,    0,   56,    0,    0,    0,    0,   45,    0,   38,
    0,    0,   24,    0,   62,   52,    0,   59,    0,    0,
    0,   73,   76,    0,    0,    0,    0,    0,  112,    0,
    9,    0,    0,    0,    0,    0,    0,    0,   72,   71,
   75,   74,    0,    0,   89,    0,    0,    0,  131,  130,
    0,    0,    0,    0,    0,    0,    0,    0,  134,    0,
   55,   54,    0,    0,    0,    0,    0,    0,    0,   43,
    0,   29,   28,    0,   99,    0,    0,   88,    8,    0,
   87,    0,  116,    0,  114,   96,    0,    0,    0,    0,
    0,    0,  142,    0,  136,  138,  137,   34,   33,   32,
   30,    0,   40,   39,    6,   42,   41,    0,   97,   90,
    0,   84,    0,    0,   93,  115,    0,    0,    0,    0,
    0,    0,  140,    0,    6,    0,    0,  100,    0,   92,
   91,    0,    0,  127,  125,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   37,   98,   94,   85,  118,  129,
    0,  119,  121,    0,  124,  123,  122,  120,    0,    0,
    0,    0,    0,  128,  126,  117,    0,    0,  149,  150,
    0,    0,    0,   35,    0,    0,  148,    0,    0,  147,
    0,  146,    0,  145,   46,
};
final static short yydgoto[] = {                          3,
    5,   15,   86,  244,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   63,   41,  119,  273,   27,  120,
  121,   28,   29,   56,   43,   44,   45,   46,   47,  158,
   30,   80,  245,  108,  109,  110,  111,  281,
};
final static short yysindex[] = {                      -244,
 -238,    0,    0,    0, -242, -147, -132, -226,  662,    0,
 -263, -216,  310,    0,    0, -181, -237,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -251, -221,    0,
    0, -168,  865,    0,    0,    0,  -60,  363,  314, -171,
  682, -251,    0,  -27,    0,  507,    9,   61,    0,  479,
 -274, -159, -122, -101,  653,  836,    0,    0,  -95, -249,
    0,  -50,  -15,  569,   -3,  -51, -246, -159,    0,    0,
    0,    0,    0,  762,  411,  411,  762,    0,    0,  569,
    0,    0,  849,  509,    0,  446,   17,    0,  569,  766,
  770,  787,  791,  795,  602,   45,  615,    0,   29,  -38,
    0,   18,   27, -209,  683,   56, -265, -200,    0,   59,
    0,  815,    0,  569,  569,  812,  -50,    0, -164,    0,
  -89, -192,    0, -265,    0,    0,  836,    0, -159,   44,
  -27,    0,    0,  -27, -265,  602, -117,   88,    0,   46,
    0, -265,   44,  -27,   44,  -27,   61, -265,    0,    0,
    0,    0,  118,  615,    0,  453,   73,  111,    0,    0,
   97,  106,  -88,  113,  569,   66,  129,  653,    0,  -81,
    0,    0,  208,  245,  171,  282, -149, -135,   89,    0,
  -57,    0,    0,  125,    0,  198,  615,    0,    0,  615,
    0,  148,    0,  454,    0,    0,  138,  143,  144,  -30,
  145,  103,    0,  109,    0,    0,    0,    0,    0,    0,
    0,  126,    0,    0,    0,    0,    0,  615,    0,    0,
 -102,    0,  161,  162,    0,    0,  520,  146,  154, -261,
  155,  168,    0,  617,    0,  -98,  176,    0,  -22,    0,
    0,  657,  471,    0,    0,  657,  657,  544,  657,  657,
  657,  737, -113,  -14,    0,    0,    0,    0,    0,    0,
  499,    0,    0,  -65,    0,    0,    0,    0,  -67,  633,
  177,  178,  180,    0,    0,    0,  633,  184,    0,    0,
  191,  633,  569,    0,  192,  633,    0,  199,  140,    0,
  201,    0,  200,    0,    0,
};
final static short yyrindex[] = {                         0,
  -54,    0,    0,    0,    0,    0,  493,    0,    0,    0,
    0,  700,    0,    0,    0,  392,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -211,    0,    0,
    0,  494,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  732,    0,   38,    0,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,  296,    0,    0,    0,  306,
    0,  -62,  416,    0,    0,    0,    0,  -86,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -33,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  339,
    0,    0,    0,    0,    0,    0,  221,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -62,    0,    0,    0,
    0,    0,    0,  355,    0,    0,  379,    0,  -55,    0,
   75,    0,    0,  112,  559,    0,    0,    0,    0,  -18,
    0,  563,  149,  186,  223,  260,  598,  600,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  229,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  429,    0,  -62,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  230,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  217,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  218,    0,    0,
};
final static short yygindex[] = {                         0,
   -2,    0, -152,  658,    8,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   19,  401,    0,    0,  341,
    0,    4,    0,  459,  547,   15,   10,  488,  -35, -151,
    0,  487,  853,    0,  362,    0,    0, -129,
};
final static int YYTABLESIZE=1150;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          6,
   77,    7,  192,  194,   90,   53,  115,   91,  248,  128,
   96,   42,   52,   90,    8,  101,   91,    9,   59,   10,
   11,   64,   49,  116,    1,   54,   12,   51,  249,   33,
    8,   60,   68,    9,   65,  223,   11,   63,  224,  117,
   13,   52,   12,   34,   35,   36,    2,   49,  138,   14,
   61,   62,    4,   52,  163,   37,   13,   57,   52,  153,
  164,   66,   38,  182,   39,   67,  237,   52,  100,  239,
  129,   57,   55,  107,   66,   57,  183,   52,   52,   52,
   52,  253,  124,   52,  132,  133,  168,   69,  131,  169,
  261,  134,   52,   52,   52,   52,   52,   52,  135,  269,
  184,  186,  150,  152,  144,  146,   58,  142,   52,    8,
  148,   69,    9,  167,   10,   11,   88,   52,   52,   52,
  213,   12,  178,  166,    8,  179,   65,    9,  181,   10,
   11,  118,  173,  174,  176,   13,   12,  178,  185,    8,
  212,  102,    9,    8,   31,   11,    9,  285,   65,   11,
   13,   12,  288,  238,    8,   12,  291,    9,    8,   32,
   11,    9,  103,   10,   11,   13,   12,  104,   52,   13,
   12,   52,   10,   39,  206,  270,  199,  114,  271,  180,
   13,  200,   60,  202,   13,   64,  107,  207,  156,    8,
  275,    8,    9,  255,    9,   11,   60,   11,  216,   44,
   60,   12,    6,   12,  126,    6,   44,    6,    6,   81,
   82,  217,  236,   58,    6,   13,  118,   13,   34,   35,
   36,  277,   68,   10,  278,  243,   10,   58,    6,   10,
   37,   58,  254,  257,  230,   10,  258,    6,   11,  231,
   90,   11,    8,   91,   11,    9,  272,   10,   11,   10,
   11,  160,   93,   94,   12,   10,   77,   77,   10,   67,
   77,   77,   77,   77,   11,  125,   97,   98,   13,   77,
   11,  122,  123,   11,   77,   77,   77,   77,   77,   77,
   77,   77,   77,   77,   77,   77,   52,  161,   77,   77,
   77,   77,   77,   63,   63,   51,  162,   63,   63,   63,
   63,  289,  154,  155,  141,   27,   63,   74,   75,   76,
   77,   63,   63,   63,   63,   63,   63,   10,  159,   63,
   63,   63,   63,   75,   76,   63,   63,   63,   63,   63,
   66,   66,  170,  189,   66,   66,   66,   66,  133,   74,
   75,   76,   77,   66,   90,  187,  188,   91,   66,   66,
   66,   66,   66,   66,   49,  203,   66,   66,   66,   66,
  195,  197,   66,   66,   66,   66,   66,   69,   69,  196,
  198,   69,   69,   69,   69,  190,  191,  201,   50,  215,
   69,   90,  218,  219,   91,   69,   69,   69,   69,   69,
   69,   16,  233,   69,   69,   69,   69,  204,  234,   69,
   69,   69,   69,   69,   65,   65,  225,  227,   65,   65,
   65,   65,  228,  229,  232,   25,  235,   65,   90,  240,
  241,   91,   65,   65,   65,   65,   65,   65,   31,  293,
   65,   65,   65,   65,  256,  246,   65,   65,   65,   65,
   65,   64,   64,  247,  250,   64,   64,   64,   64,   74,
   75,   76,   77,  220,   64,  221,  222,  251,  210,   64,
   64,   64,   64,   64,   64,  282,  283,   64,   64,   64,
   64,  284,  286,   64,   64,   64,   64,   64,   68,   68,
  287,  290,   68,   68,   68,   68,   90,  295,  292,   91,
  294,   68,    3,    1,  139,  208,   68,   68,   68,   68,
   68,   68,  143,  141,   68,   68,   68,   68,   48,   47,
   68,   68,   68,   68,   68,   67,   67,  177,  214,   67,
   67,   67,   67,   90,  127,   84,   91,   92,   67,  205,
    0,    0,  209,   67,   67,   67,   67,   67,   67,    0,
    0,   67,   67,   67,   67,    0,    0,   67,   67,   67,
   67,   67,   51,    0,    0,   51,   51,   51,   51,   57,
   90,    0,   27,   91,   51,   27,   27,   27,   27,  211,
    8,    0,    0,    9,   27,    0,   11,    0,   51,   34,
   35,   36,   12,   51,   51,    0,    0,   51,   27,    0,
    0,   37,   27,   27,    0,  133,   13,   27,  133,  133,
  133,  133,  113,    0,    0,   85,    0,  133,    0,    0,
    0,   49,   57,    0,   49,   49,   49,   49,   83,    0,
    0,  133,    0,   49,    0,    0,  133,  133,    0,    0,
  133,   49,   34,   35,   36,   50,    0,   49,   50,   50,
   50,   50,   49,   49,   37,    0,   49,   50,   16,    0,
    0,   16,   16,   16,   16,    0,    0,    0,  172,    0,
   16,   50,   16,   16,   16,   40,   50,   50,    0,    0,
   50,    0,   25,  113,   16,   25,   25,   25,   25,   49,
   34,   35,   36,   16,   25,   31,    0,    0,   31,   31,
   31,   31,   37,    0,    0,    0,   87,   31,   25,    0,
    0,    0,    8,   40,    0,    9,    0,   25,   11,    8,
    8,   31,    9,    9,   12,   11,   11,    0,    0,    0,
   31,   12,   12,    0,    0,    0,    0,    8,   13,    0,
    9,    0,    0,   11,   99,   13,   13,  139,    0,   12,
    0,   40,    0,  140,  193,  226,    0,   49,   34,   35,
   36,    0,   40,   13,  157,    8,    0,    0,    9,    0,
   37,   11,  260,    8,    0,    8,    9,   12,    9,   11,
    0,   11,    0,    0,    0,   12,    8,   12,    0,    9,
    0,   13,   11,    0,    0,    0,    0,    0,   12,   13,
  274,   13,    0,   40,   40,    0,   95,   39,  137,   39,
    8,    0,   13,    9,    0,    0,   11,    0,    0,  242,
  243,  157,   12,   87,    0,  103,  280,    0,  103,  102,
    0,  103,  102,  280,   48,  102,   13,  103,  280,    0,
    0,  102,  280,  264,  243,    0,    0,   49,   34,   35,
   36,  103,    0,    0,  157,  102,    0,  157,  103,  103,
   37,  140,  102,  102,  104,    0,  101,  104,    8,  101,
  104,    9,  101,    0,   11,    0,  104,    0,  101,    0,
   12,    8,    0,    8,    9,  157,    9,   11,  157,   11,
  104,    0,  101,   12,   13,   12,    0,  104,  104,  101,
  101,   87,   39,   16,    0,    0,    0,   13,    0,   13,
   87,  279,   34,   35,   36,  156,    0,  252,   48,   87,
  140,   16,    0,    8,   37,    0,    9,   48,  140,   11,
  105,   49,   34,   35,   36,   12,  140,    0,    0,    0,
   49,   34,   35,   36,   37,    0,    0,   89,   48,   13,
    0,  106,    0,   37,    0,    0,    0,  243,    0,    0,
   50,   49,   34,   35,   36,   61,   70,   71,   72,   73,
   90,    0,    0,   91,   37,   78,   79,    0,   61,    0,
    0,  165,   61,    0,   61,   61,   61,   61,   61,   61,
   61,   61,   61,   61,   61,   61,   61,   77,    0,    0,
    0,    0,    0,    8,    0,    0,    9,    0,    0,   11,
   57,    0,    0,    0,    0,   12,   77,   77,   77,   77,
   77,   77,   77,   77,   57,   77,   77,  130,   57,   13,
    0,  143,    0,    0,    0,  145,    0,    0,    0,    0,
   49,   34,   35,   36,   49,   34,   35,   36,   49,   34,
   35,   36,  147,   37,    0,    0,  149,   37,    0,    0,
  151,   37,    0,    0,    0,   49,   34,   35,   36,   49,
   34,   35,   36,   49,   34,   35,   36,  175,   37,    0,
  171,    0,   37,    0,    0,    0,   37,    0,    0,    0,
   49,   34,   35,   36,   34,   35,   36,    0,    0,    0,
    0,    0,    0,   37,  259,    0,   37,    0,  262,  263,
  265,  266,  267,  268,    0,   34,   35,   36,    0,    0,
    0,    0,    0,    0,    0,    0,  276,   37,    0,    0,
    0,    0,  112,   70,   71,   72,   73,   74,   75,   76,
   77,    0,   78,   79,    0,    0,    0,    0,  136,   70,
   71,   72,   73,   74,   75,   76,   77,    0,   78,   79,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
    0,    4,  154,  156,  279,  269,  256,  282,  270,  256,
   46,    8,    9,  279,  257,  290,  282,  260,  256,  262,
  263,  273,  269,  273,  269,  289,  269,    9,  290,  256,
  257,  269,   29,  260,  286,  187,  263,    0,  190,  289,
  283,   38,  269,  270,  271,  272,  291,  269,   84,  292,
  288,  289,  291,   50,  264,  282,  283,  269,   55,   95,
  270,  283,  289,  256,  291,  287,  218,   64,   50,  221,
   67,  283,  289,   55,    0,  287,  269,   74,   75,   76,
   77,  234,   64,   80,   75,   76,  287,  256,   74,  290,
  243,   77,   89,   90,   91,   92,   93,   94,   80,  252,
  136,  137,   93,   94,   90,   91,  288,   89,  105,  257,
   92,    0,  260,  106,  262,  263,  288,  114,  115,  116,
  256,  269,  287,  105,  257,  290,  286,  260,  121,  262,
  263,  267,  114,  115,  116,  283,  269,  287,  256,  257,
  290,  264,  260,  257,  292,  263,  260,  277,    0,  263,
  283,  269,  282,  256,  257,  269,  286,  260,  257,  292,
  263,  260,  264,  262,  263,  283,  269,  269,  165,  283,
  269,  168,  262,  291,  256,  289,  265,  273,  292,  269,
  283,  270,  269,  165,  283,    0,  168,  269,  291,  257,
  256,  257,  260,  292,  260,  263,  283,  263,  256,  262,
  287,  269,  257,  269,  256,  260,  269,  262,  263,  270,
  271,  269,  215,  269,  269,  283,  267,  283,  270,  271,
  272,  289,    0,  257,  292,  291,  260,  283,  283,  263,
  282,  287,  235,  256,  265,  269,  259,  292,  257,  270,
  279,  260,  257,  282,  263,  260,  261,  262,  263,  283,
  269,  290,  280,  281,  269,  289,  256,  257,  292,    0,
  260,  261,  262,  263,  283,  269,  258,  259,  283,  269,
  289,  287,  288,  292,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  283,  270,  288,  289,
  290,  291,  292,  256,  257,    0,  270,  260,  261,  262,
  263,  283,  258,  259,  288,    0,  269,  279,  280,  281,
  282,  274,  275,  276,  277,  278,  279,  262,  290,  282,
  283,  284,  285,  280,  281,  288,  289,  290,  291,  292,
  256,  257,  274,  288,  260,  261,  262,  263,    0,  279,
  280,  281,  282,  269,  279,  258,  259,  282,  274,  275,
  276,  277,  278,  279,    0,  290,  282,  283,  284,  285,
  288,  265,  288,  289,  290,  291,  292,  256,  257,  259,
  265,  260,  261,  262,  263,  258,  259,  265,    0,  291,
  269,  279,  258,  259,  282,  274,  275,  276,  277,  278,
  279,    0,  290,  282,  283,  284,  285,  269,  290,  288,
  289,  290,  291,  292,  256,  257,  259,  270,  260,  261,
  262,  263,  270,  270,  270,    0,  291,  269,  279,  259,
  259,  282,  274,  275,  276,  277,  278,  279,    0,  290,
  282,  283,  284,  285,  259,  290,  288,  289,  290,  291,
  292,  256,  257,  290,  290,  260,  261,  262,  263,  279,
  280,  281,  282,  256,  269,  258,  259,  290,  288,  274,
  275,  276,  277,  278,  279,  289,  289,  282,  283,  284,
  285,  292,  289,  288,  289,  290,  291,  292,  256,  257,
  290,  290,  260,  261,  262,  263,  279,  288,  290,  282,
  290,  269,    0,    0,  274,  288,  274,  275,  276,  277,
  278,  279,  274,  274,  282,  283,  284,  285,  292,  292,
  288,  289,  290,  291,  292,  256,  257,  117,  178,  260,
  261,  262,  263,  279,   66,   38,  282,   41,  269,  168,
   -1,   -1,  288,  274,  275,  276,  277,  278,  279,   -1,
   -1,  282,  283,  284,  285,   -1,   -1,  288,  289,  290,
  291,  292,  257,   -1,   -1,  260,  261,  262,  263,   13,
  279,   -1,  257,  282,  269,  260,  261,  262,  263,  288,
  257,   -1,   -1,  260,  269,   -1,  263,   -1,  283,  270,
  271,  272,  269,  288,  289,   -1,   -1,  292,  283,   -1,
   -1,  282,  287,  288,   -1,  257,  283,  292,  260,  261,
  262,  263,   56,   -1,   -1,  292,   -1,  269,   -1,   -1,
   -1,  257,   66,   -1,  260,  261,  262,  263,  256,   -1,
   -1,  283,   -1,  269,   -1,   -1,  288,  289,   -1,   -1,
  292,  269,  270,  271,  272,  257,   -1,  283,  260,  261,
  262,  263,  288,  289,  282,   -1,  292,  269,  257,   -1,
   -1,  260,  261,  262,  263,   -1,   -1,   -1,  112,   -1,
  269,  283,    5,    6,    7,    8,  288,  289,   -1,   -1,
  292,   -1,  257,  127,  283,  260,  261,  262,  263,  269,
  270,  271,  272,  292,  269,  257,   -1,   -1,  260,  261,
  262,  263,  282,   -1,   -1,   -1,   39,  269,  283,   -1,
   -1,   -1,  257,   46,   -1,  260,   -1,  292,  263,  257,
  257,  283,  260,  260,  269,  263,  263,   -1,   -1,   -1,
  292,  269,  269,   -1,   -1,   -1,   -1,  257,  283,   -1,
  260,   -1,   -1,  263,  256,  283,  283,  292,   -1,  269,
   -1,   84,   -1,   86,  292,  292,   -1,  269,  270,  271,
  272,   -1,   95,  283,   97,  257,   -1,   -1,  260,   -1,
  282,  263,  292,  257,   -1,  257,  260,  269,  260,  263,
   -1,  263,   -1,   -1,   -1,  269,  257,  269,   -1,  260,
   -1,  283,  263,   -1,   -1,   -1,   -1,   -1,  269,  283,
  292,  283,   -1,  136,  137,   -1,  290,  291,  290,  291,
  257,   -1,  283,  260,   -1,   -1,  263,   -1,   -1,  290,
  291,  154,  269,  156,   -1,  257,  270,   -1,  260,  257,
   -1,  263,  260,  277,  256,  263,  283,  269,  282,   -1,
   -1,  269,  286,  290,  291,   -1,   -1,  269,  270,  271,
  272,  283,   -1,   -1,  187,  283,   -1,  190,  290,  291,
  282,  194,  290,  291,  257,   -1,  257,  260,  257,  260,
  263,  260,  263,   -1,  263,   -1,  269,   -1,  269,   -1,
  269,  257,   -1,  257,  260,  218,  260,  263,  221,  263,
  283,   -1,  283,  269,  283,  269,   -1,  290,  291,  290,
  291,  234,  291,  236,   -1,   -1,   -1,  283,   -1,  283,
  243,  269,  270,  271,  272,  291,   -1,  291,  256,  252,
  253,  254,   -1,  257,  282,   -1,  260,  256,  261,  263,
  268,  269,  270,  271,  272,  269,  269,   -1,   -1,   -1,
  269,  270,  271,  272,  282,   -1,   -1,  256,  256,  283,
   -1,  289,   -1,  282,   -1,   -1,   -1,  291,   -1,   -1,
  289,  269,  270,  271,  272,  256,  275,  276,  277,  278,
  279,   -1,   -1,  282,  282,  284,  285,   -1,  269,   -1,
   -1,  289,  273,   -1,  275,  276,  277,  278,  279,  280,
  281,  282,  283,  284,  285,  286,  287,  256,   -1,   -1,
   -1,   -1,   -1,  257,   -1,   -1,  260,   -1,   -1,  263,
  269,   -1,   -1,   -1,   -1,  269,  275,  276,  277,  278,
  279,  280,  281,  282,  283,  284,  285,  256,  287,  283,
   -1,  256,   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,
  269,  270,  271,  272,  269,  270,  271,  272,  269,  270,
  271,  272,  256,  282,   -1,   -1,  256,  282,   -1,   -1,
  256,  282,   -1,   -1,   -1,  269,  270,  271,  272,  269,
  270,  271,  272,  269,  270,  271,  272,  256,  282,   -1,
  256,   -1,  282,   -1,   -1,   -1,  282,   -1,   -1,   -1,
  269,  270,  271,  272,  270,  271,  272,   -1,   -1,   -1,
   -1,   -1,   -1,  282,  242,   -1,  282,   -1,  246,  247,
  248,  249,  250,  251,   -1,  270,  271,  272,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  264,  282,   -1,   -1,
   -1,   -1,  287,  275,  276,  277,  278,  279,  280,  281,
  282,   -1,  284,  285,   -1,   -1,   -1,   -1,  290,  275,
  276,  277,  278,  279,  280,  281,  282,   -1,  284,  285,
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
"prog : ID bloque LLAVEFIN",
"prog : ID LLAVEINIC bloque",
"prog : LLAVEINIC bloque LLAVEFIN",
"prog : ID LLAVEINIC bloque LLAVEFIN error",
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
"declaracion_variable : tipo PUNTOYCOMA",
"lista_ids : ID",
"lista_ids : lista_ids COMA ID",
"lista_ids : lista_ids COMA error",
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
"bloque_if_error : IF PARENTINIC condicion PARENTFIN rama_if error",
"bloque_if_error : IF condicion PARENTFIN rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF PARENTINIC condicion rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF condicion rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF PARENTINIC condicion PARENTFIN rama_if ELSE rama_else error",
"bloque_if_error : IF rama_if ENDIF",
"bloque_if_error : IF rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF PARENTINIC error PARENTFIN rama_if ENDIF",
"bloque_if_error : IF PARENTINIC error PARENTFIN rama_if ELSE rama_else ENDIF",
"bloque_if_error : IF PARENTINIC condicion PARENTFIN error",
"bloque_if_error : IF PARENTINIC condicion PARENTFIN rama_if ELSE error",
"condicion : expresion op_relacion expresion",
"condicion : expresion error expresion",
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
"rama_else : sentencia_ejec PUNTOYCOMA",
"rama_else : LLAVEINIC bloque_ejecutable LLAVEFIN",
"rama_else : LLAVEINIC LLAVEFIN",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR ID FROM CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC FROM CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID CTEINT TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM TO CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT CTEINT PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO PARENTFIN rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT rama_for",
"bloque_for : FOR ID FROM CTEINT TO CTEINT rama_for",
"bloque_for : FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN error",
"rama_for : sentencia_ejec",
"rama_for : LLAVEINIC bloque_ejecutable LLAVEFIN",
"rama_for : LLAVEINIC LLAVEFIN",
"sentencia_print : PRINT PARENTINIC expresion PARENTFIN",
"sentencia_print : PRINT PARENTINIC error PARENTFIN",
"sentencia_print : PRINT expresion PARENTFIN",
"sentencia_print : PRINT PARENTINIC expresion",
"llamada_funcion : ID PARENTINIC lista_params_reales PARENTFIN",
"lista_params_reales : param_real_map",
"lista_params_reales : lista_params_reales COMA param_real_map",
"param_real_map : parametro_real FLECHA ID",
"param_real_map : parametro_real FLECHA error",
"parametro_real : expresion",
"parametro_real : TRUNC PARENTINIC expresion PARENTFIN",
"parametro_real : TRUNC PARENTINIC expresion",
"parametro_real : TRUNC expresion PARENTFIN",
"parametro_real : TRUNC expresion",
"parametro_real : lambda_expr",
"lambda_expr : PARENTINIC tipo ID PARENTFIN LLAVEINIC bloque_ejecutable LLAVEFIN PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC tipo ID PARENTFIN bloque_ejecutable LLAVEFIN PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC tipo ID PARENTFIN LLAVEINIC bloque_ejecutable PARENTINIC argumento PARENTFIN",
"lambda_expr : PARENTINIC tipo ID PARENTFIN bloque_ejecutable PARENTINIC argumento PARENTFIN",
"argumento : ID",
"argumento : cte",
};

//#line 409 "gramaticaDeCero.y"

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;
static TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
static int n_var = 0; //para contar variables en asignaciones multiples
static int n_cte = 0; //para contar ctes en asignaciones multiples
static boolean error_lista_ids = false; //para controlar errores en lista_ids

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
//#line 786 "Parser.java"
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
case 5:
//#line 67 "gramaticaDeCero.y"
{yyerror("Sentencias definidas fuera del bloque de programa");}
break;
case 10:
//#line 78 "gramaticaDeCero.y"
{yyerror("Falta ';' al final de la sentencia.");}
break;
case 11:
//#line 79 "gramaticaDeCero.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 16:
//#line 90 "gramaticaDeCero.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 24:
//#line 104 "gramaticaDeCero.y"
{
                                                    if (!error_lista_ids) {
                                                        int linea = lex.getLineaActual();
                                                        for (String n : val_peek(1).sval.split(",\s")) {
                                                        SINT.add(linea, "Declaracion de variable: " + n);
                                                        }
                                                    } else {
                                                        error_lista_ids = false;  /* reset para la próxima */
                                                    }
                                                    }
break;
case 25:
//#line 114 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 26:
//#line 115 "gramaticaDeCero.y"
{yyerror("Error en declaración de variables, falta identificador."); }
break;
case 27:
//#line 120 "gramaticaDeCero.y"
{ yyval = new ParserVal(((EntradaTablaSimbolos)val_peek(0).obj).getLexema()); }
break;
case 28:
//#line 121 "gramaticaDeCero.y"
{ yyval = new ParserVal(val_peek(2).sval + ", " + ((EntradaTablaSimbolos)val_peek(0).obj).getLexema()); }
break;
case 29:
//#line 123 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de coma"); error_lista_ids = true;}
break;
case 30:
//#line 129 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
break;
case 31:
//#line 130 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
break;
case 32:
//#line 131 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, expresión inválida."); }
break;
case 33:
//#line 132 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
break;
case 34:
//#line 133 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
break;
case 35:
//#line 137 "gramaticaDeCero.y"
{
                                                                                                                    String nombre = ((EntradaTablaSimbolos) val_peek(7).obj).getLexema();
                                                                                                                    SINT.add(lex.getLineaActual(), "Declaracion de funcion: " + nombre);
                                                                                                                    yyval = new ParserVal(nombre);
                                                                                                                   }
break;
case 37:
//#line 145 "gramaticaDeCero.y"
{yyerror("Error sintáctico: Falta nombre identificador de función");}
break;
case 40:
//#line 156 "gramaticaDeCero.y"
{yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
break;
case 42:
//#line 162 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de tipo en parámetro formal");}
break;
case 43:
//#line 163 "gramaticaDeCero.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 46:
//#line 170 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Return"); }
break;
case 47:
//#line 171 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 49:
//#line 178 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Asignacion simple"); }
break;
case 50:
//#line 183 "gramaticaDeCero.y"
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
//#line 197 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de variables antes del '='"); }
break;
case 52:
//#line 198 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de constantes después del '='");}
break;
case 53:
//#line 203 "gramaticaDeCero.y"
{n_cte++;}
break;
case 54:
//#line 204 "gramaticaDeCero.y"
{n_cte++;}
break;
case 55:
//#line 205 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una constante después de coma");}
break;
case 56:
//#line 206 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
break;
case 57:
//#line 209 "gramaticaDeCero.y"
{n_var++;}
break;
case 58:
//#line 210 "gramaticaDeCero.y"
{n_var++;}
break;
case 59:
//#line 211 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta identificador después de coma");}
break;
case 60:
//#line 212 "gramaticaDeCero.y"
{yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
break;
case 65:
//#line 223 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 66:
//#line 224 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresión."); }
break;
case 68:
//#line 226 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 69:
//#line 227 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresión."); }
break;
case 72:
//#line 233 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 73:
//#line 234 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '*' en expresión."); }
break;
case 75:
//#line 236 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 76:
//#line 237 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresión."); }
break;
case 79:
//#line 251 "gramaticaDeCero.y"
{ yyval = val_peek(0); }
break;
case 80:
//#line 253 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String neg = "-" + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea(), "CTEFLOAT");
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = val_peek(0);
        }
break;
case 81:
//#line 261 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String valor = ent.getLexema();
            valor = valor.substring(0, valor.length() - 1); /*sin la I del final*/
            int num = Integer.parseInt(valor);
            if (num > 32767) {
                System.err.println("Error léxico: constante entera fuera de rango en línea "
                                    + lex.getLineaActual() + ": " + num);
                tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            }
            yyval = val_peek(0);
        }
break;
case 82:
//#line 274 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String neg = "-" + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea(), "CTEINT");
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = val_peek(0);
        }
break;
case 83:
//#line 282 "gramaticaDeCero.y"
{ yyval = val_peek(0); }
break;
case 84:
//#line 287 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 85:
//#line 289 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 87:
//#line 295 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 88:
//#line 296 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 89:
//#line 297 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 90:
//#line 298 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 91:
//#line 299 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 92:
//#line 300 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 93:
//#line 301 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 94:
//#line 302 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 95:
//#line 303 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 96:
//#line 304 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 97:
//#line 305 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 98:
//#line 306 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 99:
//#line 307 "gramaticaDeCero.y"
{yyerror("Falta bloque del if");}
break;
case 100:
//#line 308 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 102:
//#line 312 "gramaticaDeCero.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 103:
//#line 313 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 104:
//#line 314 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 113:
//#line 329 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del then");}
break;
case 116:
//#line 336 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del else");}
break;
case 117:
//#line 343 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 118:
//#line 344 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 119:
//#line 345 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 120:
//#line 346 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 121:
//#line 347 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 122:
//#line 348 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 123:
//#line 349 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 124:
//#line 350 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 125:
//#line 351 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 126:
//#line 352 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 129:
//#line 359 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 130:
//#line 364 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 131:
//#line 365 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 132:
//#line 366 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 133:
//#line 367 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 134:
//#line 374 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 138:
//#line 384 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 140:
//#line 388 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 141:
//#line 389 "gramaticaDeCero.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 142:
//#line 390 "gramaticaDeCero.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 143:
//#line 391 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 145:
//#line 397 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Lambda");}
break;
case 146:
//#line 398 "gramaticaDeCero.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 147:
//#line 399 "gramaticaDeCero.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 148:
//#line 400 "gramaticaDeCero.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1371 "Parser.java"
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
