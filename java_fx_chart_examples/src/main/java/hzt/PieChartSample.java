package hzt;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public class PieChartSample extends Application {

    private static final int INIT_AMOUNT = 5;
    private static final int UPPER_BOUND_AMOUNT = 100;

    private static final List<String> POSSIBLE_FRUITS =
            new ArrayList<>(Set.of("Grapefruit", "Oranges", "Plums", "Pears",
                    "Apples", "Bananas", "Kiwis", "Mandarins", "Peaches", "Grapes", "Lemons", "Melons"));

    @Override
    public void start(Stage stage) {
        stage.setTitle("Imported Fruits");
        stage.setWidth(500);
        stage.setHeight(500);

        final Label captionLabel = new Label("");
        captionLabel.setMouseTransparent(true);
        final PieChart chart = new PieChart();
        for (int i = 0; i < INIT_AMOUNT; i++) {
            addRandomUniqueFruitData(chart, captionLabel);
        }
        chart.setTitle("Imported Fruits");
        chart.setLabelsVisible(true);

        final Button addButton = new Button("Add Fruit");
        addButton.setOnAction(e -> addRandomUniqueFruitData(chart, captionLabel));

        final Button removeButton = new Button("Remove fruit");
        removeButton.setOnAction((ActionEvent e) -> removeRandom(chart));

        final DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setColor(Color.GREY);
        chart.setEffect(shadow);

        ListChangeListener<PieChart.Data> listener = change -> updateState(chart, addButton, removeButton, captionLabel);
        chart.getData().addListener(listener);

        final HBox buttonHBox = new HBox(addButton, removeButton);
        buttonHBox.setSpacing(10);
        buttonHBox.setPadding(new Insets(10, 10, 10, 50));

        final Pane chartGroup = new Pane(chart, captionLabel);
        final VBox vbox = new VBox(chartGroup, buttonHBox);
        vbox.setStyle("-fx-base: rgb(40, 40, 40);-fx-background: rgb(60, 60, 60);");

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void updateState(PieChart chart, Button addButton, Button removeButton, Label captionLabel) {
        addButton.setDisable(chart.getData().size() >= POSSIBLE_FRUITS.size());
        removeButton.setDisable(chart.getData().isEmpty());
        captionLabel.setText("");
    }

    private final Set<String> fruitsDisplayedSet = new HashSet<>();

    private void addRandomUniqueFruitData(PieChart chart, Label captionLabel) {
        String fruit;
        do {
            fruit = POSSIBLE_FRUITS.get(new Random().nextInt(POSSIBLE_FRUITS.size()));
        } while (!fruitsDisplayedSet.containsAll(POSSIBLE_FRUITS) && fruitsDisplayedSet.contains(fruit));
        if (!fruitsDisplayedSet.contains(fruit)) {
            fruitsDisplayedSet.add(fruit);
            PieChart.Data data = new PieChart.Data(fruit, new Random().nextInt(UPPER_BOUND_AMOUNT));
            chart.getData().add(data);
            data.getNode().addEventHandler(MOUSE_PRESSED, e -> updateCaption(captionLabel, data, e));
        }
    }

    private void removeRandom(PieChart chart) {
        ObservableList<PieChart.Data> list = chart.getData();
        if (!list.isEmpty()) {
            PieChart.Data data = list.remove(new Random().nextInt(list.size()));
            fruitsDisplayedSet.remove(data.getName());
        }
    }

    private static final int PERCENT = 100;

    private void updateCaption(Label captionLabel, PieChart.Data data, MouseEvent e) {
        captionLabel.setTranslateX(e.getSceneX());
        captionLabel.setTranslateY(e.getSceneY());
        PieChart chart = data.getChart();
        double sum = chart.getData().stream().map(PieChart.Data::getPieValue).reduce(Double::sum).orElse(0D);
        captionLabel.setText(String.format("%s amount: %.0f%nTotal amount: %.0f%nPercentage: %.2f %%",
                data.getName(), data.getPieValue(), sum, sum != 0 ? (data.getPieValue() * PERCENT / sum) : 0));
    }
}