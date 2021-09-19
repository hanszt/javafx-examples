package hzt.chapt15;

import hzt.Application3D;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;
import static hzt.JavaFxResourceUtils.setEarthMaterialMaps;

/**
 * @author afsal villan
 * @version 1.0
 * <p>
 * http://www.genuinecoder.com
 */
public class SpinningEarth extends Application {

    private static final Logger LOGGER = LogManager.getLogger(SpinningEarth.class);
    private static final int EARTH_RADIUS = 150;

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Starting {}", getClass().getSimpleName());
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10_000);
        camera.translateZProperty().set(-1000);

        Group world = new Group();
        final var earth = preparedEarth();
        world.getChildren().add(earth);

        Scene scene = new Scene(world, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        new Application3D().initMouseControl(world, scene);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation(earth);
        LOGGER.info("{} started", getClass().getSimpleName());
    }

    private static void prepareAnimation(Node earth) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                earth.rotateProperty().set(earth.getRotate() + 0.2);
            }
        };
        timer.start();
    }

    private static Node preparedEarth() {
        Sphere sphere = new Sphere(EARTH_RADIUS);
        PhongMaterial earthMaterial = new PhongMaterial();
        setEarthMaterialMaps(earthMaterial);
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

}
