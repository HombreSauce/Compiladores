package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemanticaError2 implements AccionSemantica {
    private String simboloEsperado;

    public AccionSemanticaError2(String simboloEsperado) {
        this.simboloEsperado = simboloEsperado;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigo) {
        System.err.println("Error Léxico: Se esperaba '" + simboloEsperado + "' luego de '" + lexema + "', pero se encontró '" + simbolo + "' en la línea " + lineaCodigo);
        return null;
    }
    
}
