import java.util.Arrays;
public class Utilities {

    //data type agnostic swapping function
    public static void swap(Object[] arr, int i, int j) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    //distance between two nodes
    public static double distance_nodes(DrawnNodes a, DrawnNodes b){
        double x_diff = a.x - b.x;
        double y_diff = a.y - b.y;
        return Math.sqrt(x_diff * x_diff + y_diff * y_diff);
    }

    public static double distance_coords(double x1, double y1, double x2, double y2){
        double x_diff = x1 - x2;
        double y_diff = y1 - y2;
        return Math.sqrt(x_diff * x_diff + y_diff * y_diff);
    }

    public static int get_closest_node(DrawnNodes[] nodes, double x, double y){
        int closest_node = 0;
        double min_distance = Double.MAX_VALUE;
        for(int i = 0; i < nodes.length; i++){
            int offset = (int)nodes[i].radius / 2;
            double distance =distance_coords(x, y, nodes[i].x + offset, nodes[i].y + offset);
            if(distance < min_distance){
                min_distance = distance;
                closest_node = i;
            }
        }
        return closest_node;
    }

    public static int get_node_distance(DrawnNodes node, double x, double y){
        int distance = 100000;
        distance = (int)distance_coords(x, y, node.x + 10, node.y + 10);
        return distance;
    }

    public static double get_node_distance_double(DrawnNodes node, double x, double y){
        double distance = 100000;
        distance = distance_coords(x, y, node.x + 10, node.y + 10);
        return distance;
    }


    public static DrawnNodes[] removeElement(DrawnNodes[] nodes, int valueToRemove) {
            if (nodes == null || nodes.length == 0) {
                return nodes;
            }

            DrawnNodes[] newNodes = new DrawnNodes[nodes.length - 1];

            // Copy elements to newArray, skipping the removeIndex
            for (int i = 0, j = 0; i < nodes.length; i++) {
                if (i != valueToRemove) {
                    newNodes[j++] = nodes[i];
                }
            }

            return newNodes;
        }

}
