package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.EntradaTablaSimbolos;
import datos.TablaIdentificadorToken;
import datos.TablaSimbolos;

public class AccionSemantica11 implements AccionSemantica{
    private TablaSimbolos tablaSimbolos;
    private TablaIdentificadorToken tablaIDToken;

    public AccionSemantica11(TablaSimbolos TS, TablaIdentificadorToken tablaIdentificadorToken) {
        tablaSimbolos = TS;
        tablaIDToken = tablaIdentificadorToken;
    }

    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        String lexemaSinSaltos = lexema.toString().replace("\n", " ").replace("\r", " ");
        lexema.setLength(0);
        lexema.append(lexemaSinSaltos);
        EntradaTablaSimbolos entrada = tablaSimbolos.insertar(lexema.toString(), lineaCodigoActual, "CTESTR");
        Token token = new Token(tablaIDToken.getID("CTESTR"), entrada);
        return token;
    }
}
