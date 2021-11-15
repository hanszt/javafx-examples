package hzt.gaussian_chart

import javafx.application.Application
import javafx.stage.Stage

fun main() = GaussianChartApp().start()

class GaussianChartApp : Application() {

    fun start() = launch()

    override fun start(stage: Stage) = GaussianLineChart().start(stage)

}
