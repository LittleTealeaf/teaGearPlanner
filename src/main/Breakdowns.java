package main;

import classes.Effect;
import classes.Gear;
import classes.Item;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

/**
 * Calculations and displaying of the effect breakdowns 
 * @author Tealeaf
 * @version 1.0.2
 * @since 1.0.0
 */
public class Breakdowns {

    public static final GridPane gridBreakdown = new GridPane();
    public static List<Breakdown> breakdowns;
    private static int row;

    /**
     * Updates all the breakdowns from a given gearset
     * <p>Finalized Breakdowns can be accessed via {@link #breakdowns}</p>
     * @since 1.0.0
     */
    public static void updateBreakdowns() {
        breakdowns = new ArrayList<>();

        //Gets all the items
        for (Item i : Main.gear.getItems()) {
            //Gets all the effects
            for (Effect ep : i.getLinkableEffects()) {
                //Splits all effects using extraEffects()
                for (Effect e : extractEffects(ep)) {

                    //Actual comparison
                    e.setCompareStatus(0);
                    boolean attributeStored = false;
                    for (Breakdown breakdown : breakdowns) {
                        if (breakdown.attribute.toLowerCase().contentEquals(e.getName().toLowerCase())) {
                            breakdown.addEffect(e);
                            attributeStored = true;
                        }
                    }

                    if (!attributeStored) {
                        breakdowns.add(new Breakdown(e));
                    }
                }
            }
        }

        updateGridPane();
    }

    /**
     * Updates the breakdown grid-pane
     *     *
     * @since 1.0.0
     */
    public static void updateGridPane() {
        gridBreakdown.getChildren().clear();
        gridBreakdown.setHgap(5);
        gridBreakdown.setVgap(5);


        Button bFilter = new Button("Filter");
        bFilter.setOnAction(e -> filterWindow());

        gridBreakdown.add(bFilter, 0, 0);

        row = 0;
        if (Main.gear.getFilter().size() > 0) {
            for (String s : Main.gear.getFilter()) {
                boolean found = false;
                for (Breakdown b : breakdowns) {
                    if (b.getAttribute().toLowerCase().contentEquals(s.toLowerCase())) {
                        addRow(b);
                        found = true;
                    }
                }

                if (!found) {
                    row++;
                    gridBreakdown.add(new Text("0 " + s), 0, row);
                }

            }
        } else {
            for (Breakdown b : breakdowns) {
                addRow(b);
            }
        }

        gridBreakdown.setStyle("-fx-grid-lines-visible: true");
    }

    /**
     * Adds a row given a set breakdown to the grid
     *
     * @param b Breakdown to occupy the row
     */
    private static void addRow(Breakdown b) {
        row++;

        //Get Maximum and list of used attributes
        int total = 0;
        List<String> types = new ArrayList<>();
        List<Effect> used = new ArrayList<>();
        for (Effect e : b.getEffects()) {
            if (e.getCompareStatus() == 0 || (e.getCompareStatus() == 1 && !types.contains(e.getType().toLowerCase()))) {
                used.add(e);
                types.add(e.getType().toLowerCase());
                total += e.getValue();
            }
        }

        Text name = new Text(total + " " + b.getAttribute());
        gridBreakdown.add(name, 0, row);

        GridPane bonuses = new GridPane();
        bonuses.setHgap(5);

        int bRow = 0;

        for (String bonus : new String[]{"equipment", "enhancement", "competence", "", "insightful", "quality", "profane", "exceptional", "alchemical"}) {
            if (types.remove(bonus)) {
                for (Effect e : used) {
                    if (e.getType().toLowerCase().contentEquals(bonus)) {
                        bonuses.addRow(bRow, new Text(e.getType()), new Text(e.getValue() + ""), new Text(e.getSource().getName()));
                    }
                }
                bRow++;
            }
        }
        for (String type : types) {
            for (Effect e : used) {
                if (e.getType().toLowerCase().contentEquals(type.toLowerCase())) {
                    bonuses.addRow(bRow, new Text(e.getType()), new Text(e.getValue() + ""), new Text(e.getSource().getName()));
                }
            }
            bRow++;
        }

        gridBreakdown.add(bonuses, 1, row);
    }

    /**
     * Extracts effects from initial effects
     * <p>For example, since the {@code Parrying} effect description states that it grants {@code Insightful Armor
     * Class} and {@code Insightful Saves},
     * any effect labeled as {@code Parrying} will return as a {@code Insightful Saves} and {@code Insightful Armor
     * Class} effect</p>
     *
     * @param effect Effect to extract other efects from
     *
     * @return List of all effects extracted from the given effect. If the effect has no set extractable effects,
     * returns a list with just the given effect
     *
     * @since 1.0.0
     */
    public static List<Effect> extractEffects(Effect effect) {


        List<Effect> r = new ArrayList<>();
        r.add(effect);

        boolean hasItself = false;

        for (Extractable e : Settings.extractables) {
            if (e.name.toLowerCase().contentEquals(effect.getName().toLowerCase())) {
                for (Extractable.Pair p : e.replace) {
                    if (p.getName().toLowerCase().contentEquals(effect.getName().toLowerCase())) {
                        hasItself = true;
                    } else {
                        r.add(new Effect(p.getName(), p.getType(effect.getType()), effect.getValue(), effect.getSource()));
                    }
                }
            }
        }

        try {
            if (!hasItself && r.size() > 1 && Main.gear.getFilter().size() == 0) r.remove(0);
        } catch (Exception ignored) {}

        return r;
    }

