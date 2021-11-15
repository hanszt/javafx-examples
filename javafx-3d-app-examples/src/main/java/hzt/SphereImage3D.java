package hzt;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
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
public class SphereImage3D extends Application {

    private final Application3D application3D = new Application3D();

    @Override
    public void start(Stage primaryStage) {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10_000);
        camera.setTranslateZ(-1000);

        Group world = new Group();
        world.getChildren().addAll(prepare3DImageViewSphere());
        ImageView imageView = prepareImageView();
        Group root = new Group(imageView, world);
        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        application3D.initMouseControl(world, scene);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static ImageView prepareImageView() {
         return JavaFxResourceUtils.toConfiguredImageView("/galaxy/galaxy.jpg");
    }

    private static Node prepare3DImageViewSphere() {
        PhongMaterial earthMaterial = new PhongMaterial();
        JavaFxResourceUtils.acceptImageIfPresent("/image/DJI_0450.JPG", earthMaterial::setDiffuseMap);
        Sphere sphere = new Sphere(150);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

}
