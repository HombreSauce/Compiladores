import java.util.HashMap;
import java.util.Map;

public class AnalizadorLexico {

    private static final int ESTADO_FINAL = 13;

    private static int[][] matEstados;       // CARGAR LA MATRIZ CON EL CSV
    private static AccionS[][] matAcciones;  // lo mismo
    private static Map<String, Integer> alfabeto;

    private int inicioIns;
    private final String ins;

    private AnalizadorLexico(String ins){
        this.ins = ins;
        this.inicioIns = 0;
        this.alfabeto = new HashMap<>();       // TODO: poblar alfabeto.put("L", idx), etc.
        this.matEstados = new int[20][50];     // TODO: tamaños reales + carga CSV
        this.matAcciones = new AccionS[20][50];
    }


    // Convierte la letra a la letra esperada por el alfabeto (o null si no pertenece)
    private String miniTokenizar(String s) {
        // TODO: implementar de verdad. Ejemplo tonto:
        if (s.matches("[A-Z]")) return "L";  // mayúscula → L
        if (s.matches("[0-9]")) return "D";  // dígito → D
        if (s.equals(" ")) return "WS";      // espacio
        return null;                         // símbolo no reconocido
    }

    public static String getToken() { // tiene que devolver la direccion a la tabla de simbolos
        int estadoActual = 0;                   1     // Inicia en el estado inicial
        StringBuilder lexema = new StringBuilder();   // Inicia el lexema vacio
        int posTablaSimbolos = 0;

        for (int i = inicioIns; i < ins.length(); i++) {      // Recorre las instrucciones 
            String simboloCrudo = ins.substring(i, i + 1);    // Caracter por caracter
            String simbolo = miniTokenizar(simboloCrudo);     // Convierte el simbolo leido a simbolo reconocibble por el Alfabeto
            
            int col = alfabeto.get(simbolo);     // Ubicas el indice columna para la matriz de transicion 

            lexema.append(simboloCrudo);         // Lo agrego al lexema

            if (estadoActual != ESTADO_FINAL) {
                estadoActual = matEstados[estadoActual][col];
                // Si quisieras ejecutar acción semántica: // tambien hay q controlar errores aqui
                // AccionS a = matAcciones[estadoActual][col]; //////////////////ACA SE  GUARDA EN LA TABLA DE SIMBOLOS
                // if (a != null) tiraToken += a.ejecutar(...);
            } else {
                // Si llegaste a final, podrías cerrar token actual ejecutar alguna AS
                // Por ahora, simplemente cortamos y ponemos el inicioIns con el avance para que ya pueda consumir el sig token
                // token = que token es
                inicioIns = i-1; // -1 o sin nada depende de la AS, pero ya  se guarda lo avanzado
                posTablaSimbolos = /* tablaSimbolos.insertar(lexema.toString(), token) */ 0;
                break; // corTA EL BUCLE
            }
        }
        return posTablaSimbolos;
    }

    public static void main(String[] args) {
        // Inicializá matEstados, matAcciones y alfabeto antes de usar
        String ins = "IF ( 3 > 2) THEN { x = 3I;} ELSE { x = 2I; }"; // Ejemplo
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(ins); 
        String token = analizadorLexico.getToken();
        System.out.println(token);
    }
}
