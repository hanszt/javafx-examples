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
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import service.IMouseControlService;
import service.MouseControlService;

import java.util.Arrays;

import static java.lang.System.err;

public class MeshViewerOld extends Application {

    Group root;
    TriangleMesh triMesh;

    int divX = 100;
    int divY = 100;
    boolean smooth = true;
    int[] smoothGroups;
    float rotateAngle = 0.0f;
    float funcValue = -10.0f;
    float resolution = 0.1f;
    boolean texture = false;
    boolean textureSwitch = false;
    final Image diffuseMap = new Image(getClass().getResourceAsStream("images/cup_diffuseMap_1024.png"));
    final Image bumpMap = new Image(getClass().getResourceAsStream("images/cup_normalMap_1024.png"));

    static final ToysVec3f v1 = new ToysVec3f();
    static final ToysVec3f v2 = new ToysVec3f();
    static void computeNormal(ToysVec3f pa, ToysVec3f pb, ToysVec3f pc, ToysVec3f normal) {
        // compute Normal |(v1-v0)X(v2-v0)|
        v1.sub(pb, pa);
        v2.sub(pc, pa);
        normal.cross(v1, v2);
        normal.normalize();
    }

    static final float MESH_SCALE = 20;
    static final float MIN_X = -10;
    static final float MIN_Y = -10;
    static final float MAX_X = 10;
    static final float MAX_Y = 10;

    double getSinDivX(double x, double y) {
        double r = Math.sqrt(x*x + y*y);
        return funcValue * (r == 0 ? 1 : Math.sin(r) / r);
    }

    void computeMesh(int subDivX, int subDivY, float scale) {
         final int pointSize = 3;
        int numDivX = subDivX + 1;
        float[] points = triMesh.getPoints().toArray(null);
        float[] normals = triMesh.getNormals().toArray(null);
        int[] faces = triMesh.getFaces().toArray(null);

        // Initial points and texCoords
        for (int y = 0; y <= subDivY; y++) {
            float dy = (float) y / subDivY;
            double fy = (1 - dy) * MIN_Y + dy * MAX_Y;
            for (int x = 0; x <= subDivX; x++) {
                float dx = (float) x / subDivX;
                double fx = (1 - dx) * MIN_X + dx * MAX_X;
                int index = y * numDivX * pointSize + (x * pointSize);
                points[index] = (float) fx * scale;
                points[index + 1] = (float) fy * scale;
                points[index + 2] = (float) getSinDivX(fx, fy) * scale;
            }
        }
        triMesh.getPoints().set(0, points, 0, points.length);

        ToysVec3f[] triPoints = new ToysVec3f[3];
        triPoints[0] = new ToysVec3f();
        triPoints[1] = new ToysVec3f();
        triPoints[2] = new ToysVec3f();
        ToysVec3f normal = new ToysVec3f();

        for (int index = 0; index < faces.length; index += 9) {
            int pIndex = faces[index] * 3;
            triPoints[0].x = points[pIndex];
            triPoints[0].y = points[pIndex + 1];
            triPoints[0].z = points[pIndex + 2];
            pIndex = faces[index + 3] * 3;
            triPoints[1].x = points[pIndex];
            triPoints[1].y = points[pIndex + 1];
            triPoints[1].z = points[pIndex + 2];
            pIndex = faces[index + 6] * 3;
            triPoints[2].x = points[pIndex];
            triPoints[2].y = points[pIndex + 1];
            triPoints[2].z = points[pIndex + 2];

            computeNormal(triPoints[0], triPoints[1], triPoints[2], normal);
            assert (faces[index + 1] == faces[index + 4]) && (faces[index + 1] == faces[index + 7]);
            int nIndex = faces[index + 1] * 3;
            normals[nIndex] = normal.x;
            normals[nIndex + 1] = normal.y;
            normals[nIndex + 2] = normal.z;
        }

        triMesh.getNormals().set(0, normals, 0, normals.length);

    }

