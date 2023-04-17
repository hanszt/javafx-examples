package org.fxyz.tests;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.fxyz.cameras.CameraTransformer;
import org.fxyz.shapes.primitives.SpringMesh;
import org.fxyz.shapes.primitives.helper.TriangleMeshHelper;
import org.fxyz.utils.CameraUtils;
import org.fxyz.utils.OBJWriter;

import java.net.URL;
import java.util.Optional;

/**
 * @author jpereda
 */
public class SpringTest extends Application {

    private static final double SCENE_WIDTH = 600;
    private static final double SCENE_HEIGHT = 600;

    private PerspectiveCamera camera;
    private final CameraTransformer cameraTransform = new CameraTransformer();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group sceneRoot = new Group();
        Scene scene = new Scene(sceneRoot, SCENE_WIDTH, SCENE_HEIGHT, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        camera = new PerspectiveCamera(true);

        //setup camera transform for rotational support
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().add(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-500);
        cameraTransform.rotateY.setAngle(-45.0);
        cameraTransform.rotateX.setAngle(-10.0);
        //add a Point Light for better viewing of the grid coordinate system
        PointLight light = new PointLight(Color.WHITE);
        cameraTransform.getChildren().add(light);
//        cameraTransform.getChildren().add(new AmbientLight(Color.WHITE));
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());
        scene.setCamera(camera);

        Rotate rotateY = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Group group = new Group();
        group.getChildren().add(cameraTransform);

        SpringMesh spring = new SpringMesh(5, 1, 2, 4 * 2 * Math.PI,
                303, 54, 0, 0);
        spring.setDrawMode(DrawMode.LINE);
        spring.setCullFace(CullFace.NONE);
        spring.setSectionType(TriangleMeshHelper.SectionType.QUADRILATERAL);

//    // NONE
        spring.setTextureModeNone(Color.web("#4169E180"));
        // IMAGE
        final String NAME = "res/LaminateSteel.jpg";

        Optional.ofNullable(getClass().getResource(NAME))
                .map(URL::toExternalForm)
                .ifPresentOrElse(spring::setTextureModeImage, () -> System.out.println(NAME + " not found"));

        // PATTERN
       spring.setTextureModePattern(5d);
        // FUNCTION
        spring.setTextureModeVertices1D(256*256,t->t);
        // DENSITY
        spring.setTextureModeVertices3D(1530, p -> Math.sin(p.getX()));
        // FACES
        spring.setTextureModeFaces(256*256);

        spring.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS), rotateY);

        group.getChildren().add(spring);

        sceneRoot.getChildren().addAll(group);

        //First person shooter keyboard movement 
        scene.setOnKeyPressed(event -> CameraUtils.moveInXZPlane(camera, event));

        scene.setOnMousePressed(this::onMousePressedAction);
        scene.setOnMouseDragged(this::onMouseDraggedAction);

        System.nanoTime();

        primaryStage.setTitle("F(X)yz - Spring");
        primaryStage.setScene(scene);
        primaryStage.show();

        OBJWriter writer = new OBJWriter((TriangleMesh) spring.getMesh(), "spring");
        writer.setTextureColors(256 * 256);
        writer.exportMesh();

    }

    private void onMousePressedAction(MouseEvent me) {
        mousePosX = me.getSceneX();
        mousePosY = me.getSceneY();
        mouseOldX = me.getSceneX();
        mouseOldY = me.getSceneY();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void onMouseDraggedAction(MouseEvent me) {
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
            cameraTransform.rotateY.setAngle(((cameraTransform.rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier
                    * 2.0) % 360 + 540) % 360 - 180);  // +
            cameraTransform.rotateX.setAngle(((cameraTransform.rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier
                    * 2.0) % 360 + 540) % 360 - 180);  // -
        } else if (me.isSecondaryButtonDown()) {
            double z = camera.getTranslateZ();
            double newZ = z + mouseDeltaX * modifierFactor * modifier;
            camera.setTranslateZ(newZ);
        } else if (me.isMiddleButtonDown()) {
            cameraTransform.translate.setX(cameraTransform.translate.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
            cameraTransform.translate.setY(cameraTransform.translate.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
        }
    }
}
