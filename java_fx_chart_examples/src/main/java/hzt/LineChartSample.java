package hzt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Random;

public class LineChartSample extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Amount");
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, " + LocalDate.now().getYear());
        lineChart.setCreateSymbols(false);
        lineChart.setAlternativeRowFillVisible(false);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Portfolio 1");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Portfolio 2");
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Portfolio 3");

        lineChart.getData().add(series1);
        lineChart.getData().add(series2);
        lineChart.getData().add(series3);
        lineChart.getData().forEach(this::getRandomData);


        final Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> update(lineChart));

        final DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setColor(Color.GREY);
        lineChart.setEffect(shadow);

        final HBox hbox = new HBox(updateButton);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 50));

        final VBox vbox = new VBox(lineChart, hbox);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void update(LineChart<String, Number> lineChart) {
        for (XYChart.Series<String, Number> series : lineChart.getData()) {
            getRandomData(series);
        }
    }

    private static final int MAX = 60;
    private static final String[] MONTHS =
            {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private void getRandomData(XYChart.Series<String, Number> series) {
        series.getData().clear();
        for (String month : MONTHS) series.getData().add(new XYChart.Data<>(month, new Random().nextInt(MAX)));
    }

}