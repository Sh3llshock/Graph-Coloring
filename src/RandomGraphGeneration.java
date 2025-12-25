import java.util.*;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RandomGraphGeneration{
    private  int vertices;
    private static int[][] Edges;
    private  int edgeCount;

    public RandomGraphGeneration(int vertices, int edges) {
        this.vertices = vertices;
        this.Edges = new int[edges][2];
        this.edgeCount = 0;
    }

    public void addEdge(int u, int v) {
        if (u != v && !edgeExists(u, v)) {
            Edges[edgeCount][0] = u;
            Edges[edgeCount][1] = v;
            edgeCount++;
        }
    }

    public boolean edgeExists(int u, int v) {
        for (int i = 0; i < edgeCount; i++) {
            if ((Edges[i][0] == u && Edges[i][1] == v) || (Edges[i][0] == v && Edges[i][1] == u)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String difficulty) {
        int vertices = 10;
        int edges = 18;

        Random rand = new Random();

        if(difficulty == "1"){
            vertices = rand.nextInt(5,9); // number from 5-10 included
            edges = (vertices * (vertices - 1)) / 4;
        }
        else if(difficulty == "2"){
            vertices = rand.nextInt(9, 13); // number from 11-20 included
            edges = (vertices * (vertices - 2)) / 4;
        }
        else if (difficulty == "3"){
            vertices = rand.nextInt(13, 15); // from 21-30 included
            edges = (vertices * (vertices - 3)) / 4 ;
        }
        else if(difficulty == "4"){
            vertices = SharedData.getInstance().getRandomGraphDataVertex();
            edges = SharedData.getInstance().getRandomGraphDataEdge();

            if (vertices > edges - 1){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Invalid graph generation");
                alert.setHeaderText(null);
                alert.setContentText("You dont have enough edges");

                // Show the alert
                alert.showAndWait();

                vertices = 0;
                edges = 0;
            }
            else if( (vertices * (vertices - 1) / 2) < edges ){
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Invalid graph generation");
                alert.setHeaderText(null);
                alert.setContentText("You have too many edges");

                // Show the alert
                alert.showAndWait();

                vertices = 0;
                edges = 0;
            }

        }

        edges = Math.max(vertices, edges) + 1;


        RandomGraphGeneration graph = new RandomGraphGeneration(vertices, edges);

        while (graph.edgeCount < edges) {
            int u = rand.nextInt(vertices) + 1;
            int v = rand.nextInt(vertices) + 1;
            graph.addEdge(u, v);
        }

        System.out.println(Arrays.deepToString(graph.Edges));
        for(int i = 1; i < vertices + 1; i++){
            boolean vertexExists = false;

            for(int j = 0; j < Edges.length; j++){
                if(Edges[j][0] == i || Edges[j][1] == i){
                    vertexExists = true;
                }
            }
            if(!vertexExists){
                System.out.println("Vertex " + i + " not found");
                for(int j = 0; j < Edges.length; j++){
                    if(Edges[j][0] > i){
                        Edges[j][0]--;
                    }
                    if(Edges[j][1] > i){
                        Edges[j][1]--;
                    }
                }
                vertices--;
            }
        }
        System.out.println(Arrays.deepToString(graph.Edges));

        ColEdge[] edgesBigBrain = new ColEdge[Edges.length];
        for (int i = 0; i < Edges.length; i++) {
            edgesBigBrain[i] = new ColEdge();
            edgesBigBrain[i].u = Edges[i][0];
            edgesBigBrain[i].v = Edges[i][1];
        }

        SharedData.getInstance().setEdges(edgesBigBrain);
        SharedData.getInstance().setNumberOfEdges(edges);
        SharedData.getInstance().setNumberOfVertices(vertices);
        System.out.println("bobermol");

        //ClassicGameApp.loadRandomGenGraph();
    }
}