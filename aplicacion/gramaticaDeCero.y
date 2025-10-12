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
// programa    : prog
//             ;

prog	: ID LLAVEINIC bloque LLAVEFIN             
        | ID bloque LLAVEFIN {yyerror("Falta '{' en declaración de programa");}
        | ID LLAVEINIC bloque {yyerror("Falta '}' en declaración de programa");}
        | LLAVEINIC bloque LLAVEFIN {yyerror("Falta identificador en declaración de programa");}
		;

/* ========= Bloques ========= */ /* Mezcla declarativas + ejecutables */   

bloque	: /* vacío */
  		| bloque sentencia
  		;

bloque_ejecutable   : bloque_ejecutable sentencia_ejec PUNTOYCOMA
                    | sentencia_ejec PUNTOYCOMA
                    | sentencia_ejec {yyerror("Falta ';' al final de la sentencia.");}
                    | bloque_ejecutable sentencia_ejec { yyerror("Falta ';' al final de la sentencia."); }
                    ;

/* ========= Sentencias ========= */
tipo    : INT
        ;


sentencia	: declaracion_variable
            | declaracion_con_asignacion
            | sentencia_ejec PUNTOYCOMA
            | sentencia_ejec { yyerror("Falta ';' al final de la sentencia."); }
  		    | declaracion_funcion
  			;

sentencia_ejec	: asign_simple
                | asign_multiple
                | bloque_if
                | bloque_for
                | sentencia_print
                | llamada_funcion
                ;

/* ========= Declaraciones ========= */

declaracion_variable    : tipo lista_ids PUNTOYCOMA {
                                                    if (!error_lista_ids) {
                                                        int linea = lex.getLineaActual();
                                                        for (String n : $2.sval.split(",\s")) {
                                                        SINT.add(linea, "Declaracion de variable: " + n);
                                                        }
                                                    } else {
                                                        error_lista_ids = false;  /* reset para la próxima */
                                                    }
                                                    }
                        | tipo lista_ids { yyerror("Error en declaración de variables, falta ';' al final."); }
                        //| tipo error PUNTOYCOMA {yyerror("Error en declaración de variables, falta ',' entre identificadores");};
                         ;


lista_ids   : ID { yyval = new ParserVal(((EntradaTablaSimbolos)$1.obj).getLexema()); }
            | lista_ids COMA ID { yyval = new ParserVal($1.sval + ", " + ((EntradaTablaSimbolos)$3.obj).getLexema()); }
            | lista_ids COMA error { yyerror("Falta identificador después de coma"); error_lista_ids = true; }
            // | lista_ids ID {yyerror("Error en declaración de variables, falta ',' entre identificadores");};
            // | error COMA {yyerror("Identificador invalido");}
			// | error ID {yyerror("Error: falta una coma entre identificadores en la lista de variables");}
  			;

declaracion_con_asignacion  : tipo ID ASIGN expresion PUNTOYCOMA { SINT.add(lex.getLineaActual(), "Declaracion de variable con asignacion"); }
                            | tipo ID ASIGN expresion { yyerror("Error en declaración de variable con asignación, falta ';' al final."); }
                            | tipo ID ASIGN error PUNTOYCOMA { yyerror("Error en declaración de variable con asignación, expresión inválida."); }
                            | tipo ID error expresion PUNTOYCOMA { yyerror("Error en declaración de variable con asignación, falta ':=' entre identificador y expresión."); }
                            | tipo error ASIGN expresion PUNTOYCOMA { yyerror("Error en declaración de variable con asignación, falta identificador después del tipo."); }
                            ;

/* ========= Funciones (declaración) ========= */
declaracion_funcion     : tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque return_sent LLAVEFIN {
                                                                                                                    String nombre = ((EntradaTablaSimbolos) val_peek(7).obj).getLexema();
                                                                                                                    SINT.add(lex.getLineaActual(), "Declaracion de funcion: " + nombre);
                                                                                                                    yyval = new ParserVal(nombre);
                                                                                                                   }
                        | declaracion_funcion_err
                        ;

declaracion_funcion_err : tipo PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN {yyerror("Error sintáctico: Falta nombre identificador de función");}
                        // ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN {yyerror("Error sintáctico: Falta tipo de función");}
                        // // | tipo ID lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN {yyerror("Error sintáctico: Falta paréntesis de apertura '(' en declaración de función");}
                        // | tipo ID PARENTINIC error PARENTFIN LLAVEINIC bloque LLAVEFIN {yyerror("Error sintáctico: Error en lista de parámetros formales");}
                        // | tipo ID PARENTINIC lista_params_formales error LLAVEINIC bloque LLAVEFIN {yyerror("Error sintáctico: Falta paréntesis de cierre ')' en declaración de función");}
                        // | tipo ID PARENTINIC lista_params_formales PARENTFIN error bloque LLAVEFIN {yyerror("Error sintáctico: Falta llave de apertura '{' en declaración de función");}
                        // | tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque error{yyerror("Error sintáctico: Falta llave de cierre '}' en declaración de función");}
                        // ;

