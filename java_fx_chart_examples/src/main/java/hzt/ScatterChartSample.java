package hzt;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class ScatterChartSample extends Application {

    private static final int AMOUNT_DATA_POINTS_SERIES = 100;
    private static final int UPPER_BOUND_X = 10;
    private static final int UPPER_BOUND_Y = 500;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Scatter Chart Sample");

        final NumberAxis xAxis = new NumberAxis(0, UPPER_BOUND_X, 1);
        final NumberAxis yAxis = new NumberAxis(-100, UPPER_BOUND_Y, 100);
        xAxis.setLabel("Age (years)");
        yAxis.setLabel("Returns to date");

        final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Investment Overview");

        scatterChart.setPrefSize(500, 400);
        addSeries(scatterChart);
        final VBox vbox = new VBox();
        final HBox hbox = new HBox();

        final Button addButton = new Button("Add Series");
        addButton.setOnAction(e -> addSeries(scatterChart));

        final Button removeFirstButton = new Button("Remove first");
        removeFirstButton.setOnAction(e -> removeFirstSeries(scatterChart));

        final Button removeButton = new Button("Remove random");
        removeButton.setOnAction((ActionEvent e) -> removeRandomSeries(scatterChart));


        final Button removeLastButton = new Button("Remove last");
        removeLastButton.setOnAction((ActionEvent e) -> removeLastSeries(scatterChart));

        final DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setColor(Color.GREY);
        scatterChart.setEffect(shadow);

        hbox.setSpacing(10);
        hbox.getChildren().addAll(addButton, removeFirstButton, removeButton, removeLastButton);

        vbox.getChildren().addAll(scatterChart, hbox);
        hbox.setPadding(new Insets(10, 10, 10, 50));

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void addSeries(ScatterChart<Number, Number> scatterChart) {
        if (scatterChart.getData() == null) {
            scatterChart.setData(FXCollections.observableArrayList());
        }
        Series<Number, Number> series = new Series<>();
        series.setName("Option " + (scatterChart.getData().size() + 1));

        for (int i = 0; i < AMOUNT_DATA_POINTS_SERIES; i++) {
            series.getData().add(new ScatterChart.Data<>(Math.random() * UPPER_BOUND_X, Math.random() * UPPER_BOUND_Y));
        }
        scatterChart.getData().add(series);
    }

    private void removeFirstSeries(ScatterChart<Number, Number> scatterChart) {
        if (!scatterChart.getData().isEmpty()) {
            scatterChart.getData().remove(0);
        }
    }

    private void removeRandomSeries(ScatterChart<Number, Number> scatterChart) {
        if (!scatterChart.getData().isEmpty()) {
            scatterChart.getData().remove(new Random().nextInt(scatterChart.getData().size()));
        }
    }

    private void removeLastSeries(ScatterChart<Number, Number> scatterChart) {
        if (!scatterChart.getData().isEmpty()) {
            scatterChart.getData().remove(scatterChart.getData().size() - 1);
        }

    }

}