package acciones_semanticas;

import app.Token;
import datos.EntradaTablaSimbolos;
import datos.TablaSimbolos;

public class AccionSemantica3 implements AccionSemantica{ 
    private TablaSimbolos tablaSimbolos;

    public AccionSemantica3(TablaSimbolos TS) {
        tablaSimbolos = TS;
    }

    @Override
    public Token ejecutar(char simbolo, int estadoActual, StringBuilder lexema) {
        //devuelve uno para atras
        lexema.substring(0, 20);
        EntradaTablaSimbolos entrada = tablaSimbolos.insertar(lexema.toString());
        Token token = new Token(100, entrada); //cambiar 100 por un ID de la tabla de alfabeto
        return token;
    }
} 