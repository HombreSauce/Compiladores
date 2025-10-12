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
    4,    4,    6,    6,   15,   15,   15,    7,    7,    7,
    7,    7,    8,    8,   19,   17,   17,   17,   20,   20,
   20,   21,   21,   18,   18,   18,    9,   10,   10,   10,
   24,   24,   24,   24,   23,   23,   23,   23,   22,   22,
   16,   16,   16,   16,   16,   16,   16,   26,   26,   26,
   26,   26,   26,   26,   27,   27,   25,   25,   25,   25,
   25,   11,   11,   11,   31,   31,   31,   31,   31,   31,
   31,   31,   31,   31,   31,   31,   31,   31,   28,   28,
   28,   28,   32,   32,   32,   32,   32,   32,   29,   29,
   29,   30,   30,   30,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   33,   33,   33,   13,   13,   13,
   13,   14,   34,   34,   35,   35,   36,   36,   36,   36,
   36,   36,   37,   37,   37,   37,   38,   38,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    0,    2,    3,    2,    1,    2,
    1,    1,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    2,    1,    3,    3,    5,    4,    5,
    5,    5,    9,    1,    7,    1,    3,    3,    3,    3,
    2,    0,    1,    5,    4,    0,    3,    3,    2,    3,
    1,    3,    3,    2,    1,    3,    3,    2,    1,    3,
    1,    3,    3,    3,    3,    3,    3,    1,    3,    3,
    3,    3,    3,    3,    1,    1,    1,    2,    1,    2,
    1,    6,    8,    1,    5,    5,    4,    6,    7,    7,
    6,    8,    3,    5,    6,    8,    5,    7,    3,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    2,    3,
    2,    2,    3,    2,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    1,    3,    2,    4,    4,    4,
    3,    4,    1,    3,    3,    3,    1,    4,    3,    4,
    3,    1,   10,    9,    9,    8,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,   11,
    0,    0,    0,    2,    6,    0,    0,   12,   13,   16,
   17,   18,   19,   20,   21,   22,   34,    0,    0,   84,
    4,    1,    0,   79,   77,   81,    0,    0,    0,    0,
    0,    0,   76,    0,   68,    0,    0,    0,    0,    0,
    0,    0,    0,   51,   14,    0,    0,    0,    0,    0,
    0,   59,    0,    0,    0,  105,  106,  107,  108,    0,
    0,    0,    0,  104,  103,    0,   80,   78,    0,    0,
    0,  111,    0,    0,  109,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   93,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  133,    0,  142,    0,
   54,    0,    0,    0,    0,   43,    0,   36,    0,    0,
   23,    0,   60,   50,    0,   57,    0,    0,    0,   71,
   74,    0,    0,    0,    0,    0,  110,    0,    8,    0,
    0,    0,    0,    0,    0,    0,   70,   69,   73,   72,
    0,    0,   87,    0,    0,    0,  130,  129,  128,    0,
    0,    0,    0,    0,    0,    0,    0,  132,    0,   53,
   52,    0,    0,    0,    0,    0,    0,    0,   41,    0,
   27,   26,    0,   97,    0,    0,   86,    7,    0,   85,
    0,  114,    0,  112,   94,    0,    0,    0,    0,    0,
    0,    0,    0,  134,  136,  135,   32,   31,   30,   28,
    0,   38,   37,    5,   40,   39,    0,   95,   88,    0,
   82,    0,    0,   91,  113,    0,    0,    0,    0,    0,
    0,  140,  138,    0,    5,    0,    0,   98,    0,   90,
   89,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   35,   96,   92,   83,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  125,  123,  116,  117,  118,  119,  120,  121,  122,  124,
  115,    0,    0,  147,  148,    0,    0,    0,   33,  127,
    0,    0,    0,  146,    0,    0,  126,  145,    0,  144,
    0,  143,   44,
};
final static short yydgoto[] = {                          3,
    5,   15,   83,  271,   17,   18,   19,   20,   21,   22,
   23,   24,   25,   26,   59,   41,  117,  269,   27,  118,
  119,   28,   29,   53,   43,   44,   45,   46,   47,  156,
   30,   76,  272,  106,  107,  108,  109,  286,
};
final static short yysindex[] = {                      -261,
 -272,    0,    0,    0, -146, -127, -119, -236, -228,    0,
 -207, -229,  310,    0,    0, -173, -198,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -152, -258,    0,
    0,    0,  837,    0,    0,    0,   -3,  568,  454, -157,
  692, -152,    0,    7,    0,  551,   45,  670,  742, -160,
 -101,  661,  -16,    0,    0, -112, -197, -154,   46,  670,
  -93,    0, -176,  -84, -129,    0,    0,    0,    0,  747,
  387,  387,  747,    0,    0,  670,    0,    0,  821, -129,
  562,    0,  471,  -88,    0,  670,  764,  769,  786,  791,
  808, -251,   88,  515,    0,   61,  -71,   29,   66,  -48,
  -39, -175, -192,  -68, -102,  -49,    0,  -64,    0,  290,
    0,  670,  670,  813, -154,    0,   90,    0, -117,  -73,
    0, -102,    0,    0,  -16,    0, -129,   44,    7,    0,
    0,    7, -102, -251, -109,  125,    0,  -60,    0, -102,
   44,    7,   44,    7,   61, -102,    0,    0,    0,    0,
  162,  515,    0,  496,  -52,  -11,    0,    0,    0,   -1,
    2,   27, -233,  670,  670,  -17,  661,    0,  -65,    0,
    0,  332,  344,  325,  445,  127, -179,   70,    0,  -51,
    0,    0,  192,    0,  -24,  515,    0,    0,  515,    0,
   59,    0,  498,    0,    0,   40,   97,  105,  106, -239,
  103,  140,   86,    0,    0,    0,    0,    0,    0,    0,
   87,    0,    0,    0,    0,    0,  515,    0,    0,  -94,
    0,  139,  148,    0,    0,  129,  138,  143,  145,  165,
 -138,    0,    0,  653,    0,  -70,  185,    0, -205,    0,
    0, -242,  146,  155,  163,  164,  167, -240,  -10,  -62,
  739,    0,    0,    0,    0,  655,  655,  655,  655,  655,
  655,  655,  655,  -54,  -43,  411,  169,  183,  160,  511,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  411,  184,    0,    0,  176,  411,  670,    0,    0,
  513,  191,  411,    0,  198,  177,    0,    0,  199,    0,
  202,    0,    0,
};
final static short yyrindex[] = {                         0,
  -18,    0,    0,    0,    0,    0,  491,    0,    0,    0,
    0,  678,    0,    0,    0,  392,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -204,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  710,    0,   38,    0,    0,    0,    0,    0,    0,
    0,    0,  296,    0,    0,    0,  306,  -38,  416,    0,
    0,    0,    0,    0, -116,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    1,
    0,    0,    0,  446,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  339,    0,
    0,    0,    0,    0,  220,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -38,    0,    0,    0,    0,    0,
    0,  355,    0,    0,  379,    0,  -99,    0,   75,    0,
    0,  112,  566,    0,    0,    0,    0,  453,    0,  598,
  149,  186,  223,  260,  608,  613,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  429,    0,  -38,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  221,  222,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  211,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  212,    0,    0,
};
final static short yygindex[] = {                         0,
   -2,    0, -141,  658,  -47,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -45,  394,    0,    0,  333,
    0,   14,    0,  455,  520,   37,   68,  481,  -36, -147,
    0,  483,  866,    0,  358,    0,    0,  200,
};
final static int YYTABLESIZE=1130;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          6,
   75,    7,   97,   99,  191,    8,  105,    1,    9,   93,
   62,   11,  193,  256,  122,  263,  230,   12,    4,   33,
    8,   42,  199,    9,   63,  231,   11,   48,   64,    2,
  133,   13,   12,   34,   35,   36,  200,   61,  222,   39,
  140,  223,   65,  146,  136,   37,   13,  257,   50,  264,
  254,   80,   38,  255,   39,  151,  166,   56,  113,   52,
   49,   80,   80,  164,   55,   80,  172,  173,  175,  237,
   57,  180,  239,   80,   64,  114,  212,  127,   55,  124,
  162,   51,   55,   80,   80,   80,   80,  116,  163,   80,
   58,  115,  250,   34,   35,   36,  165,  183,  185,   80,
   80,   80,   80,   80,   80,   37,  129,  265,  100,  132,
    8,   67,  116,    9,   55,   10,   11,  247,  201,  202,
   60,  105,   12,  142,  144,   80,   80,   80,  291,    8,
   85,  248,    9,   61,   10,   11,   13,    8,  130,  131,
    9,   12,   10,   11,   10,   14,  184,    8,   63,   12,
    9,  179,   58,   11,  101,   13,   61,  148,  150,   12,
  112,  238,    8,   13,   31,    9,   58,  102,   11,   56,
   58,  126,   32,   13,   12,  123,   87,   80,   80,   88,
   80,   39,  181,   56,   62,   62,    8,   56,   13,    9,
  205,   10,   11,   10,    8,  182,  154,    9,   12,  139,
   11,  280,    8,  206,  215,    9,   12,   87,   11,  169,
   88,  236,   13,    8,   12,  160,    9,  216,  157,   11,
   13,  252,   66,   42,  161,   12,  266,  188,   13,  267,
   42,  219,  251,  220,  221,  194,  270,  167,    5,   13,
  168,    5,  296,    5,    5,  282,    8,  195,  283,    9,
    5,  203,   11,   34,   35,   36,   75,   75,   12,   65,
   75,   75,   75,   75,    5,   37,   77,   78,  196,   75,
  110,  197,   13,    5,   75,   75,   75,   75,   75,   75,
   75,   75,   75,   75,   75,   75,   90,   91,   75,   75,
   75,   75,   75,   61,   61,   49,  198,   61,   61,   61,
   61,   80,   94,   95,  226,   25,   61,   70,   71,   72,
   73,   61,   61,   61,   61,   61,   61,  224,  158,   61,
   61,   61,   61,   71,   72,   61,   61,   61,   61,   61,
   64,   64,  120,  121,   64,   64,   64,   64,  131,   70,
   71,   72,   73,   64,   87,  152,  153,   88,   64,   64,
   64,   64,   64,   64,   47,  159,   64,   64,   64,   64,
  214,  227,   64,   64,   64,   64,   64,   67,   67,  228,
  229,   67,   67,   67,   67,  234,  177,  235,   48,  178,
   67,   87,  186,  187,   88,   67,   67,   67,   67,   67,
   67,   15,  232,   67,   67,   67,   67,  240,  242,   67,
   67,   67,   67,   67,   63,   63,  241,  243,   63,   63,
   63,   63,  244,  177,  245,   24,  211,   63,   87,  189,
  190,   88,   63,   63,   63,   63,   63,   63,   29,  233,
   63,   63,   63,   63,  246,  258,   63,   63,   63,   63,
   63,   62,   62,  253,  259,   62,   62,   62,   62,  217,
  218,  289,  260,  261,   62,   87,  262,  287,   88,   62,
   62,   62,   62,   62,   62,  294,  301,   62,   62,   62,
   62,  288,  293,   62,   62,   62,   62,   62,   66,   66,
  298,  292,   66,   66,   66,   66,  295,  300,  302,  303,
    3,   66,  299,  137,  141,  139,   66,   66,   66,   66,
   66,   66,   46,   45,   66,   66,   66,   66,  176,  213,
   66,   66,   66,   66,   66,   65,   65,  125,   81,   65,
   65,   65,   65,   89,  204,    0,    0,    0,   65,    0,
    0,    0,   54,   65,   65,   65,   65,   65,   65,    0,
    0,   65,   65,   65,   65,  170,    0,   65,   65,   65,
   65,   65,   49,    0,    0,   49,   49,   49,   49,   34,
   35,   36,   25,    0,   49,   25,   25,   25,   25,    0,
    0,   37,  111,    0,   25,    0,    0,    0,   49,   34,
   35,   36,   54,   49,   49,    0,    0,   49,   25,    0,
    0,   37,   25,   25,    0,  131,    0,   25,  131,  131,
  131,  131,    0,   70,   71,   72,   73,  131,    0,    0,
   87,   47,  209,   88,   47,   47,   47,   47,    0,  207,
    0,  131,   87,   47,    0,   88,  131,  131,    0,  171,
  131,  208,    0,    0,    0,   48,    0,   47,   48,   48,
   48,   48,   47,   47,  111,    0,   47,   48,   15,    0,
    0,   15,   15,   15,   15,   62,   34,   35,   36,    0,
   15,   48,   16,   16,   16,   40,   48,   48,   37,    0,
   48,    0,   24,    0,   15,   24,   24,   24,   24,  284,
   34,   35,   36,   15,   24,   29,    0,    0,   29,   29,
   29,   29,   37,    0,    0,    0,   84,   29,   24,    0,
    0,    0,    9,   40,    0,    9,    0,   24,    9,   10,
    8,   29,   10,    9,    9,   10,   11,    0,    0,    0,
   29,   10,   12,   87,    0,    0,   88,    8,    9,    0,
    9,    0,  210,   11,    9,   10,   13,    9,   40,   12,
  138,   10,    0,    0,   10,   82,    0,    0,    0,   40,
    0,  155,    8,   13,    8,    9,    0,    9,   11,    0,
   11,    0,  137,    0,   12,    0,   12,    8,    0,    8,
    9,    8,    9,   11,    9,   11,    0,   11,   13,   12,
   13,   12,    0,   12,    0,  285,    0,  192,    0,  225,
    0,   40,   40,   13,    0,   13,    0,   13,    0,    0,
    0,  285,  290,    0,  297,  154,  285,    8,    0,  155,
    9,   84,  285,   11,    0,    0,    0,    0,    8,   12,
    0,    9,  101,   79,   11,  101,    0,    0,  101,    0,
   12,    0,    0,   13,  101,    0,   62,   34,   35,   36,
   92,   39,    0,  155,   13,    0,  155,    0,  101,   37,
  138,  135,   39,    0,  100,  101,  101,  100,    0,    0,
  100,    0,    0,    0,  102,    0,  100,  102,    0,   99,
  102,    0,   99,    0,  155,   99,  102,  155,    0,    0,
  100,   99,    0,    0,    0,    0,    0,  100,  100,    0,
  102,   84,    0,   16,    0,   99,    0,  102,  102,    0,
    0,    0,   99,   99,    0,    0,   84,  138,   16,    8,
    0,    8,    9,    0,    9,   11,   96,   11,    0,    0,
    0,   12,  138,   12,    0,   96,    0,   84,  103,   62,
   34,   35,   36,   59,    0,   13,    0,   13,   62,   34,
   35,   36,   37,  249,    0,  270,   59,   86,  138,  104,
   59,   37,   59,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   59,   59,   59,   75,   66,   67,   68,   69,
   87,    0,    0,   88,    0,   74,   75,    0,   55,    0,
    0,    0,    0,    0,   75,   75,   75,   75,   75,   75,
   75,   75,   55,   75,   75,    8,   55,   98,    9,  268,
   10,   11,  128,    0,    0,    0,    0,   12,    0,    0,
   62,   34,   35,   36,    0,   62,   34,   35,   36,  141,
    0,   13,    0,   37,  143,    0,    0,    0,   37,    0,
    0,    0,   62,   34,   35,   36,    0,   62,   34,   35,
   36,  145,    0,    0,    0,   37,  147,    0,    0,    0,
   37,    0,    0,    0,   62,   34,   35,   36,    0,   62,
   34,   35,   36,  149,    0,    0,    0,   37,  174,    0,
    0,    0,   37,    0,    0,    0,   62,   34,   35,   36,
    0,   62,   34,   35,   36,    0,    0,    0,    0,   37,
    0,    0,    0,    0,   37,   66,   67,   68,   69,   70,
   71,   72,   73,    0,   74,   75,    0,    0,    0,    0,
  134,   66,   67,   68,   69,   70,   71,   72,   73,    0,
   74,   75,  273,  274,  275,  276,  277,  278,  279,  281,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          2,
    0,    4,   48,   49,  152,  257,   52,  269,  260,   46,
  269,  263,  154,  256,   60,  256,  256,  269,  291,  256,
  257,    8,  256,  260,  283,  265,  263,  256,  287,  291,
   76,  283,  269,  270,  271,  272,  270,    0,  186,  291,
   86,  189,   29,   89,   81,  282,  283,  290,  256,  290,
  256,   38,  289,  259,  291,   92,  104,  256,  256,  289,
  289,   48,   49,  256,  269,   52,  112,  113,  114,  217,
  269,  119,  220,   60,    0,  273,  256,   64,  283,  256,
  256,  289,  287,   70,   71,   72,   73,  267,  264,   76,
  289,  289,  234,  270,  271,  272,  289,  134,  135,   86,
   87,   88,   89,   90,   91,  282,   70,  249,  269,   73,
  257,    0,  267,  260,  288,  262,  263,  256,  164,  165,
  273,  167,  269,   87,   88,  112,  113,  114,  270,  257,
  288,  270,  260,  286,  262,  263,  283,  257,   71,   72,
  260,  269,  262,  263,  262,  292,  256,  257,    0,  269,
  260,  269,  269,  263,  256,  283,  286,   90,   91,  269,
  273,  256,  257,  283,  292,  260,  283,  269,  263,  269,
  287,  256,  292,  283,  269,  269,  279,  164,  165,  282,
  167,  291,  256,  283,  269,    0,  257,  287,  283,  260,
  256,  262,  263,  262,  257,  269,  291,  260,  269,  288,
  263,  256,  257,  269,  256,  260,  269,  279,  263,  274,
  282,  214,  283,  257,  269,  264,  260,  269,  290,  263,
  283,  292,    0,  262,  264,  269,  289,  288,  283,  292,
  269,  256,  235,  258,  259,  288,  291,  287,  257,  283,
  290,  260,  288,  262,  263,  289,  257,  259,  292,  260,
  269,  269,  263,  270,  271,  272,  256,  257,  269,    0,
  260,  261,  262,  263,  283,  282,  270,  271,  270,  269,
  287,  270,  283,  292,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  280,  281,  288,  289,
  290,  291,  292,  256,  257,    0,  270,  260,  261,  262,
  263,  288,  258,  259,  265,    0,  269,  279,  280,  281,
  282,  274,  275,  276,  277,  278,  279,  259,  290,  282,
  283,  284,  285,  280,  281,  288,  289,  290,  291,  292,
  256,  257,  287,  288,  260,  261,  262,  263,    0,  279,
  280,  281,  282,  269,  279,  258,  259,  282,  274,  275,
  276,  277,  278,  279,    0,  290,  282,  283,  284,  285,
  291,  265,  288,  289,  290,  291,  292,  256,  257,  265,
  265,  260,  261,  262,  263,  290,  287,  291,    0,  290,
  269,  279,  258,  259,  282,  274,  275,  276,  277,  278,
  279,    0,  290,  282,  283,  284,  285,  259,  270,  288,
  289,  290,  291,  292,  256,  257,  259,  270,  260,  261,
  262,  263,  270,  287,  270,    0,  290,  269,  279,  258,
  259,  282,  274,  275,  276,  277,  278,  279,    0,  290,
  282,  283,  284,  285,  270,  290,  288,  289,  290,  291,
  292,  256,  257,  259,  290,  260,  261,  262,  263,  258,
  259,  292,  290,  290,  269,  279,  290,  289,  282,  274,
  275,  276,  277,  278,  279,  290,  290,  282,  283,  284,
  285,  289,  289,  288,  289,  290,  291,  292,  256,  257,
  290,  282,  260,  261,  262,  263,  287,  290,  290,  288,
    0,  269,  293,  274,  274,  274,  274,  275,  276,  277,
  278,  279,  292,  292,  282,  283,  284,  285,  115,  177,
  288,  289,  290,  291,  292,  256,  257,   63,   38,  260,
  261,  262,  263,   41,  167,   -1,   -1,   -1,  269,   -1,
   -1,   -1,   13,  274,  275,  276,  277,  278,  279,   -1,
   -1,  282,  283,  284,  285,  256,   -1,  288,  289,  290,
  291,  292,  257,   -1,   -1,  260,  261,  262,  263,  270,
  271,  272,  257,   -1,  269,  260,  261,  262,  263,   -1,
   -1,  282,   53,   -1,  269,   -1,   -1,   -1,  283,  270,
  271,  272,   63,  288,  289,   -1,   -1,  292,  283,   -1,
   -1,  282,  287,  288,   -1,  257,   -1,  292,  260,  261,
  262,  263,   -1,  279,  280,  281,  282,  269,   -1,   -1,
  279,  257,  288,  282,  260,  261,  262,  263,   -1,  288,
   -1,  283,  279,  269,   -1,  282,  288,  289,   -1,  110,
  292,  288,   -1,   -1,   -1,  257,   -1,  283,  260,  261,
  262,  263,  288,  289,  125,   -1,  292,  269,  257,   -1,
   -1,  260,  261,  262,  263,  269,  270,  271,  272,   -1,
  269,  283,    5,    6,    7,    8,  288,  289,  282,   -1,
  292,   -1,  257,   -1,  283,  260,  261,  262,  263,  269,
  270,  271,  272,  292,  269,  257,   -1,   -1,  260,  261,
  262,  263,  282,   -1,   -1,   -1,   39,  269,  283,   -1,
   -1,   -1,  257,   46,   -1,  260,   -1,  292,  263,  257,
  257,  283,  260,  260,  269,  263,  263,   -1,   -1,   -1,
  292,  269,  269,  279,   -1,   -1,  282,  257,  283,   -1,
  260,   -1,  288,  263,  289,  283,  283,  292,   81,  269,
   83,  289,   -1,   -1,  292,  292,   -1,   -1,   -1,   92,
   -1,   94,  257,  283,  257,  260,   -1,  260,  263,   -1,
  263,   -1,  292,   -1,  269,   -1,  269,  257,   -1,  257,
  260,  257,  260,  263,  260,  263,   -1,  263,  283,  269,
  283,  269,   -1,  269,   -1,  266,   -1,  292,   -1,  292,
   -1,  134,  135,  283,   -1,  283,   -1,  283,   -1,   -1,
   -1,  282,  292,   -1,  292,  291,  287,  257,   -1,  152,
  260,  154,  293,  263,   -1,   -1,   -1,   -1,  257,  269,
   -1,  260,  257,  256,  263,  260,   -1,   -1,  263,   -1,
  269,   -1,   -1,  283,  269,   -1,  269,  270,  271,  272,
  290,  291,   -1,  186,  283,   -1,  189,   -1,  283,  282,
  193,  290,  291,   -1,  257,  290,  291,  260,   -1,   -1,
  263,   -1,   -1,   -1,  257,   -1,  269,  260,   -1,  257,
  263,   -1,  260,   -1,  217,  263,  269,  220,   -1,   -1,
  283,  269,   -1,   -1,   -1,   -1,   -1,  290,  291,   -1,
  283,  234,   -1,  236,   -1,  283,   -1,  290,  291,   -1,
   -1,   -1,  290,  291,   -1,   -1,  249,  250,  251,  257,
   -1,  257,  260,   -1,  260,  263,  256,  263,   -1,   -1,
   -1,  269,  265,  269,   -1,  256,   -1,  270,  268,  269,
  270,  271,  272,  256,   -1,  283,   -1,  283,  269,  270,
  271,  272,  282,  291,   -1,  291,  269,  256,  291,  289,
  273,  282,  275,  276,  277,  278,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  256,  275,  276,  277,  278,
  279,   -1,   -1,  282,   -1,  284,  285,   -1,  269,   -1,
   -1,   -1,   -1,   -1,  275,  276,  277,  278,  279,  280,
  281,  282,  283,  284,  285,  257,  287,  256,  260,  261,
  262,  263,  256,   -1,   -1,   -1,   -1,  269,   -1,   -1,
  269,  270,  271,  272,   -1,  269,  270,  271,  272,  256,
   -1,  283,   -1,  282,  256,   -1,   -1,   -1,  282,   -1,
   -1,   -1,  269,  270,  271,  272,   -1,  269,  270,  271,
  272,  256,   -1,   -1,   -1,  282,  256,   -1,   -1,   -1,
  282,   -1,   -1,   -1,  269,  270,  271,  272,   -1,  269,
  270,  271,  272,  256,   -1,   -1,   -1,  282,  256,   -1,
   -1,   -1,  282,   -1,   -1,   -1,  269,  270,  271,  272,
   -1,  269,  270,  271,  272,   -1,   -1,   -1,   -1,  282,
   -1,   -1,   -1,   -1,  282,  275,  276,  277,  278,  279,
  280,  281,  282,   -1,  284,  285,   -1,   -1,   -1,   -1,
  290,  275,  276,  277,  278,  279,  280,  281,  282,   -1,
  284,  285,  257,  258,  259,  260,  261,  262,  263,  264,
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

