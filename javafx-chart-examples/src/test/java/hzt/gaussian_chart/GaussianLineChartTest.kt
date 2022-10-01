package hzt.gaussian_chart

import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import kotlin.test.assertTrue

@ExtendWith(ApplicationExtension::class)
internal class GaussianLineChartTest {

    private lateinit var gaussianLineChart: GaussianLineChart

    @Start
    fun onStart(stage: Stage) {
        gaussianLineChart = GaussianLineChart()
        gaussianLineChart.start(stage)
    }

    @Test
    fun `when chart created, no data is present`() {
        val chart = gaussianLineChart.createChart()
        assertTrue(chart.data.isEmpty())
    }
}
