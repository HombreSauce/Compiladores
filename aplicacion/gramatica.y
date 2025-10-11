%start prog

%{
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
  				| bloque_ejec sentencia_ejec PUNTOYCOMA
                | bloque_ejec sentencia_ejec { yyerror("Falta ';' al final de la sentencia."); }
  				;

/* ========= Sentencias ========= */

tipo    : INT
        ;

sentencia	: declaracion_variable
			| sentencia_ejec PUNTOYCOMA
            | sentencia_ejec { yyerror("Falta ';' al final de la sentencia."); }
  			| declaracion_funcion
            //| INT error decl {yyerror("Falta identificador despues de 'int'");}
			//| INT PUNTOYCOMA { yyerror("Falta identificador despues de 'int'");}
  			;

sentencia_ejec	: asign_simple
  				| asign_multiple            /* tema 18 */
  				| bloque_if 
  				| bloque_for                      /* tema 15 */
  				| print_sent                       /* tema 8 */
  				| llamada_funcion { SINT.add(lex.getLineaActual(), "Llamada a funcion"); }/* invocación como sentencia */
  				| return_sent                    /* termina en PUNTOYCOMA adentro */
				;

declaracion_variable	: tipo ID PUNTOYCOMA { SINT.add(lex.getLineaActual(), "Declaracion de variable"); n_var = 0;} // se reinician el contador de variables por si lo debe usar la asignacion multiple
						| tipo lista_ids { yyerror("Falta ';' al final de la declaracion."); n_var = 0;} // se reinician el contador de variables por si lo debe usar la asignacion multiple
						;

declaracion_funcion		: tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN { SINT.add(lex.getLineaActual(), "Declaracion de funcion"); }
						| declaracion_funcion_error
						;

declaracion_funcion_error	: tipo PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN { yyerror("Falta el nombre de la funcion"); }
							//| tipo ID error lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN { yyerror("Falta '(' en declaracion de funcion"); }
							//| tipo ID PARENTINIC error PARENTFIN LLAVEINIC bloque LLAVEFIN { yyerror("Falta lista de parametros formales en declaracion de funcion"); }
							//| tipo ID PARENTINIC lista_params_formales error LLAVEINIC bloque LLAVEFIN { yyerror("Falta ')' en declaracion de funcion"); }
							//| tipo ID PARENTINIC lista_params_formales PARENTFIN error bloque LLAVEFIN { yyerror("Falta '{' en declaracion de funcion"); }
							//| tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC error LLAVEFIN { yyerror("Falta cuerpo de la funcion en declaracion de funcion"); }
							//| tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque error { yyerror("Falta '}' al final de la declaracion de funcion"); }
							;




/* ========= Declaraciones ========= */

var_ref		: ID					/* tema 22 */
			| var_ref PUNTO ID
  			;	

lista_ids	: ID {n_var++;}
 			| lista_ids COMA ID {n_var++;}
			| lista_ids COMA error { yyerror("Se esperaba un identificador despues de coma");}
			//| lista_ids ID {yyerror("Falta una coma entre identificadores en la lista de variables");}
  			;

/* ========= Asignaciones ========= */
/* Asignación simple y expresión aritmética SIN paréntesis de agrupación */	

asign_simple	: var_ref ASIGN expresion { SINT.add(lex.getLineaActual(), "Asignacion"); }
  				| var_ref ASIGN error { yyerror("Falta expresion despues de ':=' en asignacion."); }
  				| ASIGN expresion { yyerror("Falta variable antes de ':=' en asignacion."); }
  				| var_ref error expresion { yyerror("Falta ':=' en asignacion."); }
				| var_ref PUNTOYCOMA { yyerror("Falta ':=' y expresion en asignacion."); }
				//| var_ref error { yyerror("Falta ':=' y expresion en asignacion."); }
				;

/* Tema 18 */ /* LHS puede tener más elementos que RHS.  RHS sólo constantes */

asign_multiple	: lista_ids IGUALUNICO lista_ctes {
					if (n_var < n_cte) {
						yyerror("Error: mas constantes que variables en la asignacion");
					} else {
						System.out.println("Asignacion valida (" + n_var + ", " + n_cte + ")");
						SINT.add(lex.getLineaActual(), "Asignacion multiple");
					}
					n_var = n_cte = 0;  /* reset para la próxima */
				}
				| IGUALUNICO lista_ctes { yyerror("Falta lista de variables antes del '='"); }
				| lista_ids lista_ctes { yyerror("Falta '=' entre la lista de variables y la lista de constantes"); }
				| lista_ids IGUALUNICO error { yyerror("Falta lista de constantes despues del '='");}
  				;

lista_ctes	: cte {n_cte++;}
  			| lista_ctes COMA cte {n_cte++;}
			| lista_ctes COMA error { yyerror("Falta una constante despues de coma");}
  			;	
			
/* ========= Constante ========= */

cte		: CTEFLOAT //no es necesario chequear el rango de los flotantes positivos ni negativos porque ya lo hace la AS9
		| MENOS CTEFLOAT {
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)$2.obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); //eliminamos la entrada del positivo que se creo en el lexico
			yyval = $2; //se reduce por CTEFLOAT
		}
		| CTEINT {
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)$1.obj;
			String valor = entrada.getLexema();
			valor = valor.substring(0, valor.length() - 1); //nos quedamos con el numero sin el I final
			int num = Integer.parseInt(valor);
			int max = 32767;
			//al ser positivo debemos chequear el maximo
			if (num > max) {
				String msg = "Error lexico: constante entera fuera de rango: " + num;
				logLexError(msg);
				tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea());
			}

			yyval = $1;
		}
		| MENOS CTEINT {
			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)$2.obj;
			String valor_negativo = '-' + entrada.getLexema();
			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); //eliminamos la entrada del positivo que se creo en el lexico

			yyval = $2;
		}
		| CTESTR
  		;

