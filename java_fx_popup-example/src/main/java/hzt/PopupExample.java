package hzt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopupExample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        final var popup = new Popup();
        popup.setX(300);
        popup.setY(200);
        popup.getContent().add(new Circle(25, 25, 50, Color.AQUAMARINE));

        var showButton = new Button("Show");
        showButton.setOnAction(event -> popup.show(primaryStage));

        var hideButton = new Button("Hide");
        hideButton.setOnAction(event -> popup.hide());

        var layout = new HBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().addAll(showButton, hideButton);

        primaryStage.setTitle("Popup Example");
        primaryStage.setScene(new Scene(layout));
        primaryStage.show();
    }
}
