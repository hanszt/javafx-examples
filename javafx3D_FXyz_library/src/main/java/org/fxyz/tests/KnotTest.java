package org.fxyz.tests;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.fxyz.cameras.CameraTransformer;
import org.fxyz.shapes.primitives.KnotMesh;
import org.fxyz.shapes.primitives.helper.TriangleMeshHelper.SectionType;
import org.fxyz.utils.OBJWriter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jpereda
 */
public class KnotTest extends Application {

    private final CameraTransformer cameraTransform = new CameraTransformer();
    
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private KnotMesh knot;
    private long lastEffect;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group sceneRoot = new Group();
        final double SCENE_WIDTH = 600;
        final double SCENE_HEIGHT = 600;
        Scene scene = new Scene(sceneRoot, SCENE_WIDTH, SCENE_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.WHEAT);
        PerspectiveCamera camera = new PerspectiveCamera(true);
     
        //setup camera transform for rotational support
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().add(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-30);
        cameraTransform.rotateY.setAngle(-45.0);
        cameraTransform.rotateX.setAngle(-10.0);
        //add a Point Light for better viewing of the grid coordinate system
        PointLight light = new PointLight(Color.WHITE);
        cameraTransform.getChildren().add(light);
        cameraTransform.getChildren().add(new AmbientLight(Color.WHITE));
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());        
        scene.setCamera(camera);

        Rotate rotateY = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Group group = new Group();
        group.getChildren().add(cameraTransform);    
        
        knot = new KnotMesh(2d,1d,0.4d,2d,3d,
                                1000,60,0,0);
        knot.setDrawMode(DrawMode.LINE);
//        knot.setCullFace(CullFace.NONE);
        knot.setSectionType(SectionType.TRIANGLE);
        
    // NONE
//        knot.setTextureModeNone(Color.BROWN);
    // IMAGE
//        knot.setTextureModeImage(getClass().getResource("res/LaminateSteel.jpg").toExternalForm());
    // PATTERN
//       knot.setTextureModePattern(3d);
    // FUNCTION
        knot.setTextureModeVertices1D(256*256,t->knot.getTau(t));
    // DENSITY
//        knot.setTextureModeVertices3D(256*256,dens);
    // FACES
//        knot.setTextureModeFaces(256*256);
 
        knot.getTransforms().addAll(new Rotate(0,Rotate.X_AXIS), rotateY);
        
        group.getChildren().add(knot);
        
        sceneRoot.getChildren().addAll(group);        
        
        //First person shooter keyboard movement 
        scene.setOnKeyPressed(event -> {
            double change = 10.0;
            //Add shift modifier to simulate "Running Speed"
            if(event.isShiftDown()) { change = 50.0; }
            //What key did the user press?
            KeyCode keycode = event.getCode();
            //Step 2c: Add Zoom controls
            if(keycode == KeyCode.W) { camera.setTranslateZ(camera.getTranslateZ() + change); }
            if(keycode == KeyCode.S) { camera.setTranslateZ(camera.getTranslateZ() - change); }
            //Step 2d:  Add Strafe controls
            if(keycode == KeyCode.A) { camera.setTranslateX(camera.getTranslateX() - change); }
            if(keycode == KeyCode.D) { camera.setTranslateX(camera.getTranslateX() + change); }
        });        
        
        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> updatePosition(camera, me));
        
        lastEffect = System.nanoTime();
        AtomicInteger count=new AtomicInteger();
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now > lastEffect + 100_000_000L) {
                    count.getAndIncrement();
                    lastEffect = now;
                }
            }
        };
        
        
        primaryStage.setTitle("F(X)yz - Knots");
        primaryStage.setScene(scene);
        primaryStage.show();   
        
        OBJWriter writer=new OBJWriter((TriangleMesh) knot.getMesh(),"knot");
        writer.setMaterialColor(Color.BROWN);
        writer.exportMesh();
    }

    private void updatePosition(PerspectiveCamera camera, MouseEvent me) {
        mouseOldX = mousePosX;
        mouseOldY = mousePosY;
        mousePosX = me.getSceneX();
        mousePosY = me.getSceneY();
        double mouseDeltaX = (mousePosX - mouseOldX);
        double mouseDeltaY = (mousePosY - mouseOldY);

        double modifier = 10.0;
        double modifierFactor = 0.1;

        if (me.isControlDown()) {
            modifier = 0.1;
        }
        if (me.isShiftDown()) {
            modifier = 50.0;
        }
        if (me.isPrimaryButtonDown()) {
            cameraTransform.rotateY
                    .setAngle(((cameraTransform.rotateY.getAngle() +
                            mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // +
            cameraTransform.rotateX
                    .setAngle(((cameraTransform.rotateX.getAngle() -
                            mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // -
        } else if (me.isSecondaryButtonDown()) {
            double z = camera.getTranslateZ();
            double newZ = z + mouseDeltaX * modifierFactor * modifier;
            camera.setTranslateZ(newZ);
        } else if (me.isMiddleButtonDown()) {
            cameraTransform.translate.setX(cameraTransform.translate.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
            cameraTransform.translate.setY(cameraTransform.translate.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
