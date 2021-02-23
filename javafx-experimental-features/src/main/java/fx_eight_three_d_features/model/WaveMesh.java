package fx_eight_three_d_features.model;

import javafx.geometry.Point2D;

public class WaveMesh extends PlaneZMesh {

    protected float computeZValue(Point2D point2D) {
        double radius = point2D.magnitude();
        return (float) (amplitudeProperty().get() * (radius != 0 ? Math.sin(radius * waveFrequencyProperty().get()) * computeDampening(radius) : 1));
    }


    private double computeDampening(double radius) {
        return (dampeningProperty().get() + (1 - dampeningProperty().get()) * radius) / radius;
    }

}
