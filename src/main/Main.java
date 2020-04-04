package main;

import classes.Effect;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author Tealeaf
 * @version 2.0.0
 * @since 1.0.0
 */
public class Main extends Application {

    public static Stage stage;

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
        Main.stage = stage;
        stage.setMaximized(Settings.startMaximized);
        stage.maximizedProperty().addListener((e, o, n) -> {
            Settings.startMaximized = n;
            Settings.save();
        });

        stage.setScene(new Scene(new HBox(new Effect().getEditNode())));

        stage.show();
    }


}
