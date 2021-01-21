package hzt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.stage.Stage;

public class Fx2D9DrawingCubicCurves extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Create the CubicCurves
//        CubicCurve(double startX, double startY, double controlX1, double controlY1, double controlX2, double controlY2, double endX, double endY)
        CubicCurve cubicCurve1 = new CubicCurve(0, 50, 20, 0, 50, 80, 50, 0);
        cubicCurve1.setFill(Color.TRANSPARENT);
        cubicCurve1.setStroke(Color.BLACK);
        CubicCurve cubicCurve2 = new CubicCurve(0, 50, 20, 0, 50, 80, 50, 0);
        cubicCurve2.setFill(Color.LIGHTGRAY);

        // Create the HBox
        HBox root = new HBox();
        // Add the Children to the HBox
        root.getChildren().addAll(cubicCurve1, cubicCurve2);
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
        stage.setTitle("A JavaFX CubicCurve Example");
        // Display the Stage
        stage.show();
    }
}
