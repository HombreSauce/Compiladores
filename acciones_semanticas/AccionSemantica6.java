package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.TablaIdentificadorToken;

public class AccionSemantica6 implements AccionSemantica{
    private TablaIdentificadorToken tablaIDToken;
    
    public AccionSemantica6(TablaIdentificadorToken tablaIdentificadorToken) {
        this.tablaIDToken = tablaIdentificadorToken;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura) {
        lexema.append(simbolo);
        Token token = new Token(tablaIDToken.getID(lexema.toString()), null);
        return token;
    }
}
