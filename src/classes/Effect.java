package classes;

import main.Settings;

import java.util.Objects;

/**
 * @author Tealeaf
 * @version 2.0.0
 * @since 1.0.0
 */
public class Effect {

    private static final String[] defaultTypes = new String[]{"Enhancement", "Equipment"};
    private String attribute = "";
    private String type = "";
    private double value;
    private transient Source source;

    public Effect() {}

    public Effect(String attribute, String type, double value) {
        this(attribute, type, value, null);
    }

    public Effect(String attribute, String type, double value, Source source) {
        this.attribute = attribute;
        this.type = type;
        this.value = value;
        this.source = source;
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

    public String getValueString() {
        String val = value + "";
        return val.indexOf(".") < 0 ? val : val.replaceAll("0*$", "").replaceAll("\\.$", "");
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
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
        return Objects.hash(attribute, type, value, source);
    }

    @Override
    public String toString() {
        if (value == 0) {
            return attribute;
        } else {
            String val = getValueString();

            if (!Settings.alwaysShowBonusType) {
                for (String s : defaultTypes) {
                    if (s.equalsIgnoreCase(type)) {
                        return val + " " + attribute;
                    }
                }
            }

            return val + " " + type + " " + attribute;
        }
    }
}
