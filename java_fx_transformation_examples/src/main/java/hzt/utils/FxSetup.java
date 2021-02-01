package hzt.utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

import static java.lang.System.err;

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
        String location = "/icons/fx-icon.png";
        InputStream inputStream = FxSetup.class.getResourceAsStream(location);
        if (inputStream != null) stage.getIcons().add(new Image(inputStream));
        else err.println("Resource at " + location + " not found...");
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
        vBox.setAlignment(Pos.TOP_LEFT);
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

    public static Slider configuredSlider(int minVal, int curVal, int maxVal) {
        Slider slider = new Slider(minVal, curVal, maxVal);
        slider.setShowTickLabels(true);
        slider.setShowTickLabels(true);
        return slider;
    }
}
