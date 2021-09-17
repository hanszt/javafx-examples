package hzt;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Material;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import org.jetbrains.annotations.NotNull;

/**
 * @author Hans Zuidervaart
 * An attempt at a 3d visible vector
 */
public class Line3D extends Group {

    private static final int RIGHT_ANGLE = 90;

    private final Cylinder cylinder = new Cylinder();
    private final Rotate cylinderRotateX = new Rotate();
    private final Rotate rotateZ = new Rotate();

    private final DoubleProperty startX = new SimpleDoubleProperty();
    private final DoubleProperty startY = new SimpleDoubleProperty();
    private final DoubleProperty startZ = new SimpleDoubleProperty();

    private final DoubleProperty endX = new SimpleDoubleProperty();
    private final DoubleProperty endY = new SimpleDoubleProperty();
    private final DoubleProperty endZ = new SimpleDoubleProperty();

    public Line3D() {
        super();
        cylinderRotateX.setAxis(Rotate.X_AXIS);
        rotateZ.setAxis(Rotate.Z_AXIS);
        getTransforms().add(rotateZ);
        cylinder.getTransforms().add(cylinderRotateX);
        bindPositionBasedOnStartAndEnd();
        bindRotationAndLengthBasedOnStartAndEnd();
        super.getChildren().add(cylinder);
    }

    private void bindRotationAndLengthBasedOnStartAndEnd() {
        DoubleProperty xDistance = bindOperation(startX.subtract(endX));
        DoubleProperty yDistance = bindOperation(startY.subtract(endY));
        DoubleProperty zDistance = bindOperation(startZ.subtract(endZ));
        DoubleProperty xDistanceSquared = bindOperation(xDistance.multiply(xDistance));
        DoubleProperty yDistanceSquared = bindOperation(yDistance.multiply(yDistance));
        DoubleProperty zDistanceSquared = bindOperation(zDistance.multiply(zDistance));
        DoubleProperty xDistance1Squared = bindOperation(xDistanceSquared.add(yDistanceSquared));
        DoubleProperty lengthSquared = bindOperation(xDistanceSquared.add(yDistanceSquared.add(zDistanceSquared)));
        lengthSquared.addListener(this::updateHeightIfNotBound);
        xDistance.addListener((o, c, n) -> rotate(xDistance, yDistance, zDistance, xDistance1Squared));
        yDistance.addListener((o, c, n) -> rotate(xDistance, yDistance, zDistance, xDistance1Squared));
        zDistance.addListener((o, c, n) -> rotateCylinderX(zDistance, xDistance1Squared));
    }

    private void rotateCylinderX(DoubleProperty zDistance, DoubleProperty xDistance1Squared) {
        double xDistance1 = Math.sqrt(xDistance1Squared.get());
        rotate(zDistance.get(), endX.get() - startX.get() > 0 ? -xDistance1 : xDistance1, cylinderRotateX);
    }

    @NotNull
    private static DoubleProperty bindOperation(DoubleBinding doubleBinding) {
        DoubleProperty doubleProperty = new SimpleDoubleProperty();
        doubleProperty.bind(doubleBinding);
        return doubleProperty;
    }

    private void rotate(DoubleProperty xDistance, DoubleProperty yDistance, DoubleProperty zDistance, DoubleProperty xDistance1Squared) {
        double angle = Math.toDegrees(Math.atan(yDistance.get() / xDistance.get()));
        rotateZ.setAngle(angle + RIGHT_ANGLE);
        rotateCylinderX(zDistance, xDistance1Squared);
    }

    private static void rotate(double distance1, double distance2, Rotate rotate) {
        double angle = Math.toDegrees(Math.atan(distance2 / distance1));
        rotate.setAngle(angle + RIGHT_ANGLE);
    }

    private void bindPositionBasedOnStartAndEnd() {
        DoubleProperty centerX = centerBasedOnStartAndEnd(startX, endX);
        DoubleProperty centerY = centerBasedOnStartAndEnd(startY, endY);
        DoubleProperty centerZ = centerBasedOnStartAndEnd(startZ, endZ);
        translateXProperty().bind(centerX);
        translateYProperty().bind(centerY);
        translateZProperty().bind(centerZ);
    }

    private static DoubleProperty centerBasedOnStartAndEnd(DoubleProperty start, DoubleProperty end) {
        DoubleProperty center = new SimpleDoubleProperty();
        center.bind(bindOperation(start.add(end)).divide(2));
        return center;
    }

    public void defineStartAndEndpoint(Node startNode, Node endNode) {
        startX.bind(startNode.translateXProperty());
        startY.bind(startNode.translateYProperty());
        startZ.bind(startNode.translateZProperty());

        endX.bind(endNode.translateXProperty());
        endY.bind(endNode.translateYProperty());
        endZ.bind(endNode.translateZProperty());
    }

    public DoubleProperty widthProperty() {
        return cylinder.radiusProperty();
    }

    public DoubleProperty startXProperty() {
        return startX;
    }

    public DoubleProperty startYProperty() {
        return startY;
    }

    public DoubleProperty startZProperty() {
        return startZ;
    }

    public ObjectProperty<Material> materialProperty() {
        return cylinder.materialProperty();
    }

    public void setStart(Point3D start) {
        startX.set(start.getX());
        startY.set(start.getY());
        startZ.set(start.getZ());
    }

    public void setEnd(Point3D end) {
        endX.set(end.getX());
        endY.set(end.getY());
        endZ.set(end.getZ());
    }

    private void updateHeightIfNotBound(ObservableValue<? extends Number> o, Number c, Number n) {
        if (!cylinder.heightProperty().isBound()) {
            cylinder.setHeight(Math.sqrt(n.doubleValue()));
        }
    }
}
