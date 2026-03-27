package hzt.marbeling;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

/**
 * <a href="https://www.youtube.com/watch?v=p7IGZTjC008">Coding Challenge 183: Paper Marbling Algorithm</a>
 */
public final class Marbling extends Application {

    private final Random random = new Random();

    @Override
    public void start(Stage stage) throws Exception {
        final var pane = new AnchorPane();
        final var scene = new Scene(pane, 600, 800);
        stage.setScene(scene);
        pane.setOnMouseClicked(e -> addDrop(e, pane));
        pane.setOnMouseDragged(e -> {
            if (e.isSecondaryButtonDown()) {
                addDrop(e, pane);
            }
        });
        stage.show();
    }

    private void addDrop(MouseEvent e, AnchorPane group) {
        final var drop = new Drop(new Point2D(e.getX(), e.getY()), random.nextInt(10, 50));
        for (var other : group.getChildren()) {
            ((Drop) other).marble(drop);
        }
        drop.setFill(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), random.nextDouble()));
        group.getChildren().add(drop);
    }
}
