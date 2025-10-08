package salida;

import datos.TablaSimbolos;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static salida.Generador_out.encabezado;

public class TS_out implements AutoCloseable {
    private final BufferedWriter w;

    public TS_out(Path path) throws IOException {
        this.w = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        w.write(encabezado("TABLA DE SIMBOLOS"));
        w.newLine();
    }

    public void dump(TablaSimbolos ts) {
        PrintWriter pw = new PrintWriter(w, true);
        ts.mostrarTabla(pw);
    }

    @Override public void close() {
        try { if (w != null) { w.flush(); w.close(); } } catch (IOException ignored) {}
    }
}
