package classes;

import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import main.Main;

import java.util.Objects;

public class EffectSlider extends Effect {

    private double min = 0;
    private double max = 20;
    private double def = 20;

    public EffectSlider() {
        super();
        setValue(getDef());
    }

    @Override
    public Node getEditNode() {

        TextField textAttribute = new TextField();
        textAttribute.setText(getAttribute());
        textAttribute.textProperty().addListener((e, o, n) -> setAttribute(n));
        textAttribute.setTooltip(new Tooltip("Attribute Name of the Effect"));

        TextField textType = new TextField();
        textType.setText(getType());
        textType.textProperty().addListener((e, o, n) -> setType(n));
        textType.setTooltip(new Tooltip("Bonus Type of the Effect.\nExamples include \"Enhancement\",\"Insightful\",\"Quality\",etc."));


        Spinner<Double> spinnerMin = new Spinner<>();
        spinnerMin.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, getMin()));
        spinnerMin.getValueFactory().valueProperty().addListener((e, o, n) -> setMin(n));
        spinnerMin.setTooltip(new Tooltip("Minimum Value"));
        spinnerMin.setPrefWidth(75);


        Spinner<Double> spinnerMax = new Spinner<>();
        spinnerMax.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, getMax()));
        spinnerMax.getValueFactory().valueProperty().addListener((e, o, n) -> setMax(n));
        spinnerMax.setTooltip(new Tooltip("Minimum Value"));
        spinnerMax.setPrefWidth(75);


        Spinner<Double> spinnerDefault = new Spinner<Double>();
        spinnerDefault.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-1000, 1000, getDef()));
        spinnerDefault.getValueFactory().valueProperty().addListener((e, o, n) -> setDef(n));
        spinnerDefault.setTooltip(new Tooltip("Default Value"));
        spinnerDefault.setPrefWidth(75);

        Main.configSpinner(spinnerMin, spinnerMax, spinnerDefault);

        GridPane grid = new GridPane();
        grid.addRow(0, new Text("Attribute"), new Text("Bonus Type"), new Text("Minimum"), new Text("Maximum"), new Text("Default"));
        grid.addRow(1, textAttribute, textType, spinnerMin, spinnerMax, spinnerDefault);

        return grid;

    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getDef() {
        return def;
    }

    public void setDef(double def) {
        this.def = def;
    }

    @Override
    public Node getDisplayNode() {
        Spinner<Double> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(getMin(), getMax(), getValue()));
        spinner.getValueFactory().valueProperty().addListener((e, o, n) -> setValue(n));
        spinner.setTooltip(new Tooltip("Select a number between " + getMin() + " and " + getMax()));
        spinner.setPrefWidth(75);
        Main.configSpinner(spinner);

        Text text = new Text(getType() + " " + getAttribute());

        HBox hb = new HBox(spinner, text);
        hb.setSpacing(10);

        return hb;
    }


    @Override
    public EffectSlider clone() {
        EffectSlider r = new EffectSlider();
        r.setValue(this.getValue());
        r.setAttribute(this.getAttribute());
        r.setDef(this.def);
        r.setMin(this.min);
        r.setMax(this.max);
        return r;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EffectSlider that = (EffectSlider) o;
        return Double.compare(that.getMin(), min) == 0 &&
                Double.compare(that.getMax(), max) == 0 &&
                Double.compare(that.getDef(), def) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), min, max, def);
    }
}
