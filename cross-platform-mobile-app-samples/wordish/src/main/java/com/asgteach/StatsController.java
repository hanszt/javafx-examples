package com.asgteach;

import com.asgteach.model.GameStatus;
import com.asgteach.model.WordStats;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class StatsController {
    private static final GameStatus gameStatus = GameStatus.getInstance();
    @FXML
    private Label statPlayed;
    @FXML
    private Label statPercent;
    @FXML
    private Label statCurrent;
    @FXML
    private Label statMax;

    @FXML
    private StackPane pane;
    @FXML
    private BarChart<Number, String> barChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private CategoryAxis yAxis;
    private final WordStats wordStats = gameStatus.getWordStats();
    private final ChangeListener<Scene> changeListener = (o, oldScene, newScene) -> {
        if (oldScene == null && newScene != null) {
            new Timeline(new KeyFrame(Duration.millis(50), ae -> fixLabels())).play();
        }
    };
    private final WordishApp wordishApp;

    public StatsController(WordishApp wordishApp) {
        this.wordishApp = wordishApp;
    }

    @FXML
    private void switchToWordish() throws IOException {
        pane.sceneProperty().removeListener(changeListener);
        wordishApp.setRoot("wordish");
    }

    public void initialize() {
        barChart.setAnimated(false);
        barChart.setTitle("Guess Distribution");
        barChart.setLegendVisible(false);
        xAxis.setTickLabelsVisible(false);

        statPlayed.setText(String.valueOf(wordStats.getGamesPlayed()));
        statPercent.setText(String.format("%4.0f", wordStats.getWinPercentage()));
        statCurrent.setText(String.valueOf(wordStats.getCurrentStreak()));
        statMax.setText(String.valueOf(wordStats.getMaxStreak()));
        barChart.getData().add(getNextSeries());
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);
        pane.sceneProperty().addListener(changeListener);

    }

    private XYChart.Series<Number, String> getNextSeries() {
        XYChart.Series<Number, String> series = new XYChart.Series<>();
        final var guessDistribution = wordStats.getGuessDistribution();
        guessDistribution.forEach((key, value) -> series.getData().add(0, new Data<>(value, String.valueOf(key))));
        return series;
    }

    public void fixLabels() {
        // this code only works after the scene is completely rendered
        List<Label> mylabels = pane.getChildren().stream()
                .filter(Label.class::isInstance)
                .map(Label.class::cast)
                .toList();
        mylabels.forEach(l -> {
            l.toFront();
            l.getStyleClass().add("chartlabel");
        });
        // find chart area Node
        Node chartArea = barChart.lookup(".chart-plot-background");
        XYChart.Series<Number, String> series = barChart.getData().get(0);

        IntStream.range(0, series.getData().size())
                .forEach(i -> updateBarChart(mylabels, chartArea, series, i));
    }

    private void updateBarChart(List<Label> mylabels, Node chartArea, XYChart.Series<Number, String> series, int i) {
        Data<Number, String> data = series.getData().get(i);
        final var label = mylabels.get(i);
        Node node = data.getNode();
        if (data.getYValue().equals(String.valueOf(wordStats.getThisGameGuesses()))) {
            node.setStyle("-fx-bar-fill: -fx-match-color;");
            label.setStyle("-fx-background-color: -fx-match-color;");
        }
        // Place the label on the bar
        // the stackpane centers the labels first
        Bounds nodeBounds = node.localToScene(chartArea.getBoundsInLocal());
        // Get the display position along xAxis/yAxis for a given X/Yvalue.
        double displayPositionX = xAxis.getDisplayPosition(data.getXValue());
        double displayPositionY = yAxis.getDisplayPosition(data.getYValue());
        label.setText(data.getXValue().toString());
        if (Double.compare(displayPositionX, 0.0) == 0) {
            label.setTranslateX(20 - (nodeBounds.getWidth() / 2));
        } else {
            label.setTranslateX(displayPositionX - 4 - ((nodeBounds.getWidth()) / 2));
        }
        label.setTranslateY(displayPositionY + 21);
        label.setVisible(true);
    }
}
