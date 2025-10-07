package datos;

import java.util.ArrayList;
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

    //Este metodo se utiliza para borrar las entradas que se crearon y luego se descubrió que es un num negativo
    public void eliminarEntrada(String lexema, int nroLinea) {
        ArrayList<Integer> lineasLexema = tablaSimbolos.get(lexema).getNroLineas();
        if(lineasLexema.size() == 1 && lineasLexema.get(0) == nroLinea) { //si la unica linea que hay es la pasada por parametro se elimina la entrada
            tablaSimbolos.remove(lexema);
        }
    }

    public void mostrarTabla() {
        System.out.println("----- TABLA DE SIMBOLOS -----");
        for (Map.Entry<String, EntradaTablaSimbolos> entrada : tablaSimbolos.entrySet()) {
            System.out.println(entrada.getValue());
        }
        System.out.println("-----------------------------");
    }
}