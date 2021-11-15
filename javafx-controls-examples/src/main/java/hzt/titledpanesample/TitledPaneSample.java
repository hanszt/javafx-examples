package hzt.titledpanesample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Optional;

public class TitledPaneSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/titledpanesample/";

    private final String[] imageNames = {"Apples", "Flowers", "Leaves"};
    private final Label label = new Label("N/A");

    @Override public void start(Stage stage) {
        stage.setTitle("TitledPane");
        Scene scene = new Scene(new Group(), 450, 250);
        scene.setFill(Color.GHOSTWHITE);

        TitledPane gridTitlePane = new TitledPane();
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("To: "), 0, 0);
        grid.add(new TextField(), 1, 0);
        grid.add(new Label("Cc: "), 0, 1);
        grid.add(new TextField(), 1, 1);
        grid.add(new Label("Subject: "), 0, 2);
        grid.add(new TextField(), 1, 2);        
        grid.add(new Label("Attachment: "), 0, 3);
        grid.add(label,1, 3);
        gridTitlePane.setText("Grid");
        gridTitlePane.setContent(grid);

        final Accordion accordion = new Accordion();

        accordion.getPanes().addAll(Arrays.stream(imageNames)
                .map(this::toTitledPane)
                .toList());
        
        accordion.expandedPaneProperty().addListener((ov, oldVal, newVal) -> setText(accordion, newVal));
        
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(20, 0, 0, 20));
        hbox.getChildren().setAll(gridTitlePane, accordion);

        Group root = (Group)scene.getRoot();
        root.getChildren().add(hbox);
        stage.setScene(scene);
        stage.show();
    }

    private TitledPane toTitledPane(String imageName) {
        var pic = Optional.ofNullable(getClass().getResourceAsStream(RELATIVE_RESOURCES_DIR + imageName + ".jpg"))
                .map(Image::new)
                .map(ImageView::new)
                .orElse(new ImageView());
        return new TitledPane(imageName, pic);
    }

    private void setText(Accordion accordion, TitledPane newVal) {
        if (newVal != null) {
            label.setText(accordion.getExpandedPane().getText() + ".jpg");
        }
    }
}
