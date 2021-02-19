/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package strechygrid;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * A fabric made up of a whole bunch of lines. When using the application, you use the
 * mouse to click and drag on the fabric, and doing so will cause the fabric to be pulled.
 * The lines on this fabric are springy and will stretch and return to their original shape.
 * <p>
 * This toy is a useful test to see what the overall cost of a large scene graph is, using
 * nothing but lines. The parameters passed to the grid constructor determine the number of
 * blocks horizontally and vertically, and the size of each block. By increasing the number of
 * blocks you put more load on the system, by decreasing the number of blocks, less load.
 */
public class StretchyGrid extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Grid grid = new Grid( 100, 8);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                grid.update();
            }
        };
        timer.start();

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Cell {

    Grid grid;
    int row;
    int col;
    DoubleProperty x = new SimpleDoubleProperty();
    DoubleProperty y = new SimpleDoubleProperty();
    double xv;
    double yv;
    double cx;
    double cy;
    Cell left;
    Cell right;
    Cell bottom;
    Cell top;
    Map<Cell, Point2D> cellToCellSizeMapperMap = new HashMap<>();

    Cell(Grid grid, int row, int col) {
        this.row = row;
        this.col = col;
        this.grid = grid;
        x.set(col * grid.cellSize);
        y.set(row * grid.cellSize);
        cx = x.get();
        cy = y.get();
    }

    // called after the whole grid.cells sequence has been initialized/filled
    void initAdjacencyRelations() {
        left = (col > 0) ? grid.cells[row * grid.numCellSide + col - 1] : null;
        cellToCellSizeMapperMap.put(left, new Point2D(1, 0));
        right = (col < grid.numCellSide - 1) ? grid.cells[row * grid.numCellSide + col + 1] : null;
        cellToCellSizeMapperMap.put(right, new Point2D(-1, 0));
        top = (row > 0) ? grid.cells[(row - 1) * grid.numCellSide + col] : null;
        cellToCellSizeMapperMap.put(top, new Point2D(0, 1));
        bottom = (row < grid.numCellSide - 1) ? grid.cells[(row + 1) * grid.numCellSide + col] : null;
        cellToCellSizeMapperMap.put(bottom, new Point2D(0, -1));
    }

    public Point2D getCellSizeMapper(Cell neighbor) {
        return cellToCellSizeMapperMap.get(neighbor);
    }
}

class Grid extends Group {

    Point2D mousePos;
    double k1 = 0.01;
    double k2 = 0.20000000000000001;
    double damp = 0.72999999999999998;
    int drag = -1;
    boolean dragging;
    final int numCellSide;
    final double cellSize;
    final Cell[] cells;

    Grid(int numCellsPerSide, double cellSize) {
        this.numCellSide = numCellsPerSide;
        this.cellSize = cellSize;

        cells = new Cell[numCellSide * numCellSide];
        for (int row = 0; row < numCellSide; row++) {
            for (int col = 0; col < numCellSide; col++) {
                cells[row * numCellSide + col] = new Cell(this, row, col);
            }
        }

        for (Cell cell : cells) {
            cell.initAdjacencyRelations();
        }

        Rectangle rectangle = new Rectangle();
        rectangle.setSmooth(false);
        rectangle.setWidth(numCellSide * this.cellSize);
        rectangle.setHeight(numCellSide * this.cellSize);
        rectangle.setFill(Color.BLACK);
        rectangle.setOnMousePressed(this::mousePressedAction);
        rectangle.setOnMouseDragged(this::updateMousePosition);
        rectangle.setOnMouseReleased(this::mouseReleasedAction);

        Group lineGroup = new Group();
        for (Cell currentCell : cells) {
            Group horAndVertLine = new Group();
            if (currentCell.right != null) {
                horAndVertLine.getChildren().add(cellLine(currentCell, currentCell.right));
            }
            if (currentCell.bottom != null) {
                horAndVertLine.getChildren().add(cellLine(currentCell, currentCell.bottom));
            }
            lineGroup.getChildren().add(horAndVertLine);
        }

        getChildren().addAll(rectangle, lineGroup);
    }

    private Line cellLine(Cell cell, Cell neighbor) {
        Line line = new Line();
        line.setSmooth(false);
        line.setStroke(Color.WHITE);
        line.startXProperty().bind(cell.x);
        line.startYProperty().bind(cell.y);
        line.endXProperty().bind(neighbor.x);
        line.endYProperty().bind(neighbor.y);
        return line;
    }

    void update() {
        if (dragging) {
            cells[drag].x.set(mousePos.getX());
            cells[drag].y.set(mousePos.getY());
        }
        int i = 0;
        for (Cell cell : cells) {
            if (drag != i) {
                cell.xv += (cell.cx - cell.x.get()) * k1;
                cell.yv += (cell.cy - cell.y.get()) * k1;
                updateCellXvAndYvIfNeighborNotNull(cell, cell.left);
                updateCellXvAndYvIfNeighborNotNull(cell, cell.right);
                updateCellXvAndYvIfNeighborNotNull(cell, cell.top);
                updateCellXvAndYvIfNeighborNotNull(cell, cell.bottom);
                cell.xv *= damp;
                cell.yv *= damp;
                cell.x.set(cell.x.get() + (int) cell.xv);
                cell.y.set(cell.y.get() + (int) cell.yv);
            }
            i++;
        }

    }

    private void updateCellXvAndYvIfNeighborNotNull(Cell cell, Cell neighbor) {
        if (neighbor != null) {
            Point2D mapper = cell.getCellSizeMapper(neighbor);
            cell.xv += (neighbor.x.get() + mapper.getX() * cellSize - cell.x.get()) * k2;
            cell.yv += (neighbor.y.get() + mapper.getY() * cellSize - cell.y.get()) * k2;
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
}
