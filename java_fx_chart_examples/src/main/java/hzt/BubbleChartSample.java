package hzt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BubbleChartSample extends Application {

    private static final int LOWER_BOUND_X = 1;
    private static final int UPPER_BOUND_X = 53;
    private static final int LOWER_BOUND_Y = 0;
    private static final int UPPER_BOUND_Y = 80;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Bubble Chart Sample");
        final NumberAxis xAxis = new NumberAxis(LOWER_BOUND_X, UPPER_BOUND_X, 4);
        final NumberAxis yAxis = new NumberAxis(LOWER_BOUND_Y, UPPER_BOUND_Y, 10);
        final BubbleChart<Number, Number> bubbleChart = new BubbleChart<>(xAxis, yAxis);

        xAxis.setLabel("Week");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("Product Budget");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$ ", null));

        bubbleChart.setTitle("Budget Monitoring");

        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Product 1");
        series1.getData().add(new XYChart.Data<>(3, 35, 2));
        series1.getData().add(new XYChart.Data<>(12, 60, 1.8));
        series1.getData().add(new XYChart.Data<>(15, 15, 7));
        series1.getData().add(new XYChart.Data<>(22, 30, 2.5));
        series1.getData().add(new XYChart.Data<>(28, 20, 1));
        series1.getData().add(new XYChart.Data<>(35, 41, 5.5));
        series1.getData().add(new XYChart.Data<>(42, 17, 9));
        series1.getData().add(new XYChart.Data<>(49, 30, 1.8));

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Product 2");
        series2.getData().add(new XYChart.Data<>(8, 15, 2));
        series2.getData().add(new XYChart.Data<>(13, 23, 1));
        series2.getData().add(new XYChart.Data<>(15, 45, 3));
        series2.getData().add(new XYChart.Data<>(24, 30, 4.5));
        series2.getData().add(new XYChart.Data<>(38, 78, 1));
        series2.getData().add(new XYChart.Data<>(40, 41, 7.5));
        series2.getData().add(new XYChart.Data<>(45, 57, 2));
        series2.getData().add(new XYChart.Data<>(47, 23, 3.8));
        bubbleChart.getData().add(series1);
        bubbleChart.getData().add(series2);

        final Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> updateData(bubbleChart));

        final HBox buttonHBox = new HBox(updateButton);
        buttonHBox.setSpacing(10);
        buttonHBox.setPadding(new Insets(10, 10, 10, 50));

        final VBox vbox = new VBox(bubbleChart, buttonHBox);
        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.show();
    }

    private static final int MAX_CHANGE_RANGE = 10;

    private void updateData(BubbleChart<Number, Number> bubbleChart) {
        for (XYChart.Series<Number, Number> series : bubbleChart.getData()) {
            for (XYChart.Data<Number, Number> data : series.getData()) {
                data.setXValue(data.getXValue().doubleValue() + MAX_CHANGE_RANGE * Math.random() - (MAX_CHANGE_RANGE / 2.));
                data.setYValue(data.getYValue().doubleValue() + MAX_CHANGE_RANGE * Math.random() - (MAX_CHANGE_RANGE / 2.));
            }
        }
    }

}