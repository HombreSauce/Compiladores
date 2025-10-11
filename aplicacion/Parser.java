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
    2,    2,    2,    4,    4,    4,    4,    6,    6,   13,
   13,   13,    7,    7,    7,    7,    7,    8,    8,   16,
   15,   15,   15,   17,   17,   17,   18,   18,    9,   10,
   10,   10,   21,   21,   21,   21,   20,   20,   20,   20,
   19,   19,   14,   14,   14,   14,   14,   14,   14,   23,
   23,   23,   23,   23,   23,   23,   23,   24,   24,   22,
   22,   22,   22,   22,   11,   11,   11,   28,   28,   28,
   28,   28,   28,   28,   28,   28,   28,   25,   25,   25,
   29,   29,   29,   29,   29,   29,   26,   26,   26,   26,
   27,   27,   27,   27,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   30,   30,   30,
};
final static short yylen[] = {                            2,
    4,    0,    2,    3,    2,    1,    2,    1,    1,    1,
    2,    1,    1,    1,    1,    1,    1,    3,    2,    1,
    3,    3,    5,    4,    5,    5,    5,    8,    1,    7,
    1,    3,    3,    3,    3,    2,    0,    1,    3,    3,
    2,    3,    1,    3,    3,    2,    1,    3,    3,    2,
    1,    3,    1,    3,    3,    3,    3,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    3,    1,    1,    1,
    2,    1,    2,    1,    6,    8,    1,    5,    5,    4,
    7,    7,    6,    3,    5,    6,    8,    3,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    3,    2,    0,
    1,    3,    2,    0,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    8,    0,   51,    0,    1,
    3,    0,    0,    9,   10,   13,   14,   15,   16,   17,
   29,    0,    0,   77,    0,   72,   70,   74,    0,    0,
    0,   97,    0,    0,   69,    0,   60,    0,    0,    0,
    0,    0,   43,   11,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   93,   94,   95,   96,    0,    0,    0,
    0,   92,   91,    0,   73,   71,    0,    0,    0,   99,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   84,    0,    0,    0,    0,   46,    0,    0,    0,
    0,   38,    0,   31,    0,    0,   18,    0,    0,   52,
   42,    0,   49,    0,    0,    0,   63,   66,    0,    0,
    0,    0,    0,   98,    0,    5,    0,    0,    0,    0,
    0,    0,   67,   62,   61,   65,   64,    0,    0,   80,
    0,  101,    0,    0,    0,    0,    0,   45,   44,    0,
    0,    0,    0,    0,    0,    0,   36,    0,   22,   21,
    0,    0,    0,   79,    4,    0,   78,    0,  103,    0,
   85,    0,    0,    0,    0,    0,   27,   26,   25,   23,
    0,   33,   32,    2,   35,   34,    0,   86,    0,   75,
    0,    0,   83,  102,    0,    0,    0,    0,    0,    0,
    2,    0,    0,    0,   82,   81,    0,    0,    0,    0,
    0,    0,    0,    0,   30,   87,   76,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   28,    0,  115,  113,
  106,  107,  108,  109,  110,  111,  112,  114,  105,  117,
    0,  116,
};
final static short yydgoto[] = {                          2,
    4,   11,   71,  219,   13,   14,   15,   16,   17,   18,
   19,   20,   48,   33,   93,   21,   94,   95,   22,   23,
   42,   35,   36,   37,   38,   39,  133,   24,   64,  220,
};
final static short yysindex[] = {                      -208,
 -215,    0,    0, -220,  144,    0, -236,    0, -260,    0,
    0, -194, -196,    0,    0,    0,    0,    0,    0,    0,
    0, -257, -205,    0,  439,    0,    0,    0, -252,  340,
 -102,    0,  296, -257,    0, -167,    0,   29, -245, -146,
  -82,  407,    0,    0, -101, -248,  -85, -231,  344,  -75,
  288,   24,  -90,    0,    0,    0,    0,  348,  440,  440,
  348,    0,    0,  344,    0,    0,  423,  -90,   65,    0,
  101,  -78,  365,  369,  373,  440,  390,  394,  -62, -154,
  167,    0,  -58,  -44, -114,  415,    0,  344,  344,  398,
  -85,    0, -119,    0,  -57,   60,    0,  326, -158,    0,
    0,  407,    0,  -90, -134, -167,    0,    0, -167, -158,
  -62,  -62,   18,    0,  -42,    0, -134, -167, -134, -167,
  326, -158,    0,    0,    0,    0,    0,   25,  167,    0,
  210,    0,  -33,  -34,   -8,   -5, -164,    0,    0,  146,
  189,  106,  253,  -79, -201,  -41,    0,   96,    0,    0,
  132,  162,  167,    0,    0,  167,    0,  -11,    0,  215,
    0,   17,   36,   53,   72,  -52,    0,    0,    0,    0,
   22,    0,    0,    0,    0,    0,  167,    0,  167,    0,
   90,   95,    0,    0,  103,  111,  122,  136,  139, -116,
    0, -166,  178,  179,    0,    0, -251,  152,  156,  157,
  159,  163, -239,  -20,    0,    0,    0,  274,  274,  274,
  274,  274,  274,  274,  274, -233,    0,  234,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  237,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  173,    0,    0,    0,    0,    0,
    0,   16,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -130,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  310,    0,  -99,    0,  173,    0,    0,
    0,  160,    0,    0,    0,  200,  -22,   52,    0,    0,
    0,    0, -121,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -147,  173,    0,
    0,  242,    0,    0,    0,    0,    0,    0,  173,    0,
  202,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -22,    0,    0,    0,    0,    0,    0,    0,  182,    0,
    0,  197,    0, -118,    0,  -60,    0,    0,  -24,  255,
  173,  173,    0,    0,  247,    0,   12,   48,   84,  120,
  264,  293,    0,    0,    0,    0,    0,    0,  202,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   88,    0,  -22,    0,    0,    0,    0,    0,
    0,    0,  202,    0,    0,  202,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  202,    0,  202,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
 -165,    0, -128,   -4,  380,    0,    0,    0,    0,    0,
    0,    0,    0,  -43,  385,    0,  319,    0,   10,    0,
  430,   -7,  183,   42,  452,  -31, -125,    0,  453,  516,
};
final static int YYTABLESIZE=732;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         12,
   32,   43,  160,  158,  208,   99,   80,   89,  192,   26,
   27,   28,   81,   82,   34,   49,  215,   65,   66,   40,
  110,   29,  228,    5,   90,  204,   72,  181,   50,    7,
  182,  122,   53,   32,   87,    8,    5,  113,  209,   68,
   91,    6,    7,   43,  140,  141,  143,  128,    8,    9,
  216,  193,   41,  194,  172,   96,   97,  218,   68,   45,
    1,  104,    9,    8,   32,   92,  115,   68,   68,   68,
   68,   10,   46,   68,   32,    3,  132,   51,  139,  151,
  152,   52,   68,   68,   68,   68,   68,   68,   76,  231,
    5,  165,   47,   44,   87,    6,    7,   68,   68,   68,
  107,  108,    8,  129,  130,  166,   32,   32,   68,   68,
   68,   68,   77,   78,   68,   68,    9,  123,  125,  127,
   73,   68,   83,   74,  132,  205,   72,   68,   68,   68,
   68,   68,   68,   68,   68,   68,   68,   68,   47,  202,
   68,  136,   68,   68,   68,   59,   60,   50,  132,  137,
   48,  132,   47,  203,    5,  115,   47,   53,   53,   53,
    7,   50,   53,   53,   48,   50,    8,  145,   48,   53,
  146,   88,  132,   84,  132,   53,   53,   53,   53,   53,
    9,   92,   53,   53,   53,   53,   85,   12,   53,   70,
   53,   53,   53,  100,    5,   50,   56,   56,   56,   12,
    7,   56,   56,  189,    6,  134,    8,  145,   56,  116,
  171,  147,  190,   72,   56,   56,   56,   56,   56,  135,
    9,   56,   56,   56,   56,  161,  115,   56,   31,   56,
   56,   56,   59,   59,   59,  162,    5,   59,   59,   37,
  106,    6,    7,  109,   59,  155,   37,  183,    8,  174,
   59,   59,   59,   59,   59,  118,  120,   59,   59,   59,
   59,  163,    9,   59,  164,   59,   59,   59,   55,   55,
   55,  217,   12,   55,   55,  153,  154,   12,   12,  103,
   55,  185,  156,  157,   12,    5,   55,   55,   55,   55,
   55,    7,    8,   55,   55,   55,   55,    8,   12,   55,
  186,   55,   55,   55,   54,   54,   54,   12,   19,   54,
   54,    9,  191,   19,   19,  149,   54,  187,   79,   31,
   19,    5,   54,   54,   54,   54,   54,    7,  150,   54,
   54,   54,   54,    8,   19,   54,  188,   54,   54,   54,
   58,   58,   58,   19,   24,   58,   58,    9,  195,   24,
   24,  175,   58,  196,  112,   31,   24,    5,   58,   58,
   58,   58,   58,    7,  176,   58,   58,   58,   58,    8,
   24,   58,  197,   58,   58,   58,   57,   57,   57,   24,
  198,   57,   57,    9,   58,   59,   60,   61,   57,  177,
  178,  199,  114,  169,   57,   57,   57,   57,   57,   25,
    5,   57,   57,   57,   57,  200,    7,   57,  201,   57,
   57,   57,    8,   26,   27,   28,   41,   41,   41,  179,
  180,   41,   41,    5,   73,   29,    9,   74,   41,    7,
  100,  100,   30,  167,   31,    8,  206,  207,   39,   39,
   39,  210,   41,   39,   39,  211,  212,   41,  213,    9,
   39,   41,  214,   40,   40,   40,   20,  131,   40,   40,
  104,   20,   20,  173,   39,   40,    5,   73,   20,   39,
   74,    5,    7,   39,  148,  144,  168,    7,    8,   40,
  102,   69,   20,    8,   40,   75,   20,   20,   40,    0,
    5,   20,    9,    5,    0,    0,    7,    9,    6,    7,
    0,  159,    8,    7,    6,    8,  184,    0,    0,    7,
    6,   89,   89,   89,    0,    7,    9,   89,    0,    9,
   90,   90,   90,   89,    6,  230,   90,    0,  232,    7,
    5,   73,   90,    6,   74,    0,    7,   89,    7,    0,
  170,    0,    8,  101,   89,   89,   90,    0,    0,   88,
   88,   88,    0,   90,   90,   88,    9,   26,   27,   28,
    0,   88,    0,    0,  218,   68,    0,    0,    0,   29,
   54,   55,   56,   57,   73,   88,    0,   74,   47,   62,
   63,    0,   88,   88,   68,   68,   68,   68,   68,   68,
   68,   68,   47,   68,   68,   67,   47,    0,    0,   98,
    0,    0,    0,  105,   58,   59,   60,   61,    8,   26,
   27,   28,    8,   26,   27,   28,    8,   26,   27,   28,
  117,   29,    0,    0,  119,   29,    0,    0,  121,   29,
    0,    0,    0,    8,   26,   27,   28,    8,   26,   27,
   28,    8,   26,   27,   28,  124,   29,    0,    0,  126,
   29,    0,    0,  142,   29,    0,    0,    0,    8,   26,
   27,   28,    8,   26,   27,   28,    8,   26,   27,   28,
  138,   29,    0,    0,    0,   29,   26,   27,   28,   29,
    0,    0,    0,    0,   26,   27,   28,    0,   29,    0,
    0,    0,    0,   86,    0,    0,   29,   54,   55,   56,
   57,   58,   59,   60,   61,    0,   62,   63,    8,   26,
   27,   28,  111,   54,   55,   56,   57,   58,   59,   60,
   61,   29,   62,   63,  221,  222,  223,  224,  225,  226,
  227,  229,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    5,    9,  131,  129,  256,   49,   38,  256,  174,  270,
  271,  272,  258,  259,    5,  273,  256,  270,  271,  256,
   64,  282,  256,  257,  273,  191,   31,  153,  286,  263,
  156,   75,   23,   38,   42,  269,  257,   69,  290,   30,
  289,  262,  263,   51,   88,   89,   90,   79,  269,  283,
  290,  177,  289,  179,  256,  287,  288,  291,   49,  256,
  269,   52,  283,  269,   69,  267,   71,   58,   59,   60,
   61,  292,  269,   64,   79,  291,   81,  283,   86,  111,
  112,  287,   73,   74,   75,   76,   77,   78,  256,  218,
  257,  256,  289,  288,  102,  262,  263,   88,   89,   90,
   59,   60,  269,  258,  259,  270,  111,  112,  256,  257,
  258,  259,  280,  281,  262,  263,  283,   76,   77,   78,
  279,  269,  269,  282,  129,  292,  131,  275,  276,  277,
  278,  279,  280,  281,  282,  283,  284,  285,  269,  256,
  288,  256,  290,  291,  292,  280,  281,  269,  153,  264,
  269,  156,  283,  270,  257,  160,  287,  257,  258,  259,
  263,  283,  262,  263,  283,  287,  269,  287,  287,  269,
  290,  273,  177,  256,  179,  275,  276,  277,  278,  279,
  283,  267,  282,  283,  284,  285,  269,  192,  288,  292,
  290,  291,  292,  269,  257,  286,  257,  258,  259,  204,
  263,  262,  263,  256,  262,  264,  269,  287,  269,  288,
  290,  269,  265,  218,  275,  276,  277,  278,  279,  264,
  283,  282,  283,  284,  285,  259,  231,  288,  291,  290,
  291,  292,  257,  258,  259,  270,  257,  262,  263,  262,
   58,  262,  263,   61,  269,  288,  269,  259,  269,  291,
  275,  276,  277,  278,  279,   73,   74,  282,  283,  284,
  285,  270,  283,  288,  270,  290,  291,  292,  257,  258,
  259,  292,  257,  262,  263,  258,  259,  262,  263,  256,
  269,  265,  258,  259,  269,  257,  275,  276,  277,  278,
  279,  263,  269,  282,  283,  284,  285,  269,  283,  288,
  265,  290,  291,  292,  257,  258,  259,  292,  257,  262,
  263,  283,  291,  262,  263,  256,  269,  265,  290,  291,
  269,  257,  275,  276,  277,  278,  279,  263,  269,  282,
  283,  284,  285,  269,  283,  288,  265,  290,  291,  292,
  257,  258,  259,  292,  257,  262,  263,  283,  259,  262,
  263,  256,  269,  259,  290,  291,  269,  257,  275,  276,
  277,  278,  279,  263,  269,  282,  283,  284,  285,  269,
  283,  288,  270,  290,  291,  292,  257,  258,  259,  292,
  270,  262,  263,  283,  279,  280,  281,  282,  269,  258,
  259,  270,  292,  288,  275,  276,  277,  278,  279,  256,
  257,  282,  283,  284,  285,  270,  263,  288,  270,  290,
  291,  292,  269,  270,  271,  272,  257,  258,  259,  258,
  259,  262,  263,  257,  279,  282,  283,  282,  269,  263,
  258,  259,  289,  288,  291,  269,  259,  259,  257,  258,
  259,  290,  283,  262,  263,  290,  290,  288,  290,  283,
  269,  292,  290,  257,  258,  259,  257,  291,  262,  263,
  259,  262,  263,  145,  283,  269,  257,  279,  269,  288,
  282,  257,  263,  292,   95,   91,  288,  263,  269,  283,
   51,   30,  283,  269,  288,   33,  287,  288,  292,   -1,
  257,  292,  283,  257,   -1,   -1,  263,  283,  257,  263,
   -1,  292,  269,  257,  263,  269,  292,   -1,   -1,  263,
  269,  257,  258,  259,   -1,  269,  283,  263,   -1,  283,
  257,  258,  259,  269,  283,  292,  263,   -1,  292,  283,
  257,  279,  269,  292,  282,   -1,  263,  283,  292,   -1,
  288,   -1,  269,  256,  290,  291,  283,   -1,   -1,  257,
  258,  259,   -1,  290,  291,  263,  283,  270,  271,  272,
   -1,  269,   -1,   -1,  291,  256,   -1,   -1,   -1,  282,
  275,  276,  277,  278,  279,  283,   -1,  282,  269,  284,
  285,   -1,  290,  291,  275,  276,  277,  278,  279,  280,
  281,  282,  283,  284,  285,  256,  287,   -1,   -1,  256,
   -1,   -1,   -1,  256,  279,  280,  281,  282,  269,  270,
  271,  272,  269,  270,  271,  272,  269,  270,  271,  272,
  256,  282,   -1,   -1,  256,  282,   -1,   -1,  256,  282,
   -1,   -1,   -1,  269,  270,  271,  272,  269,  270,  271,
  272,  269,  270,  271,  272,  256,  282,   -1,   -1,  256,
  282,   -1,   -1,  256,  282,   -1,   -1,   -1,  269,  270,
  271,  272,  269,  270,  271,  272,  269,  270,  271,  272,
  256,  282,   -1,   -1,   -1,  282,  270,  271,  272,  282,
   -1,   -1,   -1,   -1,  270,  271,  272,   -1,  282,   -1,
   -1,   -1,   -1,  287,   -1,   -1,  282,  275,  276,  277,
  278,  279,  280,  281,  282,   -1,  284,  285,  269,  270,
  271,  272,  290,  275,  276,  277,  278,  279,  280,  281,
  282,  282,  284,  285,  209,  210,  211,  212,  213,  214,
  215,  216,
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
"rama_if : sentencia_ejec",
"rama_if : LLAVEINIC bloque_ejecutable LLAVEFIN",
"rama_if : LLAVEINIC LLAVEFIN",
"rama_if :",
"rama_else : sentencia_ejec",
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
};

