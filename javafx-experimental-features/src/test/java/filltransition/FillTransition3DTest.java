package filltransition;

import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import utils.FxTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class FillTransition3DTest {

    private FillTransition3D fillTransition3D;
    private PhongMaterial material;

    @Start
    void onStartup(Stage stage) {
        final Cylinder cylinder = new Cylinder(40, 90);
        cylinder.setRotationAxis(Rotate.X_AXIS.add(Rotate.Y_AXIS));
        final RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2));
        rotateTransition.setFromAngle(90);
        rotateTransition.setToAngle(270);
        rotateTransition.setNode(cylinder);
        rotateTransition.play();
        material = new PhongMaterial(Color.GREEN);
        cylinder.setMaterial(material);
        final BorderPane parent = new BorderPane();
        parent.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        parent.setCenter(cylinder);
        fillTransition3D = new FillTransition3D(cylinder, Duration.seconds(1));
        stage.setScene(new Scene(parent, 200, 200));
        stage.show();
    }

    @Test
    void testFillTransition3DObject() {
        final Color targetColor = Color.RED;
        fillTransition3D.setFromColor(material.getDiffuseColor());
        fillTransition3D.setToColor(targetColor);

        FxTestUtils.sleep(Duration.millis(500));

        fillTransition3D.play();

        FxTestUtils.sleep(Duration.seconds(1.5));

        final Color diffuseColor = material.getDiffuseColor();

        System.out.println("diffuseColor = " + diffuseColor);

        assertEquals(targetColor, diffuseColor);
    }

}
