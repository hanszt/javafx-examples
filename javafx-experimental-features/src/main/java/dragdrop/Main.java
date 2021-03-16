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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WIDTH = 380;
    private static final int HEIGHT = 500;

    VBox launcher = new VBox(15);
    Info info = new Info();

    @Override public void start(Stage stage) {
        stage.setTitle("Touch Suite");

        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(Color.BEIGE);

        root.getChildren().add(launcher);
        root.getChildren().add(info);

        register("Color", DragDropColor.class, DragDropColor.INFO);
        register("Text", DragDropText.class, DragDropText.info());
        register("Controls", DragDropWithControls.class, DragDropWithControls.info());

        stage.setScene(scene);
        stage.show();
    }

    private Node createLauncher(final String name, final Class<?> app) {
        Text text = new Text(name);
        text.setFont(new Font(25));
        text.setFill(Color.DARKBLUE);
        text.setUnderline(true);
        text.setTranslateX(40);
        text.setTextOrigin(VPos.TOP);
        text.setOnMouseClicked(event -> textOnMouseClicked(app));
        return text;
    }

    private void textOnMouseClicked(Class<?> app) {
        Stage stage = new Stage();
        try {
            ((Application) app.getDeclaredConstructor().newInstance()).start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void register(final String name, final Class<?> app, final String description) {
        HBox g = new HBox();

        Circle bullet = new Circle(5);
        bullet.setFill(Color.BLACK);
        bullet.setTranslateX(20);
        bullet.setTranslateY(15);

        Text b1 = new Text("(");
        b1.setTranslateX(50);
        b1.setFont(new Font(25));

        Text b2 = new Text(")");
        b2.setTranslateX(50);
        b2.setFont(new Font(25));

        Text i = new Text("info");
        i.setTranslateX(50);
        i.setFont(new Font(25));
        i.setUnderline(true);
        i.setOnMouseClicked(event -> info.show(name, app, description));

        g.getChildren().add(bullet);
        g.getChildren().add(createLauncher(name, app));
        g.getChildren().addAll(b1, i, b2);

        launcher.getChildren().add(g);
    }

    private static class Info extends Group {

        private final Text n;
        private final Text t;
        private final Text runText;

        public Info() {
            VBox vb = new VBox(10);

            Rectangle bg = new Rectangle(WIDTH, HEIGHT);
            bg.setFill(Color.LIGHTBLUE);

            n = new Text();
            n.setFont(new Font(25));
            n.setTranslateX(10);
            n.setTextOrigin(VPos.TOP);

            t = new Text();
            t.setFont(new Font(15));
            t.setTextOrigin(VPos.TOP);
            t.setTranslateX(10);
            t.setWrappingWidth(WIDTH - 20.);

            runText = new Text("Start application");
            runText.setFont(new Font(25));
            runText.setTranslateX(10);
            runText.setTextOrigin(VPos.TOP);

            Text close = new Text("Close info");
            close.setFont(new Font(25));
            close.setTranslateX(10);
            close.setTextOrigin(VPos.TOP);

            setVisible(false);

            close.setOnMouseClicked(event -> Info.this.setVisible(false));

            vb.getChildren().addAll(n, t, runText, close);
            getChildren().addAll(bg, vb);
        }

        public void show(final String name, final Class<?> app, final String info) {
            n.setText(name);
            t.setText(info);
            runText.setOnMouseClicked(event -> runOnMouseClicked(app));
            setVisible(true);
        }

        private void runOnMouseClicked(Class<?> app) {
            Info.this.setVisible(false);
            Stage stage = new Stage();
            try {
                ((Application) app.getDeclaredConstructor().newInstance()).start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
