package hzt

import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import kotlin.math.pow

/**
 * Generates a nested square grid image that grows denser by a power of 2 toward the center.
 * Returns a standalone JavaFX Image.
 */
fun createDenserGridImage(width: Double, height: Double, maxDepth: Int = 5, linesPerSide: Int = 4): Image {
    // 1. Create a temporary off-screen canvas to hold the vector drawing commands
    val canvas = Canvas(width, height)
    val gc = canvas.graphicsContext2D

    // Clear the background to match the black canvas look
    gc.fill = Color.BLACK
    gc.fillRect(0.0, 0.0, width, height)

    val centerX = width / 2.0
    val centerY = height / 2.0
    val halfSize = minOf(width, height) / 2.0

    // Loop through each depth layer (0 = outermost blue grid, maxDepth = green core)
    for (depth in 0..maxDepth) {

        // Scale decreases by powers of 2: 1.0, 0.5, 0.25, 0.125...
        val scale = 0.5.pow(depth)
        val currentRadius = halfSize * scale

        // Linear interpolation for color transitioning from Cyan/Blue to Bright Green
        val ratio = depth.toDouble() / maxDepth
        gc.stroke = Color.color(
            0.0,                    // Red
            0.3 + (0.7 * ratio),    // Green boosts as it gets deeper
            1.0 - (0.8 * ratio),    // Blue drops off towards the center
            0.8                     // Opacity
        )

        // Set thinner line widths for denser deep layers to prevent blending clusters
        gc.lineWidth = maxOf(0.5, 1.5 - (depth * 0.2))

        // Draw the vertical and horizontal cross lines for the current level
        for (i in -linesPerSide..linesPerSide) {
            val offsetFactor = i.toDouble() / linesPerSide
            val offset = currentRadius * offsetFactor

            // Boundary endpoints for the current grid box frame
            val minCoord = -currentRadius
            val maxCoord = currentRadius

            // 1. Vertical Grid Lines
            val xPos = centerX + offset
            val yStart = centerY + minCoord
            val yEnd = centerY + maxCoord
            gc.strokeLine(xPos, yStart, xPos, yEnd)

            // 2. Horizontal Grid Lines
            val yPos = centerY + offset
            val xStart = centerX + minCoord
            val xEnd = centerX + maxCoord
            gc.strokeLine(xStart, yPos, xEnd, yPos)
        }
    }
    // 2. Configure snapshot settings to ensure pixel-perfect conversion without anti-aliasing shifts
    val params = SnapshotParameters().apply {
        fill = Color.BLACK
    }
    // 3. Render the vector scene graph context directly into a new image object
    val destinationImage = WritableImage(width.toInt(), height.toInt())
    canvas.snapshot(params, destinationImage)

    return destinationImage
}
