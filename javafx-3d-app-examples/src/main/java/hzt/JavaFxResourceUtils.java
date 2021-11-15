package hzt;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public final class JavaFxResourceUtils {

    private static final Logger LOGGER = LogManager.getLogger(JavaFxResourceUtils.class);
    public static final String WOOD_JPG = "/wood.jpg";

    private JavaFxResourceUtils() {
    }

    public static void acceptImageIfPresent(String location, Consumer<Image> imageConsumer) {
        Optional.ofNullable(JavaFxResourceUtils.class.getResourceAsStream(location))
                .ifPresentOrElse(stream -> imageConsumer.accept(new Image(stream)),
                        () -> LOGGER.error("Could not find {}", location));
    }

    public static ImageView toConfiguredImageView(String location) {
        return Optional.ofNullable(JavaFxResourceUtils.class.getResourceAsStream(location))
                .map(Image::new)
                .map(JavaFxResourceUtils::configuredImageView)
                .orElseGet(() -> emptyImageViewAndLog(location));
    }

    @NotNull
    private static ImageView emptyImageViewAndLog(String location) {
        LOGGER.error("Could not find {}", location);
        return new ImageView();
    }

    public static void setEarthMaterialMaps(PhongMaterial earthMaterial) {
        acceptImageIfPresent("/earth/earth-d.jpg", earthMaterial::setDiffuseMap);
        acceptImageIfPresent("/earth/earth-l.jpg", earthMaterial::setSelfIlluminationMap);
        acceptImageIfPresent("/earth/earth-s.jpg", earthMaterial::setSpecularMap);
        acceptImageIfPresent("/earth/earth-n.jpg", earthMaterial::setBumpMap);
    }

    public static ImageView configuredImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.getTransforms().add(new Translate(-image.getWidth() / 2, -image.getHeight() / 2, 800));
        return imageView;
    }

    public static Camera configuredCamera() {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10_000);
        camera.translateZProperty().set(-1000);
        return camera;
    }

    public static Box woodenBox(int width, int height, int depth) {
        PhongMaterial material = new PhongMaterial();
        JavaFxResourceUtils.acceptImageIfPresent(WOOD_JPG, material::setDiffuseMap);
        Box box = new Box(width, height, depth);
        box.setMaterial(material);
        return box;
    }

    public static Box illuminatedWoodenBox(int width, int height, int depth) {
        PhongMaterial material = new PhongMaterial();
        JavaFxResourceUtils.acceptImageIfPresent(WOOD_JPG, material::setDiffuseMap);
        JavaFxResourceUtils.acceptImageIfPresent("/illuminati2.jpg", material::setSelfIlluminationMap);
        Box box = new Box(width, height, depth);
        box.setMaterial(material);
        return box;
    }

    public static Box woodenSpecBox(int width, int height, int depth) {
        PhongMaterial material = new PhongMaterial();
        JavaFxResourceUtils.acceptImageIfPresent(WOOD_JPG, material::setDiffuseMap);
        JavaFxResourceUtils.acceptImageIfPresent("/spec.jpg", material::setSpecularMap);
        Box box = new Box(width, height, depth);
        box.setMaterial(material);
        return box;
    }
}
