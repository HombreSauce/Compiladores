package datos;

import java.util.HashMap;
import java.util.Map;

public final class Alfabeto {

    public static final Map<String, Integer> alfabeto = new HashMap<>();

    static {
        int idx = 0;
        // clases
        alfabeto.put("L", idx++);
        alfabeto.put("l", idx++);
        alfabeto.put("num", idx++);

        // símbolos individuales
        alfabeto.put("+", idx++);
        alfabeto.put("-", idx++);
        alfabeto.put("=", idx++);
        alfabeto.put("*", idx++);
        alfabeto.put("/", idx++);
        alfabeto.put(".", idx++);
        alfabeto.put(",", idx++);
        alfabeto.put(";", idx++);
        alfabeto.put(":", idx++);
        alfabeto.put(">", idx++);
        alfabeto.put("<", idx++);
        alfabeto.put("!", idx++);
        alfabeto.put("_", idx++);
        alfabeto.put("@", idx++);
        alfabeto.put("&", idx++);
        alfabeto.put("(", idx++);
        alfabeto.put(")", idx++);
        alfabeto.put("{", idx++);
        alfabeto.put("}", idx++);
        alfabeto.put("%", idx++);

        // blancos / control
        alfabeto.put("espacio", idx++);
        alfabeto.put("salto", idx++);  // usa "\\n" si tu miniTokenizar devuelve "\\n"
        alfabeto.put("tab", idx++);  // idem con '\t'
    }
}
