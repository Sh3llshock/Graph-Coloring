import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class VictoryScreenApp {

private static double total_score;
    public static void drawVictoryScreenApp(Stage primaryStage) {
        primaryStage.close();
        Stage subStage = new Stage();

       // String gameMode = SharedData.getInstance().getGameMode();
       // String playerName = SharedData.getInstance().getPlayerName();

        Label congrats_message = new Label("Congrats, you won!");
        Label colours_used = new Label("You used " + SharedData.getInstance().getFinalData().player_chromatic_bound + " colours");
        int color_used_num = SharedData.getInstance().getFinalData().player_chromatic_bound;
        long time_taken = SharedData.getInstance().getFinalData().end_time - SharedData.getInstance().getFinalData().start_time;

        Label Time_taken = new Label("It took you " +  (time_taken / 1000 ) + "." + (time_taken % 1000) + " s to do it");

      /* --- OLD ---
        // point calculation -  score : w1/(n^2)    time score:w2/t^2  (t = 10s chunks) w1=1000 w2=500
        double color_score = 1000 / (SharedData.getInstance().getFinalData().player_chromatic_bound * SharedData.getInstance().getFinalData().player_chromatic_bound);
        // if user solves under 10s it crashes, this is the fix
        double time_score;
        if (time_taken < 10000)
        {time_score = 8000;}
        else{time_score = 5000 / ((time_taken / 10000)*(time_taken / 10000));}
        total_score = color_score + time_score;*/

        // new point calc - e"difficulty * (x*edges*nodes - time*hints    diff:0.5,1,1.5
        // since difficulty doesnt work yet in rnd and rndUndo gamemode I just set it to 1 to prevent issues for now
        // unable to add it as hints are not done yet

        FinalData finalDataAccess = SharedData.getInstance().getFinalData();
        double difficulty = finalDataAccess.difficulty; // easy: 0.5 mid: 1 hard: 1.5 default: 1
        int Number_Edges = SharedData.getInstance().getNumberOfEdges();
        DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();
        double hintsUsed = finalDataAccess.hintsUsed; // all hints worth the same, each one = hints used + 0.5
        System.out.println("EDGES: " + Number_Edges);
        System.out.println("nodes: " + nodes.length);
        System.out.println("hints: " + hintsUsed);
        System.out.println("time: " + time_taken);
        // (100/(time_taken (s) * color_used)) * (nodes.length - hintsUsed) * Number_Edges
        double total_score = (1000 / ((time_taken/1000) * color_used_num)) * (nodes.length - hintsUsed) * Number_Edges;
        Label finalPointsDisplay = new Label("Score: " + total_score);

        HighScore highscoreManager = new HighScore("gameMode");
      //  highscoreManager.addHighScore("playerName", (int) total_score);
        highscoreManager.addHighScore("playerName", (int) total_score);

        VBox layout = new VBox(10); // 10 is the spacing between the elements
        layout.getChildren().addAll(congrats_message, colours_used, Time_taken, finalPointsDisplay);
        Scene scene = new Scene(layout, 300, 300);
        subStage.setScene(scene);
        subStage.setTitle("Game end");
        subStage.show();

        scene.setOnKeyPressed(event -> UserInputHandler.handleEscapeEvent(event.getCode(), subStage));
    }
    public static double getTotalScore() {
        return total_score;
    }
}