package strechygrid;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Cell {

    private static final double HUE_REF = 180;
    private static final double K_1 = 0.01;
    private static final double K_2 = 0.20000000000000001;
    private static final double DAMP = 0.74;

    private final Grid grid;
    private final int row;
    private final int col;
    private final double cx;
    private final double cy;

    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final DoubleProperty xv = new SimpleDoubleProperty();
    private final DoubleProperty yv = new SimpleDoubleProperty();
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.hsb(HUE_REF, 1, 1));

    private final Map<Cell, Point2D> cellToDirMapperMap = new HashMap<>();

    private Cell left;
    private Cell right;
    private Cell bottom;
    private Cell top;

    Cell(Grid grid, int row, int col) {
        this.row = row;
        this.col = col;
        this.grid = grid;
        x.set(col * grid.getCellSize());
        y.set(row * grid.getCellSize());
        cx = x.get();
        cy = y.get();
        xv.addListener((o, c, n) -> updateColor());
        yv.addListener((o, c, n) -> updateColor());
    }

    // called after the whole grid.getCells() sequence has been initialized/filled
    void initAdjacencyRelations() {
        left = (col > 0) ? grid.getCellByIndex(row * grid.getNumCellSide() + col - 1) : null;
        cellToDirMapperMap.put(left, new Point2D(1, 0));
        right = (col < grid.getNumCellSide() - 1) ? grid.getCellByIndex(row * grid.getNumCellSide() + col + 1) : null;
        cellToDirMapperMap.put(right, new Point2D(-1, 0));
        top = (row > 0) ? grid.getCellByIndex((row - 1) * grid.getNumCellSide() + col) : null;
        cellToDirMapperMap.put(top, new Point2D(0, 1));
        bottom = (row < grid.getNumCellSide() - 1) ? grid.getCellByIndex((row + 1) * grid.getNumCellSide() + col) : null;
        cellToDirMapperMap.put(bottom, new Point2D(0, -1));
    }

    public Point2D getCellSizeMapper(Cell neighbor) {
        return cellToDirMapperMap.get(neighbor);
    }

    void update() {
        xv.set(xv.get() + (cx - x.get()) * K_1);
        yv.set(yv.get() + (cy - y.get()) * K_1);
        updateCellXvAndYvIfNeighborNotNull(left);
        updateCellXvAndYvIfNeighborNotNull(right);
        updateCellXvAndYvIfNeighborNotNull(top);
        updateCellXvAndYvIfNeighborNotNull(bottom);
        xv.set(xv.get() * DAMP);
        yv.set(yv.get() * DAMP);
        x.set(x.get() + (int) xv.get());
        y.set(y.get() + (int) yv.get());
    }

    private void updateColor() {
        final var dX = cx - x.get();
        final var dY = cy - y.get();
        this.color.set(Color.hsb(HUE_REF + (dX * dX + dY * dY), 1, 1));
    }

    private void updateCellXvAndYvIfNeighborNotNull(Cell neighbor) {
        if (neighbor != null) {
            Point2D mapper = getCellSizeMapper(neighbor);
            xv.set(xv.get() + (neighbor.x.get() + mapper.getX() * grid.getCellSize() - x.get()) * K_2);
            yv.set(yv.get() + (neighbor.y.get() + mapper.getY() * grid.getCellSize() - y.get()) * K_2);
        }
    }

    public void setVelocityVector(double vX, double vY) {
        xv.set(vX);
        yv.set(vY);
    }

    public void damp(double factor) {
        xv.set(xv.get() * factor);
        yv.set(yv.get() * factor);
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public Cell getRight() {
        return right;
    }

    public Cell getBottom() {
        return bottom;
    }
}
