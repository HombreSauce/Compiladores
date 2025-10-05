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
        public void mostrarToken() {
        TablaIdentificadorToken tablaIdentificadorToken = TablaIdentificadorToken.getInstancia();
        TablaPalabraReservada tablaPalabraReservada = TablaPalabraReservada.getInstancia();
        if (this.getIDToken() < 269) {
            System.out.println("Palabra reservada: " + tablaPalabraReservada.getClave(this.getIDToken()));
        } else if (this.getIDToken() == 269) {
            System.out.println("Identificador: " + this.getEntradaTS().getLexema());
        } else if (this.getIDToken() == 270) {
            System.out.println("Constante entera: " + this.getEntradaTS().getLexema());
        } else if (this.getIDToken() == 271) {
            System.out.println("Constante flotante: " + this.getEntradaTS().getLexema());
        } else if (this.getIDToken() == 272) {
            System.out.println("String: " + this.getEntradaTS().getLexema());
        } else {
            System.out.println(tablaIdentificadorToken.getClave(this.getIDToken()));
        }
    }
}
