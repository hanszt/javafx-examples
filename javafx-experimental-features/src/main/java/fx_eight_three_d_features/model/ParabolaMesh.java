package fx_eight_three_d_features.model;

import javafx.geometry.Point2D;

public class ParabolaMesh extends PlaneZMesh {

    protected float computeZValue(Point2D point2D) {
        double radius = point2D.magnitude();
        return (float) (.1 * dampeningProperty().get() * radius * radius);
    }

}
