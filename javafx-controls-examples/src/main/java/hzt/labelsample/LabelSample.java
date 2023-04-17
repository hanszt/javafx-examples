package hzt.labelsample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.InputStream;

import static java.lang.System.err;

public class LabelSample extends Application {

    private final Label label3 = new Label("A label that needs to be wrapped");

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Label Sample");
        stage.setWidth(400);
        stage.setHeight(180);

        HBox hbox = new HBox();
        ImageView imageView = new ImageView();
        InputStream inputStream = getClass().getResourceAsStream("/labelsample/labels.jpg");
        if (inputStream != null) {
            Image image = new Image(inputStream);
            imageView.setImage(image);
        } else {
            err.println("inputstream was null");
        }

        Label label1 = new Label("Search");
        label1.setGraphic(imageView);
        label1.setFont(new Font("Arial", 30));
        label1.setTextFill(Color.web("#0076a3"));
        label1.setTextAlignment(TextAlignment.JUSTIFY);

        Label label2 = new Label("Values");
        label2.setFont(Font.font("Cambria", 32));
        label2.setRotate(270);
        label2.setTranslateY(50);

        label3.setWrapText(true);
        label3.setTranslateY(50);
        label3.setPrefWidth(100);

        label3.setOnMouseEntered(e -> setScale(1.5));

        label3.setOnMouseExited(e -> setScale(1));

        hbox.setSpacing(10);
        hbox.getChildren().add((label1));
        hbox.getChildren().add(label2);
        hbox.getChildren().add(label3);
        ((Group) scene.getRoot()).getChildren().add(hbox);

        stage.setScene(scene);
        stage.show();
    }

    private void setScale(double scale) {
        label3.setScaleX(scale);
        label3.setScaleY(scale);
    }

}

