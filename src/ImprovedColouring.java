import java.util.*;

public class ImprovedColouring {
    public int Number_Nodes = SharedData.getInstance().getNumberOfVertices(); // node number
    public int [] Colour_Nodes = new int[Number_Nodes]; // what each node is coloured

    public DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();// connections of verticies

    public int[] Colour_Path (){
        Recursive_Colouring(0, Number_Nodes);
        return Colour_Nodes;
    }

    public void run_new_colouring(){
        ImprovedColour.main();

    }

    public int Chromatic_Number (){
        int [] Colour_Nodes = Colour_Path();
        int Chromatic_Number = 0;
        for (int i = 0; i < Colour_Nodes.length; i++) {
            if (Chromatic_Number < Colour_Nodes[i]){
                Chromatic_Number = i;
            }
        }
        return Chromatic_Number;
    }

    public boolean Possible_Colour (int Node, int Colour){ // Checks if the Adjacent nodes have the same colour or not. (true if not false if yes)
        //int[] cons = new int[nodes[Node].connections.size()];
        //nodes[Node].connections.toArray(cons);
        int[] cons = nodes[Node].connections.stream().mapToInt(Integer::intValue).toArray();

        for (int i = 0; i < Number_Nodes; i++) {
            if (Colour_Nodes[cons[i]] == Colour){
                return false;
            }
        }
        return true;
    }

    public boolean Recursive_Colouring (int Node, int Number_Colours){ // Tries to colour the graph, using recursion
        if (Node == Number_Nodes){
            return true;

        }
        for (int Colour = 1; Colour <= Number_Colours; Colour++) {
            if (Possible_Colour(Node, Colour)){
                Colour_Nodes[Node] = Colour;
                if (Recursive_Colouring(Node + 1, Number_Colours)){
                    return true;
                }

                Colour_Nodes[Node] = 0;
            }
        }

        return false;
    }
    static void main(){
    }
}

class ProjectColour {

   /* public int Number_Nodes = SharedData.getInstance().getNumberOfVertices(); // node number
    public int [] Colour_Nodes = new int[Number_Nodes]; // what each node is coloured

    public DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();// connections of verticies
    // what each node is coloured
    static String [] Vertices = {"V1", "V2", "V3", "V4", "V5" , "V6"}; // honestly dunno

    int number_of_vertices = SharedData.getInstance().getNumberOfVertices();
   // int number_of_edges = SharedData.getInstance().getNumberOfEdges();
    ColEdge[] edges = SharedData.getInstance().getEdges();
    int [][] Vertices_Connection = new int[number_of_vertices][2];

   //     for(int i = 0; i < number_of_vertices; i++){
   //     Vertices_Connection[i][0] = edges[i].u;
   //     Vertices_Connection[i][1] = edges[i].v;
   // }

    /*static int [][] Vertices_Connection = { // the matrix connectivity
            {1, 2},
            {2, 3},
            {3, 1},
            {1, 4},
            {4, 5},
            {5, 6},
            {6, 4}
    };

    int Number_Edges = SharedData.getInstance().getNumberOfEdges(); // Number of edges, might be uesful
    int colours = Number_Nodes;

    int[][] Nodes_Connection = new int[Number_Nodes][Number_Nodes]; // Empty connectivity method

*/

    public int Number_Nodes = SharedData.getInstance().getNumberOfVertices(); // node number
    public int[] Colour_Nodes = new int[Number_Nodes]; // what each node is coloured
    public DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles(); // connections of vertices
    public int Number_Edges = SharedData.getInstance().getNumberOfEdges(); // Number of edges, might be useful
    public int colours = Number_Nodes;
    public int[][] Nodes_Connection = new int[Number_Nodes][Number_Nodes]; // Empty connectivity method
    int number_of_vertices = SharedData.getInstance().getNumberOfVertices();


