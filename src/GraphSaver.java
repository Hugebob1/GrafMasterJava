import java.io.*;

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
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("data/"+filename))) {
            // Nagłówek
            out.writeBytes("SUBG");             // 4 bajty: sygnatura
            out.writeByte(1);                   // 1 bajt: wersja
            out.writeShort(subgraphs.length);   // 2 bajty: liczba podgrafów (uint16_t)

            for (GraphChunk g : subgraphs) {
                int count = 0;
                for (Vertex v : g.vertices) {
                    if (v != null) count++;
                }

                out.writeShort(count); // 2 bajty: liczba aktywnych wierzchołków

                for (Vertex v : g.vertices) {
                    if (v == null) continue;

                    int id = v.id;
                    int x = v.x;
                    int y = v.y;
                    int deg = v.degree;

                    out.writeShort(id);   // 2 bajty: uint16_t
                    out.writeShort(x);    // 2 bajty: int16_t
                    out.writeShort(y);    // 2 bajty: int16_t
                    out.writeShort(deg);  // 2 bajty: uint16_t

                    for (int k = 0; k < deg; k++) {
                        out.writeShort(v.edges[k]);  // 2 bajty: uint16_t
                    }
                }
            }

            System.out.println("Grafy zapisane do pliku binarnego: " + filename);

        } catch (IOException e) {
            System.err.println("Nie mozna otworzyc pliku do zapisu: " + e.getMessage());
        }

    }




}
