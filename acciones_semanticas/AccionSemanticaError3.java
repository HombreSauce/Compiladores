package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import aplicacion.Parser;


public class AccionSemanticaError3 implements AccionSemantica {
    private AccionSemantica AS8;

    public AccionSemanticaError3(AccionSemantica AS8) {
        this.AS8 = AS8;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        //Asumimos que el programador quizo escribir un entero y se olvido la I.
        posicionLectura.decrementar(); // Retrocede una posicion para empezar a reconocer otra cosa
        final String orig = lexema.toString();
        Parser.logLexWarnAt(lineaCodigoActual, "Se esperaba 'I' luego de la constante numerica '" + orig + "'. Se asume '" + orig + "I'");
        Token token = AS8.ejecutar(simbolo, lexema, posicionLectura, lineaCodigoActual); // Reutiliza la accion semantica 8 para reconocer el entero
        return token;
    }
    
}
