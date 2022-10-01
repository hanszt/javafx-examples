package com.gluonhq.samples.sample;

import com.gluonhq.attach.display.DisplayService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tribuo.MutableDataset;
import org.tribuo.classification.LabelFactory;
import org.tribuo.classification.dtree.CARTClassificationTrainer;
import org.tribuo.classification.evaluation.LabelEvaluation;
import org.tribuo.classification.evaluation.LabelEvaluator;
import org.tribuo.common.tree.TreeModel;
import org.tribuo.data.csv.CSVLoader;
import org.tribuo.evaluation.TrainTestSplitter;

import java.io.IOException;
import java.net.URL;

public class Main extends MobileApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    // true positives
    private final Series<String, Number> tpSeries = new Series<>();
    // false positives
    private final Series<String, Number> fpSeries = new Series<>();

    @Override
    public void init() {
        tpSeries.setName("TP");
        fpSeries.setName("FP");
        addViewFactory(HOME_VIEW, this::createView);
    }

    @NotNull
    private View createView() {
        FloatingActionButton fab = new FloatingActionButton(MaterialDesignIcon.SEARCH.text, e -> train());

        Chart chart = createChart();

        Label label = new Label("Hello, Tribuo with Gluon Mobile!");

        VBox root = new VBox(20, label, chart);
        root.setAlignment(Pos.CENTER);

        @SuppressWarnings("squid:S110")
        View view = new View(root) {
            @Override
            protected void updateAppBar(AppBar appBar) {
                appBar.setTitleText("Gluon Mobile");
            }
        };

        fab.showOn(view);

        return view;
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.LIGHT_GREEN.assignTo(scene);
        scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());

        if (Platform.isDesktop()) {
            Dimension2D dimension2D = DisplayService.create()
                    .map(DisplayService::getDefaultDimensions)
                    .orElse(new Dimension2D(640, 480));
            scene.getWindow().setWidth(dimension2D.getWidth());
            scene.getWindow().setHeight(dimension2D.getHeight());
        }
    }

    private void train() {
        tpSeries.getData().clear();
        fpSeries.getData().clear();
        Thread thread = new Thread(this::extracted);
        thread.start();
    }

    private void extracted() {
        final String NAME = "/bezdekIris.data";
        try {
            URL dataUrl = Main.class.getResource(NAME);
            var irisHeaders = new String[]{"sepalLength", "sepalWidth", "petalLength", "petalWidth", "species"};
            final var irisData = new CSVLoader<>(new LabelFactory()).loadDataSource(dataUrl, irisHeaders[4], irisHeaders);
            TrainTestSplitter<org.tribuo.classification.Label> irisSplitter = new TrainTestSplitter<>(irisData, 0.7, 1L);
            MutableDataset<org.tribuo.classification.Label> trainData = new MutableDataset<>(irisSplitter.getTrain());
            MutableDataset<org.tribuo.classification.Label> testData = new MutableDataset<>(irisSplitter.getTest());
            var cartTrainer = new CARTClassificationTrainer();
            TreeModel<org.tribuo.classification.Label> tree = cartTrainer.train(trainData);

            var evaluator = new LabelEvaluator();
            LabelEvaluation evaluation = evaluator.evaluate(tree, testData);

            for (org.tribuo.classification.Label label : trainData.getOutputs()) {
                double fp = evaluation.fp(label);
                double tp = evaluation.tp(label);
                javafx.application.Platform.runLater(() -> addDataPoints(label, fp, tp));
            }
        } catch (IOException e) {
            LOGGER.info("Could not load " + NAME, e);
        }
    }

    private void addDataPoints(org.tribuo.classification.Label label, double fp, double tp) {
        tpSeries.getData().add(new Data<>(label.getLabel(), tp));
        fpSeries.getData().add(new Data<>(label.getLabel(), fp));
    }

    private Chart createChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        StackedBarChart<String, Number> chart = new StackedBarChart<>(xAxis, yAxis);
        ObservableList<XYChart.Series<String, Number>> chartData = FXCollections.observableArrayList();
        chartData.add(tpSeries);
        chartData.add(fpSeries);
        chart.setData(chartData);
        chart.setAnimated(false);
        chart.setTitle("Iris");
        return chart;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
