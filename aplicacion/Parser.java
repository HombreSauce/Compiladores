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

	


//#line 37 "Parser.java"




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
    0,    1,    1,    3,    3,    3,    5,    2,    2,    2,
    2,    4,    4,    4,    4,    4,    4,    4,    6,    6,
    7,    7,   17,   18,   18,   15,   15,   15,    8,    8,
    8,    8,    8,    9,    9,    9,    9,   20,   20,   20,
   21,   21,   21,   21,   21,   19,   19,   19,   19,   19,
   19,   19,   22,   22,   22,   22,   22,   22,   22,   23,
   23,   13,   13,   24,   24,   25,   25,   26,   26,   26,
   26,   26,   26,   14,   16,   16,   28,   28,   28,   29,
   29,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   30,   30,   30,   30,   33,   33,   33,   33,   33,   33,
   31,   31,   32,   32,   11,   11,   11,   11,   11,   11,
   11,   11,   11,   11,   34,   34,   12,   12,   12,   12,
   27,   27,   27,   27,   35,   35,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    3,    2,    1,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    2,
    8,    1,    7,    1,    3,    1,    3,    3,    3,    3,
    2,    3,    2,    3,    2,    2,    3,    1,    3,    3,
    1,    2,    1,    2,    1,    1,    3,    3,    3,    3,
    3,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    1,    4,    4,    1,    3,    3,    3,    1,    4,    4,
    4,    4,    1,    4,    1,    3,    3,    3,    3,    0,
    1,    6,    5,    6,    4,    6,    7,    7,    7,    4,
    3,    3,    3,    3,    1,    1,    1,    1,    1,    1,
    1,    3,    2,    2,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    2,    3,    4,    4,    4,    4,
   10,   10,   10,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    7,    0,
    0,    0,    0,    1,    3,    0,    0,    8,   11,   12,
   13,   14,   15,   16,   17,   18,    0,   22,    0,    0,
    0,   24,   43,   41,   45,    0,    0,    0,    0,   61,
    0,   53,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   38,    9,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   33,    0,    0,    0,    0,   64,    0,
   73,   97,   98,   99,  100,    0,    0,    0,    0,   96,
   95,    4,  101,    0,    0,   44,   42,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   19,    0,   81,    0,
   75,    0,   37,    0,   28,   27,    0,    0,    0,   25,
    0,    0,    0,    0,   63,    0,    0,    0,   56,   59,
    0,    0,   90,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   55,   54,   58,   57,    0,   85,  119,
  118,  120,  117,   74,    0,    0,    0,    0,   62,   40,
   39,    0,    0,    0,    0,    0,    0,    0,    0,   65,
   67,   66,  102,    0,    0,    0,    0,   83,    0,    0,
    0,    0,    0,    0,   76,    2,   79,   78,   77,   72,
   71,   70,   69,    0,    5,   84,    0,    0,   86,   82,
    0,    0,    0,    0,    0,    0,    0,    2,    0,    4,
    4,    0,  103,   89,   88,   87,    0,    0,    0,    0,
    0,    0,    0,    0,   23,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   21,    0,    0,    0,
    0,    4,    0,  113,  106,  107,  108,  109,  110,  111,
  112,    0,  105,    0,    0,    0,    0,    0,  115,    0,
    0,    0,  125,  126,    0,    0,    0,  116,  124,  122,
  123,  121,
};
final static short yydgoto[] = {                          2,
    4,   83,  132,   16,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   27,  110,   28,   38,   67,   52,
   40,   41,   42,   68,   69,   70,   71,  111,  112,   43,
   84,  198,   85,  244,  262,
};
final static short yysindex[] = {                      -250,
 -270,    0,    0,  447, -263,  816, -254, -247,    0, -227,
 -216,  275,  -63,    0,    0, -211, -226,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  654,    0, -130,  787,
  573,    0,    0,    0,    0,  -93,  330, -175,  843,    0,
  -20,    0,  605,  275,  385,  275, -151, -189,  787,  -24,
  -78, -143,    0,    0,  -18, -112, -126,  -76, -172, -143,
  275,  615, -106,    0, -223,  -65,  -78,  -77,    0,  -90,
    0,    0,    0,    0,    0,  844,  693,  693,  844,    0,
    0,    0,    0,  -60,  275,    0,    0,  926, -233,  275,
  861,  867,  884,  888,  892,  698,  -44,  -79,  811, -169,
  612,   -2,   29, -127,  -73,  549,    0, -112,    0,  -11,
    0, -174,    0, -143,    0,    0,  -78,  -24,  -78,    0,
  275,  275,    8,  787,    0, -136,   33,  -20,    0,    0,
  -20,  495,    0,  -78,  698,  722,  -78,   33,  -20,   33,
  -20,  -24,  -78,    0,    0,    0,    0,   48,    0,    0,
    0,    0,    0,    0,   38,   46,   58, -242,    0,    0,
    0,    4, -112,   39,   60, -134, -167, -163,   41,    0,
    0,    0,    0,   44,   75, -224, -120,    0,   70,   71,
   79,   80, -184,   59,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -236,    0,    0,  737,   92,    0,    0,
  -97,   83,   95,   96,   97,   98, -239,    0,  465,    0,
    0, -263,    0,    0,    0,    0, -232,   81,   82,   91,
  100,  112, -231,  480,    0,  510,  525,  758,  758,  758,
  758,  758,  758,  758,  758,  769,    0,   84,   93,   99,
  114,    0,  116,    0,    0,    0,    0,    0,    0,    0,
    0, -263,    0,  795,  764,  795,  764,  540,    0,    0,
    0,  115,    0,    0,  118,  119,  120,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  824,    0,    0,    0,    0,  414,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -109,    0,    0,
  -10,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -251, -209,    0,    0,  -71, -122,  432,    0,    0,  249,
    0,    0,    0,    0,    0,    0,  113,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -122,    0,    0,
    0,    0,    0,  267,    0,    0,  304,  322,  359,    0,
    0,    0,    0,    0,    0,    0,    0,   27,    0,    0,
   64,    0,    0,  620,    0,    0,  644,  101,  138,  175,
  212,  659,  683,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -122,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  555,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   36,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  377,    0,    0,    0,    0,    0,    0,    0,  897,
  910,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
 -170,    9, -166, -128,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  352,  298,    0,   -4,   24,   13,
  -12,   -1,   47,  369,  295,    0,    0,  261,    0,  388,
  -40,  250,  400,    5,   42,
};
final static int YYTABLESIZE=1211;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         29,
   53,   44,   97,  174,   31,   31,   31,   31,   31,   31,
   31,   31,   15,  182,   53,  209,  222,   31,    1,  210,
    3,   31,  135,  228,  235,   30,   29,  183,   47,   39,
  223,   31,  121,  197,   45,   51,   31,  224,   29,   60,
   31,   46,   55,  226,  227,   53,   35,   35,   35,   35,
   35,   35,   35,   35,  211,  148,  136,  229,  236,   35,
   39,   48,   56,   35,   30,  122,  103,   98,  100,  101,
  114,  206,   49,   35,  128,  258,   54,  131,   35,  104,
  207,  165,   35,  115,  117,  119,  152,  166,  190,  139,
  141,   29,  192,  161,  175,  177,  116,  174,  174,  243,
  243,  243,  243,  243,  243,  243,  243,  243,  134,   91,
   63,   91,   92,  137,   92,   91,  143,  102,   92,  171,
  153,  188,  191,  129,  130,   61,  193,   29,  157,  174,
   29,   29,  172,   80,  189,  199,  158,  197,  200,   80,
  145,  147,   62,  106,  167,  168,   60,   60,   60,   60,
   60,   60,   60,   60,  109,   63,  213,   64,  215,   60,
   59,  216,  120,   60,   60,   60,   60,   60,   60,   60,
   60,   60,   60,   60,   60,   60,   86,   87,   60,  113,
   60,   60,   60,  126,   26,   26,   26,   26,   26,   26,
   26,   26,   29,   33,   34,   35,  123,   26,  133,   91,
   91,   26,   92,   92,   29,   36,   33,   34,   35,  124,
  150,   26,  125,  124,  149,   26,  159,   15,   36,   29,
   26,   29,   29,   29,   29,   29,   29,   29,   29,   29,
   29,   29,   15,  245,  246,  247,  248,  249,  250,  251,
  253,  261,  264,  261,  264,   46,   46,   46,   46,   46,
   46,   46,   46,   29,   76,   77,   78,   79,   46,   94,
   95,  155,   46,   46,   46,   46,   46,   46,   46,  107,
  108,   46,   46,   46,   46,  163,  169,   46,  164,   46,
   46,   46,   49,   49,   49,   49,   49,   49,   49,   49,
  163,  104,  156,  184,  104,   49,  265,  266,  267,   49,
   49,   49,   49,   49,   49,   49,  178,  179,   49,   49,
   49,   49,   77,   78,   49,  180,   49,   49,   49,   52,
   52,   52,   52,   52,   52,   52,   52,  181,  187,  186,
  194,  195,   52,  196,  202,  203,   52,   52,   52,   52,
   52,   52,   52,  204,  205,   52,   52,   52,   52,  208,
  214,   52,  217,   52,   52,   52,   48,   48,   48,   48,
   48,   48,   48,   48,  218,  219,  220,  221,   57,   48,
  230,  231,  254,   48,   48,   48,   48,   48,   48,   48,
  232,  255,   48,   48,   48,   48,   68,  256,   48,  233,
   48,   48,   48,   47,   47,   47,   47,   47,   47,   47,
   47,  234,  257,  259,  269,  162,   47,  270,  271,  272,
   47,   47,   47,   47,   47,   47,   47,  105,  170,   47,
   47,   47,   47,  185,   89,   47,  201,   47,   47,   47,
   51,   51,   51,   51,   51,   51,   51,   51,   93,    0,
    0,    0,    0,   51,    0,    0,    0,   51,   51,   51,
   51,   51,   51,   51,    0,    0,   51,   51,   51,   51,
    0,    0,   51,    0,   51,   51,   51,   50,   50,   50,
   50,   50,   50,   50,   50,    0,    0,    0,    0,    0,
   50,    0,    0,    0,   50,   50,   50,   50,   50,   50,
   50,    0,    0,   50,   50,   50,   50,    0,    0,   50,
    0,   50,   50,   50,   36,   36,   36,   36,   36,   36,
   36,   36,    0,    0,    0,    0,    0,   36,    0,    0,
    0,   36,   34,   34,   34,   34,   34,   34,   34,   34,
   50,   36,    0,    0,    0,   34,   36,    0,    0,   34,
   36,    0,    0,   32,   33,   34,   35,    0,    0,   34,
    0,    0,    0,    0,   34,    0,   36,    0,   34,   32,
   32,   32,   32,   32,   32,   32,   32,    0,    0,    0,
    0,    0,   32,    0,    0,    0,   32,   30,   30,   30,
   30,   30,   30,   30,   30,   88,   32,    0,    0,    0,
   30,   32,    0,    0,   30,   32,    0,    0,   32,   33,
   34,   35,    0,    0,   30,    0,    0,    0,    0,   30,
    0,   36,    0,   30,   29,   29,   29,   29,   29,   29,
   29,   29,    0,    0,    0,    0,    0,   29,    0,    0,
    0,   29,  114,  114,  114,  114,  114,  114,  114,  114,
   99,   29,    0,    0,    0,  114,   29,    0,    0,  114,
   29,    0,    0,   32,   33,   34,   35,    0,    0,  114,
    0,    0,    0,    0,  114,    0,   36,    0,  114,   10,
   10,   10,   10,   10,   10,   10,   10,    0,    0,    0,
    0,    0,   10,    0,    0,    0,   10,   20,   20,   20,
   20,   20,   20,   20,   20,    0,   10,    0,    0,    0,
   20,    0,    5,    6,   20,   10,    7,    8,    9,   10,
    0,    0,    0,    0,   20,   11,    0,    0,    0,   12,
    5,    6,    0,   20,    7,    8,    9,   10,    0,   13,
    0,    0,    0,   11,    0,    5,    6,   12,   14,    7,
    8,    9,   10,    0,    0,    0,    0,   13,   11,    0,
    5,    6,   12,    0,    7,    8,  225,   10,    0,    0,
    0,    0,   13,   11,    0,  238,    6,   12,    0,    7,
    8,  237,   10,    0,    0,    0,    0,   13,   11,    0,
  240,    6,   12,    0,    7,    8,  173,   10,    0,    0,
    0,    0,   13,   11,    0,    5,    6,   12,    0,    7,
    8,  239,   10,    0,  160,    0,    0,   13,   11,    0,
    6,    6,   12,    0,    6,    6,  241,    6,   33,   34,
   35,    0,   13,    6,    0,    0,    0,    6,    5,    6,
   36,  268,    7,    8,    9,   10,    0,    6,    0,    0,
    0,   11,    0,    0,    0,   12,    6,   72,   73,   74,
   75,   76,   77,   78,   79,   13,   80,   81,    0,    0,
    5,    6,    0,   82,    7,    8,    9,   10,    0,    0,
  118,    0,    0,   11,    0,   93,   93,   12,    0,   93,
   93,   93,   93,   32,   33,   34,   35,   13,   93,    0,
   91,    0,   93,   92,   96,   82,   36,    0,    0,   92,
   92,  154,   93,   92,   92,   92,   92,    0,    0,   93,
   93,    0,   92,    0,   94,   94,   92,    0,   94,   94,
   94,   94,    0,   33,   34,   35,   92,   94,    0,    0,
    0,   94,    0,   92,   92,   36,   58,    0,   91,   91,
   59,   94,   91,   91,   91,   91,    0,    0,   94,   94,
    0,   91,    0,    5,    6,   91,    0,    7,    8,    9,
   10,   32,   33,   34,   35,   91,   11,    0,    0,    0,
   12,    0,   91,   91,   36,    0,    0,  176,    6,    0,
   13,    7,    8,    9,   10,    0,    0,    0,   82,    0,
   11,    0,  212,    6,   12,    0,    7,    8,    9,   10,
    0,    0,    0,    0,   13,   11,    0,    0,    0,   12,
    0,    0,   82,    5,    6,    0,    0,    7,    8,   13,
   10,    0,    0,    0,  252,    6,   11,   82,    7,    8,
   12,   10,  263,   33,   34,   35,    0,   11,    0,    0,
   13,   12,   50,    0,    0,   36,    0,    0,  242,    0,
   50,   13,    0,    0,   65,   32,   33,   34,   35,  242,
    0,    0,   65,  260,   33,   34,   35,    0,   36,    0,
    0,   31,    0,    0,    0,   66,   36,    0,    0,   24,
    0,    0,    0,   66,   32,   33,   34,   35,    0,   76,
   77,   78,   79,   26,   26,   26,   24,   36,   90,  127,
  151,    0,    0,    0,   37,   26,   26,    0,    0,   24,
   26,   24,   32,   33,   34,   35,  138,   72,   73,   74,
   75,   91,  140,    0,   92,   36,   80,   81,    0,   32,
   33,   34,   35,    0,    0,   32,   33,   34,   35,  142,
    0,    0,   36,  144,    0,    0,    0,  146,   36,    0,
    0,    0,   32,   33,   34,   35,   32,   33,   34,   35,
   32,   33,   34,   35,    0,   36,    0,    0,    0,   36,
   24,    0,    0,   36,    0,   24,   24,   24,   24,    0,
    0,    0,   24,   61,    0,    0,  125,    0,   61,   61,
   61,   61,    0,    0,    0,    0,    0,    0,    0,  126,
   72,   73,   74,   75,   76,   77,   78,   79,    0,   80,
   81,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
   13,  256,   43,  132,  256,  257,  258,  259,  260,  261,
  262,  263,    4,  256,   27,  186,  256,  269,  269,  256,
  291,  273,  256,  256,  256,  289,   31,  270,  256,    6,
  270,  283,  256,  258,  289,   12,  288,  208,   43,   27,
  292,  289,  269,  210,  211,   58,  256,  257,  258,  259,
  260,  261,  262,  263,  291,   96,  290,  290,  290,  269,
   37,  289,  289,  273,  289,  289,  256,   44,   45,   46,
   58,  256,  289,  283,   76,  242,  288,   79,  288,  269,
  265,  256,  292,  256,   61,   62,  256,  262,  256,   91,
   92,   96,  256,  106,  135,  136,  269,  226,  227,  228,
  229,  230,  231,  232,  233,  234,  235,  236,   85,  279,
  286,  279,  282,   90,  282,  279,   93,  269,  282,  256,
  290,  256,  290,   77,   78,  256,  290,  132,  256,  258,
  135,  136,  269,  256,  269,  256,  264,  258,  259,  262,
   94,   95,  273,  287,  121,  122,  256,  257,  258,  259,
  260,  261,  262,  263,  267,  286,  197,  288,  256,  269,
  287,  259,  269,  273,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  270,  271,  288,  256,
  290,  291,  292,  274,  256,  257,  258,  259,  260,  261,
  262,  263,  197,  270,  271,  272,  262,  269,  259,  279,
  279,  273,  282,  282,  209,  282,  270,  271,  272,  287,
  290,  283,  290,  287,  259,  287,  290,  209,  282,  224,
  292,  226,  227,  228,  229,  230,  231,  232,  233,  234,
  235,  236,  224,  229,  230,  231,  232,  233,  234,  235,
  236,  254,  255,  256,  257,  256,  257,  258,  259,  260,
  261,  262,  263,  258,  279,  280,  281,  282,  269,  280,
  281,  264,  273,  274,  275,  276,  277,  278,  279,  288,
  289,  282,  283,  284,  285,  287,  269,  288,  290,  290,
  291,  292,  256,  257,  258,  259,  260,  261,  262,  263,
  287,  256,  264,  290,  259,  269,  255,  256,  257,  273,
  274,  275,  276,  277,  278,  279,  259,  270,  282,  283,
  284,  285,  280,  281,  288,  270,  290,  291,  292,  256,
  257,  258,  259,  260,  261,  262,  263,  270,  269,  291,
  290,  288,  269,  259,  265,  265,  273,  274,  275,  276,
  277,  278,  279,  265,  265,  282,  283,  284,  285,  291,
  259,  288,  270,  290,  291,  292,  256,  257,  258,  259,
  260,  261,  262,  263,  270,  270,  270,  270,   17,  269,
  290,  290,  289,  273,  274,  275,  276,  277,  278,  279,
  290,  289,  282,  283,  284,  285,  274,  289,  288,  290,
  290,  291,  292,  256,  257,  258,  259,  260,  261,  262,
  263,  290,  289,  288,  290,  108,  269,  290,  290,  290,
  273,  274,  275,  276,  277,  278,  279,   49,  124,  282,
  283,  284,  285,  163,   37,  288,  177,  290,  291,  292,
  256,  257,  258,  259,  260,  261,  262,  263,   39,   -1,
   -1,   -1,   -1,  269,   -1,   -1,   -1,  273,  274,  275,
  276,  277,  278,  279,   -1,   -1,  282,  283,  284,  285,
   -1,   -1,  288,   -1,  290,  291,  292,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,
  269,   -1,   -1,   -1,  273,  274,  275,  276,  277,  278,
  279,   -1,   -1,  282,  283,  284,  285,   -1,   -1,  288,
   -1,  290,  291,  292,  256,  257,  258,  259,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,   -1,
   -1,  273,  256,  257,  258,  259,  260,  261,  262,  263,
  256,  283,   -1,   -1,   -1,  269,  288,   -1,   -1,  273,
  292,   -1,   -1,  269,  270,  271,  272,   -1,   -1,  283,
   -1,   -1,   -1,   -1,  288,   -1,  282,   -1,  292,  256,
  257,  258,  259,  260,  261,  262,  263,   -1,   -1,   -1,
   -1,   -1,  269,   -1,   -1,   -1,  273,  256,  257,  258,
  259,  260,  261,  262,  263,  256,  283,   -1,   -1,   -1,
  269,  288,   -1,   -1,  273,  292,   -1,   -1,  269,  270,
  271,  272,   -1,   -1,  283,   -1,   -1,   -1,   -1,  288,
   -1,  282,   -1,  292,  256,  257,  258,  259,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,   -1,
   -1,  273,  256,  257,  258,  259,  260,  261,  262,  263,
  256,  283,   -1,   -1,   -1,  269,  288,   -1,   -1,  273,
  292,   -1,   -1,  269,  270,  271,  272,   -1,   -1,  283,
   -1,   -1,   -1,   -1,  288,   -1,  282,   -1,  292,  256,
  257,  258,  259,  260,  261,  262,  263,   -1,   -1,   -1,
   -1,   -1,  269,   -1,   -1,   -1,  273,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  283,   -1,   -1,   -1,
  269,   -1,  256,  257,  273,  292,  260,  261,  262,  263,
   -1,   -1,   -1,   -1,  283,  269,   -1,   -1,   -1,  273,
  256,  257,   -1,  292,  260,  261,  262,  263,   -1,  283,
   -1,   -1,   -1,  269,   -1,  256,  257,  273,  292,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,  283,  269,   -1,
  256,  257,  273,   -1,  260,  261,  292,  263,   -1,   -1,
   -1,   -1,  283,  269,   -1,  256,  257,  273,   -1,  260,
  261,  292,  263,   -1,   -1,   -1,   -1,  283,  269,   -1,
  256,  257,  273,   -1,  260,  261,  292,  263,   -1,   -1,
   -1,   -1,  283,  269,   -1,  256,  257,  273,   -1,  260,
  261,  292,  263,   -1,  256,   -1,   -1,  283,  269,   -1,
  256,  257,  273,   -1,  260,  261,  292,  263,  270,  271,
  272,   -1,  283,  269,   -1,   -1,   -1,  273,  256,  257,
  282,  292,  260,  261,  262,  263,   -1,  283,   -1,   -1,
   -1,  269,   -1,   -1,   -1,  273,  292,  275,  276,  277,
  278,  279,  280,  281,  282,  283,  284,  285,   -1,   -1,
  256,  257,   -1,  291,  260,  261,  262,  263,   -1,   -1,
  256,   -1,   -1,  269,   -1,  256,  257,  273,   -1,  260,
  261,  262,  263,  269,  270,  271,  272,  283,  269,   -1,
  279,   -1,  273,  282,  290,  291,  282,   -1,   -1,  256,
  257,  290,  283,  260,  261,  262,  263,   -1,   -1,  290,
  291,   -1,  269,   -1,  256,  257,  273,   -1,  260,  261,
  262,  263,   -1,  270,  271,  272,  283,  269,   -1,   -1,
   -1,  273,   -1,  290,  291,  282,  283,   -1,  256,  257,
  287,  283,  260,  261,  262,  263,   -1,   -1,  290,  291,
   -1,  269,   -1,  256,  257,  273,   -1,  260,  261,  262,
  263,  269,  270,  271,  272,  283,  269,   -1,   -1,   -1,
  273,   -1,  290,  291,  282,   -1,   -1,  256,  257,   -1,
  283,  260,  261,  262,  263,   -1,   -1,   -1,  291,   -1,
  269,   -1,  256,  257,  273,   -1,  260,  261,  262,  263,
   -1,   -1,   -1,   -1,  283,  269,   -1,   -1,   -1,  273,
   -1,   -1,  291,  256,  257,   -1,   -1,  260,  261,  283,
  263,   -1,   -1,   -1,  256,  257,  269,  291,  260,  261,
  273,  263,  269,  270,  271,  272,   -1,  269,   -1,   -1,
  283,  273,  256,   -1,   -1,  282,   -1,   -1,  291,   -1,
  256,  283,   -1,   -1,  268,  269,  270,  271,  272,  291,
   -1,   -1,  268,  269,  270,  271,  272,   -1,  282,   -1,
   -1,  256,   -1,   -1,   -1,  289,  282,   -1,   -1,  256,
   -1,   -1,   -1,  289,  269,  270,  271,  272,   -1,  279,
  280,  281,  282,  270,  271,  272,  273,  282,  256,  256,
  290,   -1,   -1,   -1,  289,  282,  283,   -1,   -1,  286,
  287,  288,  269,  270,  271,  272,  256,  275,  276,  277,
  278,  279,  256,   -1,  282,  282,  284,  285,   -1,  269,
  270,  271,  272,   -1,   -1,  269,  270,  271,  272,  256,
   -1,   -1,  282,  256,   -1,   -1,   -1,  256,  282,   -1,
   -1,   -1,  269,  270,  271,  272,  269,  270,  271,  272,
  269,  270,  271,  272,   -1,  282,   -1,   -1,   -1,  282,
  274,   -1,   -1,  282,   -1,  279,  280,  281,  282,   -1,
   -1,   -1,  286,  274,   -1,   -1,  290,   -1,  279,  280,
  281,  282,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  290,
  275,  276,  277,  278,  279,  280,  281,  282,   -1,  284,
  285,
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
"tipo : INT",
"sentencia : declaracion_variable",
"sentencia : sentencia_ejec PUNTOYCOMA",
"sentencia : sentencia_ejec",
"sentencia : declaracion_funcion",
"sentencia_ejec : asign_simple",
"sentencia_ejec : asign_multiple",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion",
"sentencia_ejec : return_sent",
"declaracion_variable : tipo ID PUNTOYCOMA",
"declaracion_variable : tipo lista_ids",
"declaracion_funcion : tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
"declaracion_funcion : declaracion_funcion_error",
"declaracion_funcion_error : tipo PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
"var_ref : ID",
"var_ref : var_ref PUNTO ID",
"lista_ids : ID",
"lista_ids : lista_ids COMA ID",
"lista_ids : lista_ids COMA error",
"asign_simple : var_ref ASIGN expresion",
"asign_simple : var_ref ASIGN error",
"asign_simple : ASIGN expresion",
"asign_simple : var_ref error expresion",
"asign_simple : var_ref PUNTOYCOMA",
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

