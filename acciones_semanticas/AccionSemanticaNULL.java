package acciones_semanticas;
import aplicacion.Token;
import aplicacion.ControlPosicion;


public class AccionSemanticaNULL implements AccionSemantica {
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura) {
        return null;
    }
}