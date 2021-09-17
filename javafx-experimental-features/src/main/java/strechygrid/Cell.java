package strechygrid;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.Map;

public class Cell {

    private static final double K_1 = 0.01;
    private static final double K_2 = 0.20000000000000001;
    private static final double DAMP = 0.72999999999999998;

    private final Grid grid;
    private final int row;
    private final int col;
    private final DoubleProperty x = new SimpleDoubleProperty();
    private final DoubleProperty y = new SimpleDoubleProperty();
    private final double cx;
    private final double cy;
    private final Map<Cell, Point2D> cellToCellSizeMapperMap = new HashMap<>();

    private double xv;
    private double yv;
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
    }

    // called after the whole grid.getCells() sequence has been initialized/filled
    void initAdjacencyRelations() {
        left = (col > 0) ? grid.getCellByIndex(row * grid.getNumCellSide() + col - 1) : null;
        cellToCellSizeMapperMap.put(left, new Point2D(1, 0));
        right = (col < grid.getNumCellSide() - 1) ? grid.getCellByIndex(row * grid.getNumCellSide() + col + 1) : null;
        cellToCellSizeMapperMap.put(right, new Point2D(-1, 0));
        top = (row > 0) ? grid.getCellByIndex((row - 1) * grid.getNumCellSide() + col) : null;
        cellToCellSizeMapperMap.put(top, new Point2D(0, 1));
        bottom = (row < grid.getNumCellSide() - 1) ? grid.getCellByIndex((row + 1) * grid.getNumCellSide() + col) : null;
        cellToCellSizeMapperMap.put(bottom, new Point2D(0, -1));
    }

    public Point2D getCellSizeMapper(Cell neighbor) {
        return cellToCellSizeMapperMap.get(neighbor);
    }

    void update() {
        xv += (cx - x.get()) * K_1;
        yv += (cy - y.get()) * K_1;
        updateCellXvAndYvIfNeighborNotNull(left);
        updateCellXvAndYvIfNeighborNotNull(right);
        updateCellXvAndYvIfNeighborNotNull(top);
        updateCellXvAndYvIfNeighborNotNull(bottom);
        xv *= DAMP;
        yv *= DAMP;
        x.set(x.get() + (int) xv);
        y.set(y.get() + (int) yv);
    }

    private void updateCellXvAndYvIfNeighborNotNull(Cell neighbor) {
        if (neighbor != null) {
            Point2D mapper = getCellSizeMapper(neighbor);
            xv += (neighbor.x.get() + mapper.getX() * grid.getCellSize() - x.get()) * K_2;
            yv += (neighbor.y.get() + mapper.getY() * grid.getCellSize() - y.get()) * K_2;
        }
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

    public Cell getRight() {
        return right;
    }

    public Cell getBottom() {
        return bottom;
    }

}
