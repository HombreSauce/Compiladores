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
    0,    1,    1,    3,    3,    3,    3,    5,    2,    2,
    2,    2,    2,    4,    4,    4,    4,    4,    4,    6,
    6,   15,   15,   15,    7,    7,    7,    7,    7,    8,
    8,   18,   17,   17,   17,   19,   19,   19,   20,   20,
    9,   10,   10,   10,   23,   23,   23,   23,   22,   22,
   22,   22,   21,   21,   16,   16,   16,   16,   16,   16,
   16,   25,   25,   25,   25,   25,   25,   25,   25,   26,
   26,   24,   24,   24,   24,   24,   11,   11,   11,   30,
   30,   30,   30,   30,   30,   30,   30,   30,   30,   27,
   27,   27,   31,   31,   31,   31,   31,   31,   28,   28,
   28,   28,   29,   29,   29,   29,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   32,   32,   32,   13,
   13,   13,   13,   14,   33,   33,   34,   34,   35,   35,
   35,   35,   35,   35,   36,   36,   36,   36,   37,   37,
};
final static short yylen[] = {                            2,
    4,    0,    2,    3,    2,    1,    2,    1,    1,    1,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    2,    1,    3,    3,    5,    4,    5,    5,    5,    8,
    1,    7,    1,    3,    3,    3,    3,    2,    0,    1,
    3,    3,    2,    3,    1,    3,    3,    2,    1,    3,
    3,    2,    1,    3,    1,    3,    3,    3,    3,    3,
    3,    1,    3,    3,    3,    3,    3,    3,    3,    1,
    1,    1,    2,    1,    2,    1,    6,    8,    1,    5,
    5,    4,    7,    7,    6,    3,    5,    6,    8,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    2,    3,
    2,    0,    2,    3,    2,    0,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    1,    3,    2,    4,
    4,    4,    3,    4,    1,    3,    3,    3,    1,    4,
    3,    4,    3,    1,   10,    9,    9,    8,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    8,    0,    0,    0,
    1,    3,    0,    0,    9,   10,   13,   14,   15,   16,
   17,   18,   19,   31,    0,    0,   79,    0,   74,   72,
   76,    0,    0,    0,    0,    0,    0,   71,    0,   62,
    0,    0,    0,    0,    0,    0,    0,    0,   45,   11,
    0,    0,    0,    0,    0,    0,   53,    0,    0,    0,
   95,   96,   97,   98,    0,    0,    0,    0,   94,   93,
    0,   75,   73,    0,    0,    0,  101,    0,    0,   99,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   86,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  125,    0,  134,    0,   48,    0,    0,    0,    0,
   40,    0,   33,    0,    0,   20,    0,   54,   44,    0,
   51,    0,    0,    0,   65,   68,    0,    0,    0,    0,
    0,  100,    0,    5,    0,    0,    0,    0,    0,    0,
   69,   64,   63,   67,   66,    0,    0,   82,    0,    0,
    0,  122,  121,  120,    0,    0,    0,    0,    0,    0,
    0,    0,  124,    0,   47,   46,    0,    0,    0,    0,
    0,    0,    0,   38,    0,   24,   23,    0,    0,    0,
   81,    4,    0,   80,    0,  105,    0,  103,   87,    0,
    0,    0,    0,    0,    0,    0,    0,  126,  128,  127,
   29,   28,   27,   25,    0,   35,   34,    2,   37,   36,
    0,   88,    0,   77,    0,    0,   85,  104,    0,    0,
    0,    0,    0,    0,  132,  130,    0,    2,    0,    0,
    0,   84,   83,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   32,   89,   78,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   30,    0,
  117,  115,  108,  109,  110,  111,  112,  113,  114,  116,
  107,    0,    0,  139,  140,    0,    0,  119,    0,    0,
    0,  138,    0,  118,  137,    0,  136,  135,
};
final static short yydgoto[] = {                          2,
    4,   12,   78,  261,   14,   15,   16,   17,   18,   19,
   20,   21,   22,   23,   54,   36,  112,   24,  113,  114,
   25,   26,   48,   38,   39,   40,   41,   42,  151,   27,
   71,  262,  101,  102,  103,  104,  276,
};
final static short yysindex[] = {                      -202,
 -253,    0,    0,  279,  -83, -231,    0, -230, -186,  286,
    0,    0, -181, -247,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -213, -163,    0,  699,    0,    0,
    0, -133,  449,  372, -167,  710, -213,    0, -172,    0,
  223,  -93,  535,  620, -136, -190,  541,  683,    0,    0,
  -68, -227,  -92,  -29,  535,  -79,    0, -238, -142,  -54,
    0,    0,    0,    0,  624,  550,  550,  624,    0,    0,
  535,    0,    0,  572,  -54,  496,    0,  380,  -14,    0,
  628,  645,  649,  550,  653,  670, -147,   25,  507,    0,
   60,  -35,   66,   93,  -50,  -28,  -30, -228,   -9,  -39,
  -33,    0,   31,    0,  677,    0,  535,  535,  674,  -92,
    0,  -19,    0, -248,  -98,    0,  -39,    0,    0,  683,
    0,  -54,    9, -172,    0,    0, -172,  -39, -147, -147,
   51,    0,   23,    0,    9, -172,    9, -172,   60,  -39,
    0,    0,    0,    0,    0,   61,  507,    0,  399,   37,
   77,    0,    0,    0,   73,   85,  112, -170,  535,  535,
  122,  541,    0,  -74,    0,    0, -177,  -12,  168,  311,
   17,  -65,  106,    0,  -72,    0,    0,  103,  118,  507,
    0,    0,  507,    0,  139,    0,  410,    0,    0,  148,
  149,  150,  152,  -97,  102,  129,  128,    0,    0,    0,
    0,    0,    0,    0,  121,    0,    0,    0,    0,    0,
  507,    0,  507,    0,  161,  169,    0,    0,  157,  163,
  164,  174,  181, -155,    0,    0,  512,    0,  304,  194,
  195,    0,    0, -249,  165,  173,  179,  180,  197, -243,
   43,  -41,  312,    0,    0,    0,  525,  525,  525,  525,
  525,  525,  525,  525, -252,  319,  691,  175,    0,  414,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  691,  196,    0,    0,  199,  691,    0,  418,  200,
  691,    0,  201,    0,    0,  209,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  120,    0,    0,    0,  558,    0,
    0,    0,  323,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -102,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  590,    0,    3,    0,
  120,    0,    0,    0,    0,    0,    0,  -59,    0,    0,
    0,  -42, -198,  350,    0,    0,    0,    0,    0,  -91,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -128,  120,    0,    0,  334,    0,
    0,    0,    0,    0,    0,    0,  120,    0,  225,    0,
    0,    0,    0,  255,    0,    0,    0,    0,    0,  226,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -198,
    0,    0,    0,    0,    0,    0,  263,    0,    0,  271,
    0,  -52,    0,   39,    0,    0,   75,  454,  120,  120,
    0,    0,  361,    0,  111,  147,  183,  219,  467,  483,
    0,    0,    0,    0,    0,    0,  225,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  365,
    0, -198,    0,    0,    0,    0,    0,    0,    0,  225,
    0,    0,  225,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  231,  242,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  225,    0,  225,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
 -160,    0, -137,   -4,  -44,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -20,  409,    0,  349,    0,
   10,    0,  464,   -8,  -62,  127,  494,  -31, -131,    0,
  493,  748,    0,  368,    0,    0,  328,
};
final static int YYTABLESIZE=1003;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         13,
   35,   49,  124,  270,    5,  127,  247,    6,   51,   88,
    8,  187,  254,    7,   37,  185,    9,  119,  136,  138,
  174,   52,   92,   94,   43,   45,  100,  159,  108,   79,
   10,   29,   30,   31,  117,   60,   35,    3,  260,  106,
  248,   53,   75,   32,  131,  109,  255,  229,  215,   49,
  128,  216,   75,   75,  161,  146,   75,   44,   46,   55,
  160,  110,  140,   39,   75,   96,    1,  243,  122,  175,
   39,   35,   56,  133,   75,   75,   75,   75,   97,  230,
   75,  231,   35,   84,  150,  193,  167,  168,  170,  242,
   75,   75,   75,   75,   75,   75,  166,  178,  179,  194,
  239,   81,   47,  256,   82,   57,   50,   85,   86,    5,
  201,  106,    6,  121,  240,    8,   75,   75,   75,   58,
   80,    9,  279,   59,   35,   35,   57,   70,   70,   70,
   70,   70,   95,   70,   70,   10,   72,   73,  195,  196,
   70,  100,  150,   34,   79,   70,   70,   70,   70,   70,
   70,   70,   70,   70,   70,   70,   70,  176,  223,   70,
   70,   70,   70,   70,   89,   90,   49,  224,   75,   75,
  177,   75,   28,    5,  111,  150,    6,   52,  150,    8,
   49,  199,  133,  209,   49,    9,   29,   30,   31,  118,
  206,   52,  125,  126,  200,   52,  210,   43,   32,   10,
   43,  111,   43,   43,  107,   33,  150,   34,  150,   43,
  141,  143,  145,  155,   22,    5,   50,   22,    6,   22,
   22,    8,   79,   43,   13,  157,   22,    9,   43,   43,
   50,   56,   43,  158,   50,  156,   79,  133,   13,   81,
   22,   10,   82,   81,   22,   22,   82,  257,  275,   22,
  258,  133,    7,  162,  152,   79,  163,  115,  116,   55,
   55,   55,   55,  275,   55,   55,   81,  172,  275,   82,
  173,   55,  275,  134,  133,  202,   55,   55,   55,   55,
   55,   55,  147,  148,   55,   55,   55,   55,   66,   67,
   55,   55,   55,   55,   55,   58,   58,   58,   58,    5,
   58,   58,    6,  172,  164,    8,  205,   58,  180,  181,
  182,    9,   58,   58,   58,   58,   58,   58,  183,  184,
   58,   58,   58,   58,  188,   10,   58,   58,   58,   58,
   58,   61,   61,   61,   61,  189,   61,   61,   65,   66,
   67,   68,  190,   61,   65,   66,   67,   68,   61,   61,
   61,   61,   61,   61,  191,  153,   61,   61,   61,   61,
  211,  212,   61,   61,   61,   61,   61,   57,   57,   57,
   57,   81,   57,   57,   82,  213,  214,  102,  102,   57,
   81,  192,  154,   82,   57,   57,   57,   57,   57,   57,
  197,  225,   57,   57,   57,   57,  208,  217,   57,   57,
   57,   57,   57,   56,   56,   56,   56,   81,   56,   56,
   82,  228,  219,  220,  221,   56,  222,  227,  226,  232,
   56,   56,   56,   56,   56,   56,  234,  233,   56,   56,
   56,   56,  235,  236,   56,   56,   56,   56,   56,   60,
   60,   60,   60,  237,   60,   60,   65,   66,   67,   68,
  238,   60,  245,  246,  249,  203,   60,   60,   60,   60,
   60,   60,  250,  277,   60,   60,   60,   60,  251,  252,
   60,   60,   60,   60,   60,   59,   59,   59,   59,    5,
   59,   59,    6,  106,  281,    8,  253,   59,  282,  285,
  287,    9,   59,   59,   59,   59,   59,   59,  288,  129,
   59,   59,   59,   59,  133,   10,   59,   59,   59,   59,
   59,  123,   87,   34,  123,  131,  123,  123,  171,   41,
  207,  120,   41,  123,   41,   41,   76,   42,   83,  198,
   42,   41,   42,   42,    0,    5,    0,  123,    6,   42,
    7,    8,  123,  123,    0,   41,  123,    9,    0,    0,
   41,   41,    0,   42,   41,   29,   30,   31,   42,   42,
    5,   10,   42,    6,    0,    7,    8,   32,    5,    0,
   11,    6,    9,    7,    8,    5,    0,    0,    6,   12,
    9,    8,   12,    0,   12,   12,   10,    9,    0,   81,
    6,   12,   82,    6,   10,  244,    6,    0,  204,  280,
    0,   10,    6,  259,  283,   12,   21,  272,  286,   21,
  273,   21,   21,    0,   12,    0,    6,    7,   21,    0,
    7,   26,    6,    7,   26,    6,   26,   26,    5,    7,
    0,    6,   21,   26,    8,    0,    5,    0,    0,    6,
    9,   21,    8,    7,    0,    0,    0,   26,    9,    7,
    0,    0,    7,    0,   10,    5,   26,    0,    6,    0,
    0,    8,   10,   77,    0,    0,    5,    9,    0,    6,
    5,  132,    8,    6,    5,    0,    8,    6,    9,    0,
    8,   10,    9,    0,    0,    0,    9,    0,    0,    0,
  186,    0,   10,    0,    0,    0,   10,    0,    0,    0,
   10,  218,    0,    0,   74,  278,    0,    0,    0,  284,
   91,   91,   91,   91,    0,    0,   91,   57,   29,   30,
   31,    0,   91,   92,   92,   92,   92,    0,    0,   92,
   32,    0,    0,    0,    0,   92,   91,    0,    0,   90,
   90,   90,   90,   91,   91,   90,    0,    0,    0,   92,
    0,   90,    5,    0,    0,    6,   92,   92,    8,    0,
    0,    0,    0,    5,    9,   90,    6,    0,    5,    8,
    0,    6,   90,   90,    8,    9,    0,    0,   10,    0,
    9,    5,    0,    0,    6,  130,   34,    8,    0,   10,
   91,    0,    0,    9,   10,    0,   91,  149,    0,    0,
    0,    0,  241,   57,   29,   30,   31,   10,   98,   57,
   29,   30,   31,   53,    0,  260,   32,    0,   57,   29,
   30,   31,   32,    0,    0,    0,   53,    0,    0,   99,
   53,   32,   53,   53,   53,   53,   53,   53,   53,   53,
   53,   53,   53,   53,   53,   70,   61,   62,   63,   64,
   65,   66,   67,   68,    0,   69,   70,    0,   49,    0,
    0,  129,    0,    0,   70,   70,   70,   70,   70,   70,
   70,   70,   49,   70,   70,   93,   49,    0,    0,  123,
    0,    0,    0,  135,    0,    0,    0,    0,   57,   29,
   30,   31,   57,   29,   30,   31,   57,   29,   30,   31,
  137,   32,    0,    0,  139,   32,    0,    0,  142,   32,
    0,    0,    0,   57,   29,   30,   31,   57,   29,   30,
   31,   57,   29,   30,   31,  144,   32,    0,    0,  169,
   32,    0,  165,    0,   32,    0,    0,    0,   57,   29,
   30,   31,   57,   29,   30,   31,   29,   30,   31,    0,
    0,   32,   29,   30,   31,   32,    0,    0,   32,  274,
   29,   30,   31,    0,   32,    0,    0,    0,    0,  105,
    0,    0,   32,   61,   62,   63,   64,   65,   66,   67,
   68,    0,   69,   70,   61,   62,   63,   64,   81,    0,
    0,   82,    0,   69,   70,  263,  264,  265,  266,  267,
  268,  269,  271,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    5,   10,   65,  256,  257,   68,  256,  260,  256,   41,
  263,  149,  256,  262,    5,  147,  269,  256,   81,   82,
  269,  269,   43,   44,  256,  256,   47,  256,  256,   34,
  283,  270,  271,  272,   55,   26,   41,  291,  291,   48,
  290,  289,   33,  282,   76,  273,  290,  208,  180,   58,
   71,  183,   43,   44,   99,   87,   47,  289,  289,  273,
  289,  289,   83,  262,   55,  256,  269,  228,   59,  114,
  269,   76,  286,   78,   65,   66,   67,   68,  269,  211,
   71,  213,   87,  256,   89,  256,  107,  108,  109,  227,
   81,   82,   83,   84,   85,   86,  105,  129,  130,  270,
  256,  279,  289,  241,  282,  269,  288,  280,  281,  257,
  288,  120,  260,  256,  270,  263,  107,  108,  109,  283,
  288,  269,  260,  287,  129,  130,  269,  256,  257,  258,
  259,  260,  269,  262,  263,  283,  270,  271,  159,  160,
  269,  162,  147,  291,  149,  274,  275,  276,  277,  278,
  279,  280,  281,  282,  283,  284,  285,  256,  256,  288,
  289,  290,  291,  292,  258,  259,  269,  265,  159,  160,
  269,  162,  256,  257,  267,  180,  260,  269,  183,  263,
  283,  256,  187,  256,  287,  269,  270,  271,  272,  269,
  256,  283,   66,   67,  269,  287,  269,  257,  282,  283,
  260,  267,  262,  263,  273,  289,  211,  291,  213,  269,
   84,   85,   86,  264,  257,  257,  269,  260,  260,  262,
  263,  263,  227,  283,  229,  256,  269,  269,  288,  289,
  283,  286,  292,  264,  287,  264,  241,  242,  243,  279,
  283,  283,  282,  279,  287,  288,  282,  289,  257,  292,
  292,  256,  262,  287,  290,  260,  290,  287,  288,  257,
  258,  259,  260,  272,  262,  263,  279,  287,  277,  282,
  290,  269,  281,  288,  279,  288,  274,  275,  276,  277,
  278,  279,  258,  259,  282,  283,  284,  285,  280,  281,
  288,  289,  290,  291,  292,  257,  258,  259,  260,  257,
  262,  263,  260,  287,  274,  263,  290,  269,  258,  259,
  288,  269,  274,  275,  276,  277,  278,  279,  258,  259,
  282,  283,  284,  285,  288,  283,  288,  289,  290,  291,
  292,  257,  258,  259,  260,  259,  262,  263,  279,  280,
  281,  282,  270,  269,  279,  280,  281,  282,  274,  275,
  276,  277,  278,  279,  270,  290,  282,  283,  284,  285,
  258,  259,  288,  289,  290,  291,  292,  257,  258,  259,
  260,  279,  262,  263,  282,  258,  259,  258,  259,  269,
  279,  270,  290,  282,  274,  275,  276,  277,  278,  279,
  269,  290,  282,  283,  284,  285,  291,  259,  288,  289,
  290,  291,  292,  257,  258,  259,  260,  279,  262,  263,
  282,  291,  265,  265,  265,  269,  265,  290,  290,  259,
  274,  275,  276,  277,  278,  279,  270,  259,  282,  283,
  284,  285,  270,  270,  288,  289,  290,  291,  292,  257,
  258,  259,  260,  270,  262,  263,  279,  280,  281,  282,
  270,  269,  259,  259,  290,  288,  274,  275,  276,  277,
  278,  279,  290,  289,  282,  283,  284,  285,  290,  290,
  288,  289,  290,  291,  292,  257,  258,  259,  260,  257,
  262,  263,  260,  259,  289,  263,  290,  269,  290,  290,
  290,  269,  274,  275,  276,  277,  278,  279,  290,  274,
  282,  283,  284,  285,  274,  283,  288,  289,  290,  291,
  292,  257,  290,  291,  260,  274,  262,  263,  110,  257,
  172,   58,  260,  269,  262,  263,   33,  257,   36,  162,
  260,  269,  262,  263,   -1,  257,   -1,  283,  260,  269,
  262,  263,  288,  289,   -1,  283,  292,  269,   -1,   -1,
  288,  289,   -1,  283,  292,  270,  271,  272,  288,  289,
  257,  283,  292,  260,   -1,  262,  263,  282,  257,   -1,
  292,  260,  269,  262,  263,  257,   -1,   -1,  260,  257,
  269,  263,  260,   -1,  262,  263,  283,  269,   -1,  279,
  257,  269,  282,  260,  283,  292,  263,   -1,  288,  272,
   -1,  283,  269,  292,  277,  283,  257,  289,  281,  260,
  292,  262,  263,   -1,  292,   -1,  283,  257,  269,   -1,
  260,  257,  289,  263,  260,  292,  262,  263,  257,  269,
   -1,  260,  283,  269,  263,   -1,  257,   -1,   -1,  260,
  269,  292,  263,  283,   -1,   -1,   -1,  283,  269,  289,
   -1,   -1,  292,   -1,  283,  257,  292,   -1,  260,   -1,
   -1,  263,  283,  292,   -1,   -1,  257,  269,   -1,  260,
  257,  292,  263,  260,  257,   -1,  263,  260,  269,   -1,
  263,  283,  269,   -1,   -1,   -1,  269,   -1,   -1,   -1,
  292,   -1,  283,   -1,   -1,   -1,  283,   -1,   -1,   -1,
  283,  292,   -1,   -1,  256,  292,   -1,   -1,   -1,  292,
  257,  258,  259,  260,   -1,   -1,  263,  269,  270,  271,
  272,   -1,  269,  257,  258,  259,  260,   -1,   -1,  263,
  282,   -1,   -1,   -1,   -1,  269,  283,   -1,   -1,  257,
  258,  259,  260,  290,  291,  263,   -1,   -1,   -1,  283,
   -1,  269,  257,   -1,   -1,  260,  290,  291,  263,   -1,
   -1,   -1,   -1,  257,  269,  283,  260,   -1,  257,  263,
   -1,  260,  290,  291,  263,  269,   -1,   -1,  283,   -1,
  269,  257,   -1,   -1,  260,  290,  291,  263,   -1,  283,
  256,   -1,   -1,  269,  283,   -1,  256,  291,   -1,   -1,
   -1,   -1,  291,  269,  270,  271,  272,  283,  268,  269,
  270,  271,  272,  256,   -1,  291,  282,   -1,  269,  270,
  271,  272,  282,   -1,   -1,   -1,  269,   -1,   -1,  289,
  273,  282,  275,  276,  277,  278,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  256,  275,  276,  277,  278,
  279,  280,  281,  282,   -1,  284,  285,   -1,  269,   -1,
   -1,  290,   -1,   -1,  275,  276,  277,  278,  279,  280,
  281,  282,  283,  284,  285,  256,  287,   -1,   -1,  256,
   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,  269,  270,
  271,  272,  269,  270,  271,  272,  269,  270,  271,  272,
  256,  282,   -1,   -1,  256,  282,   -1,   -1,  256,  282,
   -1,   -1,   -1,  269,  270,  271,  272,  269,  270,  271,
  272,  269,  270,  271,  272,  256,  282,   -1,   -1,  256,
  282,   -1,  256,   -1,  282,   -1,   -1,   -1,  269,  270,
  271,  272,  269,  270,  271,  272,  270,  271,  272,   -1,
   -1,  282,  270,  271,  272,  282,   -1,   -1,  282,  269,
  270,  271,  272,   -1,  282,   -1,   -1,   -1,   -1,  287,
   -1,   -1,  282,  275,  276,  277,  278,  279,  280,  281,
  282,   -1,  284,  285,  275,  276,  277,  278,  279,   -1,
   -1,  282,   -1,  284,  285,  248,  249,  250,  251,  252,
  253,  254,  255,
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
"declaracion_con_asignacion : tipo ID ASIGN expresion PUNTOYCOMA",
"declaracion_con_asignacion : tipo ID ASIGN expresion",
"declaracion_con_asignacion : tipo ID ASIGN error PUNTOYCOMA",
"declaracion_con_asignacion : tipo ID error expresion PUNTOYCOMA",
"declaracion_con_asignacion : tipo error ASIGN expresion PUNTOYCOMA",
"declaracion_funcion : tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
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

