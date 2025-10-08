package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import aplicacion.Parser;
public class AccionSemanticaError4 implements AccionSemantica {

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        //Se asume que se olvidó el . antes de la D
        lexema.append(".D");
        Parser.logLexWarnAt(lineaCodigoActual, "Se esperaba '.' luego de '" + lexema.substring(0, lexema.length() - 2) + "', pero no se encontro. Se asume que se quiso escribir '" + lexema.toString() + "'");
        return null;
    }
    
}
