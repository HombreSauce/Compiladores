package datos;

import java.util.HashMap;
import java.util.Map;

public class TablaIdentificadorToken {
    private static TablaIdentificadorToken instancia;
    private static final Map<String, Integer> mapa_token = new HashMap<>();

    static {
        int id = 100;
        mapa_token.put("ID", 269); 
        mapa_token.put("CTEINT", 270); 
        mapa_token.put("CTEFLOAT", 271); 
        mapa_token.put("CTESTR", 272); 
        mapa_token.put(":=", 273); 
        mapa_token.put("->", 274); 
        mapa_token.put("==", 275); 
        mapa_token.put("=!", 276); 
        mapa_token.put("<=", 277);
        mapa_token.put(">=", 278);
        mapa_token.put("+", 279); 
        mapa_token.put("*", 280);
        mapa_token.put("/", 281); 
        mapa_token.put("-", 282); 
        mapa_token.put("=", 283); 
        mapa_token.put(">", 284);
        mapa_token.put("<", 285); 
        mapa_token.put(".", 286); 
        mapa_token.put(",", 287); 
        mapa_token.put(";", 288); 
        mapa_token.put("(", 289);
        mapa_token.put(")", 290); 
        mapa_token.put("{", 291); 
        mapa_token.put("}", 292); 
        //mapa_token.put("_", 2);  PARCHE ARREGLA DSP
        
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

    // HAY QUE VER COMO DEVOLVER CHAR
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
