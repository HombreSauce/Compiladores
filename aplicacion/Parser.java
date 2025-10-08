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






//#line 4 ".\gramatica.y"
	package aplicacion;
	import aplicacion.AnalizadorLexico;
	import aplicacion.Token;
	import datos.TablaSimbolos;
	import datos.EntradaTablaSimbolos;
	import datos.TablaIdentificadorToken;
	import datos.TablaPalabraReservada;
	import java.io.*;
	import java.nio.charset.StandardCharsets;
	import java.nio.file.*;

//#line 29 "Parser.java"




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
    1,    0,    3,    3,    5,    5,    5,    4,    4,    4,
    4,    4,    6,    6,    6,    6,    6,    6,    6,    7,
    7,    8,   18,   18,   16,   16,   16,   16,    9,   10,
   10,   10,   10,   20,   20,   20,   21,   21,   21,   21,
   21,   19,   19,    2,    2,    2,    2,    2,   22,   22,
   22,   22,   22,   23,   23,   23,   14,   14,   24,   24,
   25,   25,   26,   26,   26,   26,   26,   26,   15,   17,
   17,   28,   28,   28,   29,   29,   11,   11,   11,   11,
   11,   11,   11,   11,   11,   30,   30,   30,   30,   33,
   33,   33,   33,   33,   33,   31,   31,   32,   32,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   34,
   34,   13,   13,   13,   13,   27,   27,   27,   27,   35,
   35,
};
final static short yylen[] = {                            2,
    2,    4,    0,    2,    0,    3,    2,    2,    1,    3,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    1,
    3,    6,    1,    3,    1,    3,    3,    2,    3,    3,
    2,    2,    3,    1,    3,    3,    1,    2,    1,    2,
    1,    1,    1,    3,    3,    3,    3,    1,    3,    3,
    3,    3,    1,    1,    1,    1,    4,    4,    1,    3,
    3,    3,    1,    4,    4,    4,    4,    1,    4,    1,
    3,    3,    3,    2,    0,    1,    6,    5,    6,    4,
    6,    7,    7,    7,    4,    3,    3,    3,    3,    1,
    1,    1,    1,    1,    1,    1,    3,    2,    2,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    2,
    3,    4,    4,    4,    4,   10,   10,   10,   10,    1,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    3,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    2,    4,    0,   13,   14,   15,   16,   17,
   18,   19,    0,    0,    0,    0,   39,   37,   41,    0,
    0,    0,   55,    0,   56,    0,   53,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   34,    8,   23,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   59,    0,   68,   92,   93,   94,   95,   91,   90,    5,
   96,    0,    0,   40,   38,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   12,    0,   20,   10,   11,    0,    0,    0,    0,    0,
   33,    0,   27,    0,    0,   43,    0,   29,   24,    0,
    0,    0,    0,   58,    0,    0,   85,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   49,    0,
   51,    0,   80,  114,  113,  115,  112,   69,   76,    0,
   70,    0,    0,    0,    0,    0,    0,    0,   57,   36,
   35,    0,    0,    0,    0,   60,   62,   61,   97,    0,
    0,    0,    0,   78,    0,    0,    0,   74,   21,    0,
    0,    0,    0,    0,   67,   66,   65,   64,    0,    6,
   79,    0,    0,   81,   77,    0,   71,    3,   73,   72,
    0,    0,    0,    0,    0,    0,    5,    5,    0,   98,
   84,   83,   82,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   22,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    5,    0,  108,
  101,  102,  103,  104,  105,  106,  107,    0,  100,    0,
    0,    0,    0,    0,  110,    0,    0,    0,  120,  121,
    0,    0,    0,  111,  119,  117,  118,  116,
};
final static short yydgoto[] = {                          2,
  106,   59,    4,   71,  116,  229,   94,   91,   16,   17,
   18,   19,   20,   21,   22,   23,  140,   24,  108,   47,
   35,   36,   37,   60,   61,   62,   63,  141,  142,   38,
   72,  183,   73,  230,  248,
};
final static short yysindex[] = {                      -266,
 -267,    0,    0, -219, -259, -142, -227, -217, -152, -204,
 -212,  356,    0,    0, -232,    0,    0,    0,    0,    0,
    0,    0,  818, -164,  689,  529,    0,    0,    0, -129,
  361,  -62,    0, -184,    0,  -16,    0,  518,  720,  724,
  720, -182,  -84, -119, -113,  689, -141,    0,    0,    0,
 -133, -112, -184, -141,  741, -117, -191, -100,  -89,  -69,
    0, -106,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -77,  720,    0,    0,  838, -239,  720,  745,  762,
  766,  770,  787,  622,  -75,  -83,   15, -254,  -38,  -33,
    0,  -22,    0,    0,    0,  -15,  -13, -153,  -45,  -91,
    0, -141,    0, -184,  716,    0,  -89,    0,    0,  720,
  720,   18,  689,    0, -108,  452,    0,  -89,  622,  632,
  -89, -259,  -16, -259,  -16, -259,  -89, -259,    0, -259,
    0,   33,    0,    0,    0,    0,    0,    0,    0,  -44,
    0, -256, -261, -184,   38,   42,   43, -235,    0,    0,
    0,  -89, -224, -211,   36,    0,    0,    0,    0,   39,
   70, -188,  -70,    0,  -33,   50,  -71,    0,    0,   78,
   79,   80,   82, -189,    0,    0,    0,    0, -251,    0,
    0,  647,   83,    0,    0,    7,    0,    0,    0,    0,
   93,   94,   96,  100,  109, -187,    0,    0, -259,    0,
    0,    0,    0,  422, -237,   58,   60,   90,   91,   92,
 -233,  462,  480,    0,  664,  664,  664,  664,  664,  664,
  664,  664,  679,   97,   98,   99,  112,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0, -259,    0,  695,
  795,  695,  795,  490,    0, -212,    0,   95,    0,    0,
  126,  128,  129,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -2,    0,    0,    0,  -30,    0,    0,    0,    0,    0,
    0,    0,    0,  837,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   40,    0,  152,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -86,    0,    0,    0,
    0,    0,  791,  337,    0,    0,    0,    0,  130,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -102,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  352,    0,  811,    0,    0,  389,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  565,    0,    0,
  575,  189,  226,  263,  300,  590,  607,   77,    0,  115,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -163,    0,    0,    0,    0,    0,    0,
    0,  404,    0,    0,    0,    0,    0,    0,    0,  501,
    0,    0,    0,    0, -102,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   35,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  441,    0,    0,
    0,    0,    0,    0,    0, -270,  143,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  114,  195,   -3, -123,   -4,    0,  359,    0,    0,
    0,    0,    0,   53,    0,  328,    0,    8,    0,   59,
   -5,  227,  228,  386,  325,    0,    0,  274,    0,  410,
  -23,  290,  423,  912,  -31,
};
final static int YYTABLESIZE=1135;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         15,
   14,  136,    1,   23,  197,  167,   48,   50,   23,   23,
   23,   23,  168,   34,   85,   23,  119,   48,  215,  120,
  173,   15,  222,    3,   79,   52,  169,   80,   39,   25,
   53,  175,   34,   15,  174,  137,    5,    6,   34,  198,
    7,    8,    9,   10,  177,   48,   34,   34,   34,   11,
  120,   44,  216,   34,   79,   49,  223,   80,   33,  104,
  132,   40,   34,   12,  110,  176,  195,   79,  210,  182,
   80,   41,   13,  212,  213,  196,   46,   33,  178,   15,
   34,   54,  211,   33,   45,   34,   34,   34,   34,   34,
   34,   33,   33,   33,  151,  161,  163,  111,   33,  144,
   25,   56,  147,   42,  244,   25,   90,   33,   55,  102,
  148,  160,   34,   26,   15,   15,   43,   34,   34,   32,
   34,   56,  101,   25,   25,   33,   11,   27,   28,   29,
   33,   33,   33,   33,   33,   33,   27,   28,   29,   30,
   74,   75,   97,  103,   32,  100,   31,  157,   30,   96,
   53,  109,   86,   88,   89,   98,   50,   33,  200,   75,
  158,  112,   33,   33,  150,   33,   75,  115,  107,   31,
   31,   31,   31,   31,   31,   31,   31,   15,   27,   28,
   29,  117,   31,  133,  189,  184,  118,  182,  185,   79,
   30,  121,   80,   78,  127,   79,   31,  190,   80,   15,
   14,   31,   92,   93,   90,   31,  134,  160,  160,  251,
  252,  253,   64,   65,   66,   67,   79,  113,  152,   80,
  114,   68,   69,  153,  154,    9,    9,    9,    9,    9,
    9,    9,    9,  139,  247,  250,  247,  250,    9,  160,
   79,  113,  165,   80,  149,  166,   50,   34,  145,   34,
  146,  138,    9,   23,   23,   23,   23,   23,   23,   23,
   23,    9,  202,   82,   83,  203,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,  155,   23,   23,   23,
   99,  164,   33,   99,   33,   54,   54,   54,   54,   54,
   54,   54,   54,   25,  135,  123,  125,  170,   54,  129,
  131,  171,  172,   54,   54,   54,   54,   54,   54,   54,
   54,   54,   54,   54,   54,  179,  180,   54,  181,   54,
   54,   54,   50,   50,   50,   50,   50,   50,   50,   50,
  188,  201,  191,  192,  193,   50,  194,  217,  245,  218,
   50,   50,   50,   50,   50,   50,   50,   50,   50,   50,
   50,   50,  205,  206,   50,  207,   50,   50,   50,  208,
   52,   52,   52,   52,   52,   52,   52,   52,  209,  219,
  220,  221,  204,   52,  255,  240,  241,  242,   52,   52,
   52,   52,   52,   52,   52,   52,   52,   52,   52,   52,
  243,   95,   52,   63,   52,   52,   52,   48,   48,   48,
   48,   48,   48,   48,   48,  256,   56,  257,  258,  143,
   48,   56,   56,   56,   56,   48,   48,   48,   48,   48,
   48,   99,  121,   48,   48,   48,   48,  156,  187,   48,
   77,   48,   48,   48,   45,   45,   45,   45,   45,   45,
   45,   45,  186,    0,   81,    0,    0,   45,    0,    0,
    0,    0,   45,   45,   45,   45,   45,   45,    0,    0,
   45,   45,   45,   45,    0,    0,   45,    0,   45,   45,
   45,   44,   44,   44,   44,   44,   44,   44,   44,    0,
    0,    0,    0,    0,   44,    0,    0,    0,    0,   44,
   44,   44,   44,   44,   44,    0,    0,   44,   44,   44,
   44,    0,    0,   44,    0,   44,   44,   44,   47,   47,
   47,   47,   47,   47,   47,   47,    0,    0,    0,    0,
    0,   47,    0,    0,    0,    0,   47,   47,   47,   47,
   47,   47,    0,    0,   47,   47,   47,   47,    0,    0,
   47,    0,   47,   47,   47,   46,   46,   46,   46,   46,
   46,   46,   46,    0,    0,    0,    0,    0,   46,    0,
    0,    0,    0,   46,   46,   46,   46,   46,   46,    0,
    0,   46,   46,   46,   46,    0,    0,   46,    0,   46,
   46,   46,   32,   32,   32,   32,   32,   32,   32,   32,
    0,    0,    0,    0,    0,   32,    0,   30,   30,   30,
   30,   30,   30,   30,   30,    0,   76,    0,    0,   32,
   30,    0,    0,    0,   32,   27,   28,   29,   32,   11,
   27,   28,   29,    0,   30,    0,    0,   30,    0,   30,
    0,    0,   30,   30,   42,   42,   42,   42,   42,   42,
   42,   42,    0,    0,    0,    0,    0,   42,    0,    1,
    1,    1,    1,    1,    1,    1,    1,    0,    0,    0,
    0,   42,    1,    0,    0,    0,   42,    5,    6,    0,
   42,    7,    8,    9,   10,    0,    1,    0,    0,    0,
   11,    1,    0,    0,    0,    1,  109,  109,  109,  109,
  109,  109,  109,  109,   12,    0,    0,    5,    6,  109,
    0,    7,    8,  214,   10,    0,    0,  224,    6,    0,
   11,    7,    8,  109,   10,    0,    0,    0,  109,    0,
   11,    0,  109,    0,   12,  226,    6,    0,    0,    7,
    8,    0,   10,  159,   12,    5,    6,    0,   11,    7,
    8,    0,   10,  225,    0,    0,    7,    7,   11,    0,
    7,    7,   12,    7,    0,    0,    0,    0,    0,    7,
    0,  227,   12,    5,    6,    0,    0,    7,    8,    9,
   10,  254,    0,    7,    5,    6,   11,    0,    7,    8,
    9,   10,    7,    0,    0,    0,    0,   11,    0,    0,
   12,    0,    0,   64,   65,   66,   67,   84,   70,    0,
    0,   12,   68,   69,    0,    0,    0,   25,    0,   70,
   88,   88,    0,    0,   88,   88,   88,   88,    0,    0,
   87,   87,    0,   88,   87,   87,   87,   87,    0,    0,
    0,    0,    0,   87,    0,   89,   89,   88,    0,   89,
   89,   89,   89,    0,   88,   88,    0,   87,   89,    0,
    0,    0,   86,   86,   87,   87,   86,   86,   86,   86,
    0,    0,   89,    0,    0,   86,    0,    5,    6,   89,
   89,    7,    8,    9,   10,    0,    0,  162,    6,   86,
   11,    7,    8,    9,   10,    0,   86,   86,    0,    0,
   11,    0,  199,    6,   12,    0,    7,    8,    9,   10,
    0,    0,   70,    0,   12,   11,    0,    0,    0,    5,
    6,    0,   70,    7,    8,    0,   10,    0,    0,   12,
    0,    0,   11,    0,  238,    6,    0,   70,    7,    8,
    0,   10,    0,    0,    5,    0,   12,   11,    0,    0,
    5,    0,    0,    0,  228,    0,   57,   11,   27,   28,
   29,   12,   57,  246,   27,   28,   29,    0,    0,  228,
   30,    5,    0,    0,    0,    5,   30,   58,    0,   87,
    0,    0,    0,   58,   11,   27,   28,   29,   11,   27,
   28,   29,   11,   27,   28,   29,  105,   30,    0,    0,
  122,   30,    0,    0,   25,   30,    0,    0,    0,   11,
   27,   28,   29,   11,   27,   28,   29,  124,    0,    0,
    0,  126,   30,    0,    0,  128,   30,    0,    0,    0,
   11,   27,   28,   29,   11,   27,   28,   29,   11,   27,
   28,   29,  130,   30,    0,    0,    0,   30,    0,    0,
    0,   30,    0,    0,    0,   11,   27,   28,   29,   28,
   28,   28,   28,  249,   27,   28,   29,    0,   30,    0,
    0,    0,   28,   28,    0,    0,   30,   28,   28,   26,
   26,   26,   26,    0,    0,    0,   50,   27,   28,   29,
    0,    0,   26,   26,    0,    0,    0,   26,   26,   30,
   51,    0,    0,    0,   52,   25,   25,   25,   25,    0,
    0,    0,   64,   65,   66,   67,    0,    0,   25,   25,
    0,   68,   69,   25,    0,    0,   25,  231,  232,  233,
  234,  235,  236,  237,  239,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    4,  256,  269,  274,  256,  262,   12,  269,  279,  280,
  281,  282,  269,    6,   38,  286,  256,   23,  256,  290,
  256,   26,  256,  291,  279,  287,  288,  282,  256,  289,
   23,  256,   25,   38,  270,  290,  256,  257,   31,  291,
  260,  261,  262,  263,  256,   51,   39,   40,   41,  269,
  290,  256,  290,   46,  279,  288,  290,  282,    6,   52,
   84,  289,   55,  283,  256,  290,  256,  279,  256,  258,
  282,  289,  292,  197,  198,  265,  289,   25,  290,   84,
   73,   23,  270,   31,  289,   78,   79,   80,   81,   82,
   83,   39,   40,   41,  100,  119,  120,  289,   46,   92,
  289,  286,  256,  256,  228,  269,  289,   55,  273,   51,
  264,  116,  105,  256,  119,  120,  269,  110,  111,    6,
  113,  286,  256,  287,  288,   73,  269,  270,  271,  272,
   78,   79,   80,   81,   82,   83,  270,  271,  272,  282,
  270,  271,  256,  256,   31,  287,  289,  256,  282,  269,
  143,  269,   39,   40,   41,  269,  269,  105,  182,  262,
  269,  262,  110,  111,  256,  113,  269,  274,   55,  256,
  257,  258,  259,  260,  261,  262,  263,  182,  270,  271,
  272,  259,  269,  259,  256,  256,   73,  258,  259,  279,
  282,   78,  282,  256,   81,  279,  283,  269,  282,  204,
  204,  288,  287,  288,  289,  292,  290,  212,  213,  241,
  242,  243,  275,  276,  277,  278,  279,  287,  105,  282,
  290,  284,  285,  110,  111,  256,  257,  258,  259,  260,
  261,  262,  263,  267,  240,  241,  242,  243,  269,  244,
  279,  287,  287,  282,  290,  290,  269,  240,  264,  242,
  264,  290,  283,  256,  257,  258,  259,  260,  261,  262,
  263,  292,  256,  280,  281,  259,  269,  270,  271,  272,
  273,  274,  275,  276,  277,  278,  279,  280,  281,  282,
  283,  284,  285,  286,  287,  288,  269,  290,  291,  292,
  256,  259,  240,  259,  242,  256,  257,  258,  259,  260,
  261,  262,  263,  289,  290,   79,   80,  270,  269,   82,
   83,  270,  270,  274,  275,  276,  277,  278,  279,  280,
  281,  282,  283,  284,  285,  290,  288,  288,  259,  290,
  291,  292,  256,  257,  258,  259,  260,  261,  262,  263,
  291,  259,  265,  265,  265,  269,  265,  290,  288,  290,
  274,  275,  276,  277,  278,  279,  280,  281,  282,  283,
  284,  285,  270,  270,  288,  270,  290,  291,  292,  270,
  256,  257,  258,  259,  260,  261,  262,  263,  270,  290,
  290,  290,  188,  269,  290,  289,  289,  289,  274,  275,
  276,  277,  278,  279,  280,  281,  282,  283,  284,  285,
  289,   43,  288,  274,  290,  291,  292,  256,  257,  258,
  259,  260,  261,  262,  263,  290,  274,  290,  290,   92,
  269,  279,  280,  281,  282,  274,  275,  276,  277,  278,
  279,   46,  290,  282,  283,  284,  285,  113,  165,  288,
   31,  290,  291,  292,  256,  257,  258,  259,  260,  261,
  262,  263,  163,   -1,   32,   -1,   -1,  269,   -1,   -1,
   -1,   -1,  274,  275,  276,  277,  278,  279,   -1,   -1,
  282,  283,  284,  285,   -1,   -1,  288,   -1,  290,  291,
  292,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
   -1,   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,  274,
  275,  276,  277,  278,  279,   -1,   -1,  282,  283,  284,
  285,   -1,   -1,  288,   -1,  290,  291,  292,  256,  257,
  258,  259,  260,  261,  262,  263,   -1,   -1,   -1,   -1,
   -1,  269,   -1,   -1,   -1,   -1,  274,  275,  276,  277,
  278,  279,   -1,   -1,  282,  283,  284,  285,   -1,   -1,
  288,   -1,  290,  291,  292,  256,  257,  258,  259,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,
   -1,   -1,   -1,  274,  275,  276,  277,  278,  279,   -1,
   -1,  282,  283,  284,  285,   -1,   -1,  288,   -1,  290,
  291,  292,  256,  257,  258,  259,  260,  261,  262,  263,
   -1,   -1,   -1,   -1,   -1,  269,   -1,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  256,   -1,   -1,  283,
  269,   -1,   -1,   -1,  288,  270,  271,  272,  292,  269,
  270,  271,  272,   -1,  283,   -1,   -1,  282,   -1,  288,
   -1,   -1,  282,  292,  256,  257,  258,  259,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,  256,
  257,  258,  259,  260,  261,  262,  263,   -1,   -1,   -1,
   -1,  283,  269,   -1,   -1,   -1,  288,  256,  257,   -1,
  292,  260,  261,  262,  263,   -1,  283,   -1,   -1,   -1,
  269,  288,   -1,   -1,   -1,  292,  256,  257,  258,  259,
  260,  261,  262,  263,  283,   -1,   -1,  256,  257,  269,
   -1,  260,  261,  292,  263,   -1,   -1,  256,  257,   -1,
  269,  260,  261,  283,  263,   -1,   -1,   -1,  288,   -1,
  269,   -1,  292,   -1,  283,  256,  257,   -1,   -1,  260,
  261,   -1,  263,  292,  283,  256,  257,   -1,  269,  260,
  261,   -1,  263,  292,   -1,   -1,  256,  257,  269,   -1,
  260,  261,  283,  263,   -1,   -1,   -1,   -1,   -1,  269,
   -1,  292,  283,  256,  257,   -1,   -1,  260,  261,  262,
  263,  292,   -1,  283,  256,  257,  269,   -1,  260,  261,
  262,  263,  292,   -1,   -1,   -1,   -1,  269,   -1,   -1,
  283,   -1,   -1,  275,  276,  277,  278,  290,  291,   -1,
   -1,  283,  284,  285,   -1,   -1,   -1,  289,   -1,  291,
  256,  257,   -1,   -1,  260,  261,  262,  263,   -1,   -1,
  256,  257,   -1,  269,  260,  261,  262,  263,   -1,   -1,
   -1,   -1,   -1,  269,   -1,  256,  257,  283,   -1,  260,
  261,  262,  263,   -1,  290,  291,   -1,  283,  269,   -1,
   -1,   -1,  256,  257,  290,  291,  260,  261,  262,  263,
   -1,   -1,  283,   -1,   -1,  269,   -1,  256,  257,  290,
  291,  260,  261,  262,  263,   -1,   -1,  256,  257,  283,
  269,  260,  261,  262,  263,   -1,  290,  291,   -1,   -1,
  269,   -1,  256,  257,  283,   -1,  260,  261,  262,  263,
   -1,   -1,  291,   -1,  283,  269,   -1,   -1,   -1,  256,
  257,   -1,  291,  260,  261,   -1,  263,   -1,   -1,  283,
   -1,   -1,  269,   -1,  256,  257,   -1,  291,  260,  261,
   -1,  263,   -1,   -1,  256,   -1,  283,  269,   -1,   -1,
  256,   -1,   -1,   -1,  291,   -1,  268,  269,  270,  271,
  272,  283,  268,  269,  270,  271,  272,   -1,   -1,  291,
  282,  256,   -1,   -1,   -1,  256,  282,  289,   -1,  256,
   -1,   -1,   -1,  289,  269,  270,  271,  272,  269,  270,
  271,  272,  269,  270,  271,  272,  256,  282,   -1,   -1,
  256,  282,   -1,   -1,  289,  282,   -1,   -1,   -1,  269,
  270,  271,  272,  269,  270,  271,  272,  256,   -1,   -1,
   -1,  256,  282,   -1,   -1,  256,  282,   -1,   -1,   -1,
  269,  270,  271,  272,  269,  270,  271,  272,  269,  270,
  271,  272,  256,  282,   -1,   -1,   -1,  282,   -1,   -1,
   -1,  282,   -1,   -1,   -1,  269,  270,  271,  272,  269,
  270,  271,  272,  269,  270,  271,  272,   -1,  282,   -1,
   -1,   -1,  282,  283,   -1,   -1,  282,  287,  288,  269,
  270,  271,  272,   -1,   -1,   -1,  269,  270,  271,  272,
   -1,   -1,  282,  283,   -1,   -1,   -1,  287,  288,  282,
  283,   -1,   -1,   -1,  287,  269,  270,  271,  272,   -1,
   -1,   -1,  275,  276,  277,  278,   -1,   -1,  282,  283,
   -1,  284,  285,  287,   -1,   -1,  289,  216,  217,  218,
  219,  220,  221,  222,  223,
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
"expresion_error : error expresion",
"prog : ID LLAVEINIC bloque LLAVEFIN",
"bloque :",
"bloque : bloque sentencia",
"bloque_ejec :",
"bloque_ejec : bloque_ejec sentencia_ejec PUNTOYCOMA",
"bloque_ejec : bloque_ejec sentencia_ejec",
"sentencia : sentencia_ejec PUNTOYCOMA",
"sentencia : sentencia_ejec",
"sentencia : INT ID decl_var",
"sentencia : INT ID decl_func",
"sentencia : INT error decl_func",
"sentencia_ejec : asign_simple",
"sentencia_ejec : asign_multiple",
"sentencia_ejec : bloque_if",
"sentencia_ejec : bloque_for",
"sentencia_ejec : print_sent",
"sentencia_ejec : llamada_funcion",
"sentencia_ejec : return_sent",
"decl_var : PUNTOYCOMA",
"decl_var : COMA lista_ids PUNTOYCOMA",
"decl_func : PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN",
"var_ref : ID",
"var_ref : var_ref PUNTO ID",
"lista_ids : var_ref",
"lista_ids : lista_ids COMA var_ref",
"lista_ids : lista_ids COMA error",
"lista_ids : lista_ids var_ref",
"asign_simple : var_ref ASIGN expresion_posible",
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
"expresion_posible : expresion",
"expresion_posible : expresion_error",
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
"param_formal : sem_pasaje_opt ID",
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

