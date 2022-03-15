package hzt;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.dyn4j.dynamics.Body;

public final class PhysicsImageView extends ImageView {

	private final Body body = new Body();

	public PhysicsImageView() {
		this(null);
	}

	public PhysicsImageView(Image image) {
		super();
		setImage(image);
	}

	public Body getBody() {
		return body;
	}
}
