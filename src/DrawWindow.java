//import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.io.FileInputStream;

import javafx.scene.input.MouseButton;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


//🟥🟥🟥🟥🟥DO NOT REMOVE THIS WE ARE NOT DONE PORTING EVERYTHING🟥🟥🟥🟥🟥
//🟥🟥🟥🟥🟥DO NOT REMOVE THIS WE ARE NOT DONE PORTING EVERYTHING🟥🟥🟥🟥🟥
//🟥🟥🟥🟥🟥DO NOT REMOVE THIS WE ARE NOT DONE PORTING EVERYTHING🟥🟥🟥🟥🟥
/*
public class DrawWindow extends JFrame {
    private float zoomLevel = 1.0f; // initial zoom level
    private static final float ZOOM_INCREMENT = 0.1f; // zoom increment value

    public static void main() {
        int point_generation_method = 1; // 1 for FR, 2 for Daniil's method
        UpdateConnections.main(new String[]{});
        LowChromaticBound.GreedyLowChromaticBound();
        NodeLayoutGeneration.chosenGenerationMethod(point_generation_method, false);
        DrawWindow main_window = new DrawWindow();
        main_window.setVisible(true);
    }

    static class DrawVerticesAndEdges extends JComponent {
        private float zoomLevel;
        private int offsetX = 0; // X offset
        private int offsetY = 0; // Y offset

        // hashmaps to store labels
        private HashMap<Integer, String> vertexLabels = new HashMap<>();
        private HashMap<Integer, String> edgeLabels = new HashMap<>();

        public DrawVerticesAndEdges(float zoomLevel) {
            this.zoomLevel = zoomLevel;
            setFocusable(true);
            requestFocusInWindow();

            // key listener for moving the camera
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    switch (key) {
                        case KeyEvent.VK_W:
                        case KeyEvent.VK_UP:
                            offsetY -= 10;
                            break;
                        case KeyEvent.VK_A:
                        case KeyEvent.VK_LEFT:
                            offsetX -= 10;
                            break;
                        case KeyEvent.VK_S:
                        case KeyEvent.VK_DOWN:
                            offsetY += 10;
                            break;
                        case KeyEvent.VK_D:
                        case KeyEvent.VK_RIGHT:
                            offsetX += 10;
                            break;
                    }
                    repaint(); // repaint to show actual offset changes
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    requestFocusInWindow();
                    double x = e.getX();
                    double y = e.getY();

                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (!addLabelToVertexOrEdge(x, y, e)) {

                            int adjustedX = (int) ((e.getX() - offsetX) / zoomLevel);
                            int adjustedY = (int) ((e.getY() - offsetY) / zoomLevel);
                            UserInputHandler.moveClosestNodeToCursor(adjustedX, adjustedY);
                        }
                    }
                    repaint();
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        int adjustedX = (int) ((e.getX() - offsetX) / zoomLevel);
                        int adjustedY = (int) ((e.getY() - offsetY) / zoomLevel);
                        UserInputHandler.moveClosestNodeToCursor(adjustedX, adjustedY);
                        repaint();
                    }
                }
            });
        }

        private boolean addLabelToVertexOrEdge(double x, double y, MouseEvent e) {
            if (e.isShiftDown()) { // use shift+click to add label
                int vertexIndex = getVertexAtPoint(x, y);
                if (vertexIndex != -1) {
                    String label = JOptionPane.showInputDialog(this, "Enter label for vertex:");
                    if (label != null) {
                        vertexLabels.put(vertexIndex, label);
                    }
                    return true;
                } else {
                    int edgeIndex = getEdgeAtPoint(x, y);
                    if (edgeIndex != -1) {
                        String label = JOptionPane.showInputDialog(this, "Enter label for edge:");
                        if (label != null) {
                            edgeLabels.put(edgeIndex, label);
                        }
                        return true;
                    }
                }
            }
            return false;
        }

        private int getVertexAtPoint(double x, double y) {
            DrawnNodes[] Local_Vertices = SharedData.getInstance().getDrawnCircles();
            for (int i = 0; i < SharedData.getInstance().getNumberOfVertices(); i++) {
                DrawnNodes node = Local_Vertices[i];
                double scaledX = node.x * zoomLevel + offsetX;
                double scaledY = node.y * zoomLevel + offsetY;
                double dx = x - scaledX;
                double dy = y - scaledY;
                double radius = node.radius * zoomLevel;
                if (dx * dx + dy * dy <= radius * radius) {
                    return i;
                }
            }
            return -1;
        }

        private int getEdgeAtPoint(double x, double y) {
            ColEdge[] Local_Vertice_Edge = SharedData.getInstance().getEdges();
            DrawnNodes[] Local_Vertices = SharedData.getInstance().getDrawnCircles();
            for (int i = 0; i < SharedData.getInstance().getNumberOfEdges(); i++) {
                DrawnNodes nodeU = Local_Vertices[Local_Vertice_Edge[i].u - 1];
                DrawnNodes nodeV = Local_Vertices[Local_Vertice_Edge[i].v - 1];

                double x1 = nodeU.x * zoomLevel + offsetX;
                double y1 = nodeU.y * zoomLevel + offsetY;
                double x2 = nodeV.x * zoomLevel + offsetX;
                double y2 = nodeV.y * zoomLevel + offsetY;

                if (isPointNearLine(x, y, x1, y1, x2, y2)) {
                    return i; // returns index of the edge
                }
            }
            return -1;
        }

        private boolean isPointNearLine(double px, double py, double x1, double y1, double x2, double y2) {
            double distance = pointToLineDistance(px, py, x1, y1, x2, y2);
            return distance < 5.0;
        }

        private double pointToLineDistance(double px, double py, double x1, double y1, double x2, double y2) {
            double lineLengthSquared = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
            if (lineLengthSquared == 0) {
                return Math.hypot(px - x1, py - y1);
            }
            double t = ((px - x1) * (x2 - x1) + (py - y1) * (y2 - y1)) / lineLengthSquared;
            t = Math.max(0, Math.min(1, t));
            double projectionX = x1 + t * (x2 - x1);
            double projectionY = y1 + t * (y2 - y1);
            return Math.hypot(px - projectionX, py - projectionY);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // clear previous
            ColEdge[] Local_Vertice_Edge = SharedData.getInstance().getEdges();
            DrawnNodes[] Local_Vertices = SharedData.getInstance().getDrawnCircles();

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.BLACK);
            for (int i = 0; i < SharedData.getInstance().getNumberOfEdges(); i++) {
                int x1 = (int) ((Local_Vertices[Local_Vertice_Edge[i].u - 1].x * zoomLevel) + offsetX);
                int y1 = (int) ((Local_Vertices[Local_Vertice_Edge[i].u - 1].y * zoomLevel) + offsetY);
                int x2 = (int) ((Local_Vertices[Local_Vertice_Edge[i].v - 1].x * zoomLevel) + offsetX);
                int y2 = (int) ((Local_Vertices[Local_Vertice_Edge[i].v - 1].y * zoomLevel) + offsetY);
                g2d.drawLine(x1, y1, x2, y2);

                // edge labels color
                String label = edgeLabels.get(i);
                if (label != null) {
                    int labelX = (x1 + x2) /2;
                    int labelY = (y1 + y2) /2;
                    g2d.setColor(Color.RED);
                    g2d.drawString(label, labelX, labelY);
                    g2d.setColor(Color.BLACK);
                }
            }


            for (int i = 0; i < SharedData.getInstance().getNumberOfVertices(); i++) {
                int scaledX = (int) ((Local_Vertices[i].x * zoomLevel) + offsetX);
                int scaledY = (int) ((Local_Vertices[i].y * zoomLevel) + offsetY);
                int scaledRadius = (int) (Local_Vertices[i].radius * zoomLevel);

                g2d.setColor(Color.BLUE);
                g2d.fillOval(scaledX - scaledRadius/2, scaledY - scaledRadius/2, scaledRadius, scaledRadius);

                // vertex labels color
                String label = vertexLabels.get(i);
                if (label != null) {
                    g2d.setColor(Color.RED);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(label);
                    int textHeight = fm.getAscent();
                    int labelX = scaledX - textWidth/2;
                    int labelY = scaledY + textHeight/2 -2;
                    g2d.drawString(label, labelX, labelY);
                    g2d.setColor(Color.BLACK);
                }
            }
        }

        public void setZoomLevel(float zoomLevel) {
            this.zoomLevel = zoomLevel;
            repaint();
        }
    }

    public DrawWindow() {
        setTitle("Graph Display Engine v0.1");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);

        DrawVerticesAndEdges drawPanel = new DrawVerticesAndEdges(zoomLevel);
        getContentPane().add(drawPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");

        zoomInButton.addActionListener(e -> {
            zoomLevel += ZOOM_INCREMENT; // increase zoom level
            drawPanel.setZoomLevel(zoomLevel);
        });

        zoomOutButton.addActionListener(e -> {
            if (zoomLevel > ZOOM_INCREMENT) { // prevent zooming out too much
                zoomLevel -= ZOOM_INCREMENT;
                drawPanel.setZoomLevel(zoomLevel);
            }
        });

        buttonPanel.add(zoomInButton);
        buttonPanel.add(zoomOutButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
*/
//🟥🟥🟥🟥🟥DO NOT REMOVE THIS WE ARE NOT DONE PORTING EVERYTHING 🟥🟥🟥🟥🟥
//🟥🟥🟥🟥🟥DO NOT REMOVE THIS WE ARE NOT DONE PORTING EVERYTHING 🟥🟥🟥🟥🟥
//🟥🟥🟥🟥🟥DO NOT REMOVE THIS WE ARE NOT DONE PORTING EVERYTHING 🟥🟥🟥🟥🟥

