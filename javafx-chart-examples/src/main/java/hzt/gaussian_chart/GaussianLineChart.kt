package hzt.gaussian_chart

import fxutils.*
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.ColorPicker
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.control.ToggleButton
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import utils.stream.groupBy
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.stream.IntStream

open class GaussianLineChart(
    private val stdDevSlider: Slider = Slider(),
    private val meanSlider: Slider = Slider(),
    private val groupingSlider: Slider = Slider(),
    private val series: XYChart.Series<Number, Number> = XYChart.Series<Number, Number>()
) : Application() {

    private fun configureComponents() {
        stdDevSlider.configure(5.0, 20.0)
        meanSlider.configure(-100.0, 5.0, 100.0)
        groupingSlider.configure(-1.0, 1.0, 1.0)
        series.apply { name = "Gaussian distribution" }
    }

    override fun start(stage: Stage) {
        configureComponents()
        val titleLabel = Label("Gaussian Chart").apply { font = Font.font(20.0) }
        val sliderBox = configuredSliderBox()

        val toggleButton = ToggleButton("Line dashed")
        toggleButton.onPressed(series::setLineDashed)

        series.addGaussianDataPoints(meanSlider.value, stdDevSlider.value)
        val chart = createChart().apply { data.add(series) }

        val colorPicker = ColorPicker().apply {
            onChange(series::setColor)
            value = Color.BLUE
        }

        val root = VBox(titleLabel, chart, sliderBox, HBox(colorPicker, toggleButton)).apply { spacing = 10.0 }
        val scene = Scene(root, 800.0, 600.0)
        val location = "/icons/fx-icon.png"

        stage.apply {
            this.scene = scene
            GaussianLineChart::class.java.getResourceAsStream(location).ifImagePresent(icons::add)
        }.show()
    }

    private fun configuredSliderBox() = HBox(
        VBox(Label("standard deviation slider"), stdDevSlider),
        VBox(Label("mean slider"), meanSlider),
        VBox(Label("grouping slider"), groupingSlider)
    )

    private fun Slider.configure(value: Double, max: Double): Slider = configure(0.0, value, max)

    private fun Slider.configure(min: Double, value: Double, max: Double): Slider {
        this.min = min
        this.value = value
        this.max = max
        isShowTickLabels = true
        isShowTickMarks = true
        onChange { series.updateDataPoints() }
        return this
    }

    open fun createChart(): XYChart<Number, Number> = LineChart(NumberAxis(), NumberAxis())

    private fun XYChart.Series<Number, Number>.addGaussianDataPoints(mean: Double, stdDev: Double) {
        data.addAll(IntStream.range(0, 100_000)
            .mapToObj { RANDOM.nextGaussian(mean, stdDev) }
            .groupBy(::roundedGaussian)
            .map { XYChart.Data(it.key.toInt(), it.value.size) })
    }

    private fun XYChart.Series<Number, Number>.updateDataPoints() {
        data.clear()
        addGaussianDataPoints(meanSlider.value, stdDevSlider.value)
    }

    private fun roundedGaussian(gaussian: Double): BigDecimal = (gaussian * groupingSlider.value).toBigDecimal()
        .setScale(0, RoundingMode.HALF_UP)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = launch(GaussianLineChart::class.java)
        val RANDOM = Random()
    }
}
