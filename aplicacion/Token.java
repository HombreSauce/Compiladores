package aplicacion;

import datos.EntradaTablaSimbolos;

public class Token {
    private int identificadorToken;
    private EntradaTablaSimbolos entradaTS;

    public Token(int identificadorToken, EntradaTablaSimbolos entradaTS) {
        this.identificadorToken = identificadorToken;
        this.entradaTS = entradaTS;
    }

    public int getIDToken() {
        return identificadorToken;
    }

    public EntradaTablaSimbolos getEntradaTS() {
        return entradaTS;
    }
}