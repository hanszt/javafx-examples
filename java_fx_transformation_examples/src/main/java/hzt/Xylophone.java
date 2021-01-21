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
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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

public class Xylophone extends Application {

    private static final double X_START = 260.0;
    private static final double X_OFFSET = 30.0;
    private static final double Y_POS = 300.0;
    private static final double BAR_WIDTH = 22.0;
    private static final double BAR_THICKNESS = 7.0;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    final TransformableGroup root = new TransformableGroup();
    final TransformableGroup transformableGroup = new TransformableGroup();
    final Shear shear = new Shear();

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Xylophone");

        root.getChildren().add(transformableGroup);
        resetCam();

        final Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(getSceneFill());

        scene.setCamera(new PerspectiveCamera());

        AudioClip bar1Note = new AudioClip(getClass().getResource("/xylophone/Note1.wav").toString());
        AudioClip bar2Note = new AudioClip(getClass().getResource("/xylophone/Note2.wav").toString());
        AudioClip bar3Note = new AudioClip(getClass().getResource("/xylophone/Note3.wav").toString());
        AudioClip bar4Note = new AudioClip(getClass().getResource("/xylophone/Note4.wav").toString());
        AudioClip bar5Note = new AudioClip(getClass().getResource("/xylophone/Note5.wav").toString());
        AudioClip bar6Note = new AudioClip(getClass().getResource("/xylophone/Note6.wav").toString());
        AudioClip bar7Note = new AudioClip(getClass().getResource("/xylophone/Note7.wav").toString());
        AudioClip bar8Note = new AudioClip(getClass().getResource("/xylophone/Note8.wav").toString());

        Group rectangleGroup = new Group();
        rectangleGroup.getTransforms().add(shear);

        Box base1Box = createBaseBox(Y_POS - 20.0);
        Box base2Box = createBaseBox(Y_POS + 20.0);

        Box bar1Box = createBar(1, 100, Color.PURPLE);
        Box bar2Box = createBar(2, 95, Color.BLUEVIOLET);
        Box bar3Box = createBar(3, 90, Color.BLUE);
        Box bar4Box = createBar(4, 85, Color.GREEN);
        Box bar5Box = createBar(5, 80, Color.GREENYELLOW);
        Box bar6Box = createBar(6, 75, Color.YELLOW);
        Box bar7Box = createBar(7, 70, Color.ORANGE);
        Box bar8Box = createBar(8, 65, Color.RED);

        bar1Box.setOnMousePressed(me -> bar1Note.play());
        bar2Box.setOnMousePressed(me -> bar2Note.play());
        bar3Box.setOnMousePressed(me -> bar3Note.play());
        bar4Box.setOnMousePressed(me -> bar4Note.play());
        bar5Box.setOnMousePressed(me -> bar5Note.play());
        bar6Box.setOnMousePressed(me -> bar6Note.play());
        bar7Box.setOnMousePressed(me -> bar7Note.play());
        bar8Box.setOnMousePressed(me -> bar8Note.play());

        rectangleGroup.getChildren().addAll(base1Box, base2Box);
        rectangleGroup.getChildren().addAll(bar1Box, bar2Box, bar3Box, bar4Box, bar5Box, bar6Box, bar7Box, bar8Box);

        rectangleGroup.setScaleX(2.5);
        rectangleGroup.setScaleY(2.5);
        rectangleGroup.setScaleZ(2.5);
        transformableGroup.getChildren().add(rectangleGroup);

        frameCam(scene);

        scene.setOnMousePressed(this::setMouseOld);
        scene.setOnMouseDragged(this::mouseDraggedAction);
        scene.setOnKeyPressed(ke -> keyPressedAction(stage, scene, ke));

