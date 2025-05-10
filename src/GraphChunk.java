import java.util.Iterator;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class GraphChunk implements Iterable<Vertex> {
    int totalVertices;
    Vertex [] vertices;

    public GraphChunk(int totalVertices) {
        this.totalVertices = totalVertices;
        this.vertices = new Vertex[totalVertices];
    }

    public Vertex getById(int id) {
        for (int i = 0; i < totalVertices; i++) {
            if (vertices[i] != null && vertices[i].id == id)
                return vertices[i];
        }
        return null;
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new Iterator<Vertex>() {
            int index = 0;

            @Override
            public boolean hasNext(){
                return index < totalVertices;
            }

            @Override
            public Vertex next() {
                return vertices[index++];
            }
        };
    }

    public static void printGraphChunk(GraphChunk graph) {
        for (Vertex v : graph) {
            if (v == null || v.degree == 0) continue;

            System.out.println("Wierzcholek " + v.id + ":");
            for (int j = 0; j < v.degree; j++) {
                System.out.print(" -> " + v.edges[j]);
            }
            System.out.println(" (" + v.x + "," + v.y + ")");
        }
    }
    public static void printGraph(GraphChunk graph) {
        for (Vertex v : graph) {
            if (v == null || v.degree == 0) continue;

            System.out.printf("%d: ", v.id);
            for (int j = 0; j < v.degree; j++) {
                System.out.printf("%d ", v.edges[j]);
            }
            System.out.println("(" + v.x + "," + v.y + ")");
        }
    }


}
