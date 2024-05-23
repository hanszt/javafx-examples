package hzt

import javafx.application.Application
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.apache.commons.math3.complex.Complex

fun main() = MandelbrotSetRunner().start()

/**
 * [Mandelbrot](https://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html)
 */
class MandelbrotSetRunner : Application() {

    fun start() = launch()

    private val canvasSideLength = 600.0
    private val maxRgb = 255

    override fun start(primaryStage: Stage) {
        val initSize = 2.0
        val initMinXY = -2.0
        val initMaxXY = 2.0

        val canvas = Canvas(canvasSideLength, canvasSideLength)
        val gc = canvas.graphicsContext2D

        val sizeField = TextField(initSize.toString())

        val xcSlider = createSlider(min = initMinXY, max = initMaxXY, value = -0.5)
        val ycSlider = createSlider(min = initMinXY, max = initMaxXY, value = 0.0)
        val sizeSlider = createSlider(min = 0.0, max = 8.0, value = initSize)
        val submitButton = Button("Submit")

        fun update(size: Double) {
            val xc = xcSlider.value
            val yc = ycSlider.value
            gc.plotMandelbrot(xc, yc, size)
            xcSlider.min = xc + initMinXY * size
            xcSlider.max = xc + initMaxXY * size
            ycSlider.min = yc + initMinXY * size
            ycSlider.max = yc + initMaxXY * size
            sizeSlider.value = size
            sizeField.text = size.toString()
        }

        canvas.setOnMouseClicked { e ->
            val nx = e.x / canvasSideLength
            val ny = e.y / canvasSideLength
            xcSlider.value += (nx - 0.5) * sizeSlider.value
            ycSlider.value += (ny - 0.5) * sizeSlider.value
            gc.plotMandelbrot(xcSlider.value, ycSlider.value, sizeSlider.value)
        }
        xcSlider.onMouseReleased = EventHandler { update(sizeSlider.value) }
        ycSlider.onMouseReleased = EventHandler { update(sizeSlider.value) }
        sizeSlider.onMouseReleased = EventHandler { update(sizeSlider.value) }
        submitButton.onAction = EventHandler { update(sizeField.text.toDouble()) }

        gc.plotMandelbrot(xc = xcSlider.value, yc = ycSlider.value, size = sizeSlider.value)

        val controls = VBox(xcSlider, ycSlider, sizeSlider, HBox(sizeField, submitButton)).apply {
            padding = Insets(20.0)
            spacing = 20.0
        }

        primaryStage.apply {
            scene = Scene(VBox(canvas, controls), canvasSideLength, canvasSideLength + 200.0)
            title = "Mandelbrot set"
        }.show()
    }

    private fun createSlider(min: Double, max: Double, value: Double): Slider = Slider().apply {
        this.min = min
        this.max = max
        this.value = value
        blockIncrement = 1.0
        majorTickUnit = 1.0
        minorTickCount = 0
        isShowTickLabels = true
    }

    /**
     * [Mandelbrot set](https://en.wikipedia.org/wiki/Mandelbrot_set)
     *
     * z_{n+1}=z_{n}^{2}+c
     */
    private fun mand(z0: Complex): Int {
        var z = z0
        for (rgb in 0 until maxRgb) {
            if (z.abs() > 100.0) {
                return rgb
            }
            z = z.multiply(z).add(z0)
        }
        return maxRgb
    }

    private fun GraphicsContext.plotMandelbrot(xc: Double, yc: Double, size: Double) {

        val n = canvasSideLength.toInt()

        for (y in 0 until n) {
            for (x in 0 until n) {
                val x0 = xc - size / 2 + size * x / n
                val y0 = yc - size / 2 + size * y / n

                this.fill = Color.grayRgb(maxRgb - mand(Complex(x0, y0)))
                fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
                // Create a point to aim with
                if (x == n / 2 && y == n / 2) {
                    this.fill = Color.RED
                    fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
                }
            }
        }
    }
}
