package hzt;

import fx_customs.MyScene;
import hzt.model.CylinderHead;
import hzt.model.Duck;
import hzt.model.LunarLander;
import hzt.model.Plane;
import hzt.model.PropellerAssembly;
import hzt.model.Pyramid;
import hzt.model.Scooter;
import hzt.model.TuxRotation;
import interfaces.IMouseControlService;
import services.MouseControlService;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import utils.FxUtils;

import java.util.function.Consumer;

import static java.util.function.Predicate.not;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 *
 *  Run this application via FxLauncher. Then the JavaFX runtime components are missing, and are required to run this application is omitted
 *  @see hzt.FxLauncher#main(String[])
 *  @see <a href="https://stackoverflow.com/questions/61033077/how-do-i-install-and-forget-java-9-modules-on-windows">
 *      How do I install and forget java 9 modules</a>
 *
 */
public class ModelLoadApp extends Application {

    private static Scene createScene() {
        Scooter scooter = getScooter();
        Plane plane = getPlane();
        LunarLander lunarLander = new LunarLander();
        CylinderHead cylinderHead = getCylinderHead();
        PropellerAssembly propellerAssembly = getPropellerAssembly();
        Duck duck = getDuck();
        Pyramid pyramid = getPyramid();
        TuxRotation tuxRotation = getTuxRotation();
        Group root = new Group(scooter, plane, lunarLander, cylinderHead,
                propellerAssembly, duck, pyramid, tuxRotation);
        return buildScene(root);
    }

    private static MyScene buildScene(Group root) {
        MyScene scene = new MyScene(root, 1280, 720, true);
        scene.setOnKeyTyped(keyEvent -> keyTypedAction(keyEvent, root));
        root.getChildren().stream()
                .filter(not(Slider.class::isInstance))
                .forEach(addMouseControl(scene));

        PerspectiveCamera camera = getPerspectiveCamera();
        camera.setTranslateZ(-300);
        scene.setCamera(camera);
        return scene;
    }

    private static Consumer<Node> addMouseControl(MyScene scene) {
        return node -> {
            IMouseControlService planeMouseControlService = new MouseControlService();
            planeMouseControlService.initMouseControl(node, scene);
            planeMouseControlService.setOrientation(20, 20);
        };
    }

    private static void keyTypedAction(KeyEvent keyEvent, Group root) {
        if ("w".equals(keyEvent.getCharacter())) {
            FxUtils.allDescendents(root).stream()
                    .filter(MeshView.class::isInstance)
                    .map(MeshView.class::cast)
                    .forEach(ModelLoadApp::setDrawMode);
        }
    }

    private static void setDrawMode(MeshView m) {
        m.setDrawMode(m.getDrawMode() == DrawMode.LINE ? DrawMode.FILL : DrawMode.LINE);
    }

    @NotNull
    private static TuxRotation getTuxRotation() {
        TuxRotation tuxRotation = new TuxRotation();
        tuxRotation.setTranslateX(20);
        tuxRotation.setTranslateY(20);
        return tuxRotation;
    }

    @NotNull
    private static Pyramid getPyramid() {
        Pyramid pyramid = new Pyramid();
        pyramid.setTranslateY(10);
        pyramid.setScale(3);
        return pyramid;
    }

    @NotNull
    private static Plane getPlane() {
        Plane plane = new Plane();
        plane.setScale(2);
        return plane;
    }

    @NotNull
    private static Scooter getScooter() {
        Scooter scooter = new Scooter();
        scooter.setScale(10);
        return scooter;
    }

    @NotNull
    private static Duck getDuck() {
        Duck duck = new Duck();
        duck.setTranslateY(-20);
        duck.setScale(2);
        return duck;
    }

    @NotNull
    private static PropellerAssembly getPropellerAssembly() {
        PropellerAssembly propellerAssembly = new PropellerAssembly();
        propellerAssembly.setScale(.05);
        propellerAssembly.setTranslateX(-150);
        propellerAssembly.setTranslateY(-100);
        return propellerAssembly;
    }

    @NotNull
    private static PerspectiveCamera getPerspectiveCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10_000);
        camera.setTranslateZ(-100);
        return camera;
    }

    @NotNull
    private static CylinderHead getCylinderHead() {
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

    public static void main(String[] args) {
        launch(args);
    }

}
