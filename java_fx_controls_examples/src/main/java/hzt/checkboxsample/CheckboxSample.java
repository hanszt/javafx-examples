package hzt.checkboxsample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CheckboxSample extends Application {

    private static final Logger LOGGER = LogManager.getLogger(CheckboxSample.class);
    private static final String RELATIVE_RESOURCES_DIR = "/checkboxsample/";

    private final Rectangle rect = new Rectangle(90, 30);
    private final String[] names = {"Security", "Project", "Chart"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Checkbox Sample");
        stage.setWidth(230);
        stage.setHeight(120);

        rect.setArcHeight(10);
        rect.setArcWidth(10);
        rect.setFill(Color.rgb(41, 41, 41));

        VBox vbox = new VBox();
        HBox hbox = new HBox();
        for (String name : names) {
            final var LOCATION = RELATIVE_RESOURCES_DIR + name + ".png";
            final ImageView icon = Optional.ofNullable(getClass().getResourceAsStream(LOCATION))
                    .map(Image::new)
                    .map(ImageView::new)
                    .orElseGet(() -> logAndEmptyImageView(LOCATION));
            final CheckBox checkBox = new CheckBox(name);
            icon.visibleProperty().bind(checkBox.selectedProperty());
            vbox.getChildren().add(checkBox);
            hbox.getChildren().add(icon);
        }

        vbox.setSpacing(5);
        hbox.setPadding(new Insets(0, 0, 0, 5));

        StackPane stack = new StackPane();

        stack.getChildren().add(rect);
        stack.getChildren().add(hbox);
        StackPane.setAlignment(rect, Pos.TOP_CENTER);                

        HBox root = new HBox();
        root.getChildren().add(vbox);
        root.getChildren().add(stack);
        root.setSpacing(40);
        root.setPadding(new Insets(20, 10, 10, 20));

        ((Group) scene.getRoot()).getChildren().add(root);

        stage.setScene(scene);
        stage.show();
    }

    private static ImageView logAndEmptyImageView(String string) {
        LOGGER.error("Could not find {}", string);
        return new ImageView();
    }
}
