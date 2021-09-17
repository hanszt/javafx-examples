package strechygrid;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Grid extends Group {

    private Point2D mousePos;
    private int drag = -1;
    private boolean dragging;
    private final int numCellSide;
    private final double cellSize;
    private final Cell[] cells;

    Grid(int numCellsPerSide, double cellSize) {
        this.numCellSide = numCellsPerSide;
        this.cellSize = cellSize;
        this.cells = buildCells();
        Arrays.stream(cells).forEach(Cell::initAdjacencyRelations);

        Rectangle rectangle = buildOuterRectangle();

        Group lineGroup = buildLineGroup();
        super.getChildren().addAll(rectangle, lineGroup);
    }

    @NotNull
    private Group buildLineGroup() {
        Group lineGroup = new Group();
        final var horAndVertLines = Stream.of(cells)
                .map(Grid::toHorizontalAndVerticalLineGroup)
                .collect(Collectors.toUnmodifiableSet());
        lineGroup.getChildren().addAll(horAndVertLines);
        return lineGroup;
    }

    @NotNull
    private Rectangle buildOuterRectangle() {
        Rectangle rectangle = new Rectangle();
        rectangle.setSmooth(false);
        rectangle.setWidth(numCellSide * this.cellSize);
        rectangle.setHeight(numCellSide * this.cellSize);
        rectangle.setFill(Color.BLACK);
        rectangle.setOnMousePressed(this::mousePressedAction);
        rectangle.setOnMouseDragged(this::updateMousePosition);
        rectangle.setOnMouseReleased(this::mouseReleasedAction);
        return rectangle;
    }

    @NotNull
    private Cell[] buildCells() {
        final Cell[] cellArray = new Cell[numCellSide * numCellSide];
        for (int row = 0; row < numCellSide; row++) {
            for (int col = 0; col < numCellSide; col++) {
                cellArray[row * numCellSide + col] = new Cell(this, row, col);
            }
        }
        return cellArray;
    }

    private static Group toHorizontalAndVerticalLineGroup(Cell currentCell) {
        Group horAndVertLine = new Group();
        if (currentCell.getRight() != null) {
            horAndVertLine.getChildren().add(cellLine(currentCell, currentCell.getRight()));
        }
        if (currentCell.getBottom() != null) {
            horAndVertLine.getChildren().add(cellLine(currentCell, currentCell.getBottom()));
        }
        return horAndVertLine;
    }

    private static Line cellLine(Cell cell, Cell neighbor) {
        Line line = new Line();
        line.setSmooth(false);
        line.setStroke(Color.WHITE);
        line.startXProperty().bind(cell.xProperty());
        line.startYProperty().bind(cell.yProperty());
        line.endXProperty().bind(neighbor.xProperty());
        line.endYProperty().bind(neighbor.yProperty());
        return line;
    }

    void update() {
        if (dragging) {
            cells[drag].setX(mousePos.getX());
            cells[drag].setY(mousePos.getY());
        }
        int counter = 0;
        for (Cell cell : cells) {
            if (drag != counter) {
                cell.update();
            }
            counter++;
        }

    }

    private void mousePressedAction(MouseEvent e) {
        int row = (int) (e.getSceneY() / cellSize);
        int col = (int) (e.getSceneX() / cellSize);
        if (row >= 0 && row < numCellSide && col >= 0 && col < numCellSide) {
            updateMousePosition(e);
            drag = row * numCellSide + col;
        }
        dragging = true;
    }

    private void updateMousePosition(MouseEvent e) {
        mousePos = new Point2D(e.getSceneX(), e.getSceneY());
    }

    private void mouseReleasedAction(MouseEvent e) {
        dragging = false;
        drag = -1;
    }

    public int getNumCellSide() {
        return numCellSide;
    }

    public Cell getCellByIndex(int index) {
        return cells[index];
    }

    public double getCellSize() {
        return cellSize;
    }
}
