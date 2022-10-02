package hzt.model;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.net.URL;

public class CylinderHead extends Model3D {

    public CylinderHead() {
        loadModel(getClass().getResource("cylinder-head/CylinderHead-ascii.stl"));
    }

    private void loadModel(URL url) {
        StlMeshImporter importer = new StlMeshImporter();
        importer.read(url);

        TriangleMesh mesh = importer.getImport();
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(Color.SILVER));
        super.getChildren().add(meshView);
    }

}
