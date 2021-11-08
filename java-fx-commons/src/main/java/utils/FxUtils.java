package utils;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    public static <N> ChangeListener<N> forNewValue(Consumer<N> consumer) {
        return (o, c, n) -> consumer.accept(n);
    }

    public static EventHandler<ActionEvent> multiple(Runnable... runnables) {
        return event -> {
            for (Runnable runnable : runnables) {
                runnable.run();
            }
            event.consume();
        };
    }

    public static EventHandler<ActionEvent> run(Runnable runnable) {
        return event -> {
            runnable.run();
            event.consume();
        };
    }
}
