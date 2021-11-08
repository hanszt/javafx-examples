package hzt;

import hzt.controls.MultiListView;
import hzt.utils.FxSetup;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static javafx.util.Duration.seconds;

public class BarChartSample extends Application {

    private static final int MAX_VALUE = 1000;

    private static final int DIVIDE_YEAR = 2008;
    private static final SortedSet<Integer> ALL_LISTED_YEARS = new TreeSet<>(
            Set.of(2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, DIVIDE_YEAR, 2009,
                    2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021));

    private static final Set<Integer> INIT_VISIBLE_YEARS = ALL_LISTED_YEARS.headSet(DIVIDE_YEAR);
    private static final Set<Integer> INIT_INVISIBLE_YEARS = ALL_LISTED_YEARS.tailSet(DIVIDE_YEAR);

    private static final String DIVIDE_COUNTRY = "Netherlands";
    private static final SortedSet<String> ALL_LISTED_COUNTRIES = new TreeSet<>(
            Set.of(DIVIDE_COUNTRY, "Austria", "Brazil", "France", "Italy", "USA",
                    "Germany", "Denmark", "Spain", "Portugal", "Norway", "Luxembourg"));

    private static final Set<String> INIT_VISIBLE_COUNTRIES = ALL_LISTED_COUNTRIES.tailSet(DIVIDE_COUNTRY);
    private static final Set<String> INIT_INVISIBLE_COUNTRIES = ALL_LISTED_COUNTRIES.headSet(DIVIDE_COUNTRY);

    private final Map<Integer, XYChart.Series<Number, String>> allYearsToSeriesMap = new HashMap<>();
    private final Map<XYChart.Series<Number, String>, List<XYChart.Data<Number, String>>> seriesToDataPointListMap = new HashMap<>();
    private final Map<XYChart.Data<Number, String>, String> dataPointToCountryMap = new HashMap<>();

    @Override
    public void start(Stage stage) {
        start(stage, "Bar Chart Sample");
    }

    protected void start(Stage stage, String tittle) {
        populateDataMaps();
        final XYChart<Number, String> barChart = buildBarChart();
        animateBarChart(barChart);

        MultiListView<String> countryMultiListView = FxSetup.multiListView("Countries");
        MultiListView<Integer> yearMultiListView = FxSetup.multiListView("Years");

        INIT_VISIBLE_COUNTRIES.forEach(countryMultiListView::addRightListView);
        INIT_INVISIBLE_COUNTRIES.forEach(countryMultiListView::addLeftListView);
        INIT_VISIBLE_YEARS.forEach(year -> fillVisibleYearListView(barChart, yearMultiListView, year));
        INIT_INVISIBLE_YEARS.forEach(yearMultiListView::addLeftListView);

        sortCountries(barChart);

        yearMultiListView.addRightListviewListener(change -> updateByYear(barChart, change));
        countryMultiListView.addRightListviewListener(change -> updateByCountry(barChart, change));

        Scene scene = getScene(barChart, populateControlsPane(countryMultiListView, yearMultiListView));
        setStyle(scene.getRoot());
        FxSetup.configureStage(stage, scene, tittle).show();
    }

    Pane populateControlsPane(Node... controls) {
        return new VBox(controls);
    }

    private XYChart<Number, String> buildBarChart() {
        final NumberAxis xAxis = FxSetup.numberAxis("GDP (billions)");
        xAxis.setTickLabelRotation(90);

        final XYChart<Number, String> barChart = getBarChart(xAxis, FxSetup.categoryAxis("Country"));
        barChart.setTitle("Country Summary");
        return barChart;
    }

