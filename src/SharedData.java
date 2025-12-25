import java.util.List;
import java.util.ArrayList;

//I will preface this class by saying that while such practices are discouraged a singleton pattern is much prefered over public variables as to not contaminate the namespace, if anyone has any problem with this I am more than willing to go over it while refactoring this code.
//A program that is written poorly, but works is better than a program that is written well, but does not work. - me

//This class is used to store all the data that is shared between the classes, this is done to avoid having to pass the data between the classes and as previously mentioned to avoid polluting the namespace.

public class SharedData {
    // Static instance of the singleton
    private static SharedData instance;
    private SharedData() {
    }

    // Public method to provide access to saod instance
    public static SharedData getInstance() {
        if (instance == null) {
            synchronized (SharedData.class) {
                if (instance == null) {
                    instance = new SharedData();
                }
            }
        }
        return instance;
    }

    private String gameMode;
    private String playerName;
    private boolean debug;
    private ColEdge[] edges;
    private int numberOfVertices;
    private int numberOfEdges;
    private boolean[] seen;
    private String inputFile;
    private DrawnNodes[] drawnCircles;
    private int lowestChromaticBound;
    private FinalData finalData;
    private NodeColourChoices nodeColourChoices;
    private GameData gameData;
    private ColourPicker coloursPicker;
    private RandomGraphData randomGraphData;

    // Getter and Setter methods for each data type
    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public ColourPicker getColoursPicker() {
        return coloursPicker;
    }

    public void setColoursPicker(ColourPicker coloursPicker) {
        this.coloursPicker = coloursPicker;
    }

    public int getLowestChromaticBound() {
        return lowestChromaticBound;
    }

    public void setLowestChromaticBound(int lowestChromaticBound) {
        this.lowestChromaticBound = lowestChromaticBound;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public ColEdge[] getEdges() {
        return edges;
    }

    public void setEdges(ColEdge[] edges) {
        this.edges = edges;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public void setNumberOfVertices(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    public boolean[] getSeen() {
        return seen;
    }

    public void setSeen(boolean[] seen) {
        this.seen = seen;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public DrawnNodes[] getDrawnCircles() {
        return drawnCircles;
    }

    public void setDrawnNodes(DrawnNodes[] drawnCircles) {
        this.drawnCircles = drawnCircles;
    }

    public FinalData getFinalData() {
        return finalData;
    }
    public void setFinalData(FinalData finalData) {
        this.finalData = finalData;
    }

    public NodeColourChoices getNodeColourChoices() {
        return nodeColourChoices;
    }
    public void setNodeColourChoices(NodeColourChoices nodeColourChoices) {
        this.nodeColourChoices = nodeColourChoices;
    }

    public String getGameMode() { return gameMode; }

    public void setGameMode(String gameMode) { this.gameMode = gameMode; }

    public String getPlayerName() { return playerName; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public RandomGraphData getRandomGraphData() {
        return randomGraphData;
    }
    public int getRandomGraphDataEdge() {
        return randomGraphData.sharedSliderEdge;
    }
    public int getRandomGraphDataVertex() {
        return randomGraphData.sharedSliderVertex;
    }
    public void setRandomGraph(RandomGraphData sharedSliderEdge) {
        this.randomGraphData = sharedSliderEdge;
    }
    public boolean getNodeValidity(int index) {
        return drawnCircles[index].valid;  // getter
    }
    public void setNodeValidity(int index, boolean valid) {
        drawnCircles[index].valid = valid;  // setter
    }

}

class RandomGraphData{
    int sharedSliderVertex;
    int sharedSliderEdge;

}


class DrawnNodes {
    //int x;
    //int y;
    double x;
    double y;
    double dispX;
    double dispY;
    double radius;
    boolean valid;
    List<Integer> connections;
    int colour;
    public DrawnNodes() {
        this.connections = new ArrayList<>();
    }

    double difficulty = 1;
    double hintsUsed = 0;
}

class ColourPicker{
    int x;
    int y;
    int radius;
    int colour;
}

class FinalData {
    long start_time;
    long end_time;
    int upper_bound;
    int lower_bound;
    int exact_chromatic_bound;
    int player_chromatic_bound;
    int[] colours;
    boolean exact_chrom_bound_completed_till_end;
    double difficulty = 1;
    double hintsUsed = 0;
    int[][] adjecencyMatrix;
}

class NodeColourChoices{
    int red;
    int green;
    int blue;
}

class GameData{
    long start_time;
}

