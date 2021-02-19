package hzt.model;

import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;
import javafx.scene.Node;

import java.net.URL;

public class PropellerAssembly extends Model3D {

    public PropellerAssembly() {
        loadModel(getClass().getResource("PropellerAssembly-x3d/PropellerAssembly.x3d"));
    }

    private void loadModel(URL url) {
        X3dModelImporter importer = new X3dModelImporter();
        importer.read(url);

        for (Node node : importer.getImport()) {
            getChildren().add(node);
        }
    }

}
