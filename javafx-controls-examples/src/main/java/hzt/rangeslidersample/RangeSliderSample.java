package hzt.rangeslidersample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.RangeSlider;

/**
 * Source: <a href="https://github.com/controlsfx/controlsfx">org.controlsfx.samples.HelloRangeSlider</a>
 */
public final class RangeSliderSample extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(getPanel()));
        primaryStage.setTitle("Range Slider Sample");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.show();
    }

    public Pane getPanel() {
        final var root = new VBox(15);

        final var horizontalRangeSlider = createHorizontalSlider();
        final var verticalRangeSlider = createVerticalSlider();
        final var labelRangeSlider = createLabelSlider();
        root.getChildren().addAll(horizontalRangeSlider, verticalRangeSlider, labelRangeSlider);

        return root;
    }

    Region createLabelSlider() {
        final var hSlider = new RangeSlider(0, 100, 10, 90);
        hSlider.setShowTickMarks(true);
        hSlider.setShowTickLabels(true);
        hSlider.setLabelFormatter(new StringConverter<>() {

            @Override
            public String toString(final Number object) {
                return switch (object.intValue()) {
                    case 0 -> "very low";
                    case 25 -> "low";
                    case 50 -> "middle";
                    case 75 -> "high";
                    case 100 -> "very high";
                    default -> object.toString();
                };
            }

            @Override
            public Number fromString(final String string) {
                return Double.valueOf(string);
            }
        });
        hSlider.setBlockIncrement(10);
        hSlider.setPrefWidth(300);

        final var box = new HBox(10);
        box.getChildren().addAll(hSlider);
        box.setPadding(new Insets(20, 0, 0, 20));
        box.setFillHeight(false);

        return box;
    }

    Region createHorizontalSlider() {
        final var minField = new TextField();
        minField.setPrefColumnCount(5);
        final var maxField = new TextField();
        maxField.setPrefColumnCount(5);

        final var hSlider = new RangeSlider(0, 100, 10, 90);
        hSlider.setShowTickMarks(true);
        hSlider.setShowTickLabels(true);
        hSlider.setBlockIncrement(10);
        hSlider.setPrefWidth(200);

        configureDisplays(minField, maxField, hSlider);

        final var box = new HBox(10);
        box.getChildren().addAll(minField, hSlider, maxField);
        box.setPadding(new Insets(20, 0, 0, 20));
        box.setFillHeight(false);

        return box;
    }

    private void configureDisplays(final TextField minField, final TextField maxField, final RangeSlider hSlider) {
        minField.setText("" + hSlider.getLowValue());
        maxField.setText("" + hSlider.getHighValue());

        minField.setEditable(false);
        minField.setPromptText("Min");

        maxField.setEditable(false);
        maxField.setPromptText("Max");

        minField.textProperty().bind(hSlider.lowValueProperty().asString("%.2f"));
        maxField.textProperty().bind(hSlider.highValueProperty().asString("%.2f"));
    }


    Region createVerticalSlider() {
        final var minField = new TextField();
        minField.setPrefColumnCount(5);
        final var maxField = new TextField();
        maxField.setPrefColumnCount(5);

        final var vSlider = new RangeSlider(0, 200, 30, 150);
        vSlider.setOrientation(Orientation.VERTICAL);
        vSlider.setPrefHeight(200);
        vSlider.setBlockIncrement(10);
        vSlider.setShowTickMarks(true);
        vSlider.setShowTickLabels(true);

        configureDisplays(minField, maxField, vSlider);

        final var box = new VBox(10);
        box.setPadding(new Insets(0, 0, 0, 20));
        box.setAlignment(Pos.CENTER);
        box.setFillWidth(false);
        box.getChildren().addAll(maxField, vSlider, minField);
        return box;
    }
}
