/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.cameras;
// 
// Xform.java is a class that extends the Group class. It is used in the 
// MoleculeSampleApp application that is built using the Getting Started with JavaFX
// 3D Graphics tutorial. The method allows you to add your own transforms and rotation.
// 
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public final class CameraTransformer extends Group {

    public enum RotateOrder {
        XYZ, XZY, YXZ, YZX, ZXY, ZYX
    }

    public final Translate translate = new Translate();
    public final Translate pivot = new Translate();
    public final Translate inversePivot = new Translate();
    public final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    public final Rotate rotateY = new Rotate(0 ,Rotate.Y_AXIS);
    public final Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    public final Scale scale = new Scale();

    public CameraTransformer() {
        super();
        getTransforms().addAll(translate, rotateZ, rotateY, rotateX, scale);
    }

    public CameraTransformer(RotateOrder rotateOrder) {
        super();
        // choose the order of rotations based on the rotateOrder
        switch (rotateOrder) {
            case XYZ -> getTransforms().addAll(translate, pivot, rotateZ, rotateY, rotateX, scale, inversePivot);
            case XZY -> getTransforms().addAll(translate, pivot, rotateY, rotateZ, rotateX, scale, inversePivot);
            case YXZ -> getTransforms().addAll(translate, pivot, rotateZ, rotateX, rotateY, scale, inversePivot);
            case YZX -> getTransforms().addAll(translate, pivot, rotateX, rotateZ, rotateY, scale, inversePivot);  // For Camera
            case ZXY -> getTransforms().addAll(translate, pivot, rotateY, rotateX, rotateZ, scale, inversePivot);
            case ZYX -> getTransforms().addAll(translate, pivot, rotateX, rotateY, rotateZ, scale, inversePivot);
        }
    }

    public void setTranslate(double x, double y, double z) {
        translate.setX(x);
        translate.setY(y);
        translate.setZ(z);
    }

    public void setTranslate(double x, double y) {
        translate.setX(x);
        translate.setY(y);
    }

    public void setTx(double x) { translate.setX(x); }
    public void setTy(double y) { translate.setY(y); }
    public void setTz(double z) { translate.setZ(z); }

    public void setRotate(double x, double y, double z) {
        rotateX.setAngle(x);
        rotateY.setAngle(y);
        rotateZ.setAngle(z);
    }

    public void setScale(double scaleFactor) {
        scale.setX(scaleFactor);
        scale.setY(scaleFactor);
        scale.setZ(scaleFactor);
    }

    public void setScale(double x, double y, double z) {
        scale.setX(x);
        scale.setY(y);
        scale.setZ(z);
    }

    public void setSx(double x) { scale.setX(x); }
    public void setSy(double y) { scale.setY(y); }
    public void setSz(double z) { scale.setZ(z); }

    public void setPivot(double x, double y, double z) {
        pivot.setX(x);
        pivot.setY(y);
        pivot.setZ(z);
        inversePivot.setX(-x);
        inversePivot.setY(-y);
        inversePivot.setZ(-z);
    }

    public void reset() {
        translate.setX(0.0);
        translate.setY(0.0);
        translate.setZ(0.0);
        rotateX.setAngle(0.0);
        rotateY.setAngle(0.0);
        rotateZ.setAngle(0.0);
        scale.setX(1.0);
        scale.setY(1.0);
        scale.setZ(1.0);
        pivot.setX(0.0);
        pivot.setY(0.0);
        pivot.setZ(0.0);
        inversePivot.setX(0.0);
        inversePivot.setY(0.0);
        inversePivot.setZ(0.0);
    }

    public void resetTSP() {
        translate.setX(0.0);
        translate.setY(0.0);
        translate.setZ(0.0);
        scale.setX(1.0);
        scale.setY(1.0);
        scale.setZ(1.0);
        pivot.setX(0.0);
        pivot.setY(0.0);
        pivot.setZ(0.0);
        inversePivot.setX(0.0);
        inversePivot.setY(0.0);
        inversePivot.setZ(0.0);
    }
}
