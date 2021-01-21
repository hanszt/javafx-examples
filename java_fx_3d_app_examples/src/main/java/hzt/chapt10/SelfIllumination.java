package hzt.chapt10;

import hzt.Application3D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;

/**
 * @author afsal villan
 * @version 1.0
 * <p>
 * http://www.genuinecoder.com
 */
public class SelfIllumination extends Application {

    private final PointLight pointLight = new PointLight();

    @Override
    public void start(Stage primaryStage) {
        Box box = prepareBox();

        Application3D.SmartGroup group = new Application3D.SmartGroup();
        group.getChildren().add(box);
        group.getChildren().add(new PointLight());

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-200);

        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        Application3D application3D = new Application3D();
        application3D.initMouseControl(group, scene);
        application3D.initKeyControl(group, primaryStage);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                pointLight.setRotate(pointLight.getRotate() + 1);
            }
        };
        timer.start();
    }

    private Box prepareBox() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("../../wood.jpg")));

        //Set self illumination map
        material.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("../../illuminati2.jpg")));
        Box box = new Box(100, 20, 50);
        box.setMaterial(material);
        return box;
    }

}
