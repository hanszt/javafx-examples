package hzt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static utils.FxUtils.forNewValue;

public class ColorableLineChart extends Application {

    private static final Random RANDOM = new Random();

    @Override
    public void start(Stage primaryStage) {
        LineChart<Number, Number> chart = new LineChart<>(new NumberAxis(), new NumberAxis());

        Series<Number, Number> series1 = new Series<>();
        series1.setName("Data set 1");

        Series<Number, Number> series2 = new Series<>();
        series2.setName("Data set 2");

        chart.getData().add(series1);
        chart.getData().add(series2);

        series1.getData().addAll(createRandomDataSet(20));
        series2.getData().addAll(createRandomDataSet(25));

        ColorPicker picker1 = new ColorPicker();
        ColorPicker picker2 = new ColorPicker();

        picker1.valueProperty().addListener(forNewValue(newColor -> setColor(series1, newColor)));
        picker2.valueProperty().addListener(forNewValue(newColor -> setColor(series2, newColor)));

        Platform.runLater(() -> {
            picker1.setValue(Color.RED);
            picker2.setValue(Color.BLUE);
        });

        BorderPane root = new BorderPane(chart);
        HBox controls = new HBox(5, picker1, picker2);
        controls.setPadding(new Insets(5));

        root.setTop(controls);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static List<Data<Number, Number>> createRandomDataSet(int endInclusive) {
        return IntStream.rangeClosed(0, endInclusive)
                .<Data<Number, Number>>mapToObj(i -> new Data<>(i, RANDOM.nextInt(100)))
                .toList();
    }

    private static <X, Y> void setColor(Series<X, Y> series, Color color) {
        final var chart = series.getChart();
        final var seriesIndex = chart.getData().indexOf(series);

        chart.lookupAll(".series" + seriesIndex)
                .forEach(s -> s.setStyle("-fx-background-color: " + format(color) + ", white;"));

        series.getNode().setStyle("-fx-stroke: " + format(color) + ";");
    }

    private static String format(Color c) {
        int r = (int) (255 * c.getRed()) ;
        int g = (int) (255 * c.getGreen()) ;
        int b = (int) (255 * c.getBlue()) ;

        return String.format("#%02x%02x%02x", r, g, b);
    }

}
