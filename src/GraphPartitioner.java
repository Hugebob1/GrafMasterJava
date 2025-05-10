import java.util.*;

public class GraphPartitioner {

    public static GraphChunk[] splitGraphRetryIfNeeded(GraphChunk graph, int numParts, float maxDiffPercent) {
        GraphChunk[] parts = splitGraphGreedyBalanced(graph, numParts, maxDiffPercent);
        if (parts == null) return null;

        for (GraphChunk part : parts) {
            if (Validator.isGraphConnected(part) != 0) {
                System.out.println("Podział niemożliwy: niespójny podgraf");
                return null;
            }
        }

        return parts;
    }

    public static GraphChunk[] splitGraphGreedyBalanced(GraphChunk graph, int numParts, float maxDiffPercent) {
        List<Vertex> verticesList = new ArrayList<>();
        for (Vertex v : graph.vertices) {
            if (v != null) verticesList.add(v);
        }

        int total = verticesList.size();
        int[] assignment = new int[graph.vertices.length];
        Arrays.fill(assignment, -1);

        Integer[] order = new Integer[total];
        int[] degrees = new int[total];

        for (int i = 0; i < total; i++) {
            Vertex v = verticesList.get(i);
            order[i] = i;
            degrees[i] = v.degree;
        }

        Arrays.sort(order, Comparator.comparingInt((Integer i) -> degrees[i]).reversed());

        int[] seeds = new int[numParts];
        int seedCount = 0;
        for (int i = 0; i < total && seedCount < numParts; i++) {
            int idx = order[i];
            Vertex v = verticesList.get(idx);
            seeds[seedCount] = v.id;
            assignment[v.id] = seedCount;
            seedCount++;
        }

        boolean[] assigned = new boolean[graph.vertices.length];
        Queue<Integer>[] queues = new LinkedList[numParts];
        int[] partSizes = new int[numParts];
        for (int i = 0; i < numParts; i++) {
            queues[i] = new LinkedList<>();
            queues[i].add(seeds[i]);
            assigned[seeds[i]] = true;
            partSizes[i]++;
        }

        boolean updated;
        do {
            updated = false;
            for (int g = 0; g < numParts; g++) {
                int size = queues[g].size();
                for (int s = 0; s < size; s++) {
                    int u = queues[g].poll();
                    Vertex v = graph.getById(u);
                    if (v == null) continue;
                    for (int nei : v.edges) {
                        Vertex neighbor = graph.getById(nei);
                        if (neighbor == null || assigned[nei]) continue;
                        assignment[nei] = g;
                        queues[g].add(nei);
                        partSizes[g]++;
                        assigned[nei] = true;
                        updated = true;
                    }
                }
            }
        } while (updated);

        for (Vertex v : verticesList) {
            if (assignment[v.id] == -1) {
                int minIdx = 0;
                for (int j = 1; j < numParts; j++) {
                    if (partSizes[j] < partSizes[minIdx]) minIdx = j;
                }
                assignment[v.id] = minIdx;
                partSizes[minIdx]++;
            }
        }

        int maxId = Arrays.stream(graph.vertices)
                .filter(Objects::nonNull)
                .mapToInt(v -> v.id)
                .max().orElse(graph.vertices.length - 1);

        GraphChunk[] parts = new GraphChunk[numParts];
        for (int i = 0; i < numParts; i++) {
            parts[i] = new GraphChunk(maxId + 1);
        }

        for (Vertex v : verticesList) {
            int g = assignment[v.id];
            if (g == -1) continue;
            Vertex copy = Vertex.createVertex(v.id, v.numEdges);
            copy.degree = 0;
            copy.x = v.x;
            copy.y = v.y;
            parts[g].vertices[v.id] = copy;
        }

        for (Vertex v : verticesList) {
            int g = assignment[v.id];
            if (g == -1) continue;
            Vertex copy = parts[g].vertices[v.id];
            for (int nei : v.edges) {
                if (assignment[nei] == g && copy.degree < copy.edges.length) {
                    copy.edges[copy.degree++] = nei;
                }
            }
        }

        return parts;
    }
    public static boolean diff(GraphChunk[] graph, float d) {
        int totalVertices = 0;
        int mini = Integer.MAX_VALUE;
        int maxi = Integer.MIN_VALUE;

        for (GraphChunk graphChunk : graph) {
            int count = 0;
            for (Vertex v : graphChunk.vertices) {
                if (v != null) count++;
            }

            totalVertices += count;
            mini = Math.min(mini, count);
            maxi = Math.max(maxi, count);
        }

        float baseSize = (float) totalVertices / graph.length;
        float ans = (maxi - mini) / baseSize * 100.0f;

        System.out.printf("Roznica: %.2f%% (min: %d, max: %d, baza: %.2f)%n", ans, mini, maxi, baseSize);
        return ans < d;
    }

}
