package hzt.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.shape.MeshView;

import java.io.IOException;

public class PyramidFXML extends Model3D {

    public PyramidFXML() {
        load3DModel();
    }

    private void load3DModel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(this.getClass().getResource("pyramid/Pyramid.fxml"));
            MeshView pyramidModel = fxmlLoader.load();
            getChildren().add(pyramidModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
