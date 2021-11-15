package hzt.chapt16;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import utils.FxUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * To make org.testfx work within a module project, you need to add the maven surefire plugin to your pom
 * @see <a href="https://github.com/TestFX/TestFX/issues/638">OpenJFX 11 compatibility issue #638</a>
 */
@ExtendWith(ApplicationExtension.class)
class GalaxyTest {

    private final Group root = new Group();
    private ColorPicker colorPicker;
    private XYChart<Number, Number> lineChart;

    @Start
    private void start(Stage stage) {
        colorPicker = new ColorPicker();
        lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        var slider = new Slider();
        var button = new Button();
        var vBox = new VBox(lineChart, colorPicker, slider, button);
        var stackPane = new StackPane();
        root.getChildren().addAll(stackPane, vBox);
        stage.setScene(new Scene(root, 800, 800));
        stage.show();
    }

    @Test
    void testAllDescendentsRoot() {
        List<Node> allDescendents = FxUtils.allDescendents(root);
        allDescendents.stream()
                .map(Object::getClass)
                .map(Class::getSimpleName)
                .forEach(System.out::println);
        assertEquals(62, allDescendents.size());
    }

    @Test
    void testAllDescendentsColorPicker() {
        List<Node> allDescendents = FxUtils.allDescendents(colorPicker);
        allDescendents.stream()
                .map(Object::getClass)
                .map(Class::getSimpleName)
                .forEach(System.out::println);
        assertEquals(6, allDescendents.size());
    }

    @Test
    void testAllDescendentsLineChart() {
        List<Node> allDescendents = FxUtils.allDescendents(lineChart);
        allDescendents.stream()
                .map(Object::getClass)
                .map(Class::getSimpleName)
                .forEach(System.out::println);
        assertEquals(47, allDescendents.size());
    }

}