lista_params_formales	: param_formal
						| lista_params_formales COMA param_formal
                        | lista_params_formales COMA error {yyerror("Error sintáctico: falta identificador despues de coma en parametro formal");}
                        // | lista_params_formales param_formal {yyerror("Error sintactico: falta coma entre parametros formales en declaracion de funcion");}
                        // DEBERIA ESTAR ESTE DE ARRIBA
						;

param_formal		: sem_pasaje_opt tipo ID            /* tema 24 */
            		| sem_pasaje_opt tipo error { yyerror("Falta identificador después de tipo en parámetro formal");}            
                    | sem_pasaje_opt ID { yyerror("Falta tipo en parámetro formal");}
					;

sem_pasaje_opt		: /* vacío */                
					| CV
					;

return_sent		: RETURN PARENTINIC expresion PARENTFIN PUNTOYCOMA{ SINT.add(lex.getLineaActual(), "Return"); }
        		| RETURN PARENTINIC expresion PARENTFIN { yyerror("Error en declaración de variables, falta ';' al final."); }
                | /* vacio */
  				;

/* ========= Asignaciones ========= */
/* Asignación simple y expresión aritmética SIN paréntesis de agrupación */	

asign_simple	: var_ref ASIGN expresion { SINT.add(lex.getLineaActual(), "Asignacion simple"); }
  				;

/* Tema 18 */ /* LHS puede tener más elementos que RHS.  RHS sólo constantes */

asign_multiple	: lista_vars IGUALUNICO lista_ctes { 
                    //System.out.println("n_var: " + n_var + ", n_cte: " + n_cte);
                    if (n_var == 1 && n_cte == 1) {
                        yyerror("Error sintactico: para asignación simple use ':=' en lugar de '='");
                    } else {
                        if (n_var < n_cte) {
                            yyerror("Error sintactico: más constantes que variables en la asignación");
                        } else {
                            // System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
                            SINT.add(lex.getLineaActual(), "Asignacion multiple");
                        }
                    }
					n_var = n_cte = 0;  /* reset para la próxima */
				}
				| IGUALUNICO lista_ctes { yyerror("Error sintactico: falta lista de variables antes del '='"); }
				| lista_vars IGUALUNICO error { yyerror("Error sintactico: falta lista de constantes después del '='");}
				// | lista_vars lista_ctes { yyerror("Error sintactico: falta '=' entre la lista de variables y la lista de constantes"); }
                // NO LO PIDE LA HOJA, POR AHORA NO ANDA
  				;

lista_ctes	: cte {n_cte++;}
  			| lista_ctes COMA cte {n_cte++;}
			| lista_ctes COMA error { yyerror("Error sintactico: falta una constante después de coma");}
			| lista_ctes cte { yyerror("Error sintactico: falta una coma entre constantes en la lista de constantes");}
  			;	

lista_vars	: var_ref {n_var++;}
            | lista_vars COMA var_ref {n_var++;}
            | lista_vars COMA error { yyerror("Error sintactico: falta identificador después de coma");}
            | lista_vars var_ref {yyerror("Error sintactico: falta una coma entre identificadores en la lista de variables");}
            ;

var_ref		: ID					/* tema 22 */
			| var_ref PUNTO ID
  			;	

/* ========= Expresiones aritméticas (sin '()' ) ========= */

expresion	: termino
            | expresion MAS termino
            | expresion MAS error { yyerror("Falta operando derecho después de '+' en expresión."); }
            | error MAS termino { yyerror("Falta operando izquierdo antes de '+' en expresión."); }
  			| expresion MENOS termino
            | expresion MENOS error { yyerror("Falta operando derecho después de '-' en expresión."); }
  			| error MENOS termino { yyerror("Falta operando izquierdo antes de '-' en expresión."); }
			;

termino		: factor
            | termino MUL factor
            | termino MUL error { yyerror("Falta operando derecho después de '*' en expresión."); }
            | error MUL factor { yyerror("Falta operando izquierdo antes de '*' en expresión."); }
  			| termino DIV factor
            | termino DIV error { yyerror("Falta operando derecho después de '/' en expresión."); }
            | error DIV factor { yyerror("Falta operando izquierdo antes de '/' en expresión."); }
            // | termino error factor { yyerror("Falta operador entre factores en expresión."); }
            // NO ANDA CON VARIABLES O VARIABLES Y CTES
			;

factor		: var_ref
  			// | llamada_funcion
  			| cte
 			;