//#line 402 "gramaticaDeCero.y"

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
//#line 783 "Parser.java"
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
case 24:
//#line 113 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 25:
//#line 118 "gramaticaDeCero.y"
{ yyval = new ParserVal(((EntradaTablaSimbolos)val_peek(0).obj).getLexema()); }
break;
case 26:
//#line 119 "gramaticaDeCero.y"
{ yyval = new ParserVal(val_peek(2).sval + ", " + ((EntradaTablaSimbolos)val_peek(0).obj).getLexema()); }
break;
case 27:
//#line 120 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de coma"); error_lista_ids = true; }
break;
case 28:
//#line 126 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
break;
case 29:
//#line 127 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
break;
case 30:
//#line 128 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, expresión inválida."); }
break;
case 31:
//#line 129 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
break;
case 32:
//#line 130 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
break;
case 33:
//#line 134 "gramaticaDeCero.y"
{
                                                                                                                    String nombre = ((EntradaTablaSimbolos) val_peek(7).obj).getLexema();
                                                                                                                    SINT.add(lex.getLineaActual(), "Declaracion de funcion: " + nombre);
                                                                                                                    yyval = new ParserVal(nombre);
                                                                                                                   }
break;
case 35:
//#line 142 "gramaticaDeCero.y"
{yyerror("Error sintáctico: Falta nombre identificador de función");}
break;
case 38:
//#line 153 "gramaticaDeCero.y"
{yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
break;
case 40:
//#line 159 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de tipo en parámetro formal");}
break;
case 41:
//#line 160 "gramaticaDeCero.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 44:
//#line 167 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Return"); }
break;
case 45:
//#line 168 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 47:
//#line 175 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Asignacion simple"); }
break;
case 48:
//#line 180 "gramaticaDeCero.y"
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
case 49:
//#line 194 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de variables antes del '='"); }
break;
case 50:
//#line 195 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de constantes después del '='");}
break;
case 51:
//#line 200 "gramaticaDeCero.y"
{n_cte++;}
break;
case 52:
//#line 201 "gramaticaDeCero.y"
{n_cte++;}
break;
case 53:
//#line 202 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una constante después de coma");}
break;
case 54:
//#line 203 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
break;
case 55:
//#line 206 "gramaticaDeCero.y"
{n_var++;}
break;
case 56:
//#line 207 "gramaticaDeCero.y"
{n_var++;}
break;
case 57:
//#line 208 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta identificador después de coma");}
break;
case 58:
//#line 209 "gramaticaDeCero.y"
{yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
break;
case 63:
//#line 220 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 64:
//#line 221 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresión."); }
break;
case 66:
//#line 223 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 67:
//#line 224 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresión."); }
break;
case 70:
//#line 229 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 71:
//#line 230 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '*' en expresión."); }
break;
case 73:
//#line 232 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 74:
//#line 233 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresión."); }
break;
case 77:
//#line 245 "gramaticaDeCero.y"
{ yyval = val_peek(0); }
break;
case 78:
//#line 247 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String neg = '-' + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea());
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = val_peek(0);  /* devolvés el mismo ParserVal */
            }
break;
case 79:
//#line 255 "gramaticaDeCero.y"
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
case 80:
//#line 268 "gramaticaDeCero.y"
{
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)val_peek(0).obj;
            String neg = '-' + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea());
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = val_peek(0);
            }
