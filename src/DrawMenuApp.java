import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;

public class DrawMenuApp {

    private static final String FONT_FAMILY = "Courier New";
    private static final double FONT_SIZE = 24.0;
    private static final String HIGHLIGHT_STYLE =
           "-fx-background-color: #f1a208;" +
                    "-fx-text-fill: #FFFFFF;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 28px;" +
                    "-fx-padding: 15;" +
                    "-fx-effect: dropshadow(gaussian, rgb(241,162,8), 25, 0.5, 0, 0);";
    private static final String DEFAULT_STYLE =
                    "-fx-background-color: rgba(0,0,0,0);" +
                    "-fx-text-fill: #000000;" +
                    "-fx-font-weight: normal;" +
                    "-fx-font-size: 24px;" +
                    "-fx-padding: 10;";


    private static Label[] gameModes;
    private static int currentSelection = 0;

    public static void openMenuWindow(Stage stage) {
        stage.close();

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Game Mode Selection");

        String[] modeNames = {
                "Normal Display",
                "Classic",
                "Random order",
                "Random order with undo"
        };

        gameModes = new Label[modeNames.length];
        for (int i = 0; i < modeNames.length; i++) {
            gameModes[i] = createGameModeLabel(modeNames[i]);
        }

        VBox vbox = new VBox(20, gameModes);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #b3b5bb;");




        Label highScoreTitle = new Label("High Scores");
        highScoreTitle.setFont(new Font(FONT_FAMILY, FONT_SIZE));
        highScoreTitle.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
        vbox.getChildren().add(highScoreTitle);

        // Add high scores for each game mode
        addHighScores(vbox, "Classic");
        addHighScores(vbox, "Random order");
        addHighScores(vbox, "Random order with undo");

        Scene scene = new Scene(vbox, 800, 600);
        scene.setOnKeyPressed(event -> handleKeyEvent(event.getCode(), primaryStage));

        primaryStage.setScene(scene);
        primaryStage.show();

        highlightCurrentSelection();
    }

    private static void addHighScores(VBox vbox, String gameMode) {
        HighScore highScoreManager = new HighScore(gameMode);
        List<HighScore.ScoreEntry> scores = highScoreManager.getHighScores();

        StringBuilder sb = new StringBuilder(gameMode + ":\n");
        if (scores.isEmpty()) {
            sb.append("No scores available.\n");
        } else {
            for (int i = 0; i < scores.size(); i++) {
                HighScore.ScoreEntry entry = scores.get(i);
                sb.append(i + 1).append(". ").append(entry.getName()).append(": ").append(entry.getScore()).append("\n");
            }
        }

        Label highScoreLabel = new Label(sb.toString());
        highScoreLabel.setFont(new Font(FONT_FAMILY, FONT_SIZE - 4));
        highScoreLabel.setStyle("-fx-text-fill: #000000;");
        vbox.getChildren().add(highScoreLabel);
    }

    private static Label createGameModeLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font(FONT_FAMILY, FONT_SIZE));
        label.setStyle(DEFAULT_STYLE);
        return label;
    }

    private static String formatHighScores(List<HighScore.ScoreEntry> scores) {
        if (scores.isEmpty()) {
            return "No scores available.";
        }

        StringBuilder sb = new StringBuilder("Top Scores:\n");
        for (int i = 0; i < scores.size(); i++) {
            HighScore.ScoreEntry entry = scores.get(i);
            sb.append(i + 1).append(". ").append(entry.getName()).append(": ").append(entry.getScore()).append("\n");
        }
        return sb.toString();
    }

    private static void handleKeyEvent(KeyCode keyCode, Stage stage) {
        switch (keyCode) {
            case UP -> currentSelection = (currentSelection - 1 + gameModes.length) % gameModes.length;
            case DOWN -> currentSelection = (currentSelection + 1) % gameModes.length;
            case ENTER -> selectCurrentOption(stage);
            default -> {}
        }
        highlightCurrentSelection();
    }

    private static void highlightCurrentSelection() {
        for (int i = 0; i < gameModes.length; i++) {
            if(i == currentSelection){
                gameModes[i].setStyle(HIGHLIGHT_STYLE);}
            else {
                gameModes[i].setStyle(DEFAULT_STYLE);}
        }
    }

    private static void selectCurrentOption(Stage stage) {
        String selectedMode = gameModes[currentSelection].getText();
        System.out.println(selectedMode + " selected");

        switch (selectedMode) {
            case "Normal Display" -> {
                DrawWindow.main(new String[]{});
                DrawWindow.drawNormalWindow(stage);
            }
            case "Classic" -> new ClassicGameApp().drawClassicGameApp(stage);
            case "Random order" -> new RandomGameApp().drawRandomGameApp(stage);
            case "Random order with undo" -> new RandomUndoGameApp().drawRandomUndoGameApp(stage);
        }
    }
}
