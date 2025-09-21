package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.TablaIdentificadorToken;

public class AccionSemantica5 implements AccionSemantica{
    private TablaIdentificadorToken tablaIDToken;
    
    public AccionSemantica5(TablaIdentificadorToken tablaIdentificadorToken) {
        this.tablaIDToken = tablaIdentificadorToken;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura) {
        StringBuilder aux = new StringBuilder(); 
        aux.append(simbolo);
        Token token = new Token(tablaIDToken.getID(aux.toString()), null);
        return token;
    }
}
