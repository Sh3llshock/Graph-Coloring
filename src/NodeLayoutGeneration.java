import java.util.ArrayList;

public class NodeLayoutGeneration {

    private static final double STEP_MULTIPLIER = 100.0;
    private static final int MAX_HEIGHT = 600;
    private static final int MAX_WIDTH = 600;
    private static final int CIRCLE_RADIUS = 20;
    private static final int INITIAL_RADIUS = 200;
    private static final double CENTER_X = 300;
    private static final double CENTER_Y = 300;

    public static void generatePoints_YifanHu_Method() {
        // Placeholder for Yifan Hu algo
        // Reference: http://yifanhu.net/PUB/graph_draw.pdf
    }

    public static void generatePoints_FRMethod( int iterations) {
        long startTime = System.nanoTime();
        // basing the following layout generator on the Fruchterman-Reingold algorithm
        SharedData sharedData = SharedData.getInstance();

        int optimal_distance = (int) (0.5 * Math.sqrt((double) (MAX_HEIGHT * MAX_WIDTH) /(1 + sharedData.getNumberOfVertices())));          // Optimal distance between nodes
        double max_step; // limit for the step size
        double gravityConstant = 0.001;
        int numVertices = sharedData.getNumberOfVertices();

        DrawnNodes[] nodes; // Initialize the circles array
        NodeLayoutGeneration.generatePoints_DaniilCircleMethod();
        nodes = sharedData.getDrawnCircles();

        for(int i = 0; i < iterations; i++){
            max_step = STEP_MULTIPLIER * (1.0 - (double) i / iterations);
            calculateRepulsiveForces(nodes, numVertices, optimal_distance, max_step);
            calculateAttractiveForces(nodes, numVertices, optimal_distance, max_step);
            applyGravity(nodes, numVertices, gravityConstant);
            enforceBounds( nodes, numVertices);
            applyDisplacements(nodes, numVertices, max_step);
        }
        sharedData.setDrawnNodes(nodes);
        long end_time = System.nanoTime();
        long passed_time = end_time - startTime;
        System.out.println("Total passed time: " + passed_time);
    }

    private static void applyGravity(DrawnNodes[] nodes, int numVertices, double gravityConstant) {
        for (int i = 0; i < numVertices; i++) {
            double x_diff = CENTER_X - nodes[i].x;
            double y_diff = CENTER_Y - nodes[i].y;
            nodes[i].dispX += gravityConstant * x_diff;
            nodes[i].dispY += gravityConstant * y_diff;
        }
        SharedData.getInstance().setDrawnNodes(nodes);
    }

    private static void calculateAttractiveForces(DrawnNodes[] nodes, int numVertices, int optimalDistance, double maxStep) {
        for (int i = 0; i < numVertices; i++) {
            for (int connection : nodes[i].connections) {
                if (i < connection) {
                    double x_diff = nodes[i].x - nodes[connection].x;
                    double y_diff = nodes[i].y - nodes[connection].y;
                    double distance = Math.max(Math.sqrt(x_diff * x_diff + y_diff * y_diff), 0.01);
                    double force = (distance * distance) / optimalDistance;
                    double dx = (x_diff / distance) * force;
                    double dy = (y_diff / distance) * force;
                    nodes[i].dispX -= dx;
                    nodes[i].dispY -= dy;
                    nodes[connection].dispX += dx;
                    nodes[connection].dispY += dy;
                }
            }
        }
        SharedData.getInstance().setDrawnNodes(nodes);
    }

    private static void calculateRepulsiveForces(DrawnNodes[] nodes, int numVertices, int optimalDistance, double maxStep) {
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                double xDiff = nodes[i].x - nodes[j].x;
                double yDiff = nodes[i].y - nodes[j].y;
                double distance = Math.max(Math.sqrt(xDiff * xDiff + yDiff * yDiff), 0.01);

                double force = (optimalDistance * optimalDistance) / distance;

                double dx = (xDiff / distance) * force;
                double dy = (yDiff / distance) * force;

                nodes[i].dispX += dx;
                nodes[i].dispY += dy;
                nodes[j].dispX -= dx;
                nodes[j].dispY -= dy;
            }
        }
        SharedData.getInstance().setDrawnNodes(nodes);
    }

    private static void applyDisplacements(DrawnNodes[] nodes, int numVertices, double maxStep) {
        for (int i = 0; i < numVertices; i++) {
            double dispLength = Math.sqrt(nodes[i].dispX * nodes[i].dispX + nodes[i].dispY * nodes[i].dispY);

            if (dispLength > 0) {
                double limitedDispX = nodes[i].dispX / dispLength * Math.min(dispLength, maxStep);
                double limitedDispY = nodes[i].dispY / dispLength * Math.min(dispLength, maxStep);

                nodes[i].x += limitedDispX;
                nodes[i].y += limitedDispY;
            }

            nodes[i].dispX = 0;
            nodes[i].dispY = 0;
        }
    }

    private static void enforceBounds(DrawnNodes[] nodes, int numVertices) {
        double margin = 100; // Distance from the edge where the boundary force starts to act
        for (int j = 0; j < numVertices; j++) {
            double dx = 0, dy = 0;
            if (nodes[j].x < margin) {
                dx = margin - nodes[j].x;
            } else if (nodes[j].x > MAX_WIDTH - margin) {
                dx = (MAX_WIDTH - margin) - nodes[j].x;
            }
            if (nodes[j].y < margin) {
                dy = margin - nodes[j].y;
            } else if (nodes[j].y > MAX_HEIGHT - margin) {
                dy = (MAX_HEIGHT - margin) - nodes[j].y;
            }
            nodes[j].dispX += dx;
            nodes[j].dispY += dy;
        }
    }

    public static void generatePoints_DaniilCircleMethod() {
        SharedData sharedData = SharedData.getInstance();
        int numVertices = sharedData.getNumberOfVertices();
        DrawnNodes[] circles = new DrawnNodes[numVertices];
        double angleIncrement = 2 * Math.PI / numVertices;

        for (int i = 0; i < numVertices; i++) {
            circles[i] = new DrawnNodes();
            circles[i].x = CENTER_X + (INITIAL_RADIUS * Math.cos(i * angleIncrement)) + (Math.random() - 0.5) * 20;
            circles[i].y = CENTER_Y + (INITIAL_RADIUS * Math.sin(i * angleIncrement)) + (Math.random() - 0.5) * 20;
            circles[i].radius = CIRCLE_RADIUS;
        }

        sharedData.setDrawnNodes(circles);
        UpdateConnections.updateConnections(sharedData.getEdges());
    }

    public static void initialiseRandomPointPositions() {
        SharedData sharedData = SharedData.getInstance();
        int numVertices = sharedData.getNumberOfVertices();
        DrawnNodes[] nodes = new DrawnNodes[numVertices];

        for (int i = 0; i < numVertices; i++) {
            nodes[i] = new DrawnNodes();
            nodes[i].x = Math.random() * MAX_WIDTH;
            nodes[i].y = Math.random() * MAX_HEIGHT;
            nodes[i].dispX = 0;
            nodes[i].dispY = 0;
            nodes[i].radius = CIRCLE_RADIUS;
            nodes[i].connections = new ArrayList<>();
        }

        sharedData.setDrawnNodes(nodes);
    }

    public static void chosenGenerationMethod(int method, boolean debug) {
        //funny new trick
        switch (method) {
            case 1 -> generatePoints_FRMethod(100);
            case 2 -> generatePoints_DaniilCircleMethod();
        }
    }

}

