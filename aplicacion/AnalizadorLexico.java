package aplicacion;
import acciones_semanticas.AccionSemantica;
import java.util.Map;
import aplicacion.funcionalidades.MiniTokenizador;
import datos.*;
public class AnalizadorLexico {

    private String ins;  // CODIGO QUE LEE EL ARCHIVO
    private ControlPosicion inicioIns;

    private static final Map<String, Integer> alfabeto = Alfabeto.alfabeto; // MIRAR DE PONER UN GET INSTANCIA

    private final int[][] matEstados;
    private AccionSemantica[][] matAcciones;
    private int EstadoFinal = MatrizTransicion.FINAL;

    private AnalizadorLexico(String ins){
        this.ins = ins;
        inicioIns = new ControlPosicion();
        this.matEstados = MatrizTransicion.MATRIZ_ESTADOS; 
        this.matAcciones = MatrizAccionSemantica.MATRIZ_AS;
    }

    public Token getToken() { // tiene que devolver la direccion a la tabla de simbolos  // TOMAR EN CUENTA EL FIN DE ARCHIVO

        int estadoActual = 0;                         // Inicia en el estado inicial
        StringBuilder lexema = new StringBuilder();   // Inicia el lexema vacio
        Token token = null;

        for (int i = inicioIns.getPosicion(); i < ins.length(); i++) {      // Recorre las instrucciones // MIRAR DE PASAR A WHILE
            char simboloCrudo = ins.charAt(inicioIns.getPosicion());    // Caracter por caracter
            String simbolo = MiniTokenizador.miniTokenizar(simboloCrudo); // Convierte el simbolo leido a simbolo reconocible por el Alfabeto
            int col = alfabeto.get(simbolo);     // Ubicas el indice columna para la matriz de transicion 

            if (estadoActual != EstadoFinal) {
                //System.out.println("Estado actual: " + estadoActual + ", simbolo: '" + simboloCrudo + "' (" + simbolo + "), columna: " + col + " lexema-" + lexema +"-");
                token = matAcciones[estadoActual][col].ejecutar(simboloCrudo, lexema, inicioIns); // EJECUTA LA ACCION SEMANTICA
                estadoActual = matEstados[estadoActual][col];
                inicioIns.incrementar();
            } else {
                //System.out.println("Estado actual: " + estadoActual + ", simbolo: '" + simboloCrudo + "' (" + simbolo + "), columna: " + col + " lexema-" + lexema +"-");
                break; // corta el bucle
            }
        }
        return token;
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
        //                       109,101,116,117
            String contenido = "if ( 3I > 2I) { X = 3I;} else { X := 2I; }"; // Ejemplo            
            //creo la instancia del lexico con el programa leido como un string
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(contenido); 

            for (int i = 0; i < 30; i++) { // Pido tokens hasta que se acabe el string
                Token token = analizadorLexico.getToken();
                if (token != null) {
                    System.out.println("Token: " + token.getIDToken());
                } else {
                    System.out.println("Fin del análisis léxico.");
                    break;
                }

            }

       //} catch (IOException e) {
       //    System.err.println("Error al leer el archivo: " + e.getMessage());
       //}
    }
}