//#line 371 "gramaticaDeCero.y"

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
//#line 742 "Parser.java"
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
//#line 71 "gramaticaDeCero.y"
{yyerror("Falta ';' al final de la sentencia.");}
break;
case 7:
//#line 72 "gramaticaDeCero.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 12:
//#line 83 "gramaticaDeCero.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 20:
//#line 97 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable"); }
break;
case 21:
//#line 98 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 24:
//#line 104 "gramaticaDeCero.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 25:
//#line 109 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
break;
case 26:
//#line 110 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
break;
case 27:
//#line 111 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, expresión inválida."); }
break;
case 28:
//#line 112 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
break;
case 29:
//#line 113 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
break;
case 30:
//#line 117 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
break;
case 32:
//#line 121 "gramaticaDeCero.y"
{yyerror("Error sintáctico: Falta nombre identificador de función");}
break;
case 35:
//#line 132 "gramaticaDeCero.y"
{yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
break;
case 37:
//#line 138 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de tipo en parámetro formal");}
break;
case 38:
//#line 139 "gramaticaDeCero.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 41:
//#line 149 "gramaticaDeCero.y"
{System.out.println("Asignación válida");}
break;
case 42:
//#line 154 "gramaticaDeCero.y"
{ 
                    /*System.out.println("n_var: " + n_var + ", n_cte: " + n_cte);*/
                    if (n_var == 1 && n_cte == 1) {
                        yyerror("Error sintactico: para asignación simple use ':=' en lugar de '='");
                    } else {
                        if (n_var < n_cte) {
                            yyerror("Error sintactico: más constantes que variables en la asignación");
                        } else {
                            /* System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");*/
                            System.out.println("Asignación válida");
                        }
                    }
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 43:
//#line 168 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de variables antes del '='"); }
break;
case 44:
//#line 169 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de constantes después del '='");}
break;
case 45:
//#line 174 "gramaticaDeCero.y"
{n_cte++;}
break;
case 46:
//#line 175 "gramaticaDeCero.y"
{n_cte++;}
break;
case 47:
//#line 176 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una constante después de coma");}
break;
case 48:
//#line 177 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
break;
case 49:
//#line 180 "gramaticaDeCero.y"
{n_var++;}
break;
case 50:
//#line 181 "gramaticaDeCero.y"
{n_var++;}
break;
case 51:
//#line 182 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta identificador después de coma");}
break;
case 52:
//#line 183 "gramaticaDeCero.y"
{yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
break;
case 57:
//#line 195 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 58:
//#line 196 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresión."); }
break;
case 60:
//#line 198 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 61:
//#line 199 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresión."); }
break;
case 64:
//#line 206 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 65:
//#line 207 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '*' en expresión."); }
break;
case 67:
//#line 209 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 68:
//#line 210 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresión."); }
break;
case 69:
//#line 211 "gramaticaDeCero.y"
{ yyerror("Falta operador entre factores en expresión."); }
break;
case 73:
//#line 223 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 74:
//#line 230 "gramaticaDeCero.y"
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
case 75:
//#line 244 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 77:
//#line 257 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 78:
//#line 259 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 80:
//#line 265 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 81:
//#line 266 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 82:
//#line 267 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 83:
//#line 269 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 84:
//#line 270 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 85:
//#line 271 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 86:
//#line 273 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 87:
//#line 274 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 88:
//#line 275 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 89:
//#line 276 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 91:
//#line 281 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 92:
//#line 282 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 101:
//#line 297 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 102:
//#line 298 "gramaticaDeCero.y"
{yyerror("Falta bloque del then");}
break;
case 105:
//#line 303 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 106:
//#line 304 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 107:
//#line 309 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 108:
//#line 310 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 109:
//#line 311 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 110:
//#line 312 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 111:
//#line 313 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 112:
//#line 314 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 113:
//#line 315 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 114:
//#line 316 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 115:
//#line 317 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 116:
//#line 318 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 118:
//#line 322 "gramaticaDeCero.y"
{System.out.println("BLOQUE FOR");}
break;
case 119:
//#line 323 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 120:
//#line 328 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 121:
//#line 329 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 122:
//#line 330 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 123:
//#line 331 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 124:
//#line 338 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 128:
//#line 348 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 130:
//#line 352 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 131:
//#line 353 "gramaticaDeCero.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 132:
//#line 354 "gramaticaDeCero.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 133:
//#line 355 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 135:
//#line 361 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Lambda"); System.out.println("HOLAAAA");}
break;
case 136:
//#line 362 "gramaticaDeCero.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 137:
//#line 363 "gramaticaDeCero.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 138:
//#line 364 "gramaticaDeCero.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1269 "Parser.java"
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