/* ========= Expresiones aritméticas (sin '()' ) ========= */

expresion   : termino
            | expresion MAS termino
            | expresion MAS error { yyerror("Falta operando derecho despues de '+' en expresion."); }
            | error MAS termino { yyerror("Falta operando izquierdo antes de '+' en expresion."); }
            | expresion MENOS termino
            | expresion MENOS error { yyerror("Falta operando derecho despues de '-' en expresion."); }
            | error MENOS termino { yyerror("Falta operando izquierdo antes de '-' en expresion."); }
            // | expresion error termino { yyerror("Falta operador entre factores en expresion."); }
            // | expresion_error
            ;

termino     : factor
            | termino MUL factor
            | termino MUL error { yyerror("Falta operando derecho despues de '' en expresion."); }
            | error MUL factor { yyerror("Falta operando izquierdo antes de '' en expresion."); }
            | termino DIV factor
            | termino DIV error { yyerror("Falta operando derecho despues de '/' en expresion."); }
            | error DIV factor { yyerror("Falta operando izquierdo antes de '/' en expresion."); }
            //| termino error factor{ yyerror("Falta operador entre factores en expresion."); }
            ;

factor      : var_ref
            // | llamada_funcion
            | cte
            ;

/* ========= Invocaciones y retorno ========= */

/* Params reales pueden ser expr, lambda (tema 28) o trunc (expr) (tema 31) */

llamada_funcion	: ID PARENTINIC lista_params_reales PARENTFIN 
                | error PARENTINIC lista_params_reales PARENTFIN{ yyerror("Llamada a funcion sin nombre");}
  				;

lista_params_reales		: param_real_map
  						| lista_params_reales COMA param_real_map
						;

/* Cada parámetro real debe mapear a un formal con '->' */
param_real_map		: parametro_real FLECHA ID
                    | parametro_real FLECHA error { yyerror("Falta identificador despues de '->' en parametro real");}
					;	

parametro_real	: expresion                    
  				| TRUNC PARENTINIC expresion PARENTFIN { SINT.add(lex.getLineaActual(), "Trunc"); }                		 /* tema 31 */
                | TRUNC PARENTINIC expresion error { yyerror("Falta ')' en llamada a funcion con 'trunc'.");}
                | TRUNC error expresion PARENTFIN { yyerror("Falta '(' en llamada a funcion con 'trunc'.");}
                | TRUNC error expresion error { yyerror("Faltan los parentesis en llamada a funcion con 'trunc'.");}
  				| lambda_expr                                     	 /* tema 28 */
				;

/* ========= Retorno ========= */
return_sent		: RETURN PARENTINIC expresion PARENTFIN { SINT.add(lex.getLineaActual(), "Return"); }
  				;

/* ========= Funciones (declaración) ========= */

lista_params_formales	: param_formal
						| lista_params_formales COMA param_formal
						;

param_formal		: sem_pasaje_opt INT ID            /* tema 24 */
            		| sem_pasaje_opt INT error { yyerror("Falta identificador despues de 'int' en parametro formal");}            
                    | sem_pasaje_opt error ID { yyerror("Falta tipo en parametro formal");}
					;

sem_pasaje_opt		: /* vacío */                
					| CV
					;

/* ========= If (selección) ========= */

/* ========= If (selección) ========= */

