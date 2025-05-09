import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GraphWriter {


    public static void writeSubG(GraphChunk[] graphs, String filename) {
        try{
            if(filename == null){
                throw new NullPointerException("filename == null");
            }
            if(graphs == null) {
                throw new NullPointerException("graphs == null");
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter("data/"+filename));
            int graphCnt = 0;
            for(int i=0;i<graphs.length;i++){
                graphCnt++;
                writer.write("#"+graphCnt+"\n");
                for(Vertex v: graphs[i]){
                    String x = "";
                    x+=String.valueOf(v.id);
                    x+=": ";
                    for(int j=0;j<v.degree;j++){
                        x+=String.valueOf(v.edges[j])+" ";
                    }
                    x+="(";
                    x+=String.valueOf(v.x);
                    x+=",";
                    x+=String.valueOf(v.y);
                    x+=")\n";
                    writer.write(x);
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
