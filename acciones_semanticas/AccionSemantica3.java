package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.EntradaTablaSimbolos;
import datos.TablaIdentificadorToken;
import datos.TablaSimbolos;

public class AccionSemantica3 implements AccionSemantica{ 
    private TablaSimbolos tablaSimbolos;
    private TablaIdentificadorToken tablaIDToken;

    public AccionSemantica3(TablaSimbolos TS, TablaIdentificadorToken tablaIdentificadorToken) {
        tablaSimbolos = TS;
        tablaIDToken = tablaIdentificadorToken;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura) {
        posicionLectura.decrementar(); //PERO ESTA MIERDA NO FUNCIONA
        if (lexema.length() > 20) {
            lexema.setLength(20);
        }
        EntradaTablaSimbolos entrada = tablaSimbolos.insertar(lexema.toString());
        Token token = new Token(tablaIDToken.getID("ID"), entrada);
        return token;
    }
} 