package hzt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Stream;

/**
 * A small program that recursively generates a sierpinski fractal triangle
 * <p>
 * <a href="https://en.wikipedia.org/wiki/Sierpi%C5%84ski_triangle">Sierpiński triangle</a>
 */
public final class SierpinskiTriangle extends Application {

    private static final double SIDE_LENGTH = 900.0;
    private static final String AMOUNT_OF_TRIANGLES = "Amount of triangles: ";
    public static final int INIT_DEPTH = 8;

    @Override
    public void start(Stage primaryStage) {
        final var drawPane = new Pane();

        final var children = drawPane.getChildren();
        children.addAll(fractalPolygons(INIT_DEPTH).toList());

        final var depthSlider = buildSlider();
        final var label = new Label(AMOUNT_OF_TRIANGLES + children.size());
        label.setLabelFor(drawPane);

        depthSlider.setOnMouseReleased(e -> {
            children.setAll(fractalPolygons((int) depthSlider.getValue()).toList());
            label.setText(AMOUNT_OF_TRIANGLES + children.size());
        });
        final var vBox = new VBox(drawPane, depthSlider, label);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20));
        primaryStage.setTitle("Sierpinski Triangle");
        primaryStage.setScene(new Scene(vBox, SIDE_LENGTH, SIDE_LENGTH));
        primaryStage.show();
    }

    private static @NotNull Slider buildSlider() {
        final var depthSlider = new Slider();
        depthSlider.setMin(1);
        depthSlider.setMax(10);
        depthSlider.setValue(INIT_DEPTH);
        depthSlider.setBlockIncrement(1);
        depthSlider.setMajorTickUnit(1);
        depthSlider.setMinorTickCount(0);
        depthSlider.setSnapToTicks(true);
        depthSlider.setShowTickLabels(true);
        return depthSlider;
    }

    private Stream<Polygon> fractalPolygons(int depth) {
        final var halfSideLength = SIDE_LENGTH / 2;
        final var parent = new EquilateralTriangle(new Point2D(halfSideLength, halfSideLength), halfSideLength);
        return createFractal(parent, depth).map(triangle -> new Polygon(triangle.points()));
    }

    private Stream<EquilateralTriangle> createFractal(EquilateralTriangle triangle, int depth) {
        final var triangleBuilder = Stream.<EquilateralTriangle>builder();
        createFractal(triangle, triangleBuilder, 1, depth);
        return triangleBuilder.build();
    }

    private void createFractal(EquilateralTriangle triangle, Stream.Builder<EquilateralTriangle> triangles, int depth, int targetDepth) {
        if (depth == targetDepth) {
            triangles.add(triangle);
            return;
        }
        final var children = triangle.sierpinskiChildren();
        for (final var child : children) {
            createFractal(child, triangles, depth + 1, targetDepth);
        }
    }

    static class EquilateralTriangle {

        private static final double rotation = -Math.PI / 2;

        private final Point2D center;
        private final double circumscribedCircleRadius;

        EquilateralTriangle(Point2D center, double circumscribedCircleRadius) {
            this.center = center;
            this.circumscribedCircleRadius = circumscribedCircleRadius;
        }

        List<EquilateralTriangle> sierpinskiChildren() {
            final var halfR = circumscribedCircleRadius / 2;
            final var center1 = new Point2D(
                    Math.cos(rotation) * halfR + center.getX(),
                    Math.sin(rotation) * halfR + center.getY()
            );
            final var center2 = new Point2D(
                    Math.cos(rotation + 2 * Math.PI / 3) * halfR + center.getX(),
                    Math.sin(rotation + 2 * Math.PI / 3) * halfR + center.getY()
            );
            final var center3 = new Point2D(
                    Math.cos(rotation + 4 * Math.PI / 3) * halfR + center.getX(),
                    Math.sin(rotation + 4 * Math.PI / 3) * halfR + center.getY()
            );
            return List.of(
                    new EquilateralTriangle(center1, halfR),
                    new EquilateralTriangle(center2, halfR),
                    new EquilateralTriangle(center3, halfR)
            );
        }

        double[] points() {
            return new double[]{
                    Math.cos(rotation) * circumscribedCircleRadius + center.getX(),
                    Math.sin(rotation) * circumscribedCircleRadius + center.getY(),
                    Math.cos(rotation + 2 * Math.PI / 3) * circumscribedCircleRadius + center.getX(),
                    Math.sin(rotation + 2 * Math.PI / 3) * circumscribedCircleRadius + center.getY(),
                    Math.cos(rotation + 4 * Math.PI / 3) * circumscribedCircleRadius + center.getX(),
                    Math.sin(rotation + 4 * Math.PI / 3) * circumscribedCircleRadius + center.getY(),
            };
        }
    }
}
