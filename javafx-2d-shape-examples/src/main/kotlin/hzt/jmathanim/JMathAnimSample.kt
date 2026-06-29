package hzt.jmathanim

import com.jmathanim.core.Scene2D
import com.jmathanim.mathobjects.shapes.Shape


/**
 * See https://codeberg.org/davidgutierrezrubio/jmathanim
 *
 * Examples: https://davidgutierrezrubio.codeberg.page/jmathanim/Gallery/
 *
 * See also [JMathAnim: Create Mathematical Animations in Java Like 3Blue1Brown | JavaFX In Action #27](https://www.youtube.com/watch?app=desktop&v=pb54youm6AM&start=5))
 */
class JMathAnimSample {

    class FirstScene : Scene2D() {
        override fun runSketch() {
            config.parseFile("#preview.xml")
            config.parseFile("#solarized_dark.xml")
            val sq: Shape = Shape.square().center()
            play.showCreation(2.0, sq)
            play.rotate(2.0, 30 * DEGREES, sq)
            scene.waitSeconds(2.0)
        }

        companion object {
            @JvmStatic
            fun main(args: Array<String>) {
                FirstScene().execute()
            }
        }
    }
}
