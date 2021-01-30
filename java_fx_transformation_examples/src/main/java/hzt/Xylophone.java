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

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.*;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import static java.lang.System.*;

public class Xylophone extends Application {

    private static final Properties PROPERTIES = configuredProperties();

    private static final double X_POS_LOWEST_BAR = 260.0;
    private static final double X_OFFSET_BAR = 30.0;
    private static final double Y_POS_BAR = 300.0;

    private static final int NR_OF_REQUIRED_AUDIOFILES = 8;
    private static final int NR_OF_BARS = parsedIntAppProp("nr_of_bars", 8);

    private static final double BAR_WIDTH = 22.0;
    private static final double SHORTEST_BAR_LENGTH = 65;
    private static final double LONGEST_BAR_LENGTH = 100;
    private static final double BAR_THICKNESS = 7.0;

    private double mousePosX;
    private double mousePosY;
    private double prevMouseX;
    private double prevMouseY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    private final TransformableGroup container = new TransformableGroup();
    private final TransformableGroup root = new TransformableGroup(container);
    private final Shear shear = new Shear();

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Xylophone");
        resetCam();

        Box base1Box = createBaseBox(Y_POS_BAR - 20.0);
        Box base2Box = createBaseBox(Y_POS_BAR + 20.0);
        Group xylophoneAssembly = xylophoneAssembly(base1Box, base2Box);

        for (int barNr = 1; barNr <= NR_OF_BARS; barNr++) {
            xylophoneAssembly.getChildren().add(configuredBar(barNr));
        }

