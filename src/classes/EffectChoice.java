package classes;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class EffectChoice extends Effect {

    private String[] attributes = new String[1];

    private int choice = 0;

    public EffectChoice() {
        super();
    }

    public String getAttributes() {
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

        HBox hbox = new HBox(textAttribute, textType, spinnerValue);
        return hbox;
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
}
