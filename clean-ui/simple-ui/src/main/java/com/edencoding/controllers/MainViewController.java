package com.edencoding.controllers;

import com.edencoding.models.ToastContainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private StackPane contentContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToastContainer toastContainer = new ToastContainer();
        Button button = new Button("Toast test");
        button.setOnAction(e -> toastContainer.displayToast("Toast"));
        VBox vBox = new VBox(button, toastContainer);
        contentContainer.getChildren().add(vBox);
    }

    @FXML
    private void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    @FXML
    private void handleGitButtonClicked(ActionEvent event) {
        new Application() {
            @Override
            public void start(Stage stage) {
                //to go to browser
            }
        }.getHostServices().showDocument("https://github.com/edencoding/javafx-ui/");
        event.consume();
    }

}
