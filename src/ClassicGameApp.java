import javafx.animation.AnimationTimer;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ClassicGameApp {

    private long startTime;
    private AnimationTimer timer;
    private Text timerDisplay;
    private Canvas canvas;
    private Pane overlayPane;
    private Pane overlayPaneNextNode;
    private DrawVerticesAndEdgesFX drawObject;
    private double nodeRadius = 20;
    private int nextNodeID = -1;


    public void init(String[] inputfile) {
        ReadGraph.main(inputfile);

        GameData gameData = new GameData();
        gameData.start_time = System.currentTimeMillis();
        SharedData.getInstance().setGameData(gameData);

        int pointGenerationMethod = 1; // 1 for FR, 2 for Daniil's method
        UpdateConnections.main(new String[]{});
        LowChromaticBound.computeGreedyLowChromaticBound();
        NodeLayoutGeneration.chosenGenerationMethod(pointGenerationMethod, false);

        ProjectColour.compute_upper_bound();
        ProjectColour projectColourInstance = new ProjectColour();
        projectColourInstance.ComputeChromaticNumber();
        initUncoloured();

        FinalData timeSet = SharedData.getInstance().getFinalData();
        timeSet.start_time = System.currentTimeMillis();
        SharedData.getInstance().setFinalData(timeSet);


        //Tu: I keep them intentionally Dont remove anything
        //int nextNodeID = -1;
        overlayPane.getChildren().clear();
        overlayPaneNextNode.getChildren().clear();
        LabelWithNode.reInitLabelHintdata(); //Tu: I keep it intentionally
        LabelWithNode.clearLabelData();
        NextNodeHint.initHintdata() ;
        
    }

    public void drawClassicGameApp(Stage primaryStage) {
        primaryStage.close();

        timerDisplay = new Text("00:00:000");
        timerDisplay.setFont(new Font(30));

        Stage subStage = new Stage();

        GridPane gridPane = createLayout();

        Scene scene = new Scene(gridPane, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        subStage.setScene(scene);
        subStage.setTitle("Classic Game");
        subStage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font(24));
        gc.setFill(Color.RED);

        gc.fillText("Please select an existing or random graph", 50, 100);
        gc.fillText("WASD controls the viewport movement", 50, 150);
        gc.fillText("The scroll wheel zooms in and out", 50, 200);
        gc.fillText("ESC will take you back to the menu screen", 50, 250);
        gc.fillText("All illegally coloured nodes have a black outline", 50, 300);
        gc.fillText("You can change the colour by rightlicking a node,", 50, 350);
        gc.fillText("or go back to a node's previous colour by shift", 50, 400);
        gc.fillText("rightclicking", 50, 450);
        gc.fillText("Good luck!", 50, 500);

        addEventHandlers(scene, subStage);
    }

    private void loadSelectedGraph(String[] inputfile) {

        init(inputfile);
        startTimer();
        drawObject = new DrawVerticesAndEdgesFX();
        drawObject.draw(canvas.getGraphicsContext2D());
        createLabelonEdge (); //create only 1 time;
    }
    public void loadRandomGenGraph() {

        GameData gameData = new GameData();
        gameData.start_time = System.currentTimeMillis();
        SharedData.getInstance().setGameData(gameData);

        int pointGenerationMethod = 1; // 1 for FR, 2 for Daniil's method
        UpdateConnections.main(new String[]{});
        LowChromaticBound.computeGreedyLowChromaticBound();
        NodeLayoutGeneration.chosenGenerationMethod(pointGenerationMethod, false);

        ProjectColour.compute_upper_bound();
        ProjectColour projectColourInstance = new ProjectColour();
        projectColourInstance.ComputeChromaticNumber();
        initUncoloured();

        FinalData timeSet = SharedData.getInstance().getFinalData();
        timeSet.start_time = System.currentTimeMillis();
        SharedData.getInstance().setFinalData(timeSet);

        //Tu - please Dont remove
        overlayPane.getChildren().clear(); 
        overlayPaneNextNode.getChildren().clear(); 
        NextNodeHint.initHintdata(); 
        LabelWithNode.clearLabelData ();

        startTimer();
        drawObject = new DrawVerticesAndEdgesFX();
        drawObject.draw(canvas.getGraphicsContext2D());
    }

    private GridPane createLayout() {
        GridPane gridPane = new GridPane();

        ColumnConstraints columnCon1 = new ColumnConstraints();
        ColumnConstraints columnCon2 = new ColumnConstraints();
        ColumnConstraints columnCon3 = new ColumnConstraints();

        columnCon1.setPercentWidth(20);
        columnCon2.setPercentWidth(60);
        columnCon3.setPercentWidth(20);

        gridPane.getColumnConstraints().addAll(columnCon1, columnCon2, columnCon3);

        gridPane.setPrefSize(800, 600);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        canvas = new Canvas(600, 600);
        overlayPane = new Pane();
        overlayPaneNextNode = new Pane();

        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        GridPane.setConstraints(timerDisplay, 1, 0);
        GridPane.setHalignment(timerDisplay, HPos.CENTER);
        GridPane.setValignment(timerDisplay, VPos.TOP);
        gridPane.getChildren().add(timerDisplay);


        //Tu: Please add into gridPane by this order: canvas - overlayPane - left/right

        GridPane.setConstraints(canvas, 1, 0);
        gridPane.add(canvas, 1, 0);

        GridPane.setConstraints(overlayPaneNextNode, 1, 0);
        gridPane.add(overlayPaneNextNode, 1, 0);

        GridPane.setConstraints(overlayPane, 1, 0);
        gridPane.add(overlayPane, 1, 0);

        GridPane.setConstraints(leftPanel, 0, 0);
        GridPane.setRowSpan(leftPanel, GridPane.REMAINING);
        gridPane.add(leftPanel, 0, 0);

        GridPane.setConstraints(rightPanel, 2, 0);
        GridPane.setRowSpan(rightPanel, GridPane.REMAINING);
        gridPane.add(rightPanel, 2, 0);

        return gridPane;
    }

    private VBox createSidePanel() {
        VBox sidePanel = new VBox();
        sidePanel.setPrefWidth(180);
        sidePanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #2D3748, #4A5568);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );
        sidePanel.setAlignment(Pos.CENTER);
        timerDisplay.setFont(new Font("Arial", 18));
        timerDisplay.setStyle("-fx-text-fill: #38B2AC; -fx-font-weight: bold;");
        return sidePanel;
    }

    private VBox createRightPanel() {
        VBox rightPanel = new VBox(20);
        rightPanel.setPrefWidth(180);
        rightPanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #b3b5bb);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );
        rightPanel.setAlignment(Pos.CENTER);

        Button hintButton1 = createHintButton1();
        Button giveUpButton = createGiveUpBuuton();
        Button labelButton = createShowEdgeLabel();
        rightPanel.getChildren().add(hintButton1);
        rightPanel.getChildren().add(giveUpButton);
        rightPanel.getChildren().add(labelButton);
        rightPanel.setFocusTraversable(false);
        return rightPanel;
    }

    public VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPrefWidth(180);
        leftPanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #b3b5bb);" +
                        //"-fx-background-color: linear-gradient(to bottom, #2D3748, #4A5568);" +
                        "-fx-background-color: #B3B5BB;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;"
        );
        leftPanel.setAlignment(Pos.TOP_CENTER);

        Button showPreviewsButton = new Button("Show Graphs");
        Button showSlidersButton = new Button("Graph generation");

        StackPane contentArea = new StackPane();
        contentArea.setPrefHeight(360);
        contentArea.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");
        //contentArea.setStyle("-fx-border-color: lightgray; -fx-border-width: 1px;");

        ScrollPane previewScrollPane = new ScrollPane(GraphPreviewStack.createCanvasPreviews());
        previewScrollPane.setFitToWidth(true);
        previewScrollPane.setStyle("-fx-background-color: transparent;");
        previewScrollPane.setStyle("-fx-background-color: B3B5BB;");

        //ScrollPane slidersScrollPane = new ScrollPane(GraphPreviewStack.createSlidersPane());
        //slidersScrollPane.setFitToWidth(true);
        //slidersScrollPane.setStyle("-fx-background-color: transpare\nt;");

        VBox sliderPane = new VBox(GraphPreviewStack.createSlidersPane());
        sliderPane.setStyle("-fx-background-color: #B3B5BB;");


        ScrollPane slidersScrollPane = new ScrollPane(GraphPreviewStack.createSlidersPane());
        slidersScrollPane.setFitToWidth(true);
        slidersScrollPane.setStyle("-fx-background-color: transparent;");

        enableHoverScrolling(previewScrollPane);
        enableHoverScrolling(slidersScrollPane);
        //enableHoverScrolling(slidersScrollPane);

        addEventToAllChildren(previewScrollPane);
        //addEventToButtonChildren(sliderPane);
        Button easyDifficulty = new Button("Easy");
        easyDifficulty.accessibleTextProperty().setValue("1");

        easyDifficulty.setOnAction(mouseEvent -> {
            RandomGraphGeneration.main("1");
            loadRandomGenGraph();
            FinalData difficultyAccess = SharedData.getInstance().getFinalData();
            difficultyAccess.difficulty = 0.5; //make sure this is here in the merge
        });

        Button mediumDifficulty = new Button("Medium");
        mediumDifficulty.accessibleTextProperty().setValue("2");

        mediumDifficulty.setOnAction(mouseEvent -> {
            RandomGraphGeneration.main("2");
            loadRandomGenGraph();
            FinalData difficultyAccess = SharedData.getInstance().getFinalData();
            difficultyAccess.difficulty = 1;
        });

        Button hardDifficulty = new Button("Hard");
        hardDifficulty.accessibleTextProperty().setValue("3");

        hardDifficulty.setOnAction(mouseEvent -> {
            RandomGraphGeneration.main("3");
            loadRandomGenGraph();
            FinalData difficultyAccess = SharedData.getInstance().getFinalData();
            difficultyAccess.difficulty = 1.5;
        });
        Button customDifficulty = new Button("Custom");
        customDifficulty.accessibleTextProperty().setValue("4");

        Button generateGraph = new Button("Generate");
        Slider sliderNode = new Slider(0, 20, 50);
        Slider sliderEdge = new Slider(0, 50, 50);

        customDifficulty.setOnAction(mouseEvent -> {
            sliderPane.getChildren().addAll(generateGraph);

            sliderNode.setShowTickMarks(true);
            sliderNode.setShowTickLabels(true);
            sliderPane.getChildren().add(sliderNode);

            sliderEdge.setShowTickMarks(true);
            sliderEdge.setShowTickLabels(true);
            sliderPane.getChildren().add(sliderEdge);

        });

        generateGraph.setOnAction(mouseEvent ->{
            RandomGraphData Data = SharedData.getInstance().getRandomGraphData();
            if (Data == null){
                Data = new RandomGraphData();
            }
            Data.sharedSliderVertex = (int)sliderNode.getValue();
            Data.sharedSliderEdge = (int)sliderEdge.getValue();
            SharedData.getInstance().setRandomGraph(Data);
            RandomGraphGeneration.main("4");
            loadRandomGenGraph();

            sliderPane.getChildren().remove(generateGraph);
            sliderPane.getChildren().removeAll(sliderEdge, sliderNode);
        });


        sliderPane.getChildren().addAll(easyDifficulty, mediumDifficulty, hardDifficulty, customDifficulty);



        showPreviewsButton.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(previewScrollPane);
            contentArea.setPrefHeight(previewScrollPane.getPrefHeight());
        });

        showSlidersButton.setOnAction(e -> {
            contentArea.getChildren().clear();
            contentArea.getChildren().add(slidersScrollPane);
            contentArea.setPrefHeight(slidersScrollPane.getPrefHeight());
            contentArea.getChildren().add(sliderPane);
        });

        leftPanel.getChildren().addAll(showPreviewsButton, showSlidersButton, contentArea);
        leftPanel.setFocusTraversable(false);
        return leftPanel;
    }
    private Button createHintButton1() {
        Button hintButton = new Button("Show Hint");
        hintButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #9fa2a8);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        );
        hintButton.setOnMouseEntered(event -> hintButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #7d7e85);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        hintButton.setOnMouseExited(event -> hintButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #979aa3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        hintButton.setOnAction(event -> {
            SharedData.getInstance().getFinalData().hintsUsed += 1;
            DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();

            int numOfNodes = localVertices.length;
            // random node id order int currNodeID = RandomOrder.currentNodeID;
            FinalData finalData = SharedData.getInstance().getFinalData();
            int[][] adjecencyMatrix= finalData.adjecencyMatrix;
            int currNodeID = RandomOrder.currentNodeID;


            /* debugging
            //System.out.println("Color of current node: " + localVertices[].colour);
            System.out.println("Color of 0 node " + localVertices[0].colour);
            System.out.println("curr node real id: " + RandomOrder.randomOrder[RandomOrder.currentNodeID]);
            System.out.println("Color of current real node " + localVertices[0].colour);
*/

            for(int i = 0; i < numOfNodes; i++){
                if(localVertices[i].colour == 0){
                    greedyOnThisNode(i);
                    break;
                }
            }


        });
        return hintButton;
    }

    public void greedyOnThisNode(int node) {
        SharedData.getInstance().getFinalData().hintsUsed += 1;
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();

        int numOfNodes = localVertices.length;
        // random node id order int currNodeID = RandomOrder.currentNodeID;
        FinalData finalData = SharedData.getInstance().getFinalData();
        int[][] adjecencyMatrix= finalData.adjecencyMatrix;

        ArrayList<Integer> neighboringColors = new ArrayList<Integer>();
        for (int i = 0; i < numOfNodes; i++) {
            if (adjecencyMatrix[node][i] == 1) {
                neighboringColors.add(localVertices[i].colour);
            }
        }

        int chosenHintColor = 0;
        Collections.sort(neighboringColors);
        for (int clr : neighboringColors) {
            if (clr == chosenHintColor) {
                chosenHintColor++;
            }
        }
        System.out.println("neighvor colors: ");
        for (int i = 0; i < neighboringColors.size(); i++) {
            System.out.print(neighboringColors.get(i));
        }
        System.out.println("nodeID is: " + node);
        System.out.println("HINT COLOR IS: " + chosenHintColor);
        localVertices[node].colour = chosenHintColor;

        drawObject.draw(canvas.getGraphicsContext2D());

    }
    private Button createGiveUpBuuton() {
        Button giveUpButton = new Button("Give Up");
        //Tu: Make sure it has the same format with hintButton

        giveUpButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #979aa3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        );
        giveUpButton.setOnMouseEntered(event -> giveUpButton.setStyle( //hover of button, looks nicer imo
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #7d7e85);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        giveUpButton.setOnMouseExited(event -> giveUpButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #979aa3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        giveUpButton.setOnAction(event -> {
            if(Giveup.displayGiveUp()){
            drawObject.draw(canvas.getGraphicsContext2D());}
        });
        giveUpButton.setFocusTraversable(false);
        return giveUpButton;
    }
    private Button createShowEdgeLabel() {
        Button labelButton = new Button("Show Labels");
        //Tu: Make sure it has the same format with hintButton

        labelButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #979aa3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        );
        labelButton.setOnMouseEntered(event -> labelButton.setStyle( //hover of button, looks nicer imo
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #7d7e85);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        labelButton.setOnMouseExited(event -> labelButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #979aa3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        labelButton.setOnAction(event -> {
            //check if any button is visible
            boolean isButtonVisible = overlayPane.getChildren().stream()
            .anyMatch(child -> child instanceof Button && child.isVisible());
    

            overlayPane.getChildren().forEach(child -> {
                if (child instanceof Button) {
                    (child).setVisible(!isButtonVisible);
                }
                else if (child instanceof TextField) {
                    (child).setVisible(false);
                }
        });
        });
        labelButton.setFocusTraversable(false);
        return labelButton;
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                long minutes = (elapsedMillis / 60000);
                long seconds = (elapsedMillis / 1000) % 60;
                long milliseconds = elapsedMillis % 1000;
                timerDisplay.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            }
        };
        timer.start();
    }

    private void addEventToAllChildren(ScrollPane parent) {
        //having to do it this way because scroll pane children are inaccessible is stupid, but i dont know a better way
        Node content = parent.getContent();
        if (content instanceof Parent container) {

            container.getChildrenUnmodifiable().forEach(child -> {
                child.setOnMouseClicked(event -> {
                    String accessibleText = child.getAccessibleText();
                    if (accessibleText != null) {
                        loadSelectedGraph(new String[]{accessibleText});
                    }
                    event.consume();
                });
            });
        }
    }

    private void addEventHandlers(Scene scene, Stage subStage) {
        scene.setOnMouseClicked(event -> handleCtrlClick(event, overlayPane));
        scene.setOnMousePressed(event -> handleMousePressed(event, subStage));
        scene.setOnMouseDragged(this::handleMouseDragged);
        scene.setOnKeyPressed(event -> {
            handleKeyPressed(event, subStage);
        });
        scene.setOnScroll(event -> handleScroll(event));
    }

    private void handleKeyPressed(KeyEvent event, Stage stage) {
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
        int deltaX = 0, deltaY = 0;

        switch (event.getCode()) {
            case W, UP -> deltaY = -5;
            case S, DOWN -> deltaY = 5;
            case A, LEFT -> deltaX = -5;
            case D, RIGHT -> deltaX = 5;
            case ESCAPE -> DrawMenuApp.openMenuWindow(stage);
        }

        moveNodes(localVertices, deltaX, deltaY);
        drawObject.draw(canvas.getGraphicsContext2D());
    }

    private void handleScroll(ScrollEvent event) {
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
        double scaleFactor = event.getDeltaY() > 0 ? 1.1 : 0.9; // Zoom in or out

        scaleNodes(localVertices, scaleFactor);
        SharedData.getInstance().setDrawnNodes(localVertices);
        drawObject.draw(canvas.getGraphicsContext2D());
    }

    private void enableHoverScrolling(ScrollPane scrollPane) {
        scrollPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            double newVValue = scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight();
            scrollPane.setVvalue(Math.max(0, Math.min(newVValue, 1)));
            event.consume();
        });
    }

    private void enableNodeEvents(ScrollPane scrollPane) {
        scrollPane.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            double newVValue = scrollPane.getVvalue() - deltaY / scrollPane.getContent().getBoundsInLocal().getHeight();
            scrollPane.setVvalue(Math.max(0, Math.min(newVValue, 1)));
            event.consume();
        });
    }

    private void moveNodes(DrawnNodes[] nodes, int deltaX, int deltaY) {
        for (DrawnNodes node : nodes) {
            node.x += deltaX;
            node.y += deltaY;
        }
        SharedData.getInstance().setDrawnNodes(nodes);
        //Tu: update label position
        LabelWithNode.updateLabelforEgdePosition();

        ArrayList<Integer> temp = LabelWithNode.getListLabelledNode();
        for (Integer i : temp) {
            LabelWithNode.updateLabelPosition(nodes, LabelWithNode.getLabelByNodeID(i), i);
        }
        if (nextNodeID == -999 || nextNodeID == -1) {
        } else {
            createNextNodeIndicator(nextNodeID, nodeRadius + 5);
        }
    }

    private void scaleNodes(DrawnNodes[] nodes, double scaleFactor) {
        double centerX = Arrays.stream(nodes).mapToDouble(node -> node.x).average().orElse(0);
        double centerY = Arrays.stream(nodes).mapToDouble(node -> node.y).average().orElse(0);

        for (DrawnNodes node : nodes) {
            node.x = centerX + (node.x - centerX) * scaleFactor;
            node.y = centerY + (node.y - centerY) * scaleFactor;
            node.radius *= scaleFactor;
            nodeRadius = node.radius;
        }
        SharedData.getInstance().setDrawnNodes(nodes);
        //Tu: update label position
        LabelWithNode.updateLabelforEgdePosition();
        ArrayList<Integer> temp = LabelWithNode.getListLabelledNode();
        for (Integer i : temp) {
            LabelWithNode.updateLabelPosition(nodes, LabelWithNode.getLabelByNodeID(i), i);
        }

        if (nextNodeID == -999 || nextNodeID == -1) {
        } else {
            createNextNodeIndicator(nextNodeID, nodeRadius + 5);
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
            UserInputHandler.moveClosestNodeToCursorUnderDistance((int) event.getX() - 200, (int) event.getY(), (int) (50 * (localVertices[0].radius / 20)));
            drawObject.draw(canvas.getGraphicsContext2D());

            if (nextNodeID == -999 || nextNodeID == -1) {
            } else {
                createNextNodeIndicator(nextNodeID, nodeRadius + 5);
            }
        }
    }

    private void handleMousePressed(MouseEvent event, Stage stage) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();

        switch (event.getButton()) {
            case PRIMARY:
                UserInputHandler.moveClosestNodeToCursorUnderDistance((int) event.getX() - 200, (int) event.getY(), (int) (50 * (localVertices[0].radius / 20)));
                drawObject.draw(gc);

                int[][] nodesConnection = CographChecker.convertConnectionsToAdjacencyMatrix();
                CographChecker checker = new CographChecker(nodesConnection);
                boolean isCograph = checker.isCograph();
                System.out.println("Is Cograph? " + isCograph);

                break;
            case SECONDARY:

                int closestNodeIndex = Utilities.get_closest_node(localVertices, (int) event.getX() - 200, (int) event.getY());
                //ColorsPicking.openColourPicker(gc, (int) event.getX() - 200, (int) event.getY(), closestNodeIndex);

                ContextMenu menu = ColorsPicking.ColorMenuPopup(localVertices[closestNodeIndex]);
                menu.show(canvas, event.getScreenX(), event.getScreenY());

                menu.setOnHidden(e -> {
                    UniqeGraphColouring graphColoring = new UniqeGraphColouring();

                if (areAdjacentNodesColoredDifferently(localVertices)) {
                    FinalData data = SharedData.getInstance().getFinalData();
                    data.end_time = System.currentTimeMillis();

                    int[] colours = Arrays.stream(localVertices).mapToInt(node -> node.colour).toArray();
                    data.player_chromatic_bound = (int) Arrays.stream(colours).distinct().count();
                    SharedData.getInstance().setFinalData(data);
                    timer.stop();
                    VictoryScreenApp.drawVictoryScreenApp(stage);
                }

                // Draw graph and highlight nodes
                DrawVerticesAndEdgesFX drawVerticesAndEdges = drawObject;
                drawVerticesAndEdges.draw(canvas.getGraphicsContext2D());

                    //graphColoring.isValidColoring(currentColors);

                    // Draw graph and highlight nodes (Check every time you change the colors)
                    //DrawVerticesAndEdgesFX drawVerticesAndEdges = drawObject;
                    drawVerticesAndEdges.draw(canvas.getGraphicsContext2D());
                    drawObject.draw(gc);
                });



                if (areAdjacentNodesColoredDifferently(localVertices)) {
                    FinalData data = SharedData.getInstance().getFinalData();
                    data.end_time = System.currentTimeMillis();

                    int[] colours = Arrays.stream(localVertices).mapToInt(node -> node.colour).toArray();
                    data.player_chromatic_bound = (int) Arrays.stream(colours).distinct().count();
                    SharedData.getInstance().setFinalData(data);
                    timer.stop();
                    VictoryScreenApp.drawVictoryScreenApp(stage);
                }
                drawObject.draw(gc);
                break;
            default:
                break;
        }
    }

    public void createNextNodeIndicator(int NodeID, double radius) {
        overlayPaneNextNode.getChildren().clear();
        DrawnNodes[] Node = SharedData.getInstance().getDrawnCircles();
        Circle circle = new Circle(Node[NodeID].x, Node[NodeID].y, radius);
        circle.setStroke(Color.GREEN);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(15); // Shadow radius
        shadow.setOffsetX(0); // No horizontal offset
        shadow.setOffsetY(0); // No vertical offset
        shadow.setColor(Color.GREEN); // Shadow color
        circle.setEffect(shadow);
        circle.setFill(Color.TRANSPARENT); // Transparent fill
        circle.setStrokeWidth(3);
        overlayPaneNextNode.getChildren().add(circle);
    }
    //Tu: My apology, if I have  time, i will refactor this mess 
    private void setupLabelforEdge (Button labelButton, TextField Label) {
        labelButton.setStyle(
            "-fx-font-size: 10;" + 
            "-fx-shape: 'M50,0 A50,50 0 1,1 49.99,0 Z';" + 
            "-fx-min-width:  1px;" + 
            "-fx-min-height: 1px;" +
            "-fx-max-width:  30px;" +
            "-fx-max-height: 30px;" +
            "-fx-background-color: transparent;"+
            "-fx-text-fill: black;" +         
            "-fx-background-radius: 50%;"     
        );    
        labelButton.setVisible(false);
        Label.setPromptText("" );
        Label.setLayoutX(labelButton.getLayoutX()  -5); 
        Label.setLayoutY(labelButton.getLayoutY()  + 15); 
        Label.setPrefSize(50, 20);
        Label.setStyle("-fx-focus-color: darkblue;");
        Label.setVisible(false);


        labelButton.setOnAction(e -> {
            if (Label.isVisible()) {
                Label.setVisible(false);
                labelButton.setText("+");

            } else {
                Label.setVisible(true);
                labelButton.setText("—");
            }
        });
        //disable I-beam cusor when not focused
        overlayPane.setOnMouseClicked(mouseEvent -> {
            // Check if the click is outside the note field
            if (!Label.contains(mouseEvent.getX(), mouseEvent.getY())) {
                overlayPane.requestFocus();
            }
        });

        LabelWithNode.createbuttonLabelEdgeDict(labelButton,Label);
    }
    private void createLabelonEdge () {
        HashMap <String,ArrayList<Double>> temp =LabelWithNode.getEdgeNameCoordDict ();
        for (HashMap.Entry<String, ArrayList<Double>> entry : temp.entrySet()) {
            Button labelButton = new Button("+");
            TextField Label = new TextField("");
            
            String edgeName = entry.getKey(); // Get the key
            ArrayList<Double> values = entry.getValue(); // Get the value (ArrayList)
            labelButton.setLayoutX(values.get(0)); //set x based on midpoint coord
            labelButton.setLayoutY(values.get(1)); //set y based on midpoint coord

            setupLabelforEdge(labelButton,Label);

            overlayPane.getChildren().addAll(Label, labelButton);
            LabelWithNode.createEdgeButtonDict(edgeName,labelButton); //update to EdgeLabelDict 
        
        }
    }


    private void handleCtrlClick(MouseEvent event, Pane overlayPane) {
        DrawnNodes[] Node = SharedData.getInstance().getDrawnCircles();
        TextField Label = new TextField("");
        double cursorX = event.getX() - 200;
        double cursorY = event.getY();
        int closest_index = Utilities.get_closest_node(Node, cursorX, cursorY);
        Label.setPromptText("Node: " + closest_index);

        if (event.isControlDown() && event.getButton() == MouseButton.PRIMARY
                && Utilities.get_node_distance_double(Node[closest_index], cursorX, cursorY) < 50
                && !LabelWithNode.getListLabelledNode().contains(closest_index) //avoid add more than 1 label per node
        ) {
            Label.setLayoutX(Node[closest_index].x + 20);
            Label.setLayoutY(Node[closest_index].y - 10);

            Label.setPrefSize(120, 20);
            Label.setStyle("-fx-focus-color: darkblue;");
            // "-fx-font: 12px 'Arial';");
            // "-fx-text-fill: skyblue;"+
            // "-fx-background-color: lightblue;");

            Button hideButton = new Button("—");
            hideButton.setStyle("-fx-font-size: 9");
            hideButton.setLayoutX(Label.getLayoutX() - 15);
            hideButton.setLayoutY(Label.getLayoutY() - 5);
            hideButton.setOnAction(e -> {
                if (Label.isVisible()) {
                    Label.setVisible(false);
                    hideButton.setText("+");

                } else {
                    Label.setVisible(true);
                    hideButton.setText("—");
                }
            });
            overlayPane.getChildren().addAll(Label, hideButton);
            //disable I-beam cusor when not focused
            overlayPane.setOnMouseClicked(mouseEvent -> {
                // Check if the click is outside the note field
                if (!Label.contains(mouseEvent.getX(), mouseEvent.getY())) {
                    overlayPane.requestFocus();
                }
            });

            Label.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN ||
                        keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT) {
                    Label.setFocusTraversable(false);
                    overlayPane.requestFocus();
                }
            });

            hideButton.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN ||
                        keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT) {
                    hideButton.setFocusTraversable(false);
                    overlayPane.requestFocus();
                }
            });

            LabelWithNode labelWithNode = new LabelWithNode(closest_index, Label);
            LabelWithNode.updateLabelHideButton(Label, hideButton);
            labelWithNode.updateListLabelledNode(closest_index);
            labelWithNode.updateLabelledNode(closest_index, Label);

        }
    }


    private boolean areAdjacentNodesColoredDifferently(DrawnNodes[] localNodes) {
        for (DrawnNodes currentNode : localNodes) {
            int currentColor = currentNode.colour;
            for (int adjacentIndex : currentNode.connections) {
                if (currentColor == localNodes[adjacentIndex].colour) {
                    return false;
                }
            }
        }
        return true;
    }
    //do contains check

    private void initUncoloured() {
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
        for (DrawnNodes node : localVertices) {
            node.colour = 0;
        }
        SharedData.getInstance().setDrawnNodes(localVertices);
    }

    static class DrawVerticesAndEdgesFX {

        public DrawVerticesAndEdgesFX() {
        }

        public void draw(GraphicsContext gc) {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

            ColEdge[] localVerticeEdge = SharedData.getInstance().getEdges();
            DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
            FinalData data = SharedData.getInstance().getFinalData();

            gc.setStroke(javafx.scene.paint.Color.BLACK);
            gc.setLineCap(StrokeLineCap.ROUND);
            gc.setLineWidth(2 * (localVertices[0].radius / 20));

            for (ColEdge edge : localVerticeEdge) {
                gc.strokeLine(
                        (localVertices[edge.u - 1].x),
                        (localVertices[edge.u - 1].y),
                        (localVertices[edge.v - 1].x),
                        (localVertices[edge.v - 1].y)
                );
            }
            LabelWithNode.updateLabelforEgdePosition(); //Tu

            gc.setLineWidth(1);
            int nodeIndex = 0;
            for (DrawnNodes vertex : localVertices) {
                double hue = (320.0 / data.exact_chromatic_bound) * vertex.colour;
                gc.setFill(javafx.scene.paint.Color.hsb(hue, 1.0, 1.0));
                double scaledX = (vertex.x - vertex.radius / 2);
                double scaledY = (vertex.y - vertex.radius / 2);
                double scaledRadius = vertex.radius;
                gc.fillOval(scaledX, scaledY, scaledRadius, scaledRadius);

                //Highlight the invalid nodes
                boolean isValid = SharedData.getInstance().getNodeValidity(nodeIndex);
                if (!isValid) {
                    gc.setStroke(Color.BLACK);
                    gc.setLineWidth(4 * (localVertices[0].radius / 20));
                    gc.strokeOval(scaledX, scaledY, scaledRadius, scaledRadius);
                }
                gc.setFill(Color.BLACK);
                gc.setFont(Font.font("IBM Mono Plex", 15 * (localVertices[0].radius / 20)));
                gc.fillText(String.valueOf(nodeIndex), scaledX+ (25 * (localVertices[0].radius / 20)), scaledY+(25* (localVertices[0].radius / 20)));

                nodeIndex++;

            }

        }
    }
}
