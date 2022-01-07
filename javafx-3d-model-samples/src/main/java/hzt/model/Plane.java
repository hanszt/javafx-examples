package hzt.model;

import com.interactivemesh.jfx.importer.col.ColModelImporter;
import javafx.scene.Node;

import java.net.URL;

public class Plane extends Model3D {

    public Plane() {
        loadModel(getClass().getResource("Seymourplane-141-150-dae/seymourplane-141-im.dae"));
    }

    private void loadModel(URL url) {
        ColModelImporter importer = new ColModelImporter();
        importer.read(url);

        for (Node node : importer.getImport()) {
            getChildren().add(node);
        }
    }

}
