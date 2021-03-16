package fx_eight_three_d_features.model;

import javafx.geometry.Point2D;

public class WaveMesh extends PlaneZMesh {

    protected float computeZValue(Point2D point2D) {
        double radius = point2D.magnitude();
        radius = Math.max(radius, .1);
        return (float) (amplitudeProperty().get() * (Math.sin(radius * frequencyProperty().get()) * computeDampening(radius)));
    }


}
