package main;

import classes.Effect;
import classes.Item;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author Tealeaf
 * @version 1.0.2.1
 * @since 1.0.2
 */
public class Export {

    public static void export() {
        Stage stage = new Stage();
        stage.setTitle("Export");
        stage.setWidth(800);
        stage.setHeight(500);

        ExportDisplay display = new ExportDisplay();


        GridPane settings = new GridPane();
        settings.setHgap(10);
        settings.setVgap(10);
        settings.setPadding(new Insets(10));

        ChoiceBox<Style> style = new ChoiceBox<>();
        style.getItems().addAll(Style.values());
        style.getSelectionModel().select(Settings.exportSettings.style);
        style.getSelectionModel().selectedItemProperty().addListener((e, o, n) -> Settings.exportSettings.style = n);

        CheckBox includeEffects = new CheckBox("Include Effects");
        includeEffects.setSelected(Settings.exportSettings.includeEffects);
        includeEffects.selectedProperty().addListener((e, o, n) -> Settings.exportSettings.includeEffects = n);

        Button bExport = new Button("Export");
        bExport.setOnAction(e -> {
            switch (Settings.exportSettings.style) {
                case DISCORD:
                    display.display(discordExport());
                    break;
                case FORUMS:
                    break;
                case PLAINTEXT:
            }
            Settings.save();
        });

        settings.add(style, 0, 0);
        settings.add(bExport, 1, 0);
        settings.add(includeEffects, 0, 1);

        HBox content = new HBox(settings, display);

        stage.setScene(new Scene(content));

        stage.show();
    }

    public static List<String> discordExport() {

        List<String> r = new ArrayList<>();


        StringBuilder string = new StringBuilder();

        final boolean includeEffects = Settings.exportSettings.includeEffects;

        for (Item.ItemSlot itemSlot : Main.gear.getSlotItems()) {

            StringBuilder slotBuilder = new StringBuilder("\n");

            slotBuilder.append("\n").append(itemSlot.getSlot()).append(": ").append(itemSlot.getItem().getName());
            if (includeEffects) {
                slotBuilder.append("\n    ");
                for (Effect e : itemSlot.getItem().getEffects()) {
                    if (e.getCompareStatus() == 2) {
                        slotBuilder.append("~~").append(e.toString()).append("~~, ");
                    } else {
                        slotBuilder.append(e.toString()).append(", ");
                    }
                }
            }

            if (string.length() + slotBuilder.length() > 2000) {
                r.add(string.toString());
                string = new StringBuilder();
            }

            string.append(slotBuilder.toString(), 0, slotBuilder.length() - 2);

        }

        r.add(string.toString());

        return r;

    }

    public enum Style {
        DISCORD("Discord"), FORUMS("Forums"), PLAINTEXT("Plaintext");


        private final String name;

        Style(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public static class ExportDisplay extends GridPane {
        public ExportDisplay() {
            super();
        }

        public void display(String string) {
            display(Collections.singletonList(string));
        }

        public void display(List<String> list) {
            super.getChildren().clear();

            int row = 0;

            for (String text : list) {
                TextArea textArea = new TextArea(text);
                textArea.setEditable(false);

                Button bCopy = new Button("Copy to Clipboard");
                bCopy.setGraphic(Main.iconCopy.getImageView(16));
                bCopy.setOnAction(e -> {
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(text);
                    Clipboard.getSystemClipboard().setContent(content);
                });


                super.addRow(row, textArea, bCopy);
                row++;
            }

        }
    }

    public static class ExportSettings {

        public Style style = Style.DISCORD;
        public boolean includeEffects = false;


        public ExportSettings() {}
    }


}
