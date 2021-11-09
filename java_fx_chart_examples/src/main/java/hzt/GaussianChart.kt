package hzt

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Stage
import java.util.*

fun main() = GaussianChart().start()

class GaussianChart : Application() {

    fun start() = launch()

    override fun start(stage: Stage) {
        val titleLabel = Label("Gaussian Chart").apply { font = Font.font(20.0) }

        val root = VBox(titleLabel, lineChart()).apply { spacing = 10.0 }
        val scene = Scene(root, 800.0, 600.0)
        val location = "/../icons/fx-icon.png"
        stage.apply {
            this.scene = scene
            Optional.ofNullable(javaClass.getResourceAsStream(location)).map(::Image).ifPresent(icons::add)
        }.show()
    }

    private fun lineChart(): LineChart<Number, Number> {
        val nextGaussian = RANDOM.nextGaussian(3.0, 4.0)

        val dataPoint = XYChart.Data<Number, Number>(3.0, nextGaussian)
        val dataPoint2 = XYChart.Data<Number, Number>(2.0, 4.0)
        val dataPoint3 = XYChart.Data<Number, Number>(1.0, 7.0)
        val dataPoint4 = XYChart.Data<Number, Number>(.5, 1.0)

        val series = XYChart.Series<Number, Number>().apply {
            data.addAll(dataPoint, dataPoint2, dataPoint3, dataPoint4)
            name = "Gaussian distribution"
        }
        return LineChart(NumberAxis(), NumberAxis()).apply<LineChart<Number, Number>> { data.addAll(series) }
    }

    companion object {
        val RANDOM = Random()
    }


}
