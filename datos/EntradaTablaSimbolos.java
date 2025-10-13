package datos;

import java.util.ArrayList;

public class EntradaTablaSimbolos {
    private String lexema;
    ArrayList<Integer> nroLineas;
    private String categoria;

    public EntradaTablaSimbolos(String lexema, int nroLinea, String categoria) {
        this.lexema = lexema;
        this.nroLineas = new ArrayList<>();
        this.nroLineas.add(nroLinea);
        this.categoria = categoria;
    }

    public void agregarLinea(int nroLinea) {
        if (!nroLineas.contains(nroLinea)) { //si la linea no esta en la lista, la agrega
            nroLineas.add(nroLinea);
        }
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public ArrayList<Integer> getNroLineas() {
        return nroLineas;
    }

    public int getUltimaLinea() {
        if (!nroLineas.isEmpty()) {
            return nroLineas.get(nroLineas.size() - 1);
        }
        return -1; //si no hay lineas guardadas
    }

    public void setCategoria(String c) {
        this.categoria = c; 
    }
    
    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return "Lexema: " + lexema + ", lineas: " + getNroLineas(); 
    }
}