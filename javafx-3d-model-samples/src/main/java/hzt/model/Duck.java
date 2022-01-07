package hzt.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.shape.MeshView;

import java.io.IOException;

public class Duck extends Model3D {

    public Duck() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass().getResource("Duck-fxml-b94/Duck.fxml"));
            MeshView meshView = fxmlLoader.load();
            super.getChildren().add(meshView);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
