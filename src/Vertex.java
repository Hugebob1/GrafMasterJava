public class Vertex {
    int id;
    int numEdges;
    int halfEdges;
    int []edges;
    int []internalEdges;
    int []externalEdges;
    int groupId;
    int edgeDelta;
    int degree;
    int active;
    int x;
    int y;
    public static Vertex createVertex(int id, int numEdges) {
        Vertex v = new Vertex();

        v.id = id;
        v.numEdges = numEdges;
        v.halfEdges = numEdges / 2;
        v.edges = new int[numEdges];
        v.internalEdges = new int[v.halfEdges];
        v.externalEdges = new int[v.halfEdges];
        v.groupId = -1;
        v.edgeDelta = 0;
        v.degree = numEdges;
        v.active = 1;
        return v;
    }

}
