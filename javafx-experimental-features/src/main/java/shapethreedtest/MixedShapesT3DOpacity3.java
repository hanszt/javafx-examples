/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
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

package shapethreedtest;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MixedShapesT3DOpacity3 extends Application {

    private static final Logger LOGGER = LogManager.getLogger(MixedShapesT3DOpacity3.class);

    @Override public void start(Stage stage) {
        Scene scene = new Scene(new Group(), 400.0, 300.0);
        scene.setCamera(new PerspectiveCamera());
        Group group = new Group();
        group.setOpacity(0.5F);
        Group group2 = new Group();
        //                           opacity: 0.5
        group2.setRotate(60.0F);
        group2.setRotationAxis(Rotate.Y_AXIS);
        //                                    opacity: 0.5
        Ellipse ellipse = new Ellipse(100.0F, 80.0F, 50.0F, 25.0F);
        ellipse.setFill(Color.ORANGE);
        ellipse.setStroke(Color.BLUE);
        ellipse.setStrokeWidth(5.0F);
        ellipse.setOnMouseClicked(e -> LOGGER.info("Ellipse: Mouse Clicked: {}", e));
        //                                    opacity: 0.5
        Arc arc = new Arc(250.0F, 80.0F, 50.0F, 25.0F, 45.0F, 270.0F);
        arc.setType(ArcType.ROUND);
        arc.setFill(Color.RED);
        arc.setStroke(Color.BLUE);
        arc.setStrokeWidth(5.0F);
        arc.setOnMouseClicked(e -> LOGGER.info("Arc: Mouse Clicked: {}", e));
        //                                    opacity: 0.5
        Rectangle rectangle = new Rectangle(50.0F, 150.0F, 100.0F, 75.0F);
        rectangle.setArcHeight(20.0F);
        rectangle.setArcWidth(20.0F);
        rectangle.setFill(Color.GREEN);
        rectangle.setStroke(Color.BLUE);
        rectangle.setStrokeWidth(5.0F);
        rectangle.setOnMouseClicked(e -> LOGGER.info("Rectangle: Mouse Clicked: {}", e));
        //                                    opacity: 0.5
        ObservableList<Double> floats = FXCollections.observableArrayList();
        floats.addAll(200.0, 150.0, 250.0, 220.0, 300.0, 150.0);
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(floats);
        polygon.setFill(Color.YELLOW);
        polygon.setStroke(Color.BLUE);
        polygon.setStrokeWidth(5.0F);
        polygon.setOnMouseClicked(e -> LOGGER.info("Polygon: Mouse Clicked: {}", e));
        group2.getChildren().addAll(ellipse, arc, rectangle, polygon);
        group.getChildren().addAll(group2);
        ((Group)scene.getRoot()).getChildren().addAll(group);
        stage.setScene(scene);
        stage.sizeToScene();
        Shape3DTestHelper.logWarningIf3DNotSupported(LOGGER);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

