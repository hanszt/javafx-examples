package hzt.model;

import hzt.service.IMouseControlService;
import hzt.service.MouseControlService;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;

public abstract class Model3D extends Group {

    public void addMouseControl(Scene scene) {
        IMouseControlService planeMouseControlService = new MouseControlService();
        planeMouseControlService.initMouseControl(this, scene);
        planeMouseControlService.setOrientation(20, 20);
    }

    public void setScale(double scale) {
        setScaleX(scale);
        setScaleY(scale);
        setScaleZ(scale);
    }
    public static boolean nodeIsModel3D(Node n) {
        return n instanceof Model3D;
    }

    public static Model3D nodeToModel3D(Node n) {
        return (Model3D) n;
    }

}
