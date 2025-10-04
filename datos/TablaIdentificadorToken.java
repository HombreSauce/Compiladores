package datos;

import java.util.HashMap;
import java.util.Map;

public class TablaIdentificadorToken {
    private static TablaIdentificadorToken instancia;
    private static final Map<String, Integer> mapa_token = new HashMap<>();

    static {
        int id = 100;
        mapa_token.put("ID", 270); //100
        mapa_token.put("CTEINT", 270); //101
        mapa_token.put("CTEFLOAT", id++); //102
        mapa_token.put("CTESTR", id++); //103
        mapa_token.put("+", id++); //104
        mapa_token.put("-", id++); //105
        mapa_token.put("*", id++); //106
        mapa_token.put("/", id++); //107
        mapa_token.put("=", id++); //108
        mapa_token.put(":=", id++); //109
        mapa_token.put(">", id++); //110
        mapa_token.put("<", id++); //111
        mapa_token.put(">=", id++); //112
        mapa_token.put("<=", id++); //113
        mapa_token.put("==", id++); //114
        mapa_token.put("=!", id++); //115
        mapa_token.put("(", id++); //116
        mapa_token.put(")", id++); //117
        mapa_token.put("{", id++); //118
        mapa_token.put("}", id++); //119
        mapa_token.put("_", id++); //120
        mapa_token.put(";", id++); //121
        mapa_token.put(",", id++); //122
        mapa_token.put("->", id++); //123
    }

    public static TablaIdentificadorToken getInstancia() {
        if (instancia == null) {
            instancia = new TablaIdentificadorToken();
        }
        return instancia;
    }

    public int getID(String clave) {
        return mapa_token.get(clave);
    }

    //solo utilizado para el print que piden en la parte 1
    public String getClave(int id) {
        for (Map.Entry<String, Integer> entry : mapa_token.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return null; // Retorna null si no se encuentra el id
    }
}
