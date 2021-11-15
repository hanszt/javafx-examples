package hzt.chapt6;

import hzt.Application3D;
import hzt.JavaFxResourceUtils;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
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
public class Texture extends Application {

    @Override
    public void start(Stage primaryStage) {
        Box box = JavaFxResourceUtils.woodenBox(100, 20, 50);

        Group group = new Group();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-500);
        camera.setFarClip(10_000);
        camera.setNearClip(.1);
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        new Application3D().initMouseControl(group, scene);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
