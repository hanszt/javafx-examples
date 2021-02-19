package hzt;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import static javafx.animation.Animation.INDEFINITE;

public class Connection3DSample extends Application {

    private final Translate pivot = new Translate();
    public final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

    private Parent createContent() {
        Sphere point1 = createAndPositionSphere(2,10, 10, 6);
        Sphere point2 = createAndPositionSphere(2, -10, -10, 3);
        Connection connection = new Connection();
        connection.defineStartAndEndpoint(point1, point2);

        Sphere sphere = createAndPositionSphere(2.5, 2, 0, 7);
        sphere.setMaterial(new PhongMaterial(Color.FORESTGREEN));

        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.RED));

        PerspectiveCamera camera = createAndPositionCamera();
        animateCameraPosition();

        Group root = new Group(box, sphere, point1, point2, connection);

        // set the pivot for the camera position animation base upon mouse clicks on objects
        configureRootChildren(root);

        // Use a SubScene
        SubScene subScene = new SubScene(root, 300, 300, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        new MouseControlService().initMouseControl(point1, subScene);
        return new Group(subScene);
    }

    @NotNull
    private Sphere createAndPositionSphere(double radius, double xPos, double yPos, double zPos) {
        Sphere sphere = new Sphere(radius);
        sphere.setTranslateX(xPos);
        sphere.setTranslateY(yPos);
        sphere.setTranslateZ(zPos);
        return sphere;
    }

    private void configureRootChildren(Group root) {
        root.getChildren().forEach(node -> node.setOnMouseClicked(event -> updatePivot(node)));
    }

    @NotNull
    private PerspectiveCamera createAndPositionCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(.01);
        camera.setFarClip(1000);
        camera.getTransforms().addAll(
                pivot,
                yRotate,
                new Rotate(-20, Rotate.X_AXIS),
                new Translate(0, 0, -100)
        );
        return camera;
    }

    private void animateCameraPosition() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(yRotate.angleProperty(), 0)),
                new KeyFrame(Duration.seconds(15), new KeyValue(yRotate.angleProperty(), 360))
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(INDEFINITE);
        timeline.play();
    }

    private void updatePivot(Node node) {
        pivot.setX(node.getTranslateX());
        pivot.setY(node.getTranslateY());
        pivot.setZ(node.getTranslateZ());
    }

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
