package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import aplicacion.Parser;

public class AccionSemanticaError2 implements AccionSemantica {
    private String simboloEsperado;

    public AccionSemanticaError2(String simboloEsperado) {
        this.simboloEsperado = simboloEsperado;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        Parser.logLexError("Se esperaba '" + simboloEsperado + "' luego de '" + lexema + "', pero se encontro '" + simbolo + "'.");
        return null;
    }
    
}
