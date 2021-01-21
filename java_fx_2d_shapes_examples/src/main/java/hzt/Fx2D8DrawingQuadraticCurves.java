package hzt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.stage.Stage;

public class Fx2D8DrawingQuadraticCurves extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }
    /**
     * Create the QuadCurves: Params:
     * double startX, double startY, double controlX, double controlY, double endX, double endY)
     */
    @Override
    public void start(Stage stage) {
        QuadCurve quadCurve1 = new QuadCurve(0, 100, 20, 0, 150, 100);
        quadCurve1.setFill(Color.TRANSPARENT);
        quadCurve1.setStroke(Color.BLACK);
        QuadCurve quadCurve2 = new QuadCurve(0, 100, 20, 0, 150, 100);
        quadCurve2.setFill(Color.LIGHTGRAY);

        // Create the HBox
        HBox root = new HBox();
        // Add the children to the HBox
        root.getChildren().addAll(quadCurve1, quadCurve2);
        // Set Spacing of the HBox
        root.setSpacing(10);

        // Set the Style of the HBox
        root.setStyle
                (
                        "-fx-padding: 10;" +
                                "-fx-border-style: solid inside;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-insets: 5;" +
                                "-fx-border-radius: 5;" +
                                "-fx-border-color: blue;"
                );

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title of the Stage
        stage.setTitle("A JavaFX QuadCurve Example");
        // Display the Stage
        stage.show();
    }
}
