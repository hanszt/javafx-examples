package service;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Scene;

public interface IMouseControlService {

    void initMouseControl(Node target, Scene reference);

    void setOrientation(double initAngleX, double initAngleY);

    void setTargetTranslation(Point3D point);

}
