package strechygrid

import javafx.scene.Scene
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import kotlin.test.assertEquals

@ExtendWith(ApplicationExtension::class)
internal class GridTest {

    private val grid: Grid
        get() {
            return Grid(100, 8.0)
        }

    @Start
    fun onStartup(stage: Stage) {
        val grid = this.grid
        val scene = Scene(grid)
        grid.configure()
        stage.scene = scene
        stage.title = javaClass.simpleName
    }

    @Test
    fun `when grid added then cell side count is equal to 100`() {
        val numCellSide = grid.numCellSide
        assertEquals(100, numCellSide)
    }


}
