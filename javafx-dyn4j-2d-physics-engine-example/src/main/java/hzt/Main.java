package hzt;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Random;

public class Main extends Application {

    private static final double SCENE_WIDTH = 800;
    private static final double SCENE_HEIGHT = 600;

    private static final Random random = new Random();
    public static final int NR_OF_OBJECTS = 172;

    private static double rnd(double from, double to) {
        return to + (from - to) * random.nextDouble();
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();

        // the scale and translate mean 0,0 is in the centre of the screen
        // at the bottom with height increasing up the screen
        SimulationPane mainPane = new SimulationPane();
        Translate translate = new Translate(SCENE_WIDTH / 2, -SCENE_HEIGHT);
        mainPane.getTransforms().addAll(new Scale(1, -1), translate);

        root.getChildren().add(mainPane);

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();

        Image image = Optional.ofNullable(getClass().getResourceAsStream("/img/smile.png"))
                .map(Image::new)
                .orElseThrow();

        // create the floor
        Rectangle floorRect = new Rectangle(15.0, 1.0);
        PhysicsImageView floor = new PhysicsImageView(); // invisible no image...
        final var body = floor.getBody();
        body.addFixture(new BodyFixture(floorRect));
        body.setMass(MassType.INFINITE);
        // move the floor down a bit
        body.translate(0.0, -1.0);
        mainPane.addBody(floor);

        Rectangle rectangle = new Rectangle(1.0, 1.0);

        for (int i = 0; i < NR_OF_OBJECTS; i++) {
            final var physicsObject = createImageView(image, rectangle);
            mainPane.addBody(physicsObject);
        }
        createGameLoop(mainPane).start();
    }

    @NotNull
    private static AnimationTimer createGameLoop(SimulationPane mainPane) {
        return new AnimationTimer() {

            private long last;

            @Override
            public void handle(long now) { // now is in nanoseconds
                float delta = 1F / (1000.0F / ((now - last) / 1_000_000F));  // seems long-winded but avoids precision issues
                mainPane.update(delta);
                last = now;
            }
        };
    }

    @NotNull
    private static PhysicsImageView createImageView(Image image, Rectangle rectShape) {
        PhysicsImageView rectangle = new PhysicsImageView(image);
        BodyFixture fixture = new BodyFixture(rectShape);
        fixture.setDensity(1.2);
        fixture.setFriction(0.8);
        fixture.setRestitution(0.4);
        final var body = rectangle.getBody();
        body.addFixture(fixture);
        body.setMass(MassType.NORMAL);
        body.translate(rnd(-3, 3), 9.0 + rnd(-4, 2));
        body.getTransform().setRotation(rnd(-Math.PI, Math.PI));
        return rectangle;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
