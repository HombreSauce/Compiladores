package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemantica10 implements AccionSemantica{
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura) {
        lexema.setLength(0);
        return null;
    }
}
