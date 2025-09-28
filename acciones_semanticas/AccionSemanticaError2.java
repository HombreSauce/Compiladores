package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemanticaError2 implements AccionSemantica {
    private String simboloEsperado;

    public AccionSemanticaError2(String simboloEsperado) {
        this.simboloEsperado = simboloEsperado;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        System.err.println("Linea " + lineaCodigoActual + ". Error Léxico: Se esperaba '" + simboloEsperado + "' luego de '" + lexema + "', pero se encontró '" + simbolo + "'.");
        return null;
    }
    
}
