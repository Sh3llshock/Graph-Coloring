import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class NextRandomNodeAlert {
    

    public static void Alert() {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Finished");
            alert.setHeaderText("You have finished");    
            alert.getButtonTypes().setAll(ButtonType.OK);
            alert.showAndWait();
            
        
    }

    public static void invalidAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid");
        alert.setHeaderText("You have not chosen a valid colouring patter");
        alert.setContentText("Please try again");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
}

