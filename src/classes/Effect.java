package classes;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.Main;

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

    public Effect() {}

    /**
     * The "Edit Node" is the Javafx UI Node that is displayed when in the item-editing interface. The purpose of this node is to make it easy for the user to edit the
     * effect's details, such as the attribute, the bonus type (or stacking type), and value.
     * <p>This method is overwritten in further extensions of the {@link Effect} class, since they add more configurations to the specific effect</p>
     * @return An {@link HBox} containing text fields / spinners for configuration of the effect's attribute, bonus type (or stacking type), and value.
     */
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
        Main.configSpinner(spinnerValue);

        return new HBox(textAttribute, textType, spinnerValue);
    }

    /**
     * The "Simple Node" is the Javafx UI Node that is displayed when in the quick-view interface for the gear-set. The purpose of this node is to
     * allow the user to easily see and customize items.
     * <p>This method is overwritten in further extensions of the {@link Effect} class, and will display simple option interfaces</p>
     * @return An empty {@link Text} node.
     */
    public Node getSimpleNode() {
        return new Text("");
    }

    /**
     * The "Display Node" is the Javafx UI Node that is displayed on the detailed view for gear items. The purpose of this node is to allow the user to easily see
     * the effects on a given item.
     * <p>This method is overwritten in further instances of the {@link Effect} class in order to display options and further customization features</p>
     * @return A {@link Text} node, displaying the effect's {@code value}, {@code type}, and {@code attribute}
     */
    public Node getDisplayNode() {
        return new Text(value + " " + type + " " + attribute);
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
