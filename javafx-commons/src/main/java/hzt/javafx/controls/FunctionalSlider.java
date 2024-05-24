package hzt.javafx.controls;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Slider;
import javafx.util.StringConverter;

import java.util.function.DoubleUnaryOperator;

/**
 * <a href="https://stackoverflow.com/questions/18904969/javafx-2-2-configuring-slider-ticks-manually">JavaFX 2.2 configuring Slider ticks manually</a>
 */
public final class FunctionalSlider extends Slider {
    private final DoubleProperty functionValue = new SimpleDoubleProperty();

    public FunctionalSlider(DoubleUnaryOperator function) {
        this("%1$.0f", function);
    }

    public FunctionalSlider(final String labelFormat, DoubleUnaryOperator function) {
        valueProperty().addListener((o, v, n) -> functionValue.set(function.applyAsDouble(n.doubleValue())));

        setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double x) {
                return String.format(labelFormat, function.applyAsDouble(x));
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });
    }

    public double getFunctionValue() {
        return functionValue.get();
    }

    public void setValues(double functionValue, DoubleUnaryOperator reverseFunction) {
        this.functionValue.setValue(functionValue);
        setValue(reverseFunction.applyAsDouble(functionValue));
    }
}
