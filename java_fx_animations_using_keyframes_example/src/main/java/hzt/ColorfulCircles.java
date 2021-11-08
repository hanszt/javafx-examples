package hzt;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.random;

public class ColorfulCircles extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static Circle toConfiguredCircle(int i) {
        var circle = new Circle(150, Color.web("white", 0.05));
        circle.setStrokeType(StrokeType.OUTSIDE);
        circle.setStroke(Color.web("white", 0.16));
        circle.setStrokeWidth(4);
        return circle;
    }

    @Override
    public void start(Stage primaryStage) {
        var root = new Group();
        var scene = new Scene(root, 800, 600, Color.BLACK);
        scene.setOnMouseClicked(e -> primaryStage.close());

        var colorsRectangle = new Rectangle(scene.getWidth(), scene.getHeight(), getPaint());
        colorsRectangle.setBlendMode(BlendMode.OVERLAY);

        var circles = new Group(buildCircles());
        circles.setEffect(new BoxBlur(10, 10, 3));

        final var group = new Group(new Rectangle(scene.getWidth(), scene.getHeight(), Color.BLACK), circles);
        var blendModeGroup = new Group(group, colorsRectangle);
        root.getChildren().add(blendModeGroup);
        setupCircleMovementTimeline(circles);

        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    private static void setupCircleMovementTimeline(Group circles) {
        var timeline = new Timeline();
        circles.getChildren().forEach(circle -> addCircleTranslationKeyFramesToTimeline(timeline, circle));
        // play 40s of animation
        timeline.play();
    }

    @NotNull
    private static Collection<Node> buildCircles() {
        return IntStream.range(0, 30)
                .mapToObj(ColorfulCircles::toConfiguredCircle)
                .collect(Collectors.toUnmodifiableSet());
    }

    @NotNull
    private static LinearGradient getPaint() {
        return new LinearGradient(0, 1, 1, 0, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#f8bd55")),
                new Stop(0.14, Color.web("#c0fe56")),
                new Stop(0.28, Color.web("#5dfbc1")),
                new Stop(0.43, Color.web("#64c2f8")),
                new Stop(0.57, Color.web("#be4af7")),
                new Stop(0.71, Color.web("#ed5fc2")),
                new Stop(0.85, Color.web("#ef504c")),
                new Stop(1, Color.web("#f2660f")));
    }

    private static void addCircleTranslationKeyFramesToTimeline(Timeline timeline, Node circle) {
        timeline.getKeyFrames().addAll(
                // set start position at 0
                new KeyFrame(Duration.ZERO,
                        new KeyValue(circle.translateXProperty(), random() * 800),
                        new KeyValue(circle.translateYProperty(), random() * 600)),
                // set end position at 40s
                new KeyFrame(Duration.seconds(40),
                        new KeyValue(circle.translateXProperty(), random() * 800),
                        new KeyValue(circle.translateYProperty(), random() * 600)));
    }
}
