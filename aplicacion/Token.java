package aplicacion;

import datos.EntradaTablaSimbolos;
import datos.TablaIdentificadorToken;
import datos.TablaPalabraReservada;

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
        private void mostrarToken() {
        TablaIdentificadorToken tablaIdentificadorToken = TablaIdentificadorToken.getInstancia();
        TablaPalabraReservada tablaPalabraReservada = TablaPalabraReservada.getInstancia();
        if (this.getIDToken() < 100) {
            System.out.println("Palabra reservada: " + tablaPalabraReservada.getClave(this.getIDToken()));
        } else if (this.getIDToken() == 100) {
            System.out.println("Identificador: " + this.getEntradaTS().getLexema());
        } else if (this.getIDToken() == 101) {
            System.out.println("Constante entera: " + this.getEntradaTS().getLexema());
        } else if (this.getIDToken() == 102) {
            System.out.println("Constante flotante: " + this.getEntradaTS().getLexema());
        } else if (this.getIDToken() == 103) {
            System.out.println("Cadena multilinea: " + this.getEntradaTS().getLexema());
        } else {
            System.out.println(tablaIdentificadorToken.getClave(this.getIDToken()));
        }
    }
}
