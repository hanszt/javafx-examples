package hzt;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class Connection3DSample extends Application {

    private static final Point3D INIT_RED_POINT_POSITION = new Point3D(10, 0, 0);

    private final Sphere greenSphere = createAndPositionSphere(2, new Point3D(10, 30, 50));
    private final Sphere redSphere = createAndPositionSphere(2, INIT_RED_POINT_POSITION);
    private final Sphere blueSphere = createAndPositionSphere(2, new Point3D(-10, 0, 0));
    private final Sphere yellowSphere = createAndPositionSphere(2, new Point3D(0, 10,0));
    private final MouseControlService mouseControlService = new MouseControlService();

    @NotNull
    private Line3D createLine3D(Node begin, Node end) {
        Line3D line3D = new Line3D();
        line3D.widthProperty().set(.5);
        line3D.defineStartAndEndpoint(begin, end);
        return line3D;
    }

    private Parent createContent() {
        Line3D gRLine3D = createLine3D(greenSphere, redSphere);
        Line3D gBLine3D = createLine3D(greenSphere, blueSphere);
        Line3D bRLine3D = createLine3D(blueSphere, redSphere);
        Line3D gYLine3D = createLine3D(greenSphere, yellowSphere);
        Line3D bYLine3D = createLine3D(blueSphere, yellowSphere);
        Line3D rYLine3D = createLine3D(redSphere, yellowSphere);

        Group connectionGroup = new Group(gRLine3D, gBLine3D, bRLine3D, gYLine3D, bYLine3D, rYLine3D);
        greenSphere.setMaterial(new PhongMaterial(Color.GREEN));
        redSphere.setMaterial(new PhongMaterial(Color.RED));
        blueSphere.setMaterial(new PhongMaterial(Color.BLUE));
        yellowSphere.setMaterial(new PhongMaterial(Color.YELLOW));

        Group root = new Group(greenSphere, redSphere, blueSphere, yellowSphere, connectionGroup);

        SubScene subScene = createSubScene(root);
        mouseControlService.initMouseControl(root, subScene);
        return new Group(subScene);
    }

    private void addKeyControl(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressed);
    }

    private static final double DELTA = 1;

    private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.A)) {
            redSphere.setTranslateX(redSphere.getTranslateX() - DELTA);
        }
        if (keyEvent.getCode().equals(KeyCode.D)) {
            redSphere.setTranslateX(redSphere.getTranslateX() + DELTA);
        }
        if (keyEvent.getCode().equals(KeyCode.W)) {
            redSphere.setTranslateY(redSphere.getTranslateY() - DELTA);
        }
        if (keyEvent.getCode().equals(KeyCode.S)) {
            redSphere.setTranslateY(redSphere.getTranslateY() + DELTA);
        }
        if (keyEvent.getCode().equals(KeyCode.F)) {
            redSphere.setTranslateZ(redSphere.getTranslateZ() - DELTA);
        }
        if (keyEvent.getCode().equals(KeyCode.R)) {
            redSphere.setTranslateZ(redSphere.getTranslateZ() + DELTA);
        }
        if (keyEvent.getCode().equals(KeyCode.C)) {
            mouseControlService.setOrientation(0 ,0);
            mouseControlService.setTargetTranslation(Point3D.ZERO);
        }
        if (keyEvent.getCode().equals(KeyCode.V)) {
            redSphere.setTranslateX(INIT_RED_POINT_POSITION.getX());
            redSphere.setTranslateY(INIT_RED_POINT_POSITION.getY());
            redSphere.setTranslateZ(INIT_RED_POINT_POSITION.getZ());
        }
    }

    @NotNull
    private SubScene createSubScene(Group root) {
        SubScene subScene = new SubScene(root, 600, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        PerspectiveCamera camera = createAndPositionCamera();
        subScene.setCamera(camera);
        return subScene;
    }

    @NotNull
    private Sphere createAndPositionSphere(double radius, Point3D position) {
        Sphere sphere = new Sphere(radius);
        sphere.setTranslateX(position.getX());
        sphere.setTranslateY(position.getY());
        sphere.setTranslateZ(position.getZ());
        return sphere;
    }

    @NotNull
    private PerspectiveCamera createAndPositionCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(.01);
        camera.setFarClip(1000);
        camera.setTranslateZ(-200);
        return camera;
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent());
        addKeyControl(scene);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
