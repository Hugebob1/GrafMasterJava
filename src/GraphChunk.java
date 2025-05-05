public class GraphChunk {
    int totalVertices;
    Vertex [] vertices;

    public static void printGraphChunk(GraphChunk graph) {
        for (int i = 0; i < graph.totalVertices; i++) {
            Vertex v = graph.vertices[i];
            if (v == null || v.degree == 0) continue;

            System.out.println("Wierzcholek " + v.id + ":");
            for (int j = 0; j < v.degree; j++) {
                System.out.print(" -> " + v.edges[j]);
            }
            System.out.println(" (" + v.x + "," + v.y + ")");
        }
    }

}
