package hzt.chapt17;

import hzt.Application3D;
import hzt.JavaFxResourceUtils;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.FxUtils;
import org.jetbrains.annotations.VisibleForTesting;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;
import static hzt.JavaFxResourceUtils.configuredCamera;

/**
 * @author afsal villan
 * @version 1.0
 * <p>
 * <a href="http://www.genuinecoder.com">JAVA ARTICLES</a>
 */
public class GalaxyWithSlider extends Application {

    private static final Logger LOGGER = LogManager.getLogger(GalaxyWithSlider.class);
    public static final int EARTH_RADIUS = 150;
    private final Application3D application3D = new Application3D();

    @VisibleForTesting
    Slider slider;
    @VisibleForTesting
    Group world;

    @Override
    public void start(Stage primaryStage) {
        Camera camera = configuredCamera();

        world = new Group();
        final var earth = preparedEarth();
        world.getChildren().add(earth);

        slider = prepareSlider();
        world.translateZProperty().bindBidirectional(slider.valueProperty());

        Group root = new Group();
        root.getChildren().add(world);
        root.getChildren().add(JavaFxResourceUtils.toConfiguredImageView("/galaxy/galaxy.jpg"));
        root.getChildren().add(slider);

        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        application3D.initMouseControl(world, scene);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation(earth);
        LOGGER.info("All descendents from root: {}", FxUtils.allDescendents(root).stream()
                .map(Object::getClass)
                .map(Class::getSimpleName)
                .toList());
    }

    private static void prepareAnimation(Sphere sphere) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sphere.rotateProperty().set(sphere.getRotate() + 0.2);
            }
        };
        timer.start();
    }

    private static Slider prepareSlider() {
        Slider slider = new Slider();
        slider.setMax(800);
        slider.setMin(-400);
        slider.setPrefWidth(300);
        slider.setLayoutX(-150);
        slider.setLayoutY(200);
        slider.setShowTickLabels(true);
        slider.setTranslateZ(5);
        slider.setStyle("-fx-base: black");
        return slider;
    }

    private static Sphere preparedEarth() {
        final Sphere sphere = new Sphere(EARTH_RADIUS);
        PhongMaterial earthMaterial = new PhongMaterial();
        JavaFxResourceUtils.setEarthMaterialMaps(earthMaterial);
        sphere.setRotationAxis(Rotate.Y_AXIS);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

}
