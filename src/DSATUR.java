import java.util.ArrayList;

public class DSATUR {
    public static void main(String[] args) {

        int[][] adjacencyMatrix = {
                {0, 1, 1, 1, 0, 0},
                {1, 0, 1, 0, 0, 0},
                {1, 1, 0, 0, 0, 0},
                {1, 0, 0, 0, 1, 1},
                {0, 0, 0, 1, 0, 1},
                {0, 0, 0, 1, 1, 0}
        };

        int[] colors = new int[adjacencyMatrix.length];
        int[] degree = new int[adjacencyMatrix.length];
        int[] saturation = new int[adjacencyMatrix.length];

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix.length; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    degree[i]++;
                }
            }
        }
        int biggestDegID = 0;
        for (int i = 0; i < degree.length-1; i++) {
            if (degree[i] > degree[i+1]){
                biggestDegID = i;
            }
        }
        colors[biggestDegID] = 1;
        //System.out.println("Biggest degree nodeID: " + biggestDegID);

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[biggestDegID][i] == 1 && colors[i] == 0) {
                saturation[i]++;
            }
        }

        /*System.out.println("Biggest degree nodeID: " + saturation[biggestDegID]);

        for(int i = 0; i < adjacencyMatrix.length; i++){
            System.out.println("nodeID: " + i + " color: " + colors[i] + " saturation: " + saturation[i]);
        }
        System.out.println("---MAIN LOOP START---");
*/
        while (uncoloredNodesLeft(colors) == true) {

            int maxSat = -1;
            for (int i = 0; i < saturation.length; i++) {
                if(saturation[i] > maxSat && colors[i] == 0){
                    maxSat = saturation[i];
                }
            }

            ArrayList<Integer> nodesWithMaxSat = new ArrayList<>();
            for (int i = 0; i < saturation.length; i++) {
                if(colors[i] == 0 && saturation[i] == maxSat){
                    nodesWithMaxSat.add(i);
                }
            }

            int bestNode = nodesWithMaxSat.get(0);
            int bestDegree = degree[bestNode];
            for (int c : nodesWithMaxSat) {
                if (degree[c] > bestDegree) {
                    bestDegree = degree[c];
                    bestNode = c;
                }
            }


            ArrayList<Integer> usedColors = new ArrayList<>();

            for (int i = 0; i < adjacencyMatrix.length; i++) {
                if (adjacencyMatrix[bestNode][i] == 1 && colors[i] != 0) {
                    if (!usedColors.contains(colors[i])) {
                        usedColors.add(colors[i]);
                    }
                }
            }

            int chosenColor = 1;
            while (usedColors.contains(chosenColor)) { chosenColor++;}

            colors[bestNode] = chosenColor;

            for (int i = 0; i < adjacencyMatrix.length; i++) {
                if (adjacencyMatrix[bestNode][i] == 1 && colors[i] == 0) {
                    saturation[i]++;
                }
            }
        }
        System.out.println("---FINAL---");
        for(int i = 0; i < adjacencyMatrix.length; i++){
            System.out.println("nodeID: " + i + " color: " + colors[i]);
        }

        int maxColor = 0;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] > maxColor){
                maxColor = colors[i];
            }
        }
        System.out.println("Colors used: " + maxColor);


    }

    static boolean uncoloredNodesLeft(int[] colors){
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == 0) {return true;}
        }
        return false;
    }

}
