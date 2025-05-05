import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.SyncFailedException;

public class GraphLoader {
    static int n;
    public static GraphChunk[] loadGraph() {
        try{
            BufferedReader in = new BufferedReader(new FileReader("wynik2.txt"));
            int vertices = Integer.parseInt(in.readLine());
            int subGraphs = Integer.parseInt(in.readLine());
            n = subGraphs;
            GraphChunk []graph = new GraphChunk[subGraphs];
            for(int i = 0; i < subGraphs; i++) {
                graph[i] = new GraphChunk();
            }
            String []lines = new String[vertices];
            String line;
            System.out.println(vertices);
            System.out.println(subGraphs);
            int index = 0;
            while ((line = in.readLine()) != null) {
                lines[index] = line;
                index++;
            }
            in.close();

            int []verticesForGraph = new int[subGraphs];
            for(int i=0;i<vertices;i++) {
                int gn = Integer.parseInt(lines[i].split(";")[2]);
                verticesForGraph[gn]++;
            }
            int id = 1;
            for (int i = 0; i < subGraphs; i++) {
                graph[i].totalVertices = verticesForGraph[i];
                graph[i].vertices = new Vertex[verticesForGraph[i]];
                int pom = 0;
                for (int j = 0; j < vertices; j++) {
                    int gn = Integer.parseInt(lines[j].split(";")[2]);
                    int num = Integer.parseInt(lines[j].split(";")[3]);
                    int []connections = new int[num];
                    int temp = 4;
                    for(int k = 0; k < num; k++) {
                        connections[k] = Integer.parseInt(lines[j].split(";")[temp]);
                        temp++;
                    }
                    if (gn == i) {
                        Vertex v = Vertex.createVertex(id++, verticesForGraph[i]);
                        v.edges = connections;
                        v.degree = connections.length;
                        graph[i].vertices[pom++] = v;
                        v.x= Integer.parseInt(lines[j].split(";")[0]);
                        v.y= Integer.parseInt(lines[j].split(";")[1]);
                    }
                }
            }
            return graph;
            //System.out.println(graph[0].vertices[0].edges.length);

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
