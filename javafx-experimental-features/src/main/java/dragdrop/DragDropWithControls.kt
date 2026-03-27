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
package dragdrop

import fxutils.onNewValue
import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.*
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.util.Pair
import java.io.File
import java.util.*

fun main() = DragDropWithControls().start()

class DragDropWithControls : Application() {

    private val sourceControlPane = Pane()
    private val targetControlPane = Pane()
    private val sourceModes: MutableSet<TransferMode> = EnumSet.noneOf(TransferMode::class.java)
    private val targetModes: MutableSet<TransferMode> = EnumSet.noneOf(TransferMode::class.java)
    private val sourceFormats: MutableSet<DataFormat> = HashSet()
    private val targetFormats: MutableSet<DataFormat> = HashSet()
    private val log = Text()
    private val messages: MutableList<String> = LinkedList()
    private val files: MutableList<File> = LinkedList()
    var image: Image? = null

    fun start() = launch()

    override fun start(stage: Stage) {
        stage.apply {
            title = "Drag and Drop Controls"
            width = 700.0
            height = 700.0
            isResizable = true
            scene = Scene(Group(createMainPane()))
            show()
        }
        Thread(::run).start()
    }

    private fun createMainPane(): Pane {
        val hBox = HBox(10.0)
        val srcVBox = VBox(10.0)
        srcVBox.children.addAll(Text("SOURCE CONTROL:"), Separator(), sourceControlPane, Separator(), createLeftPane())

        val targetVBox = VBox(10.0)
        targetVBox.children.addAll(Text("TARGET CONTROL:"), Separator(), targetControlPane, Separator(), createRightPane())

        hBox.children.addAll(srcVBox, Separator(Orientation.VERTICAL), targetVBox)
        val tb = VBox(10.0)
        tb.children.addAll(hBox, Separator(), log)

        return Pane(tb)
    }

    private fun createLeftPane(): Node {
        val box = VBox(10.0)
        val hbox = HBox(10.0)
        val lbox = VBox(10.0).apply {
            children.addAll(Text("Source control type:"), createControlCombo(sourceControlPane, true))
            children.addAll(Text("Source transfer modes:"), createTMSelect(sourceModes))
        }
        val rbox = VBox(10.0)

        rbox.children.addAll(createFormatSelect(sourceFormats), Text("Data formats:"))
        hbox.children.addAll(lbox, Separator(Orientation.VERTICAL), rbox)

        val fileHdr = Text("Files to drag (0):")
        val textField = TextField("Put full path here")
        val add = Button("Add")
        add.onAction = EventHandler { addFile(fileHdr, textField) }
        val clear = Button("Clear")
        clear.onAction = EventHandler { clear(fileHdr) }

        val btns = HBox()
        btns.children.addAll(add, clear)
        box.children.addAll(hbox, Separator(), fileHdr, textField, btns)
        return box
    }

    private fun clear(fileHdr: Text) {
        files.clear()
        fileHdr.text = "Files to drag (0)"
        log("File list cleared")
    }

    private fun addFile(fileHdr: Text, textField: TextField) {
        val file = File(textField.text)
        if (file.exists()) {
            files.add(file)
            textField.text = ""
            fileHdr.text = "Files to drag (" + files.size + ")"
            log("Added file " + file.path)
        } else log("File doesn't exist: " + file.path)
    }

    private fun createRightPane(): Node {
        val hbox = HBox(10.0)
        val lbox = VBox(10.0)
        lbox.children.add(Text("Target control type:"))
        lbox.children.add(createControlCombo(targetControlPane, false))
        lbox.children.add(Text("Target transfer modes:"))
        lbox.children.add(createTMSelect(targetModes))
        val rbox = VBox(10.0)
        rbox.children.add(Text("Data formats:"))
        rbox.children.add(createFormatSelect(targetFormats))
        hbox.children.add(lbox)
        hbox.children.add(Separator(Orientation.VERTICAL))
        hbox.children.add(rbox)
        return hbox
    }

