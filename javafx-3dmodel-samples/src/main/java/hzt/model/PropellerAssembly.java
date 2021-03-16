package hzt.model;

import com.interactivemesh.jfx.importer.x3d.X3dModelImporter;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;

public class PropellerAssembly extends Model3D {

    public PropellerAssembly() {
        loadModel(getClass().getResource("PropellerAssembly-x3d/PropellerAssembly.x3d"));
        animateWheels();
    }

    private void loadModel(URL url) {
        X3dModelImporter importer = new X3dModelImporter();
        importer.read(url);

        for (Node node : importer.getImport()) {
            getChildren().add(node);
            System.out.println(node.getId());
        }
    }

    private void animateWheels() {
        getChildren()
                .stream()
                /*.filter(this::isPropeller)*/
                .forEach(this::animatePropeller);
    }

    public boolean isPropeller(Node view) {
        return view.getId().equals("Propeller");
    }

    public void animatePropeller(Node node) {
        RotateTransition rt = new RotateTransition(Duration.seconds(2), node);
        rt.setCycleCount(Integer.MAX_VALUE);
        rt.setAxis(Rotate.Z_AXIS);
        rt.setByAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }

}
