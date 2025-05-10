import javax.swing.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;

public class GraphSaver {
    public static void saveGraphsTxt(GraphChunk []graphs, String fileName) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+fileName));
            int i = 1;
            for (GraphChunk graph : graphs) {
                writer.write("#"+String.valueOf(i));
                writer.newLine();
                for (Vertex v : graph) {
                    if (v == null || v.degree == 0) continue;
                    writer.write(v.id+": ");
                    for (int j = 0; j < v.degree; j++) {
                        writer.write(v.edges[j]+" ");
                    }
                    writer.write("(" + v.x + "," + v.y + ")");
                    writer.newLine();
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveGraphsBin(GraphChunk[] subgraphs, String filename) {
        System.out.println("=== ✅ ZAPISUJĘ GRAF POPRAWNIE (GraphSaverFinal) ===");
        ByteBuffer buffer = ByteBuffer.allocate(10_000_000);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.put((byte)'S');
        buffer.put((byte)'U');
        buffer.put((byte)'B');
        buffer.put((byte)'G');
        buffer.put((byte)1);
        buffer.putShort((short) subgraphs.length);

        for (GraphChunk g : subgraphs) {
            int count = 0;
            for (Vertex v : g.vertices) {
                if (v != null) count++;
            }

            buffer.putShort((short) count);

            for (Vertex v : g.vertices) {
                if (v == null) continue;

                int deg = (v.edges != null) ? v.edges.length : 0;

                buffer.putShort((short) v.id);
                buffer.putShort((short) v.x);
                buffer.putShort((short) v.y);
                buffer.putShort((short) deg);

                for (int k = 0; k < deg; k++) {
                    buffer.putShort((short) v.edges[k]);
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream("data/" + filename)) {
            fos.write(buffer.array(), 0, buffer.position());
            System.out.println("✅ Plik zapisany: " + filename + " (" + buffer.position() + " bajtów)");
        } catch (IOException e) {
            System.err.println("❌ Błąd zapisu: " + e.getMessage());
        }
    }

    public static int saveSubGraphsCompactBinary(GraphChunk[] subgraphs, int numParts, String filename) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("data/"+filename))) {
            // NAGŁÓWEK
            // Sygnatura pliku - "SUBG"
            out.writeByte('S');
            out.writeByte('U');
            out.writeByte('B');
            out.writeByte('G');

            // Wersja formatu
            out.writeByte(1);

            // Liczba podgrafów
            out.writeShort(numParts);

            // Dla każdego podgrafu
            for (int i = 0; i < numParts; i++) {
                GraphChunk g = subgraphs[i];

                // Liczba wierzchołków w podgrafie
                int count = 0;
                for (int j = 0; j < g.totalVertices; j++) {
                    if (g.vertices[j] != null) count++;
                }
                out.writeShort(count);

                // Dla każdego wierzchołka
                for (int j = 0; j < g.totalVertices; j++) {
                    Vertex v = g.vertices[j];
                    if (v == null) continue;

                    // Zapisz dane wierzchołka
                    out.writeShort((short)v.id);
                    out.writeShort((short)v.x);
                    out.writeShort((short)v.y);
                    out.writeByte((byte)v.degree);

                    // Zapisz sąsiadów
                    for (int k = 0; k < v.degree; k++) {
                        out.writeShort((short)v.edges[k]);
                    }
                }
            }

            System.out.println("Grafy zapisane do pliku binarnego: " + filename);
            return 0;

        } catch (IOException e) {
            System.err.println("Nie mozna otworzyc pliku do zapisu: " + e.getMessage());
            return -1;
        }
    }


}
