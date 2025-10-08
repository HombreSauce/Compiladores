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






//#line 4 "gramatica.y"
	package aplicacion;
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

	


//#line 38 "Parser.java"




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
    4,    4,    4,    4,    4,    4,    4,    5,    5,    5,
   15,   15,   13,   13,   13,   13,    6,    6,    6,    6,
    7,    7,    7,    7,   17,   17,   17,   18,   18,   18,
   18,   18,   16,   16,   16,   16,   16,   19,   19,   19,
   19,   19,   20,   20,   20,   11,   11,   21,   21,   22,
   22,   23,   23,   23,   23,   23,   23,   12,   14,   14,
   25,   25,   25,   26,   26,    8,    8,    8,    8,    8,
    8,    8,    8,    8,   27,   27,   27,   27,   30,   30,
   30,   30,   30,   30,   28,   28,   29,   29,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,   31,   31,
   10,   10,   10,   10,   24,   24,   24,   24,   32,   32,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    3,    2,    2,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    6,
    1,    3,    1,    3,    3,    2,    3,    3,    3,    3,
    3,    2,    2,    3,    1,    3,    3,    1,    2,    1,
    2,    1,    3,    3,    3,    3,    1,    3,    3,    3,
    3,    1,    1,    1,    1,    4,    4,    1,    3,    3,
    3,    1,    4,    4,    4,    4,    1,    4,    1,    3,
    3,    3,    3,    0,    1,    6,    5,    6,    4,    6,
    7,    7,    7,    4,    3,    3,    3,    3,    1,    1,
    1,    1,    1,    1,    1,    3,    2,    2,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    2,    3,
    4,    4,    4,    4,   10,   10,   10,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    3,    0,   11,   12,   13,   14,   15,
   16,   17,    0,    0,    0,    0,    0,   40,   38,   42,
    0,    0,   54,    0,    0,   55,    0,   52,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   35,    7,
   21,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   58,    0,   67,   91,   92,   93,
   94,   90,   89,    4,   95,    0,    0,   41,   39,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   18,    0,   10,    9,    0,    0,
    0,    0,    0,   34,    0,   25,    0,    0,    0,    0,
   22,    0,    0,    0,    0,   57,    0,    0,   84,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   48,    0,   50,    0,   79,  113,  112,  114,  111,   68,
    0,    0,   75,    0,   69,    0,    0,    0,    0,    0,
   56,   37,   36,    0,    0,    0,   59,   61,   60,   96,
    0,    0,    0,    0,   77,   19,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   66,   65,   64,   63,    0,
    5,   78,    0,    0,   80,   76,    0,   70,    2,   73,
   72,   71,    0,    0,    0,    0,    0,    0,    4,    4,
    0,   97,   83,   82,   81,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    4,
    0,  107,  100,  101,  102,  103,  104,  105,  106,    0,
   99,    0,    0,    0,    0,    0,  109,    0,    0,    0,
  119,  120,    0,    0,    0,  110,  118,  116,  117,  115,
};
final static short yydgoto[] = {                          2,
    4,   75,  118,  231,   97,   16,   17,   18,   19,   20,
   33,   22,   23,  144,   34,   63,   48,   36,   37,   38,
   64,   65,   66,   67,  145,  146,   39,   76,  184,   77,
  232,  250,
};
final static short yysindex[] = {                      -254,
 -286,    0,    0, -192, -264,  751, -238, -229, -123, -235,
 -190,  332,    0,    0, -124,    0,    0,    0,    0,    0,
    0,    0,  -82, -147,  -76,  724,  564,    0,    0,    0,
 -127,   -1,    0, -179,  752,    0,  -56,    0,  600,  -76,
  337,  -76,  -80,  -80,  -77, -121,  724,  -90,    0,    0,
    0,  806,  -70, -179,  -90,  -76,  389,  -18,  -24, -242,
 -195,    5, -242,  -40,    0,   10,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   13,  -76,    0,    0,  853,
 -252,  -76,  441,  776,  782,  799,  803, -249,   47,  156,
   -7, -169,  193,   51,    0,   76,    0,    0,   94,   95,
 -201,   31,  823,    0,  -90,    0, -179, -242,  -24, -242,
    0,  -76,  -76,   92,  724,    0,  -29,  499,    0, -242,
 -249,  667, -242,  -24,  -56,  -24,  -56,  -24, -242,  -24,
    0,  -24,    0,  103,    0,    0,    0,    0,    0,    0,
 -163, -179,    0,   32,    0,  -33,   93,  107,  108, -125,
    0,    0,    0, -167, -160,   74,    0,    0,    0,    0,
  104,  121, -153,  -93,    0,    0,   76,  105,  125,  -28,
  130,  143,  149,  150,  -74,    0,    0,    0,    0, -255,
    0,    0,  682,  158,    0,    0, -164,    0,    0,    0,
    0,    0,  159,  160,  161,  162,  163,  -58,    0,    0,
 -264,    0,    0,    0,    0, -181, -246,  146,  147,  155,
  164,  176, -240,  514,  524,    0,  699,  699,  699,  699,
  699,  699,  699,  699,  714, -206,  178, -157,  179,    0,
  181,    0,    0,    0,    0,    0,    0,    0,    0, -264,
    0,  730,  863,  730,  863,  539,    0, -190,    0,  180,
    0,    0,  183,  184,  192,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   17,    0,    0,    0, -230,    0,    0,    0,    0,    0,
    0,    0,    0,  827,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -107,    0,    0,  128,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  313,    0,    0,
    0,    0,    0,  -39,  328,    0,    0,    0,    0,  365,
    0,    0,  177,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -10,    0,    0,    0,    0,
    0,    0,    0,    0,  380,    0,  820,  417,  432,  469,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  610,
    0,    0,  625,  165,  202,  239,  276,  642,  657,   54,
    0,   91,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -129,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  554,    0,    0,    0,    0,    0,  -10,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   68,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  484,
    0,    0,    0,    0,    0,    0,    0,  837,  119,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  263,   -2,  -96,   20,  444,    0,    0,    0,    0,    0,
   39,    0,  395,    0,   -4,   16,   22,   -6,  242,  254,
  456,  376,    0,    0,  338,    0,  472,  -36,  342,  474,
  928,  112,
};
final static int YYTABLESIZE=1153;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         24,
  199,   14,   89,  121,    3,   49,    5,    6,   25,  217,
    7,    8,    9,   10,    1,  224,   49,   40,   54,   11,
   45,   35,   24,   15,   26,    8,    8,    8,    8,    8,
    8,    8,    8,   12,   24,  200,   83,  122,    8,   84,
   60,   74,   21,  218,   55,   49,   15,   35,  107,  225,
   41,  134,    8,   46,  149,   90,   92,   93,   15,   42,
  112,    8,  150,    5,    6,   21,   25,    7,    8,    9,
   10,  108,  110,  105,    5,    6,   11,   21,    7,    8,
    9,   10,  242,   24,  162,  164,  138,   11,  176,  142,
   12,  204,  120,  113,  205,  178,  153,  123,   47,   13,
  129,   12,  214,  215,  183,   51,   58,   15,   56,   83,
  216,   83,   84,   24,   84,   25,   24,   24,   83,   25,
  139,   84,  177,   53,  166,   57,   21,  154,  155,  179,
  174,  244,   43,  246,  100,   26,   54,  161,   58,   23,
   15,   15,   78,   79,  175,   44,  202,  101,   53,   53,
   53,   53,   53,   53,   53,   53,   21,   23,   23,   21,
   21,   53,  185,   50,  183,  186,   53,   53,   53,   53,
   53,   53,   53,   53,   53,   53,   53,   53,   24,   59,
   53,  197,   53,   53,   53,  106,   51,   28,   29,   30,
  198,   99,   11,   28,   29,   30,  103,  212,   51,   31,
   52,   24,   15,   14,   53,   31,   94,   95,   96,   24,
   24,  213,   24,   24,   24,   24,   24,   24,   24,   24,
   24,   21,  169,   86,   87,   15,  158,  191,  170,   26,
   26,   26,   26,  161,  161,  249,  252,  249,  252,  159,
  192,   24,   26,   26,   21,   74,  115,   26,   26,  116,
  111,   74,   21,   21,   80,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   26,  161,  114,   11,   28,   29,
   30,  119,   21,   21,   21,   21,   21,   21,   21,   21,
   31,   26,  137,  117,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,  135,   21,   21,   21,   49,
   49,   49,   49,   49,   49,   49,   49,  115,  167,   51,
  151,  168,   49,   98,  125,  127,   98,   49,   49,   49,
   49,   49,   49,   49,   49,   49,   49,   49,   49,  131,
  133,   49,  143,   49,   49,   49,   51,   51,   51,   51,
   51,   51,   51,   51,  253,  254,  255,  147,  148,   51,
  156,  165,  171,  180,   51,   51,   51,   51,   51,   51,
   51,   51,   51,   51,   51,   51,  172,  173,   51,  182,
   51,   51,   51,   47,   47,   47,   47,   47,   47,   47,
   47,  181,   55,  190,  193,  189,   47,   55,   55,   55,
   55,   47,   47,   47,   47,   47,   47,  194,  120,   47,
   47,   47,   47,  195,  196,   47,  203,   47,   47,   47,
   44,   44,   44,   44,   44,   44,   44,   44,  207,  208,
  209,  210,  211,   44,   83,  219,  220,   84,   44,   44,
   44,   44,   44,   44,  221,  136,   44,   44,   44,   44,
   62,  206,   44,  222,   44,   44,   44,   43,   43,   43,
   43,   43,   43,   43,   43,  223,  243,  245,  247,  257,
   43,   83,  258,  259,   84,   43,   43,   43,   43,   43,
   43,  260,  140,   43,   43,   43,   43,   98,  141,   43,
  157,   43,   43,   43,   46,   46,   46,   46,   46,   46,
   46,   46,  102,   81,  188,  187,    0,   46,   85,    0,
    0,    0,   46,   46,   46,   46,   46,   46,    0,    0,
   46,   46,   46,   46,    0,    0,   46,    0,   46,   46,
   46,   45,   45,   45,   45,   45,   45,   45,   45,    0,
    0,    0,    0,    0,   45,    0,    0,    0,    0,   45,
   45,   45,   45,   45,   45,    0,    0,   45,   45,   45,
   45,    0,    0,   45,    0,   45,   45,   45,   32,   32,
   32,   32,   32,   32,   32,   32,    0,    0,    0,    0,
    0,   32,    0,   33,   33,   33,   33,   33,   33,   33,
   33,    0,   91,    0,    0,   32,   33,    0,    0,    0,
   32,   28,   29,   30,   32,   11,   28,   29,   30,    0,
   33,    0,    0,   31,    0,   33,    0,    0,   31,   33,
   29,   29,   29,   29,   29,   29,   29,   29,    0,    0,
    0,    0,    0,   29,    0,   31,   31,   31,   31,   31,
   31,   31,   31,    0,  109,    0,    0,   29,   31,    0,
    0,    0,   29,    0,    0,    0,   29,   11,   28,   29,
   30,    0,   31,    0,    0,    0,    0,   31,    0,    0,
   31,   31,   30,   30,   30,   30,   30,   30,   30,   30,
    0,    0,    0,    0,    0,   30,    0,   28,   28,   28,
   28,   28,   28,   28,   28,    0,  124,    0,    0,   30,
   28,    0,    0,    0,   30,    0,    0,    0,   30,   11,
   28,   29,   30,    0,   28,    0,    0,    0,    0,   28,
    0,    0,   31,   28,   27,   27,   27,   27,   27,   27,
   27,   27,    0,    0,    0,    0,    0,   27,    0,  108,
  108,  108,  108,  108,  108,  108,  108,    0,    0,    0,
    0,   27,  108,    0,    5,    6,   27,    0,    7,    8,
   27,   10,    0,    0,    0,    0,  108,   11,    0,  226,
    6,  108,    0,    7,    8,  108,   10,    0,    0,  228,
    6,   12,   11,    7,    8,    0,   10,    0,    0,    0,
  160,    0,   11,    0,    5,    6,   12,    0,    7,    8,
    0,   10,    0,    0,    0,  227,   12,   11,    0,    6,
    6,    0,    0,    6,    6,  229,    6,    0,    0,    5,
    6,   12,    6,    7,    8,    9,   10,    0,    0,    0,
  256,    0,   11,    0,    0,    0,    6,    0,   68,   69,
   70,   71,    0,    0,    0,    6,   12,   72,   73,    0,
    0,    0,   26,    0,   74,    5,    6,    0,    0,    7,
    8,    9,   10,    0,    0,   87,   87,    0,   11,   87,
   87,   87,   87,    0,    0,    0,    0,    0,   87,    0,
   86,   86,   12,    0,   86,   86,   86,   86,    0,   88,
   74,    0,   87,   86,    0,    0,    0,   88,   88,   87,
   87,   88,   88,   88,   88,    0,    0,   86,    0,    0,
   88,    0,   85,   85,   86,   86,   85,   85,   85,   85,
    0,    0,  163,    6,   88,   85,    7,    8,    9,   10,
    0,   88,   88,    0,    0,   11,    0,  201,    6,   85,
    0,    7,    8,    9,   10,    0,   85,   85,    0,   12,
   11,    0,    0,    0,    5,    6,    0,   74,    7,    8,
    0,   10,    0,    0,   12,    0,    0,   11,    0,  240,
    6,    0,   74,    7,    8,    0,   10,    0,    0,   59,
    0,   12,   11,    0,    0,   59,    0,    0,    0,  230,
    0,   61,   11,   28,   29,   30,   12,   61,  248,   28,
   29,   30,    0,    0,  230,   31,   27,   82,    0,    0,
    0,   31,   62,    0,    0,    0,    0,    0,   62,   11,
   28,   29,   30,    0,    0,    0,   68,   69,   70,   71,
   83,  126,   31,   84,    0,   72,   73,  128,    0,   32,
    0,    0,    0,    0,   11,   28,   29,   30,    0,    0,
   11,   28,   29,   30,  130,    0,    0,   31,  132,    0,
    0,  104,    0,   31,    0,    0,    0,   11,   28,   29,
   30,   11,   28,   29,   30,   28,   29,   30,  152,    0,
   31,    0,    0,    0,   31,    0,    0,   31,   24,   24,
   24,   24,   28,   29,   30,   23,   23,   23,   23,    0,
    0,   24,   24,    0,   31,    0,   24,   24,   23,   23,
   21,    0,    0,   23,    0,   21,   21,   21,   21,    0,
    0,    0,   21,    0,    0,    0,  119,   68,   69,   70,
   71,  251,   28,   29,   30,    0,   72,   73,    0,    0,
    0,   26,    0,    0,   31,  233,  234,  235,  236,  237,
  238,  239,  241,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
  256,    4,   39,  256,  291,   12,  256,  257,  273,  256,
  260,  261,  262,  263,  269,  256,   23,  256,   23,  269,
  256,    6,   27,    4,  289,  256,  257,  258,  259,  260,
  261,  262,  263,  283,   39,  291,  279,  290,  269,  282,
   25,  291,    4,  290,   23,   52,   27,   32,   53,  290,
  289,   88,  283,  289,  256,   40,   41,   42,   39,  289,
  256,  292,  264,  256,  257,   27,  273,  260,  261,  262,
  263,   56,   57,   52,  256,  257,  269,   39,  260,  261,
  262,  263,  289,   88,  121,  122,  256,  269,  256,   94,
  283,  256,   77,  289,  259,  256,  103,   82,  289,  292,
   85,  283,  199,  200,  258,  269,  286,   88,  256,  279,
  292,  279,  282,  118,  282,  273,  121,  122,  279,  273,
  290,  282,  290,  287,  288,  273,   88,  112,  113,  290,
  256,  289,  256,  230,  256,  289,  141,  118,  286,  269,
  121,  122,  270,  271,  270,  269,  183,  269,  256,  257,
  258,  259,  260,  261,  262,  263,  118,  287,  288,  121,
  122,  269,  256,  288,  258,  259,  274,  275,  276,  277,
  278,  279,  280,  281,  282,  283,  284,  285,  183,  256,
  288,  256,  290,  291,  292,  256,  269,  270,  271,  272,
  265,  269,  269,  270,  271,  272,  287,  256,  269,  282,
  283,  206,  183,  206,  287,  282,  287,  288,  289,  214,
  215,  270,  217,  218,  219,  220,  221,  222,  223,  224,
  225,  183,  256,  280,  281,  206,  256,  256,  262,  269,
  270,  271,  272,  214,  215,  242,  243,  244,  245,  269,
  269,  246,  282,  283,  206,  256,  287,  287,  288,  290,
  269,  262,  214,  215,  256,  217,  218,  219,  220,  221,
  222,  223,  224,  225,  289,  246,  262,  269,  270,  271,
  272,  259,  256,  257,  258,  259,  260,  261,  262,  263,
  282,  289,  290,  274,  246,  269,  270,  271,  272,  273,
  274,  275,  276,  277,  278,  279,  280,  281,  282,  283,
  284,  285,  286,  287,  288,  259,  290,  291,  292,  256,
  257,  258,  259,  260,  261,  262,  263,  287,  287,  269,
  290,  290,  269,  256,   83,   84,  259,  274,  275,  276,
  277,  278,  279,  280,  281,  282,  283,  284,  285,   86,
   87,  288,  267,  290,  291,  292,  256,  257,  258,  259,
  260,  261,  262,  263,  243,  244,  245,  264,  264,  269,
  269,  259,  270,  290,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  270,  270,  288,  259,
  290,  291,  292,  256,  257,  258,  259,  260,  261,  262,
  263,  288,  274,  269,  265,  291,  269,  279,  280,  281,
  282,  274,  275,  276,  277,  278,  279,  265,  290,  282,
  283,  284,  285,  265,  265,  288,  259,  290,  291,  292,
  256,  257,  258,  259,  260,  261,  262,  263,  270,  270,
  270,  270,  270,  269,  279,  290,  290,  282,  274,  275,
  276,  277,  278,  279,  290,  290,  282,  283,  284,  285,
  274,  189,  288,  290,  290,  291,  292,  256,  257,  258,
  259,  260,  261,  262,  263,  290,  289,  289,  288,  290,
  269,  279,  290,  290,  282,  274,  275,  276,  277,  278,
  279,  290,  290,  282,  283,  284,  285,   44,   94,  288,
  115,  290,  291,  292,  256,  257,  258,  259,  260,  261,
  262,  263,   47,   32,  167,  164,   -1,  269,   35,   -1,
   -1,   -1,  274,  275,  276,  277,  278,  279,   -1,   -1,
  282,  283,  284,  285,   -1,   -1,  288,   -1,  290,  291,
  292,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
   -1,   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,  274,
  275,  276,  277,  278,  279,   -1,   -1,  282,  283,  284,
  285,   -1,   -1,  288,   -1,  290,  291,  292,  256,  257,
  258,  259,  260,  261,  262,  263,   -1,   -1,   -1,   -1,
   -1,  269,   -1,  256,  257,  258,  259,  260,  261,  262,
  263,   -1,  256,   -1,   -1,  283,  269,   -1,   -1,   -1,
  288,  270,  271,  272,  292,  269,  270,  271,  272,   -1,
  283,   -1,   -1,  282,   -1,  288,   -1,   -1,  282,  292,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,   -1,
   -1,   -1,   -1,  269,   -1,  256,  257,  258,  259,  260,
  261,  262,  263,   -1,  256,   -1,   -1,  283,  269,   -1,
   -1,   -1,  288,   -1,   -1,   -1,  292,  269,  270,  271,
  272,   -1,  283,   -1,   -1,   -1,   -1,  288,   -1,   -1,
  282,  292,  256,  257,  258,  259,  260,  261,  262,  263,
   -1,   -1,   -1,   -1,   -1,  269,   -1,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  256,   -1,   -1,  283,
  269,   -1,   -1,   -1,  288,   -1,   -1,   -1,  292,  269,
  270,  271,  272,   -1,  283,   -1,   -1,   -1,   -1,  288,
   -1,   -1,  282,  292,  256,  257,  258,  259,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,  256,
  257,  258,  259,  260,  261,  262,  263,   -1,   -1,   -1,
   -1,  283,  269,   -1,  256,  257,  288,   -1,  260,  261,
  292,  263,   -1,   -1,   -1,   -1,  283,  269,   -1,  256,
  257,  288,   -1,  260,  261,  292,  263,   -1,   -1,  256,
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
   -1,  263,   -1,   -1,  283,   -1,   -1,  269,   -1,  256,
  257,   -1,  291,  260,  261,   -1,  263,   -1,   -1,  256,
   -1,  283,  269,   -1,   -1,  256,   -1,   -1,   -1,  291,
   -1,  268,  269,  270,  271,  272,  283,  268,  269,  270,
  271,  272,   -1,   -1,  291,  282,  256,  256,   -1,   -1,
   -1,  282,  289,   -1,   -1,   -1,   -1,   -1,  289,  269,
  270,  271,  272,   -1,   -1,   -1,  275,  276,  277,  278,
  279,  256,  282,  282,   -1,  284,  285,  256,   -1,  289,
   -1,   -1,   -1,   -1,  269,  270,  271,  272,   -1,   -1,
  269,  270,  271,  272,  256,   -1,   -1,  282,  256,   -1,
   -1,  256,   -1,  282,   -1,   -1,   -1,  269,  270,  271,
  272,  269,  270,  271,  272,  270,  271,  272,  256,   -1,
  282,   -1,   -1,   -1,  282,   -1,   -1,  282,  269,  270,
  271,  272,  270,  271,  272,  269,  270,  271,  272,   -1,
   -1,  282,  283,   -1,  282,   -1,  287,  288,  282,  283,
  274,   -1,   -1,  287,   -1,  279,  280,  281,  282,   -1,
   -1,   -1,  286,   -1,   -1,   -1,  290,  275,  276,  277,
  278,  269,  270,  271,  272,   -1,  284,  285,   -1,   -1,
   -1,  289,   -1,   -1,  282,  218,  219,  220,  221,  222,
  223,  224,  225,
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
"sentencia : INT ID decl_func",
"sentencia : INT error decl_func",
"sentencia_ejec : asign_simple",
"sentencia_ejec : asign_multiple",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion",
"sentencia_ejec : return_sent",
"decl_func : PUNTOYCOMA",
"decl_func : COMA lista_ids PUNTOYCOMA",
"decl_func : PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
"var_ref : ID",
"var_ref : var_ref PUNTO ID",
"lista_ids : var_ref",
"lista_ids : lista_ids COMA var_ref",
"lista_ids : lista_ids COMA error",
"lista_ids : lista_ids var_ref",
"asign_simple : var_ref ASIGN expresion",
"asign_simple : var_ref ASIGN error",
"asign_simple : error ASIGN expresion",
"asign_simple : var_ref error expresion",
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

