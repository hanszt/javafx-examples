package hzt.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

public final class ToroidSliderPane extends Group {

    private final Slider tubeRadXSlider;
    private final Slider tubeRadYSlider;
    private final Slider ringRadiusSlider;
    private final Slider tubeZeroPosSlider;
    private final Slider xOffsetSlider;
    private final Slider yOffsetSlider;
    private final Slider zOffsetSlider;
    private final Slider numberOfRingSegmentsSlider;
    private final Slider numberOfTubeSegmentsSlider;

    public ToroidSliderPane(Toroid toroid) {
        tubeRadXSlider = buildSlider(-10, 20, 10, toroid.tubeRadXProperty());
        tubeRadYSlider = buildSlider(-20, 20, 10, toroid.tubeRadYProperty());
        ringRadiusSlider = buildSlider(-100, 100, 30, toroid.ringRadProperty());
        tubeZeroPosSlider = buildSlider(0, 2 * Math.PI, 0, toroid.tubeZeroPosProperty());
        xOffsetSlider = buildSlider(-1, 1, 0, toroid.xOffsetProperty());
        yOffsetSlider = buildSlider(-1, 2, 1, toroid.yOffsetProperty());
        zOffsetSlider = buildSlider(-1, 1, 0, toroid.zOffsetProperty());
        numberOfTubeSegmentsSlider = buildSlider(32, toroid.nbrTubeSegmentsProperty());
        numberOfRingSegmentsSlider = buildSlider(64, toroid.nbrRingSegmentsProperty());
        var sliderVbox = filledSliderVbox();
        super.getChildren().add(sliderVbox);
        showTickLabelsAndTickMarks(sliderVbox);
    }

    @NotNull
    private VBox filledSliderVbox() {
        return new VBox(
                new Label("Ring radius slider"), ringRadiusSlider,
                new Label("Tube rad x slider"), tubeRadXSlider,
                new Label("Tube rad y slider"), tubeRadYSlider,
                new Label("tube zero pos slider"), tubeZeroPosSlider,
                new Label("x offset slider"), xOffsetSlider,
                new Label("y offset slider"), yOffsetSlider,
                new Label("z offset slider"), zOffsetSlider,
                new Label("number of ring segments slider"), numberOfRingSegmentsSlider,
                new Label("Number of tube segments slider"), numberOfTubeSegmentsSlider
        );
    }

    private static void showTickLabelsAndTickMarks(VBox sliderVbox) {
        sliderVbox.getChildren().stream()
                .filter(Slider.class::isInstance)
                .map(Slider.class::cast)
                .forEach(ToroidSliderPane::showTickMarksAndLabels);
    }

    private static void showTickMarksAndLabels(Slider s) {
        s.setShowTickMarks(true);
        s.setShowTickLabels(true);
    }

    private static Slider buildSlider(double min, double max, double cur, DoubleProperty property) {
        var slider = new Slider(min, max, cur);
        property.bind(slider.valueProperty());
        return slider;
    }

    private static Slider buildSlider(double cur, IntegerProperty property) {
        var slider = new Slider();
        slider.setValue(cur);
        property.bind(slider.valueProperty());
        return slider;
    }

    public void reset() {
        tubeRadXSlider.setValue(10);
        tubeRadYSlider.setValue(10);
        ringRadiusSlider.setValue(30);
        tubeZeroPosSlider.setValue(0);
        xOffsetSlider.setValue(0);
        yOffsetSlider.setValue(1);
        zOffsetSlider.setValue(0);
        numberOfTubeSegmentsSlider.setValue(32);
        numberOfRingSegmentsSlider.setValue(64);
    }

}
