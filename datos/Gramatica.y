%start prog

/* ===== Palabras reservadas ===== */
%token IF ELSE ENDIF PRINT RETURN
%token INT DFLOAT
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

/* ===== Precedencias ===== */
%left '+' '-'
%left '*' '/'

%%
/* ========= Programa ========= */ /* programa: nombre + cuerpo */

prog		: ID '{' bloque '}'                                   
  		;

/* ========= Bloques ========= */ /* Mezcla declarativas + ejecutables */   

bloque		: /* vacío */
  		| bloque sentencia
  		;

/* Sólo ejecutables: se usa en if/for y en cuerpos lambda */

bloque_ejec	: /* vacío */
  		| bloque_ejec sentencia_ejec
  		;

/* ========= Sentencias ========= */

sentencia	: sentencia_decl
  		| sentencia_ejec
  		| funcion                       /* la función es una sentencia declarativa compuesta */
  		;

sentencia_decl	: declaracion ';'
  			;

sentencia_ejec	: asign_simple ';'
  			| asign_multiple ';'            /* tema 18 */
  			| bloque_if                        
  			| bloque_for                      /* tema 15 */
  			| print_sent                       /* tema 8 */
  			| llamada_funcion ';'              /* invocación como sentencia */
  			| return_sent                    /* termina en ';' adentro */
  			;

/* ========= Declaraciones ========= */

declaracion		: INT lista_ids
  			;

var_ref			: ID			/* tema 22 */
  			| ID '.' ID                      /* prefijado */
  			;	

lista_ids		: var_ref
 			| lista_ids ',' var_ref
  			;
/* ========= Asignaciones ========= */
/* Asignación simple y expresión aritmética SIN paréntesis de agrupación */			  		  
asign_simple		: var_ref ASIGN expresion
  			;

/* Tema 18 */ /* LHS puede tener más elementos que RHS.  RHS sólo constantes */

asign_multiple		: lista_ids '=' lista_ctes
  			;
/* Acción semántica: si |LHS| > |RHS|, completar/propagar o marcar según regla del informe.
   Si |LHS| < |RHS| => error para tema 18. Chequear que todos los RHS sean constantes numéricas. */

lista_ctes		: cte
  			| lista_ctes ',' cte
  			;	
/* ========= Constante ========= */

cte	: CTEFLOAT
	| CTEINT
	| CTESTR
  	;

/* ========= Expresiones aritméticas (sin '()' de agrupación) ========= */

expresion		: expresion '+' termino
  			| expresion '-' termino
  			| termino
  			;

termino		: termino '*' factor
  			| termino '/' factor
  			| factor
  			;

factor			: var_ref
  			| llamada_funcion
  			| cte
 			;

/* ========= Invocaciones y retorno ========= */
/* Params reales pueden ser expr, lambda (tema 28) o trunc (expr) (tema 31) */

llamada_funcion	: ID '(' lista_params_reales_opt ')'
  			;

lista_params_reales_opt	: /* vacío */
  				| lista_params_reales
  				;

lista_params_reales		: param_real_map
  				| lista_params_reales ',' param_real_map
 				;

/* Cada parámetro real debe mapear a un formal con '->' */
param_real_map		: parametro_real FLECHA ID
  				;	

parametro_real		: expresion                     /* sin paréntesis de agrupación */
  				| TRUNC '(' expresion ')'                		 /* tema 31 */
  				| lambda_expr                                     	 /* tema 28 */
  				;




/* ========= Retorno ========= */
return_sent		: RETURN '(' expresion ')' ';'
  			;

/* ========= Funciones (declaración) ========= */

funcion		: INT ID '(' lista_params_formales ')' '{' cuerpo_funcion '}'
  		;

lista_params_formales	: param_formal
  				| lista_params_formales ',' param_formal
  				;

param_formal			: sem_pasaje_opt INT ID            /* tema 24 */
  				;

sem_pasaje_opt		: /* vacío */                
  				| CV             
 				;

cuerpo_funcion		: cuerpo_funcion_items
  				;

cuerpo_funcion_items		: /* vacío */
  				| cuerpo_funcion_items cuerpo_item
 	 			;

cuerpo_item			: sentencia_decl
  				| sentencia_ejec
  				;
/* ========= If (selección) ========= */

bloque_if	: IF '(' condicion ')' rama_if opt_else ENDIF ';'
  			;

condicion	: expresion relop expresion
  			;

relop		: '<' 
			| '>' 
			| IGUAL 
			| DISTINTO 
			| MENORIGUAL 
			| MAYORIGUAL
  			;

rama_if		: sentencia_ejec
  			| '{' bloque_ejec '}'
  			;

opt_else		: /* vacío */
  			| ELSE rama_if
  			;
/* ========= For (tema 15) ========= */

bloque_for		: FOR '(' ID FROM CTEINT TO CTEINT ')' rama_for ';'
  			;

rama_for		: sentencia_ejec
  			| '{' bloque_ejec '}'
  			;

/* ========= Print (tema 8) ========= */

print_sent		: PRINT '(' cte ')' ';' 
  			| PRINT '(' expresion ')' ';'       
  ;

/* ========= Lambda como parámetro (tema 28) ========= */

lambda_expr		: '(' INT ID ')' '{' bloque_ejec '}' '(' argumento ')'
 			;

argumento	: ID
  			| cte
  			;

%%

/* ---- Seccion de código ---- */

static AnalizadorLexico lex = null;
static Parser par = null;

public static void main (String [] args) {
    System.out.println("Iniciando compilacion...");
    lex = new AnalizadorLexico (args[0]);
    par = new Parser (false);
    par.run();
    System.out.println("Fin compilacion");
}

int yylex (){
        Token token = null;
        if ((token = lex.getToken()) != null) { 
            yylval = new ParserVal(token.getEntradaTS());
            System.out.print("Token ID: " + token.getIDToken() + ". ");
            return token.getIDToken();
        } else {
            return 0; // Indica que no hay más tokens
        }
}

void yyerror (String mensaje){
    System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
}
