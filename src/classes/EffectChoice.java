package classes;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class EffectChoice extends Effect {

    private String[] attributes;

    private int choice = 0;



    public EffectChoice() {
        super();
    }

    public String getAttributes() {

        if(attributes == null) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String attribute : attributes) {
            stringBuilder.append(attribute + ",");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    public void setAttributes(String string) {
        attributes = string.split(",");
    }

    @Override
    public Node getEditNode() {

        TextField textType = new TextField();
        textType.setText(getType());
        textType.textProperty().addListener((e, o, n) -> setType(n));
        textType.setTooltip(new Tooltip("Bonus Type of the Effect.\nExamples include \"Enhancement\",\"Insightful\",\"Quality\",etc."));

        TextField textAttribute = new TextField();
        textAttribute.setText(getAttributes());
        textAttribute.textProperty().addListener((e, o, n) -> setAttributes(n));
        textAttribute.setTooltip(new Tooltip("Attribute Choices, separated by commas\nExample: \"Strength,Constitution,Wisdom\" will have a choice of Strength, Constitution, or Wisdom"));

        Spinner<Double> spinnerValue = new Spinner<>();
        spinnerValue.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, getValue()));
        spinnerValue.getValueFactory().valueProperty().addListener((e, o, n) -> setValue(n));
        spinnerValue.setTooltip(new Tooltip("Value of the Bonus"));
        spinnerValue.setEditable(true);
        spinnerValue.getEditor().focusedProperty().addListener((i, o, n) -> Platform.runLater(() -> { // Highlights all text when you focus the text property
            if (n && !spinnerValue.getEditor().getText().contentEquals("")) {
                spinnerValue.getEditor().selectAll();
            }
        }));

        HBox hbox = new HBox(textAttribute, textType, spinnerValue);
        return hbox;
    }

    @Override
    public Node getDisplayNode() {
        if(attributes == null || attributes.length == 0) {
            return new ComboBox<>();
        }

        String[] options = new String[attributes.length];

        for(int i = 0; i < attributes.length; i++) {
            options[i] = Effect.displayEffectFormat(getValue(),getType(),attributes[i]);
        }

        ComboBox<String> selectionBox = new ComboBox<>();
        selectionBox.setItems(FXCollections.observableArrayList(options));
        selectionBox.getSelectionModel().select(choice);
        selectionBox.getSelectionModel().selectedIndexProperty().addListener((e,o,n) -> {
            choice = n.intValue();
        });

        return selectionBox;
    }

    @Override
    public String getAttribute() {
        try {
            return attributes[choice];
        } catch (Exception e) {
            choice = 0;
            return attributes[0];
        }
    }

    @Override
    public EffectChoice clone() {
        EffectChoice n = new EffectChoice();
        n.setAttributes(getAttributes());
        return n;
    }
}
