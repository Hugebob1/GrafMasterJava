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
                int maxId = group.stream()
                        .mapToInt(s -> Integer.parseInt(s.split(":")[0].trim()))
                        .max().orElse(group.size());

                Vertex[] vertices = new Vertex[maxId + 1];


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

                graphs[i] = new GraphChunk(vertices.length);
                graphs[i].vertices = vertices; // możesz to zostawić, ale jest nadmiarowe
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
            byte[] fileBytes = Files.readAllBytes(Path.of("data/" + filename));
            ByteBuffer buffer = ByteBuffer.wrap(fileBytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            if (buffer.remaining() < 7) { // 4 bajty sygnatury + 1 bajt wersji + 2 bajty numParts
                System.err.println("Plik zbyt krótki");
                return null;
            }

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

            int numParts = Short.toUnsignedInt(buffer.getShort()); // ✅ teraz 2 bajty
            GraphChunk[] parts = new GraphChunk[numParts];

            for (int i = 0; i < numParts; i++) {
                if (buffer.remaining() < 2) {
                    System.err.println("Brakuje danych: liczba wierzchołków");
                    return null;
                }

                int count = Short.toUnsignedInt(buffer.getShort());
                List<Vertex> vertexList = new ArrayList<>();
                int maxId = -1;

                for (int j = 0; j < count; j++) {
                    if (buffer.remaining() < 8) { // ✅ 4 × 2 bajty = id, x, y, degree
                        System.err.println("Brakuje danych: nagłówek wierzchołka");
                        return null;
                    }

                    int id = Short.toUnsignedInt(buffer.getShort());
                    int x = buffer.getShort(); // signed
                    int y = buffer.getShort(); // signed
                    int deg = Short.toUnsignedInt(buffer.getShort()); // ✅ teraz 2 bajty

                    if (buffer.remaining() < deg * 2) {
                        System.err.println("Brakuje danych: krawędzie");
                        return null;
                    }

                    int[] edges = new int[deg];
                    for (int k = 0; k < deg; k++) {
                        edges[k] = Short.toUnsignedInt(buffer.getShort());
                    }

                    Vertex v = Vertex.createVertex(id, deg);
                    v.x = x;
                    v.y = y;
                    v.edges = edges;
                    v.groupId = i;

                    vertexList.add(v);
                    maxId = Math.max(maxId, id);
                }

                GraphChunk g = new GraphChunk(maxId + 1);
                for (Vertex v : vertexList) {
                    g.vertices[v.id] = v;
                }
                g.totalVertices = maxId + 1;
                parts[i] = g;
            }

            System.out.println("Plik binarny wczytany poprawnie.");
            return parts;

        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
            return null;
        }
    }




}
