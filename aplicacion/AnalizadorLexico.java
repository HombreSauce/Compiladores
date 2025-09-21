package aplicacion;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import datos.*;
import acciones_semanticas.*;

public class AnalizadorLexico {

    private String ins;
    private int inicioIns;
    private static Map<String, Integer> alfabeto;
    private int[][] matEstados;
    private AccionSemantica[][] matAcciones;
    private int EstadoFinal = datos.MatrizTransicion.FINAL;

    private AnalizadorLexico(String ins){
        this.ins = ins;
        this.inicioIns = 0;
        this.alfabeto = new HashMap<>();                                      // RELLENAR
        this.matEstados = datos.MatrizTransicion.MATRIZ_ESTADOS;
        this.matAcciones = datos.MatrizAccionSemantica.MATRIZ_AS;
    }

    // Convierte la letra a la letra esperada por el alfabeto (o null si no pertenece)
    private String miniTokenizar(String s) {
        return null;     
    }

    public String getToken() { // tiene que devolver la direccion a la tabla de simbolos
        int estadoActual = 0;                         // Inicia en el estado inicial
        StringBuilder lexema = new StringBuilder();   // Inicia el lexema vacio
        String posTablaSimbolos = "fila 14 y tu puta madre";

        for (int i = inicioIns; i < ins.length(); i++) {      // Recorre las instrucciones 
            String simboloCrudo = ins.substring(i, i + 1);    // Caracter por caracter
            String simbolo = miniTokenizar(simboloCrudo);     // Convierte el simbolo leido a simbolo reconocibimpole por el Alfabeto
            
            int col = alfabeto.get(simbolo);     // Ubicas el indice columna para la matriz de transicion 

            lexema.append(simboloCrudo);         // Lo agrego al lexema (if)

            if (estadoActual != EstadoFinal) {
                estadoActual = matEstados[estadoActual][col];
                // Si quisieras ejecutar acción semántica: // tambien hay q controlar errores aqui
                // AccionS a = matAcciones[estadoActual][col]; //////////////////ACA SE  GUARDA EN LA TABLA DE SIMBOLOS
                // if (a != null) tiraToken += a.ejecutar(...);
            } else {
                // Si llegaste a final, podrías cerrar token actual ejecutar alguna AS
                // Por ahora, simplemente cortamos y ponemos el inicioIns con el avance para que ya pueda consumir el sig token
                // token = que token es
                inicioIns = i-1; // -1 o sin nada depende de la AS, pero ya  se guarda lo avanzado
                posTablaSimbolos = /* tablaSimbolos.insertar(lexema.toString(), token) */ "puto";
                break; // corTA EL BUCLE
            }
        }
        return posTablaSimbolos;
    }

    public static void main(String[] args) {
        // Inicializá matEstados, matAcciones y alfabeto antes de usar

        //-- VERSION CON STRING
        String ins = "IF ( 3 > 2) THEN { x = 3I;} ELSE { x = 2I; }"; // Ejemplo
        String rutaArchivo = args[0];   //esto lee el path que esta en consola y lo mete en un string // ESTO REEMPLAZA AL INS
        
        //-- VERSION CON BIBLIOTECAS
        // Verificamos que el usuario pase un archivo como argumento
        //if (args.length < 1) {
        //    System.err.println("Uso: java AnalizadorLexico <ruta-archivo>");
        //    System.exit(1);
        //}

        AnalizadorLexico analizadorLexico = new AnalizadorLexico(ins); 
        
        String token = analizadorLexico.getToken();
        System.out.println(token);
    }
}
