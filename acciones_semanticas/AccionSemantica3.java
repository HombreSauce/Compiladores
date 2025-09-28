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
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        posicionLectura.decrementar();
        if (lexema.length() > 20) {
            lexema.setLength(20);
            System.out.println("Linea " + lineaCodigoActual + ". Warning: El identificador se ha truncado a 20 caracteres.");
            System.out.println("Identificador truncado: '" + lexema.toString() + "'");
        }
        EntradaTablaSimbolos entrada = tablaSimbolos.insertar(lexema.toString(), lineaCodigoActual);
        Token token = new Token(tablaIDToken.getID("ID"), entrada);
        return token;
    }
} 