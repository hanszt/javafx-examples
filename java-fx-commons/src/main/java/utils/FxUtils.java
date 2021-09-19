package utils;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;

public final class FxUtils {

    private FxUtils() {
    }

    public static List<Node> allDescendents(Parent root) {
        List<Node> nodes = new ArrayList<>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, List<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent newParent) {
                addAllDescendents(newParent, nodes);
            }
        }
    }
}
