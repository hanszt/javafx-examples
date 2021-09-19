package hzt.chapt12;

import hzt.Application3D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;
import static hzt.JavaFxResourceUtils.woodenBox;

/**
 * @author afsal villan
 * @version 1.0
 * <p>
 * http://www.genuinecoder.com
 */
public class DepthBuffer extends Application {

    private final PointLight pointLight = new PointLight();

    @Override
    public void start(Stage primaryStage) {
        Box box = woodenBox(100, 20, 50);

        Application3D.SmartGroup group = new Application3D.SmartGroup();
        group.getChildren().add(box);
        group.getChildren().add(woodenBox(20, 100, 100));
        group.getChildren().add(new AmbientLight());

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.translateZProperty().set(-200);

        Scene scene = new Scene(group, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(0);
        group.translateYProperty().set(0);
        group.translateZProperty().set(0);

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

}
