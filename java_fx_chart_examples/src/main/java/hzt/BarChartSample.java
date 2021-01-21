package hzt;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static javafx.util.Duration.seconds;

public class BarChartSample extends Application {

    private static final int MAX_X_VALUE = 1000;
    private static final int[] YEARS = {2003, 2004, 2005,2006, 2021};
    private static final SortedSet<String> COUNTRIES = new TreeSet<>(
            Set.of("Austria", "Brazil", "France", "Italy", "USA", "Netherlands"));

    @Override
    public void start(Stage stage) {
        stage.setTitle("Bar Chart Sample");
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        final BarChart<Number, String> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Country Summary");
        xAxis.setLabel("Value");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Country");

        for (int year : YEARS) {
            XYChart.Series<Number, String> series = new XYChart.Series<>();
            series.setName(String.valueOf(year));
            barChart.getData().add(series);
            for (String country : COUNTRIES) {
                series.getData().add(new XYChart.Data<>(Math.random() * MAX_X_VALUE, country));
            }
        }

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(seconds(1), e -> updateBarChart(barChart)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();

        Scene scene = new Scene(barChart, 800, 600);

        stage.setScene(scene);
        stage.show();
    }

    private void updateBarChart(BarChart<Number, String> numberStringBarChart) {
        for (XYChart.Series<Number, String> series : numberStringBarChart.getData()) {
            for (XYChart.Data<Number, String> data : series.getData()) {
                data.setXValue(Math.random() * MAX_X_VALUE);
            }
        }
    }
}
