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
   26,   26,   26,   26,   26,   26,   27,   27,   27,   28,
   25,   25,   25,   25,   25,   11,   11,   11,   33,   33,
   33,   33,   33,   33,   33,   33,   33,   33,   33,   33,
   33,   33,   30,   30,   30,   30,   34,   34,   34,   34,
   34,   34,   31,   31,   31,   32,   32,   32,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   35,   35,
   35,   13,   13,   13,   13,   14,   29,   29,   36,   36,
   37,   37,   37,   37,   37,   37,   38,   38,   38,   38,
   39,   39,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    5,    0,    2,    3,    2,    1,
    2,    1,    1,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    2,    2,    1,    3,    3,    5,
    4,    5,    5,    5,    9,    1,    7,    1,    3,    3,
    3,    3,    2,    0,    1,    5,    4,    0,    3,    3,
    2,    3,    1,    3,    3,    2,    1,    3,    3,    2,
    1,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    1,    2,    1,    3,
    1,    2,    1,    2,    1,    6,    8,    1,    5,    5,
    4,    6,    7,    7,    6,    8,    3,    5,    6,    8,
    5,    7,    3,    3,    3,    3,    1,    1,    1,    1,
    1,    1,    2,    3,    2,    2,    3,    2,    9,    8,
    8,    8,    8,    8,    8,    8,    7,    9,    1,    3,
    2,    4,    4,    3,    3,    4,    1,    3,    3,    3,
    1,    4,    3,    3,    2,    1,   10,    9,    9,    8,
    1,    1,
};
final static short yydefred[] = {                         0,
    0,    6,    0,    6,    0,    0,    0,    0,    0,   12,
    0,    0,    0,    2,    7,    0,    0,   13,   14,   17,
   18,   19,   20,   21,   22,   23,   36,    0,    0,   88,
    4,    0,    0,    0,   83,   81,   85,    0,    0,    0,
    0,    0,   79,    0,   70,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   53,   15,    0,    0,   26,
    0,    0,    0,    0,   61,    0,    0,    0,    5,  109,
  110,  111,  112,    0,    0,    0,    0,  108,  107,    0,
    0,   78,   84,   82,    0,    0,  115,    0,    0,  113,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   97,
    0,    0,    0,  134,    0,    0,    0,    0,    0,    0,
    0,  137,    0,  146,    0,   56,    0,    0,    0,    0,
   45,    0,   38,    0,    0,   24,    0,   62,   52,    0,
   59,    0,    0,    0,   73,   76,    0,    0,    0,    0,
    0,    0,  114,    0,    9,    0,    0,    0,    0,    0,
    0,    0,   72,   71,   75,   74,    0,    0,   91,    0,
    0,    0,    0,  133,  132,    0,    0,    0,    0,    0,
    0,    0,    0,  136,    0,   55,   54,    0,    0,    0,
    0,    0,    0,    0,   43,    0,   29,   28,    0,    0,
  101,    0,    0,   90,    8,    0,   89,    0,  118,    0,
  116,   98,   80,    0,    0,    0,    0,    0,    0,  144,
    0,  138,  140,  139,   34,   33,   32,   30,    0,   40,
   39,    6,   42,   41,    0,   99,   92,    0,   86,    0,
    0,   95,  117,    0,    0,    0,    0,    0,    0,  142,
    0,    6,    0,    0,  102,    0,   94,   93,    0,    0,
  129,  127,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   37,  100,   96,   87,  120,  131,    0,  121,  123,
    0,  126,  125,  124,  122,    0,    0,    0,    0,    0,
  130,  128,  119,    0,    0,  151,  152,    0,    0,    0,
   35,    0,    0,  150,    0,    0,  149,    0,  148,    0,
  147,   46,
};
final static short yydgoto[] = {                          3,
    5,   15,   88,  251,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   62,  110,  122,  280,   27,  123,
  124,   28,   29,   55,   43,   44,   45,   82,  111,   46,
   47,  162,   30,   80,  252,  112,  113,  114,  288,
};
final static short yysindex[] = {                      -259,
 -265,    0,    0,    0, -244, -198, -156, -248,  622,    0,
 -252, -247,  310,    0,    0, -209, -236,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -212, -159,    0,
    0, -160,  821, -178,    0,    0,    0, -243,  363,  -57,
 -162,  381,    0, -111,    0,  482,  -61,   61,  -87,  651,
 -102, -134, -206, -202,  459,    0,    0, -112, -216,    0,
  -80,   22,  670,  -40,    0, -114, -166,  -49,    0,    0,
    0,    0,    0,  736,  626,  626,  736,    0,    0,  670,
 -202,    0,    0,    0,  800,  487,    0,  -27,  -39,    0,
  670,  740,  744,  761,  765,  769,  551,   88,  592,    0,
 -202,  -72,   66,    0,  -11,   -4,  -91,  630,   26, -182,
  -73,    0,   29,    0,  290,    0,  670,  670,  786,  -80,
    0,   90,    0,  -30, -148,    0, -182,    0,    0,  459,
    0,  -49,   53, -111,    0,    0, -111, -182,  127,  551,
 -174,  125,    0,    9,    0, -182,   53, -111,   53, -111,
   61, -182,    0,    0,    0,    0,  162,  592,    0,  314,
   30,  102,  164,    0,    0,   60,   97,  -54,  105,  670,
  103,  107, -202,    0, -119,    0,    0,   23,  208,  245,
  325,  204, -123,   80,    0,  -93,    0,    0,    0,  199,
    0,  194,  592,    0,    0,  592,    0,  119,    0,  440,
    0,    0,    0,  128,  129,  137,   54,  138,  140,    0,
  123,    0,    0,    0,    0,    0,    0,    0,  124,    0,
    0,    0,    0,    0,  592,    0,    0, -138,    0,  176,
  185,    0,    0,  498,  146,  155, -214,  182,  183,    0,
  594,    0, -128,  207,    0,   15,    0,    0,  599,  444,
    0,    0,  599,  599,  500,  599,  599,  599,  667, -117,
  -16,    0,    0,    0,    0,    0,    0,  457,    0,    0,
 -109,    0,    0,    0,    0,  -98,  822,  192,  193,  197,
    0,    0,    0,  822,  206,    0,    0,  203,  822,  670,
    0,  213,  822,    0,  214,  177,    0,  219,    0,  222,
    0,    0,
};
final static short yyrindex[] = {                         0,
  -79,    0,    0,    0,    0,    0,  518,    0,    0,    0,
    0,  -35,    0,    0,    0,  392,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -15,    0,    0,
    0,  519,    0,  704,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   38,    0,    0,    0,    0,    1,    0,
    0,    0,    0,    0,  296,    0,    0,    0,  306,    0,
  -19,  416,    0,    0,    0,    0,    0,  -14,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -68,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  339,    0,    0,    0,    0,    0,    0,  254,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -19,
    0,    0,    0,    0,    0,    0,  355,    0,    0,  379,
    0,   21,    0,   75,    0,    0,  112,  535,    0,    0,
    0,    0,    0,  -64,    0,  540,  149,  186,  223,  260,
  550,  575,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  257,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  429,    0,  -19,    0,    0,    0,    0,    0,  690,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  258,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  248,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  255,
    0,    0,
};
final static short yygindex[] = {                         0,
   -2,    0, -157,  688,  -95,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   -3,  434,    0,    0,  372,
    0,  -22,    0,  504,  475,   46,  -45,    0,   -9,  525,
  -42, -147,    0,  531,  816,  403,    0,    0,  -62,
};
final static int YYTABLESIZE=1106;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          6,
   77,    7,  200,   98,   42,   51,   68,   33,    8,    1,
  198,    9,    8,  172,   11,    9,   52,   10,   11,   58,
   34,   35,   36,   37,   12,    4,   83,   84,  186,  135,
  136,    2,   59,   38,   13,   42,   53,   63,   13,  118,
   39,   54,   40,  142,  132,  230,  103,   14,  231,  154,
  156,   60,   61,   48,  157,  255,  119,  106,    8,  127,
   63,    9,  107,   10,   11,  108,   49,   35,   36,   37,
   12,  139,  120,   64,   66,  256,  138,  244,   57,   38,
  246,  191,    8,  260,   13,    9,  109,  146,   11,  131,
  152,  163,  268,   31,   12,   69,   92,  190,  192,   93,
    8,  276,   65,    9,  171,   10,   11,  187,   13,   65,
   81,   69,   12,  178,  179,  181,   40,  245,    8,  134,
  188,    9,  137,   66,   11,   90,   13,   67,    8,  105,
   12,    9,  220,   10,   11,   32,  213,  148,  150,    8,
   12,  129,    9,  121,   13,   11,  282,    8,   65,  214,
    9,   12,  160,   11,   13,   35,   36,   37,    8,   12,
  117,    9,  223,  262,   11,   13,  209,   38,   95,   96,
   12,  277,  168,   13,  278,  224,   92,    6,  169,   93,
    6,  250,    6,    6,   13,   64,  121,  104,   10,    6,
  284,   10,   11,  285,   10,   11,   99,  100,   11,    8,
   10,  101,    9,    6,   11,   11,   74,   75,   76,   77,
  206,   12,    6,  173,   10,  207,  174,  164,   11,  243,
   10,  292,   68,   10,   11,   13,  295,   11,  128,    8,
  298,   10,    9,   61,   87,   11,   64,   61,  185,  261,
    8,   12,   44,    9,  279,   10,   11,   61,  145,   44,
   61,   61,   12,   57,   60,   13,   77,   77,  166,   67,
   77,   77,   77,   77,  143,  167,   13,   57,   60,   77,
  264,   57,   60,  265,   77,   77,   77,   77,   77,   77,
   77,   77,   77,   77,   77,   77,  296,   10,   77,   58,
   77,   77,   77,   63,   63,   51,  195,   63,   63,   63,
   63,   92,  175,   58,   93,   27,   63,   58,  125,  126,
  215,   63,   63,   63,   63,   63,   63,  201,  237,   63,
   63,   63,   63,  238,  204,   63,   63,   63,   63,   63,
   66,   66,   75,   76,   66,   66,   66,   66,  135,   74,
   75,   76,   77,   66,   92,  158,  159,   93,   66,   66,
   66,   66,   66,   66,   49,  165,   66,   66,   66,   66,
  202,  205,   66,   66,   66,   66,   66,   69,   69,  208,
  222,   69,   69,   69,   69,  211,  183,  232,   50,  184,
   69,   92,  193,  194,   93,   69,   69,   69,   69,   69,
   69,   16,  210,   69,   69,   69,   69,  234,  235,   69,
   69,   69,   69,   69,   65,   65,  236,  239,   65,   65,
   65,   65,  241,  173,  242,   25,  189,   65,   92,  196,
  197,   93,   65,   65,   65,   65,   65,   65,   31,  240,
   65,   65,   65,   65,  247,  253,   65,   65,   65,   65,
   65,   64,   64,  248,  254,   64,   64,   64,   64,  227,
  173,  228,  229,  203,   64,   92,  225,  226,   93,   64,
   64,   64,   64,   64,   64,  263,  300,   64,   64,   64,
   64,  257,  258,   64,   64,   64,   64,   64,   68,   68,
  289,  290,   68,   68,   68,   68,   92,   56,  291,   93,
  183,   68,  294,  219,  293,  216,   68,   68,   68,   68,
   68,   68,  297,  299,   68,   68,   68,   68,  301,  302,
   68,   68,   68,   68,   68,   67,   67,    3,    1,   67,
   67,   67,   67,   74,   75,   76,   77,  141,   67,  116,
  145,  143,  217,   67,   67,   67,   67,   67,   67,   48,
   56,   67,   67,   67,   67,  176,   47,   67,   67,   67,
   67,   67,   51,  182,  221,   51,   51,   51,   51,   35,
   36,   37,   27,   86,   51,   27,   27,   27,   27,  130,
    8,   38,   94,    9,   27,  212,   11,    0,   51,   35,
   36,   37,   12,   51,   51,    0,    0,   51,   27,  177,
    0,   38,   27,   27,    0,  135,   13,   27,  135,  135,
  135,  135,    0,   92,  116,  199,   93,  135,    0,    0,
    0,   49,  218,    0,   49,   49,   49,   49,   85,    0,
    0,  135,    0,   49,    0,    0,  135,  135,    0,    0,
  135,   49,   35,   36,   37,   50,   91,   49,   50,   50,
   50,   50,   49,   49,   38,    0,   49,   50,   16,    0,
    0,   16,   16,   16,   16,   70,   71,   72,   73,   92,
   16,   50,   93,    0,   78,   79,   50,   50,    0,    0,
   50,    0,   25,    0,   16,   25,   25,   25,   25,    0,
    0,    0,    0,   16,   25,   31,    0,    0,   31,   31,
   31,   31,   16,   16,   16,   41,    8,   31,   25,    9,
    8,    0,   11,    9,    0,    0,   11,   25,   12,    0,
    0,   31,   12,    8,    0,    0,    9,    0,    0,   11,
   31,    0,   13,    0,    0,   12,   13,   89,   35,   36,
   37,  233,    0,   41,    0,  267,    0,    0,    8,   13,
   38,    9,    0,    8,   11,  115,    9,    0,  281,   11,
   12,  287,    0,    0,    8,   12,    8,    9,  287,    9,
   11,    0,   11,  287,   13,    0,   12,  287,   12,   13,
    0,   97,   40,   41,    0,  144,  141,   40,    0,    0,
   13,    0,   13,    0,   41,    0,  161,  249,  250,  271,
  250,  105,    0,    0,  105,    0,  104,  105,    0,  104,
    0,    0,  104,  105,    0,    0,  106,    8,  104,  106,
    9,    0,  106,   11,    0,    0,    0,  105,  106,   12,
    0,    0,  104,    0,  105,  105,    0,   41,   41,  104,
  104,  103,  106,   13,  103,    0,    0,  103,    0,  106,
  106,   40,    0,  103,    0,  161,    0,   89,    8,    0,
    8,    9,    0,    9,   11,    8,   11,  103,    9,    0,
   12,   11,   12,    0,  103,  103,    0,   12,    0,    0,
    0,    0,    0,    0,   13,    0,   13,   48,    0,    0,
  161,   13,  160,  161,  259,   48,    0,  144,    0,  250,
   49,   35,   36,   37,   49,   35,   36,   37,   49,   35,
   36,   37,    0,   38,    0,    0,  102,   38,    0,    0,
   50,   38,  161,    0,    0,  161,    0,    0,  170,   49,
   35,   36,   37,    8,    0,   48,    9,    0,   89,   11,
   16,    0,   38,    0,    0,   12,    0,   89,   49,   35,
   36,   37,    0,    0,    0,   80,   89,  144,   16,   13,
    0,   38,    0,    0,    0,  144,    0,    0,    0,   77,
    0,    0,    0,  144,   80,   80,   80,   80,   80,   80,
   80,   80,   61,   80,   80,    0,   61,  136,   77,   77,
   77,   77,   77,   77,   77,   77,   61,   77,   77,   61,
   61,  133,    0,    0,    0,  147,    0,    0,    0,  149,
    0,    0,    0,    0,   49,   35,   36,   37,   49,   35,
   36,   37,   49,   35,   36,   37,  151,   38,    0,    0,
  153,   38,    0,    0,  155,   38,    0,    0,    0,   49,
   35,   36,   37,   49,   35,   36,   37,   49,   35,   36,
   37,  180,   38,    0,    0,    0,   38,    0,    0,    0,
   38,    0,    0,    0,   49,   35,   36,   37,    0,    0,
    0,    0,    0,    0,  266,    0,    0,   38,  269,  270,
  272,  273,  274,  275,   70,   71,   72,   73,   74,   75,
   76,   77,    0,   78,   79,    0,  283,    0,    0,  140,
  286,   35,   36,   37,    0,   70,   71,   72,   73,   74,
   75,   76,   77,   38,   78,   79,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
    0,    4,  160,   46,    8,    9,   29,  256,  257,  269,
  158,  260,  257,  109,  263,  260,  269,  262,  263,  256,
  269,  270,  271,  272,  269,  291,  270,  271,  124,   75,
   76,  291,  269,  282,  283,   39,  289,    0,  283,  256,
  289,  289,  291,   86,   67,  193,   50,  292,  196,   95,
   96,  288,  289,  256,   97,  270,  273,  264,  257,   63,
  273,  260,  269,  262,  263,  268,  269,  270,  271,  272,
  269,   81,  289,  286,    0,  290,   80,  225,  288,  282,
  228,  256,  257,  241,  283,  260,  289,   91,  263,  256,
   94,  101,  250,  292,  269,  256,  279,  140,  141,  282,
  257,  259,  269,  260,  108,  262,  263,  256,  283,  269,
  289,    0,  269,  117,  118,  119,  291,  256,  257,   74,
  269,  260,   77,  283,  263,  288,  283,  287,  257,  264,
  269,  260,  256,  262,  263,  292,  256,   92,   93,  257,
  269,  256,  260,  267,  283,  263,  256,  257,    0,  269,
  260,  269,  291,  263,  283,  270,  271,  272,  257,  269,
  273,  260,  256,  292,  263,  283,  170,  282,  280,  281,
  269,  289,  264,  283,  292,  269,  279,  257,  270,  282,
  260,  291,  262,  263,  283,    0,  267,  290,  257,  269,
  289,  260,  257,  292,  263,  260,  258,  259,  263,  257,
  269,  289,  260,  283,  269,  263,  279,  280,  281,  282,
  265,  269,  292,  287,  283,  270,  290,  290,  283,  222,
  289,  284,    0,  292,  289,  283,  289,  292,  269,  257,
  293,  262,  260,  269,  292,  263,  286,  273,  269,  242,
  257,  269,  262,  260,  261,  262,  263,  283,  288,  269,
  286,  287,  269,  269,  269,  283,  256,  257,  270,    0,
  260,  261,  262,  263,  292,  270,  283,  283,  283,  269,
  256,  287,  287,  259,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  290,  262,  288,  269,
  290,  291,  292,  256,  257,    0,  288,  260,  261,  262,
  263,  279,  274,  283,  282,    0,  269,  287,  287,  288,
  288,  274,  275,  276,  277,  278,  279,  288,  265,  282,
  283,  284,  285,  270,  265,  288,  289,  290,  291,  292,
  256,  257,  280,  281,  260,  261,  262,  263,    0,  279,
  280,  281,  282,  269,  279,  258,  259,  282,  274,  275,
  276,  277,  278,  279,    0,  290,  282,  283,  284,  285,
  259,  265,  288,  289,  290,  291,  292,  256,  257,  265,
  291,  260,  261,  262,  263,  269,  287,  259,    0,  290,
  269,  279,  258,  259,  282,  274,  275,  276,  277,  278,
  279,    0,  290,  282,  283,  284,  285,  270,  270,  288,
  289,  290,  291,  292,  256,  257,  270,  270,  260,  261,
  262,  263,  290,  287,  291,    0,  290,  269,  279,  258,
  259,  282,  274,  275,  276,  277,  278,  279,    0,  290,
  282,  283,  284,  285,  259,  290,  288,  289,  290,  291,
  292,  256,  257,  259,  290,  260,  261,  262,  263,  256,
  287,  258,  259,  290,  269,  279,  258,  259,  282,  274,
  275,  276,  277,  278,  279,  259,  290,  282,  283,  284,
  285,  290,  290,  288,  289,  290,  291,  292,  256,  257,
  289,  289,  260,  261,  262,  263,  279,   13,  292,  282,
  287,  269,  290,  290,  289,  288,  274,  275,  276,  277,
  278,  279,  290,  290,  282,  283,  284,  285,  290,  288,
  288,  289,  290,  291,  292,  256,  257,    0,    0,  260,
  261,  262,  263,  279,  280,  281,  282,  274,  269,   55,
  274,  274,  288,  274,  275,  276,  277,  278,  279,  292,
   66,  282,  283,  284,  285,  256,  292,  288,  289,  290,
  291,  292,  257,  120,  183,  260,  261,  262,  263,  270,
  271,  272,  257,   39,  269,  260,  261,  262,  263,   66,
  257,  282,   42,  260,  269,  173,  263,   -1,  283,  270,
  271,  272,  269,  288,  289,   -1,   -1,  292,  283,  115,
   -1,  282,  287,  288,   -1,  257,  283,  292,  260,  261,
  262,  263,   -1,  279,  130,  292,  282,  269,   -1,   -1,
   -1,  257,  288,   -1,  260,  261,  262,  263,  256,   -1,
   -1,  283,   -1,  269,   -1,   -1,  288,  289,   -1,   -1,
  292,  269,  270,  271,  272,  257,  256,  283,  260,  261,
  262,  263,  288,  289,  282,   -1,  292,  269,  257,   -1,
   -1,  260,  261,  262,  263,  275,  276,  277,  278,  279,
  269,  283,  282,   -1,  284,  285,  288,  289,   -1,   -1,
  292,   -1,  257,   -1,  283,  260,  261,  262,  263,   -1,
   -1,   -1,   -1,  292,  269,  257,   -1,   -1,  260,  261,
  262,  263,    5,    6,    7,    8,  257,  269,  283,  260,
  257,   -1,  263,  260,   -1,   -1,  263,  292,  269,   -1,
   -1,  283,  269,  257,   -1,   -1,  260,   -1,   -1,  263,
  292,   -1,  283,   -1,   -1,  269,  283,   40,  270,  271,
  272,  292,   -1,   46,   -1,  292,   -1,   -1,  257,  283,
  282,  260,   -1,  257,  263,  287,  260,   -1,  292,  263,
  269,  277,   -1,   -1,  257,  269,  257,  260,  284,  260,
  263,   -1,  263,  289,  283,   -1,  269,  293,  269,  283,
   -1,  290,  291,   86,   -1,   88,  290,  291,   -1,   -1,
  283,   -1,  283,   -1,   97,   -1,   99,  290,  291,  290,
  291,  257,   -1,   -1,  260,   -1,  257,  263,   -1,  260,
   -1,   -1,  263,  269,   -1,   -1,  257,  257,  269,  260,
  260,   -1,  263,  263,   -1,   -1,   -1,  283,  269,  269,
   -1,   -1,  283,   -1,  290,  291,   -1,  140,  141,  290,
  291,  257,  283,  283,  260,   -1,   -1,  263,   -1,  290,
  291,  291,   -1,  269,   -1,  158,   -1,  160,  257,   -1,
  257,  260,   -1,  260,  263,  257,  263,  283,  260,   -1,
  269,  263,  269,   -1,  290,  291,   -1,  269,   -1,   -1,
   -1,   -1,   -1,   -1,  283,   -1,  283,  256,   -1,   -1,
  193,  283,  291,  196,  291,  256,   -1,  200,   -1,  291,
  269,  270,  271,  272,  269,  270,  271,  272,  269,  270,
  271,  272,   -1,  282,   -1,   -1,  256,  282,   -1,   -1,
  289,  282,  225,   -1,   -1,  228,   -1,   -1,  289,  269,
  270,  271,  272,  257,   -1,  256,  260,   -1,  241,  263,
  243,   -1,  282,   -1,   -1,  269,   -1,  250,  269,  270,
  271,  272,   -1,   -1,   -1,  256,  259,  260,  261,  283,
   -1,  282,   -1,   -1,   -1,  268,   -1,   -1,   -1,  256,
   -1,   -1,   -1,  276,  275,  276,  277,  278,  279,  280,
  281,  282,  269,  284,  285,   -1,  273,  288,  275,  276,
  277,  278,  279,  280,  281,  282,  283,  284,  285,  286,
  287,  256,   -1,   -1,   -1,  256,   -1,   -1,   -1,  256,
   -1,   -1,   -1,   -1,  269,  270,  271,  272,  269,  270,
  271,  272,  269,  270,  271,  272,  256,  282,   -1,   -1,
  256,  282,   -1,   -1,  256,  282,   -1,   -1,   -1,  269,
  270,  271,  272,  269,  270,  271,  272,  269,  270,  271,
  272,  256,  282,   -1,   -1,   -1,  282,   -1,   -1,   -1,
  282,   -1,   -1,   -1,  269,  270,  271,  272,   -1,   -1,
   -1,   -1,   -1,   -1,  249,   -1,   -1,  282,  253,  254,
  255,  256,  257,  258,  275,  276,  277,  278,  279,  280,
  281,  282,   -1,  284,  285,   -1,  271,   -1,   -1,  290,
  269,  270,  271,  272,   -1,  275,  276,  277,  278,  279,
  280,  281,  282,  282,  284,  285,
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
"factor : ID",
"factor : ID restofunc",
"factor : cte",
"restofunc : PARENTINIC lista_params_reales PARENTFIN",
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

