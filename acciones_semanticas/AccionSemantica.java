package acciones_semanticas;

import aplicacion.Token;

public interface AccionSemantica {

    public abstract Token ejecutar(char simbolo, int estadoActual, StringBuilder lexema);
}
