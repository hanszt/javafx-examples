package hzt.colorpickersample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ColorPickerSample extends Application {

    private static final Logger LOGGER = LogManager.getLogger(ColorPickerSample.class);
    private static final String RELATIVE_RESOURCES_DIR = "/colorpickersample/";

    @Override
    public void start(Stage stage) {
        stage.setTitle("ColorPicker");

        Scene scene = new Scene(new VBox(20), 300, 300);
        scene.setFill(Color.web("#ccffcc"));
        VBox box = (VBox) scene.getRoot();

        ToolBar tb = new ToolBar();
        box.getChildren().add(tb);

        final ComboBox<String> logoSamples = new ComboBox<>();
        logoSamples.getItems().addAll("Oracle", "Java", "JavaFX", "Cup");
        logoSamples.setValue("Oracle");

        final var location = RELATIVE_RESOURCES_DIR + "OracleLogo.png";

        final ImageView logo = Optional.ofNullable(getClass().getResourceAsStream(location))
                .map(Image::new)
                .map(ImageView::new)
                .orElseGet(() -> {
                    logLocationNotFound(location);
                    return new ImageView();
                });

        logoSamples.valueProperty().addListener(((ov, t, name) -> setImageIfPresent(logo, name)));

        final ColorPicker colorPicker = new ColorPicker();
        tb.getItems().addAll(logoSamples, colorPicker);

        StackPane stack = new StackPane();
        box.getChildren().add(stack);

        final SVGPath svg = buildSvgPath();
        svg.setFill(colorPicker.getValue());
        stack.getChildren().addAll(svg, logo);

        colorPicker.setOnAction(event -> svg.setFill(colorPicker.getValue()));

        stage.setScene(scene);
        stage.show();
    }

    private static void setImageIfPresent(ImageView logo, String name) {
        final var location = RELATIVE_RESOURCES_DIR + name + "Logo.png";
        Optional.ofNullable(ColorPickerSample.class.getResourceAsStream(location))
                .map(Image::new)
                .ifPresentOrElse(logo::setImage, () -> logLocationNotFound(location));
    }

    private static void logLocationNotFound(String location) {
        LOGGER.error("{} not found", location);
    }

    @NotNull
    private static SVGPath buildSvgPath() {
        final SVGPath svg = new SVGPath();
        svg.setContent("M70,50 L90,50 L120,90 L150,50 L170,50"
                + "L210,90 L180,120 L170,110 L170,200 L70,200 L70,110 L60,120 L30,90"
                + "L70,50");
        svg.setStroke(Color.DARKGREY);
        svg.setStrokeWidth(2);
        svg.setEffect(new DropShadow());
        return svg;
    }
}
