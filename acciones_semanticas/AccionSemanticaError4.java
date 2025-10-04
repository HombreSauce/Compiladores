package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemanticaError4 implements AccionSemantica {

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        //Se asume que se olvidó el . antes de la D
        lexema.append(".D");
        System.err.println("Linea " + lineaCodigoActual + ". Warning Léxico: Se esperaba '.' luego de '" + lexema.substring(0, lexema.length() - 2) + "', pero no se encontró. Se asume que se quiso escribir '" + lexema.toString() + "'");
        return null;
    }
    
}
