import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.concurrent.atomic.AtomicBoolean;

public class Giveup {
    public static boolean displayGiveUp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Give Up");
        alert.setHeaderText("Do you really want to give up?");
        alert.setContentText("If you give up, the system will fill in the un-colored vertices");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: linear-gradient(to bottom, #b3b5bb, #94969c); " +
                "-fx-border-color: #f1a208; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10;");
        dialogPane.lookup(".header-panel").setStyle("-fx-background-color: transparent; -fx-text-fill: #f1a208; -fx-font-size: 18px; -fx-font-weight: bold;");
        dialogPane.lookup(".content").setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 14px;");
        dialogPane.getStylesheets().add(Giveup.class.getResource("hints.css").toExternalForm());
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        AtomicBoolean ok_check = new AtomicBoolean(false);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ProjectColour spaghetti = new ProjectColour();
                spaghetti.ComputeChromaticNumber();
                ok_check.set(true);
            }
            else{
                ok_check.set(false);
            }
        });
        return ok_check.get();
    }
}
