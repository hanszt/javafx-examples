package hzt.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;

import java.io.IOException;

public class TuxRotation extends Model3D {

    public TuxRotation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass().getResource("tux-rotation/TuxRotation.fxml"));
            Group group = fxmlLoader.load();
            getChildren().add(group);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
