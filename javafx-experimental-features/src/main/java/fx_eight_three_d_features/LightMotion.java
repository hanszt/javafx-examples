/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package fx_eight_three_d_features;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

import static java.lang.System.err;
import static javafx.animation.Animation.Status.RUNNING;

public class LightMotion extends Application {

    private int counterS = 0;
    private int counterC = 0;

    private Scene buildScene() {
        PhongMaterial material = buildMaterial();

        Group sphereGroup = buildSphere(material);
        Cylinder cylinder = buildCylinder(material);
        RotateTransition rotTrans = configuredRotateTransition(cylinder);

        final VisibleLight lightGroup1 = createVisibleLightGroup(Color.PALEGOLDENROD, 700, 200, 200);
        Timeline stationLightTL = animateColorOfLight(lightGroup1);

        final VisibleLight lightGroup2 = createVisibleLightGroup(Color.ORANGERED, -300, 300, 100);
        final Group movingLight = new Group(lightGroup2);
        movingLight.setTranslateX(400);
        Timeline movingLightTL = animateMovingLight(movingLight);

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateZ(-10);
        Group root = new Group(sphereGroup, cylinder, lightGroup1, movingLight);
        Scene scene = new Scene(root, 800, 800, true);
        scene.setCamera(camera);
        scene.setFill(Color.rgb(10, 10, 40));
        scene.setOnKeyTyped(e ->
                onKeyTyped(sphereGroup, cylinder, lightGroup1, stationLightTL, lightGroup2, movingLightTL, rotTrans, e));
        return scene;
    }

    @NotNull
    private Cylinder buildCylinder(PhongMaterial material) {
        Cylinder cylinder = new Cylinder(200, 400);
        cylinder.setTranslateX(400);
        cylinder.setTranslateY(500);
        cylinder.setTranslateZ(50);

        cylinder.setMaterial(material);
        return cylinder;
    }

    @NotNull
    private Group buildSphere(PhongMaterial material) {
        Sphere sphere = new Sphere(100);
        sphere.setMaterial(material);
        Group sphereGroup = new Group(sphere);
        sphereGroup.setTranslateX(400);
        sphereGroup.setTranslateY(100);
        sphereGroup.setTranslateZ(50);
        return sphereGroup;
    }

