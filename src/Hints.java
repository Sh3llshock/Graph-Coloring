import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class Hints {

    public static void displayHints() {
     //   String nextHint = NextNodeHint.implementHint();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hints");
        alert.setHeaderText("Color the highlighted node");
    //    alert.setContentText(nextHint);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #b3b5bb, #94969c); " +
                "-fx-border-color: #f1a208; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10;");

        dialogPane.lookup(".header-panel").setStyle("-fx-background-color: transparent; -fx-text-fill: #f1a208; -fx-font-size: 18px; -fx-font-weight: bold;");
        dialogPane.lookup(".content").setStyle("-fx-background-color: transparent; -fx-text-fill: #b3b5bb; -fx-font-size: 14px;");
        dialogPane.getStylesheets().add(Hints.class.getResource("hints.css").toExternalForm());
        
     
        alert.showAndWait();
    }
    public static void displayHints1() {
        //   String nextHint = NextNodeHint.implementHint();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hints");
        alert.setHeaderText("-");
        //    alert.setContentText(nextHint);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #b3b5bb, #94969c); " +
                "-fx-border-color: #f1a208; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10;");

        dialogPane.lookup(".header-panel").setStyle("-fx-background-color: transparent; -fx-text-fill: #f1a208; -fx-font-size: 18px; -fx-font-weight: bold;");
        dialogPane.lookup(".content").setStyle("-fx-background-color: transparent; -fx-text-fill: #b3b5bb; -fx-font-size: 14px;");
        dialogPane.getStylesheets().add(Hints.class.getResource("hints.css").toExternalForm());
        alert.showAndWait();
    }
        public static void Alert() {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Finished");
            alert.setHeaderText("You have colored all nodes");    

            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #b3b5bb, #94969c); " +
                    "-fx-border-color: #f1a208; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10;");
    
            dialogPane.lookup(".header-panel").setStyle("-fx-background-color: transparent; -fx-text-fill: #f1a208; -fx-font-size: 18px; -fx-font-weight: bold;");
            dialogPane.lookup(".content").setStyle("-fx-background-color: transparent; -fx-text-fill: #b3b5bb; -fx-font-size: 14px;");
            dialogPane.getStylesheets().add(Hints.class.getResource("hints.css").toExternalForm());
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
        
    }
}