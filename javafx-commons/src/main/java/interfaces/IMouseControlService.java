package interfaces;

import javafx.geometry.Point3D;
import javafx.scene.Node;

public interface IMouseControlService {

    void initMouseControl(Node target, MouseTarget reference);

    void setOrientation(double initAngleX, double initAngleY);

    void setTargetTranslation(Point3D point);

    void setMoveWhenTargetPressedProperty(boolean moveWhenTargetPressed);

    void setTranslationFactorProperty(double translationFactorProperty);

    double getTargetTranslateX();

    double getTargetTranslateY();

    double getTargetTranslateZ();

    double getTargetAngleX();

    double getTargetAngleY();
}
