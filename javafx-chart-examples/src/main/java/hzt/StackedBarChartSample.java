package hzt;

import hzt.utils.FxSetup;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StackedBarChartSample extends BarChartSample {

    @Override
    public void start(Stage stage) {
        super.start(stage, "Stacked Bar Chart Sample");
    }

    @Override
    protected void setStyle(Node root) {
        root.setStyle("-fx-base: rgb(40, 40, 60);-fx-background: rgb(60, 60, 100);");
    }

    @Override
    protected XYChart<Number, String> getBarChart(NumberAxis xAxis, CategoryAxis yAxis) {
        return new StackedBarChart<>(xAxis, yAxis);
    }

    @Override
    Scene getScene(Node chart, Node... controls) {
        HBox controlsHBox = FxSetup.configuredHBox(controls);
        controlsHBox.setPrefHeight(300);
        return new Scene(FxSetup.configuredVBox(chart, controlsHBox));
    }

    @Override
    Pane populateControlsPane(Node... controls) {
        return new HBox(controls);
    }

}