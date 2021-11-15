package hzt;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StackedAreaChartSample extends XYChartSample {

    @Override
    public void start(Stage stage) {
        super.start(stage, "Stacked Area Chart Sample");
    }

    @Override
    protected void setStyle(Node root) {
        root.setStyle("-fx-base: rgb(60, 40, 40);-fx-background: rgb(100, 60, 60);");
    }

    @Override
    protected XYChart<Number, Number> getChart(NumberAxis xAxis, NumberAxis yAxis) {
        return new StackedAreaChart<>(xAxis, yAxis);
    }
}