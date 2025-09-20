package acciones_semanticas;

import app.Token;

public interface AccionSemantica {

    public abstract Token ejecutar(char simbolo, int estadoActual, StringBuilder lexema);
}
