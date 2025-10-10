%start prog

%{
	package aplicacion;
	import aplicacion.AnalizadorLexico;
	import aplicacion.Token;
	import datos.TablaSimbolos;
%}

/* ===== Palabras reservadas ===== */
%token IF ELSE ENDIF PRINT RETURN
%token INT
%token FOR FROM TO
%token LAMBDA
%token CV
%token TRUNC

/* ===== Identificadores y literales ===== */

%token ID CTEINT CTEFLOAT CTESTR

/* ===== Operadores multi-caracter ===== */

%token ASIGN        /* ':=' */
%token FLECHA       /* '->' */
%token IGUAL        /* '==' */
%token DISTINTO     /* '=!' */
%token MENORIGUAL   /* '<=' */
%token MAYORIGUAL   /* '>=' */

/* ===== Agregados para arreglar el tema string/char ===== */

%token MAS			/* '+' */
%token MUL			/* '*' */
%token DIV			/* '/' */
%token MENOS		/* '-' */
%token IGUALUNICO	/* '=' */
%token MAYOR		/* '>' */
%token MENOR		/* '<' */
%token PUNTO		/* '.' */
%token COMA			/* ',' */
%token PUNTOYCOMA	/* ';' */
%token PARENTINIC	/* '(' */
%token PARENTFIN	/* ')' */
%token LLAVEINIC	/* '{' */
%token LLAVEFIN		/* '}' */




/* ===== Precedencias ===== */
%left MAS MENOS
%left MUL DIV

%%
/* ========= Programa ========= */ /* programa: nombre + cuerpo */

prog	: ID LLAVEINIC bloque LLAVEFIN                                   
  		;

/* ========= Bloques ========= */ /* Mezcla declarativas + ejecutables */   

bloque	: /* vacío */
  		| bloque sentencia
  		;

/* Sólo ejecutables: se usa en if/for y en cuerpos lambda */

bloque_ejec		: /* vacío */
  				| bloque_ejec sentencia_ejec
  				;

/* ========= Sentencias ========= */

sentencia	: sentencia_ejec
  			| INT ID decl_func
  			;

decl_func	: PUNTOYCOMA
			| COMA lista_ids PUNTOYCOMA
			| PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN
			;


sentencia_ejec	: asign_simple PUNTOYCOMA
  				| asign_multiple PUNTOYCOMA            /* tema 18 */
  				| bloque_if                        
  				| bloque_for                      /* tema 15 */
  				| print_sent                       /* tema 8 */
  				| llamada_funcion PUNTOYCOMA              /* invocación como sentencia */
  				| return_sent                    /* termina en PUNTOYCOMA adentro */
  				;

/* ========= Declaraciones ========= */

var_ref		: ID					/* tema 22 */
			| var_ref PUNTO ID
  			;	

lista_ids	: var_ref
 			| lista_ids COMA var_ref
  			;

/* ========= Asignaciones ========= */
/* Asignación simple y expresión aritmética SIN paréntesis de agrupación */	

asign_simple	: var_ref ASIGN expresion {System.out.println("Esto es una asign_simple");}
  				;

/* Tema 18 */ /* LHS puede tener más elementos que RHS.  RHS sólo constantes */

asign_multiple	: lista_ids IGUALUNICO lista_ctes
  				;

lista_ctes	: cte
  			| lista_ctes COMA cte
  			;	
			
/* ========= Constante ========= */

cte		: CTEFLOAT 
		| CTEINT
		| CTESTR
  		;

/* ========= Expresiones aritméticas (sin '()' de agrupación) ========= */

expresion	: expresion MAS termino
  			| expresion MENOS termino
  			| termino
  			;

termino		: termino MUL factor
  			| termino DIV factor
  			| factor
  			;

factor		: var_ref
  			| llamada_funcion
  			| cte
 			;

/* ========= Invocaciones y retorno ========= */

/* Params reales pueden ser expr, lambda (tema 28) o trunc (expr) (tema 31) */

llamada_funcion	: ID PARENTINIC lista_params_reales PARENTFIN
  				;

lista_params_reales		: param_real_map
  						| lista_params_reales COMA param_real_map
 						;

/* Cada parámetro real debe mapear a un formal con '->' */
param_real_map		: parametro_real FLECHA ID
  					;	

parametro_real	: expresion                     /* sin paréntesis de agrupación */
  				| TRUNC PARENTINIC expresion PARENTFIN                		 /* tema 31 */
  				| lambda_expr                                     	 /* tema 28 */
  				;

/* ========= Retorno ========= */
return_sent		: RETURN PARENTINIC expresion PARENTFIN PUNTOYCOMA
  				;

/* ========= Funciones (declaración) ========= */

lista_params_formales	: param_formal
						| lista_params_formales COMA param_formal
						;

param_formal		: sem_pasaje_opt INT ID            /* tema 24 */
  					;

sem_pasaje_opt		: /* vacío */                
					| CV             
					;

/* ========= If (selección) ========= */

bloque_if	: IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF PUNTOYCOMA
  			;

condicion	: expresion relop expresion
  			;

relop		: MENOR 
			| MAYOR 
			| IGUAL 
			| DISTINTO 
			| MENORIGUAL 
			| MAYORIGUAL
  			;

rama_if		: sentencia_ejec
  			| LLAVEINIC bloque_ejec LLAVEFIN
  			;

opt_else		: /* vacío */
				| ELSE rama_if
				;

/* ========= For (tema 15) ========= */

bloque_for		: FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for PUNTOYCOMA
  				;

rama_for		: sentencia_ejec
				| LLAVEINIC bloque_ejec LLAVEFIN
				;

/* ========= Print (tema 8) ========= */

print_sent		: PRINT PARENTINIC expresion PARENTFIN PUNTOYCOMA   
				;

/* ========= Lambda como parámetro (tema 28) ========= */

lambda_expr		: PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN PARENTINIC argumento PARENTFIN
 				;

argumento	: ID
  			| cte
  			;

%%

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;

public static void main (String [] args) {
	TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();
    System.out.println("Iniciando compilacion...");
    lex = new AnalizadorLexico (args[0]);
    par = new Parser (false);
    par.run();
	tablaSimbolos.mostrarTabla();
    System.out.println("Fin compilacion");
}

int yylex (){
        Token token = null;
        if ((token = lex.getToken()) != null) { 
            yylval = new ParserVal(token.getEntradaTS());
            System.out.println("Token ID: " + token.getIDToken() + ". ");
			if(token != null) {
				token.mostrarToken();
			}
            return token.getIDToken();
        } else {
            return 0; // Indica que no hay más tokens
        }
}

void yyerror (String mensaje){
    System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
}