package hzt;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

public class Fx3DNr4MovingCamera extends Application {

    @Override
    public void start(Stage stage) {
        Box box = buildBox();
        PerspectiveCamera camera = createCamera();
        addRotationAnimationToCamera(camera);
        PointLight redLight = buildPointLight(Color.RED, -100, 250);
        PointLight greenLight = buildPointLight(Color.GREEN, 300, 300);
        Scene scene = buildScene(box, camera, redLight, greenLight);
        stage.setScene(scene);
        stage.setTitle("An Example with a Camera");
        stage.show();
    }

    @NotNull
    private Scene buildScene(Box box, PerspectiveCamera camera, PointLight redLight, PointLight greenLight) {
        Group root = new Group(box, redLight, greenLight);
        root.setRotationAxis(Rotate.X_AXIS);
        root.setRotate(30);
        Scene scene = new Scene(root, 300, 400, true);
        scene.setCamera(camera);
        return scene;
    }

    @NotNull
    private PointLight buildPointLight(Color color, int translateY, int translateZ) {
        PointLight pointLight = new PointLight();
        pointLight.setColor(color);
        pointLight.setTranslateX(250);
        pointLight.setTranslateY(translateY);
        pointLight.setTranslateZ(translateZ);
        return pointLight;
    }

    private void addRotationAnimationToCamera(PerspectiveCamera camera) {
        RotateTransition rotation = new RotateTransition(Duration.seconds(2), camera);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.setFromAngle(0);
        rotation.setToAngle(90);
        rotation.setAutoReverse(true);
        rotation.setAxis(Rotate.Y_AXIS);
        rotation.play();
    }

    @NotNull
    private PerspectiveCamera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(100);
        camera.setTranslateY(-50);
        camera.setTranslateZ(300);
        return camera;
    }

    @NotNull
    private Box buildBox() {
        Box box = new Box(100, 100, 100);
        box.setCullFace(CullFace.NONE);
        box.setTranslateX(250);
        box.setTranslateY(100);
        box.setTranslateZ(400);
        return box;
    }

}
