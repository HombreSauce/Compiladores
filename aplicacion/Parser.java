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
    2,    2,    2,    4,    4,    4,    4,    4,    6,    6,
   14,   14,   14,    7,    7,    7,    7,    7,    8,    8,
   17,   16,   16,   16,   18,   18,   18,   19,   19,    9,
   10,   10,   10,   22,   22,   22,   22,   21,   21,   21,
   21,   20,   20,   15,   15,   15,   15,   15,   15,   15,
   24,   24,   24,   24,   24,   24,   24,   24,   25,   25,
   23,   23,   23,   23,   23,   11,   11,   11,   29,   29,
   29,   29,   29,   29,   29,   29,   29,   29,   26,   26,
   26,   30,   30,   30,   30,   30,   30,   27,   27,   27,
   27,   28,   28,   28,   28,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   31,   31,   31,   13,   13,
   13,   13,
};
final static short yylen[] = {                            2,
    4,    0,    2,    3,    2,    1,    2,    1,    1,    1,
    2,    1,    1,    1,    1,    1,    1,    1,    3,    2,
    1,    3,    3,    5,    4,    5,    5,    5,    8,    1,
    7,    1,    3,    3,    3,    3,    2,    0,    1,    3,
    3,    2,    3,    1,    3,    3,    2,    1,    3,    3,
    2,    1,    3,    1,    3,    3,    3,    3,    3,    3,
    1,    3,    3,    3,    3,    3,    3,    3,    1,    1,
    1,    2,    1,    2,    1,    6,    8,    1,    5,    5,
    4,    7,    7,    6,    3,    5,    6,    8,    3,    3,
    3,    1,    1,    1,    1,    1,    1,    1,    3,    2,
    0,    1,    3,    2,    0,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    1,    3,    2,    4,    4,
    4,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    8,    0,   52,    0,
    1,    3,    0,    0,    9,   10,   13,   14,   15,   16,
   17,   18,   30,    0,    0,   78,    0,   73,   71,   75,
    0,    0,    0,   98,    0,    0,   70,    0,   61,    0,
    0,    0,    0,    0,    0,    0,   44,   11,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   94,   95,   96,
   97,    0,    0,    0,    0,   93,   92,    0,   74,   72,
    0,    0,    0,  100,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   85,    0,    0,    0,    0,
    0,    0,    0,    0,   47,    0,    0,    0,    0,   39,
    0,   32,    0,    0,   19,    0,   53,   43,    0,   50,
    0,    0,    0,   64,   67,    0,    0,    0,    0,    0,
   99,    0,    5,    0,    0,    0,    0,    0,    0,   68,
   63,   62,   66,   65,    0,    0,   81,    0,  102,    0,
  121,  120,  119,    0,    0,    0,    0,   46,   45,    0,
    0,    0,    0,    0,    0,    0,   37,    0,   23,   22,
    0,    0,    0,   80,    4,    0,   79,    0,  104,    0,
   86,    0,    0,    0,    0,    0,   28,   27,   26,   24,
    0,   34,   33,    2,   36,   35,    0,   87,    0,   76,
    0,    0,   84,  103,    0,    0,    0,    0,    0,    0,
    2,    0,    0,    0,   83,   82,    0,    0,    0,    0,
    0,    0,    0,    0,   31,   88,   77,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   29,    0,  116,  114,
  107,  108,  109,  110,  111,  112,  113,  115,  106,  118,
    0,  117,
};
final static short yydgoto[] = {                          2,
    4,   12,   75,  229,   14,   15,   16,   17,   18,   19,
   20,   21,   22,   52,   35,  101,   23,  102,  103,   24,
   25,   46,   37,   38,   39,   40,   41,  140,   26,   68,
  230,
};
final static short yysindex[] = {                      -256,
 -236,    0,    0, -136,  174, -230,    0, -229,    0,  254,
    0,    0,  -80, -252,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -268, -253,    0,  610,    0,    0,    0,
 -167,  -65,  310,    0,   54, -268,    0, -248,    0,  424,
 -128,  394,  532,  -58, -208,  355,    0,    0,  -50, -233,
  -41, -133,  394,  -15, -107, -205,  -37,    0,    0,    0,
    0,  536,  169,  169,  536,    0,    0,  394,    0,    0,
  594,  -37,  453,    0,  315,  -31,  540,  557,  561,  169,
  565,  582,  468,  -77,  472,    0,  438,  -70,  -16,   11,
    9,   16,  -64,  221,    0,  394,  394,  586,  -41,    0,
 -280,    0, -192, -159,    0, -180,    0,    0,  355,    0,
  -37,  -59, -248,    0,    0, -248, -180,  468,  468,  -30,
    0,   -9,    0,  -59, -248,  -59, -248,  438, -180,    0,
    0,    0,    0,    0,   36,  472,    0,  331,    0,   23,
    0,    0,    0,   32,   40,   48, -161,    0,    0,   21,
   83,  -21,   87, -162, -110,    8,    0,  -72,    0,    0,
   38,   57,  472,    0,    0,  472,    0,   67,    0,  346,
    0,   70,   72,   80,   81,  -20,    0,    0,    0,    0,
   63,    0,    0,    0,    0,    0,  472,    0,  472,    0,
  108,  109,    0,    0,  102,  103,  104,  111,  112, -103,
    0,  141,  128,  129,    0,    0, -247,  100,  115,  117,
  118,  119, -244,  260,    0,    0,    0,  485,  485,  485,
  485,  485,  485,  485,  485,  278,    0,  348,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  353,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,   93,    0,    0,    0,    0,    0,
    0,    0,  282,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -207,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  502,    0,  -89,    0,   93,
    0,    0,    0,    0,    0, -238,    0,    0,    0,  245,
 -164,  293,    0,    0,    0,    0, -187,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -140,   93,    0,    0,  361,    0,    0,    0,    0,
    0,    0,   93,    0,  143,    0,    0,    0,    0,  190,
    0,    0,    0,    0,    0,    0,    0,    0, -164,    0,
    0,    0,    0,    0,    0,  212,    0,    0,  227,    0,
 -111,    0,  -44,    0,    0,   -7,  399,   93,   93,    0,
    0,  386,    0,   29,   65,  101,  137,  414,  443,    0,
    0,    0,    0,    0,    0,  143,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  297,    0, -164,    0,    0,    0,    0,    0,
    0,    0,  143,    0,    0,  143,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  143,    0,  143,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
 -173,    0, -135,   -4,  308,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   15,  318,    0,  263,    0,   10,
    0,  368,   -8,  165,   97,  400,  -34, -122,    0,  391,
  677,
};
final static int YYTABLESIZE=903;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         13,
   34,   47,  170,   49,   53,   84,  155,   80,  218,  156,
  202,  225,    1,  168,   36,    9,   50,   54,   42,   42,
   42,   42,   97,   42,   42,   42,   44,  214,   76,   55,
   42,   81,   82,   56,   57,   34,   51,   95,  120,   98,
  191,   72,  219,  192,   42,  226,   47,   92,  135,   42,
  110,   72,   72,   42,    3,   99,   88,   90,   43,   45,
   93,   48,   72,    9,  203,  111,  204,  106,   34,    7,
  122,   72,   72,   72,   72,   48,  157,   72,   34,   48,
  139,   51,  117,  161,  162,  149,   72,   72,   72,   72,
   72,   72,  241,  129,  175,   51,  159,   38,   77,   51,
   95,   78,   69,   70,   38,   72,   72,   72,  176,  160,
  150,  151,  153,   34,   34,   69,   69,   69,   69,   69,
    5,   69,   69,    6,  155,    7,    8,  181,   69,   85,
   86,  139,    9,   76,   69,   69,   69,   69,   69,   69,
   69,   69,   69,   69,   69,  182,   10,   69,  108,   69,
   69,   69,  212,  104,  105,   11,  100,   49,  139,  114,
  115,  139,   28,   29,   30,  122,  213,   54,   54,   54,
   54,   49,   54,   54,   31,   49,  130,  132,  134,   54,
  136,  137,  139,  185,  139,   54,   54,   54,   54,   54,
   71,  146,   54,   54,   54,   54,  186,   13,   54,  147,
   54,   54,   54,    9,   28,   29,   30,   48,   77,   13,
   91,   78,   57,   57,   57,   57,   31,   57,   57,  141,
   63,   64,   96,   76,   57,  100,  113,  163,  164,  116,
   57,   57,   57,   57,   57,  199,  122,   57,   57,   57,
   57,  125,  127,   57,  200,   57,   57,   57,   54,   60,
   60,   60,   60,  107,   60,   60,  123,   62,   63,   64,
   65,   60,   62,   63,   64,   65,  179,   60,   60,   60,
   60,   60,  144,  142,   60,   60,   60,   60,  165,  145,
   60,  171,   60,   60,   60,   56,   56,   56,   56,   77,
   56,   56,   78,  166,  167,  187,  188,   56,  184,   77,
  143,  172,   78,   56,   56,   56,   56,   56,  177,  173,
   56,   56,   56,   56,  189,  190,   56,  174,   56,   56,
   56,   55,   55,   55,   55,  193,   55,   55,   58,   59,
   60,   61,   77,   55,  195,   78,  196,   66,   67,   55,
   55,   55,   55,   55,  197,  198,   55,   55,   55,   55,
  101,  101,   55,  201,   55,   55,   55,   59,   59,   59,
   59,   77,   59,   59,   78,   77,  205,  206,   78,   59,
  178,  207,  208,  209,  180,   59,   59,   59,   59,   59,
  210,  211,   59,   59,   59,   59,  216,  217,   59,  220,
   59,   59,   59,   58,   58,   58,   58,    5,   58,   58,
    6,  105,    7,    8,  221,   58,  222,  223,  224,    9,
  158,   58,   58,   58,   58,   58,  154,  183,   58,   58,
   58,   58,  109,   10,   58,   79,   58,   58,   58,   27,
    5,   73,  215,    6,    0,    0,    8,    9,   28,   29,
   30,    0,    9,   28,   29,   30,  122,  122,  122,  122,
   31,  122,  122,    0,    0,   31,   10,    0,  122,    0,
    0,    0,   32,    0,   33,    0,    0,    0,   40,   40,
   40,   40,  122,   40,   40,    0,  148,  122,    0,    0,
   40,  122,    0,   41,   41,   41,   41,    0,   41,   41,
   28,   29,   30,    0,   40,   41,    0,    0,    0,   40,
    0,   21,   31,   40,   21,    0,   21,   21,    0,   41,
    0,    0,    0,   21,   41,    0,    5,    0,   41,    6,
    0,    7,    8,   28,   29,   30,    0,   21,    9,    0,
    0,   21,   21,  238,    5,   31,   21,    6,   12,    0,
    8,   12,   10,   12,   12,    0,    9,    0,    0,   20,
   12,  227,   20,   25,   20,   20,   25,    0,   25,   25,
   10,   20,    0,    0,   12,   25,    5,    0,  228,    6,
    0,    5,    8,   12,    6,   20,    0,    8,    9,   25,
    0,    0,    0,    9,   20,    0,    0,    5,   25,    0,
    6,    0,   10,    8,    0,    0,    0,   10,    0,    9,
    0,   74,    5,    0,    5,    6,  121,    6,    8,    5,
    8,    0,    6,   10,    9,    8,    9,    6,    0,    0,
    6,    9,  169,    6,   28,   29,   30,    0,   10,    6,
   10,    0,    0,    0,    0,   10,   31,  194,    0,  240,
    0,   94,    7,    6,  242,    7,    0,    0,    7,   87,
    0,    0,    6,    0,    7,   90,   90,   90,   90,    0,
    0,   90,    9,   28,   29,   30,    0,   90,    7,    0,
   91,   91,   91,   91,    0,   31,   91,    7,    0,    0,
    5,   90,   91,    6,    0,    0,    8,    0,   90,   90,
    0,    0,    9,    0,    0,    0,   91,    0,    0,   89,
   89,   89,   89,   91,   91,   89,   10,    0,    0,    5,
    0,   89,    6,   83,   33,    8,   62,   63,   64,   65,
    0,    9,    0,    0,    5,   89,    0,    6,    5,    0,
    8,    6,   89,   89,    8,   10,    9,    0,    0,    0,
    9,    5,  119,   33,    6,    0,    0,    8,    0,    0,
   10,    0,    0,    9,   10,    0,    0,   69,   33,    0,
    0,    0,  138,    0,    0,    0,    0,   10,    0,    0,
   48,    0,    0,    0,    0,  228,   69,   69,   69,   69,
   69,   69,   69,   69,   48,   69,   69,   89,   48,    0,
    0,  112,    0,    0,    0,  124,    0,    0,    0,    0,
    9,   28,   29,   30,    9,   28,   29,   30,    9,   28,
   29,   30,  126,   31,    0,    0,  128,   31,    0,    0,
  131,   31,    0,    0,    0,    9,   28,   29,   30,    9,
   28,   29,   30,    9,   28,   29,   30,  133,   31,    0,
    0,  152,   31,    0,    0,    0,   31,    0,    0,    0,
    9,   28,   29,   30,    9,   28,   29,   30,    0,    0,
    0,    0,    0,   31,    0,    0,    0,   31,   58,   59,
   60,   61,   62,   63,   64,   65,    0,   66,   67,    0,
    0,    0,    0,  118,   58,   59,   60,   61,   62,   63,
   64,   65,    0,   66,   67,  231,  232,  233,  234,  235,
  236,  237,  239,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    5,   10,  138,  256,  273,   40,  287,  256,  256,  290,
  184,  256,  269,  136,    5,  269,  269,  286,  257,  258,
  259,  260,  256,  262,  263,  256,  256,  201,   33,  283,
  269,  280,  281,  287,   25,   40,  289,   46,   73,  273,
  163,   32,  290,  166,  283,  290,   55,  256,   83,  288,
  256,   42,   43,  292,  291,  289,   42,   43,  289,  289,
  269,  269,   53,  269,  187,   56,  189,   53,   73,  262,
   75,   62,   63,   64,   65,  283,  269,   68,   83,  287,
   85,  269,   68,  118,  119,   94,   77,   78,   79,   80,
   81,   82,  228,   79,  256,  283,  256,  262,  279,  287,
  109,  282,  270,  271,  269,   96,   97,   98,  270,  269,
   96,   97,   98,  118,  119,  256,  257,  258,  259,  260,
  257,  262,  263,  260,  287,  262,  263,  290,  269,  258,
  259,  136,  269,  138,  275,  276,  277,  278,  279,  280,
  281,  282,  283,  284,  285,  256,  283,  288,  256,  290,
  291,  292,  256,  287,  288,  292,  267,  269,  163,   63,
   64,  166,  270,  271,  272,  170,  270,  257,  258,  259,
  260,  283,  262,  263,  282,  287,   80,   81,   82,  269,
  258,  259,  187,  256,  189,  275,  276,  277,  278,  279,
  256,  256,  282,  283,  284,  285,  269,  202,  288,  264,
  290,  291,  292,  269,  270,  271,  272,  288,  279,  214,
  269,  282,  257,  258,  259,  260,  282,  262,  263,  290,
  280,  281,  273,  228,  269,  267,   62,  258,  259,   65,
  275,  276,  277,  278,  279,  256,  241,  282,  283,  284,
  285,   77,   78,  288,  265,  290,  291,  292,  286,  257,
  258,  259,  260,  269,  262,  263,  288,  279,  280,  281,
  282,  269,  279,  280,  281,  282,  288,  275,  276,  277,
  278,  279,  264,  290,  282,  283,  284,  285,  288,  264,
  288,  259,  290,  291,  292,  257,  258,  259,  260,  279,
  262,  263,  282,  258,  259,  258,  259,  269,  291,  279,
  290,  270,  282,  275,  276,  277,  278,  279,  288,  270,
  282,  283,  284,  285,  258,  259,  288,  270,  290,  291,
  292,  257,  258,  259,  260,  259,  262,  263,  275,  276,
  277,  278,  279,  269,  265,  282,  265,  284,  285,  275,
  276,  277,  278,  279,  265,  265,  282,  283,  284,  285,
  258,  259,  288,  291,  290,  291,  292,  257,  258,  259,
  260,  279,  262,  263,  282,  279,  259,  259,  282,  269,
  288,  270,  270,  270,  288,  275,  276,  277,  278,  279,
  270,  270,  282,  283,  284,  285,  259,  259,  288,  290,
  290,  291,  292,  257,  258,  259,  260,  257,  262,  263,
  260,  259,  262,  263,  290,  269,  290,  290,  290,  269,
  103,  275,  276,  277,  278,  279,   99,  155,  282,  283,
  284,  285,   55,  283,  288,   35,  290,  291,  292,  256,
  257,   32,  292,  260,   -1,   -1,  263,  269,  270,  271,
  272,   -1,  269,  270,  271,  272,  257,  258,  259,  260,
  282,  262,  263,   -1,   -1,  282,  283,   -1,  269,   -1,
   -1,   -1,  289,   -1,  291,   -1,   -1,   -1,  257,  258,
  259,  260,  283,  262,  263,   -1,  256,  288,   -1,   -1,
  269,  292,   -1,  257,  258,  259,  260,   -1,  262,  263,
  270,  271,  272,   -1,  283,  269,   -1,   -1,   -1,  288,
   -1,  257,  282,  292,  260,   -1,  262,  263,   -1,  283,
   -1,   -1,   -1,  269,  288,   -1,  257,   -1,  292,  260,
   -1,  262,  263,  270,  271,  272,   -1,  283,  269,   -1,
   -1,  287,  288,  256,  257,  282,  292,  260,  257,   -1,
  263,  260,  283,  262,  263,   -1,  269,   -1,   -1,  257,
  269,  292,  260,  257,  262,  263,  260,   -1,  262,  263,
  283,  269,   -1,   -1,  283,  269,  257,   -1,  291,  260,
   -1,  257,  263,  292,  260,  283,   -1,  263,  269,  283,
   -1,   -1,   -1,  269,  292,   -1,   -1,  257,  292,   -1,
  260,   -1,  283,  263,   -1,   -1,   -1,  283,   -1,  269,
   -1,  292,  257,   -1,  257,  260,  292,  260,  263,  257,
  263,   -1,  260,  283,  269,  263,  269,  257,   -1,   -1,
  260,  269,  292,  263,  270,  271,  272,   -1,  283,  269,
  283,   -1,   -1,   -1,   -1,  283,  282,  292,   -1,  292,
   -1,  287,  257,  283,  292,  260,   -1,   -1,  263,  256,
   -1,   -1,  292,   -1,  269,  257,  258,  259,  260,   -1,
   -1,  263,  269,  270,  271,  272,   -1,  269,  283,   -1,
  257,  258,  259,  260,   -1,  282,  263,  292,   -1,   -1,
  257,  283,  269,  260,   -1,   -1,  263,   -1,  290,  291,
   -1,   -1,  269,   -1,   -1,   -1,  283,   -1,   -1,  257,
  258,  259,  260,  290,  291,  263,  283,   -1,   -1,  257,
   -1,  269,  260,  290,  291,  263,  279,  280,  281,  282,
   -1,  269,   -1,   -1,  257,  283,   -1,  260,  257,   -1,
  263,  260,  290,  291,  263,  283,  269,   -1,   -1,   -1,
  269,  257,  290,  291,  260,   -1,   -1,  263,   -1,   -1,
  283,   -1,   -1,  269,  283,   -1,   -1,  256,  291,   -1,
   -1,   -1,  291,   -1,   -1,   -1,   -1,  283,   -1,   -1,
  269,   -1,   -1,   -1,   -1,  291,  275,  276,  277,  278,
  279,  280,  281,  282,  283,  284,  285,  256,  287,   -1,
   -1,  256,   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,
  269,  270,  271,  272,  269,  270,  271,  272,  269,  270,
  271,  272,  256,  282,   -1,   -1,  256,  282,   -1,   -1,
  256,  282,   -1,   -1,   -1,  269,  270,  271,  272,  269,
  270,  271,  272,  269,  270,  271,  272,  256,  282,   -1,
   -1,  256,  282,   -1,   -1,   -1,  282,   -1,   -1,   -1,
  269,  270,  271,  272,  269,  270,  271,  272,   -1,   -1,
   -1,   -1,   -1,  282,   -1,   -1,   -1,  282,  275,  276,
  277,  278,  279,  280,  281,  282,   -1,  284,  285,   -1,
   -1,   -1,   -1,  290,  275,  276,  277,  278,  279,  280,
  281,  282,   -1,  284,  285,  219,  220,  221,  222,  223,
  224,  225,  226,
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
"sentencia_print : PRINT PARENTINIC expresion PARENTFIN",
"sentencia_print : PRINT PARENTINIC error PARENTFIN",
"sentencia_print : PRINT error expresion PARENTFIN",
"sentencia_print : PRINT PARENTINIC expresion",
};

