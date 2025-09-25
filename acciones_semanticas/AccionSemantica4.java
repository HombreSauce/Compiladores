package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.TablaIdentificadorToken;

public class AccionSemantica4 implements AccionSemantica{
    private TablaIdentificadorToken tablaIDToken;

    public AccionSemantica4(TablaIdentificadorToken tablaIdentificadorToken) {
        this.tablaIDToken = tablaIdentificadorToken;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        posicionLectura.decrementar();
        Token token = new Token(tablaIDToken.getID(lexema.toString()), null);
        return token;
    }
}
