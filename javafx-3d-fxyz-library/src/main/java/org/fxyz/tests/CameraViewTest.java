package org.fxyz.tests;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.fxyz.cameras.CameraTransformer;
import org.fxyz.shapes.primitives.TorusMesh;
import org.fxyz.tools.CameraView;
import org.fxyz.utils.CameraUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dub
 */
public class CameraViewTest extends Application {

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;

    public CameraViewTest() {
        super();
    }

    @Override
    public void start(Stage stage) throws Exception {
        var subScene = loadSubScene();
        var cameraView = buildSubSceneCameraView(subScene);
        var root = new StackPane(subScene, cameraView);
        subScene.widthProperty().bind(root.widthProperty());
        subScene.heightProperty().bind(root.heightProperty());
        root.setStyle("-fx-background-color: DEEPSKYBLUE;");
        final var scene = new Scene(root, 810, 610, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("MiniMapTest");
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }

    private SubScene loadSubScene() {
        PerspectiveCamera camera = buildCamera();
        CameraTransformer cameraTransformer = buildCameraTransformer(camera);
        TorusMesh torus = buildTorus();
        var worldRoot = new Group(cameraTransformer, torus);
        var subScene = new SubScene(worldRoot, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.DARKSLATEGRAY);
        subScene.setCamera(camera);
        subScene.setOnKeyPressed(event -> CameraUtils.moveInXZPlane(camera, event));
        subScene.setOnMousePressed(this::onMousePressed);
        subScene.setOnMouseDragged(me -> onMouseDragged(camera, me, cameraTransformer));
        return subScene;
    }

    private CameraTransformer buildCameraTransformer(PerspectiveCamera camera) {
        var cameraTransformer = new CameraTransformer();
        cameraTransformer.setTranslate(0, 0, -500);
        cameraTransformer.rotateX.setAngle(-45.0);
        cameraTransformer.rotateY.setAngle(-10.0);
        //add a Point Light for better viewing of the grid coordinate system
        PointLight light = buildCameraLight(camera);
        cameraTransformer.getChildren().addAll(camera, light);
        return cameraTransformer;
    }

    private static CameraView buildSubSceneCameraView(SubScene subScene) {
        var cameraView = new CameraView(subScene);
        cameraView.setFirstPersonNavigationEnabled(true);
        cameraView.setFitWidth(350);
        cameraView.setFitHeight(225);
        cameraView.getRx().setAngle(-45);
        cameraView.getT().setZ(-1500);
        cameraView.getT().setY(-500);
        StackPane.setAlignment(cameraView, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(cameraView, new Insets(5));
        cameraView.startViewing();
        return cameraView;
    }

    private static PerspectiveCamera buildCamera() {
        var camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1500);
        return camera;
    }

    private void onMouseDragged(PerspectiveCamera camera, MouseEvent me, CameraTransformer cameraTransform) {
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
            cameraTransform.rotateY.setAngle(((cameraTransform.rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // +
            cameraTransform.rotateX.setAngle(((cameraTransform.rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180); // -
        } else if (me.isSecondaryButtonDown()) {
            double z = camera.getTranslateZ();
            double newZ = z + mouseDeltaX * modifierFactor * modifier;
            camera.setTranslateZ(newZ);
        } else if (me.isMiddleButtonDown()) {
            cameraTransform.translate.setX(cameraTransform.translate.getX() + mouseDeltaX * modifierFactor * modifier * 0.3); // -
            cameraTransform.translate.setY(cameraTransform.translate.getY() + mouseDeltaY * modifierFactor * modifier * 0.3); // -

        }
    }

    @NotNull
    private static PointLight buildCameraLight(PerspectiveCamera camera) {
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());
        return light;
    }

    private static void animateTorus(TorusMesh torus) {
        final Timeline t = new Timeline();
        t.getKeyFrames().addAll(new KeyFrame(Duration.seconds(5), // Frame End
                new KeyValue(torus.tubeStartAngleOffsetProperty(), torus.getTubeStartAngleOffset() - 10, Interpolator.EASE_BOTH),
                new KeyValue(torus.xOffsetProperty(), torus.getxOffset() + 0.5, Interpolator.EASE_BOTH),
                new KeyValue(torus.yOffsetProperty(), torus.getyOffset() + 0.5, Interpolator.EASE_BOTH),
                new KeyValue(torus.zOffsetProperty(), torus.getzOffset() + 2, Interpolator.EASE_BOTH),
                new KeyValue(torus.tubeDivisionsProperty(), 120, Interpolator.EASE_BOTH),
                new KeyValue(torus.radiusDivisionsProperty(), 120, Interpolator.EASE_BOTH),
                new KeyValue(torus.tubeRadiusProperty(), 400, Interpolator.EASE_BOTH)));
        t.setCycleCount(Animation.INDEFINITE);
        t.setAutoReverse(true);
        t.playFromStart();
    }

    private static TorusMesh buildTorus() {
        int rDivs = 32;
        int tDivs = 32;
        double rad = 600;
        double trad = 400;
        TorusMesh torus = new TorusMesh(rDivs, tDivs, rad, trad);
        torus.setDrawMode(DrawMode.LINE);
        PhongMaterial mat = new PhongMaterial(Color.BLUEVIOLET);
        torus.setMaterial(mat);
        torus.setTranslateX(0);
        torus.setTranslateY(0);
        torus.setTranslateZ(0);
        animateTorus(torus);
        return torus;
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void onMousePressed(MouseEvent me) {
        mousePosX = me.getSceneX();
        mousePosY = me.getSceneY();
        mouseOldX = me.getSceneX();
        mouseOldY = me.getSceneY();

    }
}
