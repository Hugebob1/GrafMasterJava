import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GraphLoader {
    static int n;
    public static GraphChunk[] loadGraphFromTxt(String filename) {
        try {
            BufferedReader in = new BufferedReader(new FileReader("data/"+filename));
            String line;
            List<List<String>> groupLines = new ArrayList<>();
            int groupId = -1;

            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("#")) {
                    groupLines.add(new ArrayList<>());
                    groupId++;
                } else {
                    groupLines.get(groupId).add(line);
                }
            }
            in.close();

            int n = groupLines.size();
            GraphChunk[] graphs = new GraphChunk[n];

            for (int i = 0; i < n; i++) {
                List<String> group = groupLines.get(i);
                Vertex[] vertices = new Vertex[group.size()];

                for (int j = 0; j < group.size(); j++) {
                    String s = group.get(j);
                    String[] parts = s.split(":");
                    if (parts.length < 2) continue;

                    int id = Integer.parseInt(parts[0].trim());
                    String right = parts[1].split("\\(")[0].trim();
                    String[] liczby = right.split("\\s+");
                    int count = liczby.length;

                    Vertex v = Vertex.createVertex(id, count);
                    int[] edges = new int[count];
                    for (int k = 0; k < count; k++) {
                        edges[k] = Integer.parseInt(liczby[k].trim());
                    }

                    String wspolrzedneFragment = parts[1].substring(parts[1].indexOf("(") + 1, parts[1].indexOf(")"));
                    String[] xy = wspolrzedneFragment.split(",");
                    v.x = Integer.parseInt(xy[0].trim());
                    v.y = Integer.parseInt(xy[1].trim());
                    v.edges = edges;

                    vertices[j] = v;
                }

                graphs[i] = new GraphChunk();
                graphs[i].vertices = vertices;
                graphs[i].totalVertices = vertices.length;
            }
            System.out.println("Plik tekstowy wczytany poprawnie.");
            return graphs;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static GraphChunk[] loadGraphFromBin(String filename) {
        try {
            byte[] fileBytes = Files.readAllBytes(Path.of("data/"+filename));
            ByteBuffer buffer = ByteBuffer.wrap(fileBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            byte[] signature = new byte[4];
            buffer.get(signature);
            if (!new String(signature).equals("SUBG")) {
                System.err.println("Niepoprawna sygnatura pliku");
                return null;
            }

            int version = Byte.toUnsignedInt(buffer.get());
            if (version != 1) {
                System.err.println("Nieobsługiwana wersja: " + version);
                return null;
            }

            int numParts = Byte.toUnsignedInt(buffer.get());
            GraphChunk[] parts = new GraphChunk[numParts];

            for (int i = 0; i < numParts; i++) {
                int count = Short.toUnsignedInt(buffer.getShort());
                Vertex[] vertices = new Vertex[count];

                for (int j = 0; j < count; j++) {
                    int id = Short.toUnsignedInt(buffer.getShort());
                    int x = buffer.getShort(); // signed
                    int y = buffer.getShort(); // signed
                    int deg = Byte.toUnsignedInt(buffer.get());

                    int[] edges = new int[deg];
                    for (int k = 0; k < deg; k++) {
                        edges[k] = Short.toUnsignedInt(buffer.getShort());
                    }

                    Vertex v = Vertex.createVertex(id, deg);
                    v.x = x;
                    v.y = y;
                    v.edges = edges;
                    v.groupId = i;
                    vertices[j] = v;
                }

                GraphChunk g = new GraphChunk();
                g.vertices = vertices;
                g.totalVertices = vertices.length;
                parts[i] = g;
            }

            System.out.println("Plik binarny wczytany poprawnie.");
            return parts;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
