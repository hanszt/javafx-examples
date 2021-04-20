package hzt;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import org.jetbrains.annotations.NotNull;

import static java.lang.System.*;

public class Connection extends Cylinder {

    private final Rotate rotateX = new Rotate();
    private final Rotate rotateY = new Rotate();
    private final Rotate rotateZ = new Rotate();

    private final DoubleProperty startX = new SimpleDoubleProperty();
    private final DoubleProperty startY = new SimpleDoubleProperty();
    private final DoubleProperty startZ = new SimpleDoubleProperty();

    private final DoubleProperty endX = new SimpleDoubleProperty();
    private final DoubleProperty endY = new SimpleDoubleProperty();
    private final DoubleProperty endZ = new SimpleDoubleProperty();

    public Connection() {
        super();
        rotateX.setAxis(Rotate.X_AXIS);
        rotateY.setAxis(Rotate.Y_AXIS);
        rotateZ.setAxis(Rotate.Z_AXIS);
        getTransforms().addAll(rotateZ, rotateY, rotateX);
        bindPositionBasedOnStartAndEnd();
        bindRotationAndLengthBasedOnStartAndEnd();
    }

    private void bindRotationAndLengthBasedOnStartAndEnd() {
        DoubleProperty xDistance = bindOperation(startX.subtract(endX));
        DoubleProperty yDistance = bindOperation(startY.subtract(endY));
        DoubleProperty zDistance = bindOperation(startZ.subtract(endZ));
        DoubleProperty xDistanceSquared = bindOperation(xDistance.multiply(xDistance));
        DoubleProperty yDistanceSquared = bindOperation(yDistance.multiply(yDistance));
        DoubleProperty zDistanceSquared = bindOperation(zDistance.multiply(zDistance));
        DoubleProperty lengthSquared = bindOperation(xDistanceSquared.add(yDistanceSquared.add(zDistanceSquared)));
        lengthSquared.addListener((observableValue, number, t1) -> setHeight(Math.sqrt(lengthSquared.get())));
        endX.addListener((o, c, n) -> rotate(xDistance, yDistance, rotateZ));
        endY.addListener((o, c, n) -> rotate(xDistance, yDistance, rotateZ));
        endX.addListener((o, c, n) -> rotate(xDistance, zDistance, rotateY));
        endZ.addListener((o, c, n) -> rotate(xDistance, zDistance, rotateY));
    }

    @NotNull
    private DoubleProperty bindOperation(DoubleBinding doubleBinding) {
        DoubleProperty doubleProperty = new SimpleDoubleProperty();
        doubleProperty.bind(doubleBinding);
        return doubleProperty;
    }

    private static final int RIGHT_ANGLE = 90;

    private void rotate(DoubleProperty distance1, DoubleProperty distance2, Rotate rotate) {
//        out.println("distance 1: " + distance1);
//        out.println("distance 2: " + distance2);
        double angle = Math.toDegrees(Math.atan(distance2.get() / distance1.get()));
//        out.println("angle: " + angle);
//        out.println();
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

    private DoubleProperty centerBasedOnStartAndEnd(DoubleProperty start, DoubleProperty end) {
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

}
