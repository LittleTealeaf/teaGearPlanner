package main;

import classes.Effect;
import classes.Gear;
import classes.Item;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Tealeaf
 * @version 1.0.0
 * @since 1.0.0
 */
public class Main extends Application {

    public static final IconGenerator iconAdd = new IconGenerator("iconAdd.png");
    public static final IconGenerator iconDelete = new IconGenerator("iconDelete.png");
    public static final IconGenerator iconWarning = new IconGenerator("iconWarning.png");
    public static final IconGenerator iconError = new IconGenerator("iconError.png");
    public static Gear gear = new Gear();
    public static File file = null;
    private static GridPane itemGrid;
    private static Stage mainStage;

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
     * Generates the Menu Bar
     *
     * @return Menu Bar
     * @version 1.0.0
     * @since 1.0.0
     */
    private static MenuBar getMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("File");

        MenuItem newSet = new MenuItem("New");
        newSet.setOnAction(e -> {
            gear = new Gear();
            populateItemGrid();
        });

        MenuItem open = new MenuItem("Open");
        open.setOnAction(e -> open());

        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> save());

        MenuItem saveAs = new MenuItem("Save As...");
        saveAs.setOnAction(e -> saveAs());

        file.getItems().addAll(newSet, open, save, saveAs);

        Menu help = new Menu("Help");

        MenuItem settings = new MenuItem("Settings");
        settings.setOnAction(e -> Settings.openSettings());

        help.getItems().addAll(settings);


        menuBar.getMenus().addAll(file, help);

        return menuBar;
    }

    /**
     * Prompts the user to open a file
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    private static void open() {
        file = fileUpdate(getGearFileChooser().showOpenDialog(mainStage));
        try {
            gear = (Gear) Json.deserialize(new BufferedReader(new FileReader(file)), false, Gear.class);
            populateItemGrid();
        } catch (Exception ignored) {}
    }

    /**
     * If the file is saved (either via save-as or was opened), then will just save the gear to that file,
     * otherwise uses the {@link #saveAs()} method
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    private static void save() {
        try {
            FileWriter writer = new FileWriter(file);
            Json.serialize(gear, false, writer);
            writer.close();
        } catch (Exception e) {
            saveAs();
        }
    }

    /**
     * Prompts the user with a save-file dialog, then saves the gear to that file
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    private static void saveAs() {
        file = fileUpdate(getGearFileChooser().showSaveDialog(mainStage));
        if (file != null) save();
    }

    /**
     * Generates a File Chooser with a pre-set extension filter of {@code *.gearset}
     *
     * @return Configured File Chooser for {@code *.gearset} files
     * @version 1.0.0
     * @since 1.0.0
     */
    private static FileChooser getGearFileChooser() {
        FileChooser r = new FileChooser();
        r.setInitialDirectory(new File(Settings.fileBuilderPath));
        r.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tea's Gearset File", "*.gearset"));
        return r;
    }

    /**
     * Updates {@link Settings#fileBuilderPath} with the given file's parent directory
     * @param file The file returned from {@link FileChooser#showOpenDialog(Window)} or {@link FileChooser#showSaveDialog(Window)}
     * @return The same file passed through the parameters
     */
    private static File fileUpdate(File file) {
        Settings.fileBuilderPath = file.getParent();
        Settings.save();
        return file;
    }

    /**
     * Populates the Item Grid (Resets the grid and performs all calculations again
     *
     * @version 1.0.0
     * @since 1.0.0
     */
    public static void populateItemGrid() {
        itemGrid.getChildren().clear();

        Breakdowns.updateBreakdowns(gear); // Updates Breakdowns

        String[] headers = {"", "", "Name", "Effects", "Item Sets"};
        for (int i = 0; i < headers.length; i++) {
            Text text = new Text(headers[i]);
            text.setFont(Font.font("ubuntu", FontWeight.BOLD, 12));
            itemGrid.add(text, i, 0);
        }

        List<ItemSlot> items = Arrays.asList(
                new ItemSlot(gear.getGoggles(), "Goggles"),
                new ItemSlot(gear.getHelmet(), "Helmet"),
                new ItemSlot(gear.getNecklace(), "Necklace"),
                new ItemSlot(gear.getTrinket(), "Trinket"),
                new ItemSlot(gear.getArmor(), "Armor"),
                new ItemSlot(gear.getCloak(), "Cloak"),
                new ItemSlot(gear.getBracers(), "Bracers"),
                new ItemSlot(gear.getBelt(), "Belt"),
                new ItemSlot(gear.getRing1(), "Ring 1"),
                new ItemSlot(gear.getRing2(), "Ring 2"),
                new ItemSlot(gear.getGloves(), "Gloves"),
                new ItemSlot(gear.getBoots(), "Boots"),
                new ItemSlot(gear.getMainHand(), "Main Hand"),
                new ItemSlot(gear.getOffHand(), "Off Hand"));

        for (int j = 0; j < items.size(); j++) {
            ItemSlot i = items.get(j);

            Text tSlot = new Text(i.slot);
            tSlot.setFont(Font.font("ubuntu", FontWeight.BOLD, 11));

            Button bClear = new Button();
            bClear.setGraphic(iconDelete.getImageView(16));
            int finalJ = j;
            bClear.setOnAction(e -> {
                switch (i.slot) {
                    case "Goggles":
                        gear.setGoggles(new Item());
                        break;
                    case "Helmet":
                        gear.setHelmet(new Item());
                        break;
                    case "Necklace":
                        gear.setNecklace(new Item());
                        break;
                    case "Trinket":
                        gear.setTrinket(new Item());
                        break;
                    case "Armor":
                        gear.setArmor(new Item());
                        break;
                    case "Cloak":
                        gear.setCloak(new Item());
                        break;
                    case "Bracers":
                        gear.setBracers(new Item());
                        break;
                    case "Belt":
                        gear.setBelt(new Item());
                        break;
                    case "Ring 1":
                        gear.setRing1(new Item());
                        break;
                    case "Ring 2":
                        gear.setRing2(new Item());
                        break;
                    case "Gloves":
                        gear.setGloves(new Item());
                        break;
                    case "Boots":
                        gear.setBoots(new Item());
                        break;
                    case "Main Hand":
                        gear.setMainHand(new Item());
                        break;
                    case "Off Hand":
                        gear.setOffHand(new Item());
                        break;
                    default:
                        break;
                }
                populateItemGrid();
                itemGrid.getChildren().get(6 + finalJ * 5).requestFocus();
            });

            TextField itemName = new TextField();
            itemName.setText(i.item.getName());
            itemName.textProperty().addListener((e, o, n) -> i.item.setName(n));

            TextField itemSet = new TextField();
            itemSet.setText(i.item.getItemSets());
            itemSet.textProperty().addListener((e, o, n) -> i.item.setItemSets(n));
            itemSet.setTooltip(new Tooltip("Separate Item Sets with ','"));


            itemGrid.addRow(j + 1, tSlot, bClear, itemName, new EffectGrid(i.item), itemSet);

        }


    }

    /**
     * Creation of the main stage
     *
     * @param stage Stage created and passed through
     */
    @Override
    public void start(Stage stage) {

        mainStage = stage;

        stage.setTitle("Tealeaf Gear Planner");

        stage.setMaximized(Settings.startMaximized);
        stage.maximizedProperty().addListener((e, o, n) -> Settings.startMaximized = n);

        BorderPane content = new BorderPane();

        content.setTop(getMenuBar());

        //Create the ItemGrid
        itemGrid = new GridPane();
        itemGrid.setVgap(10);
        itemGrid.setHgap(5);
        itemGrid.setPadding(new Insets(10));


        populateItemGrid();

        ScrollPane scrollPaneItem = new ScrollPane(itemGrid);
        scrollPaneItem.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneItem.setPrefWidth(885);

        ScrollPane scrollPaneBreakdowns = new ScrollPane(Breakdowns.gridBreakdown);
        scrollPaneBreakdowns.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        HBox.setHgrow(scrollPaneBreakdowns, Priority.ALWAYS);


        content.setCenter(new HBox(scrollPaneItem, scrollPaneBreakdowns));


        Scene scene = new Scene(content);
        scene.setOnKeyPressed(keyPress -> {
            if (keyPress.getCode() == KeyCode.F5) {
                populateItemGrid();

            }
        });

        stage.setScene(scene);

        stage.show();
    }

    /**
     * A Class linking an item to a specific gear slot
     *
     * @author Tealeaf
     * @version 1.0.0
     * @since 1.0.0
     */
    private static class ItemSlot {
        private final Item item;
        private final String slot;

        public ItemSlot(Item item, String slot) {
            this.item = item;
            this.slot = slot;
        }
    }

    /**
     * Displays an interactable interface for an item's effects / enchantments
     *
     * @author Tealeaf
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class EffectGrid extends GridPane {
        final int warningWidth = 40;
        final int nameWidth = 160;
        final int typeWidth = 110;
        final int valueWidth = 70;
        private final Item item;

        /**
         * Crates an effect grid from a given item
         *
         * @param item An item that contains a  list of effects
         * @version 1.0.0
         * @since 1.0.0
         */
        public EffectGrid(Item item) {
            super();
            this.item = item;
            updateGridPane();
        }

        /**
         * Updates the Grid Pane (refreshes it, updates everything, etc)
         *
         * @version 1.0.0
         * @since 1.0.0
         */
        public void updateGridPane() {
            //Clears the children and column constraints
            super.getChildren().clear();
            super.getColumnConstraints().clear();

            if (item.getEffects().size() > 0) {
                //Only applies column constraints when there are effects
                super.getColumnConstraints().addAll(new ColumnConstraints(warningWidth), new ColumnConstraints(nameWidth), new ColumnConstraints(typeWidth), new ColumnConstraints(valueWidth));


                //Headers
                Text hName = new Text("Name");
                Text hType = new Text("Type");
                Text hValue = new Text("Value");

                super.addRow(0, new Text(""), hName, hType, hValue);
            }

            int row = 0;

            for (Effect e : item.getEffects()) {

                row++;

                //Name Field
                TextField name = new TextField();
                name.setText(e.getName());
                name.textProperty().addListener((i, o, n) -> e.setName(n));
                name.setMaxWidth(nameWidth);
                super.add(name, 1, row);

                //Effect Type field
                TextField type = new TextField();
                type.setText(e.getType());
                type.textProperty().addListener((i, o, n) -> e.setType(n));
                type.setMaxWidth(typeWidth);
                super.add(type, 2, row);

                //Effect Value field
                Spinner<Double> value = new Spinner<>();
                value.setEditable(true);
                value.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, e.getValue()));
                value.getValueFactory().valueProperty().addListener((i, o, n) -> e.setValue(n));
                value.getEditor().focusedProperty().addListener((i, o, n) -> Platform.runLater(() -> { // Highlights all text when you focus the text property

                    if (n && !value.getEditor().getText().contentEquals("")) {
                        value.getEditor().selectAll();
                    }

                }));
                value.setMaxWidth(valueWidth);
                super.add(value, 3, row);

                //The Delete Button
                Button bRemove = new Button();
                bRemove.setGraphic(iconDelete.getImageView(16));
                bRemove.setOnAction(action -> {
                    item.getEffects().remove(e);
                    updateGridPane();
                    super.getChildren().get(super.getChildren().size() - 1).requestFocus();
                });
                super.add(bRemove, 4, row);

                //Icon Warning
                if (e.getCompareStatus() == 1) {
                    super.add(iconWarning.getImageView(18), 0, row);
                } else if (e.getCompareStatus() == 2) {
                    super.add(iconError.getImageView(18), 0, row);
                }
            }

            //Add Button

            Button bAdd = new Button();
            bAdd.setGraphic(iconAdd.getImageView(16));
            bAdd.setOnAction(action -> {
                item.getEffects().add(new Effect());
                updateGridPane();
                super.getChildren().get(super.getChildren().size() - 5).requestFocus();
            });

            super.add(bAdd, (item.getEffects().size() > 0) ? 5 : 1, row);


        }

    }

    /**
     * A class used to get icons from resources
     *
     * @author Tealeaf
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class IconGenerator {
        public final Image image;

        /**
         * Creates an icon from the resources folder
         *
         * @param name Full name, including extension, of the image.
         *             <p>Example: {@code iconAdd.png}</p>
         * @version 1.0.0
         * @since 1.0.0
         */
        public IconGenerator(String name) {
            image = new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(name)));
        }

        /**
         * Converts the image into an image view
         *
         * @param size Square-size of the image view
         * @return Image View with the image and given {@code size}
         * @version 1.0.0
         * @since 1.0.0
         */
        public ImageView getImageView(int size) {
            ImageView r = new ImageView(image);
            r.setFitHeight(size);
            r.setFitWidth(size);
            return r;
        }
    }

}
