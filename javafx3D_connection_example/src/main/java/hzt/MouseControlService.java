package hzt;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;

public class MouseControlService {

    private double mouseAnchorX;
    private double mouseAnchorY;

    private double targetTranslateAnchorX = 0;
    private double targetTranslateAnchorY = 0;

    private double targetAngleAnchorX = 0;
    private double targetAngleAnchorY = 0;

    private double targetShearAnchor = 0;
    private double targetScaleAnchor = 0;

    private final Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    private final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    private final Shear shear = new Shear();
    private final Scale scale = new Scale();

    private Node target;

    public MouseControlService(Node target) {
        this();
        this.target = target;
    }

    public MouseControlService() {
        super();
    }

    public void initMouseControl(Node target, Node reference) {
        this.target = target;
        target.getTransforms().addAll(xRotate, yRotate, shear, scale);

        reference.setOnMousePressed(this::mousePressedEvent);
        reference.setOnMouseDragged(this::mouseDraggedEvent);
        reference.setOnMouseReleased(event -> target.setMouseTransparent(false));
        reference.addEventHandler(ScrollEvent.SCROLL, this::mouseScrollEvent);
    }

    private void mousePressedEvent(MouseEvent event) {
        mouseAnchorX = event.getSceneX();
        mouseAnchorY = event.getSceneY();
        targetShearAnchor = shear.getX();
        targetScaleAnchor = scale.getX();
        targetAngleAnchorX = xRotate.getAngle();
        targetAngleAnchorY = yRotate.getAngle();
        targetTranslateAnchorX = target.getTranslateX();
        targetTranslateAnchorY = target.getTranslateY();
    }

    private void mouseDraggedEvent(MouseEvent event) {
        if (!target.isPressed()) {
            double deltaX = mouseAnchorX - event.getSceneX();
            double deltaY = mouseAnchorY - event.getSceneY();
            if (event.isControlDown()) {
                shear.setX(targetShearAnchor - (deltaX * .01));
            } else if (event.isAltDown()) {
                double newScale = targetScaleAnchor - (deltaY * .01);
                scale.setX(newScale);
                scale.setY(newScale);
                scale.setZ(newScale);
            } else if (event.isSecondaryButtonDown() || event.isMiddleButtonDown()) {
                xRotate.setAngle(targetAngleAnchorX - deltaY);
                yRotate.setAngle(targetAngleAnchorY + deltaX);
            } else if (event.isPrimaryButtonDown()) {
                target.setTranslateX(targetTranslateAnchorX - deltaX);
                target.setTranslateY(targetTranslateAnchorY - deltaY);
            }
        }
    }

    private void mouseScrollEvent(ScrollEvent event) {
        double delta = event.getDeltaY();
        target.setTranslateZ(target.getTranslateZ() - delta);
    }

    public void setOrientation(double angleX, double angleY) {
        xRotate.setAngle(angleX);
        yRotate.setAngle(angleY);
    }

    public void setTargetTranslation(Point3D point3D) {
        target.setTranslateX(point3D.getX());
        target.setTranslateY(point3D.getY());
        target.setTranslateZ(point3D.getZ());
    }

    public double getTargetTranslateX() {
        return target.getTranslateX();
    }

    public double getTargetTranslateY() {
        return target.getTranslateY();
    }

    public double getTargetTranslateZ() {
        return target.getTranslateZ();
    }

    public void resetShear() {
        shear.setX(0);
        shear.setY(0);
    }

    public void resetScale() {
        scale.setX(1);
        scale.setY(1);
        scale.setZ(1);
    }

    public void resetTranslation() {
        target.setTranslateX(0);
        target.setTranslateY(0);
        target.setTranslateZ(0);
    }
}
