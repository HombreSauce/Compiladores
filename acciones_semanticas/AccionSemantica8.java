package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.EntradaTablaSimbolos;
import datos.TablaIdentificadorToken;
import datos.TablaSimbolos;

public class AccionSemantica8 implements AccionSemantica{
    private TablaSimbolos tablaSimbolos;
    private TablaIdentificadorToken tablaIDToken;

    public AccionSemantica8(TablaSimbolos TS, TablaIdentificadorToken tablaIdentificadorToken) {
        tablaSimbolos = TS;
        tablaIDToken = tablaIdentificadorToken;
    }
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura) {
        int num = Integer.parseInt(lexema.toString());
        if(num < 32768) { //si es negativo está bien, si es positivo damos error en la etapa sintactica
            lexema.append(simbolo); //agregamos la I
            EntradaTablaSimbolos entrada = tablaSimbolos.insertar(lexema.toString());
            Token token = new Token(tablaIDToken.getID("CTE"), entrada);
            return token;
        } else {
            //No sé tirar error
            return null;
        }
    }
}
