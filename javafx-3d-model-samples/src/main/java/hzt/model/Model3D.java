package hzt.model;

import fx_customs.MyScene;
import interfaces.IMouseControlService;
import javafx.scene.Group;
import javafx.scene.Node;
import services.MouseControlService;

public abstract class Model3D extends Group {

    public void addMouseControl(MyScene scene) {
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
