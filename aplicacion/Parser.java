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
   31,   31,   31,   31,   31,   31,   31,   31,   31,   31,
   31,   28,   28,   28,   32,   32,   32,   32,   32,   32,
   29,   29,   29,   30,   30,   30,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   33,   33,   33,   13,
   13,   13,   13,   14,   34,   34,   35,   35,   36,   36,
   36,   36,   36,   36,   37,   37,   37,   37,   38,   38,
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
    6,    7,    7,    6,    8,    3,    5,    6,    8,    5,
    7,    3,    3,    3,    1,    1,    1,    1,    1,    1,
    2,    3,    2,    2,    3,    2,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    1,    3,    2,    4,
    4,    4,    3,    4,    1,    3,    3,    3,    1,    4,
    3,    4,    3,    1,   10,    9,    9,    8,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    5,    5,    5,    0,    0,    0,    0,
    0,   11,    0,    0,    0,    4,    6,    0,    0,   12,
   13,   16,   17,   18,   19,   20,   21,   22,   36,    0,
    0,   87,    2,    1,    0,   82,   80,   84,    0,    0,
    0,    0,    0,    0,   79,    0,   70,    0,    0,    0,
    0,    0,    0,    0,    0,   53,   14,    0,    0,    0,
    0,    0,    0,   61,    0,    0,    0,  107,  108,  109,
  110,    0,    0,    0,    0,  106,  105,    0,   83,   81,
    0,    0,    0,  113,    0,    0,  111,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   96,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  135,    0,
  144,    0,   56,   29,    0,   28,    0,    0,    0,   45,
    0,   38,    0,    0,   23,    0,   62,   52,    0,   59,
    0,    0,    0,   73,   76,    0,    0,    0,    0,    0,
  112,    0,    8,    0,    0,    0,    0,    0,    0,   77,
   72,   71,   75,   74,    0,    0,   90,    0,    0,    0,
  132,  131,  130,    0,    0,    0,    0,    0,    0,    0,
    0,  134,    0,   55,   54,    0,    0,    0,    0,    0,
    0,    0,   43,    0,   27,   26,    0,  100,    0,    0,
   89,    7,    0,   88,    0,  116,    0,  114,   97,    0,
    0,    0,    0,    0,    0,    0,    0,  136,  138,  137,
   34,   33,   32,   30,    0,   40,   39,    5,   42,   41,
    0,   98,   91,    0,   85,    0,    0,   94,  115,    0,
    0,    0,    0,    0,    0,  142,  140,    0,    5,    0,
    0,  101,    0,   93,   92,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   37,   99,   95,   86,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  127,  125,  118,  119,  120,  121,
  122,  123,  124,  126,  117,    0,    0,  149,  150,    0,
    0,    0,   35,  129,    0,    0,    0,  148,    0,    0,
  128,  147,    0,  146,    0,  145,   46,
};
final static short yydgoto[] = {                          3,
    7,   17,   85,  275,   19,   20,   21,   22,   23,   24,
   25,   26,   27,   28,   61,   43,  121,  273,   29,  122,
  123,   30,   31,   55,   45,   46,   47,   48,   49,  160,
   32,   78,  276,  108,  109,  110,  111,  290,
};
final static short yysindex[] = {                      -197,
 -268, -247,    0,    0,    0,    0, -228, -100,  -89, -135,
 -256,    0, -238, -264,  647,    0,    0, -227, -213,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -178,
 -177,    0,    0,    0,  759,    0,    0,    0, -168,  487,
  290, -223,  770, -178,    0, -181,    0,  481,  -11,  640,
  644, -165,  -36,  590,  309,    0,    0, -129, -235, -106,
  -63,  640, -150,    0, -189,  -25, -159,    0,    0,    0,
    0,  694,  583,  583,  694,    0,    0,  640,    0,    0,
  743, -159,  491,    0,  411, -149,    0,  698,  702,  719,
  583,  723,  727, -145,   44,  551,    0,  166, -260,   64,
   28,  -79,  -75, -236, -211, -112,  -82,  -86,    0,  -22,
    0,  361,    0,    0,  640,    0,  640,  744, -106,    0,
  -68,    0,  -19,   -1,    0,  -82,    0,    0,  309,    0,
 -159,   43, -181,    0,    0, -181,  -82, -145, -252,  101,
    0,  -93,    0,   43, -181,   43, -181,  166,  -82,    0,
    0,    0,    0,    0,  121,  551,    0,  442,  -21,   72,
    0,    0,    0,   17,   62,   71,  -65,  640,  640,   98,
  590,    0,   40,    0,    0,  130,  272,   94,  341,  123,
 -114,   77,    0,   48,    0,    0,  137,    0,   81,  551,
    0,    0,  551,    0,  122,    0,  453,    0,    0,  124,
  138,  139,  146, -199,  172,  208,  126,    0,    0,    0,
    0,    0,    0,    0,  134,    0,    0,    0,    0,    0,
  551,    0,    0, -140,    0,  158,  173,    0,    0,  161,
  169,  170,  183,  191,  -40,    0,    0,  580,    0,  -85,
  209,    0,  193,    0,    0, -242,  177,  185,  186,  192,
  194, -241,  398,  -81,  -34,    0,    0,    0,    0,  587,
  587,  587,  587,  587,  587,  587,  587,  -70,  -51,  760,
  196,  199,  189,  457,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  760,  200,    0,    0,  207,
  760,  640,    0,    0,  470,  213,  760,    0,  214,  244,
    0,    0,  221,    0,  195,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  517,    0,
    0,    0,    0,  608,    0,    0,    0,  390,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -128,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  664,    0,   37,    0,    0,    0,    0,
    0,    0,    0,    0,  295,    0,    0,    0,  305,   39,
  415,    0,    0,    0,    0,    0, -117,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,    0,    0,    0,  -23,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  338,    0,    0,    0,    0,    0,  238,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   39,    0,
    0,    0,    0,    0,    0,  353,    0,    0,  377,    0,
  -73,    0,   73,    0,    0,  109,  526,    0,    0,    0,
    0,  -18,    0,  145,  181,  217,  253,  536,  541,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  426,    0,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  245,  246,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  226,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  232,    0,    0,
};
final static short yygindex[] = {                         0,
   -3,    0, -148,  683,   26,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -38,  406,    0,    0,  352,
    0,   -4,    0,  474,  506,  184,  512,  500,  -41, -130,
    0,  503,  795,    0,  378,    0,    0, -190,
};
final static int YYTABLESIZE=1063;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         50,
   78,    8,    9,  188,   10,   44,   95,   11,    5,  197,
   13,   99,  101,  260,  267,  107,   14,   52,   88,  166,
  117,   89,    4,  126,   54,  195,   67,  167,   10,  161,
   15,   11,   51,   12,   13,   82,   63,  118,   41,  137,
   14,  140,   58,    6,  168,   82,   82,  261,  268,   82,
   53,  149,  155,  119,   15,   59,  234,   82,    1,  226,
   57,  131,  227,   16,   87,  235,  128,   82,   82,   82,
   82,    2,   66,   82,   91,   60,  176,  169,  177,  179,
   36,   37,   38,   82,   82,   82,   82,   82,   82,  254,
  241,   64,   39,  243,   62,  296,  187,  189,   92,   93,
  299,   79,   80,  102,  269,   65,  303,   63,   69,   66,
   82,   10,   82,   82,   11,  242,   10,   13,  127,   11,
   35,   10,   13,   14,   11,  295,   63,   13,   14,  205,
  206,  170,  107,   14,   36,   37,   38,   15,  143,  114,
   57,  216,   15,  115,   65,   41,   39,   15,  184,   12,
  158,   60,  120,   40,   57,   41,   10,  116,   57,   11,
  120,   12,   13,   82,   82,   60,   82,   10,   14,   60,
   11,   10,   12,   13,   11,   10,   12,   13,   11,   14,
   64,   13,   15,   14,  164,  284,   10,   14,  165,   11,
  203,   33,   13,   15,  192,   58,   88,   15,   14,   89,
  171,   15,   34,  172,  204,   10,  256,  270,   11,   58,
  271,   13,   15,   58,  240,  251,   68,   14,  181,  103,
  274,  182,   10,  124,  125,   11,  272,   12,   13,  252,
  130,   15,  104,    9,   14,  255,    9,  286,   10,    9,
  287,   10,   12,   64,   10,    9,   96,   97,   15,  183,
   10,  173,   67,  300,  185,  133,   78,   78,  136,    9,
   78,   78,   78,   78,   10,    9,  198,  186,    9,   78,
   10,  145,  147,   10,   78,   78,   78,   78,   78,   78,
   78,   78,   78,   78,   78,   78,  200,   82,   78,   78,
   78,   78,   78,   63,   51,  209,   63,   63,   63,   63,
   44,  156,  157,  219,   25,   63,   88,   44,  210,   89,
   63,   63,   63,   63,   63,   63,  220,  163,   63,   63,
   63,   63,   73,   74,   63,   63,   63,   63,   63,   66,
  199,  201,   66,   66,   66,   66,  223,  133,  224,  225,
  202,   66,   72,   73,   74,   75,   66,   66,   66,   66,
   66,   66,   49,  162,   66,   66,   66,   66,  190,  191,
   66,   66,   66,   66,   66,   69,  207,  218,   69,   69,
   69,   69,   72,   73,   74,   75,   50,   69,  193,  194,
  228,  213,   69,   69,   69,   69,   69,   69,  230,   15,
   69,   69,   69,   69,  221,  222,   69,   69,   69,   69,
   69,   65,  231,  232,   65,   65,   65,   65,   88,  181,
  233,   89,  215,   65,   24,  238,  244,  211,   65,   65,
   65,   65,   65,   65,  239,   31,   65,   65,   65,   65,
  246,  245,   65,   65,   65,   65,   65,   64,  247,  248,
   64,   64,   64,   64,   72,   73,   74,   75,  258,   64,
   88,  259,  249,   89,   64,   64,   64,   64,   64,   64,
  250,  236,   64,   64,   64,   64,  262,  257,   64,   64,
   64,   64,   64,   68,  263,  264,   68,   68,   68,   68,
  293,  265,  307,  266,  291,   68,   88,  292,  297,   89,
   68,   68,   68,   68,   68,   68,  298,  237,   68,   68,
   68,   68,  302,  304,   68,   68,   68,   68,   68,   67,
  306,  139,   67,   67,   67,   67,    3,   48,  143,  141,
   56,   67,   88,   47,  180,   89,   67,   67,   67,   67,
   67,   67,  217,  305,   67,   67,   67,   67,  129,   83,
   67,   67,   67,   67,   67,   90,   10,    0,  208,   11,
   88,   51,   13,   89,   51,   51,   51,   51,   14,  212,
  113,   25,    0,   51,   25,   25,   25,   25,    0,    0,
   56,    0,   15,   25,    0,    0,    0,   51,   36,   37,
   38,   84,   51,   51,  134,  135,   51,   25,    0,    0,
   39,   25,   25,    0,  133,  112,   25,  133,  133,  133,
  133,    0,  150,  152,  154,    0,  133,    0,    0,   49,
    0,    0,   49,   49,   49,   49,  174,  175,    0,   88,
  133,   49,   89,    0,    0,  133,  133,    0,  214,  133,
   36,   37,   38,   50,  113,   49,   50,   50,   50,   50,
   49,   49,   39,    0,   49,   50,   15,    0,    0,   15,
   15,   15,   15,    0,   10,    0,    0,   11,   15,   50,
   13,    0,    0,    0,   50,   50,   14,   10,   50,    0,
   11,   24,   15,   13,   24,   24,   24,   24,    0,   14,
   15,   15,   31,   24,    0,   31,   31,   31,   31,   18,
   18,   18,   42,   15,   31,    0,    0,   24,   10,    0,
    0,   11,  141,    0,   13,    0,   24,    0,   31,   10,
   14,    0,   11,   10,    0,   13,   11,   31,    0,   13,
    0,   14,    0,   86,   15,   14,   10,    0,    0,   11,
   42,    0,   13,  196,    0,   15,    0,   10,   14,   15,
   11,    0,   81,   13,  229,    0,    0,   10,  294,   14,
   11,    0,   15,   13,    0,   64,   36,   37,   38,   14,
    0,  301,    0,   15,    0,   42,    0,  142,   39,    0,
   94,   41,    0,   15,    0,  289,   42,    0,  159,    0,
  139,   41,  103,    0,    0,  103,    0,    0,  103,    0,
    0,  289,  104,    0,  103,  104,  289,  102,  104,    0,
  102,    0,  289,  102,  104,    0,    0,   10,  103,  102,
   11,    0,    0,   13,    0,  103,  103,    0,  104,   14,
   42,   42,    0,  102,    0,  104,  104,    0,    0,    0,
  102,  102,    0,   15,    0,    0,   10,    0,  159,   11,
   86,  158,   13,   10,    0,   98,   11,    0,   14,   13,
    0,   64,   36,   37,   38,   14,    0,  105,   64,   36,
   37,   38,   15,   61,   39,    0,    0,    0,    0,   15,
  253,   39,  159,    0,    0,  159,   61,  274,  106,  142,
   61,    0,   61,   61,   61,   61,   61,   61,   61,   61,
   61,   61,   61,   61,   61,   98,    0,    0,    0,  100,
    0,    0,    0,  159,    0,    0,  159,    0,   64,   36,
   37,   38,   64,   36,   37,   38,   36,   37,   38,   78,
   86,   39,   18,    0,    0,   39,    0,    0,   39,    0,
    0,    0,   57,    0,    0,   86,  142,   18,   78,   78,
   78,   78,   78,   78,   78,   78,   57,   78,   78,  132,
   57,  142,    0,  144,    0,    0,   86,  146,    0,    0,
    0,    0,   64,   36,   37,   38,   64,   36,   37,   38,
   64,   36,   37,   38,  148,   39,    0,  142,  151,   39,
    0,    0,  153,   39,    0,    0,    0,   64,   36,   37,
   38,   64,   36,   37,   38,   64,   36,   37,   38,  178,
   39,    0,    0,    0,   39,    0,    0,    0,   39,    0,
    0,    0,   64,   36,   37,   38,    0,   68,   69,   70,
   71,   72,   73,   74,   75,   39,   76,   77,  288,   36,
   37,   38,  138,   68,   69,   70,   71,   72,   73,   74,
   75,   39,   76,   77,   68,   69,   70,   71,   88,    0,
    0,   89,    0,   76,   77,  277,  278,  279,  280,  281,
  282,  283,  285,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        256,
    0,    5,    6,  256,  257,   10,   48,  260,  256,  158,
  263,   50,   51,  256,  256,   54,  269,  256,  279,  256,
  256,  282,  291,   62,  289,  156,   31,  264,  257,  290,
  283,  260,  289,  262,  263,   40,    0,  273,  291,   78,
  269,   83,  256,  291,  256,   50,   51,  290,  290,   54,
  289,   90,   94,  289,  283,  269,  256,   62,  256,  190,
  288,   66,  193,  292,  288,  265,  256,   72,   73,   74,
   75,  269,    0,   78,  256,  289,  115,  289,  117,  118,
  270,  271,  272,   88,   89,   90,   91,   92,   93,  238,
  221,  269,  282,  224,  273,  286,  138,  139,  280,  281,
  291,  270,  271,  269,  253,  283,  297,  286,    0,  287,
  115,  257,  117,  118,  260,  256,  257,  263,  269,  260,
  256,  257,  263,  269,  260,  274,  286,  263,  269,  168,
  169,  106,  171,  269,  270,  271,  272,  283,  288,  269,
  269,  256,  283,  273,    0,  291,  282,  283,  123,  262,
  291,  269,  267,  289,  283,  291,  257,  287,  287,  260,
  267,  262,  263,  168,  169,  283,  171,  257,  269,  287,
  260,  257,  262,  263,  260,  257,  262,  263,  260,  269,
    0,  263,  283,  269,  264,  256,  257,  269,  264,  260,
  256,  292,  263,  283,  288,  269,  279,  283,  269,  282,
  287,  283,  292,  290,  270,  257,  292,  289,  260,  283,
  292,  263,  283,  287,  218,  256,    0,  269,  287,  256,
  291,  290,  257,  287,  288,  260,  261,  262,  263,  270,
  256,  283,  269,  257,  269,  239,  260,  289,  257,  263,
  292,  260,  262,  269,  263,  269,  258,  259,  283,  269,
  269,  274,    0,  292,  256,   72,  256,  257,   75,  283,
  260,  261,  262,  263,  283,  289,  288,  269,  292,  269,
  289,   88,   89,  292,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  270,  292,  288,  289,
  290,  291,  292,  257,    0,  256,  260,  261,  262,  263,
  262,  258,  259,  256,    0,  269,  279,  269,  269,  282,
  274,  275,  276,  277,  278,  279,  269,  290,  282,  283,
  284,  285,  280,  281,  288,  289,  290,  291,  292,  257,
  259,  270,  260,  261,  262,  263,  256,    0,  258,  259,
  270,  269,  279,  280,  281,  282,  274,  275,  276,  277,
  278,  279,    0,  290,  282,  283,  284,  285,  258,  259,
  288,  289,  290,  291,  292,  257,  269,  291,  260,  261,
  262,  263,  279,  280,  281,  282,    0,  269,  258,  259,
  259,  288,  274,  275,  276,  277,  278,  279,  265,    0,
  282,  283,  284,  285,  258,  259,  288,  289,  290,  291,
  292,  257,  265,  265,  260,  261,  262,  263,  279,  287,
  265,  282,  290,  269,    0,  290,  259,  288,  274,  275,
  276,  277,  278,  279,  291,    0,  282,  283,  284,  285,
  270,  259,  288,  289,  290,  291,  292,  257,  270,  270,
  260,  261,  262,  263,  279,  280,  281,  282,  256,  269,
  279,  259,  270,  282,  274,  275,  276,  277,  278,  279,
  270,  290,  282,  283,  284,  285,  290,  259,  288,  289,
  290,  291,  292,  257,  290,  290,  260,  261,  262,  263,
  292,  290,  288,  290,  289,  269,  279,  289,  289,  282,
  274,  275,  276,  277,  278,  279,  290,  290,  282,  283,
  284,  285,  290,  290,  288,  289,  290,  291,  292,  257,
  290,  274,  260,  261,  262,  263,    0,  292,  274,  274,
   15,  269,  279,  292,  119,  282,  274,  275,  276,  277,
  278,  279,  181,  290,  282,  283,  284,  285,   65,   40,
  288,  289,  290,  291,  292,   43,  257,   -1,  171,  260,
  279,  257,  263,  282,  260,  261,  262,  263,  269,  288,
   55,  257,   -1,  269,  260,  261,  262,  263,   -1,   -1,
   65,   -1,  283,  269,   -1,   -1,   -1,  283,  270,  271,
  272,  292,  288,  289,   73,   74,  292,  283,   -1,   -1,
  282,  287,  288,   -1,  257,  287,  292,  260,  261,  262,
  263,   -1,   91,   92,   93,   -1,  269,   -1,   -1,  257,
   -1,   -1,  260,  261,  262,  263,  256,  112,   -1,  279,
  283,  269,  282,   -1,   -1,  288,  289,   -1,  288,  292,
  270,  271,  272,  257,  129,  283,  260,  261,  262,  263,
  288,  289,  282,   -1,  292,  269,  257,   -1,   -1,  260,
  261,  262,  263,   -1,  257,   -1,   -1,  260,  269,  283,
  263,   -1,   -1,   -1,  288,  289,  269,  257,  292,   -1,
  260,  257,  283,  263,  260,  261,  262,  263,   -1,  269,
  283,  292,  257,  269,   -1,  260,  261,  262,  263,    7,
    8,    9,   10,  283,  269,   -1,   -1,  283,  257,   -1,
   -1,  260,  292,   -1,  263,   -1,  292,   -1,  283,  257,
  269,   -1,  260,  257,   -1,  263,  260,  292,   -1,  263,
   -1,  269,   -1,   41,  283,  269,  257,   -1,   -1,  260,
   48,   -1,  263,  292,   -1,  283,   -1,  257,  269,  283,
  260,   -1,  256,  263,  292,   -1,   -1,  257,  292,  269,
  260,   -1,  283,  263,   -1,  269,  270,  271,  272,  269,
   -1,  292,   -1,  283,   -1,   83,   -1,   85,  282,   -1,
  290,  291,   -1,  283,   -1,  270,   94,   -1,   96,   -1,
  290,  291,  257,   -1,   -1,  260,   -1,   -1,  263,   -1,
   -1,  286,  257,   -1,  269,  260,  291,  257,  263,   -1,
  260,   -1,  297,  263,  269,   -1,   -1,  257,  283,  269,
  260,   -1,   -1,  263,   -1,  290,  291,   -1,  283,  269,
  138,  139,   -1,  283,   -1,  290,  291,   -1,   -1,   -1,
  290,  291,   -1,  283,   -1,   -1,  257,   -1,  156,  260,
  158,  291,  263,  257,   -1,  256,  260,   -1,  269,  263,
   -1,  269,  270,  271,  272,  269,   -1,  268,  269,  270,
  271,  272,  283,  256,  282,   -1,   -1,   -1,   -1,  283,
  291,  282,  190,   -1,   -1,  193,  269,  291,  289,  197,
  273,   -1,  275,  276,  277,  278,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  256,   -1,   -1,   -1,  256,
   -1,   -1,   -1,  221,   -1,   -1,  224,   -1,  269,  270,
  271,  272,  269,  270,  271,  272,  270,  271,  272,  256,
  238,  282,  240,   -1,   -1,  282,   -1,   -1,  282,   -1,
   -1,   -1,  269,   -1,   -1,  253,  254,  255,  275,  276,
  277,  278,  279,  280,  281,  282,  283,  284,  285,  256,
  287,  269,   -1,  256,   -1,   -1,  274,  256,   -1,   -1,
   -1,   -1,  269,  270,  271,  272,  269,  270,  271,  272,
  269,  270,  271,  272,  256,  282,   -1,  295,  256,  282,
   -1,   -1,  256,  282,   -1,   -1,   -1,  269,  270,  271,
  272,  269,  270,  271,  272,  269,  270,  271,  272,  256,
  282,   -1,   -1,   -1,  282,   -1,   -1,   -1,  282,   -1,
   -1,   -1,  269,  270,  271,  272,   -1,  275,  276,  277,
  278,  279,  280,  281,  282,  282,  284,  285,  269,  270,
  271,  272,  290,  275,  276,  277,  278,  279,  280,  281,
  282,  282,  284,  285,  275,  276,  277,  278,  279,   -1,
   -1,  282,   -1,  284,  285,  261,  262,  263,  264,  265,
  266,  267,  268,
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

