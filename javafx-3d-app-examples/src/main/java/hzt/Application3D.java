package hzt;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Window;

public class Application3D {

    public static final float WIDTH = 1400;
    public static final float HEIGHT = 800;

    private double anchorX;
    private double anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    public Application3D() {
        super();
    }

    public void initMouseControl(Node node, Scene scene) {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        node.getTransforms().addAll(xRotate ,yRotate);

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            node.translateZProperty().set(node.getTranslateZ() + delta);
        });
    }

    public void initKeyControl(SmartGroup group, Window window) {
        window.addEventHandler(KeyEvent.KEY_PRESSED, event -> keyPressedAction(group, event));
    }

    private static void keyPressedAction(SmartGroup group, KeyEvent event) {
        switch (event.getCode()) {
            case W -> group.translateZProperty().set(group.getTranslateZ() + 100);
            case S -> group.translateZProperty().set(group.getTranslateZ() - 100);
            case Q -> group.rotateByX(10);
            case E -> group.rotateByX(-10);
            case NUMPAD6 -> group.rotateByY(10);
            case NUMPAD4 -> group.rotateByY(-10);
            default -> {
                //Do nothing
            }
        }
    }

    public static class SmartGroup extends Group {

        private Rotate r;
        private Transform t = new Rotate();

        public SmartGroup() {
            super();
        }

        public void rotateByX(int ang) {
            r = new Rotate(ang, Rotate.X_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }

        public void rotateByY(int ang) {
            r = new Rotate(ang, Rotate.Y_AXIS);
            t = t.createConcatenation(r);
            this.getTransforms().clear();
            this.getTransforms().addAll(t);
        }
    }
}