//#line 334 "gramaticaDeCero.y"

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
//#line 690 "Parser.java"
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
case 19:
//#line 96 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable"); }
break;
case 20:
//#line 97 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variables, falta ';' al final."); }
break;
case 23:
//#line 103 "gramaticaDeCero.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 24:
//#line 108 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
break;
case 25:
//#line 109 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
break;
case 26:
//#line 110 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, expresión inválida."); }
break;
case 27:
//#line 111 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
break;
case 28:
//#line 112 "gramaticaDeCero.y"
{ yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
break;
case 29:
//#line 116 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
break;
case 31:
//#line 120 "gramaticaDeCero.y"
{yyerror("Error sintáctico: Falta nombre identificador de función");}
break;
case 34:
//#line 131 "gramaticaDeCero.y"
{yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
break;
case 36:
//#line 137 "gramaticaDeCero.y"
{ yyerror("Falta identificador después de tipo en parámetro formal");}
break;
case 37:
//#line 138 "gramaticaDeCero.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 40:
//#line 148 "gramaticaDeCero.y"
{System.out.println("Asignación válida");}
break;
case 41:
//#line 153 "gramaticaDeCero.y"
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
case 42:
//#line 167 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de variables antes del '='"); }
break;
case 43:
//#line 168 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta lista de constantes después del '='");}
break;
case 44:
//#line 173 "gramaticaDeCero.y"
{n_cte++;}
break;
case 45:
//#line 174 "gramaticaDeCero.y"
{n_cte++;}
break;
case 46:
//#line 175 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una constante después de coma");}
break;
case 47:
//#line 176 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
break;
case 48:
//#line 179 "gramaticaDeCero.y"
{n_var++;}
break;
case 49:
//#line 180 "gramaticaDeCero.y"
{n_var++;}
break;
case 50:
//#line 181 "gramaticaDeCero.y"
{ yyerror("Error sintactico: falta identificador después de coma");}
break;
case 51:
//#line 182 "gramaticaDeCero.y"
{yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
break;
case 56:
//#line 194 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 57:
//#line 195 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '+' en expresión."); }
break;
case 59:
//#line 197 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 60:
//#line 198 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '-' en expresión."); }
break;
case 63:
//#line 205 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 64:
//#line 206 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '*' en expresión."); }
break;
case 66:
//#line 208 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 67:
//#line 209 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo antes de '/' en expresión."); }
break;
case 68:
//#line 210 "gramaticaDeCero.y"
{ yyerror("Falta operador entre factores en expresión."); }
break;
case 72:
//#line 222 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 73:
//#line 229 "gramaticaDeCero.y"
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
case 74:
//#line 243 "gramaticaDeCero.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 76:
//#line 256 "gramaticaDeCero.y"
{ 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
break;
case 77:
//#line 258 "gramaticaDeCero.y"
{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
break;
case 79:
//#line 264 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 80:
//#line 265 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 81:
//#line 266 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 82:
//#line 268 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 83:
//#line 269 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 84:
//#line 270 "gramaticaDeCero.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 85:
//#line 272 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 86:
//#line 273 "gramaticaDeCero.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 87:
//#line 274 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 88:
//#line 275 "gramaticaDeCero.y"
{ yyerror("Falta condicion en el if."); }
break;
case 90:
//#line 280 "gramaticaDeCero.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 91:
//#line 281 "gramaticaDeCero.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 100:
//#line 296 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 101:
//#line 297 "gramaticaDeCero.y"
{yyerror("Falta bloque del then");}
break;
case 104:
//#line 302 "gramaticaDeCero.y"
{yyerror("Falta sentencia en el bloque ejecutable");}
break;
case 105:
//#line 303 "gramaticaDeCero.y"
{yyerror("Falta bloque del else");}
break;
case 106:
//#line 308 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Sentencia for"); }
break;
case 107:
//#line 309 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 108:
//#line 310 "gramaticaDeCero.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 109:
//#line 311 "gramaticaDeCero.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 110:
//#line 312 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 111:
//#line 313 "gramaticaDeCero.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 112:
//#line 314 "gramaticaDeCero.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 113:
//#line 315 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 114:
//#line 316 "gramaticaDeCero.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 115:
//#line 317 "gramaticaDeCero.y"
{ yyerror("Falta bloque del for."); }
break;
case 117:
//#line 321 "gramaticaDeCero.y"
{System.out.println("BLOQUE FOR");}
break;
case 118:
//#line 322 "gramaticaDeCero.y"
{yyerror("Falta cuerpo en el bloque del for");}
break;
case 119:
//#line 327 "gramaticaDeCero.y"
{ SINT.add(lex.getLineaActual(), "Print"); }
break;
case 120:
//#line 328 "gramaticaDeCero.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 121:
//#line 329 "gramaticaDeCero.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 122:
//#line 330 "gramaticaDeCero.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
//#line 1177 "Parser.java"
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
