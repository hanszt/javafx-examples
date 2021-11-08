package hzt.slidersample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import static utils.FxUtils.forNewValue;

public class SliderSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/slidersample/";

    final Slider opacityLevel = new Slider(0, 1, 1);    
    final Slider sepiaTone = new Slider(0, 1, 1);
    final Slider scaling = new Slider (0.5, 1, 1);
    final Image image  = new Image(getClass().getResourceAsStream(RELATIVE_RESOURCES_DIR + "cappuccino.jpg"));

    final Label opacityCaption = new Label("Opacity Level:");
    final Label sepiaCaption = new Label("Sepia Tone:");
    final Label scalingCaption = new Label("Scaling Factor:");

    final Label opacityValue = new Label(Double.toString(opacityLevel.getValue()));
    final Label sepiaValue = new Label(Double.toString(sepiaTone.getValue()));
    final Label scalingValue = new Label(Double.toString(scaling.getValue()));

    static final SepiaTone SEPIA_EFFECT = new SepiaTone();

    @Override
    public void start(Stage stage) {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-base: rgb(40, 40, 40);-fx-background: rgb(60, 60, 60);");
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(70);

        final ImageView cappuccino = new ImageView (image);
        cappuccino.setEffect(SEPIA_EFFECT);
        GridPane.setConstraints(cappuccino, 0, 0);
        GridPane.setColumnSpan(cappuccino, 3);
        grid.getChildren().add(cappuccino);


        GridPane.setConstraints(opacityCaption, 0, 1);
        grid.getChildren().add(opacityCaption);

        opacityLevel.valueProperty().addListener((ov, oldVal, newVal) -> setOpacity(cappuccino, newVal));

        GridPane.setConstraints(opacityLevel, 1, 1);
        grid.getChildren().add(opacityLevel);

        GridPane.setConstraints(opacityValue, 2, 1);
        grid.getChildren().add(opacityValue);

        GridPane.setConstraints(sepiaCaption, 0, 2);
        grid.getChildren().add(sepiaCaption);

        sepiaTone.valueProperty().addListener(forNewValue(this::setSepiaLevel));
        GridPane.setConstraints(sepiaTone, 1, 2);
        grid.getChildren().add(sepiaTone);

        GridPane.setConstraints(sepiaValue, 2, 2);
        grid.getChildren().add(sepiaValue);

        GridPane.setConstraints(scalingCaption, 0, 3);
        grid.getChildren().add(scalingCaption);

        scaling.valueProperty().addListener((ov, oldVal, newVal) -> setScale(cappuccino, newVal));
        GridPane.setConstraints(scaling, 1, 3);
        grid.getChildren().add(scaling);

        GridPane.setConstraints(scalingValue, 2, 3);
        grid.getChildren().add(scalingValue);

        Scene scene = new Scene(grid, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Slider Sample");
        stage.show();
    }

    private void setScale(ImageView cappuccino, Number newVal) {
        cappuccino.setScaleX(newVal.doubleValue());
        cappuccino.setScaleY(newVal.doubleValue());
        scalingValue.setText(String.format("%.2f", newVal.doubleValue()));
    }

    private void setOpacity(ImageView cappuccino, Number newVal) {
        cappuccino.setOpacity(newVal.doubleValue());
        opacityValue.setText(String.format("%.2f", newVal.doubleValue()));
    }

    private void setSepiaLevel(Number newVal) {
        SEPIA_EFFECT.setLevel(newVal.doubleValue());
        sepiaValue.setText(String.format("%.2f", newVal.doubleValue()));
    }
}
