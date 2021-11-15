/*
 * Copyright (C) 2014 F(Y)zx :
 * Authored by : Jason Pollastrini aka jdub1581,
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fxyz.tests;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;
import javafx.stage.Stage;
import org.fxyz.cameras.CameraTransformer;
import org.fxyz.shapes.complex.cloth.ClothMesh;
import org.jetbrains.annotations.NotNull;
import service.IMouseControlService;
import service.MouseControlService;

import java.util.Optional;

/**
 * @author Jason Pollastrini aka jdub1581
 */
public class ClothMeshTest extends Application {

    private final IMouseControlService mouseControlService = new MouseControlService();
    private final CameraTransformer cameraTransform = new CameraTransformer();

    @Override
    public void start(Stage stage) throws Exception {
        var camera = buildPerspectiveCamera();
        var light = new PointLight(Color.LIGHTSKYBLUE);
        light.translateXProperty().bind(camera.translateXProperty());
        light.translateYProperty().bind(camera.translateYProperty());
        light.translateZProperty().bind(camera.translateZProperty());

        var cloth = buildCloth();
        var root = new StackPane();
        root.setPickOnBounds(false);

        PointLight light2 = new PointLight(Color.GAINSBORO);
        light2.setTranslateZ(-1500);
        PointLight light3 = new PointLight(Color.AZURE);
        light3.setTranslateZ(2500);
        Group group = new Group(cameraTransform, cloth, light2, light3);

        root.getChildren().add(group);
        Scene scene = new Scene(root, 1200, 600, true, SceneAntialiasing.BALANCED);

        Stop[] stops = new Stop[]{new Stop(0, Color.BLACK), new Stop(1, Color.RED)};
        LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        scene.setFill(lg);
        scene.setCamera(camera);
        scene.setOnKeyTyped(keyEvent -> keyTypedAction(keyEvent, cloth));
        addMouseControl(cloth, scene);
        stage.setScene(scene);
        stage.show();
        cloth.startSimulation();
    }

    @NotNull
    private static ClothMesh buildCloth() {
        ClothMesh cloth = new ClothMesh();
        cloth.setPerPointMass(10);
        cloth.setBendStrength(0.5);
        cloth.setStretchStrength(1.0);
        cloth.setShearStrength(0.55);
        cloth.setDrawMode(DrawMode.LINE);
        cloth.setCullFace(CullFace.NONE);
        cloth.setDiffuseMap(new Image("https://kenai.com/attachments/wiki_images/duke/Duke3DprogressionSmall.jpg"));
        cloth.setSpecularPower(5);
        return cloth;
    }

    private void addMouseControl(Node model3D, Scene scene) {
        mouseControlService.initMouseControl(model3D, scene);
        mouseControlService.setOrientation(0, 0, 90);
    }

    private void keyTypedAction(KeyEvent e, ClothMesh clothMesh) {
        switch (e.getCharacter()) {
            case "i" -> setDiffuseAndBumpMap(clothMesh);
            case "w" -> setDrawMode(clothMesh);
            case "r" -> reset();
            default -> {
            }
        }
    }

    private void reset() {
        mouseControlService.setTargetTranslation(Point3D.ZERO);
        mouseControlService.setOrientation(0, 0, 0);
    }

    private void setDiffuseAndBumpMap(ClothMesh clothMesh) {
        if (clothMesh.getDiffuseMap() == null && clothMesh.getBumpMap() == null) {
            Optional.ofNullable(getClass().getResourceAsStream("/images/cup_diffuseMap_1024.png"))
                    .ifPresent(resourceAsStream -> clothMesh.setDiffuseMap(new Image(resourceAsStream)));
            Optional.ofNullable(getClass().getResourceAsStream("/images/cup_normalMap_1024.png"))
                    .ifPresent(resourceAsStream -> clothMesh.setBumpMap(new Image(resourceAsStream)));
            clothMesh.setDiffuseColor(Color.WHITE);
        } else {
            clothMesh.setDiffuseMap(null);
            clothMesh.setBumpMap(null);
            clothMesh.setDiffuseColor(Color.LIGHTBLUE);
        }
    }

    private static void setDrawMode(Shape3D shape3D) {
        shape3D.setDrawMode(shape3D.getDrawMode() == DrawMode.LINE ? DrawMode.FILL : DrawMode.LINE);
    }

    private PerspectiveCamera buildPerspectiveCamera() {
        var camera = new PerspectiveCamera(true);
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().addAll(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(1000000.0);
        camera.setFieldOfView(42);
        camera.setVerticalFieldOfView(true);
        camera.setTranslateZ(-1500);
        return camera;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
