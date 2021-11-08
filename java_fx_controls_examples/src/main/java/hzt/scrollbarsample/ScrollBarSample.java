package hzt.scrollbarsample;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.stream.IntStream;

public class ScrollBarSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/scrollsample/";

    @Override
    public void start(Stage stage) {
        final ScrollBar scrollBar = new ScrollBar();
        final VBox vBox = new VBox();

        Group root = new Group(vBox, scrollBar);
        Scene scene = new Scene(root, 180, 180);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("Scrollbar");

        final DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GREY);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);

        vBox.setLayoutX(5);
        vBox.setSpacing(10);

        scrollBar.setLayoutX(scene.getWidth() - scrollBar.getWidth());
        scrollBar.setMin(0);
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(180);
        scrollBar.setMax(360);

        vBox.getChildren().addAll(IntStream.rangeClosed(1, 5)
                .mapToObj(i -> toImageView(i, shadow))
                .toList());
        scrollBar.valueProperty().addListener((ov, oldVal, newVal) -> vBox.setLayoutY(-newVal.doubleValue()));

        stage.show();
    }

    private ImageView toImageView(int i, DropShadow dropShadow) {
        final var pic = Optional.ofNullable(getClass()
                .getResourceAsStream(RELATIVE_RESOURCES_DIR + "fw" + i + ".jpg"))
                .map(Image::new)
                .map(ImageView::new)
                .orElse(new ImageView());
        pic.setEffect(dropShadow);
        return pic;
    }

}
