/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.tests;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxyz.cameras.CameraTransformer;
import org.fxyz.tools.CubeViewer;
import org.fxyz.utils.CameraUtils;

/**
 *
 * @author Sean
 */
public class CubeViewerTest extends Application {

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group sceneRoot = new Group();
        double sceneWidth = 600;
        double sceneHeight = 600;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        //Setup camera and scatterplot cubeviewer
        var camera = new PerspectiveCamera(true);
        var cubeViewer = new CubeViewer(1000, 100, true);
        sceneRoot.getChildren().addAll(cubeViewer);        
        //setup camera transform for rotational support
        var cameraTransform = new CameraTransformer();
        cubeViewer.getChildren().add(cameraTransform);
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().add(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-5000);
        cameraTransform.rotateY.setAngle(-45.0);
        cameraTransform.rotateX.setAngle(-10.0);
        //add a Point Light for better viewing of the grid coordinate system
        PointLight light = new PointLight(Color.WHITE);
        cameraTransform.getChildren().add(light);
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());        
        scene.setCamera(camera);
        
        //Create and add some data to the Cube
        ArrayList<Double> dataX = new ArrayList<>();
        ArrayList<Double> dataY = new ArrayList<>();
        ArrayList<Double> dataZ = new ArrayList<>();
        for(int i=-250;i<250;i++) {
            dataX.add((double) i);
            dataY.add(Math.sin(i) * 50 +i);
            dataZ.add(Math.cos(i) * 50 +i);
        }
        //The cube viewer will add data nodes as cubes here but you can add
        //your own scatter plot to the same space as the cube if you want.
        cubeViewer.setxAxisData(dataX);
        cubeViewer.setyAxisData(dataY);
        cubeViewer.setzAxisData(dataZ);
        //First person shooter keyboard movement 
        scene.setOnKeyPressed(event -> CameraUtils.moveInXZPlane(camera, event));
        
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);
            
            double modifier = 10.0;
            double modifierFactor = 0.1;
            
            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 50.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraTransform.rotateY.setAngle(((cameraTransform.rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // +
                cameraTransform.rotateX.setAngle(((cameraTransform.rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // -
                cubeViewer.adjustPanelsByPos(cameraTransform.rotateX.getAngle(), cameraTransform.rotateY.getAngle(), cameraTransform.rotateZ.getAngle());
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraTransform.translate.setX(cameraTransform.translate.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                cameraTransform.translate.setY(cameraTransform.translate.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
            }
        });
        
        primaryStage.setTitle("F(X)yz CubeViewerTest");
        primaryStage.setScene(scene);
        primaryStage.show();        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
