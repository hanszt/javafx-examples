package hzt.gaussian_chart

import fxutils.ifImagePresent
import fxutils.onChange
import fxutils.onPressed
import fxutils.setColor
import fxutils.setLineDashed
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


class GaussianChart {

    private val stdDevSlider: Slider = Slider()

    private val meanSlider: Slider = Slider()

    private val groupingSlider: Slider = Slider()

    private val series = XYChart.Series<Number, Number>().apply { name = "Gaussian distribution" }

    fun start(stage: Stage) {
        val titleLabel = Label("Gaussian Chart").apply { font = Font.font(20.0) }
        val sliderBox = configuredSliderBox()

        val toggleButton = ToggleButton("Line dashed")
        toggleButton.onPressed(series::setLineDashed)

        val chart = createLineChart()

        val colorPicker = ColorPicker().apply {
            onChange(series::setColor)
            value = Color.BLUE
        }

        val root = VBox(titleLabel, chart, sliderBox, HBox(colorPicker, toggleButton)).apply { spacing = 10.0 }
        val scene = Scene(root, 800.0, 600.0)
        val location = "/../icons/fx-icon.png"

        stage.apply {
            this.scene = scene
            javaClass.getResourceAsStream(location).ifImagePresent(icons::add)
            show()
        }
    }

    private fun configuredSliderBox(): HBox {
        meanSlider.configure(-100.0, 5.0, 100.0)
        stdDevSlider.configure(5.0, 20.0)
        groupingSlider.configure(-1.0, .5, 1.0)
        return HBox(
            VBox(Label("standard deviation slider"), stdDevSlider),
            VBox(Label("mean slider"), meanSlider),
            VBox(Label("grouping slider"), groupingSlider)
        )
    }

    private fun Slider.configure(value: Double, max: Double) = configure(0.0, value, max)

    private fun Slider.configure(min: Double, value: Double, max: Double) {
        onChange { series.updateDataPoints() }
        this.min = min
        this.value = value
        this.max = max
        isShowTickLabels = true
        isShowTickMarks = true
    }

    private fun createLineChart(): XYChart<Number, Number> {
        series.data.addAll(createDataPoints(meanSlider.value, stdDevSlider.value))
        return LineChart(NumberAxis(), NumberAxis()).apply { data.addAll(series) }
    }

    private fun createDataPoints(mean: Double, stdDev: Double): List<XYChart.Data<Number, Number>> {
        return IntStream.range(0, 100_000)
            .mapToObj { RANDOM.nextGaussian(mean, stdDev) }
            .groupBy(::roundedGaussian)
            .map { XYChart.Data(it.key.toInt(), it.value.size) }
    }

    private fun XYChart.Series<Number, Number>.updateDataPoints() {
        data.clear()
        val createDataPoints = createDataPoints(meanSlider.value, stdDevSlider.value)
        data.addAll(createDataPoints)
    }

    private fun roundedGaussian(gaussian: Double): BigDecimal {
        return gaussian.toBigDecimal()
            .multiply(BigDecimal.valueOf(groupingSlider.value))
            .setScale(0, RoundingMode.HALF_UP)
    }

    companion object {
        val RANDOM = Random()
    }

}
