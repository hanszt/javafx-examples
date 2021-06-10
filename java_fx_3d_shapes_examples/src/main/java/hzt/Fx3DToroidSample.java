package hzt;

import hzt.model.Toroid;
import hzt.service.IMouseControlService;
import hzt.service.MouseControlService;
import hzt.utils.FxUtils;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.*;
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

import static java.util.function.Predicate.not;

public class Fx3DToroidSample extends Application {

    private Scene createScene() {
        var toroid = getToroid();
        var toroidContainer = new Group(toroid);
        toroidContainer.setTranslateX(-50);
        var toroidSliderPane = new ToroidSliderPane(toroid);
        toroidSliderPane.setTranslateX(50);
        toroidSliderPane.setTranslateY(-200);
        toroidSliderPane.setTranslateZ(750);
        Group root = new Group(toroidContainer, toroidSliderPane);
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

    private void keyTypedAction(KeyEvent keyEvent, Parent root) {
        if (keyEvent.getCharacter().equals("w")) {
            FxUtils.getAllDescendents(root).stream()
                    .filter(MeshView.class::isInstance)
                    .map(FxUtils::toMeshView).forEach(this::setDrawMode);
        } else if (keyEvent.getCharacter().equals("r")) {
            root.getChildrenUnmodifiable().stream()
                    .filter(ToroidSliderPane.class::isInstance)
                    .map(ToroidSliderPane.class::cast).forEach(ToroidSliderPane::reset);
        }
    }

    private void setDrawMode(MeshView m) {
        m.setDrawMode(m.getDrawMode().equals(DrawMode.LINE) ? DrawMode.FILL : DrawMode.LINE);
    }

    private static class ToroidSliderPane extends Group {

        private final Slider tubeRadXSlider;
        private final Slider tubeRadYSlider;
        private final Slider ringRadiusSlider;
        private final Slider tubeZeroPosSlider;
        private final Slider xOffsetSlider;
        private final Slider yOffsetSlider;
        private final Slider zOffsetSlider;
        private final Slider numberOfRingSegmentsSlider;
        private final Slider numberOfTubeSegmentsSlider;

        private ToroidSliderPane(Toroid toroid) {
            tubeRadXSlider = buildSlider(-10, 20, 10, toroid.tubeRadXProperty());
            tubeRadYSlider = buildSlider(-20, 20, 10, toroid.tubeRadYProperty());
            ringRadiusSlider = buildSlider(-100, 100, 30, toroid.ringRadProperty());
            tubeZeroPosSlider = buildSlider(0, 2 * Math.PI, 0, toroid.tubeZeroPosProperty());
            xOffsetSlider = buildSlider(-1, 1, 0, toroid.xOffsetProperty());
            yOffsetSlider = buildSlider(-1, 2, 1, toroid.yOffsetProperty());
            zOffsetSlider = buildSlider(-1, 1, 0, toroid.zOffsetProperty());
            numberOfTubeSegmentsSlider = buildSlider(32, toroid.nbrTubeSegmentsProperty());
            numberOfRingSegmentsSlider = buildSlider(64, toroid.nbrRingSegmentsProperty());
            var sliderVbox = filledSliderVbox();
            getChildren().add(sliderVbox);
            showTickLabelsAndTickMarks(sliderVbox);
        }

        @NotNull
        private VBox filledSliderVbox() {
            return new VBox(
                    new Label("Ring radius slider"), ringRadiusSlider,
                    new Label("Tube rad x slider"), tubeRadXSlider,
                    new Label("Tube rad y slider"), tubeRadYSlider,
                    new Label("tube zero pos slider"), tubeZeroPosSlider,
                    new Label("x offset slider"), xOffsetSlider,
                    new Label("y offset slider"), yOffsetSlider,
                    new Label("z offset slider"), zOffsetSlider,
                    new Label("number of ring segments slider"), numberOfRingSegmentsSlider,
                    new Label("Number of tube segments slider"), numberOfTubeSegmentsSlider
            );
        }

        private void showTickLabelsAndTickMarks(VBox sliderVbox) {
            sliderVbox.getChildren().stream()
                    .filter(Slider.class::isInstance)
                    .map(Slider.class::cast)
                    .forEach(this::showTickMarksAndLabels);
        }

        private void showTickMarksAndLabels(Slider s) {
            s.setShowTickMarks(true);
            s.setShowTickLabels(true);
        }

        private Slider buildSlider(double min, double max, double cur, DoubleProperty property) {
            var slider = new Slider(min, max, cur);
            property.bind(slider.valueProperty());
            return slider;
        }

        private Slider buildSlider(double cur, IntegerProperty property) {
            var slider = new Slider();
            slider.setValue(cur);
            property.bind(slider.valueProperty());
            return slider;
        }

        private void reset() {
            tubeRadXSlider.setValue(10);
            tubeRadYSlider.setValue(10);
            ringRadiusSlider.setValue(30);
            tubeZeroPosSlider.setValue(0);
            xOffsetSlider.setValue(0);
            yOffsetSlider.setValue(1);
            zOffsetSlider.setValue(0);
            numberOfTubeSegmentsSlider.setValue(32);
            numberOfRingSegmentsSlider.setValue(64);
        }

    }


    private Toroid getToroid() {
        var toroid = new Toroid();
        var material = toroid.getPhongMaterial();
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        var diffuseMapInputStream = getClass().getResourceAsStream("/images/cup_diffuseMap_1024.png");
        var bumpMapInputStream = getClass().getResourceAsStream("/images/cup_normalMap_1024.png");
        if (diffuseMapInputStream != null && bumpMapInputStream != null) {
            toroid.setTexture(new Image(diffuseMapInputStream), new Image(bumpMapInputStream));
        }
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
