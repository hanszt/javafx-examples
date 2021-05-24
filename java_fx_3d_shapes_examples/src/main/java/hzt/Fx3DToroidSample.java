package hzt;

import hzt.model.Toroid;
import hzt.service.IMouseControlService;
import hzt.service.MouseControlService;
import hzt.utils.FxUtils;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
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
        var tubeRadXSlider = buildSlider(-10, 20, 10, toroid.tubeRadXProperty());
        var tubeRadYSlider = buildSlider(-20, 20, 10, toroid.tubeRadYProperty());
        var ringRadiusSlider = buildSlider(-100,100, 30, toroid.ringRadProperty());
        var tubeZeroPosSlider = buildSlider(0, 2 * Math.PI, 0, toroid.tubeZeroPosProperty());
        var xOffsetSlider = buildSlider(-1, 1, 0, toroid.xOffsetProperty());
        var yOffsetSlider = buildSlider(-1, 2,1, toroid.yOffsetProperty());
        var zOffsetSlider = buildSlider(-1, 1, 0, toroid.zOffsetProperty());
        var numberOfRingSegmentsSlider = buildSlider(0, 100, 64, toroid.nbrRingSegmentsProperty());
        var numberOfTubeSegmentsSlider = buildSlider(0, 100, 32, toroid.nbrTubeSegmentsProperty());
        var vBox = new VBox(
                new Label("Ring radius slider"), ringRadiusSlider,
                new Label("Tube rad x slider"), tubeRadXSlider,
                new Label("Tube rad y slider"), tubeRadYSlider,
                new Label("tube zero pos slider"), tubeZeroPosSlider,
                new Label("x offset slider"), xOffsetSlider,
                new Label("y offset slider"), yOffsetSlider,
                new Label("z offset slider"), zOffsetSlider,
                new Label("number of ring segments slider"), numberOfRingSegmentsSlider,
                new Label("Number of tube segments slider"), numberOfTubeSegmentsSlider);
        vBox.getChildren().stream().filter(Slider.class::isInstance)
                .map(Slider.class::cast)
                .forEach(this::showTickMarksAndLabels);
        return vBox;
    }

    private Slider buildSlider(double min, double max, double cur, DoubleProperty property) {
        var slider = new Slider(min, max, cur);
        property.bind(slider.valueProperty());
        return slider;
    }

    private Slider buildSlider(double min, double max, double cur, IntegerProperty property) {
        var slider = new Slider(min, max, cur);
        property.bind(slider.valueProperty());
        return slider;
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