    private void animateBarChart(XYChart<Number, String> barChart) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(seconds(1), e -> updateBarChart(barChart)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    private void fillVisibleYearListView(XYChart<Number, String> barChart,
                                         MultiListView<Integer> yearMultiListView, Integer year) {
        yearMultiListView.addRightListView(year);
        XYChart.Series<Number, String> series = allYearsToSeriesMap.get(year);
        barChart.getData().add(series);
    }

    private void populateDataMaps() {
        ALL_LISTED_YEARS.forEach(this::putInSeriesToDataPointsListMap);
    }

    private void putInSeriesToDataPointsListMap(Integer year) {
        XYChart.Series<Number, String> yearSeries = new XYChart.Series<>();
        yearSeries.setName(String.valueOf(year));
        List<XYChart.Data<Number, String>> dataList = ALL_LISTED_COUNTRIES.stream()
                .map(country -> toDataPoint(country, yearSeries))
                .toList();
        seriesToDataPointListMap.put(yearSeries, dataList);
        allYearsToSeriesMap.put(year, yearSeries);
    }

    private XYChart.Data<Number, String> toDataPoint(String country,  XYChart.Series<Number, String> yearSeries) {
        XYChart.Data<Number, String> dataPoint = new XYChart.Data<>(Math.random() * MAX_VALUE, country);
        if (INIT_VISIBLE_COUNTRIES.contains(country)) {
            yearSeries.getData().add(dataPoint);
        }
        dataPointToCountryMap.put(dataPoint, country);
        return dataPoint;
    }

    private void updateByCountry(XYChart<Number, String> barChart, ListChangeListener.Change<? extends String> change) {
        if (change.next()) {
            addOrRemoveCountry(change);
        }
        sortCountries(barChart);
    }

    private static void sortCountries(XYChart<Number, String> barChart) {
        Comparator<XYChart.Data<Number, String>> comparator = Comparator.comparing(XYChart.Data::getYValue);
        barChart.getData().forEach(series -> series.getData().sort(comparator.reversed()));
    }

    private void addOrRemoveCountry(ListChangeListener.Change<? extends String> change) {
        for (XYChart.Series<Number, String> serie : allYearsToSeriesMap.values()) {
            List<XYChart.Data<Number, String>> allCountryDataPoints = seriesToDataPointListMap.get(serie);
            allCountryDataPoints.forEach(dataPoint -> addOrRemoveCountry(change, dataPoint, serie.getData()));
        }
    }

    private void addOrRemoveCountry(ListChangeListener.Change<? extends String> change, XYChart.Data<Number, String> dataPoint,
                                    ObservableList<XYChart.Data<Number, String>> dataPoints) {
        String country = dataPointToCountryMap.get(dataPoint);
        if (change.getAddedSubList().contains(country)) {
            dataPoints.add(dataPoint);
        }
        if (!change.getList().contains(country)) {
            dataPoints.remove(dataPoint);
        }
    }

    private void updateByYear(XYChart<Number, String> barChart, ListChangeListener.Change<? extends Integer> change) {
        if (change.next()) {
            if (change.wasAdded()) {
                addYearsToBarChart(barChart, change.getAddedSubList());
            }
            if (change.wasRemoved()) {
                removeYearFromBarChart(barChart, change);
            }
        }
        barChart.getData().sort(Comparator.comparing(XYChart.Series::getName));
    }

    private void addYearsToBarChart(XYChart<Number, String> barChart, List<? extends Integer> addedYears) {
        barChart.getData().addAll(addedYears.stream()
                .map(this::toUpdatedYearSeries)
                .toList());
    }

    private XYChart.Series<Number,String> toUpdatedYearSeries(Integer year) {
        XYChart.Series<Number, String> yearSeries = allYearsToSeriesMap.get(year);
        yearSeries.getData().forEach(this::newRandomXValue);
        return yearSeries;
    }

    private void removeYearFromBarChart(XYChart<Number, String> barChart, ListChangeListener.Change<? extends Integer> change) {
        List<XYChart.Series<Number, String>> leftOver = change.getList().stream()
                .map(allYearsToSeriesMap::get)
                        .toList();

        barChart.getData().retainAll(leftOver);
    }

    Scene getScene(Node chart, Node... controls) {
        VBox controlsVBox = FxSetup.configuredVBox(controls);
        controlsVBox.setPrefWidth(300);
        return new Scene(FxSetup.configuredHBox(chart, controlsVBox), 1000, 780);
    }

    protected XYChart<Number, String> getBarChart(NumberAxis xAxis, CategoryAxis yAxis) {
        return new BarChart<>(xAxis, yAxis);
    }

    protected void setStyle(Node node) {
        node.setStyle("");
    }

    private void updateBarChart(XYChart<Number, String> barChart) {
        barChart.getData().forEach(series -> series.getData().forEach(this::newRandomXValue));
    }

    void newRandomXValue(XYChart.Data<Number, String> data) {
        data.setXValue(Math.random() * MAX_VALUE);
    }
}
