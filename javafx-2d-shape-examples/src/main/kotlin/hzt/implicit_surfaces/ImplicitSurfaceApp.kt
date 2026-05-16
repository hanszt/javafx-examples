package hzt.implicit_surfaces

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class ImplicitSurfaceApp : Application() {

    override fun start(primaryStage: Stage) {
        val width = 1000.0
        val height = 1000.0

        val canvas = Canvas(width, height)
        renderImplicitSurface(canvas)

        val root = StackPane(canvas)
        val scene = Scene(root, width, height, Color.BLACK)

        primaryStage.title = "2D Implicit Surface Renderer"
        primaryStage.scene = scene
        primaryStage.show()
    }

    private fun renderImplicitSurface(canvas: Canvas) {
        val width = canvas.width.toInt()
        val height = canvas.height.toInt()

        val image = WritableImage(width, height)
        val pixelWriter = image.pixelWriter

        // Coordinate mapping boundaries: Map pixels to math coordinates [-2.0, 2.0]
        val xMin = -2.0
        val xMax = 2.0
        val yMin = -2.0
        val yMax = 2.0

        // Parameter for our implicit equation
        val a = sqrt(2.0)

        // Thickness threshold for rendering the curve f(x, y) ≈ 0
        val thickness = 0.1

        for (screenX in 0 until width) {
            for (screenY in 0 until height) {

                // 1. Map screen pixels to Cartesian coordinates (x, y)
                val x = xMin + (screenX.toDouble() / width) * (xMax - xMin)
                // Invert Y so positive math Y goes UP on the screen
                val y = yMax - (screenY.toDouble() / height) * (yMax - yMin)

                // 2. Evaluate the implicit function: f(x, y) = (x² + y²)² - 2a²(x² - y²)
                val r2 = x.pow(2) + y.pow(2)
                val fValue = r2.pow(2) - 2 * a.pow(2) * (x.pow(2) - y.pow(2))

                // 3. Check if the point is close enough to the surface boundary (f(x,y) = 0)
                if (abs(fValue) < thickness) {
                    // Smooth out the edges using the distance from the threshold
                    val intensity = 1.0 - (abs(fValue) / thickness)

                    // Create a nice neon cyan color with varying intensity
                    val color = Color.color(0.0, intensity, intensity)
                    pixelWriter.setColor(screenX, screenY, color)
                } else {
                    // Background
                    pixelWriter.setColor(screenX, screenY, Color.BLACK)
                }
            }
        }

        // Draw the generated image onto the canvas
        canvas.graphicsContext2D.drawImage(image, 0.0, 0.0)
    }
}

fun main() {
    Application.launch(ImplicitSurfaceApp::class.java)
}
