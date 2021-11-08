package fx_customs;

import interfaces.Interactable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class MyVBox extends VBox implements Interactable {

    public MyVBox(Node... nodes) {
        super(nodes);
    }
}
