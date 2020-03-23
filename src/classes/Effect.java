package classes;

import java.util.Objects;

/**
 * @author Tealeaf
 * @version 1.0.2
 * @since 1.0.0
 */
public class Effect {

    private String name = "";
    private String type = "";
    private double value = 0;

    private transient Item source = null;
    private transient int compareStatus = 0;

    /**
     * Creates an empty effect
     *
     * @since 1.0.0
     */
    public Effect() {}

    /**
     * Creates an effect with given variables.
     * <p>All variables listed here are non-transient, meaning they will be saved into the gear-save</p>
     *
     * @param name  Official Name of the effect. Capitalization should not matter here, although keeping the first letter of each word capitalized is what is actively tested
     * @param type  Effect bonus type. This is the stacking-type of the effect bonus, examples include: {@code Competence}, {@code Exceptional}, {@code Quality}, etc.
     * @param value Value of the effect bonus
     * @since 1.0.0
     */
    public Effect(String name, String type, double value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Creates an effect with given variables
     * <p>All variables, except for {@code source}, are non-transient, meaning they will be saved into the gear-save</p>
     *
     * @param name   Official Name of the effect. Capitalization should not matter here, although keeping the first letter of each word capitalized is what is actively tested
     * @param type   Effect bonus type. This is the stacking-type of the effect bonus, examples include: {@code Competence}, {@code Exceptional}, {@code Quality}, etc.
     * @param value  Value of the effect bonus
     * @param source ({@code transient}) Item Source of the effect. Provides a link for the effect to say where they come from
     */
    public Effect(String name, String type, double value, Item source) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Item getSource() {
        return source;
    }

    public void setSource(Item source) {
        this.source = source;
    }

    public void clearSource() {
        this.source = null;
    }

    public int getCompareStatus() {
        return compareStatus;
    }

    public void setCompareStatus(int compareStatus) {
        this.compareStatus = compareStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Effect effect = (Effect) o;
        return Double.compare(effect.value, value) == 0 &&
                Objects.equals(name, effect.name) &&
                Objects.equals(type, effect.type);
    }


    @Override
    public String toString() {
        if (value != 0) {
            return type + " " + name + " " + value;
        } else if (!type.contentEquals("")) {
            return type + " " + name;
        } else {
            return name;
        }
    }
}
