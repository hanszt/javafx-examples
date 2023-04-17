/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.tests;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.fxyz.geometry.Point3D;
import org.fxyz.shapes.composites.PolyLine3D;
import org.fxyz.utils.CameraUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sean
 */
public class PolyLine3DTest extends Application {

    private double scenex;
    private double sceney;
    private double scenez = 0;
    private double fixedXAngle;
    private double fixedYAngle;
    private double fixedZAngle = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty angleZ = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) throws Exception {
        PerspectiveCamera camera = buildPerspectiveCamera();
        var polyLine3D = buildPolyLine3D();

        Group sceneRoot = new Group(polyLine3D);
        double sceneWidth = 600;
        double sceneHeight = 600;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);
        scene.setOnKeyPressed(event -> CameraUtils.moveInXZPlane(camera, event));

        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);

        polyLine3D.getTransforms().addAll(xRotate, yRotate);
        //Use Binding so your rotation doesn't have to be recreated
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        zRotate.angleProperty().bind(angleZ);

        //Start Tracking mouse movements only when a button is pressed
        scene.setOnMousePressed(this::onMousePressed);
        //Angle calculation will only change when the button has been pressed
        scene.setOnMouseDragged(this::onMouseDragged);

        primaryStage.setTitle("F(X)yz PolylineTest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @NotNull
    private PerspectiveCamera buildPerspectiveCamera() {
        var camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);
        return camera;
    }

    @NotNull
    private PolyLine3D buildPolyLine3D() {
        List<Point3D> points = new ArrayList<>();
        for (int i = -250; i < 250; i++) {
            points.add(new Point3D(
                    (float) i,
                    (float) Math.sin(i) * 50 + i,
                    (float) Math.cos(i) * 50 + i));
        }
        return new PolyLine3D(points, 3, Color.STEELBLUE);
    }

    private void onMousePressed(MouseEvent event) {
        scenex = event.getSceneX();
        sceney = event.getSceneY();
        fixedXAngle = angleX.get();
        fixedYAngle = angleY.get();
        if (event.isMiddleButtonDown()) {
            scenez = event.getSceneX();
            fixedZAngle = angleZ.get();
        }

    }

    private void onMouseDragged(MouseEvent event) {
        if (event.isMiddleButtonDown()) {
            angleZ.set(fixedZAngle - (scenez - event.getSceneY()));
        } else {
            angleX.set(fixedXAngle - (scenex - event.getSceneY()));
        }
        angleY.set(fixedYAngle + sceney - event.getSceneX());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
