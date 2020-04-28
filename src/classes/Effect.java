package classes;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.Main;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Tealeaf
 * @version 2.0.1
 * @since 1.0.0
 */
public class Effect {
    /*
    Goals with 2.0.1
     - Single Effect class, which changes itself depending on what the user needs

     Functionality
        Editing node has the text field for attributes, bonus types, and value

        When the user separates multiple attributes by commas (','), the program changes the attribute field on all displays to a combo-box with all attributes
        When the user inputs a range of numbers into the value field (such as "1-4"), the program replaces the value with a spinner that ranges between those values.

     Notes:
        The attribute separation is combined with the type, so the drop-down menu will have "Insightful Constitution", "Insightful Wisdom", etc.
     */

    private String[] attributes;
    private int attributeIndex = 0;
    private String bonusType;

    /*
    If min == max, then it is a given static value
    if min != max, then it is a range between the values
     */
    private double min = 0;
    private double max = 0;
    private double value = 0;

    private Source source;


    public Effect() {
        attributes = new String[]{""};
    }

    /**
     * Creates an editing node, or the User Interface that is used to edit the item's effect
     * <p>This node consists of 3 boxes, with labels above each one. There is a text field for the {@code Value}, {@code Bonus Type}, and {@code Attribute}.</p>
     * <ul><li>{@code Value} - The value of the effect. The user can input either a single number, or specify a range of numbers, such as typing "1-4" for a range of 1-4.</li>
     * <li>{@code Bonus Type} - The stacking / bonus type of the effect. This specifies what type of bonus the effect has, such that multiple values of the same bonus type to the same effect do not
     * stack</li>
     * <li>{@code Attribute} - The Attribute that the effect gives a bonus to. Separating multiple effects by commas will present a multi-selector, giving multiple options on the display node
     * .</li></ul>
     * <p>Changing values in here will not automatically update any current instances of {@link #getDisplayNode()}</p>
     *
     * @return {@link GridPane} with the specified {@link TextField Text Fields} and their labels above
     * @since 2.0.0
     * @see #getDisplayNode()
     */
    public Node getEditNode() {
        GridPane gridPane = new GridPane();

        //Headers
        gridPane.addRow(0, new Text("Value"), new Text("Bonus Type"), new Text("Attribute"));

        TextField textValue = new TextField();
        textValue.setText(min == max ? value + "" : min + "-" + max);
        textValue.textProperty().addListener((e, o, n) -> {
            try {
                setValue(n);
            } catch (Exception exception) {
                if (!n.contains("-") && !n.contentEquals("")) {
                    textValue.setText(o);
                }
            }
        });
        textValue.focusedProperty().addListener((e, o, n) -> {
            if (!n && textValue.getText().contentEquals("")) {
                textValue.setText("0");
            }
        });
        textValue.setTooltip(Main.generateTooltip("The value of the effect.\nTo create a slider, specify the range of the effect. For example, \"15\" will result in the effect giving a bonus of 15," +
                "while typing \"10-16\" will result in a slider that ranges between 10 and 15, and the defualt value being 15"));
        textValue.setPrefWidth(75);
        gridPane.add(textValue, 0, 1);

        TextField textBonusType = new TextField();
        textBonusType.setText(bonusType);
        textBonusType.textProperty().addListener((e, o, n) -> setBonusType(n));
        textBonusType.setTooltip(Main.generateTooltip("The bonus, or stacking, type of the effect. \nEffects with the same bonus type will not stack, and only the highest one will apply. For " +
                "example, an Enhancement +15 and Enhancement +10 bonus when put together would only yield a +15 Enhancement bonus to an attribute.\nDifferent Attributes, however, stack. For " +
                "example, an Insightful +10 and Enhancement +22 will yield a net +32 bonus to a given attribute"));
        gridPane.add(textBonusType, 1, 1);

        TextField textAttributes = new TextField();
        StringBuilder stringBuilder = new StringBuilder();
        for (String attribute : attributes) {
            stringBuilder.append(attribute + ",");
        }
        textAttributes.setText(stringBuilder.substring(0, stringBuilder.length() - 3));
        textAttributes.textProperty().addListener((e, o, n) -> setAttributes(n));
        textAttributes.setTooltip(Main.generateTooltip("Attribute name of the effect bonus. Examples include \"Constitution\". \"Wisdom\". \"Positive Spell Power\",\"Intimidate\", etc.\nTo create " +
                "an effect with a specific selection of items, such as a craftable item, separate each choice with a comma.\nFor example, an item with a craftable selection of Charisma, " +
                "Constitution, and Wisdom would be as such: \"Charisma,Constitution,Wisdom\".\nMake sure to only include spaces if the effect name has a space in it"));
        gridPane.add(textAttributes, 2, 1);

        return gridPane;
    }

