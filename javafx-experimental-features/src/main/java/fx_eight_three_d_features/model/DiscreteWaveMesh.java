package fx_eight_three_d_features.model;

import javafx.geometry.Point2D;

public class DiscreteWaveMesh extends PlaneZMesh {

    @Override
    protected float computeZValue(Point2D midPoint) {
        double radius = midPoint.magnitude();
        radius = Math.max(radius, 1);
        float amplitude = ((int) (radius * frequencyProperty().get())) % 2 == 0 ? (float) (amplitudeProperty().get()) : 0;
        return (float) (amplitude * computeDampening(radius));
    }
}
