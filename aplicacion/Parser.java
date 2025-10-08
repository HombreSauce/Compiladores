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
    0,    1,    1,    3,    3,    3,    2,    2,    2,    2,
    4,    4,    4,    4,    4,    4,    4,    5,    5,    5,
   15,   15,   13,   13,   13,   13,    6,    7,    7,    7,
    7,   17,   17,   17,   18,   18,   18,   18,   18,   16,
   16,   16,   16,   16,   19,   19,   19,   19,   19,   20,
   20,   20,   11,   11,   21,   21,   22,   22,   23,   23,
   23,   23,   23,   23,   12,   14,   14,   25,   25,   25,
   26,   26,    8,    8,    8,    8,    8,    8,    8,    8,
    8,   27,   27,   27,   27,   30,   30,   30,   30,   30,
   30,   28,   28,   29,   29,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,   31,   31,   10,   10,   10,
   10,   24,   24,   24,   24,   32,   32,
};
final static short yylen[] = {                            2,
    4,    0,    2,    0,    3,    2,    2,    1,    3,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    6,
    1,    3,    1,    3,    3,    2,    3,    3,    2,    2,
    3,    1,    3,    3,    1,    2,    1,    2,    1,    3,
    3,    3,    3,    1,    3,    3,    3,    3,    1,    1,
    1,    1,    4,    4,    1,    3,    3,    3,    1,    4,
    4,    4,    4,    1,    4,    1,    3,    3,    3,    3,
    0,    1,    6,    5,    6,    4,    6,    7,    7,    7,
    4,    3,    3,    3,    3,    1,    1,    1,    1,    1,
    1,    1,    3,    2,    2,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    2,    3,    4,    4,    4,
    4,   10,   10,   10,   10,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    3,    0,   11,   12,   13,   14,   15,
   16,   17,    0,    0,    0,    0,   37,   35,   39,    0,
    0,   51,    0,    0,   52,    0,   49,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   32,    7,   21,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   55,    0,   64,   88,   89,   90,   91,   87,   86,    4,
   92,    0,    0,   38,   36,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   18,    0,   10,    9,    0,    0,    0,    0,    0,   31,
    0,   25,    0,    0,   22,    0,    0,    0,    0,   54,
    0,    0,   81,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   45,    0,   47,    0,   76,  110,
  109,  111,  108,   65,    0,    0,   72,    0,   66,    0,
    0,    0,    0,    0,   53,   34,   33,    0,    0,    0,
   56,   58,   57,   93,    0,    0,    0,    0,   74,   19,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   63,
   62,   61,   60,    0,    5,   75,    0,    0,   77,   73,
    0,   67,    2,   70,   69,   68,    0,    0,    0,    0,
    0,    0,    4,    4,    0,   94,   80,   79,   78,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   20,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    0,  104,   97,   98,   99,  100,
  101,  102,  103,    0,   96,    0,    0,    0,    0,    0,
  106,    0,    0,    0,  116,  117,    0,    0,    0,  107,
  115,  113,  114,  112,
};
final static short yydgoto[] = {                          2,
    4,   71,  112,  225,   93,   16,   17,   18,   19,   20,
   21,   22,   23,  138,   24,   59,   47,   35,   36,   37,
   60,   61,   62,   63,  139,  140,   38,   72,  178,   73,
  226,  244,
};
final static short yysindex[] = {                      -266,
 -214,    0,    0,  357, -190,  -91, -224, -173, -156, -210,
 -151,  -45,    0,    0, -106,    0,    0,    0,    0,    0,
    0,    0,  733, -147,  608,  459,    0,    0,    0, -121,
  484,    0,  -66,  -61,    0, -157,    0,  469,  637,  643,
  637, -231, -231,  -68, -125,  608,  -48,    0,    0,    0,
  333,  -85,  -66,  -48,  637,  -24, -195,  -21,  -90,  -47,
    0,    6,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   35,  637,    0,    0,  765, -252,  637,  660,  664,
  668,  685,  689,  558,   55, -107,  -96, -237,  167,   63,
    0,   66,    0,    0,   73,   74, -246,  -31,  692,    0,
  -48,    0,  -66,  -90,    0,  637,  637,   82,  608,    0,
  -69,  -93,    0,  -90,  558,  568,  -90, -190, -157, -190,
 -157, -190,  -90, -190,    0, -190,    0,   93,    0,    0,
    0,    0,    0,    0, -166,  -66,    0,    5,    0, -114,
   84,   97,   98, -192,    0,    0,    0, -150, -149,   76,
    0,    0,    0,    0,   81,  111, -196,   -1,    0,    0,
   66,   83,  103,  -34,  108,  110,  123,  124, -245,    0,
    0,    0,    0, -254,    0,    0,  583,  132,    0,    0,
   37,    0,    0,    0,    0,    0,  133,  135,  136,  137,
  149, -161,    0,    0, -190,    0,    0,    0,    0,  391,
 -249,  138,  150,  151,  152,  153, -239,  401,  416,    0,
 -248, -248, -248, -248, -248, -248, -248, -248,  600,  155,
  158,  159,  173,    0,  168,    0,    0,    0,    0,    0,
    0,    0,    0, -190,    0,  616, -116,  616, -116,  431,
    0, -151,    0,  175,    0,    0,  187,  188,  189,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   -9,    0,    0,    0,  376,    0,    0,    0,    0,    0,
    0,    0,    0,  739,    0,    0,    0,    0,    0,    0,
    0,    0,   28,    0,    0,  139,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -233,    0,    0,    0,
    0,    0,  706, -187,    0,    0,    0,    0,  206,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -28,    0,    0,    0,    0,    0,    0,    0,    0,
  -50,    0,  713,  324,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  501,    0,    0,  511,  176,  213,  250,
  287,  526,  543,   65,    0,  102,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -141,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  441,    0,    0,    0,    0,    0,
  -28,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   42,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  339,    0,    0,    0,    0,    0,    0,
    0,  749,  130,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  242,   -3, -127,   -4,  383,    0,    0,    0,    0,    0,
   79,    0,  373,    0,    8,   96,   17,   -7,  220,  253,
  435,  374,    0,    0,  323,    0,  454,  -32,  328,  460,
  843,   92,
};
final static int YYTABLESIZE=1062;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         15,
   14,  193,    1,  115,   48,   85,  211,    5,    6,  143,
  191,    7,    8,   33,   10,   48,  218,  144,  132,  192,
   11,   15,   29,   29,   29,   29,   29,   29,   29,   29,
   53,   39,   33,   15,   12,   29,  194,  116,   33,   54,
  212,   79,  224,   48,   80,   44,   33,   33,   33,   29,
  219,  128,  133,   33,   29,   90,   91,   92,   29,  103,
  106,  177,   33,  168,   40,  208,  209,  101,   30,   30,
   30,   30,   30,   30,   30,   30,    3,  169,   45,   15,
   33,   30,  156,  158,   32,   33,   33,   33,   33,   33,
   33,  147,   25,  107,  206,   30,  240,  136,   25,   42,
   30,   34,   50,   32,   30,  170,  172,  155,  207,   32,
   15,   15,   43,   33,   33,   41,   33,   32,   32,   32,
   52,  160,   82,   83,   32,   55,   34,   23,   79,   79,
   96,   80,   80,   32,   86,   88,   89,   46,   56,  171,
  173,  163,   53,   97,  196,   23,   23,  164,   74,   75,
  104,   32,  245,   27,   28,   29,   32,   32,   32,   32,
   32,   32,    5,    6,   26,   30,    7,    8,  114,   10,
  102,   79,   15,  117,   80,   11,  123,   11,   27,   28,
   29,   49,  130,   50,   32,   32,  152,   32,   79,   12,
   30,   80,   25,  131,   78,   15,   14,   31,  154,  153,
   95,  148,  149,  155,  155,   28,   28,   28,   28,   28,
   28,   28,   28,   64,   65,   66,   67,   79,   28,   56,
   80,  185,   68,   69,   27,   28,   29,   71,  243,  246,
  243,  246,   28,   71,  186,  155,   30,   28,   99,  109,
  108,   28,  110,   33,  105,   33,   21,   21,   21,   21,
   21,   21,   21,   21,  179,  109,  177,  180,  145,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,  111,
   21,   21,   21,   50,   50,   50,   50,   50,   50,   50,
   50,  161,  198,  113,  162,  199,   50,   95,  119,  121,
   95,   50,   50,   50,   50,   50,   50,   50,   50,   50,
   50,   50,   50,  129,   32,   50,   32,   50,   50,   50,
   46,   46,   46,   46,   46,   46,   46,   46,  247,  248,
  249,   50,  137,   46,  125,  127,  141,  142,   46,   46,
   46,   46,   46,   46,   46,   46,   46,   46,   46,   46,
  150,  159,   46,  165,   46,   46,   46,   48,   48,   48,
   48,   48,   48,   48,   48,  174,  166,  167,  175,  176,
   48,  184,  187,  183,  188,   48,   48,   48,   48,   48,
   48,   48,   48,   48,   48,   48,   48,  189,  190,   48,
  197,   48,   48,   48,   44,   44,   44,   44,   44,   44,
   44,   44,  201,   52,  202,  203,  204,   44,   52,   52,
   52,   52,   44,   44,   44,   44,   44,   44,  205,  117,
   44,   44,   44,   44,  200,   94,   44,  213,   44,   44,
   44,   41,   41,   41,   41,   41,   41,   41,   41,  214,
  215,  216,  217,  236,   41,   79,  237,  238,   80,   41,
   41,   41,   41,   41,   41,  241,  134,   41,   41,   41,
   41,  239,  135,   41,  251,   41,   41,   41,   40,   40,
   40,   40,   40,   40,   40,   40,  252,  253,  254,   59,
   98,   40,  151,  182,   77,  181,   40,   40,   40,   40,
   40,   40,    0,   81,   40,   40,   40,   40,    0,    0,
   40,    0,   40,   40,   40,   43,   43,   43,   43,   43,
   43,   43,   43,    0,    0,    0,    0,    0,   43,    0,
    0,    0,    0,   43,   43,   43,   43,   43,   43,    0,
    0,   43,   43,   43,   43,    0,    0,   43,    0,   43,
   43,   43,   42,   42,   42,   42,   42,   42,   42,   42,
    0,    0,    0,    0,    0,   42,    0,    0,    0,    0,
   42,   42,   42,   42,   42,   42,    0,    0,   42,   42,
   42,   42,    0,    0,   42,    0,   42,   42,   42,   27,
   27,   27,   27,   27,   27,   27,   27,    0,  100,    0,
    0,    0,   27,    0,  105,  105,  105,  105,  105,  105,
  105,  105,   27,   28,   29,    0,   27,  105,    0,    0,
    0,   27,    5,    6,   30,   27,    7,    8,    9,   10,
    0,  105,    0,    0,    0,   11,  105,    0,    0,    0,
  105,    8,    8,    8,    8,    8,    8,    8,    8,   12,
    0,    0,    0,    0,    8,    0,    5,    6,   13,    0,
    7,    8,    9,   10,    0,    0,  220,    6,    8,   11,
    7,    8,    0,   10,    0,    0,    0,    8,    0,   11,
    0,  222,    6,   12,    0,    7,    8,    0,   10,    0,
    0,    0,  210,   12,   11,    0,    5,    6,    0,    0,
    7,    8,  221,   10,    0,    0,    6,    6,   12,   11,
    6,    6,    0,    6,    0,    0,    0,  223,    0,    6,
    0,    0,    0,   12,    5,    6,    0,    0,    7,    8,
    9,   10,  250,    6,    5,    6,    0,   11,    7,    8,
    9,   10,    6,   64,   65,   66,   67,   11,    0,   76,
    0,   12,   68,   69,    0,    0,    0,   25,    0,   70,
    0,   12,   11,   27,   28,   29,   84,   84,   84,   70,
   84,   84,   84,   84,    0,   30,   83,   83,    0,   84,
   83,   83,   83,   83,    0,    0,    0,    0,    0,   83,
    0,   85,   85,   84,    0,   85,   85,   85,   85,    0,
   84,   84,    0,   83,   85,    0,    0,    0,   82,   82,
   83,   83,   82,   82,   82,   82,    0,    0,   85,    0,
    0,   82,    0,    5,    6,   85,   85,    7,    8,    9,
   10,    0,    0,  157,    6,   82,   11,    7,    8,    9,
   10,    0,   82,   82,    0,    0,   11,    0,  195,    6,
   12,    0,    7,    8,    9,   10,    0,    0,   70,    0,
   12,   11,    0,    0,    0,  234,    6,    0,   70,    7,
    8,    0,   10,    5,    0,   12,    0,    0,   11,    0,
    0,    5,    0,   70,    0,   57,   11,   27,   28,   29,
    0,    0,   12,   57,  242,   27,   28,   29,    0,   30,
  224,    0,    5,    0,    0,    0,   58,   30,   87,    0,
    0,    0,    0,    0,   58,   11,   27,   28,   29,    0,
    0,   11,   27,   28,   29,  118,    0,    0,   30,  120,
    0,    0,    0,  122,   30,    0,    0,    0,   11,   27,
   28,   29,   11,   27,   28,   29,   11,   27,   28,   29,
  124,   30,    0,    0,  126,   30,    0,  146,    0,   30,
    0,    0,    0,   11,   27,   28,   29,   11,   27,   28,
   29,   27,   28,   29,    0,    0,   30,    0,    0,    0,
   30,    0,    0,   30,   26,   26,   26,   26,    0,    0,
    0,   24,   24,   24,   24,    0,    0,   26,   26,    0,
    0,    0,   26,   26,   24,   24,    0,    0,    0,   24,
   24,   50,   27,   28,   29,    0,    0,   23,   23,   23,
   23,    0,    0,    0,   30,   51,    0,    0,    0,   52,
   23,   23,   21,    0,    0,   23,    0,   21,   21,   21,
   21,    0,    0,    0,   21,    0,    0,    0,  116,   64,
   65,   66,   67,    0,    0,    0,    0,    0,   68,   69,
    0,    0,    0,   25,  227,  228,  229,  230,  231,  232,
  233,  235,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          4,
    4,  256,  269,  256,   12,   38,  256,  256,  257,  256,
  256,  260,  261,    6,  263,   23,  256,  264,  256,  265,
  269,   26,  256,  257,  258,  259,  260,  261,  262,  263,
   23,  256,   25,   38,  283,  269,  291,  290,   31,   23,
  290,  279,  291,   51,  282,  256,   39,   40,   41,  283,
  290,   84,  290,   46,  288,  287,  288,  289,  292,   52,
  256,  258,   55,  256,  289,  193,  194,   51,  256,  257,
  258,  259,  260,  261,  262,  263,  291,  270,  289,   84,
   73,  269,  115,  116,    6,   78,   79,   80,   81,   82,
   83,   99,  289,  289,  256,  283,  224,   90,  289,  256,
  288,    6,  269,   25,  292,  256,  256,  112,  270,   31,
  115,  116,  269,  106,  107,  289,  109,   39,   40,   41,
  287,  288,  280,  281,   46,  273,   31,  269,  279,  279,
  256,  282,  282,   55,   39,   40,   41,  289,  286,  290,
  290,  256,  135,  269,  177,  287,  288,  262,  270,  271,
   55,   73,  269,  270,  271,  272,   78,   79,   80,   81,
   82,   83,  256,  257,  256,  282,  260,  261,   73,  263,
  256,  279,  177,   78,  282,  269,   81,  269,  270,  271,
  272,  288,  290,  269,  106,  107,  256,  109,  279,  283,
  282,  282,  289,  290,  256,  200,  200,  289,  292,  269,
  269,  106,  107,  208,  209,  256,  257,  258,  259,  260,
  261,  262,  263,  275,  276,  277,  278,  279,  269,  286,
  282,  256,  284,  285,  270,  271,  272,  256,  236,  237,
  238,  239,  283,  262,  269,  240,  282,  288,  287,  287,
  262,  292,  290,  236,  269,  238,  256,  257,  258,  259,
  260,  261,  262,  263,  256,  287,  258,  259,  290,  269,
  270,  271,  272,  273,  274,  275,  276,  277,  278,  279,
  280,  281,  282,  283,  284,  285,  286,  287,  288,  274,
  290,  291,  292,  256,  257,  258,  259,  260,  261,  262,
  263,  287,  256,  259,  290,  259,  269,  256,   79,   80,
  259,  274,  275,  276,  277,  278,  279,  280,  281,  282,
  283,  284,  285,  259,  236,  288,  238,  290,  291,  292,
  256,  257,  258,  259,  260,  261,  262,  263,  237,  238,
  239,  269,  267,  269,   82,   83,  264,  264,  274,  275,
  276,  277,  278,  279,  280,  281,  282,  283,  284,  285,
  269,  259,  288,  270,  290,  291,  292,  256,  257,  258,
  259,  260,  261,  262,  263,  290,  270,  270,  288,  259,
  269,  269,  265,  291,  265,  274,  275,  276,  277,  278,
  279,  280,  281,  282,  283,  284,  285,  265,  265,  288,
  259,  290,  291,  292,  256,  257,  258,  259,  260,  261,
  262,  263,  270,  274,  270,  270,  270,  269,  279,  280,
  281,  282,  274,  275,  276,  277,  278,  279,  270,  290,
  282,  283,  284,  285,  183,   43,  288,  290,  290,  291,
  292,  256,  257,  258,  259,  260,  261,  262,  263,  290,
  290,  290,  290,  289,  269,  279,  289,  289,  282,  274,
  275,  276,  277,  278,  279,  288,  290,  282,  283,  284,
  285,  289,   90,  288,  290,  290,  291,  292,  256,  257,
  258,  259,  260,  261,  262,  263,  290,  290,  290,  274,
   46,  269,  109,  161,   31,  158,  274,  275,  276,  277,
  278,  279,   -1,   34,  282,  283,  284,  285,   -1,   -1,
  288,   -1,  290,  291,  292,  256,  257,  258,  259,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,   -1,
   -1,   -1,   -1,  274,  275,  276,  277,  278,  279,   -1,
   -1,  282,  283,  284,  285,   -1,   -1,  288,   -1,  290,
  291,  292,  256,  257,  258,  259,  260,  261,  262,  263,
   -1,   -1,   -1,   -1,   -1,  269,   -1,   -1,   -1,   -1,
  274,  275,  276,  277,  278,  279,   -1,   -1,  282,  283,
  284,  285,   -1,   -1,  288,   -1,  290,  291,  292,  256,
  257,  258,  259,  260,  261,  262,  263,   -1,  256,   -1,
   -1,   -1,  269,   -1,  256,  257,  258,  259,  260,  261,
  262,  263,  270,  271,  272,   -1,  283,  269,   -1,   -1,
   -1,  288,  256,  257,  282,  292,  260,  261,  262,  263,
   -1,  283,   -1,   -1,   -1,  269,  288,   -1,   -1,   -1,
  292,  256,  257,  258,  259,  260,  261,  262,  263,  283,
   -1,   -1,   -1,   -1,  269,   -1,  256,  257,  292,   -1,
  260,  261,  262,  263,   -1,   -1,  256,  257,  283,  269,
  260,  261,   -1,  263,   -1,   -1,   -1,  292,   -1,  269,
   -1,  256,  257,  283,   -1,  260,  261,   -1,  263,   -1,
   -1,   -1,  292,  283,  269,   -1,  256,  257,   -1,   -1,
  260,  261,  292,  263,   -1,   -1,  256,  257,  283,  269,
  260,  261,   -1,  263,   -1,   -1,   -1,  292,   -1,  269,
   -1,   -1,   -1,  283,  256,  257,   -1,   -1,  260,  261,
  262,  263,  292,  283,  256,  257,   -1,  269,  260,  261,
  262,  263,  292,  275,  276,  277,  278,  269,   -1,  256,
   -1,  283,  284,  285,   -1,   -1,   -1,  289,   -1,  291,
   -1,  283,  269,  270,  271,  272,  256,  257,  290,  291,
  260,  261,  262,  263,   -1,  282,  256,  257,   -1,  269,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,  269,
   -1,  256,  257,  283,   -1,  260,  261,  262,  263,   -1,
  290,  291,   -1,  283,  269,   -1,   -1,   -1,  256,  257,
  290,  291,  260,  261,  262,  263,   -1,   -1,  283,   -1,
   -1,  269,   -1,  256,  257,  290,  291,  260,  261,  262,
  263,   -1,   -1,  256,  257,  283,  269,  260,  261,  262,
  263,   -1,  290,  291,   -1,   -1,  269,   -1,  256,  257,
  283,   -1,  260,  261,  262,  263,   -1,   -1,  291,   -1,
  283,  269,   -1,   -1,   -1,  256,  257,   -1,  291,  260,
  261,   -1,  263,  256,   -1,  283,   -1,   -1,  269,   -1,
   -1,  256,   -1,  291,   -1,  268,  269,  270,  271,  272,
   -1,   -1,  283,  268,  269,  270,  271,  272,   -1,  282,
  291,   -1,  256,   -1,   -1,   -1,  289,  282,  256,   -1,
   -1,   -1,   -1,   -1,  289,  269,  270,  271,  272,   -1,
   -1,  269,  270,  271,  272,  256,   -1,   -1,  282,  256,
   -1,   -1,   -1,  256,  282,   -1,   -1,   -1,  269,  270,
  271,  272,  269,  270,  271,  272,  269,  270,  271,  272,
  256,  282,   -1,   -1,  256,  282,   -1,  256,   -1,  282,
   -1,   -1,   -1,  269,  270,  271,  272,  269,  270,  271,
  272,  270,  271,  272,   -1,   -1,  282,   -1,   -1,   -1,
  282,   -1,   -1,  282,  269,  270,  271,  272,   -1,   -1,
   -1,  269,  270,  271,  272,   -1,   -1,  282,  283,   -1,
   -1,   -1,  287,  288,  282,  283,   -1,   -1,   -1,  287,
  288,  269,  270,  271,  272,   -1,   -1,  269,  270,  271,
  272,   -1,   -1,   -1,  282,  283,   -1,   -1,   -1,  287,
  282,  283,  274,   -1,   -1,  287,   -1,  279,  280,  281,
  282,   -1,   -1,   -1,  286,   -1,   -1,   -1,  290,  275,
  276,  277,  278,   -1,   -1,   -1,   -1,   -1,  284,  285,
   -1,   -1,   -1,  289,  212,  213,  214,  215,  216,  217,
  218,  219,
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

