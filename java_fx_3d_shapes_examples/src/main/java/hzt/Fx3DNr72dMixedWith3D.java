package hzt;

import hzt.service.MouseControlService;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Fx3DNr72dMixedWith3D extends Application {
    @Override
    public void start(Stage primaryStage) {
        //Add 2D content here
        var globalRoot = new AnchorPane();
        globalRoot.getChildren().add(new Label("Hello World"));
        var scene = new Scene(globalRoot, 1024, 768, true);

        var root3D = new Group();
        var sub = new SubScene(root3D,1024,768,false, SceneAntialiasing.BALANCED);
        sub.setCamera(preparedPErspectiveCamera());
        globalRoot.getChildren().add(sub);


        //Add all 3D content to the root3D node
        var sphere = new Sphere(100);
        root3D.getChildren().addAll(sphere);
        new MouseControlService().initMouseControl(sphere, scene);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Camera preparedPErspectiveCamera() {
        var perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setFarClip(10000);
        perspectiveCamera.setNearClip(.1);
        perspectiveCamera.setTranslateZ(-500);
        return perspectiveCamera;
    }
}