/* ========= Constante ========= */

cte     : CTEFLOAT { yyval = $1; }
        | MENOS CTEFLOAT
            {
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)$2.obj;
            String neg = '-' + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea());
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = $2;  /* devolvés el mismo ParserVal */
            }
        | CTEINT
            {
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)$1.obj;
            String valor = ent.getLexema();
            valor = valor.substring(0, valor.length() - 1); /* quita la 'I' final */
            int num = Integer.parseInt(valor);
            if (num > 32767) {
                System.err.println("Error léxico: constante entera fuera de rango en línea "
                                + lex.getLineaActual() + ": " + num);
                tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            }
            yyval = $1;
            }
        | MENOS CTEINT
            {
            EntradaTablaSimbolos ent = (EntradaTablaSimbolos)$2.obj;
            String neg = '-' + ent.getLexema();
            tablaSimbolos.insertar(neg, ent.getUltimaLinea());
            tablaSimbolos.eliminarEntrada(ent.getLexema(), ent.getUltimaLinea());
            yyval = $2;
            }
        | CTESTR
            { yyval = $1; }
        ;

/* ========= If (selección) ========= */

bloque_if   : IF PARENTINIC condicion PARENTFIN rama_if ENDIF { 
					SINT.add(lex.getLineaActual(), "Sentencia if"); }
            | IF PARENTINIC condicion PARENTFIN rama_if ELSE rama_else ENDIF{
					SINT.add(lex.getLineaActual(), "Sentencia if");
					SINT.add(lex.getLineaActual(), "Sentencia else");}
            | bloque_if_error
            ;

bloque_if_error : IF condicion PARENTFIN rama_if ENDIF { yyerror("Falta '(' en sentencia if."); }
                | IF PARENTINIC condicion rama_if ENDIF { yyerror("Falta ')' en sentencia if."); }
                | IF condicion rama_if ENDIF { yyerror("Faltan los paréntesis en sentencia if."); }
                | IF PARENTINIC condicion PARENTFIN rama_if error { yyerror("Falta 'endif' al final del bloque if."); }
                | IF condicion PARENTFIN rama_if ELSE rama_else ENDIF { yyerror("Falta '(' en sentencia if."); }
                | IF PARENTINIC condicion rama_if ELSE rama_else ENDIF { yyerror("Falta ')' en sentencia if."); }
                | IF condicion rama_if ELSE rama_else ENDIF { yyerror("Faltan los paréntesis en sentencia if."); }
                | IF PARENTINIC condicion PARENTFIN rama_if ELSE rama_else error { yyerror("Falta 'endif' al final del bloque else."); }
                | IF rama_if ENDIF { yyerror("Falta el cuerpo de condicion en el if.");}
                | IF rama_if ELSE rama_else ENDIF { yyerror("Falta el cuerpo de condicion en el if.");}
                | IF PARENTINIC error PARENTFIN rama_if ENDIF { yyerror("Falta condicion en el if."); }
                | IF PARENTINIC error PARENTFIN rama_if ELSE rama_else ENDIF { yyerror("Falta condicion en el if."); }
                | IF PARENTINIC condicion PARENTFIN error {yyerror("Falta bloque del if");}
                |  IF PARENTINIC condicion PARENTFIN rama_if ELSE error {yyerror("Falta bloque del else");}
                ;

condicion   : expresion op_relacion expresion
            | expresion error expresion { yyerror("Falta comparador en la condicion."); }
            | error op_relacion expresion { yyerror("Falta operando izquierdo en la condicion."); }
            | expresion op_relacion error { yyerror("Falta operando derecho en la condicion."); }
            // | /* vacío */ { yyerror("Falta condicion en el if."); }
            ;

op_relacion     : MENOR 
                | MAYOR 
                | IGUAL 
                | DISTINTO 
                | MENORIGUAL 
                | MAYORIGUAL
                // | /* vacio */ {yyerror("Falta operador relacional en la condicion");}
                ;

rama_if : sentencia_ejec PUNTOYCOMA
        | LLAVEINIC bloque_ejecutable LLAVEFIN
        | LLAVEINIC LLAVEFIN  {yyerror("Falta sentencia en el bloque ejecutable del then");}
        // | /* vacio */ {yyerror("Falta bloque del then");}
        //HECHO EN IF ERROR
        ;

rama_else   : sentencia_ejec PUNTOYCOMA
            | LLAVEINIC bloque_ejecutable LLAVEFIN
            | LLAVEINIC LLAVEFIN  {yyerror("Falta sentencia en el bloque ejecutable del else");}
            // | /* vacio */ {yyerror("Falta bloque del else");}
            // HECHO EN IF ERROR
            ;

/* ========= For (tema 15) ========= */

