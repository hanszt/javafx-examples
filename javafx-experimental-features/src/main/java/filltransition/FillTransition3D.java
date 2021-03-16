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

    private int cycleCount = DEFAULT_CYCLE_COUNT;
    private final Timeline timeline = new Timeline();
    private final Shape3D shape3D;

    private ObjectProperty<Color> fromColor = new SimpleObjectProperty<>(Color.WHITE);
    private ObjectProperty<Color> toColor = new SimpleObjectProperty<>(Color.WHITE);

    public FillTransition3D(Shape3D shape3D) {
        this.shape3D = shape3D;
        timeline.setCycleCount(cycleCount);
        Duration duration = Duration.millis(5);
        timeline.getKeyFrames().add(new KeyFrame(duration, "", this::changeColor));
    }

    private int curCount = 0;

    private void changeColor(ActionEvent actionEvent) {
        ((PhongMaterial) shape3D.getMaterial()).setDiffuseColor(interpolatedColor(curCount, cycleCount));
        curCount++;
    }

    private Color interpolatedColor(int curCount, int cycleCount) {
        double lerp = (double) curCount / cycleCount;
        double deltaR = fromColor.get().getRed() - toColor.get().getRed();
        double deltaG = fromColor.get().getGreen() - toColor.get().getGreen();
        double deltaB = fromColor.get().getBlue() - toColor.get().getBlue();
        double curR = fromColor.get().getRed() - deltaR * lerp;
        double curG = fromColor.get().getGreen() - deltaG * lerp;
        double curB = fromColor.get().getBlue() - deltaB * lerp;
        return Color.color(curR, curG, curB);
    }

    private void play() {
        curCount = 0;
        timeline.play();
    }

    public void setFromColor(Color fromColor) {
        this.fromColor.set(fromColor);
    }

    public void setToColor(Color toColor) {
        this.toColor.set(toColor);
    }
}
