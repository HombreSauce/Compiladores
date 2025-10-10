package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.EntradaTablaSimbolos;
import datos.TablaIdentificadorToken;
import datos.TablaSimbolos;
import aplicacion.Parser;


public class AccionSemantica8 implements AccionSemantica{
    private TablaSimbolos tablaSimbolos;
    private TablaIdentificadorToken tablaIDToken;

    public AccionSemantica8(TablaSimbolos TS, TablaIdentificadorToken tablaIdentificadorToken) {
        tablaSimbolos = TS;
        tablaIDToken = tablaIdentificadorToken;
    }
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        int num = Integer.parseInt(lexema.toString());
        if(num <= 32768) { //si es negativo está bien, si es positivo damos error en la etapa sintactica
            lexema.append("I"); //agregamos la I
            EntradaTablaSimbolos entrada = tablaSimbolos.insertar(lexema.toString(), lineaCodigoActual);
            Token token = new Token(tablaIDToken.getID("CTEINT"), entrada);
            return token;
        } else {
            Parser.logLexError("Constante numerica entera fuera de rango '" + lexema.toString() + "'.");
            return null;
        }
    }
}
