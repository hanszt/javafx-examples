package hzt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

public final class Fx2D10DrawingTriangleSvgPath extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        SVGPath svgPath = new SVGPath();
        String path = "M 100 100 L 300 100 L 200 300 z";
        svgPath.setContent(path);
        // Create the HBox
        HBox root = new HBox();
        // Add the Children to the HBox
        root.getChildren().addAll(svgPath);
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
        stage.setTitle("A JavaFX Svg path Example");
        // Display the Stage
        stage.show();
    }
}
