package datos;

import java.util.HashMap;
import java.util.Map;

public class TablaIdentificadorToken {
    private static TablaIdentificadorToken instancia;
    private static final Map<String, Integer> mapa_token = new HashMap<>();

    static {
        int id = 100;
        mapa_token.put("ID", id++);
        mapa_token.put("CTE", id++);
        mapa_token.put("+", id++);
        mapa_token.put("-", id++);
        mapa_token.put("*", id++);
        mapa_token.put("/", id++);
        mapa_token.put("=", id++);
        mapa_token.put(":=", id++);
        mapa_token.put(">", id++);
        mapa_token.put("<", id++);
        mapa_token.put(">=", id++);
        mapa_token.put("<=", id++);
        mapa_token.put("==", id++);
        mapa_token.put("=!", id++);
        mapa_token.put("(", id++);
        mapa_token.put(")", id++);
        mapa_token.put("{", id++);
        mapa_token.put("}", id++);
        mapa_token.put("_", id++);
        mapa_token.put(";", id++);
        mapa_token.put(",", id++);
        mapa_token.put("->", id++);
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
}
