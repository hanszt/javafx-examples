package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AnchorPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // upper-right sign out control
        Hyperlink signOutLink = getSignOutlink();
        // lower-left status label
        Label statusLabel = getLowerLeftStatusLabel();
        // lower-right connection status control
        HBox connHBox = getConnectionBox();
        // top-left content; takes up extra space
        TextArea textArea = getTextArea();

        Scene scene = new Scene(new AnchorPane(signOutLink, statusLabel, connHBox, textArea));

        primaryStage.setTitle("AnchorPaneApp");
        primaryStage.setScene(scene);
        primaryStage.setWidth(568);
        primaryStage.setHeight(320);
        primaryStage.show();
    }

    private static Hyperlink getSignOutlink() {
        Hyperlink signOutLink = new Hyperlink("Sign Out");
        AnchorPane.setTopAnchor(signOutLink, 10.0D);
        AnchorPane.setRightAnchor(signOutLink, 10.0D);
        return signOutLink;
    }

    private static Label getLowerLeftStatusLabel() {
        Label statusLabel = new Label("Program status");
        AnchorPane.setBottomAnchor(statusLabel, 10.0D);
        AnchorPane.setLeftAnchor(statusLabel, 10.0D);
        return statusLabel;
    }

    private static HBox getConnectionBox() {
        Circle circle = new Circle();
        circle.setFill(Color.GREEN);
        circle.setRadius(10);

        HBox connHBox = new HBox();
        connHBox.setSpacing(4.0D);
        connHBox.setAlignment(Pos.BOTTOM_RIGHT);
        connHBox.getChildren().addAll(new Label("Connection"), circle);

        AnchorPane.setBottomAnchor(connHBox, 10.0D);
        AnchorPane.setRightAnchor(connHBox, 10.0D);
        return connHBox;
    }

    private static TextArea getTextArea() {
        TextArea textArea = new TextArea();
        AnchorPane.setTopAnchor(textArea, 40.0D);
        AnchorPane.setBottomAnchor(textArea, 40.0D);
        AnchorPane.setRightAnchor(textArea, 10.0D);
        AnchorPane.setLeftAnchor(textArea, 10.0D);
        return textArea;
    }
}
