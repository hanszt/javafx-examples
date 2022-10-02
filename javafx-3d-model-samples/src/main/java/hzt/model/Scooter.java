package hzt.model;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.net.URL;

public class Scooter extends Model3D {

    public Scooter() {
        loadModel(getClass().getResource("scooter/Scooter-smgrps.obj"));
        super.getChildren()
                .stream()
                .filter(this::isRim)
                .forEach(this::animateRim);
    }

    private void loadModel(URL url) {
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            getChildren().add(view);
        }
    }

    public boolean isRim(Styleable view) {
        return "RimFront".equals(view.getId()) || "RimRear".equals(view.getId());
    }

    public void animateRim(Node node) {
        RotateTransition rt = new RotateTransition(Duration.seconds(0.33), node);
        rt.setCycleCount(Integer.MAX_VALUE);
        rt.setAxis(Rotate.X_AXIS);
        rt.setByAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();
    }

}