break;
case 81:
//#line 276 "gramaticaDeCero.y"
{ yyval = val_peek(0); }
break;
case 82:
//#line 281 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 83:
//#line 283 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 85:
//#line 289 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 86:
//#line 290 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 87:
//#line 291 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 88:
//#line 292 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 89:
//#line 293 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 90:
//#line 294 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 91:
//#line 295 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 92:
//#line 296 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 93:
//#line 297 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 94:
//#line 298 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 95:
//#line 299 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 96:
//#line 300 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 97:
//#line 301 "gramaticaDeCero.y"
{yyerror("Falta bloque del if");}
break;
case 98:
//#line 302 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 100:
//#line 306 "gramaticaDeCero.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 101:
//#line 307 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 102:
//#line 308 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 111:
//#line 323 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del then");}
break;
case 114:
//#line 330 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del else");}
break;
case 115:
//#line 337 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 116:
//#line 338 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 117:
//#line 339 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 118:
//#line 340 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 119:
//#line 341 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 120:
//#line 342 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 121:
//#line 343 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 122:
//#line 344 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 123:
//#line 345 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 124:
//#line 346 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 127:
//#line 352 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 128:
//#line 357 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 129:
//#line 358 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 130:
//#line 359 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 131:
//#line 360 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 132:
//#line 367 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 136:
//#line 377 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 138:
//#line 381 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 139:
//#line 382 "gramaticaDeCero.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 140:
//#line 383 "gramaticaDeCero.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 141:
//#line 384 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 143:
//#line 390 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Lambda");}
break;
case 144:
//#line 391 "gramaticaDeCero.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 145:
//#line 392 "gramaticaDeCero.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 146:
//#line 393 "gramaticaDeCero.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1360 "Parser.java"
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
