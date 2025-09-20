package datos;

import java.util.HashMap;
import java.util.Map;

public class Alfabeto {

    public static final Map<String, Integer> alfabeto = new HashMap<>();

    static {
        int idx = 0;
        alfabeto.put("L", idx++);       // letra mayúscula
        alfabeto.put("l", idx++);       // letra minúscula
        alfabeto.put("num", idx++);     // dígito

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
        alfabeto.put("espacio", idx++);
        alfabeto.put("\\n", idx++);
        alfabeto.put("tab", idx++);
    }

    private Alfabeto() {
        // constructor privado para que no se instancie
    }
}