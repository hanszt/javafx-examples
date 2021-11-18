package hzt.gaussian_chart

import javafx.scene.chart.AreaChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart

class GaussianAreaChart : GaussianLineChart() {

    override fun createChart(): XYChart<Number, Number> = AreaChart(NumberAxis(), NumberAxis())

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = launch(GaussianAreaChart::class.java)
    }
}
