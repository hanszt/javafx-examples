/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
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

import fx_eight_three_d_features.model.WaveMesh;
import fx_eight_three_d_features.utils.FxSetup;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import service.IMouseControlService;
import service.MouseControlService;

public class MeshViewer extends Application {

    @SuppressWarnings("SameParameterValue")
    private Scene buildScene(int width, int height, boolean depthBuffer) {
        WaveMesh waveMesh = getConfiguredWaveMesh();

        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(-50);
        pointLight.setTranslateZ(-1000);

        VBox root = FxSetup.configuredVBox();
        root.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root, width, height, depthBuffer);

        Group subSceneRoot = new Group(waveMesh, pointLight);
        final SubScene subScene = configuredSubScene(subSceneRoot);
        subScene.widthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(scene.heightProperty().multiply(0.75));

        HBox controlsPane = buildControls(waveMesh, scene);
        controlsPane.setOpacity(.8);
        root.getChildren().addAll(subScene, controlsPane);

        IMouseControlService mouseControlService = new MouseControlService();
        mouseControlService.initMouseControl(waveMesh, scene);
        return scene;
    }

    private HBox buildControls(WaveMesh waveMesh, Scene scene) {
        Slider dampeningSlider = getSlider(scene, 0, 1, 1);
        Slider amplitudeSlider = getSlider(scene, -20, 20, -10);
        Slider resolutionXSlider = getSlider(scene, 0, 200, 100);
        Slider resolutionYSlider = getSlider(scene, 0, 200, 100);
        Slider meshSizeXSlider = getSlider(scene, 0, 100, 50);
        Slider meshSizeYSlider = getSlider(scene, 0, 100, 50);
        Slider waveFrequencySlider = getSlider(scene, -3, 3, 1);

        waveMesh.amplitudeProperty().bind(amplitudeSlider.valueProperty());
        waveMesh.dampeningProperty().bind(dampeningSlider.valueProperty());
        waveMesh.resolutionXProperty().bind(resolutionXSlider.valueProperty());
        waveMesh.resolutionYProperty().bind(resolutionYSlider.valueProperty());
        waveMesh.meshSizeXProperty().bind(meshSizeXSlider.valueProperty());
        waveMesh.meshSizeYProperty().bind(meshSizeYSlider.valueProperty());
        waveMesh.waveFrequencyProperty().bind(waveFrequencySlider.valueProperty());

        VBox vBox1 = FxSetup.configuredVBox(dampeningSlider, amplitudeSlider);
        VBox vBox2 = FxSetup.configuredVBox(resolutionXSlider, resolutionYSlider);
        VBox vBox3 = FxSetup.configuredVBox(meshSizeXSlider, meshSizeYSlider, waveFrequencySlider);
        return FxSetup.configuredHBox(vBox1, vBox2, vBox3);
    }

    private Slider getSlider(Scene scene, double minVal, double maxVal, double curVal) {
        Slider slider = new Slider(minVal, maxVal, curVal);
        slider.setMajorTickUnit(5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.prefWidthProperty().bind(scene.widthProperty());
        return slider;
    }

    private SubScene configuredSubScene(Parent root) {
        SubScene scene = new SubScene(root, 0, 0, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(1_000);
        camera.setTranslateZ(-100);
        scene.setCamera(camera);
        return scene;
    }

    private WaveMesh getConfiguredWaveMesh() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGRAY);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        WaveMesh waveMesh = new WaveMesh();
        waveMesh.setMaterial(material);
        //Set Wireframe mode
        waveMesh.setDrawMode(DrawMode.FILL);
        waveMesh.setCullFace(CullFace.BACK);

        waveMesh.setRotationAxis(Rotate.X_AXIS);
        waveMesh.setRotate(-30);
        waveMesh.setTranslateZ(10);
        return waveMesh;
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = buildScene(700, 750, true);
        primaryStage.setTitle("MeshViewer");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(750);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
