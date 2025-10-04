package datos;

import java.util.Map;
import java.util.HashMap;

public class TablaPalabraReservada {
    private static TablaPalabraReservada instancia;
    private static final Map<String, Integer> TPR = new HashMap<>();

    static {
        TPR.put("if",     257);
        TPR.put("else",   258);
        TPR.put("endif",  259);
        TPR.put("print",  260);
        TPR.put("return", 261);
        TPR.put("int",    262);
        TPR.put("dfloat", 263);
        TPR.put("for",    264);
        TPR.put("from",   265);
        TPR.put("to",    266);
        TPR.put("lambda", 267);
        TPR.put("cv",    268);
        TPR.put("trunc", 269);
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
    
    //solo utilizado para el print que piden en la parte 1
    public String getClave(int id) {
        for (Map.Entry<String, Integer> entry : TPR.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return null; // Retorna null si no se encuentra el id
    }
}
