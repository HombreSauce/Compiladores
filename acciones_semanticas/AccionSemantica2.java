package acciones_semanticas;

import app.Token;

public class AccionSemantica2 implements AccionSemantica {
    
    @Override
    public Token ejecutar(char simbolo, int estadoActual, StringBuilder lexema) {
        lexema.append(simbolo);
        return null;
    }
}
