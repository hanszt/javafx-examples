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

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import service.IMouseControlService;
import service.MouseControlService;

public class SimpleMeshTexCoordTest extends Application {

    static TriangleMesh buildTriangleMesh() {

        // Create points
        float[] points = {
            0.0f, 0.0f, 0.0f,
            400.0f, 0.0f, 0.0f,
            0.0f, 400.0f, 0.0f,
            400.0f, 400.0f, 0.0f};

        // Create texCoords
        float[] texCoords = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
        };

        // Create faces
        // Interleaving 3 point indices and 3 texCoord indices per triangle
        int[] faces = {
            // Triangle #1
            0, 0, 2, 2, 3, 3,
            // Triangle #2
            3, 3, 1, 1, 0, 0
        };

        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getPoints().setAll(points);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        return triangleMesh;
    }

    private Scene buildScene() {
        TriangleMesh triMesh = buildTriangleMesh();
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("images/cup_diffuseMap_1024.png")));
        material.setSpecularColor(Color.rgb(30, 30, 30));
        MeshView meshView = new MeshView(triMesh);
        meshView.setTranslateX(200);
        meshView.setTranslateY(200);
        meshView.setTranslateZ(20);
        meshView.setMaterial(material);
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setCullFace(CullFace.BACK);

        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(150);
        pointLight.setTranslateY(-100);
        pointLight.setTranslateZ(-1000);

        Group root = new Group(meshView, pointLight);
        Scene scene = new Scene(root, 800, 800, true);
        scene.setFill(Color.GRAY);
        scene.setCamera(new PerspectiveCamera());
        IMouseControlService mouseControlService = new MouseControlService();
        mouseControlService.initMouseControl(meshView, scene);
        return scene;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SimpleMeshTexCoordTest");
        primaryStage.setScene(buildScene());
        primaryStage.show();
    }

}
