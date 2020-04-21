package main;

import classes.EffectSlider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
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

    static Node effect;

    public static void configSpinner(Spinner... spinners) {
        for (Spinner spinner : spinners) {
            configSpinner(spinner);
        }
    }

    public static void configSpinner(Spinner spinner) {
        spinner.setEditable(true);
        spinner.getEditor().focusedProperty().addListener((i, o, n) -> Platform.runLater(() -> { // Highlights all text when you focus the text property
            if (n && !spinner.getEditor().getText().contentEquals("")) {
                spinner.getEditor().selectAll();
            }
        }));
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

        GridPane grid = new GridPane();

        grid.add(new EffectSlider().getDisplayNode(), 0, 0);

        Scene scene = new Scene(grid);

        stage.setScene(scene);
        stage.show();
    }
}
