package datos;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private static final Map<String, EntradaTablaSimbolos> tabla = new HashMap<>();

    public EntradaTablaSimbolos obtener(String lexema) {
        return tabla.get(lexema);
    }

    public EntradaTablaSimbolos insertar(String lexema) {
        if (tabla.containsKey(lexema)) {
            return tabla.get(lexema);
        } else {
            EntradaTablaSimbolos nuevaEntrada = new EntradaTablaSimbolos(lexema);
            tabla.put(lexema, nuevaEntrada);
            return nuevaEntrada;
        }
    }
}