package hzt;

import hzt.model.Xform;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import static java.lang.System.*;

/**
 * MoleculeSampleApp
 */
public class Fx3DNr5MoleculeSampleAppRefactored extends Application {

    private static final double CAMERA_DISTANCE = 450;


    final Group root = new Group();
    final Xform world = new Xform();

    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();


    final Group axisGroup = new Group();
    final Xform moleculeGroup = new Xform();

    private void buildScene() {
        out.println("buildScene");
        root.getChildren().add(world);
    }

    @Override
    public void start(Stage primaryStage) {
        buildScene();
        buildCamera();
        buildAxes();
        buildMolecule();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.GREY);
        handleKeyboard(scene);
        handleMouse(scene);

        scene.setCamera(camera);

        primaryStage.setTitle("Molecule Sample Application");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void buildCamera() {
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-CAMERA_DISTANCE);
        cameraXform.ry.setAngle(320.0);
        cameraXform.rx.setAngle(40);
    }

    private void buildAxes() {
        out.println("buildAxes()");
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(240.0, 1, 1);
        final Box yAxis = new Box(1, 240.0, 1);
        final Box zAxis = new Box(1, 1, 240.0);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        world.getChildren().addAll(axisGroup);
    }

    private void buildMolecule() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        Sphere oxygenSphere = new Sphere(40.0);
        oxygenSphere.setMaterial(redMaterial);

        Sphere hydrogen1Sphere = new Sphere(30.0);
        hydrogen1Sphere.setMaterial(whiteMaterial);

        Sphere hydrogen2Sphere = new Sphere(30.0);
        hydrogen2Sphere.setMaterial(whiteMaterial);

        Cylinder bond1Cylinder = new Cylinder(5, 100);
        bond1Cylinder.setMaterial(greyMaterial);
        bond1Cylinder.setTranslateX(50.0);
        bond1Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond1Cylinder.setRotate(90.0);

        Cylinder bond2Cylinder = new Cylinder(5, 100);
        bond2Cylinder.setMaterial(greyMaterial);
        bond2Cylinder.setTranslateX(50.0);
        bond2Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond2Cylinder.setRotate(90.0);

        Xform hydrogen2Xform = new Xform(hydrogen2Sphere);
        hydrogen2Xform.setTx(100.0);

        Xform hydrogen2SideXform = new Xform(hydrogen2Xform, bond2Cylinder);
        hydrogen2SideXform.setRotateY(104.5);

        Xform hydrogen1Xform = new Xform(hydrogen1Sphere);
        hydrogen1Xform.setTx(100.0);

        Group hydrogen1SideXform = new Group(hydrogen1Xform, bond1Cylinder);
        Group moleculeXform = new Group(oxygenSphere, hydrogen1SideXform, hydrogen2SideXform);

        moleculeGroup.getChildren().add(moleculeXform);
        world.getChildren().addAll(moleculeGroup);
    }
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        setProperty("prism.dirtyopts", "false");
        launch(args);
    }

    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 0.1;
    private static final double ALT_MULTIPLIER = 0.5;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    private void handleMouse(Scene scene) {
        scene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 1.0;
            double modifierFactor = 0.1;

            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 10.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX*modifierFactor*modifier*2.0);  // +
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY*modifierFactor*modifier*2.0);  // -
            }
            else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX*modifierFactor*modifier;
                camera.setTranslateZ(newZ);
            }
            else if (me.isMiddleButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX*modifierFactor*modifier*0.3);  // -
                cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY*modifierFactor*modifier*0.3);  // -
            }
        });
    }

    private void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(this::keyPressedAction);
    }

    private void keyPressedAction(KeyEvent event) {
        switch (event.getCode()) {
            case Z:
                if (event.isShiftDown()) {
                    cameraXform.ry.setAngle(0.0);
                    cameraXform.rx.setAngle(0.0);
                    camera.setTranslateZ(-300.0);
                }
                cameraXform2.t.setX(0.0);
                cameraXform2.t.setY(0.0);
                break;
            case X:
                if (event.isControlDown()) {
                    if (axisGroup.isVisible()) {
                        out.println("setVisible(false)");
                        axisGroup.setVisible(false);
                    } else {
                        out.println("setVisible(true)");
                        axisGroup.setVisible(true);
                    }
                }
                break;
            case S:
                if (event.isControlDown()) {
                    moleculeGroup.setVisible(!moleculeGroup.isVisible());
                }
                break;
            case UP:
                if (event.isControlDown() && event.isShiftDown()) {
                    cameraXform2.t.setY(cameraXform2.t.getY() - 10.0 * CONTROL_MULTIPLIER);
                } else if (event.isAltDown() && event.isShiftDown()) {
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 10.0 * ALT_MULTIPLIER);
                } else if (event.isControlDown()) {
                    cameraXform2.t.setY(cameraXform2.t.getY() - CONTROL_MULTIPLIER);
                } else if (event.isAltDown()) {
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 2.0 * ALT_MULTIPLIER);
                } else if (event.isShiftDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z + 5.0 * SHIFT_MULTIPLIER;
                    camera.setTranslateZ(newZ);
                }
                break;
            case DOWN:
                if (event.isControlDown() && event.isShiftDown()) {
                    cameraXform2.t.setY(cameraXform2.t.getY() + 10.0 * CONTROL_MULTIPLIER);
                } else if (event.isAltDown() && event.isShiftDown()) {
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 10.0 * ALT_MULTIPLIER);
                } else if (event.isControlDown()) {
                    cameraXform2.t.setY(cameraXform2.t.getY() + CONTROL_MULTIPLIER);
                } else if (event.isAltDown()) {
                    cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 2.0 * ALT_MULTIPLIER);
                } else if (event.isShiftDown()) {
                    double z = camera.getTranslateZ();
                    double newZ = z - 5.0 * SHIFT_MULTIPLIER;
                    camera.setTranslateZ(newZ);
                }
                break;
            case RIGHT:
                if (event.isControlDown() && event.isShiftDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + 10.0 * CONTROL_MULTIPLIER);
                } else if (event.isAltDown() && event.isShiftDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 10.0 * ALT_MULTIPLIER);
                } else if (event.isControlDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() + CONTROL_MULTIPLIER);
                } else if (event.isAltDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 2.0 * ALT_MULTIPLIER);
                }
                break;
            case LEFT:
                if (event.isControlDown() && event.isShiftDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() - 10.0 * CONTROL_MULTIPLIER);
                } else if (event.isAltDown() && event.isShiftDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 10.0 * ALT_MULTIPLIER);  // -
                } else if (event.isControlDown()) {
                    cameraXform2.t.setX(cameraXform2.t.getX() - CONTROL_MULTIPLIER);
                } else if (event.isAltDown()) {
                    cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 2.0 * ALT_MULTIPLIER);  // -
                }
                break;
        }
    }
}
