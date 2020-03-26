package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SetBonus extends Source {

    private List<BonusTier> bonuses = new ArrayList<>();

    public SetBonus() {}

    public SetBonus(String name) {
        super(name);
    }

    public SetBonus(String name, List<BonusTier> bonuses) {
        super(name);
        this.bonuses = bonuses;
    }

    private class BonusTier {
        private int items;
        private List<Effect> effectList = new ArrayList<>();

        public BonusTier() {}

        public BonusTier(int items, List<Effect> effectList) {
            this.items = items;
            this.effectList = effectList;
        }

        public int getItems() {
            return items;
        }

        public void setItems(int items) {
            this.items = items;
        }

        public List<Effect> getEffectList() {
            return effectList;
        }

        public void setEffectList(List<Effect> effectList) {
            this.effectList = effectList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BonusTier bonusTier = (BonusTier) o;
            return items == bonusTier.items &&
                    Objects.equals(effectList, bonusTier.effectList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(items, effectList);
        }

        @Override
        public String toString() {
            return "bonusTier{" +
                    "items=" + items +
                    ", effectList=" + effectList +
                    '}';
        }
    }
}
