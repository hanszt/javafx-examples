/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.shapes.composites;

import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import org.fxyz.utils.ListOfOne;

import java.util.List;

/**
 * @author Sean
 *
 * Provides a simple but configurable Group object whose children
 * are each nodes rendered in 3D space.  The data structure allows for each
 * of the three axes to be independently changed there by affording the user to
 * create variations on the view dynamically.
 */
public class ScatterPlot extends Group {

    private final Group scatterDataGroup = new Group();
    private final AmbientLight selfLight = new AmbientLight(Color.WHITE);
    private final boolean selfLightEnabled;
    private final double nodeRadius;
    private final NodeType defaultNodeType;

    public enum NodeType {SPHERE, CUBE}


    public ScatterPlot(double nodeRadius, boolean selfLit) {
        selfLightEnabled = selfLit;
        this.nodeRadius = nodeRadius;
        defaultNodeType = NodeType.SPHERE;
        init();
    }

    private void init() {
        getChildren().add(scatterDataGroup); //Holds ScatterPlot data        
        if (selfLightEnabled) {
            getChildren().add(selfLight);
        }
        setDepthTest(DepthTest.ENABLE);
    }

    public void setXYZData(List<Point3D> data) {
        setXYZData(data, new ListOfOne<>(Color.WHITE, data.size()));
    }

    public void setXYZData(List<Point3D> data, List<Color> colors) {
        scatterDataGroup.getChildren().clear();
        //for now we will always default to x axis
        //later we could maybe dynamically determine the smallest axis and then
        //uses 0's for the other axes that are larger.
        for (int i = 0; i < data.size(); i++) {
            final var point3D = data.get(i);
            final Shape3D shape3D = createDefaultNode(nodeRadius);
            shape3D.setTranslateX(point3D.getX());
            shape3D.setTranslateY(point3D.getY());
            shape3D.setTranslateZ(point3D.getZ());
            shape3D.setMaterial(new PhongMaterial(colors.get(i)));
            scatterDataGroup.getChildren().add(shape3D);
        }
    }

    private Shape3D createDefaultNode(double size) {
        return switch (defaultNodeType) {
            case SPHERE -> new Sphere(size);
            case CUBE -> new Box(size, size, size);
        };
    }
}
