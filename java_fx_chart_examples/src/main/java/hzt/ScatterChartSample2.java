package hzt;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class ScatterChartSample2 extends XYChartSample {

    @Override
    public void start(Stage stage) {
        super.start(stage, "Scatter Chart Sample");
    }

    @Override
    protected void setStyle(Node root) {
        root.setStyle("");
    }

    @Override
    protected XYChart<Number, Number> getChart(NumberAxis xAxis, NumberAxis yAxis) {
        return new ScatterChart<>(xAxis, yAxis);
    }
}
