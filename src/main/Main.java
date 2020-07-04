package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Tealeaf
 * @version 0.2.0
 * @since 0.1.0
 */
public class Main extends Application {

    public static Stage stage;
    static Node effect;

    /**
     * Main Class
     * <p>To-Do: {@code args} might have some form of "Application Opened from this file", and I could look at having it open directly to that file?</p>
     *
     * @param args Arguments to be passed through into the application when launched
     */
    public static void main(String[] args) {
        System.out.println((double) ((int) 5) / (double) ((int) 7));
        Json.load();
        Settings.load();
        UserData.load();


        launch(args);
    }

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

    public static Tooltip generateTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setWrapText(true);
        tooltip.setPrefWidth(400);
        return tooltip;
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


        Scene scene = new Scene(grid);

        stage.setScene(scene);
        stage.show();
    }
}
