package hzt;

import hzt.utils.FxSetup;
import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class PieChartSample extends Application {

    private static final Random RANDOM = new Random();
    private static final int PERCENT = 100;
    private static final int INIT_AMOUNT = 5;
    private static final int UPPER_BOUND_AMOUNT = 100;

    private static final List<String> POSSIBLE_FRUITS = List.of("Grapefruit", "Oranges", "Plums", "Pears",
                    "Apples", "Bananas", "Kiwis", "Mandarins", "Peaches", "Grapes", "Lemons", "Melons");
    private final Set<String> fruitsDisplayedSet = new HashSet<>();

    @Override
    public void start(Stage stage) {
        final Label captionLabel = new Label("");
        captionLabel.setMouseTransparent(true);

        final PieChart chart = new PieChart();
        chart.setTitle("Imported Fruits");
        chart.setLabelsVisible(true);
        chart.setEffect(FxSetup.preparedShadow(Color.GREY));

        Button addButton = FxSetup.configuredButton("Add Fruit", e -> addRandomUniqueFruitData(chart, captionLabel));
        Button removeButton = FxSetup.configuredButton("Remove fruit", e -> removeRandom(chart));
        ListChangeListener<PieChart.Data> listener = change -> updateState(chart, addButton, removeButton, captionLabel);

        chart.getData().addListener(listener);
        for (int i = 0; i < INIT_AMOUNT; i++) {
            addRandomUniqueFruitData(chart, captionLabel);
        }

        final Pane chartGroup = new Pane(chart, captionLabel);
        final VBox vbox = new VBox(chartGroup, FxSetup.configuredHBox(addButton, removeButton));
        vbox.setStyle("-fx-base: rgb(40, 40, 40);-fx-background: rgb(60, 60, 60);");

        Scene scene = new Scene(vbox);
        stage.setWidth(500);
        stage.setHeight(500);
        FxSetup.configureStage(stage, scene, "Imported Fruits").show();
    }

    private static void updateState(PieChart chart, Button addButton, Button removeButton, Label captionLabel) {
        addButton.setDisable(chart.getData().size() >= POSSIBLE_FRUITS.size());
        removeButton.setDisable(chart.getData().isEmpty());
        captionLabel.setText("");
    }

    private void addRandomUniqueFruitData(PieChart chart, Label captionLabel) {
        String fruit;
        do {
            fruit = POSSIBLE_FRUITS.get(RANDOM.nextInt(POSSIBLE_FRUITS.size()));
        } while (!fruitsDisplayedSet.containsAll(POSSIBLE_FRUITS) && fruitsDisplayedSet.contains(fruit));
        if (!fruitsDisplayedSet.contains(fruit)) {
            fruitsDisplayedSet.add(fruit);
            PieChart.Data data = new PieChart.Data(fruit, RANDOM.nextInt(UPPER_BOUND_AMOUNT));
            chart.getData().add(data);
            data.getNode().addEventHandler(MOUSE_PRESSED, e -> updateCaption(captionLabel, data, e));
        }
    }

    private void removeRandom(PieChart chart) {
        ObservableList<PieChart.Data> list = chart.getData();
        if (!list.isEmpty()) {
            PieChart.Data data = list.remove(RANDOM.nextInt(list.size()));
            fruitsDisplayedSet.remove(data.getName());
        }
    }

    private static void updateCaption(Label captionLabel, PieChart.Data data, MouseEvent e) {
        captionLabel.setTranslateX(e.getSceneX());
        captionLabel.setTranslateY(e.getSceneY());
        PieChart chart = data.getChart();
        double sum = chart.getData().stream()
                .map(PieChart.Data::getPieValue)
                .reduce(0D, Double::sum);

        captionLabel.setText(String.format("%s amount: %.0f%nTotal amount: %.0f%nPercentage: %.2f %%",
                data.getName(), data.getPieValue(), sum, data.getPieValue() * PERCENT / sum));
    }
}
