package hzt.animations;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.FxUtils;

import java.util.Timer;
import java.util.TimerTask;

public class AsciiSimSample extends Application {

    private static final String TEST_STRING = "Hello, this is a test for making ascii animations! " +
            "You can also use non ascii characters ☺...";

    final TextArea textArea = new TextArea();
    private final AnimationTimer animationTimer = FxUtils.animationTimer(this::update);
    private int counter = 0;

    @Override
    public void start(Stage stage) {
        final var root = new VBox();
        root.getChildren().add(textArea);
        animationTimer.start();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(getClass().getSimpleName());
        stage.show();
        stage.setOnCloseRequest(e -> Runtime.getRuntime().exit(0));
    }

    private void update() {
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        textArea.setText(TEST_STRING.substring(0, counter));
        if (counter >= TEST_STRING.length()) {
            counter = 0;
            animationTimer.stop();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    animationTimer.start();
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 1000L);
        } else {
            counter++;
        }
    }
}
