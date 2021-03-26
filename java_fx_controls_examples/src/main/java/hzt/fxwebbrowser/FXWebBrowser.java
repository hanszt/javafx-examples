package hzt.fxwebbrowser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXWebBrowser extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxwebbrowser/fxWebBrowser.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("FX Web browser");
        stage.setScene(scene);
        stage.show();
    }
}
