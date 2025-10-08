package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import aplicacion.Parser;

public class AccionSemanticaError1 implements AccionSemantica {
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        Parser.logLexError("Simbolo no reconocido: '" + simbolo + "'. Sera ignorado.");
        return null;
    }
    
}
