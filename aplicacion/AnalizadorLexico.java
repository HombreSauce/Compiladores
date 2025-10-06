package aplicacion;
import acciones_semanticas.AccionSemantica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import aplicacion.funcionalidades.MiniTokenizador;
import datos.*;

public class AnalizadorLexico {

    private String instrucciones;  // CODIGO QUE LEE EL ARCHIVO
    private ControlPosicion inicioInstruccion;
    private ControlPosicion lineaPosicion;  // Es el nro de linea actual


    private static final Alfabeto alfabeto = Alfabeto.getInstancia();

    private final int[][] matEstados;
    private AccionSemantica[][] matAcciones;
    private int EstadoFinal = MatrizTransicion.FINAL;

    public AnalizadorLexico(String pathArchivo){
        try {
            this.instrucciones = Files.readString(Paths.get(pathArchivo), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer la RUTA del archivo: " + pathArchivo, e);
        }
        inicioInstruccion = new ControlPosicion();
        this.matEstados = MatrizTransicion.MATRIZ_ESTADOS; 
        this.matAcciones = MatrizAccionSemantica.MATRIZ_AS;
        lineaPosicion = new ControlPosicion(); // inicia en cero porque no leyo nada
        lineaPosicion.incrementar(); // la primera linea es la 1
    }


    public int getLineaActual() {
        return lineaPosicion.getPosicion();
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
}
