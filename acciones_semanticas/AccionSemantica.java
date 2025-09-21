package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public interface AccionSemantica {

    public abstract Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura);
}
