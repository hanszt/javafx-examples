package hzt

import javafx.application.Application
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.image.Image
import javafx.scene.image.PixelReader
import javafx.scene.image.PixelWriter
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.slf4j.LoggerFactory
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

        val twistSlider = Slider(0.0, 1.0, 0.1)
        val centerXSlider = Slider(-1.0, 1.0, 0.0)
        val centerYSlider = Slider(-1.0, 1.0, 0.0)
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

        val image = createDenserGridImage(2000.0, 2000.0)
        //        val path = "/Users/hans/git/hzt/jvm/javafx-examples/javafx-2d-shape-examples/src/main/resources/img_1.png"
//        val fileUrl = File(path).toURI().toString()
//        log.info("Loading image from: $fileUrl")
//        val backgroundLoading = true
//        val sourceImage = Image(fileUrl, backgroundLoading)
//
//        sourceImage.progressProperty().addListener { _, _, progress ->
//            log.info("Image loading progress: ${progress.toDouble() * 100.0} %")
//            if (progress.toDouble() == 1.0) {
//                renderEscherDroste(canvas, sourceImage)
//            }
//        }

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
            println("logRatio = ${logRatio}")

            val alpha = atan(logRatio / (2 * PI))
            val f = cos(alpha)

            // Complex scale factor vector 'c' component construction
            val cReal = f * cos(alpha)
            val cImag = f * sin(alpha)

            // Inverse matrix coefficients for shifting backwards from screen to source
            val denom = cReal * cReal + cImag * cImag
            val invCReal = cReal / denom
            val invCImag = -cImag / denom

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

                    val radius = sqrt(nx * nx + ny * ny)
                    if (radius < 0.0001) continue // Prevent log(0) errors
                    val theta = atan2(ny, nx)

                    // 2. Forward Logarithmic mapping: ln(w) <- w
                    val logR = ln(radius)

                    // 3. Untwist: Divide by complex scale factor 'c' to flatten the diagonal grid
                    var transLogR = logR * invCReal - theta * invCImag
                    val transLogTheta = logR * invCImag + theta * invCReal

                    // 4. Structural Periodicity: Map the periodic tiles back into the [r1, r2] ring bounds
                    transLogR = ((transLogR % logRatio) + logRatio) % logRatio + ln(r1)

                    // 5. Exponential mapping: z -> e^z
                    val finalRadius = exp(transLogR)

                    // 6. Convert back to target Cartesian coordinates
                    val finalX = finalRadius * cos(transLogTheta) + centerShiftX
                    val finalY = finalRadius * sin(transLogTheta) + centerShiftY

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

}

fun main() {
    Application.launch(EscherDrosteApp::class.java)
}
