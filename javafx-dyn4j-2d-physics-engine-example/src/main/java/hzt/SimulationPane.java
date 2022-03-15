package hzt;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.geometry.Transform;
import org.dyn4j.world.World;

public final class SimulationPane extends Pane {

    /** The scale 64 pixels per meter - where 1m = 1 sprite width */
    private static final double SCALE = 64;

    private final World<PhysicsBody> world = new World<>();

    public void addBody(PhysicsImageView imageView) {
        getChildren().add(imageView);
        world.addBody(imageView.getBody());
    }

    public void update(float delta) {
        world.update(delta);
        for (Node node : getChildren()) {
            // only attempt to update if it has a visual
            if (node instanceof PhysicsImageView imageView && imageView.getImage() != null) {
                Transform transform = imageView.getBody().getTransform();
                imageView.setTranslateX(transform.getTranslationX() * SCALE);
                imageView.setTranslateY(transform.getTranslationY() * SCALE);
                imageView.setRotate(transform.getRotation().getCost() / 0.017453292519943295);
            }
        }
    }
}
