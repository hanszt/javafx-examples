/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.tests;

import java.util.Random;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.fxyz.cameras.CameraTransformer;
import org.fxyz.shapes.Torus;
import org.fxyz.utils.CameraUtils;

/**
 *
 * @author Sean
 */
public class TorusTest extends Application {
    private PerspectiveCamera camera;
    private final double sceneWidth = 600;
    private final double sceneHeight = 600;
    private final CameraTransformer cameraTransform = new CameraTransformer();
    
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group sceneRoot = new Group();
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        //Setup camera and scatterplot cubeviewer
        camera = new PerspectiveCamera(true);        
     
        //setup camera transform for rotational support
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
        
        //Make a bunch of semi random Torusesessses(toroids?) and stuff
        Group torusGroup = new Group();
         torusGroup.getChildren().add(cameraTransform);       
        for(int i=0;i<30;i++){
            Random r = new Random();
            //A lot of magic numbers in here that just artificially constrain the math
            float randomRadius = (float) ((r.nextFloat()*300) + 50);
            float randomTubeRadius = (float) ((r.nextFloat()*100)+ 1);
            int randomTubeDivisions = (int) ((r.nextFloat()*64) + 1);
            int randomRadiusDivisions = (int) ((r.nextFloat()*64) + 1);
            Color randomColor = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble());
            
            Torus torus = new Torus(randomTubeDivisions, randomRadiusDivisions, randomRadius, randomTubeRadius, randomColor);
                    
            double translationX = Math.random()*sceneWidth*1.95;
            if(Math.random() >= 0.5) {
                translationX *= -1;
            }
            double translationY = Math.random()*sceneWidth*1.95;
            if(Math.random() >= 0.5) {
                translationY *= -1;
            }
            double translationZ = Math.random()*sceneWidth*1.95;
            if(Math.random() >= 0.5) {
                translationZ *= -1;
            }
            Translate translate = new Translate(translationX, translationY, translationZ);
            Rotate rotateX = new Rotate(Math.random()*360, Rotate.X_AXIS);
            Rotate rotateY = new Rotate(Math.random()*360, Rotate.Y_AXIS);
            Rotate rotateZ = new Rotate(Math.random()*360, Rotate.Z_AXIS);
            
            torus.getTransforms().addAll(translate,rotateX,rotateY,rotateZ);
            //torus.getTransforms().add(translate);
            torusGroup.getChildren().add(torus);

        }
        sceneRoot.getChildren().addAll(torusGroup);        
        
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
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraTransform.translate.setX(cameraTransform.translate.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                cameraTransform.translate.setY(cameraTransform.translate.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
            }
        });
        
        primaryStage.setTitle("F(X)yz TorusTest");
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
