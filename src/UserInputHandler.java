import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class UserInputHandler {

    public static void moveClosestNodeToCursor(int x, int y) {
        DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();
        moveClosestNodeToXY(nodes, x, y);
    }

    public static void moveClosestNodeToCursorUnderDistance(int x, int y, int distance) {
        DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();
        int closest_index = Utilities.get_closest_node(nodes, x, y);
        if(Utilities.get_node_distance(nodes[closest_index], x, y) > distance){}
        else{
        moveClosestNodeToXY(nodes, x, y);
        if (LabelWithNode.getListLabelledNode().contains(closest_index))
        LabelWithNode.updateLabelPosition(nodes, LabelWithNode.getLabelByNodeID(closest_index),closest_index) ;

    }
    }

    private static void moveClosestNodeToXY(DrawnNodes[] nodes, int x, int y) {
        int closest_index = Utilities.get_closest_node(nodes, x, y);
        nodes[closest_index].x = x ;
        nodes[closest_index].y = y ;
        SharedData.getInstance().setDrawnNodes(nodes);
    }

    public static void handleEscapeEvent(KeyCode code, Stage subStage) {
        if (code == KeyCode.ESCAPE) {
            DrawMenuApp.openMenuWindow(subStage);
        }
    }


}