//#line 330 "gramatica.y"

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
//#line 744 "Parser.java"
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
//#line 80 "gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 8:
//#line 86 "gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 10:
//#line 88 "gramatica.y"
{yyerror("Falta identificador despues de 'int'");}
break;
case 16:
//#line 96 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 18:
//#line 100 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable"); }
break;
case 19:
//#line 101 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable"); n_var = 0;}
break;
case 20:
//#line 102 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
break;
case 23:
//#line 112 "gramatica.y"
{n_var++;}
break;
case 24:
//#line 113 "gramatica.y"
{n_var++;}
break;
case 25:
//#line 114 "gramatica.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 26:
//#line 115 "gramatica.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 27:
//#line 121 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Asignacion"); }
break;
case 28:
//#line 122 "gramatica.y"
{ yyerror("Error: falta expresión después de ':=' en asignación."); }
break;
case 29:
//#line 123 "gramatica.y"
{ yyerror("Error: falta variable antes de ':=' en asignación."); }
break;
case 30:
//#line 124 "gramatica.y"
{ yyerror("Error: falta ':=' en asignación."); }
break;
case 31:
//#line 129 "gramatica.y"
{
					if (n_var < n_cte) {
						yyerror("Error: más constantes que variables en la asignación");
					} else {
						System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
						SINT.add(lex.getLineaActual(), "Asignacion multiple");
					}
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 32:
//#line 138 "gramatica.y"
{ yyerror("Error: falta lista de variables antes del '='"); }
break;
case 33:
//#line 139 "gramatica.y"
{ yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 34:
//#line 140 "gramatica.y"
{ yyerror("Error: falta lista de constantes después del '='");}
break;
case 35:
//#line 143 "gramatica.y"
{n_cte++;}
break;
case 36:
//#line 144 "gramatica.y"
{n_cte++;}
break;
case 37:
//#line 145 "gramatica.y"
{ yyerror("Error: falta una constante después de coma");}
break;
case 39:
//#line 151 "gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 40:
//#line 158 "gramatica.y"
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
case 41:
//#line 173 "gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 44:
//#line 187 "gramatica.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 46:
//#line 189 "gramatica.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 49:
//#line 194 "gramatica.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 51:
//#line 196 "gramatica.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 54:
//#line 201 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
break;
case 57:
//#line 210 "gramatica.y"
{ yyerror("Llamada a función sin nombre");}
break;
case 61:
//#line 219 "gramatica.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 63:
//#line 223 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Trunc"); }
break;
case 64:
//#line 224 "gramatica.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 65:
//#line 225 "gramatica.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 66:
//#line 226 "gramatica.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 68:
//#line 231 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Return"); }
break;
case 72:
//#line 241 "gramatica.y"
{ yyerror("Falta identificador después de 'int' en parámetro formal");}
break;
case 73:
//#line 242 "gramatica.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 76:
//#line 253 "gramatica.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 77:
//#line 255 "gramatica.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 78:
//#line 256 "gramatica.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 79:
//#line 257 "gramatica.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 80:
//#line 258 "gramatica.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 81:
//#line 259 "gramatica.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 82:
//#line 262 "gramatica.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 83:
//#line 263 "gramatica.y"
{ yyerror("Falta bloque del then."); }
break;
case 84:
//#line 264 "gramatica.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 86:
//#line 269 "gramatica.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 87:
//#line 270 "gramatica.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 88:
//#line 271 "gramatica.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 98:
//#line 288 "gramatica.y"
{ yyerror("Falta bloque del else."); }
break;
case 99:
//#line 293 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 100:
//#line 294 "gramatica.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 101:
//#line 295 "gramatica.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 102:
//#line 296 "gramatica.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 103:
//#line 297 "gramatica.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 104:
//#line 298 "gramatica.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 105:
//#line 299 "gramatica.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 106:
//#line 300 "gramatica.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 107:
//#line 301 "gramatica.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 108:
//#line 302 "gramatica.y"
{ yyerror("Falta bloque del for."); }
break;
case 111:
//#line 311 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 112:
//#line 312 "gramatica.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 113:
//#line 313 "gramatica.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 114:
//#line 314 "gramatica.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 115:
//#line 319 "gramatica.y"
{ SINT.add(lex.getLineaActual(), "Lambda"); }
break;
case 116:
//#line 320 "gramatica.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 117:
//#line 321 "gramatica.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 118:
//#line 322 "gramatica.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1211 "Parser.java"
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