//#line 327 ".\gramatica.y"

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;
static TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
static int n_var = 0; //para contar variables en asignaciones multiples
static int n_cte = 0; //para contar ctes en asignaciones multiples

// writers de salida
static BufferedWriter wTokens = null;   // para los tokens
static BufferedWriter wTabla  = null;   // para la tabla de símbolos
static BufferedWriter wSint = null;     // para las estructuras sintacticas

// helpers de ruta
static Path rutaLexico(Path fuente, boolean acentos) {
    String base = quitarExt(fuente.getFileName().toString());
    String suf  = acentos ? "-léxico" : "-lexico";
    return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
            .resolve(base + suf + ".txt");
}
static Path rutaTabla(Path fuente, boolean acentos) {
    String base = quitarExt(fuente.getFileName().toString());
    String suf  = acentos ? "-tabla-de-símbolos" : "-tabla-simbolos";
    return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
            .resolve(base + suf + ".txt");
}
static Path rutaSintactico(Path fuente, boolean acentos) {
    String base = quitarExt(fuente.getFileName().toString());
    String suf  = acentos ? "-sintáctico" : "-sintactico";
    return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
            .resolve(base + suf + ".txt");
}
static String quitarExt(String s) {
    int i = s.lastIndexOf('.');
    return (i > 0) ? s.substring(0, i) : s;
}



