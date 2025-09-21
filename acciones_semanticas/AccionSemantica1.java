package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemantica1 implements AccionSemantica{ 
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura) {
        lexema.delete(0, lexema.length());
        lexema.append(simbolo);
        return null;
    }
}
