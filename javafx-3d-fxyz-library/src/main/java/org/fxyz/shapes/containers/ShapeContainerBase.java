/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fxyz.shapes.containers;

import javafx.collections.ObservableList;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

/**
 * Basic Group for holding FX(yz) primitive MeshViews
 * Allows for selfIllumination and emissiveLighting.
 * Lazily implemented Transforms: Rotate(x,y,z), Translate, Scale.
 *
 * @author Dub
 * @param <T> the primitiveMeshClass this container implements
 */
public interface ShapeContainerBase<T extends MeshView>{
    
    
    default void initialize(){
        getSelfIlluminationLight().setLightOn(false);
        getEmissiveLight().setLightOn(false);
        
        getShape().setMaterial(getMaterial());
        getContainer().getChildren().addAll(getSelfIlluminationLight(), getEmissiveLight(), getShape());
    }
    
    
    /*
     * 
     * Material and Lighting methods 
     * 
     */
    default void setEmissiveLightingColor(Color value) {
        getEmissiveLight().setColor(value);
    }

    default Color getEmissiveLightingColor() {
        return getEmissiveLight().getColor();
    }

    default void setEmissiveLightingOn(boolean value) {
        getEmissiveLight().setLightOn(value);
    }

    default boolean isEmissiveLightingOn() {
        return getEmissiveLight().isLightOn();
    }

    default ObservableList<Node> getEmissiveLightingScope() {
        return getEmissiveLight().getScope();
    }

    
    default void setDiffuseColor(Color value) {
        getMaterial().setDiffuseColor(value);
    }

    default Color getDiffuseColor() {
        return getMaterial().getDiffuseColor();
    }

    default void setSpecularColor(Color value) {
        getMaterial().setSpecularColor(value);
    }

    default Color getSpecularColor() {
        return getMaterial().getSpecularColor();
    }

    default void setSpecularPower(double value) {
        getMaterial().setSpecularPower(value);
    }

    default double getSpecularPower() {
        return getMaterial().getSpecularPower();
    }

    default void setDiffuseMap(Image value) {
        getMaterial().setDiffuseMap(value);
    }

    default Image getDiffuseMap() {
        return getMaterial().getDiffuseMap();
    }

    default void setSpecularMap(Image value) {
        getMaterial().setSpecularMap(value);
    }

    default Image getSpecularMap() {
        return getMaterial().getSpecularMap();
    }

    default void setBumpMap(Image value) {
        getMaterial().setBumpMap(value);
    }

    default Image getBumpMap() {
        return getMaterial().getBumpMap();
    }

    default void setSelfIlluminationMap(Image value) {
        getMaterial().setSelfIlluminationMap(value);
    }

    default Image getSelfIlluminationMap() {
        return getMaterial().getSelfIlluminationMap();
    }

    
    
    default void setSelfIlluminationColor(Color value) {
        getSelfIlluminationLight().setColor(value);
    }

    default Color getSelfIlluminationColor() {
        return getSelfIlluminationLight().getColor();
    }

    default void setSelfIlluminationLightOn(boolean value) {
        getSelfIlluminationLight().setLightOn(value);
    }

   default boolean isSelfIlluminationLightOn() {
        return getSelfIlluminationLight().isLightOn();
    }
    
    T getShape();
    Group getContainer();
    PhongMaterial getMaterial();
    PointLight getEmissiveLight();
    AmbientLight getSelfIlluminationLight();
    
}
