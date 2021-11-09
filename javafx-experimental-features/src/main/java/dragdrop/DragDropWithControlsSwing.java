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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

public class DragDropWithControlsSwing extends Application {

    private static final DataFormat CUSTOM_BYTES = new DataFormat("dndwithcontrolsswing.custom.bytes");
    private static final DataFormat CUSTOM_STRING = new DataFormat("dndwithcontrolsswing.custom.string");

    private static final boolean SWING = true;

    private static final String HTML =
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
        "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
        "<head></head>" +
        "<body>Hello!</body>" +
        "</html>";

    private static final String RTF = "{\\rtf1\\ansi\\uc1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i FRED\\par rtf\\par text}";

    private final Pane sourceControlPane = new Pane();
    private final Pane targetControlPane = new Pane();
    private final Set<TransferMode> sourceModes = EnumSet.noneOf(TransferMode.class);
    private final Set<TransferMode> targetModes = EnumSet.noneOf(TransferMode.class);
    private final Set<DataFormat> sourceFormats = new HashSet<>();
    private final Set<DataFormat> targetFormats = new HashSet<>();
    private final Text log = new Text();
    private final List<String> messages = new LinkedList<>();
    private final List<File> files = new LinkedList<>();

    private Image image;

    Scene createScene() {
        final Group root = new Group();
        root.getChildren().add(createMainPane());
        return new Scene(root);
    }
    @Override public void start(final Stage stage) {
        final Scene scene = createScene();

        stage.setTitle("Drag and Drop Controls");
        stage.setWidth(700);
        stage.setHeight(700);
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();

        new Thread(this::loadNewImage).start();
    }

    private Node createMainPane() {
        Pane main = new Pane();

        HBox lr = new HBox(10);

        VBox src = new VBox(10);
        src.getChildren().add(new Text("SOURCE CONTROL:"));
        src.getChildren().add(new Separator());
        src.getChildren().add(sourceControlPane);
        src.getChildren().add(new Separator());
        src.getChildren().add(createLeftPane());

        VBox trgt = new VBox(10);
        trgt.getChildren().add(new Text("TARGET CONTROL:"));
        trgt.getChildren().add(new Separator());
        trgt.getChildren().add(targetControlPane);
        trgt.getChildren().add(new Separator());
        trgt.getChildren().add(createRightPane());

        lr.getChildren().add(src);
        lr.getChildren().add(new Separator(Orientation.VERTICAL));
        lr.getChildren().add(trgt);

        VBox tb = new VBox(10);
        tb.getChildren().add(lr);
        tb.getChildren().add(new Separator());
        tb.getChildren().add(log);

        main.getChildren().add(tb);
        return main;
    }

    private Node createLeftPane() {
        VBox box = new VBox(10);
        HBox hbox = new HBox(10);

        VBox lbox = new VBox(10);
        lbox.getChildren().add(new Text("Source control type:"));
        lbox.getChildren().add(createControlCombo(sourceControlPane, true));
        lbox.getChildren().add(new Text("Source transfer modes:"));
        lbox.getChildren().add(createTMSelect(sourceModes));

        VBox rbox = new VBox(10);
        rbox.getChildren().add(new Text("Data formats:"));
        rbox.getChildren().add(createFormatSelect(sourceFormats));

        hbox.getChildren().add(lbox);
        hbox.getChildren().add(new Separator(Orientation.VERTICAL));
        hbox.getChildren().add(rbox);

        final Text fileHdr = new Text("Files to drag (0):");
        final TextField tb = new TextField("Put full path here");
        final Button add = new Button("Add");
        add.setOnAction(event -> addFile(fileHdr, tb));
        final Button clear = new Button("Clear");
        clear.setOnAction(event -> clearFiles(fileHdr));

        HBox btns = new HBox();
        btns.getChildren().add(add);
        btns.getChildren().add(clear);

        box.getChildren().add(hbox);
        box.getChildren().add(new Separator());
        box.getChildren().add(fileHdr);
        box.getChildren().add(tb);
        box.getChildren().add(btns);

        return box;
    }

