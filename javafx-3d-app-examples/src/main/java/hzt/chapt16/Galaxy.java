package hzt.chapt16;

import hzt.Application3D;
import hzt.JavaFxResourceUtils;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;

/**
 * @author afsal villan
 * @version 1.0
 * <p>
 * http://www.genuinecoder.com
 */
public class Galaxy extends Application {

    private final Application3D application3D = new Application3D();
    private final Sphere sphere = new Sphere(150);

    @Override
    public void start(Stage primaryStage) {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-1000);

       Group world = new Group();
        world.getChildren().add(prepareEarth());

        Group root = new Group();
        root.getChildren().add(world);
        root.getChildren().add(JavaFxResourceUtils.toConfiguredImageView("/galaxy/galaxy.jpg"));

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        application3D.initMouseControl(world, scene);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation();
    }

    private void prepareAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.rotateProperty().set(sphere.getRotate() + 0.2);
            }
        };
        timer.start();
    }

    private Node prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        JavaFxResourceUtils.setEarthMaterialMaps(earthMaterial);
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }
}
