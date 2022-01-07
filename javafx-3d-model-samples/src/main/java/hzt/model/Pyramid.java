package hzt.model;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Pyramid extends Model3D {

    private final TriangleMesh mesh = new TriangleMesh();

    public Pyramid() {
        buildMesh();
        getChildren().add(new MeshView(mesh));
    }

    private void buildMesh() {
        mesh.getPoints().addAll(0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F,
                -1.0F, -1.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F);
        mesh.getTexCoords().addAll(0.0F, 0.0F);
        mesh.getFaces().addAll(0, 0, 4, 0, 1, 0, 1, 0, 4, 0, 2, 0, 2, 0, 4, 0, 3, 0, 3, 0, 4,
                0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 2, 0, 3, 0);
        mesh.getFaceSmoothingGroups().addAll(1, 2, 4, 8, 16, 16);
    }
}
