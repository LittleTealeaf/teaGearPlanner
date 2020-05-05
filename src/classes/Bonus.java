package classes;

import java.util.Objects;

/**
 * @author Tealeaf
 * @version 0.3.0
 * @since 0.3.0
 */
public class Bonus {

    private String attribute = "";
    private String type = "";
    private double value = 0.0;

    private transient Source source;

    public Bonus() {}

    public Bonus(String attribute, String type, double value) {
        this.attribute = attribute;
        this.type = type;
        this.value = value;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bonus bonus = (Bonus) o;
        return Double.compare(bonus.getValue(), getValue()) == 0 &&
                Objects.equals(getAttribute(), bonus.getAttribute()) &&
                Objects.equals(getType(), bonus.getType()) &&
                Objects.equals(getSource(), bonus.getSource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttribute(), getType(), getValue(), getSource());
    }

    @Override
    public String toString() {
        return attribute + " " + type + " " + value;
    }
}
