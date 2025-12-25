import java.util.*;

public class JonesPlassmann {

    //done sequentially not parallel yet
    public static int[] jonesPlassmann(List<List<Integer>> graph) {

        // assign random weights to the nodes
        int n = graph.size();
        double[] weight = new double[n];

        Random random = new Random();

        for (int i = 0; i < n; i++) {
            weight[i] = random.nextDouble(n);
        }

        //list for the nodes
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(i);
        }
        // sorted by descending weight
        nodes.sort((a, b) -> {
            if (weight[a] < weight[b]) {
                return -1;
            }
            if (weight[a] > weight[b]) {
                return 1;
            }
            return Integer.compare(a, b);
        });

        int[] colour = new int[n];

        for (int node : nodes) {
            //array to track used colors
            boolean[] usedColour = new boolean[n+1];

            //marks colours used by neighbours
            for (int adjacentNode : graph.get(node)) {
                int neighbourColour = colour[adjacentNode];
                if (neighbourColour != 0 ) {
                   usedColour[neighbourColour] = true;
                }
            }
            //&assigns the smallest not used colour
            colour[node] = smallestAvailableColour(usedColour);
        }
        return colour;
    }

    //finds smallest color not used
    private static int smallestAvailableColour(boolean[] usedColour) {
        for (int i = 1; i < usedColour.length; i++) {
            if (!usedColour[i]) {
                return i;
            }
        }
        return usedColour.length;
    }

    public static void main(String[] args) {
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            graph.add(new ArrayList<>());
        }

       /* graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(1).add(0);
        graph.get(1).add(3);
        graph.get(2).add(0);
        graph.get(2).add(4);
        graph.get(3).add(1);
        graph.get(3).add(4);
        graph.get(4).add(2);
        graph.get(4).add(3); */

        graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(0).add(3);
        graph.get(1).add(0);
        graph.get(1).add(2);
        graph.get(2).add(0);
        graph.get(2).add(1);
        graph.get(3).add(0);

        int[] colour = jonesPlassmann(graph);
        for (int i = 0; i < colour.length; i++) {
            System.out.println("Node " + i + ": " + graph.get(i));
        }

       /* System.out.println("node : colour");
        int[] colour = jonesPlassmann(graph);
        for (int i = 0; i < colour.length; i++) {
            System.out.println("  " + i + "  : "+ colour[i]) */
    }
}
