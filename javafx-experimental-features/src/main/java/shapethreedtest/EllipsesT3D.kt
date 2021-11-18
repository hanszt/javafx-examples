/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package shapethreedtest

import javafx.animation.Animation
import javafx.animation.Interpolator
import javafx.animation.RotateTransition
import javafx.application.Application
import javafx.application.ConditionalFeature
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.scene.shape.Ellipse
import javafx.scene.transform.Rotate
import javafx.stage.Stage
import javafx.util.Duration

fun main() = EllipsesT3D().start()

class EllipsesT3D : Application() {

    fun start() = launch()

    override fun start(stage: Stage) {
        val sceneWidth = 640.0
        val sceneHeight = 480.0

        val ellipse = Ellipse(sceneWidth / 2, sceneHeight / 2, 200.0, 100.0).configure(-100.0, Color.BLUE)
        val ellipse2 = Ellipse(sceneWidth / 2, sceneHeight / 2, 200.0, 100.0).apply { fill = Color.GREEN }
        val ellipse3 = Ellipse(sceneWidth / 2, sceneHeight / 2, 200.0, 100.0).configure(100.0, Color.RED)

        val group = Group(ellipse, ellipse2, ellipse3).apply { rotationAxis = Rotate.Y_AXIS }
        val scene = Scene(group, 640.0, 480.0, true).apply {
            fill = Color.BLACK
            camera = PerspectiveCamera()
        }
        stage.apply {
            this.scene = scene
            sizeToScene()
            show()
        }
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            println("*************************************************************")
            println("*    WARNING: common conditional SCENE3D isn't supported    *")
            println("*************************************************************")
        }
        RotateTransition(Duration.seconds(20.0), group).apply {
            toAngle = 360.0
            cycleCount = Animation.INDEFINITE
            interpolator = Interpolator.LINEAR
        }.play()
    }

    private fun Ellipse.configure(translate: Double, color: Color): Ellipse {
        fill = color
        translateX = translate
        translateY = translate
        translateZ = translate
        return this
    }
}
