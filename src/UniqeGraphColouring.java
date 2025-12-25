import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UniqeGraphColouring {
    public int numVertices = SharedData.getInstance().getNumberOfVertices(); // node number
    public DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();
    public List<String> uniqueColorings = new ArrayList<>();
    public int chromaticNumber = 3; // this needs to be changed later
    public int[] colours = new int[numVertices];



    // Check if the current coloring is allowed
    public boolean isValidColoring(int[] colours) {
        boolean Validity = true;

        for (int i = 0; i < nodes.length; i++) {
            SharedData.getInstance().setNodeValidity(i, true);
        }

        for (int i = 0; i < numVertices; i++) {
            int[] cons = nodes[i].connections.stream().mapToInt(Integer::intValue).toArray();

            for (int j = 0; j < cons.length; j++) {
                if (colours[i] == colours[cons[j]]) {
                    SharedData.getInstance().setNodeValidity(i, false);
                    SharedData.getInstance().setNodeValidity(cons[j], false);
                    Validity = false;
                }
            }
        }
        return Validity;
    }

    //  Find all unique colorings using brute force
    public void findAllColorings(int[] colours, int vertex, int maxColors) {
        if (vertex == numVertices) {
            if (isValidColoring(colours)) {
                String colouring = canonicalForm(colours);
                if (!uniqueColorings.contains(colouring)) {
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

    // Create a canonical form of coloring using arrays
    public String canonicalForm(int[] colors) {
        int[] colorMapping = new int[numVertices];
        int nextColor = 1;
        int[] canonicalColors = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            if (colorMapping[colors[i]] == 0) {
                colorMapping[colors[i]] = nextColor++;
            }
            canonicalColors[i] = colorMapping[colors[i]];
        }
        return Arrays.toString(canonicalColors);
    }
}
