package salida;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static salida.Generador_out.encabezado;

public class Errors_out implements AutoCloseable {
    public static class Err {
        public final int linea; public final String mensaje;
        public Err(int l, String m){ this.linea=l; this.mensaje=m; }
    }

    private final List<Err> errs = new ArrayList<>();
    private final BufferedWriter w;

    public Errors_out(Path path) throws IOException {
        this.w = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        w.write(encabezado("ERRORES DETECTADOS")); w.newLine();
        w.write(String.format("%-6s | %s%n", "Linea", "Descripcion"));
        w.write(String.format("%-6s | %s%n", "------", "------------------------------"));
    }

    public void add(int linea, String mensaje) {
        errs.add(new Err(linea, mensaje));
    }

    public void flushNow() {
        try {
            for (Err e : errs) {
                w.write(String.format("%6d | %s%n", e.linea, e.mensaje));
            }
            w.flush();
        } catch (IOException ex) {
            throw new RuntimeException("Error escribiendo errores", ex);
        }
    }

    @Override public void close() {
        try { flushNow(); if (w != null) { w.flush(); w.close(); } } catch (IOException ignored) {}
    }
}