    private fun createControlCombo(sourceControlPane: Pane, source: Boolean): Node {
        val choiceBox = ChoiceBox<String>()
        choiceBox.items.addAll(
            "Rectangle", "Button", "Checkbox", "ChoiceBox", "Hyperlink",
            "Label", "List", "RadioButton", "Tree"
        )
        choiceBox.selectionModel.selectedItemProperty().onNewValue { newVal ->
            val text = if (source) "This is source" else "This is target"

            val control = when (newVal) {
                "Rectangle" -> Rectangle(50.0, 25.0, Color.DARKGRAY)
                "Button" -> Button(text)
                "List" -> createListView(text)
                "Tree" -> buildTreeView(text)
                "Checkbox" -> CheckBox(text)
                "Hyperlink" -> Hyperlink(text)
                "Label" -> Label(text)
                "RadioButton" -> RadioButton(text)
                "ChoiceBox" -> createChoiceBox(text)
                else -> error("$newVal not supported")
            }
            sourceControlPane.children.clear()
            sourceControlPane.children.add(control)

            if (source) {
                control.onDragDetected = EventHandler { onDragDetected(control, it) }
                control.onDragDone = EventHandler(::onDragDone)
            } else {
                control.onDragOver = EventHandler(::onDragOver)
                control.onDragDropped = EventHandler(::dragDroppedAction)
            }
        }
        choiceBox.selectionModel.select(0)
        return choiceBox
    }

    private fun onDragDetected(control: Node?, event: MouseEvent) {
        val db = control!!.startDragAndDrop(
            *sourceModes.toTypedArray()
        )
        if (db == null) {
            log("Cannot start drag and drop.")
            return
        }
        val content = ClipboardContent()
        if (sourceFormats.contains(DataFormat.PLAIN_TEXT)) {
            log("Source is putting string on dragboard")
            content.putString("Hello!")
        }
        if (sourceFormats.contains(DataFormat.URL)) {
            log("Source is putting URL on dragboard")
            content.putUrl("https://www.oracle.com")
        }
        if (sourceFormats.contains(DataFormat.IMAGE)) {
            log("Source is putting image on dragboard")
            content.putImage(image)
        }
        if (sourceFormats.contains(DataFormat.HTML)) {
            log("Source is putting HTML on dragboard")
            content.putHtml(HTML)
        }
        if (sourceFormats.contains(DataFormat.RTF)) {
            log("Source is putting RTF on dragboard")
            content.putRtf(RTF)
        }
        if (sourceFormats.contains(CUSTOM_BYTES)) {
            log("Source is putting custom four bytes on dragboard")
            content[CUSTOM_BYTES] = byteArrayOf(1, 2, 3, 4)
        }
        if (sourceFormats.contains(CUSTOM_STRING)) {
            log("Source is putting custom four bytes on dragboard")
            content[CUSTOM_STRING] = "Hello Custom String!"
        }
        if (sourceFormats.contains(DataFormat.FILES)) {
            log("Source is putting two files on dragboard")
            content.putFiles(files)
        }
        db.setContent(content)
        event.consume()
    }

    private fun createChoiceBox(text: String): Node {
        return ChoiceBox<String>().apply {
            items.addAll(text, "More")
            selectionModel.select(0)
        }
    }

    private fun buildTreeView(text: String): Node {
        val treeItem: TreeItem<String> = TreeItem(text).apply {
            children.addAll(TreeItem("Foo"), TreeItem("Bar"), TreeItem("More"), TreeItem("Enough"))
        }
        return TreeView(treeItem).apply {
            selectionModel.selectionMode = SelectionMode.MULTIPLE
            setPrefSize(prefWidth, 80.0)
        }
    }

    private fun createListView(text: String): Node {
        return ListView(FXCollections.observableArrayList(text, "Foo", "Bar", "More", "Enough")).apply {
            selectionModel.selectionMode = SelectionMode.MULTIPLE
            setPrefSize(prefWidth, 80.0)
        }
    }

    private fun createTMSelect(tms: MutableSet<TransferMode>): Node {
        val box = VBox()
        for (tm in TransferMode.entries) {
            val cb = CheckBox(tm.toString())
            cb.selectedProperty().onNewValue { onCheckboxChanged(tms, tm, it) }
            if (tm == TransferMode.COPY) {
                cb.selectedProperty().set(true)
            }
            box.children.add(cb)
        }
        return box
    }

    private fun onCheckboxChanged(tms: MutableSet<TransferMode>, tm: TransferMode, t1: Boolean) {
        if (java.lang.Boolean.TRUE == t1) tms.add(tm) else tms.remove(tm)
    }

