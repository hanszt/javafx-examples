package hzt.radiobuttonsample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RadioButtonSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/radiobuttonsample/";

    final ImageView icon = new ImageView();

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Radio Button Sample");
        stage.setWidth(250);
        stage.setHeight(150);

        final ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Home");
        rb1.setToggleGroup(toggleGroup);
        rb1.setUserData("Home");

        RadioButton rb2 = new RadioButton("Calendar");
        rb2.setToggleGroup(toggleGroup);
        rb2.setUserData("Calendar");

        RadioButton rb3 = new RadioButton("Contacts");
        rb3.setToggleGroup(toggleGroup);
        rb3.setUserData("Contacts");

        toggleGroup.selectedToggleProperty().addListener((ov, oldToggle, newToggle) -> changeImage(toggleGroup));

        HBox hbox = new HBox();
        VBox vbox = new VBox();

        vbox.getChildren().add(rb1);
        vbox.getChildren().add(rb2);
        vbox.getChildren().add(rb3);
        vbox.setSpacing(10);

        hbox.getChildren().add(vbox);
        hbox.getChildren().add(icon);
        hbox.setSpacing(50);
        hbox.setPadding(new Insets(20, 10, 10, 20));

        ((Group) scene.getRoot()).getChildren().add(hbox);
        stage.setScene(scene);
        stage.show();
    }

    private void changeImage(ToggleGroup toggleGroup) {
        if (toggleGroup.getSelectedToggle() != null) {
            final Image image = new Image(getClass().getResourceAsStream(
                    RELATIVE_RESOURCES_DIR + toggleGroup
                            .getSelectedToggle()
                            .getUserData()
                            .toString() + ".jpg"

            ));
            icon.setImage(image);
        }
    }
}