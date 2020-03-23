package classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Tealeaf
 * @version 1.0.2
 * @since 1.0.0
 */
public class Gear {

    private Item goggles = new Item(), helmet = new Item(), necklace = new Item(), trinket = new Item(), armor = new Item(), cloak = new Item(), bracers = new Item(),
            belt = new Item(), ring1 = new Item(), ring2 = new Item(), boots = new Item(), gloves = new Item(), mainHand = new Item(), offHand = new Item();

    private List<String> filter = new ArrayList<>();

    public Gear() {

    }

    public Item getGoggles() {
        return goggles;
    }

    public void setGoggles(Item goggles) {
        this.goggles = goggles;
    }

    public Item getHelmet() {
        return helmet;
    }

    public void setHelmet(Item helmet) {
        this.helmet = helmet;
    }

    public Item getNecklace() {
        return necklace;
    }

    public void setNecklace(Item necklace) {
        this.necklace = necklace;
    }

    public Item getTrinket() {
        return trinket;
    }

    public void setTrinket(Item trinket) {
        this.trinket = trinket;
    }

    public Item getArmor() {
        return armor;
    }

    public void setArmor(Item armor) {
        this.armor = armor;
    }

    public Item getCloak() {
        return cloak;
    }

    public void setCloak(Item cloak) {
        this.cloak = cloak;
    }

    public Item getBracers() {
        return bracers;
    }

    public void setBracers(Item bracers) {
        this.bracers = bracers;
    }

    public Item getBelt() {
        return belt;
    }

    public void setBelt(Item belt) {
        this.belt = belt;
    }

    public Item getRing1() {
        return ring1;
    }

    public void setRing1(Item ring1) {
        this.ring1 = ring1;
    }

    public Item getRing2() {
        return ring2;
    }

    public void setRing2(Item ring2) {
        this.ring2 = ring2;
    }

    public Item getBoots() {
        return boots;
    }

    public void setBoots(Item boots) {
        this.boots = boots;
    }

    public Item getGloves() {
        return gloves;
    }

    public void setGloves(Item gloves) {
        this.gloves = gloves;
    }

    public Item getMainHand() {
        return mainHand;
    }

    public void setMainHand(Item mainHand) {
        this.mainHand = mainHand;
    }

    public Item getOffHand() {
        return offHand;
    }

    public void setOffHand(Item offHand) {
        this.offHand = offHand;
    }

    public List<String> getFilter() {
        return filter;
    }

    public void setFilter(List<String> filter) {
        this.filter = filter;
    }

    public List<Item.ItemSlot> getSlotItems() {
        return Arrays.asList(
                new Item.ItemSlot(goggles, "Goggles"),
                new Item.ItemSlot(helmet, "Helmet"),
                new Item.ItemSlot(necklace, "Necklace"),
                new Item.ItemSlot(trinket, "Trinket"),
                new Item.ItemSlot(armor, "Armor"),
                new Item.ItemSlot(cloak, "Cloak"),
                new Item.ItemSlot(bracers, "Bracers"),
                new Item.ItemSlot(belt, "Belt"),
                new Item.ItemSlot(ring1, "Ring 1"),
                new Item.ItemSlot(ring2, "Ring 2"),
                new Item.ItemSlot(gloves, "Gloves"),
                new Item.ItemSlot(boots, "Boots"),
                new Item.ItemSlot(mainHand, "Main Hand"),
                new Item.ItemSlot(offHand, "Off Hand"));
    }

    /**
     * Returns all items stored in the gearset
     *
     * @return All items in the gearset
     */
    public List<Item> getItems() {
        return Arrays.asList(goggles, helmet, necklace, trinket, armor, cloak, bracers, belt, ring1, ring2, boots, gloves, mainHand, offHand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goggles, helmet, necklace, trinket, armor, cloak, bracers, belt, ring1, ring2, boots, gloves, mainHand, offHand);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gear gear = (Gear) o;
        return Objects.equals(goggles, gear.goggles) &&
                Objects.equals(helmet, gear.helmet) &&
                Objects.equals(necklace, gear.necklace) &&
                Objects.equals(trinket, gear.trinket) &&
                Objects.equals(armor, gear.armor) &&
                Objects.equals(cloak, gear.cloak) &&
                Objects.equals(bracers, gear.bracers) &&
                Objects.equals(belt, gear.belt) &&
                Objects.equals(ring1, gear.ring1) &&
                Objects.equals(ring2, gear.ring2) &&
                Objects.equals(boots, gear.boots) &&
                Objects.equals(gloves, gear.gloves) &&
                Objects.equals(mainHand, gear.mainHand) &&
                Objects.equals(offHand, gear.offHand);
    }

    @Override
    public String toString() {
        return "Gear{" +
                "goggles=" + goggles +
                ", helmet=" + helmet +
                ", necklace=" + necklace +
                ", trinket=" + trinket +
                ", armor=" + armor +
                ", cloak=" + cloak +
                ", bracers=" + bracers +
                ", belt=" + belt +
                ", ring1=" + ring1 +
                ", ring2=" + ring2 +
                ", boots=" + boots +
                ", gloves=" + gloves +
                ", main hand=" + mainHand +
                ", off hand=" + offHand +
                '}';
    }
}
