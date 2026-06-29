package hzt

import javafx.application.Application
import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.image.Image
import javafx.scene.image.PixelReader
import javafx.scene.image.PixelWriter
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.apache.commons.math3.complex.Complex
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.math.*

/**
 * Inspired by [How (and why) to take a logarithm of an image](https://www.youtube.com/watch?v=ldxFjLJ3rVY&t=2416s)
 *
 * See also [How (and why) to take a logarithm of an image](https://www.3blue1brown.com/?lesson=print-gallery&search=escher)
 */
class EscherDrosteApp : Application() {

    private val log = LoggerFactory.getLogger("EscherDrosteApp")

    private val windowWidth = 900.0
    private val windowHeight = 900.0

    override fun start(stage: Stage) {
        log.info("Starting EscherDroste sim...")
        val canvas = Canvas()

        val canvasWrapper = Pane(canvas)

        canvas.widthProperty().bind(canvasWrapper.widthProperty())
        canvas.heightProperty().bind(canvasWrapper.heightProperty())

        val initialZoom = 1.0 / 128.0
        val twistSlider = ResettableSlider(0.0, 1.0, initialZoom)
        val centerXSlider = ResettableSlider( -1.0, 1.0, 0.0)
        val centerYSlider = ResettableSlider( -1.0, 1.0, 0.0)
        val controlPane = VBox(
            2.0,
            Label("Twist slider"), twistSlider,
            Label("Center x slider"), centerXSlider,
            Label("Center y slider"), centerYSlider,
        ).apply {
            padding = Insets(10.0)
        }

        VBox.setVgrow(canvasWrapper, Priority.ALWAYS)
        VBox.setVgrow(controlPane, Priority.NEVER)

        val root = VBox(2.0, canvasWrapper, controlPane)

        val scene = Scene(root, windowWidth, windowHeight)

//        val image = createDenserGridImage(900.0, 900.0)
        val image = loadImage("/Users/hans/git/hzt/jvm/javafx-examples/javafx-2d-shape-examples/src/main/resources/img_1.png")

        val drosteRenderer = DrosteRenderer(
            innerFrameRadius = twistSlider.valueProperty(),
            centerXSlider.valueProperty(),
            centerYSlider.valueProperty(),
        )

        canvas.widthProperty().addListener { _, _, _ -> drosteRenderer.renderEscherDroste(canvas, image) }
        canvas.heightProperty().addListener { _, _, _ -> drosteRenderer.renderEscherDroste(canvas, image) }
        twistSlider.valueProperty().addListener { _, _, _ -> drosteRenderer.renderEscherDroste(canvas, image) }
        centerXSlider.valueProperty().addListener { _, _, _ -> drosteRenderer.renderEscherDroste(canvas, image) }
        centerYSlider.valueProperty().addListener { _, _, _ -> drosteRenderer.renderEscherDroste(canvas, image) }

        // Initial render
        drosteRenderer.renderEscherDroste(canvas, image)

        stage.title = "Escher-Droste Effect Simulation"
        stage.scene = scene
        stage.show()
    }

    private fun loadImage(path: String): Image {
        val fileUrl = File(path).toURI().toString()
        log.info("Loading image from: $fileUrl")
        val backgroundLoading = false
        val image = Image(fileUrl, backgroundLoading)

        image.progressProperty().addListener { _, _, progress ->
            log.info("Image loading progress: ${progress.toDouble() * 100.0} %")
        }
        return image
    }

    class DrosteRenderer(
        private val innerFrameRadius: ReadOnlyDoubleProperty,
        private val centerOffsetX: ReadOnlyDoubleProperty,
        private val centerOffsetY: ReadOnlyDoubleProperty,
    ) {

        fun renderEscherDroste(canvas: Canvas, source: Image) {
            val srcW = source.width
            val srcH = source.height
            val canvasWidth = canvas.width
            val canvasHeight = canvas.height

            val r1 = innerFrameRadius.value
            val r2 = 1.0
            val logRatio = ln(r2 / r1)

            // Calculate the twist angle 'alpha' that fixes the diagonal tile alignment
            val alpha = atan(logRatio / (2 * PI))
            val f = cos(alpha)

            // Complex scale factor vector 'c' component construction
            val c = Complex(f * cos(alpha), f * sin(alpha))

            // Center offsets
            val centerShiftX = centerOffsetX.value
            val centerShiftY = centerOffsetY.value

            val outCenterX = canvasWidth / 2.0
            val outCenterY = canvasHeight / 2.0

            // Process every output pixel on the canvas
            for (y in 0 until canvasHeight.toInt()) {
                for (x in 0 until canvasWidth.toInt()) {

                    // 1. Normalize screen position into Complex Plane coordinates [-1.0, 1.0]
                    val nx = (x - outCenterX) / (canvasWidth / 2.0)
                    val ny = (outCenterY - y) / (canvasHeight / 2.0) // Invert Y axis

                    val w = Complex(nx, ny)

                    if (w.abs() < 0.0001) continue // Prevent log(0) errors
                    // 2 & 3. Forward Logarithmic mapping AND Untwist in one step
                    // w.log() handles finding the radius and theta automatically
                    val complex = w.log()
                    val transformedLog = complex.divide(c)
                    // 4. Structural Periodicity: Map back into the [r1, r2] ring bounds
                    val transLogR = ((transformedLog.real % logRatio) + logRatio) % logRatio + ln(r1)

                    // 5. Exponential mapping: z -> e^z
                    val finalRadius = exp(transLogR)

                    // 6. Convert back to target Cartesian coordinates
                    val finalX = finalRadius * cos(transformedLog.imaginary) + centerShiftX
                    val finalY = finalRadius * sin(transformedLog.imaginary) + centerShiftY

                    // 7. Map normalized coordinate scales back to physical source image pixels
                    val srcPixelX = ((finalX + 1.0) * 0.5 * srcW).toInt()
                    val srcPixelY = ((1.0 - finalY) * 0.5 * srcH).toInt()

                    val gc = canvas.graphicsContext2D
                    val writer: PixelWriter = gc.pixelWriter
                    val reader: PixelReader = source.pixelReader
                    // 8. Draw pixel color if validly positioned inside frame boundaries
                    if (srcPixelX in 0 until srcW.toInt() && srcPixelY in 0 until srcH.toInt()) {
                        val color = reader.getColor(srcPixelX, srcPixelY)
                        writer.setColor(x, y, color)
                    }
                }
            }
        }
    }

    private class ResettableSlider(min: Double, max: Double, value: Double) : HBox(2.0) {
        private val slider = Slider(min, max, value).also {
            children.add(it)
            // Tell the HBox that this slider should take up all extra horizontal space
            setHgrow(it, Priority.ALWAYS)
            // Ensure the slider itself is configured to fill its allotted width
            it.maxWidth = Double.MAX_VALUE
        }
        init {
            Button("Reset").also {
                children.add(it)
                it.setOnAction { slider.value = value }
            }
        }

        fun valueProperty(): DoubleProperty = slider.valueProperty()
    }
}

fun main() {
    Application.launch(EscherDrosteApp::class.java)
}
