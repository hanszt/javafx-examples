package hzt.model;

import interfaces.IMouseControlService;
import interfaces.MouseTarget;
import javafx.scene.Group;
import javafx.scene.Node;
import services.MouseControlService;

import java.util.function.Consumer;

public abstract class Model3D extends Group {

    public void addMouseControl() {
        IMouseControlService planeMouseControlService = new MouseControlService();
        planeMouseControlService.initMouseControl(this, (MouseTarget) getScene());
        planeMouseControlService.setOrientation(20, 20);
    }

    public void setScale(double scale) {
        setScaleX(scale);
        setScaleY(scale);
        setScaleZ(scale);
    }
    public static void toModel3DIfInstance(Node n, Consumer<Model3D> model3DConsumer) {
        if (n instanceof Model3D model3D) {
            model3DConsumer.accept(model3D);
        }
    }

}
