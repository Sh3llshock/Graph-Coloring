import javafx.application.Application;
import javafx.stage.Stage;
/*
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // This does not create a window but initializes the JavaFX runtime
    }

    public static void main(String[] args) {
            Application.launch(MainApp.class, args);
    }
}
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//This method creates the only javafx application we will use and then calls the main menu method
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        showMenu(primaryStage);
    }

    // Method to display the main menu
    public void showMenu(Stage stage) {
        DrawMenuApp.openMenuWindow(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
