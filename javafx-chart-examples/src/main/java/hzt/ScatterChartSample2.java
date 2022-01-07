package hzt;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class ScatterChartSample2 extends XYChartSample {

    @Override
    public void start(@NotNull Stage stage) {
        super.start(stage, "Scatter Chart Sample");
    }

    @Override
    protected void setStyle(Node root) {
        root.setStyle("");
    }

    @Override
    protected @NotNull XYChart<Number, Number> getChart(@NotNull NumberAxis xAxis, @NotNull NumberAxis yAxis) {
        return new ScatterChart<>(xAxis, yAxis);
    }
}