    TriangleMesh buildTriangleMesh(int subDivX, int subDivY, float scale) {

        final int pointSize = 3;
        final int normalSize = 3;
        final int texCoordSize = 2;
        final int faceSize = 9; // 3 point indices, 3 normal indices and 3 texCoord indices per triangle
        int numDivX = subDivX + 1;
        int numVerts = (subDivY + 1) * numDivX;
        float[] points = new float[numVerts * pointSize];
        float[] texCoords = new float[numVerts * texCoordSize];
        int faceCount = subDivX * subDivY * 2;
        float[] normals = new float[faceCount * normalSize];
        int[] faces = new int[ faceCount * faceSize];

        // Create points and texCoords
        for (int y = 0; y <= subDivY; y++) {
            float dy = (float) y / subDivY;
            double fy = (1 - dy) * MIN_Y + dy * MAX_Y;

            for (int x = 0; x <= subDivX; x++) {
                float dx = (float) x / subDivX;
                double fx = (1 - dx) * MIN_X + dx * MAX_X;
                int index = y * numDivX * pointSize + (x * pointSize);

                points[index] = (float) fx * scale;
                points[index + 1] = (float) fy * scale;
                points[index + 2] = (float) getSinDivX(fx, fy) * scale;
                index = y * numDivX * texCoordSize + (x * texCoordSize);

                texCoords[index] = dx;
                texCoords[index + 1] = dy;
            }
        }

        // Initial faces and normals
        int normalCount = 0;
        ToysVec3f[] triPoints = new ToysVec3f[3];
        triPoints[0] = new ToysVec3f();
        triPoints[1] = new ToysVec3f();
        triPoints[2] = new ToysVec3f();
        ToysVec3f normal = new ToysVec3f();
        for (int y = 0; y < subDivY; y++) {
            for (int x = 0; x < subDivX; x++) {
                int p00 = y * numDivX + x;
                int p01 = p00 + 1;
                int p10 = p00 + numDivX;
                int p11 = p10 + 1;
                int tc00 = y * numDivX + x;
                int tc01 = tc00 + 1;
                int tc10 = tc00 + numDivX;
                int tc11 = tc10 + 1;

                int ii = p00 * 3;
                triPoints[0].x = points[ii];
                triPoints[0].y = points[ii + 1];
                triPoints[0].z = points[ii + 2];
                ii = p10 * 3;
                triPoints[1].x = points[ii];
                triPoints[1].y = points[ii + 1];
                triPoints[1].z = points[ii + 2];
                ii = p11 * 3;
                triPoints[2].x = points[ii];
                triPoints[2].y = points[ii + 1];
                triPoints[2].z = points[ii + 2];
                computeNormal(triPoints[0], triPoints[1], triPoints[2], normal);

                int normalIndex = normalCount * normalSize;
                normals[normalIndex] = normal.x; //nx
                normals[normalIndex + 1] = normal.y; //ny
                normals[normalIndex + 2] = normal.z; //nz

                int index = (y * subDivX * faceSize + (x * faceSize)) * 2;

                faces[index] = p00;
                faces[index + 1] = normalCount;
                faces[index + 2] = tc00;
                faces[index + 3] = p10;
                faces[index + 4] = normalCount;
                faces[index + 5] = tc10;
                faces[index + 6] = p11;
                faces[index + 7] = normalCount++;
                faces[index + 8] = tc11;
                index += faceSize;

                ii = p11 * 3;
                triPoints[0].x = points[ii];
                triPoints[0].y = points[ii + 1];
                triPoints[0].z = points[ii + 2];
                ii = p01 * 3;
                triPoints[1].x = points[ii];
                triPoints[1].y = points[ii + 1];
                triPoints[1].z = points[ii + 2];
                ii = p00 * 3;
                triPoints[2].x = points[ii];
                triPoints[2].y = points[ii + 1];
                triPoints[2].z = points[ii + 2];
                computeNormal(triPoints[0], triPoints[1], triPoints[2], normal);

                normalIndex = normalCount * normalSize;
                normals[normalIndex] = normal.x; //nx
                normals[normalIndex + 1] = normal.y; //ny
                normals[normalIndex + 2] = normal.z; //nz

                faces[index] = p11;
                faces[index + 1] = normalCount;
                faces[index + 2] = tc11;
                faces[index + 3] = p01;
                faces[index + 4] = normalCount;
                faces[index + 5] = tc01;
                faces[index + 6] = p00;
                faces[index + 7] = normalCount++;
                faces[index + 8] = tc00;
            }
        }

        TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
        triangleMesh.getPoints().setAll(points);
        triangleMesh.getNormals().setAll(normals);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);