    @NotNull
    private PhongMaterial buildMaterial() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGRAY);
        material.setSpecularColor(Color.rgb(30, 30, 30));
        return material;
    }

    private void onKeyTyped(
            Group sphereGroup, Cylinder cylinder, VisibleLight lightGroup1,
            Timeline stationLightTL, VisibleLight lightGroup2, Timeline movingLightTL,
            RotateTransition rotTrans, KeyEvent e) {
        switch (e.getCharacter()) {
            case "[":
                pausePlayAnimation(stationLightTL);
                break;
            case "]":
                pausePlayAnimation(movingLightTL);
                break;
            case "\\":
                pausePlayAnimation(rotTrans);
                break;
            case "c":
                lightGroup1.addScope(cylinder);
                lightGroup2.addScope(cylinder);
                break;
            case "C":
                err.println("C = " + counterC++ + ". Removing cylinder from lights");
                lightGroup1.removeScope(cylinder);
                lightGroup2.removeScope(cylinder);
                break;
            case "s":
                lightGroup1.addScope(sphereGroup);
                lightGroup2.addScope(sphereGroup);
                break;
            case "S":
                err.println("S = " + counterS++ + ". Removing sphere from lights");
                lightGroup1.removeScope(sphereGroup);
                lightGroup2.removeScope(sphereGroup);
                break;
            default:
                break;
        }
    }

    @NotNull
    private RotateTransition configuredRotateTransition(Node node) {
        RotateTransition rotTrans = new RotateTransition(Duration.seconds(35), node);
        rotTrans.setAutoReverse(true);
        rotTrans.setAxis(new Point3D(2, 1, 0).normalize());
        rotTrans.setInterpolator(Interpolator.EASE_BOTH);
        rotTrans.setCycleCount(Animation.INDEFINITE);
        rotTrans.setByAngle(360);
        return rotTrans;
    }

    @NotNull
    private Timeline animateMovingLight(Group movingLight) {
        final Rotate rot = new Rotate(0, Rotate.Y_AXIS);
        movingLight.getTransforms().add(rot);
        KeyValue kv = new KeyValue(rot.angleProperty(), 360.0);
        KeyFrame kf = new KeyFrame(Duration.seconds(7), kv);
        Timeline movingLightTL = new Timeline(kf);
        movingLightTL.setCycleCount(Animation.INDEFINITE);
        return movingLightTL;
    }

    @NotNull
    private Timeline animateColorOfLight(VisibleLight lightGroup1) {
        DoubleProperty rotation = new SimpleDoubleProperty(0);
        rotation.addListener((observable, oldValue, newValue) -> {
            double t1 = newValue.doubleValue();
            Color c = Color.WHITE.interpolate(Color.GREEN, t1);
            lightGroup1.setColor(c);
        });

        KeyValue keyValue = new KeyValue(rotation, 1.0, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), keyValue);
        Timeline stationLightTL = new Timeline(keyFrame);
        stationLightTL.setAutoReverse(true);
        stationLightTL.setCycleCount(Animation.INDEFINITE);
        return stationLightTL;
    }

    private LightMotion.VisibleLight createVisibleLightGroup(Color palegoldenrod, int i, int i2, int i3) {
        final VisibleLight lightGroup1 = new VisibleLight(palegoldenrod);
        lightGroup1.setTranslateX(i);
        lightGroup1.setTranslateY(i2);
        lightGroup1.setTranslateZ(-i3);
        return lightGroup1;
    }

    private void pausePlayAnimation(Animation animation) {
        if (animation.getStatus() == RUNNING) {
            animation.pause();
        } else {
            animation.play();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = buildScene();
        primaryStage.setTitle("FX8 Light Motion");
        primaryStage.setScene(scene);
        primaryStage.show();
        err.println("Both cylinder and sphere need to be added to lights"
                + " first before removing them from lights can work.");
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class VisibleLight extends Group {

        private final PhongMaterial material;
        private final PointLight pointLight;
        private final Sphere lightShape;

        private void makeConstantImage(WritableImage img, Color color) {
            int width = (int) img.getWidth();
            ByteBuffer scanline = ByteBuffer.allocate(3 * width);
            byte r = (byte) ((int) Math.round(color.getRed() * 255.0));
            byte g = (byte) ((int) Math.round(color.getGreen() * 255.0));
            byte b = (byte) ((int) Math.round(color.getBlue() * 255.0));
            for (int i = 0; i < width; i++) {
                scanline.put(r);
                scanline.put(g);
                scanline.put(b);
            }
            scanline.rewind();
            img.getPixelWriter().setPixels(0, 0, width, width, PixelFormat.getByteRgbInstance(), scanline, 0);
        }

        VisibleLight(Color color) {
            pointLight = new PointLight();
            material = new PhongMaterial(Color.BLACK);
            material.setSpecularColor(Color.TRANSPARENT);
            setColor(color);

            lightShape = new Sphere(50);
            lightShape.setScaleX(0.2);
            lightShape.setScaleY(0.2);
            lightShape.setScaleZ(0.2);
            lightShape.setMaterial(material);

            this.getChildren().addAll(pointLight, lightShape);
        }

        final void setColor(Color color) {
            pointLight.setColor(color);
            WritableImage selfIllumMap = new WritableImage(64, 64);
            makeConstantImage(selfIllumMap, color);
            material.setSelfIlluminationMap(selfIllumMap);
        }

        final void addScope(Node node) {
            ObservableList<Node> scope = pointLight.getScope();
            if (scope.isEmpty()) {
                scope.add(lightShape);
            }
            if ((node != null) && (!scope.contains(node))) {
                scope.add(node);
            }
        }

        final void removeScope(Node node) {
            ObservableList<Node> scope = pointLight.getScope();
            if (node != null) {
                scope.remove(node);
            }
        }
    }
}