        container.getChildren().add(xylophoneAssembly);
        final Scene scene = configuredScene(stage);
        stage.setScene(scene);
        stage.show();
    }

    @NotNull
    private static Properties configuredProperties() {
        Properties properties = new Properties();
        InputStream fileInputStream = Xylophone.class.getResourceAsStream("/xylofoon.properties");
        if (fileInputStream != null) {
            try {
                properties.load(new BufferedInputStream(fileInputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else err.println("file inputstream was null...");
        return properties;
    }

    public static int parsedIntAppProp(String property, int defaultVal) {
        int value = defaultVal;
        String propertyVal = PROPERTIES.getProperty(property);
        if (propertyVal != null) {
            try {
                value = Integer.parseInt(propertyVal);
            } catch (NumberFormatException e) {
                err.printf("Property '%s' with value '%s' could not be parsed to an int... " +
                        "Falling back to default: %d...", property, propertyVal, defaultVal);
            }
        } else err.printf("Property '%s' not found. Falling back to default: %d",
                property, defaultVal);
        return value;
    }

    @NotNull
    private Group xylophoneAssembly(Node... nodes) {
        Group group = new Group(nodes);
        group.getTransforms().add(shear);
        group.setScaleX(2.5);
        group.setScaleY(2.5);
        group.setScaleZ(2.5);
        return group;
    }

    @NotNull
    private Scene configuredScene(Stage stage) {
        final Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(getSceneFill());
        scene.setCamera(new PerspectiveCamera());
        scene.setOnMousePressed(this::setMousePositionParams);
        scene.setOnMouseDragged(this::mouseDraggedAction);
        scene.setOnKeyPressed(ke -> keyPressedAction(stage, scene, ke));
        frameCam(scene);
        return scene;
    }

    @NotNull
    private RadialGradient getSceneFill() {
        return new RadialGradient(225, 0.85, 300, 300, 500, false,
                CycleMethod.NO_CYCLE, new Stop(0f, Color.BLUE), new Stop(1f, Color.LIGHTBLUE));
    }

    private void setMousePositionParams(MouseEvent mouseEvent) {
        mousePosX = mouseEvent.getX();
        mousePosY = mouseEvent.getY();
        prevMouseX = mouseEvent.getX();
        prevMouseY = mouseEvent.getY();
    }

    private void mouseDraggedAction(MouseEvent e) {
        prevMouseX = mousePosX;
        prevMouseY = mousePosY;
        mousePosX = e.getX();
        mousePosY = e.getY();
        mouseDeltaX = mousePosX - prevMouseX;
        mouseDeltaY = mousePosY - prevMouseY;
        if (e.isAltDown() && e.isShiftDown() && e.isPrimaryButtonDown()) rotateAroundAxis(container.rotateZ);
        else if (e.isAltDown() && e.isPrimaryButtonDown()) rotateInXAndYDir();
        else if (e.isShiftDown() && e.isPrimaryButtonDown()) shear();
        else if (e.isAltDown() && e.isSecondaryButtonDown()) scale();
        else if (e.isAltDown() && e.isMiddleButtonDown()) translate();
    }

    private void translate() {
        double tx = container.translate.getX();
        double ty = container.translate.getY();
        container.translate.setX(tx + mouseDeltaX);
        container.translate.setY(ty + mouseDeltaY);
    }

    private void scale() {
        double scale = container.scale.getX();
        double newScale = scale + mouseDeltaX * 0.01;
        container.scale.setX(newScale);
        container.scale.setY(newScale);
        container.scale.setZ(newScale);
    }

    private void shear() {
        shear.setY(shear.getY() + mouseDeltaY / 1000.0);
        shear.setX(shear.getX() + mouseDeltaX / 1000.0);
    }

    private void rotateInXAndYDir() {
        rotateAroundAxis(container.rotateY);
        double rxAngle = container.rotateX.getAngle();
        container.rotateX.setAngle(rxAngle + mouseDeltaY);
    }

    private void rotateAroundAxis(Rotate rotate) {
        double angle = rotate.getAngle();
        rotate.setAngle(angle - mouseDeltaX);
    }

    private void keyPressedAction(Stage stage, Scene scene, KeyEvent keyEvent) {
        if (KeyCode.A.equals(keyEvent.getCode())) resetCamAndShear();
        if (KeyCode.F.equals(keyEvent.getCode())) resetFrameCamAndShear(scene);
        if (KeyCode.SPACE.equals(keyEvent.getCode())) setFullScreen(stage, scene);
    }

    private void setFullScreen(Stage stage, Scene scene) {
        stage.setFullScreen(!stage.isFullScreen());
        frameCam(scene);
    }

    private void resetFrameCamAndShear(Scene scene) {
        frameCam(scene);
        shear.setX(0.0);
        shear.setY(0.0);
    }

    private void resetCamAndShear() {
        resetCam();
        shear.setX(0.0);
        shear.setY(0.0);
    }

    private Box configuredBar(int barNr) {
        Color color = extractHueColorFromBarNr(barNr);
        double barLength = calculateBarLengthByNumber(barNr);
        Box barBox = new Box(BAR_WIDTH, BAR_THICKNESS, barLength);
        barBox.setMaterial(new PhongMaterial(color));
        barBox.setTranslateX(X_POS_LOWEST_BAR + barNr * X_OFFSET_BAR);
        barBox.setTranslateZ(Y_POS_BAR);
        int audioFileNr = barNr <= NR_OF_REQUIRED_AUDIOFILES ? barNr : ((barNr - 2) % (NR_OF_REQUIRED_AUDIOFILES - 1)) + 2;
        String audiofileLocation = String.format("/xylophone/Note%d.wav", audioFileNr);
        URL audioUrl = getClass().getResource(audiofileLocation);
        if (audioUrl != null) {
            AudioClip barNote = configuredAudioClip(barNr, audioUrl);
            barBox.setOnMousePressed(me -> barNote.play());
        } else err.println("No audiofile found at resource " + audiofileLocation);
        return barBox;
    }

    @NotNull
    private AudioClip configuredAudioClip(int barNr, URL audioUrl) {
        AudioClip barNote = new AudioClip(audioUrl.toString());
        int octave = NR_OF_BARS > NR_OF_REQUIRED_AUDIOFILES ? ((barNr - 2) / (NR_OF_REQUIRED_AUDIOFILES - 1)) - 1 : 0;
        barNote.setRate(Math.pow(2, octave));
        return barNote;
    }

    private Color extractHueColorFromBarNr(int barNr) {
        int maxHueVal = 270;
        double hueValue = (1 - ((double) (barNr - 1) / (NR_OF_BARS - 1))) * maxHueVal;
        return Color.hsb(hueValue, 1, 1);
    }

    private double calculateBarLengthByNumber(int barNr) {
        double difference = LONGEST_BAR_LENGTH - SHORTEST_BAR_LENGTH;
        return LONGEST_BAR_LENGTH - (((double) (barNr - 1) / (NR_OF_BARS - 1)) * difference);
    }

    private Box createBaseBox(double yPos) {
        Box baseBox = new Box(X_OFFSET_BAR * NR_OF_BARS, 14, 10);
        baseBox.setMaterial(new PhongMaterial(new Color(0.2, 0.12, 0.1, 1.0)));
        baseBox.setTranslateX(X_POS_LOWEST_BAR + X_OFFSET_BAR * ((NR_OF_BARS + 1) / 2.));
        baseBox.setTranslateZ(yPos);
        baseBox.setTranslateY(11.0);
        return baseBox;
    }

    //=========================================================================
    // BoxSystem.frameCam
    //=========================================================================

    public void frameCam(final Scene scene) {
        setCamOffset(root, scene);
        setCamPivot(container);
        setCamTranslate(container);
        setCamScale(container, scene);
    }
    //=========================================================================
    // BoxSystem.setCamOffset
    //=========================================================================

    public void setCamOffset(final TransformableGroup transformableGroupOffset, final Scene scene) {
        double width = scene.getWidth();
        double height = scene.getHeight();
        transformableGroupOffset.translate.setX(width / 2.0);
        transformableGroupOffset.translate.setY(height / 2.0);
    }
    //=========================================================================
    // setCamScale
    //=========================================================================

    public void setCamScale(final TransformableGroup transformableGroup, final Scene scene) {
        final Bounds bounds = transformableGroup.getBoundsInLocal();

        double width = scene.getWidth();
        double height = scene.getHeight();

        double scaleFactor;
        double scaleFactorY = 1.0;
        double scaleFactorX = 1.0;
        if (bounds.getWidth() > 0.0001) {
            scaleFactorX = width / bounds.getWidth();
        }
        if (bounds.getHeight() > 0.0001) {
            scaleFactorY = height / bounds.getHeight();
        }
        scaleFactor = Math.min(scaleFactorX, scaleFactorY);
        transformableGroup.scale.setX(scaleFactor);
        transformableGroup.scale.setY(scaleFactor);
        transformableGroup.scale.setZ(scaleFactor);
    }
    //=========================================================================
    // setCamPivot
    //=========================================================================

    public void setCamPivot(final TransformableGroup transformableGroup) {
        setPivot(transformableGroup);
    }

    private void setPivot(TransformableGroup transformableGroup) {
        final Bounds bounds = transformableGroup.getBoundsInLocal();
        final double pivotX = bounds.getMinX() + bounds.getWidth() / 2;
        final double pivotY = bounds.getMinY() + bounds.getHeight() / 2;
        final double pivotZ = bounds.getMinZ() + bounds.getDepth() / 2;
        transformableGroup.pivot.setX(pivotX);
        transformableGroup.pivot.setY(pivotY);
        transformableGroup.pivot.setZ(pivotZ);
        transformableGroup.inversePivot.setX(-pivotX);
        transformableGroup.inversePivot.setY(-pivotY);
        transformableGroup.inversePivot.setZ(-pivotZ);
    }

    //=========================================================================
    // setCamTranslate
    //=========================================================================

    public void setCamTranslate(final TransformableGroup transformableGroup) {
        final Bounds bounds = transformableGroup.getBoundsInLocal();
        final double pivotX = bounds.getMinX() + bounds.getWidth() / 2;
        final double pivotY = bounds.getMinY() + bounds.getHeight() / 2;
        transformableGroup.translate.setX(-pivotX);
        transformableGroup.translate.setY(-pivotY);
    }

    public void resetCam() {
        container.translate.setX(0.0);
        container.translate.setY(0.0);
        container.translate.setZ(0.0);
        container.rotateX.setAngle(45.0);
        container.rotateY.setAngle(-7.0);
        container.rotateZ.setAngle(0.0);
        container.scale.setX(1.25);
        container.scale.setY(1.25);
        container.scale.setZ(1.25);

        container.pivot.setX(0.0);
        container.pivot.setY(0.0);
        container.pivot.setZ(0.0);

        container.inversePivot.setX(0.0);
        container.inversePivot.setY(0.0);
        container.inversePivot.setZ(0.0);

        setPivot(container);
    }

    private static class TransformableGroup extends Group {

        Translate translate = new Translate();
        Translate pivot = new Translate();
        Translate inversePivot = new Translate();

        Rotate rotateX = new Rotate();
        Rotate rotateY = new Rotate();
        Rotate rotateZ = new Rotate();

        Scale scale = new Scale();

        public TransformableGroup(Node... nodes) {
            super(nodes);
            rotateX.setAxis(Rotate.X_AXIS);
            rotateY.setAxis(Rotate.Y_AXIS);
            rotateZ.setAxis(Rotate.Z_AXIS);
            getTransforms().addAll(translate, pivot, rotateX, rotateZ, rotateY, scale, inversePivot);
        }

    }
}