//#line 387 "gramaticaDeCero.y"

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
//#line 772 "Parser.java"
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
//#line 279 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 92:
//#line 280 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 93:
//#line 281 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 94:
//#line 282 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 95:
//#line 283 "gramaticaDeCero.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 96:
//#line 284 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 97:
//#line 285 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 98:
//#line 286 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 99:
//#line 287 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 100:
//#line 288 "gramaticaDeCero.y"
{yyerror("Falta bloque del if");}
break;
case 101:
//#line 289 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 103:
//#line 294 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 104:
//#line 295 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 113:
//#line 310 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del then");}
break;
case 116:
//#line 316 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable del else");}
break;
case 117:
//#line 322 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 118:
//#line 323 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 119:
//#line 324 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 120:
//#line 325 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 121:
//#line 326 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 122:
//#line 327 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 123:
//#line 328 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 124:
//#line 329 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 125:
//#line 330 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 126:
//#line 331 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 129:
//#line 337 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 130:
//#line 342 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 131:
//#line 343 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 132:
//#line 344 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 133:
//#line 345 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 134:
//#line 352 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 138:
//#line 362 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 140:
//#line 366 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 141:
//#line 367 "gramaticaDeCero.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 142:
//#line 368 "gramaticaDeCero.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 143:
//#line 369 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 145:
//#line 375 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Lambda");}
break;
case 146:
//#line 376 "gramaticaDeCero.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 147:
//#line 377 "gramaticaDeCero.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 148:
//#line 378 "gramaticaDeCero.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1331 "Parser.java"
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