    private fun createFormatSelect(dataFormats: MutableSet<DataFormat>): Node {
        val box = VBox()
        val list = listOf(
            Pair(DataFormat.PLAIN_TEXT, "PLAIN_TEXT"),
            Pair(DataFormat.HTML, "HTML"),
            Pair(DataFormat.RTF, "RTF"),
            Pair(DataFormat.URL, "URL"),
            Pair(DataFormat.IMAGE, "IMAGE"),
            Pair(DataFormat.FILES, "FILES"),
            Pair(CUSTOM_BYTES, "Custom (bytes)"),
            Pair(CUSTOM_STRING, "Custom (String)")
        )
        for (df in list) {
            val cb = CheckBox(df.value)
            cb.selectedProperty().onNewValue { adaptDataFormats(dataFormats, df, it) }
            box.children.add(cb)
        }
        (box.children[0] as CheckBox).selectedProperty().set(true)
        return box
    }

    private fun adaptDataFormats(dataFormats: MutableSet<DataFormat>, df: Pair<DataFormat, String>, t1: Boolean) {
        if (java.lang.Boolean.TRUE == t1) dataFormats.add(df.key) else dataFormats.remove(df.key)
    }

    private fun log(text: String) {
        println(text)
        messages.add(text)
        if (messages.size > 15) messages.removeAt(0)
        val sb = StringBuilder()
        for (msg in messages) {
            sb.append(msg).append("\n")
        }
        Platform.runLater { log.text = sb.toString() }
    }

    private fun run() {
        log("Loading image..")
        image = Image("https://openjdk.java.net/images/duke-thinking.png")
        log("Ready.")
        log("")
    }

    private fun onDragDone(event: DragEvent) {
        log("Transfer done: " + event.transferMode)
        log("")
    }

    private fun onDragOver(event: DragEvent) {
        val db = event.dragboard
        for (df in targetFormats) {
            if (db.hasContent(df)) {
                event.acceptTransferModes(*targetModes.toTypedArray())
                return
            }
        }
    }

    private fun dragDroppedAction(event: DragEvent) {
        val dragboard = event.dragboard
        conditionalLogAndSetDropCompleted(
            event, "Dropped string: " + dragboard.string,
            dragboard.hasString() && targetFormats.contains(DataFormat.PLAIN_TEXT)
        )
        conditionalLogAndSetDropCompleted(
            event, "Dropped HTML: " + dragboard.html,
            targetFormats.contains(DataFormat.HTML) && dragboard.hasHtml()
        )
        conditionalLogAndSetDropCompleted(
            event, "Dropped RTF: " + dragboard.rtf,
            targetFormats.contains(DataFormat.RTF) && dragboard.hasRtf()
        )
        conditionalLogAndSetDropCompleted(
            event, "Dropped URL: " + dragboard.url,
            targetFormats.contains(DataFormat.URL) && dragboard.hasUrl()
        )
        conditionalLogAndSetDropCompleted(
            event, "Dropped image: " + dragboard.image,
            targetFormats.contains(DataFormat.IMAGE) && dragboard.hasImage()
        )
        conditionalLogAndSetDropCompleted(
            event, "Dropped files:" + dragboard.files.joinToString { "   " + it.path },
            targetFormats.contains(DataFormat.FILES) && dragboard.hasFiles()
        )
        conditionalLogAndSetDropCompleted(
            event, getCustomBytesText(dragboard),
            targetFormats.contains(CUSTOM_BYTES) && dragboard.hasContent(
                CUSTOM_BYTES
            )
        )
        conditionalLogAndSetDropCompleted(
            event, "Dropped custom string: " + dragboard.getContent(CUSTOM_STRING),
            targetFormats.contains(CUSTOM_STRING) && dragboard.hasContent(
                CUSTOM_STRING
            )
        )
    }

    private fun getCustomBytesText(db: Dragboard): String {
        val b = db.getContent(CUSTOM_BYTES) as ByteArray
        return "Dropped custom bytes: " + b[0] + ", " + b[1] + ", " + b[2] + ", " + b[3]
    }

    private fun conditionalLogAndSetDropCompleted(event: DragEvent, logMessage: String, dropCompleted: Boolean) {
        if (dropCompleted) {
            log(logMessage)
            event.isDropCompleted = true
        }
    }

    companion object {
        val CUSTOM_BYTES = DataFormat("dndwithcontrols.custom.bytes")
        val CUSTOM_STRING = DataFormat("dndwithcontrols.custom.string")
        const val HTML =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                    "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
                    "<head></head>" +
                    "<body>Hello!</body>" +
                    "</html>"
        const val RTF = "{\\rtf1\\ansi\\uc1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i FRED\\par rtf\\par text}"

        @JvmStatic
        fun info(): String {
            return ("This application provides for drag and drop"
                    + "between controls with toggles for the drag"
                    + "options and source types")
        }
    }
}