    private void clearFiles(Text fileHdr) {
        files.clear();
        fileHdr.setText("Files to drag (0)");
        log("File list cleared");
    }

    private void addFile(Text fileHdr, TextField tb) {
        File f = new File(tb.getText());
        if (f.exists()) {
            files.add(f);
            tb.setText("");
            fileHdr.setText("Files to drag (" + files.size() + ")");
            log("Added file " + f.getPath());
        } else {
            log("File doesn't exist: " + f.getPath());
        }
    }

    private Node createRightPane() {
        HBox hbox = new HBox(10);

        VBox lbox = new VBox(10);

        lbox.getChildren().add(new Text("Target control type:"));
        lbox.getChildren().add(createControlCombo(targetControlPane, false));
        lbox.getChildren().add(new Text("Target transfer modes:"));
        lbox.getChildren().add(createTMSelect(targetModes));

        VBox rbox = new VBox(10);
        rbox.getChildren().add(new Text("Data formats:"));
        rbox.getChildren().add(createFormatSelect(targetFormats));

        hbox.getChildren().add(lbox);
        hbox.getChildren().add(new Separator(Orientation.VERTICAL));
        hbox.getChildren().add(rbox);

        return hbox;
    }

    private Node createControlCombo(final Pane sourceControlPane, final boolean source) {
        ChoiceBox<String> cb = new ChoiceBox<>();
        cb.getItems().addAll("Rectangle", "Button", "Checkbox", "ChoiceBox", "Hyperlink",
                "Label", "List", "RadioButton", "Tree");

        cb.getSelectionModel().selectedItemProperty().addListener((ov, t, newValue) ->
                choiceBoxSelectionModelChangedAction(sourceControlPane, source, newValue));

        cb.getSelectionModel().select(0);
        return cb;
    }

    private void choiceBoxSelectionModelChangedAction(Pane sourceControlPane, boolean source, String newValue) {
        String text = source ? "This is source" : "This is target";
        Node controlNode = createControlNode(newValue, text);
        sourceControlPane.getChildren().clear();
        sourceControlPane.getChildren().add(controlNode);

        if (source) {
            controlNode.setOnDragDetected(event -> dragDetectedAction(controlNode, event));
            controlNode.setOnDragDone(this::dragDoneAction);
        } else {
            controlNode.setOnDragOver(this::dragOverAction);
            controlNode.setOnDragDropped(this::dragDroppedAction);
        }
    }

    private void dragDoneAction(DragEvent event) {
        log("Transfer done: " + event.getTransferMode());
        log("");
    }

    private void dragOverAction(DragEvent event) {
        Dragboard db = event.getDragboard();
        for (DataFormat df : targetFormats) {
            if (db.hasContent(df)) {
                event.acceptTransferModes(
                        targetModes.toArray(new TransferMode[0]));
                return;
            }
        }
    }

    private void dragDetectedAction(Node control, MouseEvent event) {
        Dragboard db = control.startDragAndDrop(
                sourceModes.toArray(new TransferMode[0]));
        if (db == null) {
            log("Cannot start drag and drop.");
            return;
        }
        ClipboardContent content = new ClipboardContent();
        if (sourceFormats.contains(DataFormat.PLAIN_TEXT)) {
            log("Source is putting string on dragboard");
            content.putString("Hello!");
        }
        if (sourceFormats.contains(DataFormat.URL)) {
            log("Source is putting URL on dragboard");
            content.putUrl("http://www.oracle.com");
        }
        if (sourceFormats.contains(DataFormat.IMAGE)) {
            log("Source is putting image on dragboard");
            content.putImage(image);
        }
        if (sourceFormats.contains(DataFormat.HTML)) {
            log("Source is putting HTML on dragboard");
            content.putHtml(HTML);
        }
        if (sourceFormats.contains(DataFormat.RTF)) {
            log("Source is putting RTF on dragboard");
            content.putRtf(RTF);
        }
        if (sourceFormats.contains(CUSTOM_BYTES)) {
            log("Source is putting custom four bytes on dragboard");
            content.put(CUSTOM_BYTES, new byte[]{1, 2, 3, 4});
        }
        if (sourceFormats.contains(CUSTOM_STRING)) {
            log("Source is putting custom four bytes on dragboard");
            content.put(CUSTOM_STRING, "Hello Custom String!");
        }
        if (sourceFormats.contains(DataFormat.FILES)) {
            log("Source is putting two files on dragboard");
            content.putFiles(files);
        }
        db.setContent(content);
        event.consume();
    }

