/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fxyz.shapes;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import org.fxyz.shapes.containers.ShapeContainer;
import org.fxyz.shapes.primitives.TorusMesh;

/**
 *
 * @author JDub <a href="https://github.com/jdub1581">jdub repo</a>
 * Refactored by Sean. Refactored again by jdub
 */
public final class Torus extends ShapeContainer<TorusMesh> {

    private final TorusMesh mesh;

    public Torus() {
        super(new TorusMesh());
        this.mesh = getShape();        
    }
    
    public Torus(int rDivs, int tDivs, double radius, double tRadius) {
        this();
        mesh.setRadiusDivisions(rDivs);
        mesh.setTubeDivisions(tDivs);
        mesh.setRadius(radius);
        mesh.setTubeRadius(tRadius);
    }
    
    public Torus(int rDivs, int tDivs, double radius, double tRadius, Color c) {
        this(rDivs, tDivs,radius, tRadius);
        this.setDiffuseColor(c);
    }

    public double getRadius() {
        return mesh.getRadius();
    }

    public void setRadius(double value) {
        mesh.setRadius(value);
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
}
