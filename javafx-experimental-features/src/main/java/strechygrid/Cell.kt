package strechygrid

import fxutils.onNewValue
import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import java.util.HashMap

class Cell internal constructor(private val grid: Grid, private val row: Int, private val col: Int) {

    private val cx: Double
    private val cy: Double
    private val x: DoubleProperty = SimpleDoubleProperty()
    private val y: DoubleProperty = SimpleDoubleProperty()
    private val xv: DoubleProperty = SimpleDoubleProperty()
    private val yv: DoubleProperty = SimpleDoubleProperty()
    private val color: ObjectProperty<Color> = SimpleObjectProperty(Color.hsb(HUE_REF, 1.0, 1.0))
    private val cellToDirMapperMap: MutableMap<Cell?, Point2D> = HashMap()

    private var left: Cell? = null
    var right: Cell? = null
        private set
    var bottom: Cell? = null
        private set
    private var top: Cell? = null

    // called after the whole grid.getCells() sequence has been initialized/filled
    fun initAdjacencyRelations() {
        left = if (col > 0) grid.getCellByIndex(row * grid.numCellSide + col - 1) else null
        cellToDirMapperMap[left] = Point2D(1.0, 0.0)
        right = if (col < grid.numCellSide - 1) grid.getCellByIndex(row * grid.numCellSide + col + 1) else null
        cellToDirMapperMap[right] = Point2D(-1.0, 0.0)
        top = if (row > 0) grid.getCellByIndex((row - 1) * grid.numCellSide + col) else null
        cellToDirMapperMap[top] = Point2D(0.0, 1.0)
        bottom = if (row < grid.numCellSide - 1) grid.getCellByIndex((row + 1) * grid.numCellSide + col) else null
        cellToDirMapperMap[bottom] = Point2D(0.0, -1.0)
    }

    private fun getCellSizeMapper(neighbor: Cell?): Point2D? {
        return cellToDirMapperMap[neighbor]
    }

    fun update() {
        xv.set(xv.get() + (cx - x.get()) * K_1)
        yv.set(yv.get() + (cy - y.get()) * K_1)
        updateCellXvAndYvIfNeighborNotNull(left)
        updateCellXvAndYvIfNeighborNotNull(right)
        updateCellXvAndYvIfNeighborNotNull(top)
        updateCellXvAndYvIfNeighborNotNull(bottom)
        xv.set(xv.get() * DAMP)
        yv.set(yv.get() * DAMP)
        x.set(x.get() + xv.get().toInt())
        y.set(y.get() + yv.get().toInt())
    }

    private fun updateColor() {
        val dX = cx - x.get()
        val dY = cy - y.get()
        color.set(Color.hsb(HUE_REF + (dX * dX + dY * dY), 1.0, 1.0))
    }

    private fun updateCellXvAndYvIfNeighborNotNull(neighbor: Cell?) {
        if (neighbor != null) {
            val mapper = getCellSizeMapper(neighbor)
            xv.set(xv.get() + (neighbor.x.get() + mapper!!.x * grid.cellSize - x.get()) * K_2)
            yv.set(yv.get() + (neighbor.y.get() + mapper.y * grid.cellSize - y.get()) * K_2)
        }
    }

    fun setVelocityVector(vX: Double, vY: Double) {
        xv.set(vX)
        yv.set(vY)
    }

    fun damp(factor: Double) {
        xv.set(xv.get() * factor)
        yv.set(yv.get() * factor)
    }

    fun xProperty(): DoubleProperty {
        return x
    }

    fun setX(x: Double) {
        this.x.set(x)
    }

    fun yProperty(): DoubleProperty {
        return y
    }

    fun setY(y: Double) {
        this.y.set(y)
    }

    fun colorProperty(): ObjectProperty<Color> {
        return color
    }

    companion object {
        private const val HUE_REF = 180.0
        private const val K_1 = 0.01
        private const val K_2 = 0.20000000000000001
        private const val DAMP = 0.74
    }

    init {
        x.set(col * grid.cellSize)
        y.set(row * grid.cellSize)
        cx = x.get()
        cy = y.get()
        xv.onNewValue { updateColor() }
        yv.onNewValue { updateColor() }
    }
}