//#line 357 "gramatica.y"

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
    //System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
	if (ERR != null) ERR.add(linea, "Error sintactico: " + mensaje);

}
//#line 769 "Parser.java"
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
//#line 79 "gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 10:
//#line 89 "gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 17:
//#line 100 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 19:
//#line 104 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable"); n_var = 0;}
break;
case 20:
//#line 105 "gramatica.y"
{ yyerror("Falta ';' al final de la declaracion."); n_var = 0;}
break;
case 21:
//#line 108 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
break;
case 23:
//#line 112 "gramatica.y"
{ yyerror("Falta el nombre de la funcion"); }
break;
case 26:
//#line 130 "gramatica.y"
{n_var++;}
break;
case 27:
//#line 131 "gramatica.y"
{n_var++;}
break;
case 28:
//#line 132 "gramatica.y"
{ yyerror("Se esperaba un identificador despues de coma");}
break;
case 29:
//#line 139 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Asignacion"); }
break;
case 30:
//#line 140 "gramatica.y"
{ yyerror("Falta expresion despues de ':=' en asignacion."); }
break;
case 31:
//#line 141 "gramatica.y"
{ yyerror("Falta variable antes de ':=' en asignacion."); }
break;
case 32:
//#line 142 "gramatica.y"
{ yyerror("Falta ':=' en asignacion."); }
break;
case 33:
//#line 143 "gramatica.y"
{ yyerror("Falta ':=' y expresion en asignacion."); }
break;
case 34:
//#line 149 "gramatica.y"
{
					if (n_var < n_cte) {
						yyerror("Error: mas constantes que variables en la asignacion");
					} else {
						System.out.println("Asignacion valida (" + n_var + ", " + n_cte + ")");
						SINT.add(lex.getLineaActual(), "Asignacion multiple");
					}
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 35:
//#line 158 "gramatica.y"
{ yyerror("Falta lista de variables antes del '='"); }
break;
case 36:
//#line 159 "gramatica.y"
{ yyerror("Falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 37:
//#line 160 "gramatica.y"
{ yyerror("Falta lista de constantes despues del '='");}
break;
case 38:
//#line 163 "gramatica.y"
{n_cte++;}
break;
case 39:
//#line 164 "gramatica.y"
{n_cte++;}
break;
case 40:
//#line 165 "gramatica.y"
{ yyerror("Falta una constante despues de coma");}
break;
case 42:
//#line 171 "gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 43:
//#line 178 "gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor = entrada.getLexema();
			valor = valor.substring(0, valor.length() - 1); /*nos quedamos con el numero sin el I final*/
			int num = Integer.parseInt(valor);
			int max = 32767;
			/*al ser positivo debemos chequear el maximo*/
			if (num > max) {
				String msg = "Error lexico: constante entera fuera de rango: " + num;
				logLexError(msg);
				tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea());
			}

			yyval = val_peek(0);
		}
break;
case 44:
//#line 193 "gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 48:
//#line 208 "gramatica.y"
{ yyerror("Falta operando derecho despues de '+' en expresion."); }
break;
case 49:
//#line 209 "gramatica.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresion."); }
break;
case 51:
//#line 211 "gramatica.y"
{ yyerror("Falta operando derecho despues de '-' en expresion."); }
break;
case 52:
//#line 212 "gramatica.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresion."); }
break;
case 55:
//#line 219 "gramatica.y"
{ yyerror("Falta operando derecho despues de '' en expresion."); }
break;
case 56:
//#line 220 "gramatica.y"
{ yyerror("Falta operando izquierdo antes de '' en expresion."); }
break;
case 58:
//#line 222 "gramatica.y"
{ yyerror("Falta operando derecho despues de '/' en expresion."); }
break;
case 59:
//#line 223 "gramatica.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresion."); }
break;
case 63:
//#line 237 "gramatica.y"
{ yyerror("Llamada a funcion sin nombre");}
break;
case 67:
//#line 246 "gramatica.y"
{ yyerror("Falta identificador despues de '->' en parametro real");}
break;
case 69:
//#line 250 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 70:
//#line 251 "gramatica.y"
{ yyerror("Falta ')' en llamada a funcion con 'trunc'.");}
break;
case 71:
//#line 252 "gramatica.y"
{ yyerror("Falta '(' en llamada a funcion con 'trunc'.");}
break;
case 72:
//#line 253 "gramatica.y"
{ yyerror("Faltan los parentesis en llamada a funcion con 'trunc'.");}
break;
case 74:
//#line 258 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Return"); }
break;
case 78:
//#line 268 "gramatica.y"
{ yyerror("Falta identificador despues de 'int' en parametro formal");}
break;
case 79:
//#line 269 "gramatica.y"
{ yyerror("Falta tipo en parametro formal");}
break;
case 82:
//#line 280 "gramatica.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 83:
//#line 282 "gramatica.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 84:
//#line 283 "gramatica.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 85:
//#line 284 "gramatica.y"
{ yyerror("Faltan los parentesis en sentencia if."); }
break;
case 86:
//#line 285 "gramatica.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 87:
//#line 286 "gramatica.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 88:
//#line 289 "gramatica.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 89:
//#line 290 "gramatica.y"
{ yyerror("Falta bloque del then."); }
break;
case 90:
//#line 291 "gramatica.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 92:
//#line 296 "gramatica.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 93:
//#line 297 "gramatica.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 94:
//#line 298 "gramatica.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 104:
//#line 315 "gramatica.y"
{ yyerror("Falta bloque del else."); }
break;
case 105:
//#line 320 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 106:
//#line 321 "gramatica.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 107:
//#line 322 "gramatica.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 108:
//#line 323 "gramatica.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 109:
//#line 324 "gramatica.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 110:
//#line 325 "gramatica.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 111:
//#line 326 "gramatica.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 112:
//#line 327 "gramatica.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 113:
//#line 328 "gramatica.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 114:
//#line 329 "gramatica.y"
{ yyerror("Falta bloque del for."); }
break;
case 117:
//#line 338 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 118:
//#line 339 "gramatica.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 119:
//#line 340 "gramatica.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 120:
//#line 341 "gramatica.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 121:
//#line 346 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Lambda"); }
break;
case 122:
//#line 347 "gramatica.y"
{ yyerror("Falta '{' en expresion lambda."); }
break;
case 123:
//#line 348 "gramatica.y"
{ yyerror("Falta '}' en expresion lambda."); }
break;
case 124:
//#line 349 "gramatica.y"
{ yyerror("Faltan los delimitadores '{}' en expresion lambda."); }
break;
//#line 1248 "Parser.java"
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
