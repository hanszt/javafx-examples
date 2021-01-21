package hzt.hyperlinksample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HyperlinkSample extends Application {

    private static final String RELATIVE_RESOURCES_DIR = "/hyperlinksample/";

    static final String[] IMAGE_FILES = {
        "product.png",
        "education.png",
        "partners.png",
        "support.png"
    };
    static final String[] CAPTIONS = {
        "Products",
        "Education",
        "Partners",
        "Support"
    };
    final ImageView selectedImage = new ImageView();
    final Hyperlink[] hyperlinks = new Hyperlink[CAPTIONS.length];
    final Image[] images = new Image[IMAGE_FILES.length];

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Hyperlink Sample");
        stage.setWidth(300);
        stage.setHeight(200);

        selectedImage.setLayoutX(100);
        selectedImage.setLayoutY(10);

        for (int i = 0; i < CAPTIONS.length; i++) {
            final Hyperlink hpl = hyperlinks[i] = new Hyperlink(CAPTIONS[i]);
            final Image image = images[i] = 
                new Image(getClass().getResourceAsStream(RELATIVE_RESOURCES_DIR + IMAGE_FILES[i]));
            hpl.setOnAction(e -> selectedImage.setImage(image));
        }

        final Button button = new Button("Refresh links");
        button.setOnAction(this::refreshLinks);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(hyperlinks);
        vbox.getChildren().add(button);
        vbox.setSpacing(5);

        ((Group) scene.getRoot()).getChildren().addAll(vbox, selectedImage);
        stage.setScene(scene);
        stage.show();
    }

    private void refreshLinks(ActionEvent e) {
        for (int i = 0; i < CAPTIONS.length; i++) {
            hyperlinks[i].setVisited(false);
            selectedImage.setImage(null);
        }
    }
}