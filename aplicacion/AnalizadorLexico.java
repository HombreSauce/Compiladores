package aplicacion;
import acciones_semanticas.AccionSemantica;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import aplicacion.funcionalidades.MiniTokenizador;
import datos.*;
public class AnalizadorLexico {

    private String instrucciones;  // CODIGO QUE LEE EL ARCHIVO
    private ControlPosicion inicioInstruccion;
    private ControlPosicion lineaPosicion;


    private static final Alfabeto alfabeto = Alfabeto.getInstancia();

    private final int[][] matEstados;
    private AccionSemantica[][] matAcciones;
    private int EstadoFinal = MatrizTransicion.FINAL;

    private AnalizadorLexico(String instrucciones){
        this.instrucciones = instrucciones;
        inicioInstruccion = new ControlPosicion();
        this.matEstados = MatrizTransicion.MATRIZ_ESTADOS; 
        this.matAcciones = MatrizAccionSemantica.MATRIZ_AS;
        lineaPosicion = new ControlPosicion(); // inicia en cero porque no leyo nada
        lineaPosicion.incrementar(); // la primera linea es la 1
    }
    
    private void  controlSaltoLinea(char simboloCrudo) {
        if ( simboloCrudo == '\n' ) {
            lineaPosicion.incrementar();
        }  
    }

    public Token getToken() {

        int estadoActual = 0;                         // Inicia en el estado inicial
        StringBuilder lexema = new StringBuilder();   // Inicia el lexema vacio
        Token token = null;

        while (inicioInstruccion.getPosicion() < instrucciones.length()) {      // Recorre las instrucciones 
            char simboloCrudo = instrucciones.charAt(inicioInstruccion.getPosicion());    // Caracter por caracter
            String simbolo = MiniTokenizador.miniTokenizar(simboloCrudo); // Convierte el simbolo leido a simbolo reconocible por el Alfabeto
            int col = alfabeto.getColumna(simbolo); // Ubica el indice columna para la matriz de transicion utilizando el simbolo del Alfabeto

            if (estadoActual != EstadoFinal) {            
                // System.out.println("Estado actual: " + estadoActual + ", simbolo: '" + simboloCrudo + "' (" + simbolo + "), columna: " + col + " lexema-" + lexema +"-");
                token = matAcciones[estadoActual][col].ejecutar(simboloCrudo, lexema, inicioInstruccion, lineaPosicion.getPosicion()); // Ejecuta la accion semantica
                estadoActual = matEstados[estadoActual][col];
                inicioInstruccion.incrementar();   
                controlSaltoLinea(simboloCrudo); // Controla si hay un salto de linea
            } else {
                if (token == null) {
                    // Si llegó al estado final y el token es null quiere decir que no se leyó algo valido, vuelve al estado inicial
                    estadoActual = 0;
                } else {
                    break; // corta el bucle
                }
            }
        }
        if (token == null && inicioInstruccion.getPosicion() == instrucciones.length()) {
            char vacio = ' ';
            int col = alfabeto.getColumna("EOF");
            token = matAcciones[estadoActual][col].ejecutar(vacio, lexema, inicioInstruccion, lineaPosicion.getPosicion());
            inicioInstruccion.incrementar(); //Se incrementa porque la AS anterior devuelve el caracter que consumio de más, pero al ser el final de archivo no es necesario.
        }
        return token;
    }

    public static void main(String[] args){
        TablaIdentificadorToken tablaIdentificadorToken = TablaIdentificadorToken.getInstancia();
        TablaPalabraReservada tablaPalabraReservada = TablaPalabraReservada.getInstancia();
        TablaSimbolos tablaSimbolos = TablaSimbolos.getInstancia();

        // Verifica que el usuario pase un archivo como argumento
        if (args.length < 1) {
           System.err.println("Uso: java AnalizadorLexico <archivo>");
           System.exit(1);
        }

        try {
            // Carga lo que hay en el archivo a un string
            String contenido = Files.readString(Paths.get(args[0]));
            // String contenido = "&un string que no termina";
            System.out.println("Contenido del archivo:");
            System.out.println(contenido);
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(contenido); 
            Token token = null;

            while ((token = analizadorLexico.getToken()) != null) { 
                // Pide tokens hasta que se acabe el string (devuelve null)
                System.out.print("Token ID: " + token.getIDToken() + ". ");
                if (token.getIDToken() < 100) {
                    System.out.println("Palabra reservada: " + tablaPalabraReservada.getClave(token.getIDToken()));
                } else if (token.getIDToken() == 100) {
                    System.out.println("Identificador: " + token.getEntradaTS().getLexema());
                } else if (token.getIDToken() == 101) {
                    System.out.println("Constante entera: " + token.getEntradaTS().getLexema());
                } else if (token.getIDToken() == 102) {
                    System.out.println("Constante flotante: " + token.getEntradaTS().getLexema());
                } else if (token.getIDToken() == 103) {
                    System.out.println("Cadena multilinea: " + token.getEntradaTS().getLexema());
                } else {
                    System.out.println(tablaIdentificadorToken.getClave(token.getIDToken()));
                }
            }
            tablaSimbolos.mostrarTabla();
            System.out.println("Fin del análisis léxico.");
        } catch (IOException e) {
          System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