//#line 419 "gramaticaDeCero.y"

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
//#line 785 "Parser.java"
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
case 80:
//#line 255 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 81:
//#line 261 "gramaticaDeCero.y"
{ yyval = val_peek(0); }
break;
case 82:
//#line 263 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String neg = '-' + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea());
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = val_peek(0);  /* devolvés el mismo ParserVal */
            }
break;
case 83:
//#line 271 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String valor = ent.getLexema();
            valor = valor.substring(0, valor.length() - 1); /* quita la 'I' final */
            int num = Integer.parseInt(valor);
            if (num > 32767) {
                System.err.println("Error léxico: constante entera fuera de rango en línea "
                                + lex.getLineaActual() + ": " + num);
                tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            }
            yyval = val_peek(0);
            }
break;
case 84:
//#line 284 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String neg = '-' + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea());
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = val_peek(0);
            }
break;
case 85:
//#line 292 "gramaticaDeCero.y"
{ yyval = val_peek(0); }
break;
case 86:
//#line 297 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 87:
//#line 299 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 89:
//#line 305 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 90:
//#line 306 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 91:
//#line 307 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 92:
//#line 308 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 93:
//#line 309 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 94:
//#line 310 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 95:
//#line 311 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 96:
//#line 312 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 97:
//#line 313 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 98:
//#line 314 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 99:
//#line 315 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 100:
//#line 316 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 101:
//#line 317 "gramaticaDeCero.y"
{yyerror("Falta bloque del if");}
break;
case 102:
//#line 318 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 104:
//#line 322 "gramaticaDeCero.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 105:
//#line 323 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 106:
//#line 324 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 115:
//#line 339 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del then");}
break;
case 118:
//#line 346 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del else");}
break;
case 119:
//#line 353 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 120:
//#line 354 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 121:
//#line 355 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 122:
//#line 356 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 123:
//#line 357 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 124:
//#line 358 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 125:
//#line 359 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 126:
//#line 360 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 127:
//#line 361 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 128:
//#line 362 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 131:
//#line 369 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 132:
//#line 374 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 133:
//#line 375 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 134:
//#line 376 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 135:
//#line 377 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 136:
//#line 384 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 140:
//#line 394 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 142:
//#line 398 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 143:
//#line 399 "gramaticaDeCero.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 144:
//#line 400 "gramaticaDeCero.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 145:
//#line 401 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 147:
//#line 407 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Lambda");}
break;
case 148:
//#line 408 "gramaticaDeCero.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 149:
//#line 409 "gramaticaDeCero.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 150:
//#line 410 "gramaticaDeCero.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1374 "Parser.java"
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
