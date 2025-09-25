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

    public EntradaTablaSimbolos insertar(String lexema, int nroLinea) {
        if (tablaSimbolos.containsKey(lexema)) {
            EntradaTablaSimbolos entrada = tablaSimbolos.get(lexema);
            entrada.agregarLinea(nroLinea);
            return tablaSimbolos.get(lexema);
        } else {
            EntradaTablaSimbolos nuevaEntrada = new EntradaTablaSimbolos(lexema, nroLinea);
            tablaSimbolos.put(lexema, nuevaEntrada);
            return nuevaEntrada;
        }
    }
}