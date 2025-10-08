package salida;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Generador_out {
     public static String encabezado(String titulo) {
        String barra = "=".repeat(Math.max(24, titulo.length() + 8));
        return barra + "\n" + "=== " + titulo + " ===\n" + barra;
    }

    public static Path rutaLexico(Path fuente, boolean acentos) {
        String base = quitarExt(fuente.getFileName().toString());
        String suf  = acentos ? "-léxico" : "-lexico";
        return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
                .resolve(base + suf + ".txt");
    }

    public static Path rutaTabla(Path fuente, boolean acentos) {
        String base = quitarExt(fuente.getFileName().toString());
        String suf  = acentos ? "-tabla-de-símbolos" : "-tabla-simbolos";
        return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
                .resolve(base + suf + ".txt");
    }

    public static Path rutaSintactico(Path fuente, boolean acentos) {
        String base = quitarExt(fuente.getFileName().toString());
        String suf  = acentos ? "-sintáctico" : "-sintactico";
        return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
                .resolve(base + suf + ".txt");
    }

    public static Path rutaErrores(Path fuente, boolean acentos) {
        String base = quitarExt(fuente.getFileName().toString());
        String suf  = acentos ? "-errores" : "-errores";
        return (fuente.getParent() == null ? Paths.get(".") : fuente.getParent())
                .resolve(base + suf + ".txt");
    }

    private static String quitarExt(String s) {
        int i = s.lastIndexOf('.');
        return (i > 0) ? s.substring(0, i) : s;
    }

    public static String trunc(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        if (max <= 1) return s.substring(0, max);
        return s.substring(0, max-1) + "…";
    }
}
