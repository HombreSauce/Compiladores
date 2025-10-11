package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemantica12 implements AccionSemantica{
    AccionSemantica AS4;
    AccionSemantica AS9;

    public AccionSemantica12(AccionSemantica AS4, AccionSemantica AS9) {
        this.AS4 = AS4;
        this.AS9 = AS9;
    }
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        if (lexema.length() == 1) { //Es solo un punto
            return AS4.ejecutar(simbolo, lexema, posicionLectura, lineaCodigoActual);
        } else {
            return AS9.ejecutar(simbolo, lexema, posicionLectura, lineaCodigoActual);
        }
    }


}
