package classes;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 * @author Tealeaf
 * @version 2.0.0
 * @since 1.0.0
 */
public class Effect {

    private String attribute = "";
    private String type = "";
    private double value = 0;

    public Effect(Effect e) {
        this.attribute = e.attribute;
        this.type = e.type;
        this.value = e.value;
    }

    public Effect() {}

    public Node getEditNode() {

        TextField textType = new TextField();
        textType.setText(type);
        textType.textProperty().addListener((e, o, n) -> setType(n));
        textType.setTooltip(new Tooltip("Bonus Type of the Effect.\nExamples include \"Enhancement\",\"Insightful\",\"Quality\",etc."));

        TextField textAttribute = new TextField();
        textAttribute.setText(attribute);
        textAttribute.textProperty().addListener((e, o, n) -> setAttribute(n));
        textAttribute.setTooltip(new Tooltip("Attribute Name of the Effect"));

        Spinner<Double> spinnerValue = new Spinner<>();
        spinnerValue.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-100, 100, value));
        spinnerValue.getValueFactory().valueProperty().addListener((e, o, n) -> setValue(n));
        spinnerValue.setTooltip(new Tooltip("Value of the Bonus"));
        spinnerValue.setEditable(true);

        HBox hbox = new HBox(textAttribute, textType, spinnerValue);

        return hbox;
    }

    public Node getDisplayNode() {
        Text text = new Text(value + " " + type + " " + attribute);
        return text;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Effect effect = (Effect) o;
        return Double.compare(effect.value, value) == 0 &&
                Objects.equals(attribute, effect.attribute) &&
                Objects.equals(type, effect.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, type, value);
    }

    @Override
    public Effect clone() {
        Effect e = new Effect();
        e.setAttribute(this.attribute);
        e.setType(this.type);
        e.setValue(this.value);
        return e;
    }
}
