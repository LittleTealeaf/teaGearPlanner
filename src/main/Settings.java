package main;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Tealeaf
 * @version 1.0.2.1
 * @since 1.0.0
 */
public class Settings {

    //INTERNAL SETTINGS
    public static boolean startMaximized = true;

    //USER MODIFIED SETTINGS
    /**
     * Whether or not the application will display a crash report whenever an error occurs
     * <br>Defaults to {@code True}
     *
     * @since 1.0.0
     */
    public static boolean showCrashReports = true;


    /**
     * List of the extractables
     *
     * @since 1.0.0
     */
    public static List<Breakdowns.Extractable> extractables;

    /**
     * Default export settings
     *
     * @since 1.0.2
     */
    public static Export.ExportSettings exportSettings = new Export.ExportSettings();
    /**
     * Initial Directory for the Open and Close file choosers
     *
     * @since 1.0.1
     */
    public static String fileBuilderPath = System.getProperty("user.home");


    public Settings() {}

    /**
     * Attempts to load the data, and then saves (so any settings not initialized will be saved)
     *
     * @since 1.0.0
     */
    public static void load() {
        try {
            extractables = ((ExtractableContainer) Json.deserialize(new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("extractables")))), false, ExtractableContainer.class)).extractables;
        } catch (Exception e) {
            extractables = new ArrayList<>();
            e.printStackTrace();
        }


        Json.readObject(true, Settings.class, "Settings.json");
        save();
    }

    public static void save() {
        Json.saveObject(new Settings(), true, "Settings.json");
    }

    /**
     * Opens the Settings UI
     *
     * @since 1.0.0
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
     *
     * @since 1.0.0
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


        Button bExtractSave = new Button("Save");
        bExtractSave.setOnAction(e -> save());
        ScrollPane extractablePane = new ScrollPane(new ExtractableInterface());
        HBox extrContents = new HBox(bExtractSave, extractablePane);
        extrContents.setSpacing(5);
        settingObs.add(new SettingObj("Extractables", false, extrContents, "Extractables", ""));

        return settingObs;
    }

    /**
     * A Grid-Pane interface for modifying the extractables
     *
     * @author Tealeaf
     * @version 1.0.0
     * @see Breakdowns.Extractable
     * @since 1.0.0
     */
    private static class ExtractableInterface extends GridPane {

        /**
         * Creates a new ExtractableInterface. Populates the GridPane by invoking the {@link #updateGridPane()} method
         *
         * @since 1.0.0
         */
        public ExtractableInterface() {
            super();
            updateGridPane();
        }

        /**
         * Updates the Grid Pane, populating it with extractables listed in {@link #extractables}
         *
         * @since 1.0.0
         */
        public void updateGridPane() {
            super.getChildren().clear();

            int row = 0;

            for (Breakdowns.Extractable extractable : extractables) {
                row++;

                Button bDelete = new Button();
                bDelete.setGraphic(Main.iconDelete.getImageView(16));
                bDelete.setOnAction(e -> {
                    extractables.remove(extractable);
                    updateGridPane();
                });
                super.add(bDelete, 1, row);

                TextField effectFrom = new TextField();
                effectFrom.setText(extractable.getName());
                effectFrom.textProperty().addListener((e, o, n) -> extractable.setName(n));
                super.add(effectFrom, 2, row);

                super.add(new PairInterface(extractable), 3, row);

            }

            Button bAdd = new Button();
            bAdd.setGraphic(Main.iconAdd.getImageView(16));
            bAdd.setOnAction(e -> {
                extractables.add(new Breakdowns.Extractable());
                updateGridPane();
            });
            super.add(bAdd, 0, row);


        }

        /**
         * Displaying of the individual pairs for the Extractable Interface
         *
         * @author Tealeaf
         * @version 1.0.0
         * @see ExtractableInterface
         * @see Breakdowns.Extractable.Pair
         * @since 1.0.0
         */
        private static class PairInterface extends GridPane {
            private final Breakdowns.Extractable extractable;

            /**
             * Creates a new PairInterface from a given Extractable
             * <p>This populates the GridPane by invoking the {@link #updateGridPane()}} method</p>
             *
             * @param extractable The extractable that the Pair-Interface is paired to
             *
             * @since 1.0.0
             */
            public PairInterface(Breakdowns.Extractable extractable) {
                super();
                this.extractable = extractable;
                updateGridPane();
            }

            /**
             * Updates the grid pane, populating it with pairs specified in the {@code Extractable}
             *
             * @since 1.0.0
             */
            public void updateGridPane() {
                super.getChildren().clear();

                int row = 0;

                super.add(new Text("Effect"), 2, 0);
                super.add(new Text("Type-Replace"), 3, 0);
                super.add(new Text("Type-Add"), 4, 0);

                for (Breakdowns.Extractable.Pair pair : extractable.getReplace()) {
                    row++;

                    Button bDelete = new Button();
                    bDelete.setGraphic(Main.iconDelete.getImageView(16));
                    bDelete.setOnAction(e -> {
                        extractable.getReplace().remove(pair);
                        updateGridPane();
                    });
                    if (extractable.getReplace().size() > 1) super.add(bDelete, 1, row);

                    TextField effect = new TextField();
                    effect.setText(pair.getName());
                    effect.textProperty().addListener((e, o, n) -> pair.setName(n));
                    super.add(effect, 2, row);

                    TextField typeReplace = new TextField();
                    typeReplace.setText(pair.getTypeReplace());
                    typeReplace.textProperty().addListener((e, o, n) -> pair.setTypeReplace(n));
                    super.add(typeReplace, 3, row);

                    TextField typeAdd = new TextField();
                    typeAdd.setText(pair.getTypeAdd());
                    typeAdd.textProperty().addListener((e, o, n) -> pair.setTypeAdd(n));
                    super.add(typeAdd, 4, row);
                }

                Button bAdd = new Button();
                bAdd.setGraphic(Main.iconAdd.getImageView(16));
                bAdd.setOnAction(e -> {
                    extractable.getReplace().add(new Breakdowns.Extractable.Pair());
                    updateGridPane();
                });
                super.add(bAdd, 0, row);
            }
        }

    }

    /**
     * A modular setting class that includes a setting, it's visual element, and keywords
     *
     * @author Tealeaf
     * @version 1.0.0
     * @since 1.0.0
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
         *
         * @since 1.0.0
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
         *
         * @since 1.0.0
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
         *
         * @return {@code True} if this setting matches the filter<br>{@code False} if it does not
         *
         * @since 1.0.0
         */
        public boolean contains(String contents) {
            return contents.contentEquals("") || name.contains(contents.toLowerCase()) || keyWords.contains(contents.toLowerCase()) || category.contains(contents.toLowerCase());
        }

        /**
         * Compares whether or not this SettingObj's values equals a given object
         *
         * @since 1.0.0
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
         *
         * @since 1.0.0
         */
        @Override
        public String toString() {
            return name + " (" + category + ")";
        }
    }

    /**
     * A Container object used to store extractables in the initial values, found in the resources file
     */
    private static class ExtractableContainer {
        public final List<Breakdowns.Extractable> extractables;

        public ExtractableContainer(List<Breakdowns.Extractable> extractables) {
            this.extractables = extractables;
        }
    }
}