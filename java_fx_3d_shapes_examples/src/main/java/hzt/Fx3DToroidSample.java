package hzt;

import hzt.model.Toroid;
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

import static java.util.function.Predicate.not;

public class Fx3DToroidSample extends Application {

    private Scene createScene() {
        var toroid = getToroid();
        var toroidContainer = new Group(toroid);
        toroidContainer.setTranslateX(-50);
        var labeledSliders = labeledToroidSliders(toroid);
        labeledSliders.setTranslateX(50);
        labeledSliders.setTranslateY(-150);
        labeledSliders.setTranslateZ(350);
        Group root = new Group(toroidContainer, labeledSliders);
        return buildScene(root);
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

    private PerspectiveCamera getPerspectiveCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10_000);
        camera.setTranslateZ(-100);
        return camera;
    }

    private void addMouseControl(Node model3D, Scene scene) {
        IMouseControlService mouseControlService = new MouseControlService();
        mouseControlService.initMouseControl(model3D, scene);
        mouseControlService.setOrientation(20, 20);
    }

    private void keyTypedAction(KeyEvent keyEvent, Group root) {
        if (keyEvent.getCharacter().equals("w")) {
            FxUtils.getAllDescendents(root).stream()
                    .filter(MeshView.class::isInstance)
                    .map(FxUtils::toMeshView).forEach(this::setDrawMode);
        }
    }

    private void setDrawMode(MeshView m) {
        m.setDrawMode(m.getDrawMode().equals(DrawMode.LINE) ? DrawMode.FILL : DrawMode.LINE);
    }

    private VBox labeledToroidSliders(Toroid toroid) {
        Slider tubeRadXSlider = new Slider();
        tubeRadXSlider.setValue(10);
        tubeRadXSlider.setMin(-20);
        tubeRadXSlider.setMax(20);
        toroid.tubeRadXProperty().bind(tubeRadXSlider.valueProperty());
        Slider tubeRadYSlider = new Slider();
        tubeRadYSlider.setValue(10);
        tubeRadYSlider.setMin(-20);
        tubeRadYSlider.setMax(20);
        toroid.tubeRadYProperty().bind(tubeRadYSlider.valueProperty());
        Slider ringRadiusSlider = new Slider();
        ringRadiusSlider.setMin(-100);
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
        var vBox = new VBox(
                new Label("Ring radius slider"), ringRadiusSlider,
                new Label("Tube rad x slider"), tubeRadXSlider,
                new Label("Tube rad y slider"), tubeRadYSlider,
                new Label("tube zero pos slider"), tubeZeroPosSlider,
                new Label("number of ring segments slider"), numberOfRingSegmentsSlider,
                new Label("Number of tube segments slider"), numberOfTubeSegmentsSlider);
        vBox.getChildren().stream().filter(Slider.class::isInstance)
                .map(Slider.class::cast)
                .forEach(this::showTickMarksAndLabels);
        return vBox;
    }

    private void showTickMarksAndLabels(Slider s) {
        s.setShowTickMarks(true);
        s.setShowTickLabels(true);
    }

    private Toroid getToroid() {
        var toroid = new Toroid();
        var material = toroid.getPhongMaterial();
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        var diffuseMap = new Image(getClass().getResourceAsStream("/images/cup_diffuseMap_1024.png"));
        var bumpMap = new Image(getClass().getResourceAsStream("/images/cup_normalMap_1024.png"));
        toroid.setTexture(diffuseMap, bumpMap);
        toroid.setRotationAxis(Rotate.Z_AXIS);
        toroid.setRotate(90);
        return toroid;
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(createScene());
        stage.show();
    }
}
