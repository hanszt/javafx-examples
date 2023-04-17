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
import org.fxyz.shapes.composites.Histogram;
import org.fxyz.utils.CameraUtils;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author Sean
 */
public class HistogramTest extends Application {

    private double scenex;
    private double sceney;
    private double scenez;
    private double fixedXAngle;
    private double fixedYAngle;
    private double fixedZAngle;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty angleZ = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) throws Exception {
        var sceneRoot = new Group();
        double sceneWidth = 600;
        double sceneHeight = 600;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        PerspectiveCamera camera = buildPerspectiveCamera();
        scene.setCamera(camera);

        Histogram histogram = new Histogram(true);
        sceneRoot.getChildren().addAll(histogram);

        float[][] arrayY = calculateHistogramData();
        histogram.setHeightData(arrayY, 1, 4, Color.SKYBLUE, false, true);
        scene.setOnKeyPressed(event -> CameraUtils.moveInXZPlane(camera, event));

        addMouseRotationAndZoomHandler(scene, histogram);
        primaryStage.setTitle("F(X)yz HistogramTest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addMouseRotationAndZoomHandler(Scene scene, Histogram histogram) {
        var xRotate = new Rotate(0, Rotate.X_AXIS);
        var yRotate = new Rotate(0, Rotate.Y_AXIS);
        var zRotate = new Rotate(0, Rotate.Z_AXIS);

        histogram.getTransforms().addAll(xRotate, yRotate);

        bindAngles(xRotate, yRotate, zRotate);
        scene.setOnMousePressed(this::updateRotationAndZoomParams);
        scene.setOnMouseDragged(this::rotateOrZoom);
    }

    private void bindAngles(Rotate xRotate, Rotate yRotate, Rotate zRotate) {
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        zRotate.angleProperty().bind(angleZ);
    }

    private float[][] calculateHistogramData() {
        int size = 30;
        float[][] arrayY = new float[2 * size][2 * size];
        for (int i = -size; i < size; i++) {
            for (int j = -size; j < size; j++) {
                //Transcedental Gradient
                final double cos = Math.cos(Math.PI * i / size);
                double xterm = (cos * cos);
                final double cos1 = Math.cos(Math.PI * j / size);
                double yterm = (cos1 * cos1);
                arrayY[i + size][j + size] = (float) (10 * ((xterm + yterm) * (xterm + yterm)));
            }
        }
        return arrayY;
    }

    @NotNull
    private static PerspectiveCamera buildPerspectiveCamera() {
        var camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);
        return camera;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void updateRotationAndZoomParams(MouseEvent event) {
        scenex = event.getSceneX();
        sceney = event.getSceneY();
        fixedXAngle = angleX.get();
        fixedYAngle = angleY.get();
        if (event.isMiddleButtonDown()) {
            scenez = event.getSceneX();
            fixedZAngle = angleZ.get();
        }

    }

    private void rotateOrZoom(MouseEvent event) {
        if (event.isMiddleButtonDown()) {
            angleZ.set(fixedZAngle - (scenez - event.getSceneY()));
        } else {
            angleX.set(fixedXAngle - (scenex - event.getSceneY()));
        }
        angleY.set(fixedYAngle + sceney - event.getSceneX());
    }
}
