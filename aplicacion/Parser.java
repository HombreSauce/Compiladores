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
   26,   26,   26,   26,   26,   27,   27,   25,   25,   25,
   25,   25,   11,   11,   11,   31,   31,   31,   31,   31,
   31,   31,   31,   31,   31,   31,   28,   28,   28,   32,
   32,   32,   32,   32,   32,   29,   29,   29,   29,   30,
   30,   30,   30,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   33,   33,   33,   13,   13,   13,   13,
   14,   34,   34,   35,   35,   36,   36,   36,   36,   36,
   36,   37,   37,   37,   37,   38,   38,
};
final static short yylen[] = {                            2,
    4,    4,    3,    4,    0,    2,    3,    2,    1,    2,
    1,    1,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    1,    3,    2,    1,    3,    3,    5,    4,    5,
    5,    5,    9,    1,    7,    1,    3,    3,    3,    3,
    2,    0,    1,    5,    4,    0,    3,    3,    2,    3,
    1,    3,    3,    2,    1,    3,    3,    2,    1,    3,
    1,    3,    3,    3,    3,    3,    3,    1,    3,    3,
    3,    3,    3,    3,    3,    1,    1,    1,    2,    1,
    2,    1,    6,    8,    1,    5,    5,    4,    7,    7,
    6,    3,    5,    6,    8,    5,    3,    3,    3,    1,
    1,    1,    1,    1,    1,    2,    3,    2,    0,    2,
    3,    2,    0,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    1,    3,    2,    4,    4,    4,    3,
    4,    1,    3,    3,    3,    1,    4,    3,    4,    3,
    1,   10,    9,    9,    8,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    5,    5,    5,    0,    0,    0,    0,
    0,   11,    0,    0,    0,    4,    6,    0,    0,   12,
   13,   16,   17,   18,   19,   20,   21,   22,   34,    0,
    0,   85,    2,    1,    0,   80,   78,   82,    0,    0,
    0,    0,    0,    0,   77,    0,   68,    0,    0,    0,
    0,    0,    0,    0,    0,   51,   14,    0,    0,    0,
    0,    0,    0,   59,    0,    0,    0,  102,  103,  104,
  105,    0,    0,    0,    0,  101,  100,    0,   81,   79,
    0,    0,    0,  108,    0,    0,  106,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   92,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  132,    0,
  141,    0,   54,    0,    0,    0,    0,   43,    0,   36,
    0,    0,   23,    0,   60,   50,    0,   57,    0,    0,
    0,   71,   74,    0,    0,    0,    0,    0,  107,    0,
    8,    0,    0,    0,    0,    0,    0,   75,   70,   69,
   73,   72,    0,    0,   88,    0,    0,    0,  129,  128,
  127,    0,    0,    0,    0,    0,    0,    0,    0,  131,
    0,   53,   52,    0,    0,    0,    0,    0,    0,    0,
   41,    0,   27,   26,    0,   96,    0,    0,   87,    7,
    0,   86,    0,  112,    0,  110,   93,    0,    0,    0,
    0,    0,    0,    0,    0,  133,  135,  134,   32,   31,
   30,   28,    0,   38,   37,    5,   40,   39,    0,   94,
    0,   83,    0,    0,   91,  111,    0,    0,    0,    0,
    0,    0,  139,  137,    0,    5,    0,    0,    0,   90,
   89,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   35,   95,   84,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  124,
  122,  115,  116,  117,  118,  119,  120,  121,  123,  114,
    0,    0,  146,  147,    0,    0,    0,   33,  126,    0,
    0,    0,  145,    0,    0,  125,  144,    0,  143,    0,
  142,   44,
};
final static short yydgoto[] = {                          3,
    7,   17,   85,  270,   19,   20,   21,   22,   23,   24,
   25,   26,   27,   28,   61,   43,  119,  268,   29,  120,
  121,   30,   31,   55,   45,   46,   47,   48,   49,  158,
   32,   78,  271,  108,  109,  110,  111,  285,
};
final static short yysindex[] = {                      -238,
 -268, -245,    0,    0,    0,    0, -207, -182, -158, -244,
 -224,    0, -220, -230,  478,    0,    0, -240, -174,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -179,
 -141,    0,    0,    0,  788,    0,    0,    0, -249,  361,
  -92, -217,  799, -179,    0, -247,    0,  472,  -37,  421,
  495, -183, -120,  519,  761,    0,    0, -185, -165, -177,
  -15,  421, -155,    0,  -16,  -64, -125,    0,    0,    0,
    0,  648,  775,  775,  648,    0,    0,  421,    0,    0,
  673, -125,  587,    0,  -90, -112,    0,  721,  726,  730,
  775,  748,  753,  290,   44,  600,    0, -124,  208,   64,
  244, -133,  -82, -204, -173,  -76,  -85,  -43,    0,  -96,
    0,  284,    0,  421,  421,  757, -177,    0,  -42,    0,
 -122,  -23,    0,  -85,    0,    0,  761,    0, -125,   28,
 -247,    0,    0, -247,  -85,  290, -199,   80,    0, -100,
    0,   28, -247,   28, -247, -124,  -85,    0,    0,    0,
    0,    0,  101,  600,    0,  -73,  -62,   51,    0,    0,
    0,   70,  111,  119, -246,  421,  421,   55,  519,    0,
   -4,    0,    0,  130,  324,   94,  388,   17, -227,  122,
    0,   49,    0,    0,  121,    0,  137,  600,    0,    0,
  600,    0,  156,    0,  -56,    0,    0,  151,  160,  166,
  167, -167,  267,  286,  127,    0,    0,    0,    0,    0,
    0,    0,  155,    0,    0,    0,    0,    0,  600,    0,
  600,    0,  188,  189,    0,    0,  179,  182,  183,  184,
  191, -193,    0,    0,  630,    0, -140,  192,  209,    0,
    0, -241,  177,  193,  194,  195,  198, -239,  325, -157,
  -32,    0,    0,    0,  640,  640,  640,  640,  640,  640,
  640,  640, -119, -130,  780,  200,  214,  190,  -51,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  780,  215,    0,    0,  207,  780,  421,    0,    0,  -49,
  227,  780,    0,  228,  293,    0,    0,  229,    0,  232,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  481,  152,
    0,    0,    0,  659,    0,    0,    0,  426,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -84,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  691,    0,   37,    0,  152,    0,    0,
    0,    0,    0,    0,  301,    0,    0,    0,  317,  -46,
  445,    0,    0,    0,    0,    0,  -45,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    1,  152,    0,    0, -109,    0,    0,    0,    0,
    0,    0,    0,  152,    0,  262,    0,    0,    0,    0,
  353,    0,    0,    0,    0,    0,  250,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -46,    0,    0,    0,
    0,    0,    0,  377,    0,    0,  390,    0,   54,    0,
   73,    0,    0,  109,  338,  152,  152,    0,    0,  -94,
    0,  145,  181,  217,  253,  555,  571,    0,    0,    0,
    0,    0,    0,  262,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  462,    0,  -46,    0,
    0,    0,    0,    0,    0,    0,    0,  262,    0,    0,
  262,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  251,  259,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  262,    0,
  262,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  247,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  256,
    0,    0,
};
final static short yygindex[] = {                         0,
   -1,    0, -156,  616,  -86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -48,  434,    0,    0,  373,
    0,  707,    0,  502,  603,  199,  176,  520,  -40, -147,
    0,  526,  829,    0,  402,    0,    0,  363,
};
final static int YYTABLESIZE=1092;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        195,
   76,   99,  101,    8,    9,  107,  193,   95,   91,  201,
    5,   35,   10,  124,  255,   11,  262,    1,   13,  168,
   79,   80,    4,  202,   14,   36,   37,   38,  214,  135,
    2,   50,   92,   93,  182,   52,   61,   39,   15,  118,
  223,  147,  138,  224,   40,    6,   41,   57,  256,   10,
  263,  164,   11,  153,   12,   13,  186,   10,   54,  165,
   11,   14,  247,   13,   51,  174,  175,  177,   53,   14,
   87,  238,   64,  239,   10,   15,  248,   11,  250,   12,
   13,   58,  166,   15,   16,  102,   14,  114,  231,  118,
  115,   41,  264,   62,   59,  185,  187,  232,   10,   10,
   15,   11,   11,   12,   13,   13,   63,  116,   67,   33,
   14,   14,  290,  125,   60,  167,   10,  203,  204,   11,
  107,   12,   13,  117,   15,   15,   10,   64,   14,   11,
  162,  265,   13,   34,  266,  103,  279,   10,   14,   12,
   11,   65,   15,   13,   63,   66,  181,    9,  104,   14,
    9,  252,   15,    9,   72,   73,   74,   75,  281,    9,
   63,  282,   10,   15,   10,   10,   10,   11,   10,   11,
   13,  269,   13,    9,   10,  141,   14,  171,   14,    9,
   62,  163,    9,   10,   55,   12,   11,  190,   10,   13,
   15,  128,   15,   88,   10,   14,   89,   10,   55,   84,
   10,  139,   55,   11,   64,   10,   13,   10,   11,   15,
   11,   13,   14,   13,  237,   42,   66,   14,  194,   14,
   96,   97,   42,   58,   10,  196,   15,   11,  267,   12,
   13,   15,  183,   15,  251,  226,   14,   58,  295,  126,
  289,   58,  296,  169,  179,  184,  170,  180,  132,  133,
   15,  207,   65,   36,   37,   38,   76,   76,   76,   76,
   76,   76,   76,   76,  208,   39,  148,  150,  152,   76,
  131,  122,  123,  134,   76,   76,   76,   76,   76,   76,
   76,   76,   76,   76,   76,   76,  143,  145,   76,   76,
   76,   76,   76,   61,   61,   61,   61,   61,   61,   61,
   49,  154,  155,  179,  217,   61,  213,   73,   74,  197,
   61,   61,   61,   61,   61,   61,   25,  218,   61,   61,
   61,   61,   56,  205,   61,   61,   61,   61,   61,   64,
   64,   64,   64,   64,   64,   64,   56,  188,  189,  198,
   56,   64,   72,   73,   74,   75,   64,   64,   64,   64,
   64,   64,  130,  160,   64,   64,   64,   64,  191,  192,
   64,   64,   64,   64,   64,   67,   67,   67,   67,   67,
   67,   67,   72,   73,   74,   75,   47,   67,  219,  220,
  199,  211,   67,   67,   67,   67,   67,   67,  200,   48,
   67,   67,   67,   67,  221,  222,   67,   67,   67,   67,
   67,   63,   63,   63,   63,   63,   63,   63,   88,  109,
  109,   89,  216,   63,  225,  227,  235,  209,   63,   63,
   63,   63,   63,   63,  228,   15,   63,   63,   63,   63,
  229,  230,   63,   63,   63,   63,   63,   62,   62,   62,
   62,   62,   62,   62,   24,  236,  240,  241,  242,   62,
  253,  243,  244,  245,   62,   62,   62,   62,   62,   62,
  246,   29,   62,   62,   62,   62,  257,  254,   62,   62,
   62,   62,   62,   66,   66,   66,   66,   66,   66,   66,
    3,  288,  258,  259,  260,   66,   88,  261,  286,   89,
   66,   66,   66,   66,   66,   66,  293,  159,   66,   66,
   66,   66,  287,  292,   66,   66,   66,   66,   66,   65,
   65,   65,   65,   65,   65,   65,  297,  299,  301,  302,
  113,   65,   88,  136,  140,   89,   65,   65,   65,   65,
   65,   65,  138,  161,   65,   65,   65,   65,   46,  172,
   65,   65,   65,   65,   65,   88,   10,   45,   89,   11,
  178,  215,   13,   36,   37,   38,  233,   49,   14,   83,
   49,   49,   49,   49,   88,   39,  127,   89,   90,   49,
  206,   88,   15,   25,   89,  234,   25,   25,   25,   25,
   41,   10,  300,   49,   11,   25,    0,   13,   49,   49,
    0,    0,   49,   14,   98,   98,   98,   98,    0,   25,
   98,    0,   88,   25,   25,   89,   98,   15,   25,  130,
    0,  210,  130,  130,  130,  130,   81,   56,    0,    0,
   98,  130,   18,   18,   18,   42,    0,   98,   98,   64,
   36,   37,   38,   47,    0,  130,   47,   47,   47,   47,
  130,  130,   39,  291,  130,   47,   48,    0,  294,   48,
   48,   48,   48,    0,  298,    0,   86,  113,   48,   47,
    0,    0,    0,   42,   47,   47,   88,   56,   47,   89,
    0,    0,   48,    0,    0,  212,   98,   48,   48,    0,
    0,   48,   15,    0,    0,   15,   15,   15,   15,   64,
   36,   37,   38,    0,   15,    0,    0,    0,   42,    0,
  140,   24,   39,    0,   24,   24,   24,   24,   15,   42,
    0,  157,    0,   24,  173,    0,   44,   15,   29,    0,
    0,   29,   29,   29,   29,    0,    0,   24,   10,  113,
   29,   11,    0,    0,   13,    0,   24,   67,    0,    0,
   14,    0,    0,    0,   29,    0,   82,   36,   37,   38,
  100,   42,   42,   29,   15,    0,   82,   82,    0,   39,
   82,   94,   41,   64,   36,   37,   38,    0,   82,  157,
    0,   86,  129,    0,   98,    0,   39,    0,   82,   82,
   82,   82,    0,    0,   82,    0,  105,   64,   36,   37,
   38,    0,    0,    0,   82,   82,   82,   82,   82,   82,
   39,    0,    0,  157,    0,    0,  157,  106,    0,    0,
  140,   99,   99,   99,   99,    0,    0,   99,    0,    0,
   82,   82,   82,   99,    0,    0,    0,   97,   97,   97,
   97,    0,    0,   97,  157,    0,  157,   99,    0,   97,
    0,    0,    0,   10,   99,   99,   11,    0,    0,   13,
   86,    0,   18,   97,    0,   14,   10,    0,    0,   11,
   97,   97,   13,    0,   86,  140,   18,  284,   14,   15,
    0,    0,   82,   82,    0,   82,  137,   41,    0,  140,
    0,    0,   15,  284,   86,    0,   10,    0,  284,   11,
  156,    0,   13,    0,  284,    0,   10,    0,   14,   11,
    0,    0,   13,  130,    0,  140,    0,    0,   14,    0,
    0,    0,   15,    0,   59,    0,   64,   36,   37,   38,
  249,    0,   15,    0,    0,    0,    0,   59,    0,   39,
  269,   59,    0,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   59,   59,   59,   59,   76,   68,   69,   70,
   71,   72,   73,   74,   75,    0,   76,   77,    0,   55,
    0,    0,  136,    0,    0,   76,   76,   76,   76,   76,
   76,   76,   76,   55,   76,   76,  142,   55,    0,    0,
    0,  144,    0,    0,    0,  146,    0,    0,    0,   64,
   36,   37,   38,   82,   64,   36,   37,   38,   64,   36,
   37,   38,   39,  149,    0,    0,    0,   39,  151,    0,
    0,   39,  176,    0,    0,    0,   64,   36,   37,   38,
    0,   64,   36,   37,   38,   64,   36,   37,   38,   39,
   36,   37,   38,    0,   39,    0,    0,    0,   39,    0,
    0,    0,   39,   64,   36,   37,   38,  112,  283,   36,
   37,   38,    0,    0,    0,    0,   39,    0,    0,    0,
    0,   39,   68,   69,   70,   71,   72,   73,   74,   75,
    0,   76,   77,   68,   69,   70,   71,   88,    0,    0,
   89,    0,   76,   77,  272,  273,  274,  275,  276,  277,
  278,  280,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        156,
    0,   50,   51,    5,    6,   54,  154,   48,  256,  256,
  256,  256,  257,   62,  256,  260,  256,  256,  263,  106,
  270,  271,  291,  270,  269,  270,  271,  272,  256,   78,
  269,  256,  280,  281,  121,  256,    0,  282,  283,  267,
  188,   90,   83,  191,  289,  291,  291,  288,  290,  257,
  290,  256,  260,   94,  262,  263,  256,  257,  289,  264,
  260,  269,  256,  263,  289,  114,  115,  116,  289,  269,
  288,  219,    0,  221,  257,  283,  270,  260,  235,  262,
  263,  256,  256,  283,  292,  269,  269,  273,  256,  267,
  256,  291,  249,  273,  269,  136,  137,  265,  257,  257,
  283,  260,  260,  262,  263,  263,  286,  273,    0,  292,
  269,  269,  269,  269,  289,  289,  257,  166,  167,  260,
  169,  262,  263,  289,  283,  283,  257,  269,  269,  260,
  264,  289,  263,  292,  292,  256,  256,  257,  269,  262,
  260,  283,  283,  263,    0,  287,  269,  257,  269,  269,
  260,  292,  283,  263,  279,  280,  281,  282,  289,  269,
  286,  292,  257,  283,  257,  260,  257,  260,  263,  260,
  263,  291,  263,  283,  269,  288,  269,  274,  269,  289,
    0,  264,  292,  257,  269,  262,  260,  288,  283,  263,
  283,  256,  283,  279,  289,  269,  282,  292,  283,  292,
  257,  292,  287,  260,  269,  257,  263,  257,  260,  283,
  260,  263,  269,  263,  216,  262,    0,  269,  292,  269,
  258,  259,  269,  269,  257,  288,  283,  260,  261,  262,
  263,  283,  256,  283,  236,  292,  269,  283,  287,  256,
  292,  287,  292,  287,  287,  269,  290,  290,   73,   74,
  283,  256,    0,  270,  271,  272,  256,  257,  258,  259,
  260,  261,  262,  263,  269,  282,   91,   92,   93,  269,
   72,  287,  288,   75,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,   88,   89,  288,  289,
  290,  291,  292,  257,  258,  259,  260,  261,  262,  263,
    0,  258,  259,  287,  256,  269,  290,  280,  281,  259,
  274,  275,  276,  277,  278,  279,    0,  269,  282,  283,
  284,  285,  269,  269,  288,  289,  290,  291,  292,  257,
  258,  259,  260,  261,  262,  263,  283,  258,  259,  270,
  287,  269,  279,  280,  281,  282,  274,  275,  276,  277,
  278,  279,    0,  290,  282,  283,  284,  285,  258,  259,
  288,  289,  290,  291,  292,  257,  258,  259,  260,  261,
  262,  263,  279,  280,  281,  282,    0,  269,  258,  259,
  270,  288,  274,  275,  276,  277,  278,  279,  270,    0,
  282,  283,  284,  285,  258,  259,  288,  289,  290,  291,
  292,  257,  258,  259,  260,  261,  262,  263,  279,  258,
  259,  282,  291,  269,  259,  265,  290,  288,  274,  275,
  276,  277,  278,  279,  265,    0,  282,  283,  284,  285,
  265,  265,  288,  289,  290,  291,  292,  257,  258,  259,
  260,  261,  262,  263,    0,  291,  259,  259,  270,  269,
  259,  270,  270,  270,  274,  275,  276,  277,  278,  279,
  270,    0,  282,  283,  284,  285,  290,  259,  288,  289,
  290,  291,  292,  257,  258,  259,  260,  261,  262,  263,
    0,  292,  290,  290,  290,  269,  279,  290,  289,  282,
  274,  275,  276,  277,  278,  279,  290,  290,  282,  283,
  284,  285,  289,  289,  288,  289,  290,  291,  292,  257,
  258,  259,  260,  261,  262,  263,  290,  290,  290,  288,
  259,  269,  279,  274,  274,  282,  274,  275,  276,  277,
  278,  279,  274,  290,  282,  283,  284,  285,  292,  256,
  288,  289,  290,  291,  292,  279,  257,  292,  282,  260,
  117,  179,  263,  270,  271,  272,  290,  257,  269,   40,
  260,  261,  262,  263,  279,  282,   65,  282,   43,  269,
  169,  279,  283,  257,  282,  290,  260,  261,  262,  263,
  291,  257,  290,  283,  260,  269,   -1,  263,  288,  289,
   -1,   -1,  292,  269,  257,  258,  259,  260,   -1,  283,
  263,   -1,  279,  287,  288,  282,  269,  283,  292,  257,
   -1,  288,  260,  261,  262,  263,  256,   15,   -1,   -1,
  283,  269,    7,    8,    9,   10,   -1,  290,  291,  269,
  270,  271,  272,  257,   -1,  283,  260,  261,  262,  263,
  288,  289,  282,  281,  292,  269,  257,   -1,  286,  260,
  261,  262,  263,   -1,  292,   -1,   41,   55,  269,  283,
   -1,   -1,   -1,   48,  288,  289,  279,   65,  292,  282,
   -1,   -1,  283,   -1,   -1,  288,  256,  288,  289,   -1,
   -1,  292,  257,   -1,   -1,  260,  261,  262,  263,  269,
  270,  271,  272,   -1,  269,   -1,   -1,   -1,   83,   -1,
   85,  257,  282,   -1,  260,  261,  262,  263,  283,   94,
   -1,   96,   -1,  269,  112,   -1,   10,  292,  257,   -1,
   -1,  260,  261,  262,  263,   -1,   -1,  283,  257,  127,
  269,  260,   -1,   -1,  263,   -1,  292,   31,   -1,   -1,
  269,   -1,   -1,   -1,  283,   -1,   40,  270,  271,  272,
  256,  136,  137,  292,  283,   -1,   50,   51,   -1,  282,
   54,  290,  291,  269,  270,  271,  272,   -1,   62,  154,
   -1,  156,   66,   -1,  256,   -1,  282,   -1,   72,   73,
   74,   75,   -1,   -1,   78,   -1,  268,  269,  270,  271,
  272,   -1,   -1,   -1,   88,   89,   90,   91,   92,   93,
  282,   -1,   -1,  188,   -1,   -1,  191,  289,   -1,   -1,
  195,  257,  258,  259,  260,   -1,   -1,  263,   -1,   -1,
  114,  115,  116,  269,   -1,   -1,   -1,  257,  258,  259,
  260,   -1,   -1,  263,  219,   -1,  221,  283,   -1,  269,
   -1,   -1,   -1,  257,  290,  291,  260,   -1,   -1,  263,
  235,   -1,  237,  283,   -1,  269,  257,   -1,   -1,  260,
  290,  291,  263,   -1,  249,  250,  251,  265,  269,  283,
   -1,   -1,  166,  167,   -1,  169,  290,  291,   -1,  264,
   -1,   -1,  283,  281,  269,   -1,  257,   -1,  286,  260,
  291,   -1,  263,   -1,  292,   -1,  257,   -1,  269,  260,
   -1,   -1,  263,  256,   -1,  290,   -1,   -1,  269,   -1,
   -1,   -1,  283,   -1,  256,   -1,  269,  270,  271,  272,
  291,   -1,  283,   -1,   -1,   -1,   -1,  269,   -1,  282,
  291,  273,   -1,  275,  276,  277,  278,  279,  280,  281,
  282,  283,  284,  285,  286,  287,  256,  275,  276,  277,
  278,  279,  280,  281,  282,   -1,  284,  285,   -1,  269,
   -1,   -1,  290,   -1,   -1,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  256,  287,   -1,   -1,
   -1,  256,   -1,   -1,   -1,  256,   -1,   -1,   -1,  269,
  270,  271,  272,  287,  269,  270,  271,  272,  269,  270,
  271,  272,  282,  256,   -1,   -1,   -1,  282,  256,   -1,
   -1,  282,  256,   -1,   -1,   -1,  269,  270,  271,  272,
   -1,  269,  270,  271,  272,  269,  270,  271,  272,  282,
  270,  271,  272,   -1,  282,   -1,   -1,   -1,  282,   -1,
   -1,   -1,  282,  269,  270,  271,  272,  287,  269,  270,
  271,  272,   -1,   -1,   -1,   -1,  282,   -1,   -1,   -1,
   -1,  282,  275,  276,  277,  278,  279,  280,  281,  282,
   -1,  284,  285,  275,  276,  277,  278,  279,   -1,   -1,
  282,   -1,  284,  285,  256,  257,  258,  259,  260,  261,
  262,  263,
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
//#line 775 "Parser.java"
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
//#line 115 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
break;
case 29:
//#line 116 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
break;
case 30:
//#line 117 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, expresión inválida."); }
break;
case 31:
//#line 118 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
break;
case 32:
//#line 119 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
break;
case 33:
//#line 123 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
break;
case 35:
//#line 127 "gramaticaDeCero.y"
{yyerror("Error sintáctico: Falta nombre identificador de función");}
break;
case 38:
//#line 138 "gramaticaDeCero.y"
{yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
break;
case 40:
//#line 144 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de tipo en parámetro formal");}
break;
case 41:
//#line 145 "gramaticaDeCero.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 44:
//#line 152 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Return"); }
break;
case 45:
//#line 153 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 47:
//#line 160 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Asignacion simple"); }
break;
case 48:
//#line 165 "gramaticaDeCero.y"
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
//#line 179 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de variables antes del '='"); }
break;
case 50:
//#line 180 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de constantes después del '='");}
break;
case 51:
//#line 185 "gramaticaDeCero.y"
{n_cte++;}
break;
case 52:
//#line 186 "gramaticaDeCero.y"
{n_cte++;}
break;
case 53:
//#line 187 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una constante después de coma");}
break;
case 54:
//#line 188 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
break;
case 55:
//#line 191 "gramaticaDeCero.y"
{n_var++;}
break;
case 56:
//#line 192 "gramaticaDeCero.y"
{n_var++;}
break;
case 57:
//#line 193 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta identificador después de coma");}
break;
case 58:
//#line 194 "gramaticaDeCero.y"
{yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
break;
case 63:
//#line 206 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 64:
//#line 207 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresión."); }
break;
case 66:
//#line 209 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 67:
//#line 210 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresión."); }
break;
case 70:
//#line 217 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 71:
//#line 218 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '*' en expresión."); }
break;
case 73:
//#line 220 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 74:
//#line 221 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresión."); }
break;
case 75:
//#line 222 "gramaticaDeCero.y"
{ yyerror("Falta operador entre factores en expresión."); }
break;
case 79:
//#line 234 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 80:
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
case 81:
//#line 255 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 83:
//#line 268 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 84:
//#line 270 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 86:
//#line 276 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 87:
//#line 277 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 88:
//#line 278 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 89:
//#line 280 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 90:
//#line 281 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 91:
//#line 282 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 92:
//#line 284 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 93:
//#line 285 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 94:
//#line 286 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 95:
//#line 287 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 96:
//#line 288 "gramaticaDeCero.y"
{yyerror("Falta bloque del if");}
break;
case 98:
//#line 293 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 99:
//#line 294 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 108:
//#line 309 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 109:
//#line 310 "gramaticaDeCero.y"
{yyerror("Falta bloque del then");}
break;
case 112:
//#line 315 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 113:
//#line 316 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 114:
//#line 321 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 115:
//#line 322 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 116:
//#line 323 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 117:
//#line 324 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 118:
//#line 325 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 119:
//#line 326 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 120:
//#line 327 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 121:
//#line 328 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 122:
//#line 329 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 123:
//#line 330 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 126:
//#line 336 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 127:
//#line 341 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 128:
//#line 342 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 129:
//#line 343 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 130:
//#line 344 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 131:
//#line 351 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 135:
//#line 361 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 137:
//#line 365 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 138:
//#line 366 "gramaticaDeCero.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 139:
//#line 367 "gramaticaDeCero.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 140:
//#line 368 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 142:
//#line 374 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Lambda"); System.out.println("HOLAAAA");}
break;
case 143:
//#line 375 "gramaticaDeCero.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 144:
//#line 376 "gramaticaDeCero.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 145:
//#line 377 "gramaticaDeCero.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1322 "Parser.java"
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