//#line 313 ".\gramatica.y"

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
//#line 826 "Parser.java"
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
//#line 71 ".\gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 8:
//#line 77 ".\gramatica.y"
{ yyerror("Falta ';' al final de la sentencia."); }
break;
case 10:
//#line 79 ".\gramatica.y"
{yyerror("Falta identificador despues de 'int'");}
break;
case 19:
//#line 92 ".\gramatica.y"
{n_var = 0;}
break;
case 23:
//#line 103 ".\gramatica.y"
{n_var++;}
break;
case 24:
//#line 104 ".\gramatica.y"
{n_var++;}
break;
case 25:
//#line 105 ".\gramatica.y"
{ yyerror("Error: falta identificador después de coma");}
break;
case 26:
//#line 106 ".\gramatica.y"
{yyerror("Error: falta una coma entre identificadores en la lista de variables");}
break;
case 28:
//#line 117 ".\gramatica.y"
{
					if (n_var < n_cte) {
						yyerror("Error: más constantes que variables en la asignación");
					} else {
						System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
					}
					n_var = n_cte = 0;  /* reset para la próxima */
				}
break;
case 29:
//#line 125 ".\gramatica.y"
{ yyerror("Error: falta lista de variables antes del '='"); }
break;
case 30:
//#line 126 ".\gramatica.y"
{ yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
break;
case 31:
//#line 127 ".\gramatica.y"
{ yyerror("Error: falta lista de constantes después del '='");}
break;
case 32:
//#line 130 ".\gramatica.y"
{n_cte++;}
break;
case 33:
//#line 131 ".\gramatica.y"
{n_cte++;}
break;
case 34:
//#line 132 ".\gramatica.y"
{ yyerror("Error: falta una constante después de coma");}
break;
case 36:
//#line 138 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/
			yyval = val_peek(0); /*se reduce por CTEFLOAT*/
		}
