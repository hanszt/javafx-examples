package hzt.chapt14;

import hzt.Application3D;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
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
public class DetailedEarth extends Application {

    @Override
    public void start(Stage primaryStage) {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-1000);

       Group world = new Group();
        world.getChildren().add(prepareEarth());

        Scene scene = new Scene(world, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        new Application3D().initMouseControl(world, scene);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node prepareEarth() {
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("../../earth/earth-d.jpg")));
        earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("../../earth/earth-l.jpg")));
        earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("../../earth/earth-s.jpg")));
        earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream("../../earth/earth-n.jpg")));

        Sphere sphere = new Sphere(150);
        sphere.setMaterial(earthMaterial);
        return sphere;
    }

}
