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
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.fxyz.shapes.composites.SurfacePlot;
import org.jetbrains.annotations.NotNull;

/**
 * @author Sean
 */
public final class SurfacePlotTest extends Application {

    private double scenex;
    private double sceney;
    private double scenez = 0;
    private double fixedXAngle;
    private double fixedYAngle;
    private double fixedZAngle = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty angleZ = new SimpleDoubleProperty(0);

    public SurfacePlotTest() {
        super();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        double sceneWidth = 600;
        double sceneHeight = 600;
        PointLight light = buildPointLight(-sceneHeight + 10, sceneWidth / 2);
        PointLight light2 = buildPointLight(-sceneHeight + 10,-sceneWidth + 10);

        PerspectiveCamera camera = buildCamera();
        SurfacePlot surfacePlot = buildSurfacePlot();
        bindRotationsToSurfacePlot(surfacePlot);

        Group sceneRoot = new Group(surfacePlot, light, light2);
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);
        scene.setOnKeyPressed(e -> keyPressedAction(e, camera));
        //Start Tracking mouse movements only when a button is pressed
        scene.setOnMousePressed(this::mousePressedAction);
        //Angle calculation will only change when the button has been pressed
        scene.setOnMouseDragged(this::mouseDraggedAction);
        primaryStage.setTitle("F(X)yz SurfacePlotTest");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @NotNull
    private static SurfacePlot buildSurfacePlot() {
        int sombreroSize = 10;
        int sombreroHeight = 100;
        float[][] arrayY = createSombreroShape(sombreroSize, sombreroHeight);
        return new SurfacePlot(arrayY, 10, Color.AQUA, false, false);
    }

    private void bindRotationsToSurfacePlot(SurfacePlot surfacePlot) {
        var xRotate = new Rotate(0, Rotate.X_AXIS);
        var yRotate = new Rotate(0, Rotate.Y_AXIS);
        var zRotate = new Rotate(0, Rotate.Z_AXIS);

        surfacePlot.getTransforms().addAll(xRotate, yRotate, zRotate);
        //Use Binding so your rotation doesn't have to be recreated
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        zRotate.angleProperty().bind(angleZ);
    }

    @NotNull
    private static PerspectiveCamera buildCamera() {
        var camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);
        return camera;
    }

    @NotNull
    private static PointLight buildPointLight(double translateY, double translateZ) {
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateZ(translateZ);
        light.setTranslateY(translateY);
        return light;
    }

    private static float[][] createSombreroShape(int sombreroSize, int sombreroHeight) {
        float[][] arrayXY = new float[2 * sombreroSize][2 * sombreroSize];
        for (int x = -sombreroSize; x < sombreroSize; x++) {
            for (int y = -sombreroSize; y < sombreroSize; y++) {
                double radius = distance(x, y);
                radius = radius + (1 / Double.MAX_VALUE); // to avoid zero division
                arrayXY[x + sombreroSize][y + sombreroSize] = ((float) -(Math.sin(radius) / radius)) * sombreroHeight;
            }
        }
        return arrayXY;
    }

    private static double distance(double x, double y) {
        return Math.sqrt((x * x) + (y * y));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private static void keyPressedAction(KeyEvent event, PerspectiveCamera camera) {
        double change = 10.0;
        //Add shift modifier to simulate "Running Speed"
        if (event.isShiftDown()) {
            change = 50.0;
        }
        //What key did the user press?
        KeyCode keycode = event.getCode();
        //Step 2c: Add Zoom controls
        if (keycode == KeyCode.W) {
            camera.setTranslateZ(camera.getTranslateZ() + change);
        }
        if (keycode == KeyCode.S) {
            camera.setTranslateZ(camera.getTranslateZ() - change);
        }
        //Step 2d:  Add Strafe controls
        if (keycode == KeyCode.A) {
            camera.setTranslateX(camera.getTranslateX() - change);
        }
        if (keycode == KeyCode.D) {
            camera.setTranslateX(camera.getTranslateX() + change);
        }
    }

    private void mousePressedAction(MouseEvent event) {
        scenex = event.getSceneX();
        sceney = event.getSceneY();
        fixedXAngle = angleX.get();
        fixedYAngle = angleY.get();
        if (event.isMiddleButtonDown()) {
            scenez = event.getSceneX();
            fixedZAngle = angleZ.get();
        }

    }

    private void mouseDraggedAction(MouseEvent event) {
        if (event.isMiddleButtonDown()) {
            angleZ.set(fixedZAngle - (scenez - event.getSceneY()));
        } else {
            angleX.set(fixedXAngle - (scenex - event.getSceneY()));
        }
        angleY.set(fixedYAngle + sceney - event.getSceneX());
    }
}
