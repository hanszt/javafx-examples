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

import fx_eight_three_d_features.model.DiscreteWaveMesh;
import fx_eight_three_d_features.model.ParabolaMesh;
import fx_eight_three_d_features.model.PlaneZMesh;
import fx_eight_three_d_features.model.WaveMesh;
import fx_eight_three_d_features.utils.FxSetup;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import service.IMouseControlService;
import service.MouseControlService;
import utils.FxUtils;

import static java.lang.System.*;

public final class MeshViewer extends Application {

    private static final int INIT_ANGLE_X = 30;
    private static final int INIT_AMPLITUDE = -10;
    private static final int INIT_RESOLUTION_X = 100;
    private static final int INIT_RESOLUTION_Y = 100;
    private static final double INIT_DAMPENING_VALUE = 1;
    private static final int INIT_MESH_SIZE_X = 20;
    private static final int INIT_MES_SIZE_Y = 20;
    private static final int INIT_FREQUENCY = 1;

    private final Group planeMeshContainer = new Group();

    private final IMouseControlService mouseControlService = new MouseControlService();
    private final ComboBox<PlaneMeshPicker> planeZMeshComboBox = new ComboBox<>();
    private final Slider dampeningSlider = new Slider(0, 1, INIT_DAMPENING_VALUE);
    private final Slider amplitudeSlider = new Slider(-20, 20, INIT_AMPLITUDE);
    private final Slider resolutionXSlider = new Slider(0, 200, INIT_RESOLUTION_X);
    private final Slider resolutionYSlider = new Slider(0, 200, INIT_RESOLUTION_Y);
    private final Slider meshSizeXSlider = new Slider(0, 100, INIT_MESH_SIZE_X);
    private final Slider meshSizeYSlider = new Slider(0, 100, INIT_MES_SIZE_Y);
    private final Slider waveFrequencySlider = new Slider(-3, 3, INIT_FREQUENCY);

    private Scene buildScene() {
        var root = FxSetup.configuredVBox();
        root.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        var scene = new Scene(root, 700, 750, true);

        var controlsPane = buildControls(scene, new WaveMesh(), new ParabolaMesh(), new DiscreteWaveMesh());
        controlsPane.setOpacity(.8);

        planeMeshContainer.getChildren().add(planeZMeshComboBox.getValue().getPlaneZMesh());

        final var subScene = configuredSubScene();
        subScene.widthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(scene.heightProperty().multiply(0.75));

        root.getChildren().addAll(subScene, controlsPane);

        scene.setOnKeyTyped(e -> keyTypedAction(e, planeZMeshComboBox.getValue().getPlaneZMesh()));
        configurePlaneZMeshes(planeZMeshComboBox.getItems());
        mouseControlService.setOrientation(INIT_ANGLE_X, 0);
        mouseControlService.initMouseControl(planeMeshContainer, scene);
        return scene;
    }

    private void configurePlaneZMeshes(ObservableList<PlaneMeshPicker> planeZMeshPickers) {
        planeZMeshPickers.stream()
                .map(PlaneMeshPicker::getPlaneZMesh)
                .forEach(this::configureMesh);
    }

    private Parent buildControls(Scene scene, PlaneZMesh waveMesh, PlaneZMesh parabolaMesh, PlaneZMesh mazeMesh) {
        var waveMeshPicker = new PlaneMeshPicker("Wave mesh", waveMesh);
        planeZMeshComboBox.getItems().addAll(waveMeshPicker,
                new PlaneMeshPicker("Parabola mesh", parabolaMesh),
                new PlaneMeshPicker("Maze mesh", mazeMesh));
        planeZMeshComboBox.setValue(waveMeshPicker);
        planeZMeshComboBox.valueProperty().addListener(this::updatePlaneZMeshView);
        planeZMeshComboBox.prefWidthProperty().bind(scene.widthProperty());
        var vBox0 = FxSetup.configuredVBox(planeZMeshComboBox);
        var vBox1 = FxSetup.configuredVBox(dampeningSlider, amplitudeSlider);
        var vBox2 = FxSetup.configuredVBox(resolutionXSlider, resolutionYSlider);
        var vBox3 = FxSetup.configuredVBox(meshSizeXSlider, meshSizeYSlider, waveFrequencySlider);
        var controlsRoot = FxSetup.configuredHBox(vBox0, vBox1, vBox2, vBox3);

        FxUtils.allDescendents(controlsRoot).stream()
                .filter(Slider.class::isInstance)
                .map(Slider.class::cast)
                .forEach(s -> configureSlider(scene, s));
        return controlsRoot;
    }

