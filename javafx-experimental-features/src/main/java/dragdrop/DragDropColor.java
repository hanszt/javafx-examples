/*
 * Copyright (c) 2011, 2016, Oracle and/or its affiliates. All rights reserved.
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
package dragdrop;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DragDropColor extends Application {

    private static final DataFormat DATA_FORMAT_COLOR = new DataFormat("javafx.scene.color");
    public static final String INFO = "Drag the color boxes onto the circles";

    private static void onDragOver(DragEvent event) {
        if (event.getDragboard().hasContent(DATA_FORMAT_COLOR)) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
    }

    private static void toCopyOrMove(DragEvent event) {
        if (event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
    }

    @Override
    public void start(final Stage stage) {

        Rectangle red = new Rectangle(25, 25, 50, 50);
        red.setFill(Color.RED);
        makeSource(red, TransferMode.COPY);

        Rectangle green = new Rectangle(25, 100, 50, 50);
        green.setFill(Color.GREEN);
        makeSource(green, TransferMode.COPY);

        Rectangle blue = new Rectangle(25, 175, 50, 50);
        blue.setFill(Color.BLUE);
        makeSource(blue, TransferMode.COPY);

        Circle left = new Circle(175, 100, 75);
        left.setFill(Color.GRAY);
        makeSource(left, TransferMode.COPY_OR_MOVE);
        makeTarget(left);

        Circle right = new Circle(350, 100, 75);
        right.setFill(Color.GRAY);
        makeSource(right, TransferMode.COPY_OR_MOVE);
        makeTarget(right);

        Text text = new Text(100, 200, "Text drop target");
        text.setTextOrigin(VPos.TOP);
        text.setFont(Font.font(text.getFont().getFamily(), 20.0));
        makeTarget(text);

        final Group root = new Group();
        root.getChildren().add(red);
        root.getChildren().add(green);
        root.getChildren().add(blue);
        root.getChildren().add(left);
        root.getChildren().add(right);
        root.getChildren().add(text);
        final Scene scene = new Scene(root);

        stage.setTitle("Drag and Drop Colors");
        stage.setWidth(500);
        stage.setHeight(300);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    private static void makeSource(final Shape shape, final TransferMode... transferModes) {
        shape.setOnDragDetected(event -> onDragDetected(shape, event, transferModes));
        shape.setOnDragDone(event -> onDragDone(shape, event));
    }

    private static void onDragDone(Shape shape, DragEvent event) {
        System.out.println("DONE, " + event.getTransferMode());
        if (event.getTransferMode() == TransferMode.MOVE) {
            shape.setFill(Color.GRAY);
        }
    }

    private static void onDragDetected(Shape shape, MouseEvent event, TransferMode[] transferModes) {
        if (shape.getFill().equals(Color.GRAY)) return;

        Dragboard db = shape.startDragAndDrop(transferModes);
        ClipboardContent content = new ClipboardContent();
        content.putString(shape.getFill().toString());

        if (shape.getFill() instanceof Color) {
            Color c = (Color) shape.getFill();

            // Color is not Serializable, have to make something
            // we can transfer
            double[] arr = {c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity()};
            content.put(DATA_FORMAT_COLOR, arr);
        }
        db.setContent(content);

        event.consume();
    }

    private static void makeTarget(final Shape shape) {
        shape.setOnDragOver(DragDropColor::onDragOver);
        shape.setOnDragDropped(event -> onDragDropped(shape, event));
    }

    private static void onDragDropped(Shape shape, DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasContent(DATA_FORMAT_COLOR)) {
            double[] arr = (double[]) db.getContent(DATA_FORMAT_COLOR);
            shape.setFill(new Color(arr[0], arr[1], arr[2], arr[3]));
        }
        event.setDropCompleted(true);
    }

    private static void makeTarget(final Text text) {
        text.setOnDragOver(DragDropColor::toCopyOrMove);
        text.setOnDragDropped(event -> targetSetOnDragDropped(text, event));
    }

    private static void targetSetOnDragDropped(Text text, DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasString()) {
            text.setText(db.getString());
        }
        event.setDropCompleted(true);
    }

    public static void main(String[] args) {
        Application.launch(DragDropColor.class, args);
    }
}