    /**
     * Creates a display node for the effect, or the User Interface to display for the effect in the gear-set layout
     * <p>This displays the value, bonus type, and specified attribute in a single line</p>
     * <ul><li>{@code Value} - The value will display as a single number (in a {@link Text} object), if a single number is specified. Otherwise, if the effect specifies a range, it will display a
     * {@link Spinner} with the minimum and maximum values as the specified minimum and maximum values</li>
     * <li>{@code Bonus Type} - Will always be a {@link Text} object with the bonus type.</li>
     * <li>{@code Attribute} - If only one attribute is specified, will combine with the {@code Bonus Type}'s {@link Text} as the given Attribute. If multiple attributes are specified, this will
     * display a {@link ComboBox} of all the given options, updating the effect whenever the user selects an option</li></ul>
     *
     * @return An {@link HBox} with UI elements representing the effect
     * @since 2.0.0
     * @see #getEditNode()
     */
    public Node getDisplayNode() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);

        //Value
        if (min == max) {
            Text textValue = new Text(value + "");
            hbox.getChildren().add(textValue);
        } else {
            Spinner<Double> valueSpinner = new Spinner<>();
            valueSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory.DoubleSpinnerValueFactory(min, max, value));
            valueSpinner.valueProperty().addListener((e, o, n) -> setValue(n));
            valueSpinner.setPrefWidth(75);
            Main.configSpinner(valueSpinner);
            hbox.getChildren().add(valueSpinner);
        }

        if (attributes.length > 1) {

            Text textBonusType = new Text(bonusType);
            hbox.getChildren().add(textBonusType);

            ComboBox<String> attributeCombo = new ComboBox<>();
            attributeCombo.setItems(FXCollections.observableArrayList(attributes));
            attributeCombo.getSelectionModel().select(attributeIndex);
            attributeCombo.getSelectionModel().selectedIndexProperty().addListener((e, o, n) -> setAttributeIndex(n.intValue()));
            hbox.getChildren().add(attributeCombo);
        } else {
            Text attributeText = new Text(bonusType + " " + getAttribute());
            hbox.getChildren().add(attributeText);
        }

        return hbox;
    }

    public String[] getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes from a user-inputted string.
     * <ul><li>If only a single attribute is given, will set the attribute array to an array with {@code length = 1}, the first value being the specified attribute</li>
     * <li>If multiple attributes are given, this will separate the attributes (separated by commas) into the array.</li></ul>
     * Then this will verify that the set index is within the range of the items, defaulting to {@code index = 0}.
     *
     * @param input User string input
     * @see #getEditNode()
     * @since 2.0.1
     */
    public void setAttributes(String input) {
        if (input == null) {
            return;
        }
        attributes = input.split(",");
        if (attributeIndex >= attributes.length) {
            attributeIndex = 0;
        }
    }

    /**
     * Gets the currently selected attribute, whether or not there is multiple attributes specified in the array
     * @return Attribute of the effect
     * @since 2.0.1
     */
    public String getAttribute() {
        return attributes[attributeIndex];
    }

    /**
     * @since 2.0.1
     * @return The {@code Index} of the selected {@code Attribute}
     */
    public int getAttributeIndex() {
        return attributeIndex;
    }

    /**
     * Sets the attribute's current index.
     * <p>This function is typically called by the {@link #getDisplayNode()} method, if there are multiple attributes specified</p>
     * @param attributeIndex The index of the attribute. Checks whether or not the index given is within the attribute array's range. If it is out of bounds, the stored attribute index will be unchanged.
     * @see #getDisplayNode()
     * @since 2.0.1
     */
    public void setAttributeIndex(int attributeIndex) {
        if (attributeIndex < attributes.length && attributeIndex > -1) {
            this.attributeIndex = attributeIndex;
        } else {
            System.out.println("Error: Index Out of Bounds");
        }
    }

    /**
     * @since 2.0.1
     * @return The selected {@code Value} of the effect
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the effect.
     * <p>This will clamp the value between the {@code Minimum} and the {@code Maximum} if the effect has a specified range.</p>
     * @param value Effect value to set to the item.
     * @since 2.0.1
     */
    public void setValue(double value) {
        this.value = value;
        if(min != max) {
            if (value > max) {
                value = max;
            } else if (value < min) {
                value = min;
            }
        } else {
            max = value;
            min = value;
        }
    }

    /**
     * Sets the value of the effect.
     * <p>This will take a user-input and set the {@code Value}, {@code Maximum}, and {@code Minimum} depending on the input</p>
     * <ul><li>If the user specifies a single number, the {@code Value}, {@code Maximum}, and {@code Minimum} will all be set to that number</li>
     *     <li>If the user specifies a range ({@code "4-7"}), this will set the {@code Minimum} and {@code Maximum} values accordingly, and the {@code Value} will be set to the {@code Maximum}</li>
     * </ul>
     * @param input User-Input String for the effect's {@code Value}, specifically from the {@link #getEditNode()}
     * @see #getEditNode()
     * @since 2.0.1
     */
    public void setValue(String input) {
        if (input == null) {
            return;
        } else if (input.contains("-")) {
            String[] parse = input.split("-");
            min = Double.parseDouble(parse[0]);
            max = Double.parseDouble(parse[1]);
            value = max;
        } else {
            value = Double.parseDouble(input);
            max = value;
            min = value;
        }
    }

    /**
     * Sets the value of the effect.
     * <p>If the {@code Value} is out of the range specified by the {@code Maximum} and {@code Minimum}, it will extend the range to include the given value</p>
     * @param value Effect {@code Value} to set to the item.
     * @since 2.0.1
     */
    public void setValueOverride(double value) {
        this.value = value;
        if (value > max) {
            max = value;
        } else if (value < min) {
            min = value;
        }
    }

    /**
     * @since 2.0.1
     * @return The effect's {@code Bonus Type}
     */
    public String getBonusType() {
        return bonusType;
    }

    /**
     * Sets the effect's Bonus Type
     * @since 2.0.1
     * @param bonusType The specific bonus type of the effect
     */
    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    /**
     * @since 2.0.1
     * @return Effect's original source.
     */
    public Source getSource() {
        return source;
    }

    /**
     * Sets the effect's source
     * @param source The effect's source
     * @since 2.0.1
     */
    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Effect effect = (Effect) o;
        return getAttributeIndex() == effect.getAttributeIndex() &&
                Double.compare(effect.min, min) == 0 &&
                Double.compare(effect.max, max) == 0 &&
                Double.compare(effect.getValue(), getValue()) == 0 &&
                Arrays.equals(getAttributes(), effect.getAttributes()) &&
                Objects.equals(getBonusType(), effect.getBonusType()) &&
                Objects.equals(getSource(), effect.getSource());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getAttributeIndex(), getBonusType(), min, max, getValue(), getSource());
        result = 31 * result + Arrays.hashCode(getAttributes());
        return result;
    }

    @Override
    public Effect clone() {
        Effect effect = new Effect();
        effect.attributes = this.attributes;
        effect.attributeIndex = this.attributeIndex;
        effect.bonusType = this.bonusType;
        effect.max = this.max;
        effect.min = this.min;
        effect.value = this.value;
        return effect;
    }
}