    private void dragDroppedAction(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        conditionalLogAndSetDropCompleted(event, "Dropped string: " + dragboard.getString(),
                dragboard.hasString() && targetFormats.contains(DataFormat.PLAIN_TEXT));
        conditionalLogAndSetDropCompleted(event, "Dropped HTML: " + dragboard.getHtml(),
                targetFormats.contains(DataFormat.HTML) && dragboard.hasHtml());
        conditionalLogAndSetDropCompleted(event, "Dropped RTF: " + dragboard.getRtf(),
                targetFormats.contains(DataFormat.RTF) && dragboard.hasRtf());
        conditionalLogAndSetDropCompleted(event, "Dropped URL: " + dragboard.getUrl(),
                targetFormats.contains(DataFormat.URL) && dragboard.hasUrl());
        conditionalLogAndSetDropCompleted(event, "Dropped image: " + dragboard.getImage(),
                targetFormats.contains(DataFormat.IMAGE) && dragboard.hasImage());
        conditionalLogAndSetDropCompleted(event, "Dropped files:" + getDroppedFilesAsString(dragboard),
                targetFormats.contains(DataFormat.FILES) && dragboard.hasFiles());
        conditionalLogAndSetDropCompleted(event, getCustomBytesText(dragboard),
                targetFormats.contains(CUSTOM_BYTES) && dragboard.hasContent(CUSTOM_BYTES));
        conditionalLogAndSetDropCompleted(event, "Dropped custom string: " + dragboard.getContent(CUSTOM_STRING),
                targetFormats.contains(CUSTOM_STRING) && dragboard.hasContent(CUSTOM_STRING));
    }

    @NotNull
    private static String getCustomBytesText(Dragboard db) {
        byte[] b = (byte[]) db.getContent(CUSTOM_BYTES);
        return "Dropped custom bytes: " + b[0] + ", " + b[1] + ", " + b[2] + ", " + b[3];
    }

    private void conditionalLogAndSetDropCompleted(DragEvent event, String logMessage, boolean dropCompleted) {
        if (dropCompleted) {
            log(logMessage);
            event.setDropCompleted(true);
        }
    }

    @NotNull
    private static String getDroppedFilesAsString(Dragboard db) {
        return db.getFiles().stream()
                .map(file -> "   " + file.getPath())
                .collect(Collectors.joining());
    }

    @NotNull
    private static Node createControlNode(String newValue, String text) {
        return switch (newValue) {
            case "Rectangle" -> new Rectangle(50, 25, Color.DARKGRAY);
            case "Button" -> new Button(text);
            case "List" -> createListView(text);
            case "Tree" -> createTree(text);
            case "Checkbox" -> new CheckBox(text);
            case "Hyperlink" -> new Hyperlink(text);
            case "Label" -> new Label(text);
            case "RadioButton" -> new RadioButton(text);
            case "ChoiceBox" -> createChoiceBox(text);
            default -> new Node() {
            };
        };
    }

    private static ChoiceBox<String> createChoiceBox(String text) {
        ChoiceBox<String> c = new ChoiceBox<>();
        c.getItems().addAll(text, "More");
        c.getSelectionModel().select(0);
        return c;
    }

    private static TreeView<String> createTree(String text) {
        TreeItem<String> ti = new TreeItem<>(text);
        ti.getChildren().add(new TreeItem<>("Foo"));
        ti.getChildren().add(new TreeItem<>("Bar"));
        ti.getChildren().add(new TreeItem<>("More"));
        ti.getChildren().add(new TreeItem<>("Enough"));
        TreeView<String> tree = new TreeView<>(ti);
        tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tree.setPrefSize(tree.getPrefWidth(), 80);
        return tree;
    }

