package datos;

import java.util.ArrayList;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public EntradaTablaSimbolos insertar(String lexema, int nroLinea, String categoria) {
        if (tablaSimbolos.containsKey(lexema)) {
            EntradaTablaSimbolos entrada = tablaSimbolos.get(lexema);
            entrada.agregarLinea(nroLinea);
            return tablaSimbolos.get(lexema);
        } else {
            EntradaTablaSimbolos nuevaEntrada = new EntradaTablaSimbolos(lexema, nroLinea, categoria);
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

//Para el archivo de la tabla de simbolos
    private static String sanitizar(String s) {
        if (s == null) return "";
        return s.replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
    }

    private static String trunc(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        if (max <= 1) return s.substring(0, max);
        return s.substring(0, max-1) + "…";
    }



    public void mostrarTabla(PrintWriter pw) {
        // Encabezado de columnas (el encabezado grande ya lo escribís en TS_out)
        pw.printf("%-30s | %-12s | %-8s | %s%n", "Lexema", "Categoria", "Ocurr.", "Lineas");
        pw.println("------------------------------ | ------------ | -------- | ------------------------------");

        tablaSimbolos.entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // ordena por lexema
            .forEach(e -> {
                EntradaTablaSimbolos ets = e.getValue();
                String lex    = sanitizar(ets.getLexema());
                String cat    = ets.getCategoria();
                int ocurr     = ets.getNroLineas().size();
                String lineas = ets.getNroLineas().stream()
                                .sorted()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "));
                pw.printf("%-30s | %-12s | %8d | %s%n", trunc(lex, 30), cat, ocurr, lineas);
            });

        pw.flush();
    }
}