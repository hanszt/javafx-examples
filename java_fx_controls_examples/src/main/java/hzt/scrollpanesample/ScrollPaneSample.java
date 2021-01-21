package hzt.scrollpanesample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScrollPaneSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/scrollsample/";

    final ScrollPane scrollPane = new ScrollPane();

    final String[] imageNames = {"fw1.jpg", "fw2.jpg", "fw3.jpg", "fw4.jpg", "fw5.jpg"};
    final Image[] images = new Image[imageNames.length];
    final ImageView[] pics = new ImageView[imageNames.length];
    final VBox vb = new VBox();
    final Label fileName = new Label();

    @Override
    public void start(Stage stage) {
        VBox box = new VBox();
        Scene scene = new Scene(box, 180, 180);
        stage.setScene(scene);
        stage.setTitle("Scroll Pane");
        box.getChildren().addAll(scrollPane, fileName);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        fileName.setLayoutX(30);
        fileName.setLayoutY(160);

        for (int i = 0; i < 5; i++) {
            images[i] = new Image(getClass().getResourceAsStream(RELATIVE_RESOURCES_DIR + imageNames[i]));
            pics[i] = new ImageView(images[i]);
            pics[i].setFitWidth(100);
            pics[i].setPreserveRatio(true);
            vb.getChildren().add(pics[i]);
        }

        scrollPane.setVmax(440);
        scrollPane.setPrefSize(115, 150);
        scrollPane.setContent(vb);
        scrollPane.vvalueProperty().addListener((ov, oldVal, newVal) ->
                fileName.setText(imageNames[(newVal.intValue() - 1) / 100]));
        stage.show();
    }

}