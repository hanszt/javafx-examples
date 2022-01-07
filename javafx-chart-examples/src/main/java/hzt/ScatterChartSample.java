package hzt;

import hzt.utils.FxSetup;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class ScatterChartSample extends Application {

    private static final Random RANDOM = new Random();
    private static final int AMOUNT_DATA_POINTS_SERIES = 100;
    private static final int UPPER_BOUND_X = 10;
    private static final int UPPER_BOUND_Y = 500;

    @Override
    public void start(Stage stage) {
        final NumberAxis xAxis = new NumberAxis("Age (years)",0, UPPER_BOUND_X, 1);
        final NumberAxis yAxis = new NumberAxis("Returns to date",-100, UPPER_BOUND_Y, 100);

        final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Investment Overview");
        scatterChart.setPrefSize(500, 400);
        scatterChart.setEffect(FxSetup.preparedShadow(Color.GREY));
        addSeries(scatterChart);

        final Button addButton = FxSetup.configuredButton("Add Series", e -> addSeries(scatterChart));
        final Button removeFirstButton = FxSetup.configuredButton("Remove first", e -> removeFirstSeries(scatterChart));
        final Button removeButton = FxSetup.configuredButton("Remove random", e -> removeRandomSeries(scatterChart));
        final Button removeLastButton = FxSetup.configuredButton("Remove last", e -> removeLastSeries(scatterChart));
        final VBox vbox = new VBox(scatterChart, FxSetup.configuredHBox(addButton, removeFirstButton, removeButton, removeLastButton));
        FxSetup.configureStage(stage, new Scene(vbox), "Scatter Chart Sample").show();
    }

    private static void addSeries(ScatterChart<Number, Number> scatterChart) {
        if (scatterChart.getData() == null) {
            scatterChart.setData(FXCollections.observableArrayList());
        }
        Series<Number, Number> series = FxSetup.series("Option " + (scatterChart.getData().size() + 1));
        for (int i = 0; i < AMOUNT_DATA_POINTS_SERIES; i++) {
            series.getData().add(new ScatterChart.Data<>(Math.random() * UPPER_BOUND_X, Math.random() * UPPER_BOUND_Y));
        }
        scatterChart.getData().add(series);
    }

    private static void removeFirstSeries(ScatterChart<Number, Number> scatterChart) {
        if (!scatterChart.getData().isEmpty()) {
            scatterChart.getData().remove(0);
        }
    }

    private static void removeRandomSeries(ScatterChart<Number, Number> scatterChart) {
        if (!scatterChart.getData().isEmpty()) {
            scatterChart.getData().remove(RANDOM.nextInt(scatterChart.getData().size()));
        }
    }

    private static void removeLastSeries(ScatterChart<Number, Number> scatterChart) {
        if (!scatterChart.getData().isEmpty()) {
            scatterChart.getData().remove(scatterChart.getData().size() - 1);
        }

    }

}
