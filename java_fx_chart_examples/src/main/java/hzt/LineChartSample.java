package hzt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Random;

import static hzt.utils.FxSetup.*;
import static javafx.scene.paint.Color.GREY;

public class LineChartSample extends Application {

    private static final int MAX = 60;
    private static final String[] MONTHS =
            {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    @Override
    public void start(Stage stage) {
        final LineChart<String, Number> lineChart = new LineChart<>(categoryAxis("Month"), numberAxis("Amount"));

        lineChart.setTitle("Stock Monitoring, " + LocalDate.now().getYear());
        lineChart.setCreateSymbols(false);
        lineChart.setAlternativeRowFillVisible(false);
        //noinspection unchecked
        lineChart.getData().addAll(series("Portfolio 1"), series("Portfolio 2"), series("Portfolio 3"));
        lineChart.getData().forEach(LineChartSample::getRandomData);
        lineChart.setEffect(preparedShadow(GREY));

        final Button updateButton = configuredButton("Update", e -> update(lineChart));
        Scene scene = new Scene(new VBox(lineChart, configuredHBox(updateButton)));
        configureStage(stage, scene, "Line Chart Sample").show();
    }

    private static void update(LineChart<String, Number> lineChart) {
        for (XYChart.Series<String, Number> series : lineChart.getData()) {
            getRandomData(series);
        }
    }

    private static void getRandomData(XYChart.Series<String, Number> series) {
        series.getData().clear();
        for (String month : MONTHS) {
            series.getData().add(new XYChart.Data<>(month, new Random().nextInt(MAX)));
        }
    }

}
