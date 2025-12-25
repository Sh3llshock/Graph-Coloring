import java.util.ArrayList;

public class NodeLayoutGenerationPreRefactor {

    //I broke it just now and I honestly do not know what I did wrong :( - Crow (Noah)
    public static void generatePoints_YifanHu_Method(){
        // basing the following layout generator on the Yifan Hu algorithm as described here http://yifanhu.net/PUB/graph_draw.pdf

    }

    public static void generatePoints_FRMethod( int iterations, boolean debug) {
        // basing the following layout generator on the Fruchterman-Reingold algorithm (further research suggests it may be sub-optimal for our purpoises)
        final double STEP_MULTIPLIER = 4.0; // Multiplier for the step size
        int max_height = 700; // Max height of the area to layout the graph
        int max_width = 700;    // Width and height of the area to layout the graph
        int optimal_distance = (int) Math.sqrt((double) (max_height * max_width) /(1 + SharedData.getInstance().getNumberOfVertices()));          // Optimal distance between nodes
        //int optimal_distance = 1000;
        //int iterations = 100; // Number of iterations to run
        double max_step; // limit for the step size

        SharedData sharedData = SharedData.getInstance();
        int numVertices = sharedData.getNumberOfVertices();

        DrawnNodes[] nodes = new DrawnNodes[numVertices]; // Initialize the circles array

        ColEdge[] edges = sharedData.getEdges();

        final int CIRCLE_RADIUS = 20;

        // Initialize the circles array using random values
        /*if(!debug) {
            for (int i = 0; i < numVertices; i++) {
                nodes[i] = new DrawnNodes();
                nodes[i].x = (int) (Math.random() * max_width);
                nodes[i].y = (int) (Math.random() * max_height);
                nodes[i].connections = new ArrayList<>();
                nodes[i].radius = CIRCLE_RADIUS;
                nodes[i].colour = 0;
            }
        }*/

        //else{

        //nodes = sharedData.getDrawnCircles();
        //}
        NodeLayoutGenerationPreRefactor.generatePoints_DaniilCircleMethod();
        nodes = SharedData.getInstance().getDrawnCircles();

        for(int i = 0; i < iterations; i++){
            max_step = STEP_MULTIPLIER * (1.0 - (double) i / iterations);
            //so far 3/5 seem to be the best multipliers
            //calculating repulsive forces
            //refactor this to use the Utility functions

            for(int j = 0; j < numVertices; j++){
                for(int k = 0; k < numVertices; k++){
                    double x_diff = nodes[j].x - nodes[k].x;
                    double y_diff = nodes[j].y - nodes[k].y;
                    double distance = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
                    if(distance == 0){
                        distance = 1;
                    }
                    double force = optimal_distance / distance;
                    double x_force = force * x_diff / distance;
                    double y_force = force * y_diff / distance;

                    nodes[j].x += x_force * max_step * 0.5;
                    nodes[j].y += y_force * max_step * 0.5;
                }
            }

            //calculating attractive forces
            //refactor this to use Utility function
            for (int j = 0; j < numVertices; j++) {
                for (int k : nodes[j].connections) {
                    if (j < k) {
                        double x_diff = nodes[j].x - nodes[k].x;
                        double y_diff = nodes[j].y - nodes[k].y;
                        double distance = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
                        if (distance > 0) {
                            double force = distance / optimal_distance;
                            double x_force = force * x_diff / distance;
                            double y_force = force * y_diff / distance;
                            nodes[j].x -= (x_force * max_step);
                            nodes[j].y -= (y_force * max_step);
                        }
                    }
                }
            }

            /*for(int j = 0; j < numVertices; j++) {
                for(int k = 0; k < numVertices; k++) {
                    if(j != k && (nodes[j].connections[0] == k || nodes[j].connections[1] == k)) {
                        double x_diff = nodes[j].x - nodes[k].x;
                        double y_diff = nodes[j].y - nodes[k].y;
                        double distance = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
                        if (distance > 0) {
                            double force = distance / optimal_distance;
                            double x_force = force * x_diff / distance;
                            double y_force = force * y_diff / distance;
                            nodes[j].x -= (int) (x_force * max_step);
                            nodes[j].y -= (int) (y_force * max_step);
                        }
                    }
                }
            }*/

            //making sure the nodes don't go out of bounds
            for(int j = 0; j < numVertices; j++){
                if(nodes[j].x < 0){
                    nodes[j].x = 0;
                }
                if(nodes[j].x > max_width){
                    nodes[j].x = max_width;
                }
                if(nodes[j].y < 0){
                    nodes[j].y = 0;
                }
                if(nodes[j].y > max_height){
                    nodes[j].y = max_height;
                }
            }

            //push the nodes away from the edges of the board
            for(int j = 0; j < numVertices; j++){
                double x_diff = 350 - nodes[j].x;
                double y_diff = 350 - nodes[j].y;
                double distance = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
                if(distance == 0){
                    distance = 1;
                }
                double x_force = 2 * x_diff / distance;
                double y_force = 2 * y_diff / distance;
                nodes[j].x += (int)x_force * max_step;
                nodes[j].y += (int)y_force * max_step;
            }

        }
        sharedData.setDrawnNodes(nodes);
    }

    public static void generatePoints_DaniilCircleMethod() {
        //basing the following layout generator on the idea proposed by Daniil
        SharedData sharedData = SharedData.getInstance();
        int numberOfVertices = sharedData.getNumberOfVertices();
        DrawnNodes[] circles = new DrawnNodes[numberOfVertices];
        double angleIncrement = 2 * Math.PI / numberOfVertices;
        int radius = 200; //
        double centerX = 300;
        double centerY = 300;

        //initalising all the nodes
        for (int i = 0; i < numberOfVertices; i++) {
            circles[i] = new DrawnNodes();
            circles[i].x = centerX + (radius * Math.cos(i * angleIncrement));
            circles[i].y = centerY + (radius * Math.sin(i * angleIncrement));
            circles[i].radius = 20;
        }
        sharedData.setDrawnNodes(circles);
        UpdateConnections.updateConnections(sharedData.getEdges());
    }

    public static void generatePoints_OpposingNodesCircleMethod() {
    }

    public static void initialiseRandomPointPositions(){
        int max_height = 700; // Max height of the area to layout the graph
        int max_width = 700;    // Width and height of the area to layout the graph
        SharedData sharedData = SharedData.getInstance();
        int numVertices = sharedData.getNumberOfVertices();
        DrawnNodes[] nodes = new DrawnNodes[numVertices];
        ColEdge[] edges = sharedData.getEdges();
        final int CIRCLE_RADIUS = 20;

        for (int i = 0; i < numVertices; i++) {
            nodes[i] = new DrawnNodes();
            nodes[i].x = (Math.random() * max_width);
            nodes[i].y = (Math.random() * max_height);
            nodes[i].connections = new ArrayList<>();
            nodes[i].radius = CIRCLE_RADIUS;
        }
    }

    static void chosenGenerationMethod(int method, boolean debug){
        switch (method){
            case 1:
                NodeLayoutGenerationPreRefactor.generatePoints_FRMethod(200, debug);
                break;
            case 2:
                NodeLayoutGenerationPreRefactor.generatePoints_DaniilCircleMethod();
                break;
        }
    }

}
