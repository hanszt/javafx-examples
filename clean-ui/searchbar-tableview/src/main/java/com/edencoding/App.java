package com.edencoding;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public final class App extends Application {

    private double xOffset;
    private double yOffset;

    public App() {
        super();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));

        root.setOnMousePressed(this::setOffsetPoints);
        root.setOnMouseDragged(event -> updateStagePosition(primaryStage, event));

        Scene scene = new Scene(root);
        scene.getRoot().setEffect(new DropShadow(10, Color.rgb(100, 100, 100)));
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/EdenCodingIcon.png")));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateStagePosition(Stage primaryStage, MouseEvent event) {
        primaryStage.setX(event.getScreenX() - xOffset);
        primaryStage.setY(event.getScreenY() - yOffset);
        event.consume();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setOffsetPoints(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
        event.consume();
    }
}
