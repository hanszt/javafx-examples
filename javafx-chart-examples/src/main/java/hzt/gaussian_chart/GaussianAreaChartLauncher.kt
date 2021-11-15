package hzt.gaussian_chart

import javafx.application.Application
import javafx.stage.Stage

fun main() = GaussianAreaChartApp().start()

class GaussianAreaChartApp : Application() {

    fun start() = launch()

    override fun start(stage: Stage) = GaussianAreaChart().start(stage)

}
