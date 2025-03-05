package hzt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class ColorSample extends Application {

    @Override
    public void start(Stage primaryStage) {
        var content = new BorderPane();
        content.setPadding(new Insets(20));
        var button = new Button("Hello BootstrapFX");
        button.getStyleClass().setAll("btn","btn-danger");
        button.setTextFill(Color.hsb());
        content.setCenter(button);

        var scene = new Scene(content);

        primaryStage.setTitle("BootstrapFX");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
