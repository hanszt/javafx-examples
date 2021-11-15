package hzt.chapt13;

import hzt.JavaFxResourceUtils;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;

/**
 * @author afsal villan
 * @version 1.0
 * <p>
 * http://www.genuinecoder.com
 */
public class Earth extends Application {

    @Override
    public void start(Stage primaryStage) {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-1000);

        Group world = new Group();
        world.getChildren().add(preparedEarth());

        Scene scene = new Scene(world, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Sphere preparedEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        JavaFxResourceUtils.acceptImageIfPresent("/earth/earth-d.jpg", earthMaterial::setDiffuseMap);
        Sphere sphere = new Sphere(150);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }
}
