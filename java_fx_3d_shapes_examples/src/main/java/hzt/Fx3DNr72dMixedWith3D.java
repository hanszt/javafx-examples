package hzt;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Fx3DNr72dMixedWith3D extends Application {
    @Override
    public void start(Stage primaryStage) {
        //Add 2D content here
        AnchorPane globalRoot = new AnchorPane();
        globalRoot.getChildren().add(new Label("Hello World"));
        Scene scene = new Scene(globalRoot, 1024, 768, true);

        Group root3D = new Group();
        SubScene sub = new SubScene(root3D,1024,768,false, SceneAntialiasing.BALANCED);
        sub.setCamera(preparedPErspectiveCamera());
        globalRoot.getChildren().add(sub);


        //Add all 3D content to the root3D node
        Sphere sphere = new Sphere(100);
        root3D.getChildren().addAll(sphere);
        new MouseControlService(sphere).initMouseControl(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Camera preparedPErspectiveCamera() {
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setFarClip(10000);
        perspectiveCamera.setNearClip(.1);
        perspectiveCamera.setTranslateZ(-500);
        return perspectiveCamera;
    }
}
