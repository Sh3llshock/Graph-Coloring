import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

// DIDNT DELETE UNUSED CODE JUST IN CASE
public class undoHint {
    /*public static int[][] graph = {
            {0, 1, 1, 0, 0, 0},
            {1, 0, 1, 1, 0, 0},
            {1, 1, 0, 0, 1, 0},
            {0, 1, 0, 0, 1, 1},
            {0, 0, 1, 1, 0, 1},
            {0, 0, 0, 1, 1, 0}
    };*/
    static int[][] graph = GraphData.getInstance().getAdjacencyMatrix();

    public static int numVertices = graph.length;

    public static List<int[]> uniqueColorings = new ArrayList<>();
    public static int chromaticNumber = 3;
    public static int[] colours = new int[numVertices];
    public static int[] midGameColors = {1, 2, 1, 1, 1, 1};

    public static void main(String[] args) {
        mainMethodCleaner();
    }


    public static void mainMethodCleaner(){


        findAllColorings(colours, 0, chromaticNumber);
        for (int[] i : uniqueColorings) {
            System.out.println(Arrays.toString(i));
        }


        // visual part that asks for input
        System.out.println("---HINT GEN---");
        System.out.println("Current graph : " + Arrays.toString(midGameColors));
        Scanner input = new Scanner(System.in);
        int color = 0;
        int numOfColoredSoFar = 0;
        String colorException = "";
        for(int i = 0; i < numVertices; i++){
            colorException = "";
            System.out.println("Node " + i + " Color: ");
            try{
                color = input.nextInt();
            } catch (Exception e){
                colorException = input.nextLine();
            }
            if (colorException.equals("h")){

                int hintColour = getHintColor(numOfColoredSoFar, midGameColors);
                while (hintColour == -1){
                    findAllColorings(colours, 0, chromaticNumber + 1);
                    hintColour = getHintColor(numOfColoredSoFar, midGameColors);
                }
                System.out.println("Hint: Use color " + hintColour);

                System.out.println("Node "+ i + " Color: ");
                try{
                    color = input.nextInt();
                } catch (Exception e){
                    colorException = input.nextLine();
                }
            }
            else{
                numOfColoredSoFar += 1;
            }

            midGameColors[i] = color;
        }
        System.out.println("Your graph: " + Arrays.toString(midGameColors));
    }

    // Check if the current coloring is allowed
    public static boolean isValidColoring(int[] colors) {
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (graph[i][j] == 1 && colors[i] == colors[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void findAllColorings(int[] colours, int vertex, int maxColors) {
        if (vertex == numVertices) {
            if (isValidColoring(colours)) {
                int[] colouring = canonicalForm(colours);
                boolean isTrue = false;

                if(uniqueColorings.isEmpty()){
                    uniqueColorings.add(colouring);
                }
                for (int i = 0; i < uniqueColorings.size(); i++) {
                    if (!compareFirstNElements(colouring, uniqueColorings.get(i), vertex)) {
                        isTrue = true;
                    }
                }
                if(isTrue){
                    uniqueColorings.add(colouring);
                }
            }
            return;
        }

        for (int colour = 1; colour <= maxColors; colour++) {
            colours[vertex] = colour;
            findAllColorings(colours, vertex + 1, maxColors);
        }
    }

    public static int[] canonicalForm(int[] colors) {
        int[] colorMapping = new int[colors.length];
        int nextColor = 1;
        int[] canonicalColors = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            if (colorMapping[colors[i]] == 0) {
                colorMapping[colors[i]] = nextColor++;
            }
            canonicalColors[i] = colorMapping[colors[i]];
        }
        return (canonicalColors);
    }
    public static int[] canonicalFormSecond(int[] colors) {
        int[] colorMapping = new int[colors.length];
        int nextColor = 1;
        int[] canonicalColors = new int[colors.length];
        for (int i = 0; i < colors.length; i++) {
            if (colorMapping[colors[i] - 1] == 0) {
                colorMapping[colors[i] - 1] = nextColor++;
            }
            canonicalColors[i] = colorMapping[colors[i] -1];
        }
        return (canonicalColors);
    }

    public static int getHintColor(int hintIndex, int[] current){
        findAllColorings(colours, 0, chromaticNumber);

        for (int[] i : uniqueColorings) {
            System.out.println(Arrays.toString(i));
        }

        System.out.println("---HINT METHOD---");
        if (hintIndex == 0){
            return 1;
        }
        System.out.println("DEBUGGING: current je " + Arrays.toString(current));
        //String current = "[1, 2, 3, 0, 0]";
        // int index = getIndex(current, 0);
        int[] currentCopy = new int[hintIndex];
        System.arraycopy(current, 0, currentCopy, 0, hintIndex); // example: "[1, 2, 3]"
        //DONT-REMOVE currentCopy = colourReassignmentSurgery(currentCopy);
        //DONT-REMOVE currentCopy = canonicalFormSecond(currentCopy);

        System.out.println("DEBUGGING: currentCopy je " + Arrays.toString(currentCopy));
        System.out.println("uniqueColorings: " + Arrays.toString(uniqueColorings.toArray()));

        for (int[] i : uniqueColorings){
            if(compareFirstNElements(currentCopy, i, hintIndex) == true){
                System.out.println(Arrays.toString(i) + " found");

                int hintNumber = i[currentCopy.length];
                return hintNumber;
            }
        }
        return -1;

    }
    public static int [] colourReassignmentSurgery(int[] colourPath){
        int [] usedColours = new int[colourPath.length];
        boolean usedColour;
        int amountOfColours = 0;

        for (int i = 0; i < colourPath.length; i++) {
            usedColour = false;
            int colour = colourPath[i];

            for (int j = 0; j < colourPath.length; j++) {
                int used = usedColours[j];
                if(colour == used){
                    usedColour = true;
                }
            }

            if (!usedColour){
                usedColours[amountOfColours] = colourPath[i];
                amountOfColours++;
            }
        }

        int[] nonSorted = new int[amountOfColours];

        for (int i = 0; i < amountOfColours; i++) {
            nonSorted[i] = usedColours[i];
        }

        int[] nonSorted2 = new int[nonSorted.length];
        System.arraycopy(nonSorted, 0, nonSorted2, 0, nonSorted.length);

        int[] Sorted = bubbleSort(nonSorted2);

        int[][] usedColoursHash = new int[amountOfColours][2];

        for (int i = 0; i < amountOfColours; i++) {
            usedColoursHash[i][0] =  Sorted[i];
            usedColoursHash[i][1] = i + 1;
        }

        int[] revampedColourPath = new int[colourPath.length];

        for (int i = 0; i < usedColoursHash.length; i++) {
            int oldValue = usedColoursHash[i][0];
            int newValue = usedColoursHash[i][1];

            for (int j = 0; j < colourPath.length; j++) {
                if(colourPath[j] == oldValue){
                    revampedColourPath[j] = newValue;
                }
            }

        }
        return revampedColourPath;
    }

    public static int[] bubbleSort(int[] colourNonSorted) {
        int n = colourNonSorted.length;
        boolean change;

        for (int i = 0; i < n - 1; i++) {
            change = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (colourNonSorted[j] > colourNonSorted[j + 1]) {
                    int temp = colourNonSorted[j];
                    colourNonSorted[j] = colourNonSorted[j + 1];
                    colourNonSorted[j + 1] = temp;
                    change = true;
                }
            }
            if (!change) {
                break;
            }
        }

        return colourNonSorted;
    }

    public static int getIndex(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1; // if not found
    }
    public static boolean compareFirstNElements(int[] array1, int[] array2, int n) {
        for (int i = 0; i < n; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }


}
