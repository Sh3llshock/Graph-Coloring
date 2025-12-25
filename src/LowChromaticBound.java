import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LowChromaticBound {
    public static void main(String[] args) {
        computeGreedyLowChromaticBound();
    }

    public static void computeGreedyLowChromaticBound() {
        long startTime = System.nanoTime(); // Start the timer
        int degeneracy = calculateDegeneracy();

        FinalData data = SharedData.getInstance().getFinalData();
        if (data == null) {
            data = initializeFinalData();
        }

        data.lower_bound = degeneracy;
        System.out.println(degeneracy);
        SharedData.getInstance().setFinalData(data);
        long endTime = System.nanoTime(); // End the timer

        // Calculate and display the elapsed time in milliseconds
        long elapsedTime = (endTime - startTime) / 1000000;
        long elapsedTime2 = (endTime - startTime)/ 1000;
        System.out.println("Execution time: " + elapsedTime + " ms");
        System.out.println("Full Execution time: " + elapsedTime2 + "divided by 1k");
    }

    private static int calculateDegeneracy() {
        int degeneracy = 0;
        List<DrawnNodes> nodes = new ArrayList<>(Arrays.asList(SharedData.getInstance().getDrawnCircles()));

        while (!nodes.isEmpty()) {
            int minDegree = Integer.MAX_VALUE;
            int minDegreeIndex = -1;

            for (int i = 0; i < nodes.size(); i++) {
                int degree = nodes.get(i).connections.size();
                if (degree < minDegree) {
                    minDegree = degree;
                    minDegreeIndex = i;
                }
            }

            degeneracy = Math.max(degeneracy, minDegree);
            nodes.remove(minDegreeIndex);
        }

        return degeneracy;
    }

    private static FinalData initializeFinalData() {
        FinalData data = new FinalData();
        data.lower_bound = 0;
        data.upper_bound = Integer.MAX_VALUE;
        data.exact_chromatic_bound = 0;

        int numberOfVertices = SharedData.getInstance().getNumberOfVertices();
        data.colours = new int[numberOfVertices];
        Arrays.fill(data.colours, 0);

        data.exact_chrom_bound_completed_till_end = false;
        return data;
    }
}
