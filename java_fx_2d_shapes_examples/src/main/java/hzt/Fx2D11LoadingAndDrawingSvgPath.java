package hzt;

import hzt.model.BufferedImageTranscoder;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;

import java.io.IOException;
import java.io.InputStream;

public class Fx2D11LoadingAndDrawingSvgPath extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        ImageView arrow = new ImageView();
        ImageView circles = new ImageView();
        setImageViewWithSvg(arrow, "../svg/arrow.svg");
        setImageViewWithSvg(circles, "../svg/circles.svg");
        arrow.setFitHeight(100);
        arrow.setPreserveRatio(true);

        double scale = 2;
        SVGPath githubIcon = new SVGPath();
        githubIcon.setContent("m12 .5c-6.63 0-12 5.28-12 11.792 0 5.211 3.438 9.63 8.205 11.188.6.111.82-.254.82-.567 " +
                "0-.28-.01-1.022-.015-2.005-3.338.711-4.042-1.582-4.042-1.582-.546-1.361-1.335-1.725-1.335-1.725-1.087-.731.084-.716.084-.716 " +
                "1.205.082 1.838 1.215 1.838 1.215 1.07 1.803 2.809 " +
                "1.282 3.495.981.108-.763.417-1.282.76-1.577-2.665-.295-5.466-1.309-5.466-5.827 " +
                "0-1.287.465-2.339 1.235-3.164-.135-.298-.54-1.497.105-3.121 0 0 1.005-.316 3.3 1.209.96-.262 1.98-.392 " +
                "3-.398 1.02.006 2.04.136 3 .398 2.28-1.525 3.285-1.209 3.285-1.209.645 1.624.24 2.823.12 3.121.765.825 " +
                "1.23 1.877 1.23 3.164 0 4.53-2.805 5.527-5.475 5.817.42.354.81 1.077.81 2.182 0 1.578-.015 2.846-.015 " +
                "3.229 0 .309.21.678.825.56 4.801-1.548 8.236-5.97 8.236-11.173 0-6.512-5.373-11.792-12-11.792z");
        githubIcon.setFill(Color.web("#81c483"));
        githubIcon.setFill(Color.color(1, 0, 0));
        githubIcon.setScaleX(scale);
        githubIcon.setScaleY(scale);

        // Create the HBox
        HBox root = new HBox();
        // Add the Children to the HBox
        root.getChildren().addAll(arrow, circles, githubIcon);
        // Set Spacing of the HBox
        root.setSpacing(10);

        // Set the Style of the HBox
        root.setStyle
                (
                        "-fx-padding: 10;" +
                                "-fx-border-style: solid inside;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-insets: 5;" +
                                "-fx-border-radius: 5;" +
                                "-fx-border-color: blue;"
                );

        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title of the Stage
        stage.setTitle("A JavaFX Svg path Example");
        // Display the Stage
        stage.show();
    }

    private void setImageViewWithSvg(ImageView imageView, String path) {
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            extractImage(imageView, inputStream);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private void extractImage(ImageView imageView, InputStream inputStream) {
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        TranscoderInput transIn = new TranscoderInput(inputStream);
        try {
            transcoder.transcode(transIn, null);
            Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
            imageView.setImage(img);
        } catch (TranscoderException ex) {
            ex.printStackTrace();
        }
    }
}
