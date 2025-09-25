package datos;

import java.util.Map;
import java.util.HashMap;

public class TablaPalabraReservada {
    private static TablaPalabraReservada instancia;
    private static final Map<String, Integer> TPR = new HashMap<>();

    static {
        TPR.put("if",     1);
        TPR.put("else",   2);
        TPR.put("return", 3);
        TPR.put("endif",  4);
        TPR.put("print",  5);
        TPR.put("int",    6);
        TPR.put("dfloat", 7);
        TPR.put("for",    8);
        TPR.put("from",   9);
        TPR.put("to",    10);
        TPR.put("lambda",11);
        TPR.put("cv",    12);
        TPR.put("trunc", 13);
    }

    public static TablaPalabraReservada getInstancia() {
        if (instancia == null) {
            instancia = new TablaPalabraReservada();
        }
        return instancia;
    }

    public int getID(String clave) {
        return TPR.getOrDefault(clave, -1); // Retorna -1 si no se encuentra la clave
    }

    public String mejorMatchPorPrefijo(String clave) {
        String mejorCoincidencia = null;
        int maxPrefijo = -1;

        for (String palabra : TPR.keySet()) {
            int prefijo = prefijoComun(clave, palabra);
            if (prefijo > maxPrefijo) {
                maxPrefijo = prefijo;
                mejorCoincidencia = palabra;
            }
        }

        return mejorCoincidencia;
    }

    private int prefijoComun(String a, String b) {
        int i = 0;
        while (i < a.length() && i < b.length() && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return i;
    }
}
