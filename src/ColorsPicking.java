import java.util.HashSet;
import java.util.Set;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ColorsPicking{
/*
    private static final int NumberColors = 4; //don't know how to generate colors depends on number of colors
    private static Color[] availableColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.PURPLE};

    @Override
    public void start(Stage primaryStage) {
        //Create a circle
        Circle circle = new Circle(50); // create circle
        circle.setFill(Color.BLACK); //set circle black

        //Create Popup
        ContextMenu contextMenu = ColorMenuPopup(circle);

        //Click on the circle
    circle.setOnMouseClicked(event -> {
        contextMenu.show(circle, event.getScreenX(), event.getScreenY());
    });

    // Create the window (Tạo giao diện)
        StackPane root = new StackPane(circle);
        Scene scene = new Scene(root, 600, 300);
        primaryStage.setTitle("Color Project");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static ContextMenu ColorMenuPopup(Circle circle) {
        ContextMenu colorMenu = new ContextMenu();

        for (Color color : availableColors) { //Import colors in "availableColors" inside "color"
            MenuItem colorItem = new MenuItem();
            Rectangle colorIcon = new Rectangle(20, 20, color); // Create colors icon (Tạo icon màu cho mỗi mục)
            colorItem.setGraphic(colorIcon);

            colorItem.setOnAction(event -> {
                circle.setFill(color); // Change the node color (Đổi màu hình tròn thành màu đã chọn)
            });

            colorMenu.getItems().add(colorItem);
        }
        return colorMenu;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/

/*
public static void openColourPicker(GraphicsContext gc, int x, int y, int closestNodeIndex) {
    int colour_count = SharedData.getInstance().getFinalData().player_chromatic_bound + 1;

    ColourPicker[] circles = new ColourPicker[colour_count];
    double angleIncrement = 2 * Math.PI / colour_count;
    int radius = 100;

    // Initializing all the nodes
    for (int i = 0; i < colour_count; i++) {
        circles[i] = new ColourPicker();
        circles[i].x = (int) (x + 200 + (radius * Math.cos(i * angleIncrement)));
        circles[i].y = (int) (y + (radius * Math.sin(i * angleIncrement)));
        circles[i].radius = 20;
        circles[i].colour = i;
    }

    // Drawing the circles
    for (int i = 0; i < colour_count; i++) {
        double hue = (320.0 / colour_count) * i;
        gc.setFill(Color.hsb(hue, 1.0, 1.0));
        gc.fillOval(
                circles[i].x - circles[i].radius / 2.0,
                circles[i].y - circles[i].radius / 2.0,
                circles[i].radius,
                circles[i].radius
        );
    }
} */

//Use to get the colors (The same colors in the draw graph method)
    private static Color getColorForIndex(int index) {
        FinalData data = SharedData.getInstance().getFinalData();
        double hue = (320.0 / data.exact_chromatic_bound) * index;
        return Color.hsb(hue, 1.0, 1.0);
    }

    public static ContextMenu ColorMenuPopup(DrawnNodes node) {
        ContextMenu contextMenu = new ContextMenu();
        DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();

        // Find used colors
        Set<Integer> usedColors = new HashSet<>();
        for (DrawnNodes existingNode : nodes) {
            if (existingNode != null && existingNode.colour != -1) {
                usedColors.add(existingNode.colour);
            }
        }

        int colour_count = usedColors.size() + 1; //Colors count

        // Generate menu item for each color
        for (int i = 0; i < colour_count; i++) {
            MenuItem colorItem = new MenuItem();

            Rectangle colorIcon = new Rectangle(20, 20, getColorForIndex(i));
            colorItem.setGraphic(colorIcon);

            final int colorIndex = i;
            colorItem.setOnAction(event -> {
                node.colour = colorIndex;
                DrawnNodes[] updatedNodes = SharedData.getInstance().getDrawnCircles();
                SharedData.getInstance().setDrawnNodes(updatedNodes);
            });

            contextMenu.getItems().add(colorItem);
        }

        return contextMenu;
    }



}