    private void keyTypedAction(KeyEvent e, PlaneZMesh meshView) {
        switch (e.getCharacter()) {
            case "i" -> meshView.setDiffuseAndBumpMapMaterial();
            case "w" -> {
                boolean wireframe = meshView.getDrawMode() == DrawMode.LINE;
                meshView.setDrawMode(wireframe ? DrawMode.FILL : DrawMode.LINE);
            }
            case "r" -> reset(meshView);
            default -> {
                // nothing to do
            }
        }
    }

    private void reset(PlaneZMesh meshView) {
        meshView.setMeshSizeX(INIT_MESH_SIZE_X);
        meshView.setMeshSizeY(INIT_MES_SIZE_Y);
        meshView.setAmplitude(INIT_AMPLITUDE);
        meshView.setFrequency(INIT_FREQUENCY);
        meshView.setResolutionX(INIT_RESOLUTION_X);
        meshView.setResolutionY(INIT_RESOLUTION_Y);
        meshView.setDampening(INIT_DAMPENING_VALUE);
        planeMeshContainer.setTranslateX(0);
        planeMeshContainer.setTranslateY(0);
        planeMeshContainer.setTranslateZ(0);
        mouseControlService.setOrientation(INIT_ANGLE_X, 0);
    }

    private static void configureSlider(Scene scene, Slider slider) {
        slider.setMajorTickUnit(5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.prefWidthProperty().bind(scene.widthProperty());
    }

    private SubScene configuredSubScene() {
        var pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(-50);
        pointLight.setTranslateZ(-1000);

        var subSceneRoot = new Group(planeMeshContainer, pointLight);
        var scene = new SubScene(subSceneRoot, 0, 0, true, SceneAntialiasing.BALANCED);
        var camera = new PerspectiveCamera(true);
        camera.setFarClip(1_000);
        camera.setTranslateZ(-100);
        scene.setCamera(camera);
        return scene;
    }

    private void configureMesh(PlaneZMesh planeZMesh) {
        setMaterial(planeZMesh);
        setTexture(planeZMesh);
        planeZMesh.setDrawMode(DrawMode.FILL);
        planeZMesh.setCullFace(CullFace.NONE);
        planeZMesh.setRotationAxis(Rotate.X_AXIS);
        planeZMesh.setRotate(-90);
        bindPropertiesBidirectional(planeZMesh);
    }

    private void setTexture(PlaneZMesh planeZMesh) {
        var diffuseMapLocation = "images/cup_diffuseMap_1024.png";
        var diffuseMapInputStream = getClass().getResourceAsStream(diffuseMapLocation);
        if (diffuseMapInputStream != null) {
            planeZMesh.setDiffuseMap(new Image(diffuseMapInputStream));
        } else {
            err.println(diffuseMapLocation + " not found");
        }
        var bumpMapLocation = "images/cup_normalMap_1024.png";
        var bumpMapInputStream = getClass().getResourceAsStream(bumpMapLocation);
        if (bumpMapInputStream != null) {
            planeZMesh.setBumpMap(new Image(bumpMapInputStream));
        } else {
            err.println(bumpMapLocation + " not found");
        }
    }

    private static void setMaterial(PlaneZMesh planeZMesh) {
        var material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGRAY);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        planeZMesh.setMaterial(material);
    }

    private void bindPropertiesBidirectional(PlaneZMesh planeZMesh) {
        planeZMesh.amplitudeProperty().bindBidirectional(amplitudeSlider.valueProperty());
        planeZMesh.dampeningProperty().bindBidirectional(dampeningSlider.valueProperty());
        planeZMesh.resolutionXProperty().bindBidirectional(resolutionXSlider.valueProperty());
        planeZMesh.resolutionYProperty().bindBidirectional(resolutionYSlider.valueProperty());
        planeZMesh.meshSizeXProperty().bindBidirectional(meshSizeXSlider.valueProperty());
        planeZMesh.meshSizeYProperty().bindBidirectional(meshSizeYSlider.valueProperty());
        planeZMesh.frequencyProperty().bindBidirectional(waveFrequencySlider.valueProperty());
    }

    @Override
    public void start(Stage primaryStage) {
        var scene = buildScene();
        primaryStage.setTitle("MeshViewer");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(750);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void updatePlaneZMeshView(ObservableValue<? extends PlaneMeshPicker> o,
                                      PlaneMeshPicker curVal, PlaneMeshPicker newVal) {
        planeMeshContainer.getChildren().remove(curVal.getPlaneZMesh());
        planeMeshContainer.getChildren().add(newVal.getPlaneZMesh());
    }

    private static class PlaneMeshPicker {

        private final String displayText;
        private final PlaneZMesh planeZMesh;

        protected PlaneMeshPicker(String displayText, PlaneZMesh planeZMesh) {
            this.displayText = displayText;
            this.planeZMesh = planeZMesh;
        }

        public PlaneZMesh getPlaneZMesh() {
            return planeZMesh;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

}
