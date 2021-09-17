package hzt.passwordfieldsample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PasswordFiledSample extends Application {

    private final Label message = new Label("");

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 260, 80);
        stage.setScene(scene);
        stage.setTitle("Password Field Sample");

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.setSpacing(10);
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Password");
        final PasswordField passwordField = new PasswordField();

        passwordField.setOnAction(e -> displayMessage(passwordField));

        hBox.getChildren().addAll(label, passwordField);
        vBox.getChildren().addAll(hBox, message);

        scene.setRoot(vBox);
        stage.show();
    }

    private void displayMessage(PasswordField passwordField) {
        if (!"T2f$Ay!".equals(passwordField.getText())) {
            message.setText("Your password is incorrect!");
            message.setTextFill(Color.rgb(210, 39, 30));
        } else {
            message.setText("Your password has been confirmed");
            message.setTextFill(Color.rgb(21, 117, 84));
        }
        passwordField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
