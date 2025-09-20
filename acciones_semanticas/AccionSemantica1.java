package acciones_semanticas;

import app.Token;

public class AccionSemantica1 implements AccionSemantica{

    @Override
    public Token ejecutar(char simbolo, int estadoActual, StringBuilder lexema) {
        lexema.delete(0, lexema.length());
        lexema.append(simbolo);
        return null;
    }
}
