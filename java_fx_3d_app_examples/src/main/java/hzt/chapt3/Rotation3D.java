package hzt.chapt3;

import hzt.Application3D;
import javafx.application.Application;
import javafx.scene.Camera;
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
public class Rotation3D extends Application {

    @Override
    public void start(Stage primaryStage) {
        Box box = new Box(100, 20, 50);

        Application3D.SmartGroup group = new Application3D.SmartGroup();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);

        group.translateXProperty().set(WIDTH / 2.);
        group.translateYProperty().set(HEIGHT / 2.);
        group.translateZProperty().set(-1200);

        new Application3D().initKeyControl(group, primaryStage);

        primaryStage.setTitle("Genuine Coder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
