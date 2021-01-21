package hzt;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class AreaChartSample extends Application {

    private enum MonthlyTemperatureData {
        JANUARY(Month.JANUARY, -15, 8),
        FEBRUARI(Month.FEBRUARY, -15, 6),
        MARCH(Month.MARCH, -2, 15),
        APRIL(Month.APRIL, 3, 18),
        MAY(Month.MAY, 10, 22),
        JUNE(Month.JUNE, 16, 30),
        JULY(Month.JULY, 18, 35),
        AUGUST(Month.AUGUST, 24, 40),
        SEPTEMBER(Month.SEPTEMBER, 16, 30),
        OCTOBER(Month.OCTOBER, 14, 24),
        NOVEMBER(Month.NOVEMBER, -2, 15),
        DECEMBER(Month.DECEMBER, -10, 10);

        private final Month month;
        private final double minTemp;
        private final double maxTemp;

        MonthlyTemperatureData(Month month, double minTemp, double maxTemp) {
            this.month = month;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
        }

        private double getRandomTemperature() {
            double difference = maxTemp - minTemp;
            return minTemp + (difference * Math.random());
        }

        private String formattedName() {
            return month.name().charAt(0) + month.name().substring(1).toLowerCase();
        }

        @Override
        public String toString() {
            return formattedName();
        }
    }

    private final Map<Series<Number, Number>, MonthlyTemperatureData> seriesToTempDataMap = new HashMap<>();
    private final Map<MonthlyTemperatureData, Series<Number, Number>> tempDataToSeriesMap = new EnumMap<>(MonthlyTemperatureData.class);

    @Override
    public void start(Stage stage) {
        stage.setTitle("Area Chart Sample");
        final NumberAxis xAxis = new NumberAxis(1, 31, 1);
        final NumberAxis yAxis = new NumberAxis(-15, 40, 5);
        final AreaChart<Number, Number> areaChart = new AreaChart<>(xAxis, yAxis);

        xAxis.setForceZeroInRange(true);
        xAxis.setLabel("Days");

        yAxis.setLabel("Temperature (degrees C)");

        areaChart.setTitle("Temperature Monitoring (in Degrees C)");

        final MultiListView<MonthlyTemperatureData> monthsMultiListView =
                new MultiListView<>("Undisplayed months", "Displayed months");
        Comparator<MonthlyTemperatureData> comparator = Comparator.comparingInt(t -> t.month.getValue());
        monthsMultiListView.setComparatorListViews(comparator, true);


        monthsMultiListView.addRightListviewListener(change -> updateAreaChart(areaChart, change));
        monthsMultiListView.setSelectionMode(SelectionMode.MULTIPLE);

        addInitData(monthsMultiListView,
                List.of(MonthlyTemperatureData.MARCH, MonthlyTemperatureData.APRIL, MonthlyTemperatureData.MAY));

        final Button updateButton = new Button("Update");
        updateButton.setPrefWidth(100);
        updateButton.setOnAction(e -> updateData(areaChart));

        final HBox controlsHBox = new HBox(updateButton, monthsMultiListView);
        controlsHBox.setSpacing(10);
        controlsHBox.setAlignment(Pos.CENTER);
        controlsHBox.setPadding(new Insets(10, 10, 10, 10));

        final VBox vbox = new VBox(areaChart, controlsHBox);
        vbox.setStyle("-fx-base: rgb(40, 40, 40);-fx-background: rgb(60, 60, 60);");

        Scene scene = new Scene(vbox, 1200, 780);

        stage.setMinWidth(400);
        stage.setMinHeight(500);
        InputStream inputStream = getClass().getResourceAsStream("/icons/fx-icon.png");
        if (inputStream != null) stage.getIcons().add(new Image(inputStream));

        areaChart.setHorizontalZeroLineVisible(true);
        areaChart.setPrefHeight(500);
        areaChart.setMinHeight(300);
        stage.setScene(scene);
        stage.show();
    }

    private void addInitData(MultiListView<MonthlyTemperatureData> multiListView, List<MonthlyTemperatureData> initData) {
        for (MonthlyTemperatureData data : MonthlyTemperatureData.values()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(data.formattedName());
            seriesToTempDataMap.put(series, data);
            tempDataToSeriesMap.put(data, series);
            if (initData.contains(data)) multiListView.addRightListView(data);
            else multiListView.addLeftListView(data);
            for (int day = 1; day <= data.month.length(false); day++) {
                if (day % 3 == 1 || day == data.month.maxLength()) {
                    series.getData().add(new XYChart.Data<>(day, data.getRandomTemperature()));
                }
            }
        }
    }

    private void updateAreaChart(
            AreaChart<Number, Number> areaChart, ListChangeListener.Change<? extends MonthlyTemperatureData> change) {
        if (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().stream()
                        .map(tempDataToSeriesMap::get)
                        .filter(s -> !areaChart.getData().contains(s))
                        .forEach(s -> areaChart.getData().add(s));
            }
            if (change.wasRemoved()) {
                List<Series<Number, Number>> leftOver = change.getList().stream()
                        .map(tempDataToSeriesMap::get)
                        .collect(Collectors.toList());
                        areaChart.getData().retainAll(leftOver);
            }
        }
    }

    private void updateData(AreaChart<Number, Number> areaChart) {
        for (XYChart.Series<Number, Number> series : areaChart.getData()) {
            MonthlyTemperatureData temperatureData = seriesToTempDataMap.get(series);
            for (XYChart.Data<Number, Number> data : series.getData()) {
                data.setYValue(temperatureData.getRandomTemperature());
            }
        }
    }

}