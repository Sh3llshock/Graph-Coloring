public class GraphData {
    private static GraphData instance;
    private int[][] adjacencyMatrix;

    private GraphData() {}

    public static GraphData getInstance() {
        if (instance == null) {
            instance = new GraphData();
        }
        return instance;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }
}
