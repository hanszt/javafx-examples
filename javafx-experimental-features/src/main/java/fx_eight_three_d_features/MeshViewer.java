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

import fx_eight_three_d_features.model.ParabolaMesh;
import fx_eight_three_d_features.model.PlaneZMesh;
import fx_eight_three_d_features.model.WaveMesh;
import fx_eight_three_d_features.utils.FxSetup;
import fx_eight_three_d_features.utils.FxUtils;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
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

    private static final int INIT_ANGLE_X = 30;
    private static final int INIT_AMPLITUDE = -10;
    private static final int INIT_RESOLUTION_X = 100;
    private static final int INIT_RESOLUTION_Y = 100;
    private static final double INIT_DAMPENING_VALUE = 1;
    private static final int INIT_MESH_SIZE_X = 20;
    private static final int INIT_MES_SIZE_Y = 20;
    private static final int INIT_FREQUENCY = 1;

    private final Group planeMeshContainer = new Group();

    IMouseControlService mouseControlService = new MouseControlService();
    private final ComboBox<PlaneMeshPicker> planeZMeshComboBox = new ComboBox<>();
    private final Slider dampeningSlider = new Slider(0, 1, INIT_DAMPENING_VALUE);
    private final Slider amplitudeSlider = new Slider(-20, 20, INIT_AMPLITUDE);
    private final Slider resolutionXSlider = new Slider(0, 200, INIT_RESOLUTION_X);
    private final Slider resolutionYSlider = new Slider(0, 200, INIT_RESOLUTION_Y);
    private final Slider meshSizeXSlider = new Slider(0, 100, INIT_MESH_SIZE_X);
    private final Slider meshSizeYSlider = new Slider(0, 100, INIT_MES_SIZE_Y);
    private final Slider waveFrequencySlider = new Slider(-3, 3, INIT_FREQUENCY);

    @SuppressWarnings("SameParameterValue")
    private Scene buildScene(int width, int height, boolean depthBuffer) {
        VBox root = FxSetup.configuredVBox();
        root.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 40), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root, width, height, depthBuffer);

        PlaneZMesh waveMesh = new WaveMesh();
        PlaneZMesh parabolaMesh = new ParabolaMesh();
        HBox controlsPane = buildControls(scene, waveMesh, parabolaMesh);
        controlsPane.setOpacity(.8);

        planeMeshContainer.getChildren().add(planeZMeshComboBox.getValue().getPlaneZMesh());
        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(-50);
        pointLight.setTranslateZ(-1000);

        Group subSceneRoot = new Group(planeMeshContainer, pointLight);

        final SubScene subScene = configuredSubScene(subSceneRoot);
        subScene.widthProperty().bind(scene.widthProperty());
        subScene.heightProperty().bind(scene.heightProperty().multiply(0.75));

        root.getChildren().addAll(subScene, controlsPane);

        scene.setOnKeyTyped(e -> keyTypedAction(e, planeZMeshComboBox.getValue().getPlaneZMesh()));
        configurePlaneZMeshes(waveMesh, parabolaMesh);
        mouseControlService.setOrientation(INIT_ANGLE_X, 0);
        mouseControlService.initMouseControl(planeMeshContainer, scene);
        return scene;
    }

    private void configurePlaneZMeshes(PlaneZMesh... planeZMeshes) {
        for (PlaneZMesh zMesh : planeZMeshes) {
            configureMesh(zMesh);
        }
    }

    private HBox buildControls(Scene scene, PlaneZMesh waveMesh, PlaneZMesh parabolaMesh) {
        PlaneMeshPicker waveMeshPicker = new PlaneMeshPicker("Wave mesh", waveMesh);
        planeZMeshComboBox.getItems().addAll(waveMeshPicker, new PlaneMeshPicker("Parabola mesh", parabolaMesh));
        planeZMeshComboBox.setValue(waveMeshPicker);
        planeZMeshComboBox.valueProperty().addListener(this::planeZComboBoxChangedAction);

        VBox vBox0 = FxSetup.configuredVBox(planeZMeshComboBox);
        VBox vBox1 = FxSetup.configuredVBox(dampeningSlider, amplitudeSlider);
        VBox vBox2 = FxSetup.configuredVBox(resolutionXSlider, resolutionYSlider);
        VBox vBox3 = FxSetup.configuredVBox(meshSizeXSlider, meshSizeYSlider, waveFrequencySlider);
        HBox controlsRoot = FxSetup.configuredHBox(vBox0, vBox1, vBox2, vBox3);
        FxUtils.getAllNodes(controlsRoot).stream().filter(n -> n instanceof Slider)
                .map(n -> (Slider) n).forEach(s -> configureSlider(scene, s));
        return controlsRoot;
    }

    private void keyTypedAction(KeyEvent e, PlaneZMesh meshView) {
        switch (e.getCharacter()) {
            case "i":
                meshView.setDiffuseAndBumpMapMaterial();
                break;
            case "w":
                boolean wireframe = meshView.getDrawMode() == DrawMode.LINE;
                meshView.setDrawMode(wireframe ? DrawMode.FILL : DrawMode.LINE);
                break;
            case "r":
                reset(meshView);
                break;
            default:
                break;
        }
    }

    private void reset(PlaneZMesh meshView) {
        meshView.setMeshSizeX(INIT_MESH_SIZE_X);
        meshView.setMeshSizeY(INIT_MES_SIZE_Y);
        meshView.setAmplitude(INIT_AMPLITUDE);
        meshView.setWaveFrequency(INIT_FREQUENCY);
        meshView.setResolutionX(INIT_RESOLUTION_X);
        meshView.setResolutionY(INIT_RESOLUTION_Y);
        meshView.setDampening(INIT_DAMPENING_VALUE);
        planeMeshContainer.setTranslateX(0);
        planeMeshContainer.setTranslateY(0);
        planeMeshContainer.setTranslateZ(0);
        mouseControlService.setOrientation(INIT_ANGLE_X, 0);
    }

    private void configureSlider(Scene scene, Slider slider) {
        slider.setMajorTickUnit(5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.prefWidthProperty().bind(scene.widthProperty());
    }

    private SubScene configuredSubScene(Group subSceneRoot) {
        SubScene scene = new SubScene(subSceneRoot, 0, 0, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(1_000);
        camera.setTranslateZ(-100);
        scene.setCamera(camera);
        return scene;
    }

    private void configureMesh(PlaneZMesh planeZMesh) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGRAY);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        planeZMesh.setMaterial(material);
        planeZMesh.setDiffuseMap(new Image(getClass().getResourceAsStream("images/cup_diffuseMap_1024.png")));
        planeZMesh.setBumpMap(new Image(getClass().getResourceAsStream("images/cup_normalMap_1024.png")));
        planeZMesh.setDrawMode(DrawMode.FILL);
        planeZMesh.setCullFace(CullFace.NONE);
        planeZMesh.setRotationAxis(Rotate.X_AXIS);
        planeZMesh.setRotate(-90);
        planeZMesh.amplitudeProperty().bindBidirectional(amplitudeSlider.valueProperty());
        planeZMesh.dampeningProperty().bindBidirectional(dampeningSlider.valueProperty());
        planeZMesh.resolutionXProperty().bindBidirectional(resolutionXSlider.valueProperty());
        planeZMesh.resolutionYProperty().bindBidirectional(resolutionYSlider.valueProperty());
        planeZMesh.meshSizeXProperty().bindBidirectional(meshSizeXSlider.valueProperty());
        planeZMesh.meshSizeYProperty().bindBidirectional(meshSizeYSlider.valueProperty());
        planeZMesh.waveFrequencyProperty().bindBidirectional(waveFrequencySlider.valueProperty());
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

    private void planeZComboBoxChangedAction(ObservableValue<? extends PlaneMeshPicker> o,
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
