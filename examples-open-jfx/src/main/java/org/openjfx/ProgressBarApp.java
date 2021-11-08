package org.openjfx;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProgressBarApp extends Application {

    private HBox bottomControls;
    private ProgressBar pb;
    private Label messageLabel;

    private TextField urlTextField;

    private TextArea contents;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent p = createMainView();

        Scene scene = new Scene(p);

        primaryStage.setTitle("ProgressBarApp");
        primaryStage.setWidth(667);
        primaryStage.setHeight(376);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent createMainView() {

        Label label = new Label("URL");
        urlTextField = new TextField();
        HBox.setHgrow(urlTextField, Priority.ALWAYS);
        Button getHtmlButton = new Button("Get HTML");
        getHtmlButton.setOnAction(this::getHTML);

        HBox topControls = new HBox(label, urlTextField, getHtmlButton);
        topControls.setAlignment(Pos.CENTER_LEFT);
        topControls.setSpacing(4);

        contents = new TextArea();
        VBox.setVgrow(contents, Priority.ALWAYS);

        bottomControls = new HBox();
        bottomControls.setVisible(false);
        bottomControls.setSpacing(4);
        HBox.setMargin(bottomControls, new Insets(4));

        pb = new ProgressBar();
        messageLabel = new Label("");
        bottomControls.getChildren().addAll(pb, messageLabel);

        VBox vbox = new VBox(topControls, contents, bottomControls);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        return vbox;
    }

    public void getHTML(ActionEvent evt) {

        String url = urlTextField.getText();

        class HtmlTask extends Task<String> {

            @Override
            protected String call() throws Exception {

                updateMessage("Getting HTML from " + url);
                updateProgress(0.5D, 1.0D);

                String retval = "";
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                try (InputStream is  = httpURLConnection.getInputStream()) {
                    updateProgress(0.6D, 1.0D);
                    int ch;
                    while ((ch = is.read()) != -1) {
                        retval += (char) ch;
                    }
                } finally {
                    httpURLConnection.disconnect();
                }

                updateMessage("HTML retrieved");
                updateProgress(1.0D, 1.0D);
                return retval;
            }

            @Override
            protected void succeeded() {
                contents.setText(getValue());
            }

            @Override
            protected void failed() {
                Alert alert = new Alert(Alert.AlertType.ERROR, getException().getMessage());
                alert.showAndWait();
            }
        }
        Task<String> task = new HtmlTask();

        bottomControls.visibleProperty().bind(task.runningProperty());
        pb.progressProperty().bind(task.progressProperty());
        messageLabel.textProperty().bind(task.messageProperty());

        new Thread(task).start();
    }

}
