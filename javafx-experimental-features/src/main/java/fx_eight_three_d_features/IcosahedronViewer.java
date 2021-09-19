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

import fx_eight_three_d_features.model.Icosahedron;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import service.IMouseControlService;
import service.MouseControlService;

public class IcosahedronViewer extends Application {

    private static final float RESOLUTION = 0.1F;
    private float rotateAngle = 0.0F;


    @SuppressWarnings("SameParameterValue")
    private Scene buildScene(int width, int height, boolean depthBuffer) {
        MeshView icosahedron = getConfiguredIcosahedron();

        PointLight pointLight = getPointLight();

        Group root = new Group(icosahedron, pointLight);
        Scene scene = new Scene(root, width, height, depthBuffer);

        IMouseControlService mouseControlService = new MouseControlService();
        mouseControlService.initMouseControl(icosahedron, scene);

        scene.setOnKeyTyped(e -> keyTypedAction(icosahedron, e));
        return scene;
    }

    private static PointLight getPointLight() {
        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(100);
        pointLight.setTranslateY(-100);
        pointLight.setTranslateZ(50);
        return pointLight;
    }

    private static Icosahedron getConfiguredIcosahedron() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGRAY);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        Icosahedron icosahedron = new Icosahedron(10);
        icosahedron.setCullFace(CullFace.BACK);
        icosahedron.setMaterial(material);
        icosahedron.setRotate(-30);
        icosahedron.setRotationAxis(Rotate.X_AXIS);
        icosahedron.setTranslateZ(200);
        return icosahedron;
    }

    private void keyTypedAction(MeshView icosahedron, KeyEvent e) {
        switch (e.getCharacter()) {
            case "l" -> setDrawMode(icosahedron);
            case "<" -> {
                rotateAngle -= (RESOLUTION * 5);
                icosahedron.setRotate(rotateAngle);
            }
            case ">" -> {
                rotateAngle += (RESOLUTION * 5);
                icosahedron.setRotate(rotateAngle);
            }
            case "X" -> icosahedron.setRotationAxis(Rotate.X_AXIS);
            case "Y" -> icosahedron.setRotationAxis(Rotate.Y_AXIS);
            case "Z" -> icosahedron.setRotationAxis(Rotate.Z_AXIS);
            case "P" -> {
                rotateAngle = 0;
                icosahedron.setRotate(rotateAngle);
            }
            default -> {
                //nothing to do
            }
        }
    }

    private static void setDrawMode(MeshView icosahedron) {
        boolean wireframe = icosahedron.getDrawMode() == DrawMode.LINE;
        icosahedron.setDrawMode(wireframe ? DrawMode.FILL : DrawMode.LINE);
    }


    @Override
    public void start(Stage primaryStage) {
        Scene scene = buildScene(800, 800, true);
        scene.setFill(Color.rgb(10, 10, 40));
        PerspectiveCamera perspectiveCamera = new PerspectiveCamera(true);
        perspectiveCamera.setFarClip(10_000);
        scene.setCamera(perspectiveCamera);
        primaryStage.setTitle("Icosahedron Viewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
