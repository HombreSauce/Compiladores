package datos;

import java.util.HashMap;
import java.util.Map;

public final class Alfabeto {
    private static Alfabeto instancia;
    private static final Map<String, Integer> alfabeto = new HashMap<>();

    static {
        // clases
        alfabeto.put("L", 0);
        alfabeto.put("num", 1);
        alfabeto.put("%", 2);
        alfabeto.put("+", 3);
        alfabeto.put("*", 4);
        alfabeto.put("/", 5);
        alfabeto.put("-", 6);
        alfabeto.put(":", 7);
        alfabeto.put("=", 8);
        alfabeto.put("<", 9);
        alfabeto.put(">", 10);
        alfabeto.put("l", 11);
        alfabeto.put(".", 12);
        alfabeto.put("@", 13);
        alfabeto.put("&", 14);
        alfabeto.put(",", 18);
        alfabeto.put(";", 19);
        alfabeto.put("(", 20);
        alfabeto.put(")", 21);
        alfabeto.put("{", 22);
        alfabeto.put("}", 23);
        alfabeto.put("_", 24);
        alfabeto.put("I", 25);
        alfabeto.put("D", 26);
        alfabeto.put("!", 27);
        alfabeto.put("C", 28);
        alfabeto.put("EOF", 29);

        // blancos / control
        alfabeto.put("tab", 15);  // idem con '\t'
        alfabeto.put("salto", 16);  // usa "\\n" si tu miniTokenizar devuelve "\\n"
        alfabeto.put("espacio", 17);
    }

    public static Alfabeto getInstancia() {
        if (instancia == null) {
            instancia = new Alfabeto();
        }
        return instancia;
    }

    public int getColumna(String clave) {
        return alfabeto.get(clave);
    }
}
