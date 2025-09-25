package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemanticaError3 implements AccionSemantica {
    private AccionSemantica AS8;

    public AccionSemanticaError3(AccionSemantica AS8) {
        this.AS8 = AS8;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigo) {
        //Asumimos que el programador quizo escribir un entero y se olvido la I.
        posicionLectura.decrementar(); // Retrocede una posicion para empezar a reconocer otra cosa
        Token token = AS8.ejecutar(simbolo, lexema, posicionLectura, lineaCodigo); // Reutiliza la accion semantica 8 para reconocer el entero
        System.out.println("Warning Léxico: Se esperaba 'I' luego de la constante numérica '" + lexema.toString() + "', pero no se encontró en la línea " + lineaCodigo + ". Se asume que se quiso escribir '" + lexema.toString() + "'");
        return token;
    }
    
}
