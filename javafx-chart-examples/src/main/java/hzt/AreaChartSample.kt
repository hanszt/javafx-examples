package hzt

import javafx.scene.Node
import javafx.scene.chart.AreaChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.stage.Stage

class AreaChartSample : XYChartSample() {

    override fun start(stage: Stage) = start(stage, "Area Chart Sample")

    override fun setStyle(root: Node) {
        root.style = "-fx-base: rgb(40, 40, 40);-fx-background: rgb(60, 60, 60);"
    }

    override fun getChart(xAxis: NumberAxis, yAxis: NumberAxis): XYChart<Number, Number> = AreaChart(xAxis, yAxis)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = launch(AreaChartSample::class.java)
    }
}
