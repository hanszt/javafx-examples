package hzt;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.animation.Timeline.INDEFINITE;

public class Fx3DNr2MovingObjectsWrappedInSubScene extends Application {

    private final SplitPane splitPane = new SplitPane();
    private final Group subSceneRoot = new Group();
    private final SubScene subScene = new SubScene(subSceneRoot, 500, 500, true, SceneAntialiasing.BALANCED);

    private void createAndAddSubSceneContent(Group subSceneRoot) {
        Sphere sphere = new Sphere(2.5);
        sphere.setMaterial(new PhongMaterial(Color.FORESTGREEN));

        sphere.setTranslateZ(7);
        sphere.setTranslateX(10);

        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.RED));
        subSceneRoot.getChildren().addAll(sphere, box);
    }

    private void configureSubScene(SubScene subScene) {
        subScene.setFill(Color.ALICEBLUE);
        MovableCamera camera = new MovableCamera();
        camera.animateCamera();
        subSceneRoot.getChildren().forEach(camera::setCameraPivotBasedOnMouseClickedOnObject);
        subScene.setCamera(camera);
    }

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        AnchorPane subScenePane = new AnchorPane();
        createAndAddSubSceneContent(subSceneRoot);
        configureSubScene(subScene);
        subScenePane.getChildren().add(subScene);
        splitPane.getItems().addAll(subScenePane, new SliderPane());
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
                    yRotate,
                    new Rotate(-20, Rotate.X_AXIS),
                    new Translate(0, 0, -100)
            );
        }

        private void animateCamera() {
            // animate the camera position.
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0),
                            new KeyValue(yRotate.angleProperty(), 0)
                    ),
                    new KeyFrame(
                            Duration.seconds(15),
                            new KeyValue(yRotate.angleProperty(), 360)
                    )
            );
            timeline.setCycleCount(INDEFINITE);
            timeline.play();
        }

        private void setCameraPivotBasedOnMouseClickedOnObject(Node node) {
            // set the pivot for the camera position animation base upon mouse clicks on objects
            node.setOnMouseClicked(event -> {
                System.out.println("clicked");
                pivot.setX(node.getTranslateX());
                pivot.setY(node.getTranslateY());
                pivot.setZ(node.getTranslateZ());
            });

        }

    }

    private static class SliderPane extends AnchorPane {

        private final Slider translateXSlider = new Slider();
        private final Slider translateYSlider = new Slider();
        private final Slider translateZSlider = new Slider();

        private final Slider rotateAxisXSlider = new Slider();
        private final Slider rotateAxisYSlider = new Slider();
        private final Slider rotateAxisZXSlider = new Slider();

        private final Slider rotationAngleSlider = new Slider();

        public SliderPane() {
            VBox hBox1 = new VBox(
                    new Label("Translate x slider"),translateXSlider,
                    new Label("Translate y slider"),translateYSlider,
                    new Label("Translate z slider"),translateZSlider);
            VBox hBox2 = new VBox(
                    new Label("Rotate axis x component slider"),rotateAxisXSlider,
                    new Label("Rotate axis y component slider"),rotateAxisYSlider,
                    new Label("Rotate axis z component slider"),rotateAxisZXSlider,
                    new Label("rotation angle slider"),rotationAngleSlider);
            HBox container = new HBox(hBox1, hBox2);
            getChildren().add(container);
        }

        private Slider getConfiguredSlider() {
            Slider slider = new Slider();
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            return slider;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
