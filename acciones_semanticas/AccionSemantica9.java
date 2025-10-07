package acciones_semanticas;

import aplicacion.ControlPosicion;
import aplicacion.Token;
import datos.EntradaTablaSimbolos;
import datos.TablaIdentificadorToken;
import datos.TablaSimbolos;

public class AccionSemantica9 implements AccionSemantica {
    private TablaSimbolos tablaSimbolos;
    private TablaIdentificadorToken tablaIDToken;
    private final double MIN_NORMAL = Double.MIN_NORMAL;   // 2.2250738585072014E-308
    private final double MAX_VALUE  = Double.MAX_VALUE;    // 1.7976931348623157E+308
    //LOS LIMITES SON SIMETRICOS [-MAX_VALUE, MAX_VALUE]

    public AccionSemantica9(TablaSimbolos TS, TablaIdentificadorToken tablaIdentificadorToken) {
        tablaSimbolos = TS;
        tablaIDToken = tablaIdentificadorToken;
    }
    
    @Override
    public Token ejecutar(char simbolo, StringBuilder lexema, ControlPosicion posicionLectura, int lineaCodigoActual) {
        posicionLectura.decrementar();
        StringBuilder numero = new StringBuilder(lexema);
        double num;
        if(lexema.toString().startsWith(".")) {
            numero.setLength(0);
            numero.append("0").append(lexema);
        }
        if(lexema.toString().contains("D")) {
            String numNotacionJava = numero.toString().replace("D", "E").replace("+", "");
            num = Double.parseDouble(numNotacionJava);
        } else {
            num = Double.parseDouble(numero.toString());
        }
        //Como el rango es simétrico chequeamos por el absoluto
        if((Math.abs(num) >= MIN_NORMAL && Math.abs(num) <= MAX_VALUE || num == 0.0)) {
            EntradaTablaSimbolos entrada = tablaSimbolos.insertar(lexema.toString(), lineaCodigoActual);
            Token token = new Token(tablaIDToken.getID("CTEFLOAT"), entrada);
            return token;
        } else {
            System.err.println("Linea " + lineaCodigoActual + ". Error Lexico: Constante numerica real fuera de rango '" + lexema.toString() + "'.");
            return null;
        }
    }
}
