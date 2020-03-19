package classes;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A representation of an item, it's effects, and the item sets it's a part of
 *
 * @author Tealeaf
 * @version 1.0.0
 * @since 1.0.0
 */
public class Item {

    private String name = "";

    private ArrayList<Effect> effects = new ArrayList<>();

    private String itemSets;

    public Item() {}

    public Item(String name, ArrayList<Effect> effects, String itemSets) {
        this.name = name;
        this.effects = effects;
        this.itemSets = itemSets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    /**
     * Sets the effects of the item
     * <br>Clears the source of each effect first before setting them, does not modify the passed list
     *
     * @param effects Effects to give the item
     *
     * @since 1.0.0
     */
    public void setEffects(ArrayList<Effect> effects) {
        ArrayList<Effect> cleaned = new ArrayList<>(effects);
        for (Effect e : cleaned) {
            e.clearSource();
        }
        this.effects = cleaned;
    }

    /**
     * Returns a list of the item effects, each with a reference to this item
     *
     * @return List of effects with the linked reference
     *
     * @since 1.0.0
     */
    public ArrayList<Effect> getLinkableEffects() {
        ArrayList<Effect> linkedEffects = new ArrayList<>(effects);
        for (Effect e : linkedEffects) {
            e.setSource(this);
        }
        return linkedEffects;
    }

    public String getItemSets() {
        return itemSets;
    }

    public void setItemSets(String itemSets) {
        this.itemSets = itemSets;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, effects);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) &&
                Objects.equals(effects, item.effects);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", effects=" + effects +
                '}';
    }
}
