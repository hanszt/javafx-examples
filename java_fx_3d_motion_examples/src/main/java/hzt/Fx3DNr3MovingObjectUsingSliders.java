package hzt;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Fx3DNr3MovingObjectUsingSliders extends Application {

    private final SplitPane splitPane = new SplitPane();
    private final Group subSceneRoot = new Group();
    private final Group objectGroup = new Group();
    private final Group axisGroup = new Group();
    private final SubScene subScene = new SubScene(subSceneRoot, 800, 800, true, SceneAntialiasing.BALANCED);

    private void createAndAddSubSceneContent(Group subSceneRoot) {
        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.RED));

        Cylinder cylinder = new Cylinder();
        Rotate rotate = new Rotate(90, Rotate.Z_AXIS);
        cylinder.setHeight(20);
        Translate translate = new Translate(10, 0, 0);
        cylinder.getTransforms().addAll(translate, rotate);

        objectGroup.getChildren().addAll(box, cylinder);
        subSceneRoot.getChildren().add(objectGroup);
    }

    private void configureSubScene(SubScene subScene) {
        subScene.setFill(Color.ALICEBLUE);
        MovableCamera camera = new MovableCamera();
        subSceneRoot.getChildren().forEach(camera::setCameraPivotBasedOnMouseClickedOnObject);
        subScene.setCamera(camera);
    }

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        AnchorPane subScenePane = new AnchorPane();
        createAndAddSubSceneContent(subSceneRoot);
        buildAxes();
        configureSubScene(subScene);
        ControlPane sliderPane = new ControlPane();
        sliderPane.bindFieldOfViewSliderToCamera((PerspectiveCamera) subScene.getCamera());
        sliderPane.bindNodeTranslationToTranslationSliders(objectGroup);
        sliderPane.bindNodeRotationToRotationSliders(objectGroup);
        sliderPane.bindNodeTranslationToTranslationSliders(axisGroup);
        subScenePane.getChildren().add(subScene);
        splitPane.getItems().addAll(subScenePane, sliderPane);
        Scene scene = new Scene(splitPane);
        stage.setScene(scene);
        stage.show();
    }

    private static class MovableCamera extends PerspectiveCamera {

        private final Translate pivot = new Translate();
        private final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        public MovableCamera() {
            super(true);
            configureCamera();
        }

        private void configureCamera() {
            // Create and position camera
            setNearClip(.01);
            setFarClip(1000);
            getTransforms().addAll(
                    pivot,
                    yRotate
            );
        }

        private void setCameraPivotBasedOnMouseClickedOnObject(Node node) {
            // set the pivot for the camera position animation base upon mouse clicks on objects
            node.setOnMouseClicked(event -> {
                pivot.setX(node.getTranslateX());
                pivot.setY(node.getTranslateY());
                pivot.setZ(node.getTranslateZ());
            });

        }

    }

    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(500.0, 1, 1);
        final Box yAxis = new Box(1, 500.0, 1);
        final Box zAxis = new Box(1, 1, 500.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        subSceneRoot.getChildren().addAll(axisGroup);
    }

    private static class ControlPane extends Pane {

        private final Slider translateXSlider = getConfiguredSlider(-100, 100);
        private final Slider translateYSlider = getConfiguredSlider(-100, 100);
        private final Slider translateZSlider = getConfiguredSlider(-100, 100, 500);

        private final Slider rotateAxisXSlider = getConfiguredSlider(-1, 1);
        private final Slider rotateAxisYSlider = getConfiguredSlider(-1, 1);
        private final Slider rotateAxisZSlider = getConfiguredSlider(-1, 1);

        private final Slider rotationAngleSlider = getConfiguredSlider(-360, 360);

        private final Slider fieldOfViewSlider = getConfiguredSlider(-360, 40, 360);

        public ControlPane() {
            Button resetSlidersButton = new Button("reset");
            resetSlidersButton.setPrefWidth(300);
            VBox hBox = new VBox(
                    titleLabel("Object properties", 20),
                    titleLabel("Translation properties", 16),
                    new Label("Translate x"), translateXSlider,
                    new Label("Translate y"), translateYSlider,
                    new Label("Translate z"), translateZSlider,
                    titleLabel("Rotational properties", 16),
                    new Label("Rotate axis x component"), rotateAxisXSlider,
                    new Label("Rotate axis y component"), rotateAxisYSlider,
                    new Label("Rotate axis z component"), rotateAxisZSlider,
                    new Label("Rotation angle slider (degrees)"), rotationAngleSlider,
                    titleLabel("Camera settings", 16),
                    new Label("Adjust field of view Camera (degrees)"), fieldOfViewSlider,
                    resetSlidersButton);
            resetSlidersButton.setOnAction(resetButtonAction());
            hBox.setMinWidth(300);
            getChildren().add(hBox);
        }

        private Label titleLabel(String text, double fontSize) {
            Label label = new Label(text);
            label.setFont(Font.font(fontSize));
            return label;
        }

        private EventHandler<ActionEvent> resetButtonAction() {
            return event -> {
                translateXSlider.setValue(0);
                translateYSlider.setValue(0);
                translateZSlider.setValue(100);
                rotateAxisXSlider.setValue(0);
                rotateAxisYSlider.setValue(0);
                rotateAxisZSlider.setValue(0);
                rotationAngleSlider.setValue(0);
                fieldOfViewSlider.setValue(40);
            };
        }

        private void bindFieldOfViewSliderToCamera(PerspectiveCamera camera) {
            camera.fieldOfViewProperty().bind(fieldOfViewSlider.valueProperty());
        }


        private void bindNodeTranslationToTranslationSliders(Node node) {
            node.translateXProperty().bind(translateXSlider.valueProperty());
            node.translateYProperty().bind(translateYSlider.valueProperty());
            node.translateZProperty().bind(translateZSlider.valueProperty());
        }

        private void bindNodeRotationToRotationSliders(Node node) {
            node.rotateProperty().bind(rotationAngleSlider.valueProperty());
            rotateAxisXSlider.valueProperty().addListener((prevVal, curVal, newVal) -> {
                Point3D currentAxis = node.getRotationAxis();
                node.setRotationAxis(new Point3D(newVal.doubleValue(), currentAxis.getY(), currentAxis.getZ()));
            });
            rotateAxisYSlider.valueProperty().addListener((prevVal, curVal, newVal) -> {
                Point3D currentAxis = node.getRotationAxis();
                node.setRotationAxis(new Point3D(currentAxis.getX(), newVal.doubleValue(), currentAxis.getZ()));
            });
            rotateAxisZSlider.valueProperty().addListener((prevVal, curVal, newVal) -> {
                Point3D currentAxis = node.getRotationAxis();
                node.setRotationAxis(new Point3D(currentAxis.getX(), currentAxis.getY(), newVal.doubleValue()));
            });
        }

        private Slider getConfiguredSlider(double minVal, double curVal, double maxVal) {
            Slider slider = new Slider();
            slider.setMin(minVal);
            slider.setValue(curVal);
            slider.setMax(maxVal);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            return slider;
        }

        private Slider getConfiguredSlider(double minVal, double maxVal) {
            return getConfiguredSlider(minVal, 0, maxVal);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
