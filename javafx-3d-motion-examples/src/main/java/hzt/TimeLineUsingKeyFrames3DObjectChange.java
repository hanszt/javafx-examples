package hzt;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import static java.lang.System.*;

public class TimeLineUsingKeyFrames3DObjectChange extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        Box box = new Box();
        Sphere sphere = new Sphere(10);
        sphere.setMaterial(new PhongMaterial());
        Button button = new Button("Change color");
        button.setOnAction(e -> graduallyChangeColor(sphere, Color.color(Math.random(), Math.random(), Math.random())));
        HBox controlsHBox = new HBox(button);

        Group subSceneRoot = new Group(sphere, box);
        SubScene subScene = new SubScene(subSceneRoot, 400, 400, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(getCamera());
        subScene.setFill(Color.RED);
        VBox root = new VBox(subScene, controlsHBox);
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle(getClass().getName());
        stage.show();
    }

    @NotNull
    private PerspectiveCamera getCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(1000);
        camera.setTranslateZ(-200);
        return camera;
    }

    public void graduallyChangeColor(Shape3D shape3D, Color toColor) {
        double fadeTime = 1000;
        PhongMaterial material = (PhongMaterial) shape3D.getMaterial();
        Timeline displayNode = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(material.diffuseColorProperty(), material.getDiffuseColor())),
                new KeyFrame(Duration.millis(fadeTime), new KeyValue(material.diffuseColorProperty(), toColor)));
        displayNode.playFromStart();
        displayNode.setOnFinished(e -> out.println("finished"));
    }
}