        return triangleMesh;
    }

    @SuppressWarnings("SameParameterValue")
    private Scene buildScene(int width, int height, boolean depthBuffer) {

        triMesh = buildTriangleMesh(divX, divY, MESH_SCALE);
        smoothGroups = new int[divX * divY * 2];
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.LIGHTGRAY);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        MeshView meshView = getConfiguredMeshView(material);

        PointLight pointLight = new PointLight(Color.ANTIQUEWHITE);
        pointLight.setTranslateX(300);
        pointLight.setTranslateY(-50);
        pointLight.setTranslateZ(-1000);

        root = new Group(meshView, pointLight);
        Scene scene = new Scene(root, width, height, depthBuffer);
        scene.setOnKeyTyped(e -> keyTypedAction(meshView, e));
        IMouseControlService mouseControlService = new MouseControlService();
        mouseControlService.initMouseControl(meshView, scene);
        return scene;
    }

    private MeshView getConfiguredMeshView(PhongMaterial material) {
        MeshView waveMesh = new MeshView();
        waveMesh.setMesh(triMesh);
        waveMesh.setMaterial(material);
        //Set Wireframe mode
        waveMesh.setDrawMode(DrawMode.FILL);
        waveMesh.setCullFace(CullFace.BACK);

        waveMesh.setRotate(-30);
        waveMesh.setRotationAxis(Rotate.X_AXIS);
        waveMesh.setTranslateX(400);
        waveMesh.setTranslateY(400);
        waveMesh.setTranslateZ(10);
        return waveMesh;
    }

    private void keyTypedAction(MeshView meshView, KeyEvent e) {
        PhongMaterial material = (PhongMaterial) meshView.getMaterial();
        switch (e.getCharacter()) {
            case "[":
                funcValue -= resolution;
                if (funcValue < -20.0f) {
                    funcValue = -20.0f;
                }
                computeMesh(divX, divY, MESH_SCALE);
                break;
            case "]":
                funcValue += resolution;
                if (funcValue > 20.0f) {
                    funcValue = 20.0f;
                }
                computeMesh(divX, divY, MESH_SCALE);
                break;
            case "p":
                funcValue = -10;
                computeMesh(divX, divY, MESH_SCALE);
                break;
            case "i":
                err.print("i ");
                setDiffuseAndBumpMapMaterial(material);
                break;
            case "s":
                smoothMesh();
                break;
            case "k":
                setDiffuseMapMaterial(material);
                break;
            case "u":
                setDiffuseMap(material);
                break;
            case "l":
                boolean wireframe = meshView.getDrawMode() == DrawMode.LINE;
                meshView.setDrawMode(wireframe ? DrawMode.FILL : DrawMode.LINE);
                break;
            case "<":
                meshView.setRotate(rotateAngle -= (resolution * 5));
                break;
            case ">":
                meshView.setRotate(rotateAngle += (resolution * 5));
                break;
            case "X":
                meshView.setRotationAxis(Rotate.X_AXIS);
                break;
            case "Y":
                meshView.setRotationAxis(Rotate.Y_AXIS);
                break;
            case "Z":
                meshView.setRotationAxis(Rotate.Z_AXIS);
                break;
            case "P":
                rotateAngle = 0;
                meshView.setRotate(rotateAngle);
                break;
            case "1":
                err.print("1 ");
                smoothMesh(meshView, 5);
                break;
            case "2":
                err.print("2 ");
                smoothMesh(meshView, 70);
                break;
            case " ":
                root.getChildren().add(new Button("Button"));
                break;

            default:
                break;
        }
    }

    private void setDiffuseMap(PhongMaterial material) {
        if (texture) {
            material.setDiffuseMap(null);
            material.setBumpMap(bumpMap);
            material.setDiffuseColor(Color.LIGHTGRAY);
                                textureSwitch = true;
        } else {
            material.setDiffuseMap(null);
            material.setBumpMap(null);
            material.setDiffuseColor(Color.LIGHTGRAY);
        }
    }

    private void smoothMesh() {
        smooth = !smooth;
        if (smooth) {
            Arrays.fill(smoothGroups, 0);
        } else {
            for (int i = 0; i < smoothGroups.length; i++) {
                smoothGroups[i] = i % 32;
            }
        }
        triMesh.getFaceSmoothingGroups().setAll(smoothGroups);
    }

    private void setDiffuseMapMaterial(PhongMaterial material) {
        if ((texture) || (!textureSwitch)) {
           material.setDiffuseMap(diffuseMap);
           material.setBumpMap(null);
           material.setDiffuseColor(Color.WHITE);
           texture = true;
           textureSwitch = true;
       } else {
           material.setDiffuseMap(null);
           material.setBumpMap(null);
           material.setDiffuseColor(Color.LIGHTGRAY);
       }
    }

    private void setDiffuseAndBumpMapMaterial(PhongMaterial material) {
        if (!textureSwitch) {
            texture = !texture;
        } else {
            textureSwitch = false;
        }
        if (texture) {
            material.setDiffuseMap(diffuseMap);
            material.setBumpMap(bumpMap);
            material.setDiffuseColor(Color.WHITE);
        } else {
            material.setDiffuseMap(null);
            material.setBumpMap(null);
            material.setDiffuseColor(Color.LIGHTGRAY);
        }
    }

    private void smoothMesh(MeshView meshView, int i2) {
        divX = i2;
        divY = i2;
        smooth = true;
        smoothGroups = new int[divX * divY * 2];
        rotateAngle = 0.0f;
        funcValue = 0.0f;
        triMesh = buildTriangleMesh(divX, divY, MESH_SCALE);
        meshView.setMesh(triMesh);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = buildScene(800, 800, true);
        scene.setFill(Color.rgb(10, 10, 40));
        scene.setCamera(new PerspectiveCamera());
        primaryStage.setTitle("PNT MeshViewer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
