package aplicacion;
import acciones_semanticas.AccionSemantica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import aplicacion.funcionalidades.MiniTokenizador;
import datos.*;
public class AnalizadorLexico {

    private String ins;  // CODIGO QUE LEE EL ARCHIVO
    private ControlPosicion inicioIns;
    private ControlPosicion lineaPos;


    private static final Alfabeto alfabeto = Alfabeto.getInstancia();

    private final int[][] matEstados;
    private AccionSemantica[][] matAcciones;
    private int EstadoFinal = MatrizTransicion.FINAL;

    private AnalizadorLexico(String ins){
        this.ins = ins;
        inicioIns = new ControlPosicion();
        this.matEstados = MatrizTransicion.MATRIZ_ESTADOS; 
        this.matAcciones = MatrizAccionSemantica.MATRIZ_AS;
        lineaPos = new ControlPosicion(); // inicia en cero porque no leyo nada
        lineaPos.incrementar(); // la primera linea es la 1
    }
    
    private void  controlSaltoLinea(char simboloCrudo) {
        if ( simboloCrudo == '\n' ) { //habría que poner '\r' tmb?
            lineaPos.incrementar();
        }  
    }

    public Token getToken() { // tiene que devolver la direccion a la tabla de simbolos  // TOMAR EN CUENTA EL FIN DE ARCHIVO

        int estadoActual = 0;                         // Inicia en el estado inicial
        StringBuilder lexema = new StringBuilder();   // Inicia el lexema vacio
        Token token = null;

        while (inicioIns.getPosicion() < ins.length()) {      // Recorre las instrucciones 
            char simboloCrudo = ins.charAt(inicioIns.getPosicion());    // Caracter por caracter
            String simbolo = MiniTokenizador.miniTokenizar(simboloCrudo); // Convierte el simbolo leido a simbolo reconocible por el Alfabeto
            int col = alfabeto.getColumna(simbolo);     // Ubicas el indice columna para la matriz de transicion 

            if (estadoActual != EstadoFinal) {            
                // System.out.println("Estado actual: " + estadoActual + ", simbolo: '" + simboloCrudo + "' (" + simbolo + "), columna: " + col + " lexema-" + lexema +"-");
                token = matAcciones[estadoActual][col].ejecutar(simboloCrudo, lexema, inicioIns, lineaPos.getPosicion()); // EJECUTA LA ACCION SEMANTICA
                estadoActual = matEstados[estadoActual][col];
                inicioIns.incrementar();   
                controlSaltoLinea(simboloCrudo); // CONTROL DE LINEA
            } else {
                break; // corta el bucle
            }
        }
        if (token == null && inicioIns.getPosicion() == ins.length()) {
            char vacio = ' ';
            int col = alfabeto.getColumna("EOF");
            token = matAcciones[estadoActual][col].ejecutar(vacio, lexema, inicioIns, lineaPos.getPosicion()); // EJECUTA LA ACCION SEMANTICA
            inicioIns.incrementar(); //se incrementa porque la AS anterior devuelve el caracter que consumio de más, pero al ser el final de archivo no es necesario.
        }
        return token;
    }

    public static void main(String[] args){
        // Verificamos que el usuario pase un archivo como argumento
        if (args.length < 1) {
           System.err.println("Uso: java AnalizadorLexico <archivo>");
           System.exit(1);
        }

        try {//esto carga lo que hay en ese path y lo mete en un string
            String contenido = Files.readString(Paths.get(args[0]));
            // String contenido = "if HOLA else 4I";
            System.out.println("Contenido del archivo:");
            System.out.println(contenido);
            //creo la instancia del lexico con el programa leido como un string
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(contenido); 
            Token token = null;

            while ((token = analizadorLexico.getToken()) != null) { // Pido tokens hasta que se acabe el string (devuelve null)
                System.out.println("Token ID: " + token.getIDToken());
                if (token.getIDToken() == 101 || token.getIDToken() == 100 || token.getIDToken() == 102) {
                    System.out.println("Lexema: '" + token.getEntradaTS().getLexema());
                    ArrayList<Integer> lineas = token.getEntradaTS().getNroLineas();
                    System.out.println("  Aparece en las líneas: " + lineas);
                }
            }
            System.out.println("Fin del análisis léxico.");
        } catch (IOException e) {
          System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
