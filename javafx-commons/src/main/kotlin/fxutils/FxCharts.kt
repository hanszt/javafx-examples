package fxutils

import javafx.application.Platform
import javafx.collections.ListChangeListener
import javafx.scene.chart.XYChart
import javafx.scene.paint.Color

fun <X, Y> XYChart.Series<X, Y>.setColor(color: Color) {
    val rgb = rgb(color)
    val seriesIndex = chart.data.indexOf(this)
    val dataPointStyle = "-fx-background-color: rgba($rgb, 1.0), white;"
    Platform.runLater {
        chart.lookupAll(".series$seriesIndex").forEach { it.style = dataPointStyle }
        node.style = "-fx-stroke: rgba($rgb, 1.0);"
        node.lookup(".chart-series-area-fill")?.style = "-fx-fill: rgba($rgb, .15)"
        node.lookup(".chart-series-area-line")?.style = "-fx-stroke: rgba($rgb, 1.0)"
    }
    data.addListener(ListChangeListener { change ->
        if (change.next()) {
            change.addedSubList.forEach { it.node.style = dataPointStyle }
        }
    })
}

fun <X, Y> XYChart.Series<X, Y>.setColorAndResetOtherSeriesColors(color: Color) {
    val seriesIndex = chart.data.indexOf(this)
    chart.style = "CHART_COLOR_${seriesIndex + 1}: rgba(${rgb(color)}, 1.0);"
}

fun <X, Y> XYChart.Series<X, Y>.setLineDashed(dashed: Boolean) {
    Platform.runLater {
        node.lookup(".chart-series-line")?.style = "-fx-stroke-dash-array: ${if (dashed) "10 10 10 10" else null};"
    }

}

private fun rgb(color: Color) = String.format(
    "%d, %d, %d",
    (color.red * 255).toInt(),
    (color.green * 255).toInt(),
    (color.blue * 255).toInt()
)