    //public static void main(String[] args) { // to test the methods
    public void ComputeChromaticNumber() {

        long start_timer = System.nanoTime(); // start timer

        int number_of_edges = SharedData.getInstance().getNumberOfEdges();
        ColEdge[] edges = SharedData.getInstance().getEdges();
        int [][] Vertices_Connection = new int[number_of_edges][2];

        for(int i = 0; i < number_of_edges; i++){
            Vertices_Connection[i][0] = edges[i].u;
            Vertices_Connection[i][1] = edges[i].v;
        }


        Nodes_Connection = Nodes_Connection_Ammount(Vertices_Connection);

        if (Recursive_Colouring(0, colours)){
            int Chnumber = 0;
            boolean timecheck = true;
            for (int i = 0; i < Colour_Nodes.length; i++) {
                if (Colour_Nodes[i] > Chnumber){
                    Chnumber = Colour_Nodes[i];

                    long current_timer = System.nanoTime();
                    long elapsedTime = (current_timer - start_timer) / 1000000000;

                    if (elapsedTime >= 30000) { //stop if take more than 30s
                        timecheck = false;
                        break;
                    }
                }
            }


            compute_upper_bound();

            if (timecheck) {
                System.out.println("chromatic number " + (Chnumber));

                FinalData data = SharedData.getInstance().getFinalData();
                System.out.println("lower + upper bound " + data.lower_bound + " " + data.upper_bound);
                data.exact_chromatic_bound = Chnumber;
                data.exact_chrom_bound_completed_till_end = true;
                SharedData.getInstance().setFinalData(data);

                DrawnNodes nodes[] = SharedData.getInstance().getDrawnCircles();
                for(int i = 0; i < nodes.length; i++){
                    nodes[i].colour = Colour_Nodes[i];
                }
                SharedData.getInstance().setDrawnNodes(nodes);

                System.out.println(Arrays.toString(Colour_Nodes));
            }


        }
    }

    public int[][] Nodes_Connection_Ammount(int[][] VC){ // method creates Adjacency Matrix for the Verticies_Connection
        for (int i = 0; i < VC.length; i++) {
            int j = VC[i][0] - 1;
            int k = VC[i][1] - 1;

            int[][] private_matrix = new int[Number_Nodes][Number_Nodes];
            for (int l = 0; l < Number_Nodes; l++) {
                for (int m = 0; m < Number_Nodes; m++) {
                    private_matrix[l][m] = 0;
                }
            }
            Nodes_Connection[j][k] ++;
            Nodes_Connection[k][j] ++;
        }
        GraphData.getInstance().setAdjacencyMatrix(Nodes_Connection);
        FinalData haha = SharedData.getInstance().getFinalData();
        haha.adjecencyMatrix = Nodes_Connection;
        System.out.println("NEWWW does it work? " + Arrays.deepToString(Nodes_Connection));
        return Nodes_Connection;
    }

    public boolean Possible_Colour (int Node, int Colour){ // Checks if the Adjacent nodes have the same colour or not. (true if not false if yes)
        for (int i = 0; i < Number_Nodes; i++) {
            if (Nodes_Connection[Node][i] == 1 && Colour_Nodes[i] == Colour){
                return false;
            }
        }
        return true;
    }

    public boolean Recursive_Colouring (int Node, int Number_Colours){ // Tries to colour the graph, using recursion
        if (Node == Number_Nodes){
            return true;

        }
        for (int Colour = 1; Colour <= Number_Colours; Colour++) {
            if (Possible_Colour(Node, Colour)){
                Colour_Nodes[Node] = Colour;
                if (Recursive_Colouring(Node + 1, Number_Colours)){
                    return true;
                }

                Colour_Nodes[Node] = 0;
            }
        }

        return false;
    }

