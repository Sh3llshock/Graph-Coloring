public class CographChecker {
    private int[][] graph;
    private int vertices;

    public CographChecker(int[][] adjacencyMatrix) {
        this.graph = adjacencyMatrix; // Adjacency matrix
        this.vertices = adjacencyMatrix.length; // Number of vertices
    }

    //Turn connections into adjacency matrix
    public static int[][] convertConnectionsToAdjacencyMatrix() {
        DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();
        int numVertices = nodes.length;
        int[][] adjacencyMatrix = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            for (int connectedNode : nodes[i].connections) {
                adjacencyMatrix[i][connectedNode] = 1;
                adjacencyMatrix[connectedNode][i] = 1;
            }
        }

        return adjacencyMatrix;
    }


    // Check if there is a P4
    public boolean isCograph() {
        // Check all possible combinations of 4 nodes
        for (int a = 0; a < vertices; a++) {
            for (int b = a + 1; b < vertices; b++) {
                for (int c = b + 1; c < vertices; c++) {
                    for (int d = c + 1; d < vertices; d++) {
                        if (isP4Path(a, b, c, d)) {
                            return false; // Found P4
                        }
                    }
                }
            }
        }
        return true; // No P4
    }

    // Check if 4 nodes form a P4 path
    private boolean isP4Path(int a, int b, int c, int d) {
        // Check if there is an edge between any two nodes
        return graph[a][b] == 1 && 
               graph[b][c] == 1 && 
               graph[c][d] == 1 && 
               // Check if there is no edge between any two nodes
               graph[a][c] == 0 && 
               graph[a][d] == 0 && 
               graph[b][d] == 0;
    }
}