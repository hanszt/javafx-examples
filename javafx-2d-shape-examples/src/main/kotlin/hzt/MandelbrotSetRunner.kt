package hzt

import hzt.javafx.controls.FunctionalSlider
import javafx.application.Application
import javafx.beans.property.DoubleProperty
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Button
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.util.StringConverter
import org.apache.commons.math3.complex.Complex
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.pow

fun main() = MandelbrotSetRunner().start()

private const val SCIENTIFIC_FORMAT = "%1.1e"

/**
 * [Mandelbrot](https://introcs.cs.princeton.edu/java/32class/Mandelbrot.java.html)
 */
class MandelbrotSetRunner : Application() {

    fun start() = launch()

    private val canvasSideLength = 600.0
    private val max = 255

    override fun start(primaryStage: Stage) {
        val initSize = 2.0
        val initMinXY = -2.0
        val initMaxXY = 2.0

        val canvas = Canvas(canvasSideLength, canvasSideLength)
        val gc = canvas.graphicsContext2D

        val sizeField = TextField(initSize.toString()).apply { prefWidth = 200.0 }

        val xcSlider = Slider().configureSlider(min = initMinXY, max = initMaxXY, value = -0.5)
        val ycSlider = Slider().configureSlider(min = initMinXY, max = initMaxXY, value = 0.0).apply {
            orientation = Orientation.VERTICAL
            labelFormatter = positionSliderLabelFormatter(minProperty(), maxProperty())
        }
        val sizeSlider = FunctionalSlider(SCIENTIFIC_FORMAT) { 10.0.pow(it) }.configureSlider(
            min = -16.0,
            max = 1.0,
            value = log(x = initSize, base = 10.0)
        )

        fun update(size: Double) {
            val xc = xcSlider.value
            val yc = ycSlider.value
            gc.plotMandelbrot(xc, -yc, size)
            xcSlider.min = xc + initMinXY * size
            xcSlider.max = xc + initMaxXY * size
            ycSlider.min = yc + initMinXY * size
            ycSlider.max = yc + initMaxXY * size
            sizeSlider.setValues(size) { log(x = it, base = 10.0) }
            sizeField.text = size.toString()
        }

        val submitButton = Button("Submit")

        sizeField.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                update(sizeField.text.toDouble())
            }
        }

        canvas.setOnMouseClicked { e ->
            val nx = e.x / canvasSideLength
            val ny = e.y / canvasSideLength
            xcSlider.value += (nx - 0.5) * sizeSlider.functionValue
            ycSlider.value -= (ny - 0.5) * sizeSlider.functionValue
            update(sizeSlider.functionValue)
        }
        xcSlider.onMouseReleased = EventHandler { update(sizeSlider.functionValue) }
        ycSlider.onMouseReleased = EventHandler { update(sizeSlider.functionValue) }
        sizeSlider.onMouseReleased = EventHandler { update(sizeSlider.functionValue) }
        submitButton.onAction = EventHandler { update(sizeField.text.toDouble()) }

        gc.plotMandelbrot(xc = xcSlider.value, yc = ycSlider.value, size = sizeSlider.functionValue)

        val controls = VBox(xcSlider, sizeSlider, HBox(sizeField, submitButton)).apply {
            padding = Insets(20.0)
            spacing = 20.0
        }

        primaryStage.apply {
            val sceneWidth = canvasSideLength + 50.0
            val sceneHight = canvasSideLength + 200.0
            val root = VBox(HBox(canvas, ycSlider), controls)
            scene = Scene(root, sceneWidth, sceneHight)
            title = "Mandelbrot set"
        }.show()
    }

    private fun positionSliderLabelFormatter(min: DoubleProperty, max: DoubleProperty) = object : StringConverter<Double>() {
        override fun toString(x: Double): String {
            return if ((max.get() - min.get()).absoluteValue < 1e-2) SCIENTIFIC_FORMAT.format(x) else "%.2f".format(x)
        }

        override fun fromString(s: String): Double? {
            return null
        }
    }

    private fun <S : Slider> S.configureSlider(min: Double, max: Double, value: Double): S = apply {
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
     *
     * [Adding colors](https://stackoverflow.com/questions/29664389/adding-color-layers-to-the-mandelbrot-set)
     */
    private fun mandSmooth(z0: Complex): Double {
        var z = z0
        for (t in 0 until max) {
            val fAbs = z.abs()
            if (fAbs > 2.0) {
                // based on the final value, add a fractional amount based on
                // how much it escaped by (fAbs will be in the range of 2 to around 4):
                return (t.toDouble() + (2.0 - (ln(fAbs) / ln(2.0))))
            }
            z = z.multiply(z).add(z0)
        }
        return max.toDouble()
    }


    private fun GraphicsContext.plotMandelbrot(xc: Double, yc: Double, size: Double) {

        val n = canvasSideLength.toInt()

        for (y in 0 until n) {
            for (x in 0 until n) {
                val halfSize = size / 2
                val x0 = xc - halfSize + size * x / n
                val y0 = yc - halfSize + size * y / n

                val z0 = Complex(x0, y0)
                val result = mandSmooth(z0)
                this.fill = Color.hsb(result, 1.0, 1.0)
                fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
                // Create a point to aim with
                if (x == n / 2 && y == n / 2) {
                    this.fill = Color.BLACK
                    fillRect(x.toDouble(), y.toDouble(), 1.0, 1.0)
                }
            }
        }
    }
}
