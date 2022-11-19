package com.asgteach;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public final class WordishApp {

    private final Scene scene;

    public WordishApp(Stage stage) throws IOException {
        this.scene = new Scene(loadFXML("wordish"), 414, 736);
        stage.setScene(scene);
        stage.show();
    }

    void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WordishApp.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory(this::getController);
        return fxmlLoader.load();
    }

    private <C> C getController(Class<C> c) {
        try {
            return c.getConstructor(WordishApp.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
