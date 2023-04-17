/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import org.fxyz.shapes.containers.ShapeContainer;
import org.fxyz.shapes.primitives.ConeMesh;

/**
 *
 * @author Birdasaur
 * @adapted Dub's Capsule class served as an example framework to adapt 
 * my existing Cone code.
 */
public final class Cone extends ShapeContainer<ConeMesh>{
     private ConeMesh mesh;
    
    public Cone() {
        super(new ConeMesh());
        this.mesh = getShape();
    }
    public Cone(int divisions, double radius, double height){
        this();
        mesh.setDivisions(divisions);
        mesh.setRadius(radius);
        mesh.setHeight(height);        
    }
    public Cone(Color c){
        this();
        this.setDiffuseColor(c);
    }
    public Cone(int divisions, double radius, double height, Color c){
        //this(divisions, radius, height);
        super(new ConeMesh(divisions, radius, height));
        this.mesh = getShape();
        this.setDiffuseColor(c);
    }
    public void setRadius(double value) {
        mesh.setRadius(value);
    }
    public void setHeight(double value) {
        mesh.setHeight(value);
    }
    public void setDivisions(int value) {
        mesh.setDivisions(value);
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
    public int getDivisions() {
        return mesh.getDivisions();
    }
    public IntegerProperty divisionsProperty() {
        return mesh.divisionsProperty();
    }       
}