bloque_if   : IF PARENTINIC condicion PARENTFIN rama_if ENDIF { 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
            | IF condicion PARENTFIN rama_if ENDIF { yyerror("Falta '(' en sentencia if."); }
            | IF PARENTINIC condicion error rama_if ENDIF { yyerror("Falta ')' en sentencia if."); }
            | IF condicion rama_if ENDIF { yyerror("Faltan los parentesis en sentencia if."); }
            | IF PARENTINIC condicion PARENTFIN rama_if error { yyerror("Falta 'endif' al final del bloque if."); }
            | IF PARENTINIC condicion PARENTFIN rama_if opt_else ENDIF{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
            | IF PARENTINIC condicion PARENTFIN rama_if opt_else error { yyerror("Falta 'endif' al final del bloque else."); }
            | IF PARENTINIC condicion PARENTFIN error  opt_else ENDIF { yyerror("Falta bloque del then."); }
            | IF error rama_if ENDIF { yyerror("Falta el cuerpo de condicion en el if.");}
            ;
            /*se podrian agregar errores por si falta alguno de los dos parentesis de la condicion*/

condicion   : expresion relop expresion
            | expresion error expresion { yyerror("Falta comparador en la condicion."); }
            | error relop expresion { yyerror("Falta operando izquierdo en la condicion."); }
            | expresion relop error { yyerror("Falta operando derecho en la condicion."); }
            // | /* vacío */ { yyerror("Falta condicion en el if."); }
            ;

relop       : MENOR 
            | MAYOR 
            | IGUAL 
            | DISTINTO 
            | MENORIGUAL 
            | MAYORIGUAL
            ;

rama_if     : sentencia
            | LLAVEINIC bloque_ejec LLAVEFIN
            ;

opt_else    : ELSE rama_if
            | ELSE error { yyerror("Falta bloque del else."); }
            ;

/* ========= For (tema 15) ========= */

bloque_for		: FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for { SINT.add(lex.getLineaActual(), "Sentencia for"); }
                | FOR error ID FROM CTEINT TO CTEINT PARENTFIN rama_for { yyerror("Falta '(' en sentencia for."); }
                | FOR PARENTINIC error FROM CTEINT TO CTEINT PARENTFIN rama_for { yyerror("Falta identificador en sentencia for."); }
                | FOR PARENTINIC ID error CTEINT TO CTEINT PARENTFIN rama_for { yyerror("Falta 'from' en sentencia for."); }
                | FOR PARENTINIC ID FROM error TO CTEINT PARENTFIN rama_for { yyerror("Falta constante entera después de 'from' en sentencia for."); }
                | FOR PARENTINIC ID FROM CTEINT error CTEINT PARENTFIN rama_for { yyerror("Falta 'to' en sentencia for."); }
                | FOR PARENTINIC ID FROM CTEINT TO error PARENTFIN rama_for { yyerror("Falta constante entera después de 'to' en sentencia for."); }
                | FOR PARENTINIC ID FROM CTEINT TO CTEINT error rama_for { yyerror("Falta ')' en sentencia for."); }
                | FOR error ID FROM CTEINT TO CTEINT error rama_for { yyerror("Faltan los parentesis en sentencia for."); }
                | FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN error { yyerror("Falta bloque del for."); }
				;

rama_for		: sentencia_ejec PUNTOYCOMA
				| LLAVEINIC bloque_ejec LLAVEFIN
				;

/* ========= Print (tema 8) ========= */

print_sent		: PRINT PARENTINIC expresion PARENTFIN { SINT.add(lex.getLineaActual(), "Print"); }
                | PRINT PARENTINIC error PARENTFIN { yyerror("Falta argumento en sentencia print."); }
                | PRINT error expresion PARENTFIN { yyerror("Falta '(' en sentencia print."); }
                | PRINT PARENTINIC expresion error { yyerror("Falta ')' en sentencia print."); }
  				;

/* ========= Lambda como parámetro (tema 28) ========= */

lambda_expr		: PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec LLAVEFIN PARENTINIC argumento PARENTFIN { SINT.add(lex.getLineaActual(), "Lambda"); }
                | PARENTINIC INT ID PARENTFIN error bloque_ejec LLAVEFIN PARENTINIC argumento PARENTFIN { yyerror("Falta '{' en expresion lambda."); }
                | PARENTINIC INT ID PARENTFIN LLAVEINIC bloque_ejec error PARENTINIC argumento PARENTFIN { yyerror("Falta '}' en expresion lambda."); }
                | PARENTINIC INT ID PARENTFIN error bloque_ejec error PARENTINIC argumento PARENTFIN { yyerror("Faltan los delimitadores '{}' en expresion lambda."); }
                ;

argumento	: ID
  			| cte
  			;

%%

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
    //System.err.println("Error sintactico en linea " + lex.getLineaActual() + ": " + mensaje);
	if (ERR != null) ERR.add(linea, "Error sintactico: " + mensaje);

}
