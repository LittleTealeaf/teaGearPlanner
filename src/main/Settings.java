package main;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Tealeaf
 * @version 0.2.0
 * @since 0.1.0
 */
public class Settings {


    public static boolean startMaximized = true;


    /**
     * Whether or not the application will display a crash report whenever an error occurs
     * <br>Defaults to {@code True}
     *
     * @since 0.1.0
     */
    public static boolean showCrashReports = true;

    /**
     * Whether or not to exclude default bonus types
     * <br>Example:
     * <ul><li>{@code False} - 21 Constitution</li>
     * <li>{@code True} - 21 Enhancement Constitution</li></ul>
     * <br>Defaults to {@code True}
     */
    public static boolean alwaysShowBonusType = false;

    /**
     * Initial Directory for the Open and Close file choosers
     *
     * @since 0.1.1
     */
    public static String fileBuilderPath = System.getProperty("user.home");


    public Settings() {}

    /**
     * Attempts to load the data, and then saves (so any settings not initialized will be saved)
     *
     * @since 0.1.0
     */
    public static void load() {
        Json.readObject(true, Settings.class, "Settings.json");
        save();
    }

    public static void save() {
        Json.saveObject(new Settings(), true, "Settings.json");
    }

    /**
     * Opens the Settings UI
     *
     * @since 0.1.0
     */
    public static void openSettings() {
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setWidth(500);

        //Get the Settings
        List<SettingObj> sobjs = getSettingObjects();

        //Create the Categories
        ListView<String> category = new ListView<>();
        category.setPrefWidth(150);
        for (SettingObj obj : sobjs) {
            if (!category.getItems().contains(obj.category)) {
                category.getItems().add(obj.category);
            }
        }

        GridPane sectionDisplay = new GridPane();
        sectionDisplay.setPadding(new Insets(10));
        sectionDisplay.setHgap(10);
        sectionDisplay.setVgap(10);

        //Create the Filter
        TextField filter = new TextField();
        filter.setPromptText("Filter Settings");
        filter.textProperty().addListener((e, o, n) -> {
            String currentSelected = category.getSelectionModel().getSelectedItem();
            List<String> categories = new ArrayList<>();

            for (SettingObj setting : sobjs) {
                if (setting.contains(n) && !categories.contains(setting.category)) {
                    categories.add(setting.category);
                }
            }

            category.getItems().setAll(categories);

            if (categories.contains(currentSelected)) {
                category.getSelectionModel().select(currentSelected);
            } else {
                category.getSelectionModel().select(0);
            }
        });

        //Generating Category Settings
        category.getSelectionModel().selectedItemProperty().addListener((e, o, n) -> {
            //Cleans GridPane
            sectionDisplay.getChildren().clear();

            //Gets a list of the setting objs to display
            int row = 0;
            for (SettingObj setting : sobjs) {
                if (setting.category.equals(n) && setting.contains(filter.getText())) {
                    if (setting.showName) {
                        sectionDisplay.add(new Text(setting.name), 0, row);
                        sectionDisplay.add(setting.node, 1, row);
                    } else {
                        sectionDisplay.add(setting.node, 0, row, 2, 1);
                    }
                    row++;
                }
            }
        });

        HBox center = new HBox(new VBox(filter, category), sectionDisplay);

        Scene scene = new Scene(center);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Gets all the setting objects, each one with a given category, node, and keywords
     *
     * @return List of {@link SettingObj SettingObjs}
     * @since 0.1.0
     */
    private static List<SettingObj> getSettingObjects() {
        List<SettingObj> settingObs = new ArrayList<>();

        //Show Crash Reports
        CheckBox sShowCrashReports = new CheckBox("Show Crash Reports");
        sShowCrashReports.setSelected(showCrashReports);
        sShowCrashReports.selectedProperty().addListener((e, o, n) -> {
            showCrashReports = n;
            save();
        });
        sShowCrashReports.setTooltip(new Tooltip("When set to true, will display a crash screen with options to create a bug report on Github"));
        settingObs.add(new SettingObj("Show Crash Reports", false, sShowCrashReports, "Advanced", "github crash report crash log debug"));


        CheckBox sAlwaysShowBonusType = new CheckBox("Always Show Bonus Type");
        sAlwaysShowBonusType.setTooltip(new Tooltip("If checked, will always display the bonus type of an effect. \nIf unchecked, effects such as +21 Enhancement Constitution \nWill be displayed as +21 Constitution"));
        sAlwaysShowBonusType.setSelected(alwaysShowBonusType);
        sAlwaysShowBonusType.selectedProperty().addListener((e, o, n) -> {
            alwaysShowBonusType = n;
            save();
        });
        settingObs.add(new SettingObj("Always Show Bonus Type", false, sAlwaysShowBonusType, "Display", "display bonus type effect attribute enhancement equipment"));


        return settingObs;
    }


    /**
     * A modular setting class that includes a setting, it's visual element, and keywords
     *
     * @author Tealeaf
     * @version 0.1.0
     * @since 0.1.0
     */
    private static class SettingObj {
        private final String name;
        private final Node node;
        private final String category;
        private final String keyWords;
        private boolean showName = true;

        /**
         * Creates a SettingObj object with the given parameters
         * <br>{@code showName} defaults to true with this constructor
         *
         * @param name     Name of the Setting
         * @param node     {@code JavaFX} {@link Node} of the setting
         * @param category Category of the Setting
         * @param keyWords Key Search Terms of the setting
         * @since 0.1.0
         */
        public SettingObj(String name, Node node, String category, String keyWords) {
            this.name = name;
            this.node = node;
            this.category = category;
            this.keyWords = keyWords;
        }

        /**
         * Creates a SettingObj object with the given parameters
         *
         * @param name     Name of the Setting
         * @param showName Whether or not to include the name in the settings page
         * @param node     {@code JavaFX} {@link Node} of the setting
         * @param category Category of the Setting
         * @param keyWords Key Search Terms of the setting
         * @since 0.1.0
         */
        public SettingObj(String name, boolean showName, Node node, String category, String keyWords) {
            this.name = name;
            this.showName = showName;
            this.node = node;
            this.category = category;
            this.keyWords = keyWords;
        }

        /**
         * Filters out if this setting matches the given filter
         *
         * @param contents String Filter
         * @return {@code True} if this setting matches the filter<br>{@code False} if it does not
         * @since 0.1.0
         */
        public boolean contains(String contents) {
            return contents.contentEquals("") || name.contains(contents.toLowerCase()) || keyWords.contains(contents.toLowerCase()) || category.contains(contents.toLowerCase());
        }

        /**
         * Compares whether or not this SettingObj's values equals a given object
         *
         * @since 0.1.0
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SettingObj that = (SettingObj) o;
            return showName == that.showName &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(node, that.node) &&
                    Objects.equals(category, that.category) &&
                    Objects.equals(keyWords, that.keyWords);

        }

        /**
         * Returns a String object that contains the name and it's category
         *
         * @return String representing the setting's name and category
         * @since 0.1.0
         */
        @Override
        public String toString() {
            return name + " (" + category + ")";
        }
    }
}