package hzt.chapt8;

import hzt.Application3D;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;

/**
 * @author afsal villan
 * @version 1.0
 * <p>
 * http://www.genuinecoder.com
 */
public class Lighting extends Application {

    @Override
    public void start(Stage primaryStage) {
        Box box = prepareBox();

        Group group = new Group();
        group.getChildren().add(box);
        group.getChildren().addAll(prepareLightSource());

        //1. Ambient Light
        //2. Point Light
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-200);

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        new Application3D().initMouseControl(group, scene);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node[] prepareLightSource() {
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.RED);
        pointLight.getTransforms().add(new Translate(0, -50, 100));

        Sphere sphere = new Sphere(2);
        sphere.getTransforms().setAll(pointLight.getTransforms());
        return new Node[]{pointLight, sphere};
    }

    private Box prepareBox() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("../../wood.jpg")));

        Box box = new Box(100, 20, 50);
        box.setMaterial(material);
        return box;
    }

}
