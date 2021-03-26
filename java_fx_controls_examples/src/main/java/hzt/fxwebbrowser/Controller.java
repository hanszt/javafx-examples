package hzt.fxwebbrowser;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField mainTextField;
    @FXML
    private WebView webView;

    private WebEngine webEngine;

    private WebHistory webHistory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        webHistory = webEngine.getHistory();
        mainTextField.setText("google.com");
        loadPage();
        webEngine.setOnStatusChanged(e ->
                mainTextField.setText(webHistory.getEntries().get(webHistory.getCurrentIndex()).getUrl()));
        webView.zoomProperty().bind(webViewZoom);
    }

    @FXML
    private void reload() {
        webEngine.reload();
    }

    @FXML
    private void loadPage() {
        webEngine.load("http://www." + mainTextField.getText());
    }

    private final DoubleProperty webViewZoom = new SimpleDoubleProperty(1);

    @FXML
    private void zoomIn() {
        webViewZoom.setValue(webViewZoom.multiply(1.25).getValue());
    }

    @FXML
    private void zoomOut() {
        webViewZoom.setValue(webViewZoom.multiply(0.75).getValue());
    }

    @FXML
    private void showHistory() {
        ObservableList<WebHistory.Entry> entries = webHistory.getEntries();
        StringBuilder stringBuilder = new StringBuilder();
        for (WebHistory.Entry entry : entries) {
            stringBuilder.append(String.format("%s %s%n", entry.getUrl(), entry.getLastVisitedDate()));
        }
        System.out.println(stringBuilder);
        PopupController.showPopupMessage(stringBuilder.toString(), webView);
    }

    @FXML
    private void back() {
        ObservableList<WebHistory.Entry> entries = webHistory.getEntries();
        if (!entries.isEmpty()) {
            boolean atOldest = webHistory.getCurrentIndex() == 0;
            if (!atOldest) webHistory.go(-1);
            WebHistory.Entry entry = entries.get(webHistory.getCurrentIndex());
            mainTextField.setText(entry.getUrl());
        }
    }

    @FXML
    private void forward() {
        ObservableList<WebHistory.Entry> entries = webHistory.getEntries();
        if (!entries.isEmpty()) {
            boolean atMostRecent = webHistory.getCurrentIndex() == entries.size() - 1;
            if (!atMostRecent) webHistory.go(1);
            WebHistory.Entry entry = entries.get(webHistory.getCurrentIndex());
            mainTextField.setText(entry.getUrl());
        }
    }

    @FXML
    private void executeJavaScript() {
        webEngine.executeScript("window.location = \"https://www.youtube.com\";");
    }

    private static class PopupController {

        private PopupController() {

        }

        private static Popup createPopup(final String message) {
            final Popup popup = new Popup();
            popup.setAutoFix(true);
            popup.setAutoHide(true);
            popup.setHideOnEscape(true);
            Label label = new Label(message);
            label.setPrefHeight(10);
            label.setOnMouseReleased(e -> popup.hide());
            popup.getContent().add(label);
            return popup;
        }

        private static void showPopupMessage(final String message, final Node node) {
            final Popup popup = createPopup(message);
            popup.setOnShown(e -> setPosition(popup));
            popup.show(node, 100, 100);
            new Timeline(new KeyFrame(Duration.millis(1400), e -> popup.hide())).play();
        }

        private static void setPosition(Popup popup) {
            popup.setX(400);
            popup.setY(600);
        }
    }
}
