package hzt;

import hzt.model.*;
import hzt.service.IMouseControlService;
import hzt.service.MouseControlService;
import hzt.utils.FxUtils;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ModelLoadApp extends Application {

    private static void setDrawMode(MeshView m) {
        m.setDrawMode(m.getDrawMode().equals(DrawMode.LINE) ? DrawMode.FILL : DrawMode.LINE);
    }

    private Scene createScene() {
        Scooter scooter = getScooter();
        Plane plane = getPlane();
        LunarLander lunarLander = new LunarLander();
        CylinderHead cylinderHead = getCylinderHead();
        PropellerAssembly propellerAssembly = getPropellerAssembly();
        Duck duck = getDuck();
        Pyramid pyramid = getPyramid();
        TuxRotation tuxRotation = getTuxRotation();
        var toroid = getToroid();
        Group root = new Group(scooter, plane, lunarLander, cylinderHead,
                propellerAssembly, duck, pyramid, tuxRotation, toroid);
        return buildScene(root);
    }

    @NotNull
    private Scene buildScene(Group root) {
        Scene scene = new Scene(root, 1280, 720, true);
        scene.setOnKeyTyped(keyEvent -> keyTypedAction(keyEvent, root));
        root.getChildren().forEach(model -> addMouseControl(model, scene));

        PerspectiveCamera camera = getPerspectiveCamera();
        scene.setCamera(camera);
        return scene;
    }

    private void addMouseControl(Node model3D, Scene scene) {
        IMouseControlService planeMouseControlService = new MouseControlService();
        planeMouseControlService.initMouseControl(model3D, scene);
        planeMouseControlService.setOrientation(20, 20);
    }

    private void keyTypedAction(KeyEvent keyEvent, Group root) {
        if (keyEvent.getCharacter().equals("w")) {
            FxUtils.getAllDescendents(root).stream()
                    .filter(MeshView.class::isInstance)
                    .map(FxUtils::toMeshView).forEach(ModelLoadApp::setDrawMode);
        }
    }

    @NotNull
    private TuxRotation getTuxRotation() {
        TuxRotation tuxRotation = new TuxRotation();
        tuxRotation.setTranslateX(20);
        tuxRotation.setTranslateY(20);
        return tuxRotation;
    }

    private Group getToroid() {
        var toroid = new Toroid(10, 2, 2, 90);
        toroid.setRotationAxis(Rotate.Z_AXIS);
        toroid.setRotate(90);
        toroid.setDrawMode(DrawMode.LINE);
        toroid.setScaleX(1);
        return new Group(toroid);
    }

    @NotNull
    private Pyramid getPyramid() {
        Pyramid pyramid = new Pyramid();
        pyramid.setTranslateY(10);
        pyramid.setScale(3);
        return pyramid;
    }

    @NotNull
    private Plane getPlane() {
        Plane plane = new Plane();
        plane.setScale(2);
        return plane;
    }

    @NotNull
    private Scooter getScooter() {
        Scooter scooter = new Scooter();
        scooter.setScale(10);
        return scooter;
    }

    @NotNull
    private Duck getDuck() {
        Duck duck = new Duck();
        duck.setTranslateY(-20);
        duck.setScale(2);
        return duck;
    }

    @NotNull
    private PropellerAssembly getPropellerAssembly() {
        PropellerAssembly propellerAssembly = new PropellerAssembly();
        propellerAssembly.setScale(.05);
        propellerAssembly.setTranslateX(50);
        return propellerAssembly;
    }

    @NotNull
    private PerspectiveCamera getPerspectiveCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10_000);
        camera.setTranslateZ(-100);
        return camera;
    }

    @NotNull
    private CylinderHead getCylinderHead() {
        CylinderHead cylinderHead = new CylinderHead();
        cylinderHead.setTranslateZ(100);
        cylinderHead.setTranslateY(70);
        cylinderHead.setTranslateX(-50);
        cylinderHead.setScale(.2);
        return cylinderHead;
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(createScene());
        stage.show();
    }

}
