package hzt.fxwebbrowser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class FXWebBrowser extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        final var url = Optional.ofNullable(getClass().getResource("/fxwebbrowser/fxWebBrowser.fxml"))
                .orElseThrow();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setTitle("FX Web browser");
        stage.setScene(scene);
        stage.show();
    }
}
