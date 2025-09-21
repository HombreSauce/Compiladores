package datos;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private static TablaSimbolos instancia;
    private static final Map<String, EntradaTablaSimbolos> tablaSimbolos = new HashMap<>();

    public static TablaSimbolos getInstancia() {
        if (instancia == null) {
            instancia = new TablaSimbolos();
        }
        return instancia;
    }


    public EntradaTablaSimbolos obtener(String lexema) {
        return tablaSimbolos.get(lexema);
    }

    public EntradaTablaSimbolos insertar(String lexema) {
        if (tablaSimbolos.containsKey(lexema)) {
            return tablaSimbolos.get(lexema);
        } else {
            EntradaTablaSimbolos nuevaEntrada = new EntradaTablaSimbolos(lexema);
            tablaSimbolos.put(lexema, nuevaEntrada);
            return nuevaEntrada;
        }
    }
}