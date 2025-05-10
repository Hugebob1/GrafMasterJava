import java.util.List;

public class Validator {
    public static final int ERR_GRAPH_NULL = 30;
    public static final int ERR_ALLOC_FAILED = 31;
    public static final int ERR_NO_ACTIVE_VERTICES = 32;
    public static final int ERR_GRAPH_NOT_CONNECTED = 33;
    public static final int GRAPH_CONNECTED = 0;

    public static int isGraphConnected(GraphChunk graph) {
        if (graph == null || graph.vertices == null) {
            return ERR_GRAPH_NULL;
        }

        int totalVertices = graph.totalVertices;
        boolean[] visited = new boolean[totalVertices];

        int start = -1;
        for (int i = 0; i < totalVertices; i++) {
            Vertex v = graph.vertices[i];
            if (v != null && v.degree > 0) {
                start = i;
                break;
            }
        }

        if (start == -1) {
            return ERR_NO_ACTIVE_VERTICES;
        }

        dfs(graph.vertices, start, visited, totalVertices);

        for (int i = 0; i < totalVertices; i++) {
            Vertex v = graph.vertices[i];
            if (v != null && v.degree > 0 && !visited[i]) {
                return ERR_GRAPH_NOT_CONNECTED;
            }
        }

        return GRAPH_CONNECTED;
    }

    private static void dfs(Vertex[] vertices, int currentId, boolean[] visited, int totalVertices) {
        visited[currentId] = true;
        Vertex v = vertices[currentId];

        for (int i = 0; i < v.degree; i++) {
            int neighborId = v.edges[i];
            if (neighborId < 0 || neighborId >= totalVertices) continue;
            if (vertices[neighborId] == null) continue;
            if (!visited[neighborId]) {
                dfs(vertices, neighborId, visited, totalVertices);
            }
        }
    }
    public static boolean validateFile(List<List<String>> groupLines) {
        return true;
    }
}
