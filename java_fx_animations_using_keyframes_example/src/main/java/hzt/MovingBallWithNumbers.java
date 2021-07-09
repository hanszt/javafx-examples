package hzt;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MovingBallWithNumbers extends Application {

    //variable for storing actual frame
    private int i = 0;

    @Override
    public void start(Stage stage) {
        //create a circle with effect
        final var circle = new Circle(20, Color.rgb(156, 216, 255));
        circle.setEffect(new Lighting());
        //create a text inside a circle
        final var text = new Text("" + i);
        text.setStroke(Color.BLACK);
        //create a layout for circle with text inside
        final var stackPane = new StackPane(circle, text);
        stackPane.setLayoutX(30);
        stackPane.setLayoutY(30);

        //You can add a specific action when each frame is started.
        var timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                text.setText("" + i);
                i++;
            }
        };
        timer.start();

        //create a keyValue with factory: scaling the circle 2times
        var keyValueX = new KeyValue(stackPane.scaleXProperty(), 2);
        var keyValueY = new KeyValue(stackPane.scaleYProperty(), 2);

        var duration = Duration.seconds(2);
        //one can add a specific action when the keyframe is reached
        var keyFrame = new KeyFrame(duration, t -> onAnimationFinished(stackPane), keyValueX, keyValueY);

        var timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        var group = new Group(stackPane);
        group.setTranslateX(80);
        group.setTranslateY(80);

        var scene = new Scene(group);

        stage.setScene(scene);
        stage.setWidth(500);
        stage.setHeight(500);
        stage.show();
    }

    private void onAnimationFinished(StackPane stackPane) {
        stackPane.setTranslateX(Math.random() * 200 - 100);
        //reset counter
        i = 0;
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
