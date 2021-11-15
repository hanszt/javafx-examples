package fx_customs;

import interfaces.Interactable;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MyScene extends Scene implements Interactable {

    public MyScene(Parent root, double width, double height) {
        super(root, width, height);
    }

    public MyScene(Parent root, int width, int height, boolean b) {
        super(root, width, height, b);
    }
}
