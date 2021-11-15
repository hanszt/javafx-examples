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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.Optional;

public class App extends Application {

    private static final Logger LOGGER = LogManager.getLogger(App.class);
    private double xOffset;
    private double yOffset;

    public App() {
        super();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainView.fxml")));

        root.setOnMousePressed(this::updateOffset);
        root.setOnMouseDragged(event -> updatePositionOnScreen(primaryStage, event));

        Scene scene = new Scene(root);
        scene.getRoot().setEffect(new DropShadow(10, Color.rgb(100, 100, 100)));
        scene.setFill(Color.TRANSPARENT);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        final var iconLocation = "/img/EdenCodingIcon.png";
        Optional.ofNullable(getClass().getResourceAsStream(iconLocation))
                .map(Image::new)
                .ifPresentOrElse(primaryStage.getIcons()::add,
                        () -> LOGGER.warn(() -> iconLocation + " not found"));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updatePositionOnScreen(Stage primaryStage, MouseEvent event) {
        primaryStage.setX(event.getScreenX() - xOffset);
        primaryStage.setY(event.getScreenY() - yOffset);
        event.consume();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void updateOffset(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
        event.consume();
    }
}