//public class DrawWindow extends Application {
    public class DrawWindow{

    private static Stage primarystage;

    public DrawWindow(){
        DrawWindow instance = this;
    }

    private static float zoomLevel = 1.0f; // initial zoom level
    private static final float ZOOM_INCREMENT = 0.1f; // zoom increment value

    public static void main(String[] args) {

        String[] inputfile = {InputChooser.inputChooser()};
        ReadGraph.main(inputfile);

        GameData gameData = new GameData();
        gameData.start_time = System.currentTimeMillis();
        SharedData.getInstance().setGameData(gameData);

        int point_generation_method = 1; // 1 for FR, 2 for Daniil's method
        UpdateConnections.main(new String[]{});
        LowChromaticBound.computeGreedyLowChromaticBound();

        NodeLayoutGeneration.chosenGenerationMethod(point_generation_method, false);

        //wrapper for compute chromatic number to solve static reference problem
        ProjectColour ProjectColourInstance = new ProjectColour();
        ProjectColourInstance.compute_upper_bound();
        ProjectColourInstance.ComputeChromaticNumber();
    }

        public static void drawNormalWindow(Stage oldstage) {
        oldstage.close();
        Stage primarystage = new Stage();
        //this.primarystage = primarystage;

        long start_time = SharedData.getInstance().getGameData().start_time;
        //double start_time = System.currentTimeMillis();
        Group root = new Group();
        Scene scene = new Scene(root, 900, 700);
        Canvas canvas = new Canvas(700, 600);

            DrawVerticesAndEdgesFX drawVerticesAndEdges = new DrawVerticesAndEdgesFX(zoomLevel);
        canvas.widthProperty().bind(scene.widthProperty());
        canvas.heightProperty().bind(scene.heightProperty());

        FinalData data = SharedData.getInstance().getFinalData();

        Text lower_chromatic_bound_text = new Text("The lower chromatic bound is: " + data.lower_bound);
        lower_chromatic_bound_text.setFont(new Font("Courier New", 20));
        lower_chromatic_bound_text.setX(10);
        lower_chromatic_bound_text.setY(canvas.getHeight() - 80);

        Text higher_chromatic_bound_text = new Text("The higher chromatic bound is: "+ data.upper_bound);
        higher_chromatic_bound_text.setFont(new Font("Courier New", 20));
        higher_chromatic_bound_text.setX(10);
        higher_chromatic_bound_text.setY(canvas.getHeight() - 60);

        Text exact_chromatic_bound_text = new Text("The exact chromatic bound is: " + data.exact_chromatic_bound);
        exact_chromatic_bound_text.setFont(new Font("Courier New", 20));
        exact_chromatic_bound_text.setX(10);
        exact_chromatic_bound_text.setY(canvas.getHeight() - 40);

        Text time_text = new Text("time since start: " + ((System.currentTimeMillis() - start_time)/1000) + "." + (System.currentTimeMillis() - start_time)%1000);
        time_text.setFont(new Font("Courier New", 20));
        time_text.setX(10);
        time_text.setY(canvas.getHeight() - 20);

        root.getChildren().addAll(canvas, lower_chromatic_bound_text, higher_chromatic_bound_text, exact_chromatic_bound_text);

        scene.setOnKeyPressed(event -> handleKeyEvent(event, drawVerticesAndEdges, canvas));
        scene.setOnMousePressed(event -> handleMousePressed(event, drawVerticesAndEdges, canvas));
        scene.setOnMouseDragged(event -> handleMouseDragged(event, drawVerticesAndEdges, canvas));

        canvas.getGraphicsContext2D().setLineCap(StrokeLineCap.ROUND);
        drawVerticesAndEdges.draw(canvas.getGraphicsContext2D());

        primarystage.setScene(scene);
        primarystage.setTitle("JavaFX Render");
        primarystage.show();

            scene.setOnKeyPressed(event -> UserInputHandler.handleEscapeEvent(event.getCode(), primarystage));
    }

    private static void handleKeyEvent(KeyEvent event, DrawVerticesAndEdgesFX drawVerticesAndEdges, Canvas canvas) {
        if (event.getCode() == KeyCode.EQUALS || event.getCode() == KeyCode.PLUS) {
            zoomLevel += ZOOM_INCREMENT;
            drawVerticesAndEdges.setZoomLevel(zoomLevel);
            drawVerticesAndEdges.draw(canvas.getGraphicsContext2D());
        } else if (event.getCode() == KeyCode.MINUS) {
            zoomLevel -= ZOOM_INCREMENT;
            drawVerticesAndEdges.setZoomLevel(zoomLevel);
            drawVerticesAndEdges.draw(canvas.getGraphicsContext2D());
        }
    }

    private static void handleMousePressed(MouseEvent event, DrawVerticesAndEdgesFX drawVerticesAndEdges, Canvas canvas) {
        if (event.isPrimaryButtonDown()) {
            int adjustedX = (int) ((event.getX() - drawVerticesAndEdges.getOffsetX()) / zoomLevel);
            int adjustedY = (int) ((event.getY() - drawVerticesAndEdges.getOffsetY()) / zoomLevel);
           // UserInputHandler.moveClosestNodeToCursor(adjustedX, adjustedY);
            UserInputHandler.moveClosestNodeToCursorUnderDistance(adjustedX, adjustedY, 40);
            drawVerticesAndEdges.draw(canvas.getGraphicsContext2D());
        }
    }

    private static void handleMouseDragged(javafx.scene.input.MouseEvent event, DrawVerticesAndEdgesFX drawVerticesAndEdges, Canvas canvas) {
        if (event.getButton() == MouseButton.PRIMARY) {
            int adjustedX = (int) ((event.getX() - drawVerticesAndEdges.getOffsetX()) / zoomLevel);
            int adjustedY = (int) ((event.getY() - drawVerticesAndEdges.getOffsetY()) / zoomLevel);
            //UserInputHandler.moveClosestNodeToCursor(adjustedX, adjustedY);
            UserInputHandler.moveClosestNodeToCursorUnderDistance(adjustedX, adjustedY, 40);
            drawVerticesAndEdges.draw(canvas.getGraphicsContext2D());
        }
    }

    static class DrawVerticesAndEdgesFX {
        private float zoomLevel;
        private int offsetX = 10; // X offset
        private int offsetY = 10; // Y offset

        public DrawVerticesAndEdgesFX(float zoomLevel) {
            this.zoomLevel = zoomLevel;
        }

        public void setZoomLevel(float zoomLevel) {
            this.zoomLevel = zoomLevel;
        }

        public int getOffsetX() {
            return offsetX;
        }

        public int getOffsetY() {
            return offsetY;
        }

        public void draw(GraphicsContext gc) {
            long start_time = SharedData.getInstance().getGameData().start_time;

            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

            ColEdge[] localVerticeEdge = SharedData.getInstance().getEdges();
            DrawnNodes[] localVertices = SharedData.getInstance().getDrawnCircles();

            gc.save();

            gc.scale(zoomLevel, zoomLevel);
            gc.translate(offsetX, offsetY);

            FinalData data = SharedData.getInstance().getFinalData();

            for (int i = 0; i < SharedData.getInstance().getNumberOfVertices(); i++) {
                double hue = (320.0 / data.exact_chromatic_bound) * localVertices[i].colour;
                //System.out.println(hue);
                gc.setFill(Color.hsb(hue,1.0,1.0));
                int scaledX = (int) ((localVertices[i].x * zoomLevel) + offsetX);
                int scaledY = (int) ((localVertices[i].y * zoomLevel) + offsetY);
                int scaledRadius = (int) (localVertices[i].radius * zoomLevel);
                gc.fillOval(scaledX - (scaledRadius / 2), scaledY - (scaledRadius / 2), scaledRadius, scaledRadius);
            }

            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);

            for (int i = 0; i < SharedData.getInstance().getNumberOfEdges(); i++) {
                gc.strokeLine(
                        (int) ((localVertices[localVerticeEdge[i].u - 1].x * zoomLevel) + offsetX),
                        (int) ((localVertices[localVerticeEdge[i].u - 1].y * zoomLevel) + offsetY),
                        (int) ((localVertices[localVerticeEdge[i].v - 1].x * zoomLevel) + offsetX),
                        (int) ((localVertices[localVerticeEdge[i].v - 1].y * zoomLevel) + offsetY)
                );
            }

            gc.restore(); // Restore the original state

        }
    }


}
