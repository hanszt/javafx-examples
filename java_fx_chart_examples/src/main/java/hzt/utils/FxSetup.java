package hzt.utils;

import hzt.controls.MultiListView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

public class FxSetup {

    private static final int DEFAULT_SPACING = 10;

    private FxSetup() {
    }

    public static Stage configureStage(Stage stage, Scene scene, String title, double minWidth, double minHeight) {
        configureStage(stage, scene, title);
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);
        return stage;
    }

    public static Stage configureStage(Stage stage, Scene scene, String title) {
        stage.setScene(scene);
        stage.setTitle(title);
        InputStream inputStream = FxSetup.class.getResourceAsStream("/icons/fx-icon.png");
        if (inputStream != null) stage.getIcons().add(new Image(inputStream));
        return stage;
    }

    public static HBox configuredHBox(Node... nodes) {
        return configuredHBox(new Insets(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING), nodes);
    }

    public static HBox configuredHBox(Insets insets, Node... nodes) {
        HBox hbox = new HBox(nodes);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(DEFAULT_SPACING);
        hbox.setPadding(insets);
        return hbox;
    }

    public static VBox configuredVBox(Node... nodes) {
        VBox vBox = new VBox(nodes);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(DEFAULT_SPACING);
        vBox.setPadding(new Insets(DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING, DEFAULT_SPACING));
        return vBox;
    }

    public static Effect preparedShadow(Color color) {
        final DropShadow shadow = new DropShadow();
        shadow.setOffsetX(2);
        shadow.setColor(color);
        return shadow;
    }

    public static <X, Y> XYChart.Series<X, Y> series(String name) {
        XYChart.Series<X, Y> series = new XYChart.Series<>();
        series.setName(name);
        return series;
    }

    public static Button configuredButton(String label, EventHandler<ActionEvent> handler) {
        Button button = new Button(label);
        button.setOnAction(handler);
        return button;
    }

    public static Button configuredButton(String label, double prefWidth, EventHandler<ActionEvent> handler) {
        Button button = configuredButton(label, handler);
        button.setPrefWidth(prefWidth);
        return button;
    }

    public static NumberAxis numberAxis(String s) {
        final NumberAxis axis = new NumberAxis();
        axis.setLabel(s);
        return axis;
    }


    public static CategoryAxis categoryAxis(String s) {
        final CategoryAxis axis = new CategoryAxis();
        axis.setLabel(s);
        return axis;
    }


    public static <T> MultiListView<T> multiListView(String title) {
        MultiListView<T> multiListView = new MultiListView<>("Non-visible", "Visible");
        multiListView.setSorting(true);
        multiListView.setSelectionMode(SelectionMode.MULTIPLE);
        multiListView.setMainTitle(title);
        return multiListView;
    }
}