    private static ListView<String> createListView(String text) {
        ListView<String> list = new ListView<>(FXCollections.observableArrayList(
                text, "Foo", "Bar", "More", "Enough"));
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list.setPrefSize(list.getPrefWidth(), 80);
        return list;
    }

    private static Node createTMSelect(final Set<TransferMode> tms) {
        VBox box = new VBox();

        for (final TransferMode tm : TransferMode.values()) {
            CheckBox cb = new CheckBox(tm.toString());
            cb.selectedProperty().addListener((ov, t, t1) -> updateTransferModeSet(tms, tm, t1));
            if (tm == TransferMode.COPY) {
                cb.selectedProperty().set(true);
            }
            box.getChildren().add(cb);
        }

        return box;
    }

    private static void updateTransferModeSet(Set<TransferMode> transferModes, TransferMode transferMode, boolean addMode) {
        if (addMode) {
            transferModes.add(transferMode);
        } else {
            transferModes.remove(transferMode);
        }
    }

    private static Node createFormatSelect(final Set<DataFormat> dataFormats) {
        VBox box = new VBox();

        List<Pair<DataFormat, String>> list = new ArrayList<>(10);
        list.add(new Pair<>(DataFormat.PLAIN_TEXT, "PLAIN_TEXT"));
        list.add(new Pair<>(DataFormat.HTML, "HTML"));
        list.add(new Pair<>(DataFormat.RTF, "RTF"));
        list.add(new Pair<>(DataFormat.URL, "URL"));
        list.add(new Pair<>(DataFormat.IMAGE, "IMAGE"));
        list.add(new Pair<>(DataFormat.FILES, "FILES"));
        list.add(new Pair<>(CUSTOM_BYTES, "Custom (bytes)"));
        list.add(new Pair<>(CUSTOM_STRING, "Custom (String)"));

        for (final Pair<DataFormat, String> df : list) {
            CheckBox cb = new CheckBox(df.getValue());
            cb.selectedProperty().addListener((ov, t, t1) -> updateDataFormats(dataFormats, df, t1));
            box.getChildren().add(cb);
        }
        ((CheckBox) box.getChildren().get(0)).selectedProperty().set(true);

        return box;
    }

    private static void updateDataFormats(Set<DataFormat> dataFormats, Pair<DataFormat, String> dataFormat, boolean add) {
        if (add) {
            dataFormats.add(dataFormat.getKey());
        } else {
            dataFormats.remove(dataFormat.getKey());
        }
    }

    private void log(String text) {
        System.out.println(text);

        messages.add(text);
        if (messages.size() > 15) {
            messages.remove(0);
        }

        StringBuilder sb = new StringBuilder();
        for (String msg : messages) {
            sb.append(msg).append("\n");
        }
        log.setText(sb.toString());
    }

    private static void initAndShowGUI() {
        JFrame frame = new JFrame("Swing: Hello Drag And Drop");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final JFXPanel fxPanel = new JFXPanel();
        fxPanel.setPreferredSize(new Dimension(550, 400));
        frame.getContentPane().add(fxPanel, BorderLayout.CENTER);
        Platform.runLater(() -> runJfxPanel(fxPanel));
        frame.pack();
        frame.setVisible(true);
    }

    private static void runJfxPanel(JFXPanel fxPanel) {
        final Scene scene = new DragDropWithControlsSwing().createScene();
        SwingUtilities.invokeLater(() -> fxPanel.setScene(scene));
    }

    public static void main(String[] args) {
        if (SWING) {
            SwingUtilities.invokeLater(DragDropWithControlsSwing::initAndShowGUI);
        } else {
            Application.launch(args);
        }
    }

    private void loadNewImage() {
        log("Loading image..");
        image = new Image("https://openjdk.java.net/images/duke-thinking.png");
        log("Ready.");
        log("");
    }
}
