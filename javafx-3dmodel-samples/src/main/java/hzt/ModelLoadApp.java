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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import static java.util.function.Predicate.*;

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
        var toroidContainer = new Group(toroid);
        toroidContainer.setTranslateX(-50);
        var labeledSlider = labeledToroidSliders(toroid);
        labeledSlider.setTranslateX(50);
        labeledSlider.setTranslateY(-100);
        labeledSlider.setTranslateZ(300);
        Group root = new Group(scooter, plane, lunarLander, cylinderHead,
                propellerAssembly, duck, pyramid, tuxRotation, toroidContainer, labeledSlider);
        return buildScene(root);
    }

    private VBox labeledToroidSliders(Toroid toroid) {
        Slider tubeRadXSlider = new Slider();
        tubeRadXSlider.setValue(10);
        tubeRadXSlider.setMin(.5);
        tubeRadXSlider.setMax(20);
        toroid.tubeRadXProperty().bind(tubeRadXSlider.valueProperty());
        Slider tubeRadYSlider = new Slider();
        tubeRadYSlider.setValue(10);
        tubeRadYSlider.setMin(.5);
        tubeRadYSlider.setMax(20);
        toroid.tubeRadYProperty().bind(tubeRadYSlider.valueProperty());
        Slider ringRadiusSlider = new Slider();
        ringRadiusSlider.setValue(30);
        toroid.ringRadProperty().bind(ringRadiusSlider.valueProperty());
        Slider tubeZeroPosSlider = new Slider();
        tubeZeroPosSlider.setMax(2 * Math.PI);
        toroid.tubeZeroPosProperty().bind(tubeZeroPosSlider.valueProperty());
        Slider numberOfRingSegmentsSlider = new Slider();
        numberOfRingSegmentsSlider.setValue(64);
        toroid.nbrRingSegmentsProperty().bind(numberOfRingSegmentsSlider.valueProperty());
        Slider numberOfTubeSegmentsSlider = new Slider();
        numberOfTubeSegmentsSlider.setValue(32);
        toroid.nbrTubeSegmentsProperty().bind(numberOfTubeSegmentsSlider.valueProperty());
        return new VBox(
                new Label("Tube rad x slider"), tubeRadXSlider,
                new Label("Tube rad y slider"), tubeRadYSlider,
                new Label("tube zero pos slider"), tubeZeroPosSlider,
                new Label("Ring radius slider"), ringRadiusSlider,
                new Label("number of ring segments slider"), numberOfRingSegmentsSlider,
                new Label("Number of tube segments slider"), numberOfTubeSegmentsSlider);
    }

    private Scene buildScene(Group root) {
        Scene scene = new Scene(root, 1280, 720, true);
        scene.setOnKeyTyped(keyEvent -> keyTypedAction(keyEvent, root));
        root.getChildren().stream()
                .filter(not(Slider.class::isInstance))
                .forEach(model -> addMouseControl(model, scene));

        PerspectiveCamera camera = getPerspectiveCamera();
        camera.setTranslateZ(-300);
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

    private Toroid getToroid() {
        var toroid = new Toroid();
        var material = toroid.getPhongMaterial();
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        var diffuseMap = new Image(getClass().getResourceAsStream("model/images/cup_diffuseMap_1024.png"));
        var bumpMap = new Image(getClass().getResourceAsStream("model/images/cup_normalMap_1024.png"));
        toroid.setTexture(diffuseMap, bumpMap);
        toroid.setRotationAxis(Rotate.Z_AXIS);
        toroid.setRotate(90);
        return toroid;
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
        propellerAssembly.setTranslateX(-150);
        propellerAssembly.setTranslateY(-100);
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
