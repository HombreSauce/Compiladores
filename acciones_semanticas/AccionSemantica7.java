package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.TablaPalabraReservada;

public class AccionSemantica7 implements AccionSemantica{
    private TablaPalabraReservada tpr;

    public AccionSemantica7(TablaPalabraReservada tablaPalabraReservada) {
        this.tpr = tablaPalabraReservada;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        posicionLectura.decrementar();
        int idToken = tpr.getID(lexema.toString());
        if (idToken == -1) {
            System.out.println("Error Léxico: Palabra reservada inválida '" + lexema.toString() + "' en la línea " + lineaCodigoActual);
            System.out.println("Quizás quisiste escribir: '" + tpr.mejorMatchPorPrefijo(lexema.toString()) + "'");
            return null; // o lanzar excepción
        }
        Token token = new Token(tpr.getID(lexema.toString()), null);
        return token;
    }
}