        stage.setScene(scene);
        stage.show();
    }

    @NotNull
    private RadialGradient getSceneFill() {
        return new RadialGradient(225, 0.85, 300, 300, 500, false,
                CycleMethod.NO_CYCLE, new Stop(0f, Color.BLUE), new Stop(1f, Color.LIGHTBLUE));
    }

    private void setMouseOld(MouseEvent me) {
        mousePosX = me.getX();
        mousePosY = me.getY();
        mouseOldX = me.getX();
        mouseOldY = me.getY();
    }

    private void mouseDraggedAction(MouseEvent me) {
        mouseOldX = mousePosX;
        mouseOldY = mousePosY;
        mousePosX = me.getX();
        mousePosY = me.getY();
        mouseDeltaX = mousePosX - mouseOldX;
        mouseDeltaY = mousePosY - mouseOldY;
        if (me.isAltDown() && me.isShiftDown() && me.isPrimaryButtonDown()) rotateAroundAxis(transformableGroup.rotateZ);
        else if (me.isAltDown() && me.isPrimaryButtonDown()) rotateInXAndYDir();
        else if (me.isShiftDown() && me.isPrimaryButtonDown()) shear();
        else if (me.isAltDown() && me.isSecondaryButtonDown()) scale();
        else if (me.isAltDown() && me.isMiddleButtonDown()) translate();
    }

    private void translate() {
        double tx = transformableGroup.translate.getX();
        double ty = transformableGroup.translate.getY();
        transformableGroup.translate.setX(tx + mouseDeltaX);
        transformableGroup.translate.setY(ty + mouseDeltaY);
    }

    private void scale() {
        double scale = transformableGroup.scale.getX();
        double newScale = scale + mouseDeltaX * 0.01;
        transformableGroup.scale.setX(newScale);
        transformableGroup.scale.setY(newScale);
        transformableGroup.scale.setZ(newScale);
    }

    private void shear() {
        double yShear = shear.getY();
        shear.setY(yShear + mouseDeltaY / 1000.0);
        double xShear = shear.getX();
        shear.setX(xShear + mouseDeltaX / 1000.0);
    }

    private void rotateInXAndYDir() {
        rotateAroundAxis(transformableGroup.rotateY);
        double rxAngle = transformableGroup.rotateX.getAngle();
        transformableGroup.rotateX.setAngle(rxAngle + mouseDeltaY);
    }

    private void rotateAroundAxis(Rotate rotate) {
        double angle = rotate.getAngle();
        rotate.setAngle(angle - mouseDeltaX);
    }

    private void keyPressedAction(Stage stage, Scene scene, javafx.scene.input.KeyEvent ke) {
        if (KeyCode.A.equals(ke.getCode())) resetCamAndShear();
        if (KeyCode.F.equals(ke.getCode())) resetFrameCamAndShear(scene);
        if (KeyCode.SPACE.equals(ke.getCode())) setFullScreen(stage, scene);
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

    private Box createBar(int barNr, double barLength, Color color) {
        Box bar1Box = new Box(BAR_WIDTH, BAR_THICKNESS, barLength);
        bar1Box.setMaterial(new PhongMaterial(color));
        bar1Box.setTranslateX(X_START + barNr * X_OFFSET);
        bar1Box.setTranslateZ(Y_POS);
        return bar1Box;
    }

    private Box createBaseBox(double yPos) {
        Box baseBox = new Box(253, 14, 10);
        baseBox.setMaterial(new PhongMaterial(new Color(0.2, 0.12, 0.1, 1.0)));
        baseBox.setTranslateX(395.0);
        baseBox.setTranslateZ(yPos);
        baseBox.setTranslateY(11.0);
        return baseBox;
    }

    //=========================================================================
    // BoxSystem.frameCam
    //=========================================================================

    public void frameCam(final Scene scene) {
        setCamOffset(root, scene);
        setCamPivot(transformableGroup);
        setCamTranslate(transformableGroup);
        setCamScale(transformableGroup, scene);
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
        transformableGroup.translate.setX(0.0);
        transformableGroup.translate.setY(0.0);
        transformableGroup.translate.setZ(0.0);
        transformableGroup.rotateX.setAngle(45.0);
        transformableGroup.rotateY.setAngle(-7.0);
        transformableGroup.rotateZ.setAngle(0.0);
        transformableGroup.scale.setX(1.25);
        transformableGroup.scale.setY(1.25);
        transformableGroup.scale.setZ(1.25);

        transformableGroup.pivot.setX(0.0);
        transformableGroup.pivot.setY(0.0);
        transformableGroup.pivot.setZ(0.0);

        transformableGroup.inversePivot.setX(0.0);
        transformableGroup.inversePivot.setY(0.0);
        transformableGroup.inversePivot.setZ(0.0);

        setPivot(transformableGroup);
    }

    private static class TransformableGroup extends Group {

        Translate translate = new Translate();
        Translate pivot = new Translate();
        Translate inversePivot = new Translate();

        Rotate rotateX = new Rotate();
        Rotate rotateY = new Rotate();
        Rotate rotateZ = new Rotate();

        Scale scale = new Scale();

        public TransformableGroup() {
            super();
            rotateX.setAxis(Rotate.X_AXIS);
            rotateY.setAxis(Rotate.Y_AXIS);
            rotateZ.setAxis(Rotate.Z_AXIS);
            getTransforms().addAll(translate, pivot, rotateX, rotateZ, rotateY, scale, inversePivot);
        }
    }
}
