package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Tealeaf
 * @version 1.0.0
 * @since 1.0.0
 */
public class Main extends Application {

    public static Stage mainStage;

    /**
     * Main Class
     * <p>To-Do: {@code args} might have some form of "Application Opened from this file", and I could look at having it open directly to that file?</p>
     *
     * @param args Arguments to be passed through into the application when launched
     */
    public static void main(String[] args) {
        Json.load();
        Settings.load();

        launch(args);
    }

    /**
     * Creation of the main stage
     *
     * @param stage Stage created and passed through
     */
    @Override
    public void start(Stage stage) {

        mainStage = stage;

        stage.setScene(new Scene(new BorderPane()));

        stage.show();
    }

}
