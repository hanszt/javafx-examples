package filltransition;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.util.Duration;

public class FillTransition3D {

    private static final int DEFAULT_CYCLE_COUNT = 100;
    private final Timeline timeline = new Timeline();
    private final Shape3D shape3D;

    private final ObjectProperty<Color> fromColor = new SimpleObjectProperty<>(Color.WHITE);
    private final ObjectProperty<Color> toColor = new SimpleObjectProperty<>(Color.WHITE);

    private int curCount = 1;

    public FillTransition3D(Shape3D shape3D, Duration duration) {
        this.shape3D = shape3D;
        timeline.setCycleCount(DEFAULT_CYCLE_COUNT);
        timeline.getKeyFrames().add(new KeyFrame(duration.divide(DEFAULT_CYCLE_COUNT), "", this::changeColor));
    }

    private void changeColor(ActionEvent actionEvent) {
        ((PhongMaterial) shape3D.getMaterial()).setDiffuseColor(interpolatedColor(curCount));
        curCount++;
    }

    private Color interpolatedColor(final int curCount) {
        final double lerp = (double) curCount / DEFAULT_CYCLE_COUNT;
        return fromColor.get().interpolate(toColor.get(), lerp);
    }

    void play() {
        curCount = 1;
        timeline.play();
    }

    public void setFromColor(Color fromColor) {
        this.fromColor.set(fromColor);
    }

    public void setToColor(Color toColor) {
        this.toColor.set(toColor);
    }
}
