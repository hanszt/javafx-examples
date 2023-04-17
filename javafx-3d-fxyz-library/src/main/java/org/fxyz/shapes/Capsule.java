/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fxyz.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import org.fxyz.shapes.containers.ShapeContainer;
import org.fxyz.shapes.primitives.CapsuleMesh;

/**
 *
 * @author Dub
 */
public final class Capsule extends ShapeContainer<CapsuleMesh> {
    private final CapsuleMesh mesh;
    
    public Capsule() {
        super(new CapsuleMesh());
        this.mesh = getShape();
    }
    
    public Capsule(double radius, double height){
        this();
        mesh.setRadius(radius);
        mesh.setHeight(height);        
    }
    
    public Capsule(Color c){
        this();
        this.setDiffuseColor(c);
    }
    
    public Capsule(double radius, double height, Color c){
        this(radius, height);
        this.setDiffuseColor(c);
    }

    public void setRadius(double value) {
        mesh.setRadius(value);
    }

    public void setHeight(double value) {
        mesh.setHeight(value);
    }

    public void setMaterial(Material value) {
        mesh.setMaterial(value);
    }

    public void setDrawMode(DrawMode value) {
        mesh.setDrawMode(value);
    }

    public void setCullFace(CullFace value) {
        mesh.setCullFace(value);
    }

    public double getRadius() {
        return mesh.getRadius();
    }


    public DoubleProperty radiusProperty() {
        return mesh.radiusProperty();
    }

    public double getHeight() {
        return mesh.getHeight();
    }


    public DoubleProperty heightProperty() {
        return mesh.heightProperty();
    }
    
}
