package salida;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static salida.Generador_out.encabezado;

public class SintaxStruct_out implements AutoCloseable {
    public static class Item {
        public final int linea;
        public final String estructura;
        public Item(int linea, String estructura) { this.linea = linea; this.estructura = estructura; }
    }

    private final List<Item> items = new ArrayList<>();
    private final BufferedWriter w;

    public SintaxStruct_out(Path path) throws IOException {
        this.w = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
        // header se escribe al final para que si no hay items, igual quede prolijo
    }

    public void add(int linea, String estructura) {
        if (linea <= 0) linea = 1;
        if (estructura == null || estructura.isBlank()) return;
        items.add(new Item(linea, estructura));
    }

    public void writeNow() {
        try {
            items.sort(Comparator.comparingInt(i -> i.linea));
            w.write(encabezado("ESTRUCTURAS SINTACTICAS DETECTADAS")); w.newLine();
            w.write(String.format("%-6s | %-30s%n", "Linea", "Estructura"));
            w.write(String.format("%-6s | %-30s%n", "------", "------------------------------"));
            for (Item it : items) {
                w.write(String.format("%6d | %-30s%n", it.linea, it.estructura));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo estructuras", e);
        }
    }

    @Override public void close() {
        try {
            writeNow();
            if (w != null) { w.flush(); w.close(); }
        } catch (Exception ignored) {}
    }
}
