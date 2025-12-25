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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
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

public class RandomUndoGameApp  {

    private long startTime;
    private AnimationTimer timer;
    private Text timerDisplay;
    private Canvas canvas;
    private Pane overlayPane;
    private Pane overlayPaneLabel;
    private Pane  overlayPaneNextNode;
    private DrawVerticesAndEdgesFX drawObject;
    private double nodeRadius = 20;



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


        //reset data
        overlayPane.getChildren().clear();
        overlayPaneLabel.getChildren().clear();
        overlayPaneNextNode.getChildren().clear();
        //LabelWithNode.reInitLabelHintdata ();
        LabelWithNode.reInitLabelHintdata();
        //display first random node when load the graph
        RandomOrder.udpateCurrentNodeID ();
        createNextNodeIndicator (RandomOrder.randomOrder[RandomOrder.currentNodeID],25);

    }

    public void drawRandomUndoGameApp (Stage primaryStage) {
        primaryStage.close();

        timerDisplay = new Text("00:00:000");
        timerDisplay.setFont(new Font(30));

        Stage subStage = new Stage();

        GridPane gridPane = createLayout();

        Scene scene = new Scene(gridPane, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        subStage.setScene(scene);
        subStage.setTitle("Random Game");
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


        // init();



        // drawObject = new DrawVerticesAndEdgesFX();
        // drawObject.draw(canvas.getGraphicsContext2D());

        addEventHandlers(scene, subStage);

    }

    private void loadSelectedGraph(String[] inputfile) {

        init(inputfile);
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
        overlayPaneLabel  = new Pane();
        overlayPaneNextNode  = new Pane();

        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();

        GridPane.setConstraints(timerDisplay, 1, 0);
        GridPane.setHalignment(timerDisplay, HPos.CENTER);
        GridPane.setValignment(timerDisplay, VPos.TOP);
        gridPane.getChildren().add(timerDisplay);


        //Tu: Please add into gridPane by this order: canvas - overlayPaneLabel - left/right

        GridPane.setConstraints(canvas, 1, 0);
        gridPane.add(canvas, 1, 0);

        GridPane.setConstraints(overlayPaneNextNode, 1, 0);
        gridPane.add(overlayPaneNextNode, 1, 0);

        GridPane.setConstraints(overlayPaneLabel, 1, 0);
        gridPane.add(overlayPaneLabel, 1, 0);

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


        Button nextNodeButton = createNextNodeButon();
        Button undoNodeButton = createUndoNodeButton();
        Button hintButton = createHintButton();

        rightPanel.getChildren().add(nextNodeButton);
        rightPanel.getChildren().add(undoNodeButton);
        rightPanel.getChildren().add(hintButton);
        rightPanel.setFocusTraversable(false);
        return rightPanel;
    }
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPrefWidth(180);
        leftPanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #b3b5bb);" +
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

        ScrollPane previewScrollPane = new ScrollPane(GraphPreviewStack.createCanvasPreviews());
        previewScrollPane.setFitToWidth(true);
        previewScrollPane.setStyle("-fx-background-color: transparent;");
        previewScrollPane.setStyle("-fx-background-color: B3B5BB;");

        VBox sliderPane = new VBox(GraphPreviewStack.createSlidersPane());
        sliderPane.setStyle("-fx-background-color: #B3B5BB;");

        ScrollPane slidersScrollPane = new ScrollPane(GraphPreviewStack.createSlidersPane());
        slidersScrollPane.setFitToWidth(true);
        slidersScrollPane.setStyle("-fx-background-color: transparent;");

        enableHoverScrolling(previewScrollPane);
        enableHoverScrolling(slidersScrollPane);

        addEventToAllChildren(previewScrollPane);

        Button easyDifficulty = new Button("Easy");
        easyDifficulty.accessibleTextProperty().setValue("1");

        easyDifficulty.setOnAction(mouseEvent -> {
            RandomGraphGeneration.main("1");
            loadRandomGenGraph();
        });

        Button mediumDifficulty = new Button("Medium");
        mediumDifficulty.accessibleTextProperty().setValue("2");

        mediumDifficulty.setOnAction(mouseEvent -> {
            RandomGraphGeneration.main("2");
            loadRandomGenGraph();
        });

        Button hardDifficulty = new Button("Hard");
        hardDifficulty.accessibleTextProperty().setValue("3");

        hardDifficulty.setOnAction(mouseEvent -> {
            RandomGraphGeneration.main("3");
            loadRandomGenGraph();
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

    private Button createNextNodeButon() {
        Button nextNodeButton = new Button("Next Node");


        nextNodeButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #9fa2a8);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        );
        nextNodeButton.setOnMouseEntered(event -> nextNodeButton.setStyle( //hover of button, looks nicer imo
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #7d7e85);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        nextNodeButton.setOnMouseExited(event -> nextNodeButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #979aa3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));


        nextNodeButton.setOnAction(event -> {
            if (RandomOrder.currentNodeID<SharedData.getInstance().getNumberOfVertices()-1
            ){
                RandomOrder.udpateCurrentNodeID ();
                createNextNodeIndicator (RandomOrder.randomOrder[RandomOrder.currentNodeID],25);
                // startTimer();
                nextNodeButton.setText("Next Node");

            }
            else {
                timer.stop();
                timer.stop();

                if(areAdjacentNodesColoredDifferently(SharedData.getInstance().getDrawnCircles())) {
                    NextRandomNodeAlert.Alert();
                }
                else {
                    NextRandomNodeAlert.invalidAlert();
                }
            }

        });
        nextNodeButton.setFocusTraversable(false);
        return nextNodeButton;
    }

    private Button createUndoNodeButton() {
        Button UndoButton = new Button("Undo Node");


        UndoButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #9fa2a8);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        );
        UndoButton.setOnMouseEntered(event -> UndoButton.setStyle( //hover of button, looks nicer imo
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #7d7e85);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));
        UndoButton.setOnMouseExited(event -> UndoButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #979aa3);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        ));


        UndoButton.setOnAction(event -> {
            if (RandomOrder.currentNodeID<SharedData.getInstance().getNumberOfVertices()-1
            ){
                RandomOrder.undoCurrentNodeID ();
                createNextNodeIndicator (RandomOrder.randomOrder[RandomOrder.currentNodeID],25);
                // startTimer();
                UndoButton.setText("Undo Node");

            }
            else {
                timer.stop();
                timer.stop();

                if(areAdjacentNodesColoredDifferently(SharedData.getInstance().getDrawnCircles())) {
                    NextRandomNodeAlert.Alert();
                }
                else {
                    NextRandomNodeAlert.invalidAlert();
                }
            }

        });
        UndoButton.setFocusTraversable(false);
        return UndoButton;
    }

    private Button createHintButton() {
        Button hintButton = new Button("Hint");

        hintButton.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #b3b5bb, #9fa2a8);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 15 25;" +
                        "-fx-background-radius: 15;"
        );
        hintButton.setOnMouseEntered(event -> hintButton.setStyle( //hover of button, looks nicer imo
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


            //System.out.println("Color of current node: " + localVertices[].colour);
            System.out.println("Color of 0 node " + localVertices[0].colour);
            System.out.println("curr node real id: " + RandomOrder.randomOrder[RandomOrder.currentNodeID]);
            System.out.println("Color of current real node " + localVertices[0].colour);


            ArrayList<Integer> neighboringColors = new ArrayList<Integer>();
            for (int i = 0; i < numOfNodes; i++) {
                if (adjecencyMatrix[RandomOrder.randomOrder[RandomOrder.currentNodeID]][i] == 1){
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
            for(int i = 0; i < neighboringColors.size(); i++) {
                System.out.print(neighboringColors.get(i));
            }
            System.out.println("HINT COLOR IS: " + chosenHintColor);


            createNodeHintIndicator(RandomOrder.randomOrder[RandomOrder.currentNodeID],25, chosenHintColor);
        });
        hintButton.setFocusTraversable(false);
        return hintButton;
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
        scene.setOnMouseClicked(event -> handleCtrlClick(event,overlayPaneLabel ));
        scene.setOnMousePressed(event -> handleMousePressed(event, subStage));
        scene.setOnMouseDragged(this::handleMouseDragged);
        scene.setOnKeyPressed(event ->{
            System.out.println("KeyCode: " + event.getCode());
            handleKeyPressed(event, subStage);});

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
            default -> System.out.println("Unhandled key: " + event.getCode());
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

        //Tu: update
        ArrayList <Integer> temp = LabelWithNode.getListLabelledNode();
        for (Integer i:temp) {
            LabelWithNode.updateLabelPosition(nodes, LabelWithNode.getLabelByNodeID(i),i) ;
        }
        System.out.println(RandomOrder.currentNodeID);
        if (RandomOrder.currentNodeID!=-1) { //only indicate when the player press start

            createNextNodeIndicator (RandomOrder.randomOrder[RandomOrder.currentNodeID] ,  nodeRadius+5);
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
        //Tu: update
        ArrayList <Integer> temp = LabelWithNode.getListLabelledNode();
        for (Integer i:temp) {
            LabelWithNode.updateLabelPosition(nodes, LabelWithNode.getLabelByNodeID(i),i) ;
        }
        if (RandomOrder.currentNodeID!=-1) {

            createNextNodeIndicator (RandomOrder.randomOrder[RandomOrder.currentNodeID] ,  nodeRadius+5);
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
            UserInputHandler.moveClosestNodeToCursorUnderDistance((int) event.getX() - 200, (int) event.getY(), (int) (50 * (localVertices[0].radius/20)));
            drawObject.draw(canvas.getGraphicsContext2D());

            createNextNodeIndicator (RandomOrder.randomOrder[RandomOrder.currentNodeID],  nodeRadius+5);

        }
    }

    private void handleMousePressed(MouseEvent event, Stage stage) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();

        switch (event.getButton()) {
            case PRIMARY:
                UserInputHandler.moveClosestNodeToCursorUnderDistance((int) event.getX() - 200, (int) event.getY(), (int) (50 * (localVertices[0].radius/20)));
                drawObject.draw(gc);

                break;
            case SECONDARY:
                int closestNodeIndex = Utilities.get_closest_node(localVertices, (int) event.getX() - 200, (int) event.getY());
                if (closestNodeIndex == RandomOrder.randomOrder[RandomOrder.currentNodeID]) {
                    ColorsPicking.ColorMenuPopup(localVertices[closestNodeIndex]);

                    if(event.isShiftDown()) {
                        localVertices[closestNodeIndex].colour--;
                    }
                    else {
                        localVertices[closestNodeIndex].colour++;
                    }

                    SharedData.getInstance().setDrawnNodes(localVertices);
                }

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


    private void  createNextNodeIndicator (int NodeID,double radius) {
        overlayPaneNextNode.getChildren().clear();
        DrawnNodes[] Node = SharedData.getInstance().getDrawnCircles();
        Circle circle = new Circle(Node[NodeID].x, Node[NodeID].y, radius);
        circle.setStroke(Color.GREY);
        DropShadow shadow = new DropShadow();
        shadow.setRadius(15); // Shadow radius
        shadow.setOffsetX(0); // No horizontal offset
        shadow.setOffsetY(0); // No vertical offset
        shadow.setColor(Color.GREY); // Shadow color
        circle.setEffect(shadow);
        circle.setFill(Color.TRANSPARENT); // Transparent fill
        circle.setStrokeWidth(3);
        overlayPaneNextNode.getChildren().add(circle);
    }

    private void  createNodeHintIndicator (int NodeID,double radius, int hintColour) {
        overlayPaneNextNode.getChildren().clear();
        DrawnNodes[] Node = SharedData.getInstance().getDrawnCircles();
        Circle circle = new Circle(Node[NodeID].x, Node[NodeID].y, radius);
        FinalData data = SharedData.getInstance().getFinalData();
        double hue = (320.0 / data.exact_chromatic_bound) * hintColour;
        circle.setStroke(javafx.scene.paint.Color.hsb(hue, 1.0, 1.0));

        DropShadow shadow = new DropShadow();
        shadow.setRadius(15); // Shadow radius
        shadow.setOffsetX(0); // No horizontal offset
        shadow.setOffsetY(0); // No vertical offset
        shadow.setColor(Color.STEELBLUE); // Shadow color
        circle.setEffect(shadow);
        circle.setFill(Color.TRANSPARENT); // Transparent fill
        circle.setStrokeWidth(3);
        overlayPaneNextNode.getChildren().add(circle);
    }




    private void handleCtrlClick(MouseEvent event , Pane  overlayPaneLabel) {
        DrawnNodes[] Node = SharedData.getInstance().getDrawnCircles();
        TextField Label = new TextField("");
        double cursorX = event.getX() -200;
        double cursorY = event.getY();
        int closest_index = Utilities.get_closest_node(Node, cursorX,cursorY);
        Label.setPromptText("Node: "+closest_index);

        if (event.isControlDown() && event.getButton() == MouseButton.PRIMARY
                && Utilities.get_node_distance(Node[closest_index], cursorX, cursorY) < 50
                && !LabelWithNode.getListLabelledNode().contains(closest_index) //avoid add more than 1 label per node
        ) {
            Label.setLayoutX(Node[closest_index].x + 20);
            Label.setLayoutY(Node[closest_index].y - 10);

            Label.setPrefSize(120,20);
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

                }
                else {
                    Label.setVisible(true);
                    hideButton.setText("—");
                }
            });
            overlayPaneLabel.getChildren().addAll(Label,hideButton);
            //disable I-beam cusor when not focused
            overlayPaneLabel.setOnMouseClicked(mouseEvent -> {
                // Check if the click is outside the note field
                if ( !Label.contains(mouseEvent.getX(), mouseEvent.getY())) {
                    overlayPaneLabel.requestFocus();
                }
            });

            Label.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN ||
                        keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT) {
                    Label.setFocusTraversable(false);
                    overlayPaneLabel.requestFocus();
                }
            });

            hideButton.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN ||
                        keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT) {
                    hideButton.setFocusTraversable(false);
                    overlayPaneLabel.requestFocus();
                }
            });

            LabelWithNode labelWithNode = new LabelWithNode(closest_index,  Label);
            LabelWithNode.updateLabelHideButton(Label,hideButton);
            labelWithNode.updateListLabelledNode(closest_index);
            labelWithNode.updateLabelledNode (closest_index,Label);

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

    private void initUncoloured() {
        DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();
        for (DrawnNodes node : localVertices) {
            node.colour = 0;
        }
        SharedData.getInstance().setDrawnNodes(localVertices);
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
        drawObject = new RandomUndoGameApp.DrawVerticesAndEdgesFX();
        drawObject.draw(canvas.getGraphicsContext2D());
    }

    static class DrawVerticesAndEdgesFX {

        public DrawVerticesAndEdgesFX() {}

        public void draw(GraphicsContext gc) {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

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
    }
}