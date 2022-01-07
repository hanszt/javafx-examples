package hzt;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class LineChartSample2 extends XYChartSample {

    @Override
    public void start(@NotNull Stage stage) {
        super.start(stage, "Line Chart Sample");
    }

    @Override
    protected void setStyle(Node root) {
        root.setStyle("-fx-base: rgb(40, 40, 40);-fx-background: rgb(60, 60, 60);");
    }

    @Override
    protected @NotNull XYChart<Number, Number> getChart(@NotNull NumberAxis xAxis, @NotNull NumberAxis yAxis) {
        return new LineChart<>(xAxis, yAxis);
    }
}
