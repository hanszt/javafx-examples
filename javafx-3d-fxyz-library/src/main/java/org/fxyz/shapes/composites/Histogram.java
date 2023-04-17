/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fxyz.shapes.composites;

import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;

/**
 *
 * @author Sean
 */
public class Histogram extends Group {

    private final Group histogramDataGroup = new Group();
    private final AmbientLight selfLight = new AmbientLight(Color.WHITE);
    private final boolean selfLightEnabled;

    public enum NodeType {
        CYLINDER, CUBE
    }
    private NodeType defaultNodeType = NodeType.CUBE;

    public Histogram(boolean selfLit) {
        selfLightEnabled = selfLit;
        init();
    }

    private void init() {
        getChildren().add(histogramDataGroup);
        if (selfLightEnabled) {
            getChildren().add(selfLight);
        }
        setDepthTest(DepthTest.ENABLE);
    }

    public void setHeightData(float[][] arrayY, int barSize, int spacing,
            Color color, boolean ambient, boolean fill) {
        final var children = histogramDataGroup.getChildren();
        children.clear();
        PhongMaterial phong = new PhongMaterial();
        phong.setSpecularColor(color);
        phong.setDiffuseColor(color);

        float min = arrayY[0][0];
        float max = arrayY[0][0];
        // Find min and max's for color gradient
        for (float[] floats : arrayY) {
            for (int z = 0; z < arrayY[0].length; z++) {
                if (floats[z] < min) {
                    min = floats[z];
                }
                if (floats[z] > max) {
                    max = floats[z];
                }
            }
        }
        float range = max - min; //

        // Generate Bars 
        for (int x = 0; x < arrayY.length; x++) {
            for (int z = 0; z < arrayY[0].length; z++) {
                double ySize = arrayY[x][z];
                if (ySize < 0) {
                    ySize *= -1;
                }
                Node node = createDefaultNode(barSize, ySize);
                node.setTranslateX(x * spacing);
                node.setTranslateY(-arrayY[x][z]);
                node.setTranslateZ(z * spacing);
                //Apply normalized color scale
                double normalizedColor = (arrayY[x][z] - min) / range;
                //if it is in the lower half of the range
                //if it is in the upper half of the range
                phong = arrayY[x][z] < range / 2 ?
                        new PhongMaterial(new Color(0.25, 0, normalizedColor, 1)) :
                        new PhongMaterial(new Color(normalizedColor, 0, 0.25, 1));
                if (node instanceof Cylinder cylinder) {
                    cylinder.setMaterial(phong);
                    cylinder.setCullFace(CullFace.BACK);
                    //show lines only by default
                    cylinder.setDrawMode(fill ? DrawMode.FILL : DrawMode.LINE);
                }
                if (node instanceof Box box) {
                    box.setMaterial(phong);
                    box.setCullFace(CullFace.BACK);
                    //show lines only by default
                    box.setDrawMode(fill ? DrawMode.FILL : DrawMode.LINE);
                }

                if (ambient) {
                    AmbientLight light = new AmbientLight(color);
                    light.getScope().add(node);
                    children.add(light);
                }
                children.add(node);
            }
        }
    }

    private Node createDefaultNode(double barWidth, double barHeight) {
        return switch (defaultNodeType) {
            case CYLINDER -> new Cylinder(barWidth / 2, barHeight);
            case CUBE -> new Box(barWidth, barHeight, barWidth);
        };
    }

    /**
     * @return the defaultNodeType
     */
    public NodeType getDefaultNodeType() {
        return defaultNodeType;
    }

    /**
     * @param defaultNodeType the defaultNodeType to set
     */
    public void setDefaultNodeType(NodeType defaultNodeType) {
        this.defaultNodeType = defaultNodeType;
    }
}
