import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The GraphPreviewStack class provides functionalities to create visual previews of graph data
 * from text files and generate a UI containing graphical representations and adjustable sliders.
 */

public class GraphPreviewStack {
    static List<String> file_names = new ArrayList<>();

    public static VBox createCanvasPreviews() {
        VBox canvasPreviews = new VBox(5);
        canvasPreviews.setPadding(new Insets(5));
        canvasPreviews.setAlignment(Pos.TOP_CENTER);

        File inputfolder = new File(InputChooser.folderChooser());
        findTxtFiles(inputfolder);

        for (int i = 0; i < file_names.size(); i++) {
            String[] file_to_read = new String[]{file_names.get(i)};
            ReadGraph.main(file_to_read);

            Canvas canvas = new Canvas(600, 600); //static since it's static in the main method anyways
            drawContent(canvas.getGraphicsContext2D(), i);

            WritableImage snapshot = new WritableImage(600, 600);
            canvas.snapshot(null, snapshot);

                ImageView miniature = new ImageView(snapshot);
                miniature.setFitWidth(100); // Scale down for thumbnails
                miniature.setFitHeight(100);
                miniature.setPreserveRatio(true);
                miniature.accessibleTextProperty().setValue(file_names.get(i));
                File file = new File(file_names.get(i));
                Text text = new Text(file.getName());
                text.accessibleTextProperty().setValue(file_names.get(i));
                canvasPreviews.getChildren().addAll(miniature, text);
            }
            canvasPreviews.setStyle("-fx-background-color: #B3B5BB;");
            canvasPreviews.setStyle("-fx-border-color: #BEBFC5;");
            canvasPreviews.setPrefHeight(500);
        return canvasPreviews;
    }

    public static VBox createSlidersPane() {
        VBox slidersPane = new VBox(10);
        slidersPane.setPadding(new Insets(10));
        slidersPane.setAlignment(Pos.TOP_CENTER);

        slidersPane.setStyle("-fx-background-color: #B3B5BB;");
        return slidersPane;
    }

    private static void drawContent(GraphicsContext gc, int index) {
        int pointGenerationMethod = 1; // 1 for FR, 2 for Daniil's method
        UpdateConnections.main(new String[]{});
        LowChromaticBound.computeGreedyLowChromaticBound();
        NodeLayoutGeneration.chosenGenerationMethod(pointGenerationMethod, false);

        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        GameData gameData = new GameData();
        SharedData.getInstance().setGameData(gameData);
        ColEdge[] localVerticeEdge = SharedData.getInstance().getEdges();
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
        FinalData data = SharedData.getInstance().getFinalData();

        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineWidth(2*(localVertices[0].radius / 20));

        for (ColEdge edge : localVerticeEdge) {
            gc.strokeLine(
                    (localVertices[edge.u - 1].x),
                    (localVertices[edge.u - 1].y),
                    (localVertices[edge.v - 1].x),
                    (localVertices[edge.v - 1].y)
            );
        }

        gc.setLineWidth(1);
        for (DrawnNodes vertex : localVertices) {
            double hue = (320.0 / data.exact_chromatic_bound) * vertex.colour;
            gc.setFill(javafx.scene.paint.Color.hsb(hue, 1.0, 1.0));
            double scaledX =  (vertex.x - vertex.radius / 2);
            double scaledY =  (vertex.y - vertex.radius / 2);
            double scaledRadius =  vertex.radius;
            gc.fillOval(scaledX, scaledY, scaledRadius, scaledRadius);
        }
    }

    public static void findTxtFiles(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findTxtFiles(file); //search subfolders
                } else if (file.isFile() && file.getName().endsWith(".txt")) {
                    file_names.add(file.getAbsolutePath());
                }
            }
        }
    }
}

