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
        return TPR.get(clave);
    }
}

// Esto se usa asi:
// if (TablaPalabraReservada.TPR.contains(lexema)) {
//      es palabra reservada
// }