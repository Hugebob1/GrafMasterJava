//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        GraphChunk[] graphs = GraphLoader.loadGraph();
        for(int i=0;i<GraphLoader.n;i++){
            if (graphs != null && graphs.length > 0 && graphs[i] != null) {
                GraphChunk.printGraphChunk(graphs[i]);
            }
        }
    }
}