break;
case 37:
//#line 145 ".\gramatica.y"
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
case 38:
//#line 159 ".\gramatica.y"
{
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)val_peek(0).obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); /*eliminamos la entrada del positivo que se creo en el lexico*/

			yyval = val_peek(0);
		}
break;
case 41:
//#line 173 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '+' en expresión."); }
break;
case 43:
//#line 175 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '-' en expresión."); }
break;
case 46:
//#line 180 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '*' en expresión."); }
break;
case 48:
//#line 182 ".\gramatica.y"
{ yyerror("Falta operando derecho después de '/' en expresión."); }
break;
case 54:
//#line 196 ".\gramatica.y"
{ yyerror("Llamada a función sin nombre");}
break;
case 58:
//#line 205 ".\gramatica.y"
{ yyerror("Falta identificador después de '->' en parámetro real");}
break;
case 61:
//#line 210 ".\gramatica.y"
{ yyerror("Falta ')' en llamada a función con 'trunc'.");}
break;
case 62:
//#line 211 ".\gramatica.y"
{ yyerror("Falta '(' en llamada a función con 'trunc'.");}
break;
case 63:
//#line 212 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
break;
case 69:
//#line 227 ".\gramatica.y"
{ yyerror("Falta identificador después de 'int' en parámetro formal");}
break;
case 70:
//#line 228 ".\gramatica.y"
{ yyerror("Falta tipo en parámetro formal");}
break;
case 74:
//#line 240 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia if."); }
break;
case 75:
//#line 241 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia if."); }
break;
case 76:
//#line 242 ".\gramatica.y"
{ yyerror("Faltan los paréntesis en sentencia if."); }
break;
case 77:
//#line 243 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque if."); }
break;
case 79:
//#line 245 ".\gramatica.y"
{ yyerror("Falta 'endif' al final del bloque else."); }
break;
case 80:
//#line 246 ".\gramatica.y"
{ yyerror("Falta bloque del then."); }
break;
case 81:
//#line 247 ".\gramatica.y"
{ yyerror("Falta el cuerpo de condicion en el if.");}
break;
case 83:
//#line 252 ".\gramatica.y"
{ yyerror("Falta comparador en la condicion."); }
break;
case 84:
//#line 253 ".\gramatica.y"
{ yyerror("Falta operando izquierdo en la condicion."); }
break;
case 85:
//#line 254 ".\gramatica.y"
{ yyerror("Falta operando derecho en la condicion."); }
break;
case 95:
//#line 271 ".\gramatica.y"
{ yyerror("Falta bloque del else."); }
break;
case 97:
//#line 277 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia for."); }
break;
case 98:
//#line 278 ".\gramatica.y"
{ yyerror("Falta identificador en sentencia for."); }
break;
case 99:
//#line 279 ".\gramatica.y"
{ yyerror("Falta 'from' en sentencia for."); }
break;
case 100:
//#line 280 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'from' en sentencia for."); }
break;
case 101:
//#line 281 ".\gramatica.y"
{ yyerror("Falta 'to' en sentencia for."); }
break;
case 102:
//#line 282 ".\gramatica.y"
{ yyerror("Falta constante entera después de 'to' en sentencia for."); }
break;
case 103:
//#line 283 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia for."); }
break;
case 104:
//#line 284 ".\gramatica.y"
{ yyerror("Faltan los parentesis en sentencia for."); }
break;
case 105:
//#line 285 ".\gramatica.y"
{ yyerror("Falta bloque del for."); }
break;
case 109:
//#line 295 ".\gramatica.y"
{ yyerror("Falta argumento en sentencia print."); }
break;
case 110:
//#line 296 ".\gramatica.y"
{ yyerror("Falta '(' en sentencia print."); }
break;
case 111:
//#line 297 ".\gramatica.y"
{ yyerror("Falta ')' en sentencia print."); }
break;
case 113:
//#line 303 ".\gramatica.y"
{ yyerror("Falta '{' en expresión lambda."); }
break;
case 114:
//#line 304 ".\gramatica.y"
{ yyerror("Falta '}' en expresión lambda."); }
break;
case 115:
//#line 305 ".\gramatica.y"
{ yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
break;
//#line 1228 "Parser.java"
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
