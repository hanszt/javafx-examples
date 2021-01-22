package hzt;

import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class BubbleChartSample2 extends XYChartSample {

    @Override
    public void start(Stage stage) {
        super.start(stage, "Bubble Chart Sample");
    }

    @Override
    protected void setStyle(Node root) {
        root.setStyle("-fx-base: rgb(40, 40, 40);-fx-background: rgb(60, 60, 60);");
    }

    @Override
    protected XYChart<Number, Number> getChart(NumberAxis xAxis, NumberAxis yAxis) {
        return new BubbleChart<>(xAxis, yAxis);
    }
}