package hzt;

import hzt.utils.FxSetup;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class BubbleChartSample extends Application {

    private static final int LOWER_BOUND_X = 1;
    private static final int UPPER_BOUND_X = 53;
    private static final int SCALE_X = 4;
    private static final int MAX_CHANGE_RANGE = 10;

    private static final int LOWER_BOUND_Y = 0;
    private static final int UPPER_BOUND_Y = 80;
    private static final int SCALE_Y = 10;

    @Override
    public void start(Stage stage) {
        final NumberAxis xAxis = new NumberAxis("Week", LOWER_BOUND_X, UPPER_BOUND_X, SCALE_X);
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        final NumberAxis yAxis = new NumberAxis("Product Budget", LOWER_BOUND_Y, UPPER_BOUND_Y, SCALE_Y);
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$ ", null));

        XYChart.Series<Number, Number> series1 = FxSetup.series("Product 1");
        fillSeries1(series1);
        XYChart.Series<Number, Number> series2 = FxSetup.series("Product 2");
        fillSeries2(series2);

        final BubbleChart<Number, Number> bubbleChart = new BubbleChart<>(xAxis, yAxis);
        bubbleChart.setTitle("Budget Monitoring");
        bubbleChart.getData().add(series1);
        bubbleChart.getData().add(series2);

        final Button updateButton = FxSetup.configuredButton("Update", e -> updateData(bubbleChart));
        Scene scene = new Scene(new VBox(bubbleChart, FxSetup.configuredHBox(updateButton)));
        FxSetup.configureStage(stage, scene, "Bubble Chart Sample").show();
    }

    private static void fillSeries1(XYChart.Series<Number, Number> series1) {
        series1.getData().addAll(List.of(
                new XYChart.Data<>(3, 35, 2),
                new XYChart.Data<>(12, 60, 1.8),
                new XYChart.Data<>(15, 15, 7),
                new XYChart.Data<>(22, 30, 2.5),
                new XYChart.Data<>(28, 20, 1),
                new XYChart.Data<>(35, 41, 5.5),
                new XYChart.Data<>(42, 17, 9),
                new XYChart.Data<>(49, 30, 1.8)
        ));
    }

    private static void fillSeries2(XYChart.Series<Number, Number> series2) {
        series2.getData().addAll(List.of(
                new XYChart.Data<>(8, 15, 2),
                new XYChart.Data<>(13, 23, 1),
                new XYChart.Data<>(15, 45, 3),
                new XYChart.Data<>(24, 30, 4.5),
                new XYChart.Data<>(38, 78, 1),
                new XYChart.Data<>(40, 41, 7.5),
                new XYChart.Data<>(45, 57, 2),
                new XYChart.Data<>(47, 23, 3.8)));
    }

    private static void updateData(BubbleChart<Number, Number> bubbleChart) {
        for (XYChart.Series<Number, Number> series : bubbleChart.getData()) {
            for (XYChart.Data<Number, Number> data : series.getData()) {
                data.setXValue(data.getXValue().doubleValue() + MAX_CHANGE_RANGE * Math.random() - (MAX_CHANGE_RANGE / 2.));
                data.setYValue(data.getYValue().doubleValue() + MAX_CHANGE_RANGE * Math.random() - (MAX_CHANGE_RANGE / 2.));
            }
        }
    }

}
