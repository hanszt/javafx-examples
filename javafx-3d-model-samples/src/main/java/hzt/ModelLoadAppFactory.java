package hzt;

import fx_customs.MyScene;
import hzt.model.CylinderHead;
import hzt.model.Duck;
import hzt.model.LunarLander;
import hzt.model.Model3D;
import hzt.model.Plane;
import hzt.model.PropellerAssembly;
import hzt.model.Pyramid;
import hzt.model.Scooter;
import hzt.model.TuxRotation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import org.jetbrains.annotations.NotNull;
import utils.FxUtils;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 * <p>
 * Run this application via FxLauncher. Then the JavaFX runtime components are missing, and are required to run this application is omitted
 * @see hzt.FxLauncher#main(String[])
 * @see <a href="https://stackoverflow.com/questions/61033077/how-do-i-install-and-forget-java-9-modules-on-windows">
 * How do I install and forget java 9 modules</a>
 */
public final class ModelLoadAppFactory {

    private ModelLoadAppFactory() {
    }

    static Scene createScene() {
        Scooter scooter = getScooter();
        Node plane = scooter.isDisabled() ? getPlane() : new Sphere();
        Node lunarLander = scooter.isDisabled() ? new LunarLander() : new Box();
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
        var scene = new MyScene(root, 1280, 720, true);
        scene.setOnKeyTyped(keyEvent -> viewAsMeshWhenWTyped(keyEvent, root));

        root.getChildren().stream()
                .mapMulti(Model3D::toModel3DIfInstance)
                .forEach(Model3D::addMouseControl);

        PerspectiveCamera camera = getPerspectiveCamera();
        camera.setTranslateZ(-300);
        scene.setCamera(camera);
        return scene;
    }

    private static void viewAsMeshWhenWTyped(KeyEvent keyEvent, Group root) {
        if ("w".equals(keyEvent.getCharacter())) {
            FxUtils.allDescendents(root).stream()
                    .filter(MeshView.class::isInstance)
                    .map(MeshView.class::cast)
                    .forEach(ModelLoadAppFactory::setDrawMode);
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

}