    /**
     * Opens up a new filtered window
     *
     * @since 1.0.1
     */
    public static void filterWindow() {
        Stage stage = new Stage();
        stage.setTitle("Filters");

        stage.setOnCloseRequest(e -> Main.populateItemGrid());

        TextArea textField = new TextArea();

        StringBuilder text = new StringBuilder();
        for (String str : Main.gear.getFilter()) {
            text.append("\n").append(str);
        }
        try {
            textField.setText(text.substring(1));
        } catch (Exception ignored) {}

        textField.textProperty().addListener((e, o, n) -> Main.gear.setFilter(Arrays.asList(n.split("\n"))));

        Scene scene = new Scene(textField);
        scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.show();

    }

    /**
     * A Breakdown, including the attribute name and all effects
     *
     * @author Tealeaf
     * @since 1.0.0
     */
    public static class Breakdown {
        private final String attribute;
        private final List<Effect> effects;

        public Breakdown(String attribute, List<Effect> effects) {
            this.attribute = attribute;
            this.effects = effects;
            updateEffects();
        }

        public Breakdown(Effect startEffect) {
            this.attribute = startEffect.getName();
            this.effects = new ArrayList<>();
            effects.add(startEffect);
        }

        public String getAttribute() {
            return attribute;
        }

        public List<Effect> getEffects() {
            return effects;
        }

        /**
         * Adds an effect to the effects list, then invokes {@link #updateEffects()} to update each effect's {@code
         * compareStatus}
         *
         * @param effect Specific effect to add to the breakdown
         *
         * @since 1.0.0
         */
        public void addEffect(Effect effect) {
            effects.add(effect);
            updateEffects();
        }

        /**
         * Updates the Breakdown Effects with their comparative statuses
         *
         * @since 1.0.0
         */
        public void updateEffects() {
            for (int i = 0; i < effects.size(); i++) {
                Effect e = effects.get(i);
                for (int j = i + 1; j < effects.size(); j++) {
                    Effect f = effects.get(j);

                    if (e.getType().toLowerCase().contentEquals(f.getType().toLowerCase())) {
                        if (e.getCompareStatus() == 0 && e.getValue() > f.getValue()) { // a is used and b is less, set B to 2
                            f.setCompareStatus(2);
                        } else if (e.getCompareStatus() <= 1 && e.getValue() == f.getValue()) { // a is either used or equal, and B is equal to A, then set both to 1
                            e.setCompareStatus(1);
                            f.setCompareStatus(1);
                        } else { // b > a set A to 2
                            e.setCompareStatus(2);
                        }
                    }


                }
            }
        }
    }

    /**
     * An extractable is an object that specifies a given effect name, and what that effect name instead boosts. Used
     * alongside {@link #extractEffects(Effect)}
     *
     * @author Tealeaf
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Extractable {
        private String name = "";

        private List<Pair> replace = new ArrayList<>(Collections.singletonList(new Pair()));

        /**
         * Creates an empty Extractable
         *
         * @since 1.0.0
         */
        public Extractable() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Pair> getReplace() {
            return replace;
        }

        public void setReplace(List<Pair> replace) {
            this.replace = replace;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, replace);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Extractable that = (Extractable) o;
            return Objects.equals(name, that.name) &&
                    Objects.equals(replace, that.replace);
        }

        @Override
        public String toString() {
            return "Extractable{" +
                    "name='" + name + '\'' +
                    ", replace=" + replace +
                    '}';
        }

        /**
         * Represents an effect, and any type changes, that the {@link Extractable}'s effect name is split into
         * <p><i>Don't ask me what I called it a pair, I honestly have no clue, and don't feel like changing the name
         * <br>Honestly none of the naming scheme here makes any sense to me</i></p>
         *
         * @author Tealeaf
         * @version 1.0.0
         * @since 1.0.0
         */
        public static class Pair {
            private String name = "";
            private String typeReplace = "";
            private String typeAdd = "";

            /**
             * Creates an empty pair
             *
             * @since 1.0.0
             */
            public Pair() {}

            /**
             * Creates a pair with set parameters
             *
             * @param name        Name of the final effect
             * @param typeReplace Type to replace the initial effect type with
             * @param typeAdd     Text to add to the end of the final effect type
             *
             * @since 1.0.0
             */
            public Pair(String name, String typeReplace, String typeAdd) {
                this.name = name;
                this.typeReplace = typeReplace;
                this.typeAdd = typeAdd;
            }

            /**
             * Returns the final type of the pair
             *
             * @param type Initial type of the initial effect
             *
             * @return Modified type of the final effect
             *
             * @since 1.0.0
             */
            public String getType(String type) {
                return (typeReplace.contentEquals("")) ? type + getTypeAddSpaces() : typeReplace + getTypeAddSpaces();
            }

            /**
             * Adds a space in the beginning if the typeAdd has a value, otherwise returns an empty string
             *
             * @return Type Add value
             *
             * @since 1.0.0
             */
            public String getTypeAddSpaces() {
                return (typeAdd.contentEquals("")) ? "" : " " + typeAdd;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTypeReplace() {
                return typeReplace;
            }

            public void setTypeReplace(String typeReplace) {
                this.typeReplace = typeReplace;
            }

            public String getTypeAdd() {
                return typeAdd;
            }

            public void setTypeAdd(String typeAdd) {
                this.typeAdd = typeAdd;
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, typeReplace, typeAdd);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Pair pair = (Pair) o;
                return Objects.equals(name, pair.name) &&
                        Objects.equals(typeReplace, pair.typeReplace) &&
                        Objects.equals(typeAdd, pair.typeAdd);
            }

            @Override
            public String toString() {
                return "Pair{" +
                        "name='" + name + '\'' +
                        ", typeReplace='" + typeReplace + '\'' +
                        ", typeAdd='" + typeAdd + '\'' +
                        '}';
            }
        }
    }

}
