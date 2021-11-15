package hzt.togglebuttonsample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ToggleButtonSample extends Application {

    private static final Label label = new Label("Priority:");

    private final Rectangle rectangle = new Rectangle(145, 50);

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Toggle Button Sample");
        stage.setWidth(250);
        stage.setHeight(180);

        rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.DARKGRAY);
        rectangle.setStrokeWidth(2);

        final ToggleGroup group = new ToggleGroup();

        group.selectedToggleProperty().addListener((ov, toggle, newToggle) -> setColor(group, newToggle));

        ToggleButton tb1 = new ToggleButton("Minor");
        tb1.setToggleGroup(group);
        tb1.setUserData(Color.LIGHTGREEN);
        tb1.setSelected(true);
        tb1.setStyle("-fx-base: lightgreen;");

        ToggleButton tb2 = new ToggleButton("Major");
        tb2.setToggleGroup(group);
        tb2.setUserData(Color.LIGHTBLUE);
        tb2.setStyle("-fx-base: lightblue;");

        ToggleButton tb3 = new ToggleButton("Critical");
        tb3.setToggleGroup(group);
        tb3.setUserData(Color.SALMON);
        tb3.setStyle("-fx-base: salmon;");

        HBox hbox = new HBox();

        hbox.getChildren().add(tb1);
        hbox.getChildren().add(tb2);
        hbox.getChildren().add(tb3);

        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);

        VBox vbox = new VBox();

        vbox.getChildren().add(label);
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(rectangle);
        vbox.setPadding(new Insets(20, 10, 10, 20));

        ((Group) scene.getRoot()).getChildren().add(vbox);
        stage.setScene(scene);
        stage.show();
    }

    private void setColor(ToggleGroup group, javafx.scene.control.Toggle newToggle) {
        rectangle.setFill(newToggle == null ? Color.WHITE : (Color) group.getSelectedToggle().getUserData());
    }
}
