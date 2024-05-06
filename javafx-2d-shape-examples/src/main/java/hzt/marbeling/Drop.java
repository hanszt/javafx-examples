package hzt.marbeling;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.stream.DoubleStream;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public final class Drop extends Polygon {

    private final double radius;
    private final Point2D center;

    public Drop(Point2D position, double radius) {
        super(createCircle(radius, position));
        this.radius = radius;
        this.center = position;
        setFill(Color.BLACK);
        setStroke(Color.RED);
    }

    private static double[] createCircle(double radius, Point2D position) {
        final var resolution = 500;
        return DoubleStream.iterate(0, d -> d + 1.0)
                .limit(resolution)
                .mapToObj(d -> {
                    final var angle = 2 * PI * (d / resolution);
                    return new Point2D(cos(angle) * radius + position.getX(), sin(angle) * radius + position.getY());
                })
                .flatMapToDouble(p -> DoubleStream.of(p.getX(), p.getY()))
                .toArray();
    }

    public void marble(Drop other) {
        final var points = getPoints();
        for (int i = 0; i < points.size(); i += 2) {
            final var marbled = marble(other, new Point2D(points.get(i), points.get(i + 1)));
            points.set(i, marbled.getX());
            points.set(i + 1, marbled.getY());
        }
    }

    private static Point2D marble(Drop other, Point2D p) {
        var c = other.center;
        var r = other.radius;
        final var diff = p.subtract(c);
        var m = diff.magnitude();
        var root = Math.sqrt(1 + ((r * r) / (m * m)));
        return c.add(diff.multiply(root));
    }
}
