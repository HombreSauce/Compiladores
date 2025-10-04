package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemanticaError1 implements AccionSemantica {
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        System.err.println("Linea " + lineaCodigoActual + ": Error Léxico: Símbolo no reconocido '" + simbolo + "'. Será ignorado.");
        return null;
    }
    
}