//para el archivo de tokens
static void escribirHeaderTokens(BufferedWriter w) throws IOException {
    w.write(encabezado("TOKENS DETECTADOS")); w.newLine();
    w.write(String.format("%-6s | %-18s | %-28s | %-4s", "Linea", "Token", "Lexema", "ID")); w.newLine();
    w.write("------ | ------------------ | ---------------------------- | ----"); w.newLine();
}
static String filaToken(Token t, int linea) {
    String[] tl = tipoYLexema(t);
    String tipo   = humanizeTipo(tl[0]);                              
    String lexema = tl[1].replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
    lexema = trunc(lexema, 28);                                       // evita que desborde la columna
    return String.format("%6d | %-18s | %-28s | %4d", linea, tipo, lexema, t.getIDToken());
}
// Mapea los IDs a (tipo, lexema) usando las tablas
static String[] tipoYLexema(Token t) {
    TablaIdentificadorToken tid = TablaIdentificadorToken.getInstancia();
    TablaPalabraReservada  tpr  = TablaPalabraReservada.getInstancia();
    int id = t.getIDToken();

    if (id < 269) {                            // Palabra reservada
        String kw = tpr.getClave(id);          // ej: IF
        return new String[]{ kw, kw.toLowerCase() }; // tipo=IF, lexema="if"
    } else if (id == 269) {
        return new String[]{ "ID",           t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else if (id == 270) {
        return new String[]{ "CONST_INT",    t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else if (id == 271) {
        return new String[]{ "CONST_FLOAT",  t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else if (id == 272) {
        return new String[]{ "CONST_STRING", t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
    } else {
        String raw = tid.getClave(id); // puede ser "(", "ASIGNACION", ";", etc.
        String lex = (t.getEntradaTS()!=null) ? t.getEntradaTS().getLexema() : raw;
        return new String[]{ raw, lex };
    }
}
// Cambia simbolos sueltos para que la columna Token se vea prolija
static String humanizeTipo(String raw) {
    return switch (raw) {
        case "if" -> "IF";
        case "else" -> "ELSE";
        case "endif" -> "ENDIF";
        case "print" -> "PRINT";
        case "return" -> "RETURN";
        case "int" -> "INT";
        case "for" -> "FOR";
        case "from" -> "FROM";
        case "to" -> "TO";
        case "lambda" -> "LAMBDA";
        case "cv" -> "CV";
        case "trunc" -> "TRUNC";
        case ":=" -> "ASIGN";
        case "->" -> "FLECHA";
        case "==" -> "IGUAL";
        case "=!" -> "DISTINTO";
        case "<=" -> "MENORIGUAL";
        case "=>" -> "MAYORIGUAL";
        case "+" -> "MAS";
        case "*" -> "MUL";
        case "/" -> "DIV";
        case "-" -> "MENOS";
        case "=" -> "IGUALUNICO";
        case ">" -> "MAYOR";
        case "<" -> "MENOR";
        case "." -> "PUNTO";
        case "(" -> "PARENTINIC";
        case ")" -> "PARENTFIN";
        case "{" -> "LLAVEINIC";
        case "}" -> "LLAVEFIN";
        case ";" -> "PUNTOYCOMA";
        case "," -> "COMA";
        default -> raw;
    };
}
// Recorta con puntos si el lexema es muy largo
static String trunc(String s, int max) {
    if (s == null) return "";
    if (s.length() <= max) return s;
    if (max <= 1) return s.substring(0, max);
    return s.substring(0, max-1) + "…";
}


public static void main (String [] args) {
    try {
        System.out.println("Iniciando compilacion...");
        TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();

        Path fuente = Paths.get(args[0]);

        // abrir archivos de salida
        wTokens = Files.newBufferedWriter(
                rutaLexico(fuente, false), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        escribirHeaderTokens(wTokens);

        // (opcional) abrimos el de tabla ahora o despues — aca lo abrimos ahora
        wTabla = Files.newBufferedWriter(
                rutaTabla(fuente, false), StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

        // correr analisis
        lex = new AnalizadorLexico(args[0]);
        par = new Parser(false);
        par.run();

        // volcar TABLA DE SIMBOLOS a archivo
        wTabla.write(encabezado("TABLA DE SIMBOLOS"));
        wTabla.newLine();
        PrintWriter pw = new PrintWriter(wTabla, true);
        tablaSimbolos.mostrarTabla(pw); 

        System.out.println("Fin compilacion");
        
    } catch (IOException e) {
        throw new RuntimeException(e);
    } finally {
        try { if (wTokens != null) { wTokens.flush(); wTokens.close(); } } catch (IOException ignored) {}
        try { if (wTabla  != null) { wTabla.flush();  wTabla.close();  } } catch (IOException ignored) {}
    }
}

static String encabezado(String titulo) {
    String barra = "=".repeat(Math.max(24, titulo.length() + 8));
    return barra + "\n" + "=== " + titulo + " ===\n" + barra;
}


int yylex (){
    Token token = null;
    if ((token = lex.getToken()) != null) {
        yylval = new ParserVal(token.getEntradaTS());

        // --- escribir al archivo de tokens ---
        if (wTokens != null) {
            try {
				int lineaTok = lex.getLineaActual(); 
                wTokens.write(filaToken(token, lineaTok)); 
                wTokens.newLine();
            } catch (IOException e) {
                throw new RuntimeException("Error escribiendo token", e);
            }
        }
        return token.getIDToken();
    } else {
        return 0; // no hay más tokens
    }
}

void yyerror(String mensaje){
    if ("syntax error".equals(mensaje)) return;  // suprime el genérico
    System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
}
//#line 846 "Parser.java"
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
case 7:
//#line 74 ".\gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 9:
//#line 80 ".\gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 12:
//#line 83 ".\gramatica.y"
{yyerror("Falta identificador de función despues de 'int'");}
break;
case 21:
//#line 97 ".\gramatica.y"
{n_var = 0;}
break;
case 25:
//#line 110 ".\gramatica.y"
{n_var++;}
break;
case 26:
//#line 111 ".\gramatica.y"
{n_var++;}
break;
case 27:
//#line 112 ".\gramatica.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 28:
//#line 113 ".\gramatica.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 29:
//#line 119 ".\gramatica.y"
{System.out.println("Asignación válida");}
break;
case 30:
//#line 124 ".\gramatica.y"
{
                    if (n_var == 1 && n_cte == 1) {
                        yyerror("Error: para asignación simple use ':=' en lugar de '='");
                    } else {
                        if (n_var < n_cte) {
                            yyerror("Error: más constantes que variables en la asignación");
                        } else {
                            System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
                        }
                    }
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 31:
//#line 136 ".\gramatica.y"
{ yyerror("Error: falta lista de variables antes del '='"); }
break;
case 32:
//#line 137 ".\gramatica.y"
{ yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 33:
//#line 138 ".\gramatica.y"
{ yyerror("Error: falta lista de constantes después del '='");}
break;
case 34:
//#line 141 ".\gramatica.y"
{n_cte++;}
break;
case 35:
//#line 142 ".\gramatica.y"
{n_cte++;}
break;
case 36:
//#line 143 ".\gramatica.y"
{ yyerror("Error: falta una constante después de coma");}
break;
case 38:
//#line 149 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 39:
//#line 156 ".\gramatica.y"
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
case 40:
//#line 170 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 43:
//#line 184 ".\gramatica.y"
{yyerror("Error en la expresión, falta un operador entre los operandos.");}
break;
case 45:
//#line 188 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 47:
//#line 190 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 50:
//#line 197 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 52:
//#line 199 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 58:
//#line 213 ".\gramatica.y"
{ yyerror("Llamada a función sin nombre");}
break;
case 62:
//#line 222 ".\gramatica.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 65:
//#line 227 ".\gramatica.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 66:
//#line 228 ".\gramatica.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 67:
//#line 229 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 73:
//#line 244 ".\gramatica.y"
{ yyerror("Falta identificador después de 'int' en parámetro formal");}
break;
case 74:
//#line 245 ".\gramatica.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 78:
//#line 255 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 79:
//#line 256 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 80:
//#line 257 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 81:
//#line 258 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 83:
//#line 260 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 84:
//#line 261 ".\gramatica.y"
{ yyerror("Falta bloque del then."); }
break;
case 85:
//#line 262 ".\gramatica.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 87:
//#line 267 ".\gramatica.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 88:
//#line 268 ".\gramatica.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 89:
//#line 269 ".\gramatica.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 99:
//#line 285 ".\gramatica.y"
{ yyerror("Falta bloque del else."); }
break;
case 101:
//#line 291 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 102:
//#line 292 ".\gramatica.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 103:
//#line 293 ".\gramatica.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 104:
//#line 294 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 105:
//#line 295 ".\gramatica.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 106:
//#line 296 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 107:
//#line 297 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 108:
//#line 298 ".\gramatica.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 109:
//#line 299 ".\gramatica.y"
{ yyerror("Falta bloque del for."); }
break;
case 113:
//#line 309 ".\gramatica.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 114:
//#line 310 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 115:
//#line 311 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 117:
//#line 317 ".\gramatica.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 118:
//#line 318 ".\gramatica.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 119:
//#line 319 ".\gramatica.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1260 "Parser.java"
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