bloque_for	: FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN rama_for { SINT.add(lex.getLineaActual(), "Sentencia for"); }
            | FOR error ID FROM CTEINT TO CTEINT PARENTFIN rama_for { yyerror("Falta '(' en sentencia for."); }
            | FOR PARENTINIC error FROM CTEINT TO CTEINT PARENTFIN rama_for { yyerror("Falta identificador en sentencia for."); }
            | FOR PARENTINIC ID error CTEINT TO CTEINT PARENTFIN rama_for { yyerror("Falta 'from' en sentencia for."); }
            | FOR PARENTINIC ID FROM error TO CTEINT PARENTFIN rama_for { yyerror("Falta constante entera después de 'from' en sentencia for."); }
            | FOR PARENTINIC ID FROM CTEINT error CTEINT PARENTFIN rama_for { yyerror("Falta 'to' en sentencia for."); }
            | FOR PARENTINIC ID FROM CTEINT TO error PARENTFIN rama_for { yyerror("Falta constante entera después de 'to' en sentencia for."); }
            | FOR PARENTINIC ID FROM CTEINT TO CTEINT error rama_for { yyerror("Falta ')' en sentencia for."); }
            | FOR error ID FROM CTEINT TO CTEINT error rama_for { yyerror("Faltan los parentesis en sentencia for."); }
            | FOR PARENTINIC ID FROM CTEINT TO CTEINT PARENTFIN error { yyerror("Falta bloque del for."); }
            //EL DE ARRIBA FUNCIONA A VECES, SI TIENE UN PUNTO Y COMA AL FINAL FUNCIONA 
            ;

rama_for	: sentencia_ejec //sin punto y coma porque ya lo pide la sentencia ejecutable
            | LLAVEINIC bloque_ejecutable LLAVEFIN
            | LLAVEINIC LLAVEFIN  {yyerror("Falta cuerpo en el bloque del for");}
            ;

/* ========= Print (tema 8) ========= */

sentencia_print	: PRINT PARENTINIC expresion PARENTFIN { SINT.add(lex.getLineaActual(), "Print"); }
                | PRINT PARENTINIC error PARENTFIN { yyerror("Falta argumento en sentencia print."); }
                | PRINT error expresion PARENTFIN { yyerror("Falta '(' en sentencia print."); }
                | PRINT PARENTINIC expresion { yyerror("Falta ')' en sentencia print."); }
                ;

/* ========= Invocaciones y retorno ========= */

/* Params reales pueden ser expr, lambda (tema 28) o trunc (expr) (tema 31) */

llamada_funcion	: ID PARENTINIC lista_params_reales PARENTFIN { SINT.add(lex.getLineaActual(), "Llamada a funcion"); }
                // | PARENTINIC lista_params_reales PARENTFIN{ yyerror("Llamada a función sin nombre");}
  				;

lista_params_reales	: param_real_map
                    | lista_params_reales COMA param_real_map
                    ;

/* Cada parámetro real debe mapear a un formal con '->' */
param_real_map	: parametro_real FLECHA ID
                | parametro_real FLECHA error { yyerror("Falta identificador después de '->' en parámetro real");}
                ;	

parametro_real	: expresion                    
  				| TRUNC PARENTINIC expresion PARENTFIN { SINT.add(lex.getLineaActual(), "Trunc"); }                		 /* tema 31 */
                | TRUNC PARENTINIC expresion { yyerror("Falta ')' en llamada a función con 'trunc'.");}
                | TRUNC error expresion PARENTFIN { yyerror("Falta '(' en llamada a función con 'trunc'.");}
                | TRUNC error expresion { yyerror("Faltan los paréntesis en llamada a función con 'trunc'.");}
  				| lambda_expr                                     	 /* tema 28 */
				;

/* ========= Lambda como parámetro (tema 28) ========= */

lambda_expr		: PARENTINIC tipo ID PARENTFIN LLAVEINIC bloque_ejecutable LLAVEFIN PARENTINIC argumento PARENTFIN { SINT.add(lex.getLineaActual(), "Lambda");}
                | PARENTINIC tipo ID PARENTFIN bloque_ejecutable LLAVEFIN PARENTINIC argumento PARENTFIN { yyerror("Falta '{' en expresión lambda."); }
                | PARENTINIC tipo ID PARENTFIN LLAVEINIC bloque_ejecutable PARENTINIC argumento PARENTFIN { yyerror("Falta '}' en expresión lambda."); }
                | PARENTINIC tipo ID PARENTFIN bloque_ejecutable PARENTINIC argumento PARENTFIN { yyerror("Faltan los delimitadores '{}' en expresión lambda."); }
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
static boolean error_lista_ids = false; //para controlar errores en lista_ids

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
