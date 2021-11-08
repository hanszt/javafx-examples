package fx_customs;

import interfaces.Interactable;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;

public class MySubScene extends SubScene implements Interactable {

    public MySubScene(Parent parent, double width, double height, boolean depthBuffer, SceneAntialiasing sceneAntialiasing) {
        super(parent, width, height, depthBuffer, sceneAntialiasing);
    }
}
