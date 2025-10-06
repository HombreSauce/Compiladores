package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;

public class AccionSemanticaError3 implements AccionSemantica {
    private AccionSemantica AS8;

    public AccionSemanticaError3(AccionSemantica AS8) {
        this.AS8 = AS8;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        //Asumimos que el programador quizo escribir un entero y se olvido la I.
        posicionLectura.decrementar(); // Retrocede una posicion para empezar a reconocer otra cosa
        Token token = AS8.ejecutar(simbolo, lexema, posicionLectura, lineaCodigoActual); // Reutiliza la accion semantica 8 para reconocer el entero
        System.out.println("Linea " + lineaCodigoActual + ". Warning Lexico: Se esperaba 'I' luego de la constante numerica '" + lexema.toString() + "', pero no se encontro. Se asume que se quiso escribir '" + lexema.toString() + "'");
        return token;
    }
    
}
