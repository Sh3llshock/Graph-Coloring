import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateConnections {

    public static void updateConnections(ColEdge[] edges) {
        DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();
        if (nodes == null || nodes.length != SharedData.getInstance().getNumberOfVertices()) {
            int numberOfVertices = SharedData.getInstance().getNumberOfVertices();
            nodes = new DrawnNodes[numberOfVertices];
            for (int i = 0; i < numberOfVertices; i++) {
                nodes[i] = new DrawnNodes();
            }
        }

        for (ColEdge edge : edges) {
            int u = edge.u - 1;
            int v = edge.v - 1;
            nodes[u].connections.add(v);
            nodes[v].connections.add(u);
        }
        SharedData.getInstance().setDrawnNodes(nodes);
    }

    public static void main(String[] args) {
        ColEdge[] edges = SharedData.getInstance().getEdges();
        updateConnections(edges);
    }
}