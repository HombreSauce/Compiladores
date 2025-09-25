package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemanticaError1 implements AccionSemantica {
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigo) {
        System.err.println("Error Léxico: Símbolo no reconocido '" + simbolo + "' en la línea " + lineaCodigo + ". Será ignorado.");
        return null;
    }
    
}