    public static void compute_upper_bound () {
        //int [][] Vertices_Connection in main
        //manipulate matrix

        int number_of_edges = SharedData.getInstance().getNumberOfEdges();
        ColEdge[] edges = SharedData.getInstance().getEdges();
        int [][] Vertices_Connection = new int[number_of_edges][2];
        for(int i = 0; i < number_of_edges; i++){
            Vertices_Connection[i][0] = edges[i].u;
            Vertices_Connection[i][1] = edges[i].v;
        }

        //int [] flat_Vertices_Connection = new int [Vertices_Connection.length*Vertices_Connection[0].length];
        int [] flat_Vertices_Connection = new int [Vertices_Connection.length*2];
        int [] vertex_degree = new int[number_of_edges];

        //flatten the matrix
        for (int i =0,k=0;i<Vertices_Connection.length && k < flat_Vertices_Connection.length;i++) {
            for (int j=0;j<Vertices_Connection[i].length ;j++) {
                flat_Vertices_Connection [k] = Vertices_Connection[i][j];
                k++;
            }
        }

        //get distinct vertices
        int []  distinct_vertex = Arrays.stream(flat_Vertices_Connection).distinct().toArray();


        //use Brooks’ Theorem:
        //χ(G) ≤ Δ(G) +1, for any graph G
        //Δ(G) denotes the largest degree of any vertex in graph G.
        //χ(G) denotes the chromatic number of G.


        //compute vertex degree
        for (int i =0; i<distinct_vertex.length;i++) {
            int vertex_count=0;
            for (int j=0;j<flat_Vertices_Connection.length;j++) {
                if(flat_Vertices_Connection[j]==distinct_vertex[i])
                    vertex_count++;
            }
            if (i < Vertices_Connection.length) {
                vertex_degree[i] = vertex_count;
            }
        }


        //compute upper_bound
        int upper_bound = vertex_degree[0];

        // Find max value
        for (int i = 1; i < vertex_degree.length; i++) {
            if (vertex_degree[i] > upper_bound) {
                upper_bound = vertex_degree[i];
            }
        }


        //Print upper bound
        System.out.printf("The upper bound of the given graph is: %d\n",upper_bound+1);

        FinalData data = SharedData.getInstance().getFinalData();
        data.upper_bound = upper_bound+1;
        SharedData.getInstance().setFinalData(data);

    }

}

class ImprovedColour {

    // Number of vertices in the graph
    private static int V;
    private static int E;

    // Function to check if the current color assignment is safe for vertex v
    private static boolean isSafe(int v, int[][] graph, int[] color, int c) {
        for (int i = 0; i < V; i++) {
            for(int j = 0; j < 2; j++){
                if (graph[v][j] == 1 && c == color[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    // A recursive utility function to solve the coloring problem
    private static boolean graphColoringUtil(int[][] graph, int m, int[] color, int v) {
        if (v == V) {
            return true;
        }

        for (int c = 1; c <= m; c++) {
            if (isSafe(v, graph, color, c)) {
                color[v] = c;
                if (graphColoringUtil(graph, m, color, v + 1)) {
                    return true;
                }
                color[v] = 0;
            }
        }
        return false;
    }

    // Function to solve the m Coloring problem
    private static boolean graphColoring(int[][] graph, int m) {
        int[] color = new int[V];
        if (!graphColoringUtil(graph, m, color, 0)) {
            System.out.println("Solution does not exist");
            return false;
        }

        // Print the solution
        DrawnNodes[] colourNodes = SharedData.getInstance().getDrawnCircles();
        int largestColour = 0;
        System.out.println("Solution exists: Following are the assigned colors");
        for (int i = 0; i < V; i++) {
            //System.out.print(" " + color[i] + " ");
            if(largestColour < color[i]){
                largestColour = color[i];
            }
            colourNodes[V - i - 1].colour = color[i];
            System.out.println(color[i]);
        }
        SharedData.getInstance().setDrawnNodes(colourNodes);

        FinalData data = SharedData.getInstance().getFinalData();
        data.exact_chromatic_bound = largestColour;
        SharedData.getInstance().setFinalData(data);

        //System.out.println();
        //System.out.println("The chromatic number is: " + largestColour);

        return true;
    }

    // Function to read the graph from a file
    private static int[][] readGraphFromFile(){
        int number_of_edges = SharedData.getInstance().getNumberOfEdges();
        ColEdge[] edges = SharedData.getInstance().getEdges();
        int [][] Vertices_Connection = new int[number_of_edges][2];
        for(int i = 0; i < number_of_edges; i++){
            Vertices_Connection[i][0] = edges[i].u;
            Vertices_Connection[i][1] = edges[i].v;
        }

        return Vertices_Connection;
    }

    public static void main(){
        long start_time = System.nanoTime();
        int[][] graph = readGraphFromFile();
        V = SharedData.getInstance().getNumberOfVertices();
        int m = SharedData.getInstance().getNumberOfVertices(); // Number of colors
        graphColoring(graph, m);
        long end_time = System.nanoTime();
        long elapsedTime = (end_time - start_time) / 1000000000;
        System.out.println("Time taken: " + elapsedTime + " seconds");
    }
}
