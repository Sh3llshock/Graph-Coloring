import java.util.*;

public class ComparabilityColouring {

    public static int[] colourComparability(List<List<Integer>> graph) {
        int numberOfNodes = graph.size();
        int[] colors = new int[numberOfNodes];

        // Compute degrees of all vertices
        int[] degrees = new int[numberOfNodes];
        for (int u = 0; u < numberOfNodes; u++) {
            degrees[u] = graph.get(u).size();
        }

        // Create an array of vertex indices
        Integer[] nodes = new Integer[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            nodes[i] = i;
        }

        //Sort vertices by descending degree
        Arrays.sort(nodes, new Comparator<Integer>() {

            public int compare(Integer a, Integer b) {
                return Integer.compare(degrees[a], degrees[b]); // descending order
            }
        });

        // Assign colors greedily
        for (int i = 0; i < numberOfNodes; i++) {
            int vertex = nodes[i];

            // Mark which colors are used by adjacent vertices
            boolean[] usedColors = new boolean[numberOfNodes + 1]; // Colors start at 1

            for (int neighbor : graph.get(vertex)) {
                if (colors[neighbor] != 0) {
                    usedColors[colors[neighbor]] = true;
                }
            }

            // Assign the smallest available color
            int assignedColor = 1;
            while (usedColors[assignedColor]) {
                assignedColor++;
            }
            colors[vertex] = assignedColor;
        }

        return colors;
    }
}