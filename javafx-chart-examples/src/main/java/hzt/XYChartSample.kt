package hzt

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import hzt.controls.MultiListView
import hzt.utils.FxSetup
import javafx.application.Application
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.SelectionMode
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.time.Month
import java.util.*

open class XYChartSample : Application() {

    private val tempDataSeriesBiMap: BiMap<TempData, XYChart.Series<Number, Number>> = HashBiMap.create()

    override fun start(stage: Stage) = start(stage, "Default XY Chart Sample")

    protected fun start(stage: Stage?, title: String?) {
        val xAxis = NumberAxis("Days", 1.0, 31.0, 1.0).apply { isForceZeroInRange = true }
        val chart = getChart(xAxis, FxSetup.numberAxis("Temperature (degrees C)")).apply {
            this.title = "Temperature Monitoring (in Degrees C)"
            isHorizontalZeroLineVisible = true
            prefHeight = 500.0
            minHeight = 300.0
        }
        val monthsMultiListView = MultiListView<TempData>("Undisplayed months", "Displayed months")
            .apply {
                setComparatorListViews(Comparator.comparingInt { t: TempData -> t.month.value }, true)
                addRightListviewListener { change -> updateXYChart(chart, change) }
                setSelectionMode(SelectionMode.MULTIPLE)
                addInitData(listOf(TempData.MARCH, TempData.APRIL, TempData.MAY))
            }
        val updateButton = FxSetup.configuredButton("Update", 100.0) { updateData(chart) }
        val vbox = VBox(chart, FxSetup.configuredHBox(Insets(10.0, 10.0, 10.0, 50.0), updateButton, monthsMultiListView))
        setStyle(vbox)
        val scene = Scene(vbox, 1200.0, 780.0)
        FxSetup.configureStage(stage, scene, title, 400.0, 500.0).show()
    }

    protected open fun setStyle(root: Node) {
        root.style = ""
    }

    protected open fun getChart(xAxis: NumberAxis, yAxis: NumberAxis): XYChart<Number, Number> = LineChart(xAxis, yAxis)

    private fun MultiListView<TempData>.addInitData(initData: List<TempData>) {
        for (data in TempData.entries) {
            val series = XYChart.Series<Number, Number>().apply { name = data.formattedName() }
            tempDataSeriesBiMap[data] = series
            if (data in initData) addRightListView(data) else addLeftListView(data)

            for (day in 1..data.month.length(false)) {
                if (day % 3 == 1 || day == data.month.maxLength()) {
                    series.data.add(XYChart.Data(day, data.randomTemperature))
                }
            }
        }
    }

    private fun updateXYChart(areaChart: XYChart<Number, Number>, change: ListChangeListener.Change<out TempData>) {
        if (change.next()) {
            val data = areaChart.data
            if (change.wasAdded()) {
                data.addAll(change.addedSubList
                    .map { key -> tempDataSeriesBiMap[key] }
                    .filter { o -> o !in data }
                )
            }
            if (change.wasRemoved()) {
                data.retainAll(change.list.map { key -> tempDataSeriesBiMap[key] })
            }
        }
    }

    private fun updateData(areaChart: XYChart<Number, Number>) {
        for (series in areaChart.data) {
            val temperatureData = tempDataSeriesBiMap.inverse()[series]
            series.data.forEach { data -> data.yValue = temperatureData?.randomTemperature ?: 0 }
        }
    }

    private enum class TempData(
        @field:Transient val month: Month,
        private val minTemp: Double,
        private val maxTemp: Double
    ) {
        JANUARY(Month.JANUARY, -15.0, 8.0),
        FEBRUARY(Month.FEBRUARY, -15.0, 6.0),
        MARCH(Month.MARCH, -2.0, 15.0),
        APRIL(Month.APRIL, 3.0, 18.0),
        MAY(Month.MAY, 10.0, 22.0),
        JUNE(Month.JUNE, 16.0, 30.0),
        JULY(Month.JULY, 18.0, 35.0),
        AUGUST(Month.AUGUST, 24.0, 40.0),
        SEPTEMBER(Month.SEPTEMBER, 16.0, 30.0),
        OCTOBER(Month.OCTOBER, 14.0, 24.0),
        NOVEMBER(Month.NOVEMBER, -2.0, 15.0),
        DECEMBER(Month.DECEMBER, -10.0, 10.0);

        val randomTemperature: Double
            get() {
                val difference = maxTemp - minTemp
                return minTemp + difference * Math.random()
            }

        fun formattedName(): String = month.name[0].toString() + month.name.substring(1).lowercase(Locale.getDefault())

        override fun toString(): String = formattedName()
    }
}
