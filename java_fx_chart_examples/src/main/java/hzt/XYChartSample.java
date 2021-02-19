package hzt;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import hzt.controls.MultiListView;
import hzt.utils.FxSetup;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class XYChartSample extends Application {

    private final BiMap<TempData, XYChart.Series<Number, Number>> tempDataSeriesBiMap = HashBiMap.create();

    @Override
    public void start(Stage stage) throws Exception {
        start(stage, "Default XY Chart Sample");
    }

    protected void start(Stage stage, String title) {
        final NumberAxis xAxis = new NumberAxis("Days", 1, 31, 1);
        xAxis.setForceZeroInRange(true);

        final XYChart<Number, Number> chart = getChart(xAxis, FxSetup.numberAxis("Temperature (degrees C)"));
        chart.setTitle("Temperature Monitoring (in Degrees C)");
        chart.setHorizontalZeroLineVisible(true);
        chart.setPrefHeight(500);
        chart.setMinHeight(300);
        final MultiListView<TempData> monthsMultiListView = new MultiListView<>(
                "Undisplayed months", "Displayed months");
        Comparator<TempData> comparator = Comparator.comparingInt(t -> t.month.getValue());

        monthsMultiListView.setComparatorListViews(comparator, true);
        monthsMultiListView.addRightListviewListener(change -> updateXYChart(chart, change));
        monthsMultiListView.setSelectionMode(SelectionMode.MULTIPLE);

        addInitData(monthsMultiListView,
                List.of(TempData.MARCH, TempData.APRIL, TempData.MAY));

        final Button updateButton = FxSetup.configuredButton("Update", 100, e -> updateData(chart));
        final VBox vbox = new VBox(chart, FxSetup.configuredHBox(new Insets(10, 10, 10, 50), updateButton, monthsMultiListView));
        setStyle(vbox);
        Scene scene = new Scene(vbox, 1200, 780);
        FxSetup.configureStage(stage, scene, title, 400, 500).show();
    }

    protected void setStyle(Node root) {
        root.setStyle("");
    }

    protected XYChart<Number, Number> getChart(NumberAxis xAxis, NumberAxis yAxis) {
        return new LineChart<>(xAxis, yAxis);
    }

    private void addInitData(MultiListView<TempData> multiListView, List<TempData> initData) {
        for (TempData data : TempData.values()) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(data.formattedName());
            tempDataSeriesBiMap.put(data, series);
            if (initData.contains(data)) multiListView.addRightListView(data);
            else multiListView.addLeftListView(data);
            for (int day = 1; day <= data.month.length(false); day++) {
                if (day % 3 == 1 || day == data.month.maxLength()) {
                    series.getData().add(new XYChart.Data<>(day, data.getRandomTemperature()));
                }
            }
        }
    }

    private void updateXYChart(
            XYChart<Number, Number> areaChart, ListChangeListener.Change<? extends TempData> change) {
        if (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().stream()
                        .map(tempDataSeriesBiMap::get)
                        .filter(s -> !areaChart.getData().contains(s))
                        .forEach(s -> areaChart.getData().add(s));
            }
            if (change.wasRemoved()) {
                List<XYChart.Series<Number, Number>> leftOver = change.getList().stream()
                        .map(tempDataSeriesBiMap::get)
                        .collect(Collectors.toList());
                areaChart.getData().retainAll(leftOver);
            }
        }
    }

    private void updateData(XYChart<Number, Number> areaChart) {
        for (XYChart.Series<Number, Number> series : areaChart.getData()) {
            TempData temperatureData = tempDataSeriesBiMap.inverse().get(series);
            for (XYChart.Data<Number, Number> data : series.getData()) {
                data.setYValue(temperatureData.getRandomTemperature());
            }
        }
    }


    private enum TempData {
        JANUARY(Month.JANUARY, -15, 8),
        FEBRUARY(Month.FEBRUARY, -15, 6),
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

        TempData(Month month, double minTemp, double maxTemp) {
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
}
