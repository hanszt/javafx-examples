/*
 * Copyright (c) 2010, 2014, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package hzt;

import hzt.service.IMouseControlService;
import hzt.service.MouseControlService;
import hzt.utils.FxSetup;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;

import static hzt.utils.PropertiesService.parsedDoubleAppProp;
import static hzt.utils.PropertiesService.parsedIntAppProp;
import static java.lang.System.err;

public class Xylophone extends Application {

    private static final int NR_OF_REQUIRED_AUDIOFILES = 8;
    private static final double INIT_ANGLE_X = parsedDoubleAppProp("init_angle_x", 0);
    private static final double INIT_ANGLE_Y = parsedDoubleAppProp("init_angle_y", 0);

    private static final int MAX_NUMBER_OF_BARS = parsedIntAppProp("max_bar_slider_val", 29);

    private static final int INIT_NUMBER_OF_BARS = parsedIntAppProp("init_nr_of_bars", 8);
    private static final double X_OFFSET_BAR = 30.0;
    private static final double Y_POS_BAR = 0.0;
    private static final double BAR_WIDTH = 22.0;
    private static final double SHORTEST_BAR_LENGTH = 50;
    private static final double LONGEST_BAR_LENGTH = 100;
    private static final double BAR_THICKNESS = 7.0;

    private final Deque<Node> barsDeque = new ArrayDeque<>();
    private final IMouseControlService mouseControlService = new MouseControlService();

    private final IntegerProperty nrOfVisibleBars = new SimpleIntegerProperty();

    @Override
    public void start(final Stage stage) {
        int amount = Math.min(INIT_NUMBER_OF_BARS, MAX_NUMBER_OF_BARS);
        nrOfVisibleBars.set(amount);

        Box base1Box = createBaseBox(Y_POS_BAR - 20.0);
        Box base2Box = createBaseBox(Y_POS_BAR + 20.0);
        Group xylophoneBase = new Group(base1Box, base2Box);

        Group xylophoneBars = buildXylophoneBars();
        Group xylophoneAssembly = new Group(xylophoneBase, xylophoneBars);

        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(getSceneFill(), CornerRadii.EMPTY, Insets.EMPTY)));

        final Scene scene = configuredScene(root);
        final SubScene subScene = configuredSubScene(xylophoneAssembly);
        subScene.widthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(scene.heightProperty().multiply(0.9));

        HBox controlsPane = buildControls(xylophoneBars, scene);
        controlsPane.setOpacity(.8);
        root.getChildren().addAll(subScene, controlsPane);
        mouseControlService.initMouseControl(xylophoneAssembly, scene);
        recenter();
        FxSetup.configureStage(stage, scene, "Xylophone").show();
    }

    private HBox buildControls(Group xylophoneBars, Scene scene) {
        Button recenterButton = FxSetup.configuredButton("Recenter", e -> recenter());
        recenterButton.setPrefWidth(200);
        int minBarSliderVal = parsedIntAppProp("min_bar_slider_val", 4);
        Slider barAmountSlider = FxSetup.configuredSlider(minBarSliderVal, MAX_NUMBER_OF_BARS, nrOfVisibleBars.get());
        barAmountSlider.setMajorTickUnit(5);
        barAmountSlider.setShowTickLabels(true);
        barAmountSlider.setShowTickMarks(true);
        barAmountSlider.prefWidthProperty().bind(scene.widthProperty());
        nrOfVisibleBars.bind(barAmountSlider.valueProperty());
        nrOfVisibleBars.addListener((o, c, n) -> updateXylophoneLength(n, xylophoneBars.getChildren()));

        return FxSetup.configuredHBox(recenterButton, barAmountSlider);
    }

    private Group buildXylophoneBars() {
        Group xylophoneBars = new Group();
        for (int barNr = 1; barNr <= MAX_NUMBER_OF_BARS; barNr++) {
            Box bar = buildConfiguredBar(barNr);
            bar.setId(String.valueOf(barNr));
            barsDeque.push(bar);
        }
        for (int i = 0; i < nrOfVisibleBars.get(); i++) {
            Box bar = (Box) barsDeque.pollLast();
            if (bar != null) {
                adaptProperties(bar);
                xylophoneBars.getChildren().add(bar);
            }
        }
        return xylophoneBars;
    }

    private void recenter() {
        MouseControlService mcs = (MouseControlService) this.mouseControlService;
        mcs.setOrientation(INIT_ANGLE_X, INIT_ANGLE_Y);
        mcs.resetShear();
        mcs.resetScale();
        mcs.resetTranslation();
    }

    private Scene configuredScene(Parent root) {
        int sceneWidth = parsedIntAppProp("init_scene_width", 800);
        int sceneHeight = parsedIntAppProp("init_scene_height", 600);
        return new Scene(root, sceneWidth, sceneHeight, true);
    }

    private SubScene configuredSubScene(Parent root) {
        SubScene scene = new SubScene(root, 0, 0, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10_000);
        camera.setTranslateZ(-1000);
        scene.setCamera(camera);
        return scene;
    }

    private RadialGradient getSceneFill() {
        return new RadialGradient(225, 0.85, 300, 300, 500, false,
                CycleMethod.NO_CYCLE, new Stop(0f, Color.BLUE), new Stop(1f, Color.LIGHTBLUE));
    }

    private Box buildConfiguredBar(int barNr) {
        Box barBox = new Box();
        barBox.setWidth(BAR_WIDTH);
        barBox.setHeight(BAR_THICKNESS);
        DoubleProperty xPosLowestBar = new SimpleDoubleProperty();
        xPosLowestBar.bind(nrOfVisibleBars.multiply(-X_OFFSET_BAR / 2));
        barBox.translateXProperty().bind(xPosLowestBar.add(barNr * X_OFFSET_BAR));
        barBox.setTranslateZ(Y_POS_BAR);
        addAudioToBar(barNr, barBox);
        return barBox;
    }

    private void addAudioToBar(int barNr, Box barBox) {
        int audioFileNr = barNr <= NR_OF_REQUIRED_AUDIOFILES ? barNr : ((barNr - 2) % (NR_OF_REQUIRED_AUDIOFILES - 1)) + 2;
        String audiofileLocation = String.format("/xylophone/Note%d.wav", audioFileNr);
        URL audioUrl = getClass().getResource(audiofileLocation);
        if (audioUrl != null) {
            AudioClip barNote = configuredAudioClip(barNr, audioUrl);
            barBox.setOnMousePressed(me -> barNote.play());
        } else err.println("No audiofile found at resource " + audiofileLocation);
    }

    @NotNull
    private AudioClip configuredAudioClip(int barNr, URL audioUrl) {
        AudioClip barNote = new AudioClip(audioUrl.toString());
        int octave = ((barNr - 2) / (NR_OF_REQUIRED_AUDIOFILES - 1)) - 1;
        barNote.setRate(Math.pow(2, octave));
        return barNote;
    }

    private Color extractHueColorFromBarNr(int barNr) {
        int maxHueVal = 270;
        double hueValue = (1 - lerp(barNr)) * maxHueVal;
        return Color.hsb(hueValue, 1, 1);
    }

    private double calculateBarLengthByNumber(int barNr) {
        double difference = LONGEST_BAR_LENGTH - SHORTEST_BAR_LENGTH;
        return LONGEST_BAR_LENGTH - (lerp(barNr) * difference);
    }

    private double lerp(int barNr) {
        double denominator = nrOfVisibleBars.get() - 1 != 0 ? nrOfVisibleBars.get() - 1 : 1;
        return (barNr - 1) / denominator;
    }

    private static final double BASE_HEIGHT = 14;
    private static final double BASE_DEPTH = 10;

    private Box createBaseBox(double yPos) {
        Box baseBox = new Box(X_OFFSET_BAR * nrOfVisibleBars.get(), BASE_HEIGHT, BASE_DEPTH);
        baseBox.widthProperty().bind(nrOfVisibleBars.multiply(X_OFFSET_BAR));
        baseBox.setMaterial(new PhongMaterial(new Color(0.2, 0.12, 0.1, 1.0)));
        double xPosLowestBar = -nrOfVisibleBars.get() * X_OFFSET_BAR / 2;
        baseBox.setTranslateX(xPosLowestBar + X_OFFSET_BAR * ((nrOfVisibleBars.get() + 1) / 2.));
        baseBox.setTranslateZ(yPos);
        baseBox.setTranslateY(11.0);
        return baseBox;
    }

    private void updateXylophoneLength(Number newValue, ObservableList<Node> children) {
        int newVal = newValue.intValue();
        int size = children.size();
        while (size != newVal) {
            if (newVal > size) {
                children.add(barsDeque.pollLast());
            } else {
                barsDeque.addLast(children.remove(size - 1));
            }
            size = children.size();
        }
        children.stream().map(Box.class::cast).forEach(this::adaptProperties);
    }

    private void adaptProperties(Box bar) {
        int barNr = Integer.parseInt(bar.getId());
        bar.setDepth(calculateBarLengthByNumber(barNr));
        bar.setMaterial(new PhongMaterial(extractHueColorFromBarNr(barNr)));
    }
}