//#line 325 "gramaticaDeCero.y"

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
//#line 644 "Parser.java"
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
case 18:
//#line 95 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable"); }
break;
case 19:
//#line 96 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 22:
//#line 102 "gramaticaDeCero.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 23:
//#line 107 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
break;
case 24:
//#line 108 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
break;
case 25:
//#line 109 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, expresión inválida."); }
break;
case 26:
//#line 110 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
break;
case 27:
//#line 111 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
break;
case 28:
//#line 115 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
break;
case 30:
//#line 119 "gramaticaDeCero.y"
{yyerror("Error sintáctico: Falta nombre identificador de función");}
break;
case 33:
//#line 130 "gramaticaDeCero.y"
{yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
break;
case 35:
//#line 136 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de tipo en parámetro formal");}
break;
case 36:
//#line 137 "gramaticaDeCero.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 39:
//#line 147 "gramaticaDeCero.y"
{System.out.println("Asignación válida");}
break;
case 40:
//#line 152 "gramaticaDeCero.y"
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
case 41:
//#line 166 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de variables antes del '='"); }
break;
case 42:
//#line 167 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de constantes después del '='");}
break;
case 43:
//#line 172 "gramaticaDeCero.y"
{n_cte++;}
break;
case 44:
//#line 173 "gramaticaDeCero.y"
{n_cte++;}
break;
case 45:
//#line 174 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una constante después de coma");}
break;
case 46:
//#line 175 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
break;
case 47:
//#line 178 "gramaticaDeCero.y"
{n_var++;}
break;
case 48:
//#line 179 "gramaticaDeCero.y"
{n_var++;}
break;
case 49:
//#line 180 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta identificador después de coma");}
break;
case 50:
//#line 181 "gramaticaDeCero.y"
{yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
break;
case 55:
//#line 193 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 56:
//#line 194 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresión."); }
break;
case 58:
//#line 196 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 59:
//#line 197 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresión."); }
break;
case 62:
//#line 204 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 63:
//#line 205 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '*' en expresión."); }
break;
case 65:
//#line 207 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 66:
//#line 208 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresión."); }
break;
case 67:
//#line 209 "gramaticaDeCero.y"
{ yyerror("Falta operador entre factores en expresión."); }
break;
case 71:
//#line 221 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 72:
//#line 228 "gramaticaDeCero.y"
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
case 73:
//#line 242 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 75:
//#line 255 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 76:
//#line 257 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 78:
//#line 263 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 79:
//#line 264 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 80:
//#line 265 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 81:
//#line 267 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 82:
//#line 268 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 83:
//#line 269 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 84:
//#line 271 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 85:
//#line 272 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 86:
//#line 273 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 87:
//#line 274 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 89:
//#line 279 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 90:
//#line 280 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 99:
//#line 295 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 100:
//#line 296 "gramaticaDeCero.y"
{yyerror("Falta bloque del then");}
break;
case 103:
//#line 301 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 104:
//#line 302 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 105:
//#line 307 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 106:
//#line 308 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 107:
//#line 309 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 108:
//#line 310 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 109:
//#line 311 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 110:
//#line 312 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 111:
//#line 313 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 112:
//#line 314 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 113:
//#line 315 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 114:
//#line 316 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 116:
//#line 320 "gramaticaDeCero.y"
{System.out.println("BLOQUE FOR");}
break;
case 117:
//#line 321 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
//#line 1115 "Parser.java"
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
