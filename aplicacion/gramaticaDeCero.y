%start prog

%{
	import aplicacion.AnalizadorLexico;
	import aplicacion.Token;
	import datos.TablaSimbolos;
	import datos.EntradaTablaSimbolos;
	import datos.TablaIdentificadorToken;
	import datos.TablaPalabraReservada;
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
prog	: ID LLAVEINIC bloque LLAVEFIN             
		;

/* ========= Bloques ========= */ /* Mezcla declarativas + ejecutables */   

bloque	: /* vacío */
  		| bloque sentencia
  		;

/* ========= Sentencias ========= */
tipo    : INT
        ;


sentencia	: sentencia_ejec PUNTOYCOMA
            | sentencia_ejec { yyerror("Falta ';' al final de la sentencia."); }
  			//  tipo lista_ids PUNTOYCOMA
            // | tipo lista_ids error { yyerror("Error en declaración de variables, falta ';' al final."); }
  		    | declaracion_funcion
  			;

declaracion_variable    :  tipo lista_ids
                        ;

sentencia_ejec	: declaracion_variable
                // | asign_simple
                ;

lista_ids	: var_ref
 			| lista_ids COMA var_ref
			| lista_ids COMA error { yyerror("Error: falta identificador después de coma");}
			| lista_ids var_ref {yyerror("Error: falta una coma entre identificadores en la lista de variables");}
  			;

var_ref		: ID					/* tema 22 */
			| var_ref PUNTO ID
  			;	

/* ========= Funciones (declaración) ========= */
declaracion_funcion     : tipo ID PARENTINIC lista_params_formales PARENTFIN LLAVEINIC bloque LLAVEFIN
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
                        | lista_params_formales param_formal {yyerror("Error sintactico: falta coma entre parametros formales en declaracion de funcion");}
						;

param_formal		: sem_pasaje_opt tipo ID            /* tema 24 */
            		| sem_pasaje_opt tipo error { yyerror("Falta identificador después de tipo en parámetro formal");}            
                    | sem_pasaje_opt ID { yyerror("Falta tipo en parámetro formal");}
					;

sem_pasaje_opt		: /* vacío */                
					| CV
					;

// /* ========= Asignaciones ========= */
// /* Asignación simple y expresión aritmética SIN paréntesis de agrupación */	

// asign_simple	: var_ref ASIGN expresion {System.out.println("Asignación válida");}
//   				;

// // /* Tema 18 */ /* LHS puede tener más elementos que RHS.  RHS sólo constantes */

// // asign_multiple	: lista_ids IGUALUNICO lista_ctes { 
// //                     System.out.println("n_var: " + n_var + ", n_cte: " + n_cte);
// //                     if (n_var == 1 && n_cte == 1) {
// //                         yyerror("Error: para asignación simple use ':=' en lugar de '='");
// //                     } else {
// //                         if (n_var < n_cte) {
// //                             yyerror("Error: más constantes que variables en la asignación");
// //                         } else {
// //                             System.out.println("Asignación válida (" + n_var + ", " + n_cte + ")");
// //                         }
// //                     }
// // 					n_var = n_cte = 0;  /* reset para la próxima */
// // 				}
// // 				| IGUALUNICO lista_ctes { yyerror("Error: falta lista de variables antes del '='"); }
// // 				| lista_ids lista_ctes { yyerror("Error: falta '=' entre la lista de variables y la lista de constantes"); }
// // 				| lista_ids IGUALUNICO error { yyerror("Error: falta lista de constantes después del '='");}
// //   				;

// // lista_ctes	: cte {n_cte++;}
// //   			| lista_ctes COMA cte {n_cte++;}
// // 			| lista_ctes COMA error { yyerror("Error: falta una constante después de coma");}
// //   			;	

// /* ========= Expresiones aritméticas (sin '()' ) ========= */

// expresion	: termino
//             | expresion MAS termino
//             // | expresion MAS error { yyerror("Falta operando derecho después de '+' en expresión."); }
//   			| expresion MENOS termino
//             // | expresion MENOS error { yyerror("Falta operando derecho después de '-' en expresión."); }
//             // | expresion termino { yyerror("Falta operador entre factores en expresión."); }
//             // | expresion_error
// 			;

// termino		: factor
//             | termino MUL factor
//             // | termino MUL error { yyerror("Falta operando derecho después de '*' en expresión."); }
//   			| termino DIV factor
//             // | termino DIV error { yyerror("Falta operando derecho después de '/' en expresión."); }
// 			;

// factor		: var_ref
//   			// | llamada_funcion
//   			| cte
//  			;

// /* ========= Constante ========= */

// cte		: CTEFLOAT //no es necesario chequear el rango de los flotantes positivos ni negativos porque ya lo hace la AS9
// 		| MENOS CTEFLOAT {
// 			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)$2.obj;
// 			String valor_negativo = '-' + entrada.getLexema();
// 			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
// 			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); //eliminamos la entrada del positivo que se creo en el lexico
// 			yyval = $2; //se reduce por CTEFLOAT
// 		}
// 		| CTEINT {
// 			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)$1.obj;
// 			String valor = entrada.getLexema();
// 			valor = valor.substring(0, valor.length() - 1); //nos quedamos con el numero sin el I final
// 			int num = Integer.parseInt(valor);
// 			int max = 32767;
// 			//al ser positivo debemos chequear el maximo
// 			if (num > max) {
// 				System.err.println("Error léxico: constante entera fuera de rango en línea " + lex.getLineaActual() + ": " + num);
// 				tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea());
// 			}

// 			yyval = $1;
// 		}
// 		| MENOS CTEINT {
// 			EntradaTablaSimbolos entrada = (EntradaTablaSimbolos)$2.obj;
// 			String valor_negativo = '-' + entrada.getLexema();
// 			tablaSimbolos.insertar(valor_negativo, entrada.getUltimaLinea());
// 			tablaSimbolos.eliminarEntrada(entrada.getLexema(), entrada.getUltimaLinea()); //eliminamos la entrada del positivo que se creo en el lexico

// 			yyval = $2;
// 		}
// 		| CTESTR
//   		;

%%

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
