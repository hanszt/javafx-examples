package hzt.chapt15;

import hzt.Application3D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;

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
        acceptImageIfPresent("/earth/earth-d.jpg", earthMaterial::setDiffuseMap);
        acceptImageIfPresent("/earth/earth-l.jpg", earthMaterial::setSelfIlluminationMap);
        acceptImageIfPresent("/earth/earth-s.jpg", earthMaterial::setSpecularMap);
        acceptImageIfPresent("/earth/earth-n.jpg", earthMaterial::setBumpMap);
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

    private static void acceptImageIfPresent(String location, Consumer<Image> imageConsumer) {
        Optional.ofNullable(SpinningEarth.class.getResourceAsStream(location))
                .ifPresentOrElse(stream -> imageConsumer.accept(new Image(stream)),
                        () -> LOGGER.error("Could not find {}", location));
    }

}
