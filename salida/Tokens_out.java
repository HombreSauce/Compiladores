package salida;

import aplicacion.Token;
import datos.TablaIdentificadorToken;
import datos.TablaPalabraReservada;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static salida.Generador_out.*;

public class Tokens_out implements AutoCloseable {
    private final BufferedWriter w;

    public Tokens_out(Path path) throws IOException {
        this.w = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        escribirHeader();
    }

    private void escribirHeader() throws IOException {
        w.write(encabezado("TOKENS DETECTADOS")); w.newLine();
        w.write(String.format("%-6s | %-18s | %-28s | %-4s", "Linea", "Token", "Lexema", "ID")); w.newLine();
        w.write("------ | ------------------ | ---------------------------- | ----"); w.newLine();
    }

    public void append(Token t, int linea) {
        try {
            String[] tl = tipoYLexema(t);
            String tipo   = humanizeTipo(tl[0]);
            String lexema = tl[1].replace("\n","\\n").replace("\r","\\r").replace("\t","\\t");
            lexema = trunc(lexema, 28);
            w.write(String.format("%6d | %-18s | %-28s | %4d", linea, tipo, lexema, t.getIDToken()));
            w.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo token", e);
        }
    }

    private static String[] tipoYLexema(Token t) {
        TablaIdentificadorToken tid = TablaIdentificadorToken.getInstancia();
        TablaPalabraReservada   tpr = TablaPalabraReservada.getInstancia();
        int id = t.getIDToken();

        if (id < 269) { // Palabra reservada
            String kw = tpr.getClave(id);
            return new String[]{ kw, kw.toLowerCase() };
        } else if (id == 269) {
            return new String[]{ "ID",           t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
        } else if (id == 270) {
            return new String[]{ "CONST_INT",    t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
        } else if (id == 271) {
            return new String[]{ "CONST_FLOAT",  t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
        } else if (id == 272) {
            return new String[]{ "CONST_STRING", t.getEntradaTS()!=null ? t.getEntradaTS().getLexema() : "" };
        } else {
            String raw = tid.getClave(id); 
            String lex = (t.getEntradaTS()!=null) ? t.getEntradaTS().getLexema() : raw;
            return new String[]{ raw, lex };
        }
    }

    private static String humanizeTipo(String raw) {
        return switch (raw) {
            case "if" -> "IF"; 
            case "else" -> "ELSE"; 
            case "endif" -> "ENDIF";
            case "print" -> "PRINT"; 
            case "return" -> "RETURN"; 
            case "int" -> "INT";
            case "for" -> "FOR"; 
            case "from" -> "FROM"; 
            case "to" -> "TO";
            case "lambda" -> "LAMBDA"; 
            case "cv" -> "CV"; 
            case "trunc" -> "TRUNC";
            case ":=" -> "ASIGN"; 
            case "->" -> "FLECHA"; 
            case "==" -> "IGUAL";
            case "=!" -> "DISTINTO"; 
            case "<=" -> "MENORIGUAL"; 
            case "=>" -> "MAYORIGUAL";
            case "+" -> "MAS"; 
            case "*" -> "MUL"; 
            case "/" -> "DIV"; 
            case "-" -> "MENOS";
            case "=" -> "IGUALUNICO"; 
            case ">" -> "MAYOR"; 
            case "<" -> "MENOR"; 
            case "." -> "PUNTO";
            case "(" -> "PARENTINIC"; 
            case ")" -> "PARENTFIN";
            case "{" -> "LLAVEINIC"; 
            case "}" -> "LLAVEFIN";
            case ";" -> "PUNTOYCOMA"; 
            case "," -> "COMA";
            default -> raw;
        };
    }

    @Override public void close() {
        try { if (w != null) { w.flush(); w.close(); } } catch (IOException ignored) {}
    }
}
