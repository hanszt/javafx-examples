package hzt

import fxutils.firstCharUpperCase
import hzt.utils.FxSetup
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.time.LocalDate
import java.time.Month
import java.util.*
import java.util.function.Consumer

fun main() = LineChartSample().start()

class LineChartSample : Application() {

    fun start() = launch()

    override fun start(stage: Stage) {
        val lineChart = LineChart(FxSetup.categoryAxis("Month"), FxSetup.numberAxis("Amount")).apply {
            title = "Stock Monitoring, " + LocalDate.now().year
            createSymbols = false
            isAlternativeRowFillVisible = false
            data.addAll(
                FxSetup.series("Portfolio 1"),
                FxSetup.series("Portfolio 2"),
                FxSetup.series("Portfolio 3")
            )
            data.forEach(Consumer { series: XYChart.Series<String, Number> -> getRandomData(series) })
            effect = FxSetup.preparedShadow(Color.GREY)
        }

        val updateButton = FxSetup.configuredButton("Update") { update(lineChart) }
        val scene = Scene(VBox(lineChart, FxSetup.configuredHBox(updateButton)))
        FxSetup.configureStage(stage, scene, "Line Chart Sample").show()
    }

    companion object {

        private const val MAX = 60

        private fun update(lineChart: LineChart<String, Number>) = lineChart.data.forEach { getRandomData(it) }

        private fun getRandomData(series: XYChart.Series<String, Number>) {
            val data = series.data.apply { clear() }
            Month.values().mapTo(data) { XYChart.Data(it.name.firstCharUpperCase(), Random().nextInt(MAX)) }
        }
    }
}
