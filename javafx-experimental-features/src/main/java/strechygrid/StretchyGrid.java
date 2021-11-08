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
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A fabric made up of a bunch of lines. When using the application, you use the
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
        final Grid grid = new Grid(100, 8);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                grid.update();
            }
        };
        timer.start();
        Scene scene = new Scene(grid);
        grid.configure();
        primaryStage.setScene(scene);
        primaryStage.setTitle(getClass().getSimpleName());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
