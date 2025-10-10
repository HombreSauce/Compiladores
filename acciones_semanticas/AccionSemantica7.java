package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.TablaPalabraReservada;
import aplicacion.Parser;

public class AccionSemantica7 implements AccionSemantica{
    private TablaPalabraReservada tpr;

    public AccionSemantica7(TablaPalabraReservada tablaPalabraReservada) {
        this.tpr = tablaPalabraReservada;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        posicionLectura.decrementar();
        final String lx = lexema.toString();
        final int idToken = tpr.getID(lx);

        if (idToken == -1) {
            // error al archivo + consola
            Parser.logLexError("palabra reservada invalida: '" + lx + "'");

            // sugerencia (si aplica) como warning separado
            String sugerencia = tpr.mejorMatchPorPrefijo(lx);
            if (sugerencia != null && !sugerencia.isEmpty()) {
                Parser.logLexWarnAt(lineaCodigoActual, "Quizas quisiste escribir '" + sugerencia + "'?");
            }

            return null; // o tu estrategia de recuperación
        }
        Token token = new Token(tpr.getID(lexema.toString()), null);
        return token;
    }
}
