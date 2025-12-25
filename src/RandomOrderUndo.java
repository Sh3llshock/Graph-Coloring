import java.util.ArrayList;
import java.util.Stack;

public class RandomOrderUndo {

    // Array for the random node order
    private static int[] randomOrder;
    // Stack containing color history
    private static Stack<Integer> lastAction = new Stack<>();
    private static ArrayList<ArrayList<Integer>> VerticesColorTrack = NextNodeHint.getColorTracking();

    public static void initialiseRandomOrder() {
        randomOrder = RandomOrder.randomOrder; // Assuming RandomOrder.randomOrder is already generated
    }

    // Colors the next node in the random order that is generated
    public static void colorNextNode() {
        for (int node : randomOrder) {
            for (ArrayList<Integer> row : VerticesColorTrack) {
                if (row.get(0) == node && row.get(1) == 0) { // If not yet colored
                    ColorTracking(node, false); // Mark as colored
                    lastAction.push(node); // Add to undo stack
                    System.out.println("Node " + node + " has been colored.");
                    return;
                }
            }
        }
        System.out.println("No uncolored nodes left.");
    }

    // Uncolors the last node
    public static int undoLastAction() {
        if (!lastAction.isEmpty()) {
            int lastNode = lastAction.pop();
            ColorTracking(lastNode, true); // Undo action (uncolor)
            return lastNode;
        }
        System.out.println("No actions to undo.");
        return -1; // Indicate no action to undo
    }


    // Displays the color tracking table
    public static void showColorTrackingTable() {
        System.out.println("Color Tracking Table:");
        for (ArrayList<Integer> row : VerticesColorTrack) {
            int node = row.get(0);
            int isColored = row.get(1);
            System.out.println("Node " + node + " is " + (isColored == 1 ? "colored" : "uncolored"));
        }
    }

    // Displays the color history as a string
    public static String getColorHistory() {
        StringBuilder history = new StringBuilder("Color History:\n");
        Stack<Integer> tempStack = (Stack<Integer>) lastAction.clone();
        while (!tempStack.isEmpty()) {
            history.append("Node ").append(tempStack.pop()).append("\n");
        }
        return history.toString();
    }

    // Checks if there are actions to undo
    public static boolean hasUndoAction() {
        return !lastAction.isEmpty();
    }

    // Tracks coloring and uncoloring actions
    public static void ColorTracking(int vertex_input, boolean isUndoAction) {
        for (int i = 0; i < VerticesColorTrack.size(); i++) {
            if (VerticesColorTrack.get(i).get(0) == vertex_input) {
                if (!isUndoAction && VerticesColorTrack.get(i).get(1) == 0) {
                    // Color the vertex
                    VerticesColorTrack.get(i).set(1, 1);
                    System.out.println("Node " + vertex_input + " is now colored.");
                } else if (isUndoAction && VerticesColorTrack.get(i).get(1) == 1) {
                    // Uncolor the vertex
                    VerticesColorTrack.get(i).set(1, 0);
                    System.out.println("Node " + vertex_input + " is now uncolored.");
                }
                return;
            }
        }
        System.out.println("Node " + vertex_input + " not found in VerticesColorTrack.");
    }
}