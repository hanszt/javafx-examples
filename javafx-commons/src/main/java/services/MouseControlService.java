package services;

import interfaces.IMouseControlService;
import interfaces.MouseTarget;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;

public class MouseControlService implements IMouseControlService {

    private double mouseAnchorX;
    private double mouseAnchorY;

    private double targetTranslateAnchorX = 0;
    private double targetTranslateAnchorY = 0;

    private double targetAngleAnchorX = 0;
    private double targetAngleAnchorY = 0;

    private double targetShearAnchor = 0;
    private double targetScaleAnchor = 0;

    private final BooleanProperty moveWhenTargetPressedProperty = new SimpleBooleanProperty(true);
    private final DoubleProperty translationFactorProperty = new SimpleDoubleProperty(1);

    private final Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
    private final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    private final Shear shear = new Shear();
    private final Scale scale = new Scale();

    private Node target;

    public MouseControlService() {
        super();
    }

    public void initMouseControl(Node target, MouseTarget reference) {
        this.target = target;
        target.getTransforms().addAll(xRotate, yRotate, shear, scale);

        reference.setOnMousePressed(this::mousePressedEvent);
        reference.setOnMouseDragged(this::mouseDraggedEvent);
        reference.setOnMouseReleased(event -> target.setMouseTransparent(false));
        reference.setOnScroll(this::mouseScrollEvent);
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
        if (!target.isPressed() || moveWhenTargetPressedProperty.get()) {
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
                target.setTranslateX(targetTranslateAnchorX - (deltaX * translationFactorProperty.get()));
                target.setTranslateY(targetTranslateAnchorY - (deltaY * translationFactorProperty.get()));
            } else {
                // do nothing...
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
        setTargetTranslation(point3D.getX(), point3D.getY(), point3D.getZ());
    }

    public void setTargetTranslation(double x, double y, double z) {
        target.setTranslateX(x);
        target.setTranslateY(y);
        target.setTranslateZ(z);
    }

    public void setShear(double shearX, double shearY) {
        shear.setX(shearX);
        shear.setY(shearY);
    }

    public boolean isMoveWhenTargetPressedProperty() {
        return moveWhenTargetPressedProperty.get();
    }

    public BooleanProperty moveWhenTargetPressedPropertyProperty() {
        return moveWhenTargetPressedProperty;
    }

    public void setMoveWhenTargetPressedProperty(boolean moveWhenTargetPressed) {
        this.moveWhenTargetPressedProperty.set(moveWhenTargetPressed);
    }

    public double getTranslationFactorProperty() {
        return translationFactorProperty.get();
    }

    public DoubleProperty translationFactorPropertyProperty() {
        return translationFactorProperty;
    }

    public void setTranslationFactorProperty(double translationFactorProperty) {
        this.translationFactorProperty.set(translationFactorProperty);
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

    public double getTargetAngleX() {
        return xRotate.getAngle();
    }

    public double getTargetAngleY() {
        return yRotate.getAngle();
    }
}
