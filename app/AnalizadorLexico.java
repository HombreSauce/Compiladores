package app;
import acciones_semanticas.*;
import funcionalidades.MiniTokenizador;
import java.util.Map;
public class AnalizadorLexico {

    private String ins;
    private int inicioIns;

    private static final Map<String, Integer> alfabeto = datos.Alfabeto.alfabeto;

    private final int[][] matEstados;
    private AccionSemantica[][] matAcciones;
    private int EstadoFinal = datos.MatrizTransicion.FINAL;

    private AnalizadorLexico(String ins){
        this.ins = ins;
        this.inicioIns = 0;;
        this.matEstados = datos.MatrizTransicion.MATRIZ_ESTADOS;
        this.matAcciones = datos.MatrizAccionSemantica.MATRIZ_AS;
    }

    public String getToken() { // tiene que devolver la direccion a la tabla de simbolos
        int estadoActual = 0;                         // Inicia en el estado inicial
        StringBuilder lexema = new StringBuilder();   // Inicia el lexema vacio
        String posTablaSimbolos = "fila 14 y tu puta madre";

        for (int i = inicioIns; i < ins.length(); i++) {      // Recorre las instrucciones 
            char simboloCrudo = ins.charAt(i);    // Caracter por caracter
            String simbolo = MiniTokenizador.miniTokenizar(simboloCrudo); // Convierte el simbolo leido a simbolo reconocible por el Alfabeto
            
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

    public static void main(String[] args){

        // Verificamos que el usuario pase un archivo como argumento
        //if (args.length < 1) {
        //    System.err.println("Uso: java AnalizadorLexico <archivo>");
        //    System.exit(1);
        //}

        //try {//esto carga lo que hay en ese path y lo mete en un string
        //    String contenido = Files.readString(Paths.get(args[0]));
        //    System.out.println("Contenido del archivo:");
        //    System.out.println(contenido);
            String contenido = "IF ( 3 > 2) THEN { x = 3I;} ELSE { x = 2I; }"; // Ejemplo            
            //creo la instancia del lexico con el programa leido como un string
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(contenido); 

            String token = analizadorLexico.getToken();
            System.out.println(token);


       //} catch (IOException e) {
       //    System.err.println("Error al leer el archivo: " + e.getMessage());
       //}
    }
}
