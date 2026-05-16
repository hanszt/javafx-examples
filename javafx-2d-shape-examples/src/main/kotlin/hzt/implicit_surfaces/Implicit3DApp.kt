package hzt.implicit_surfaces

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.*
import javafx.scene.paint.Color
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape.MeshView
import javafx.scene.shape.TriangleMesh
import javafx.scene.transform.Rotate
import javafx.stage.Stage
import kotlin.math.cos
import kotlin.math.sin

class Implicit3DApp : Application() {

    override fun start(primaryStage: Stage) {
        // 1. Create the 3D Mesh representing our surface
        val meshView = MeshView(createImplicitMesh())
        
        // Style the surface with a shiny material
        val material = PhongMaterial().apply {
            diffuseColor = Color.CYAN
            specularColor = Color.WHITE
            specularPower = 20.0
        }
        meshView.material = material

        // Center and scale the mesh so it's visible
        meshView.scaleX = 150.0
        meshView.scaleY = 150.0
        meshView.scaleZ = 150.0

        // 2. Set up the 3D Scene Group
        val group = Group(meshView)
        
        // Add subtle continuous rotation so we can appreciate the 3D depth
        val rotateX = Rotate(30.0, Rotate.X_AXIS)
        val rotateY = Rotate(45.0, Rotate.Y_AXIS)
        group.transforms.addAll(rotateX, rotateY)

        // 3. Add Lighting
        val pointLight = PointLight(Color.WHITE).apply {
            translateX = 200.0
            translateY = -300.0
            translateZ = -400.0
        }
        val ambientLight = AmbientLight(Color.rgb(40, 40, 40))
        group.children.addAll(pointLight, ambientLight)

        // 4. Setup Camera
        val camera = PerspectiveCamera(true).apply {
            nearClip = 0.1
            farClip = 1000.0
            translateZ = -800.0 // Move back to look at the center
        }

        // 5. Create Scene and Window
        val scene = Scene(group, 800.0, 600.0, true, SceneAntialiasing.BALANCED).apply {
            fill = Color.rgb(15, 15, 25) // Dark space background
            this.camera = camera
        }

        primaryStage.title = "3D Implicit Mesh Renderer"
        primaryStage.scene = scene
        primaryStage.show()

        // Simple animation loop to rotate the 3D object
        object : AnimationTimer() {
            override fun handle(now: Long) {
                rotateY.angle += 0.5
            }

        }.start()
    }

    private fun createImplicitMesh(): TriangleMesh {
        val mesh = TriangleMesh()
        
        // Resolution parameters
        val uDivs = 60
        val vDivs = 60
        
        // Constants for a standard Torus implicit surface equation
        val outerR = 1.0 // Major radius
        val innerR = 0.3 // Minor radius

        // 1. Generate Vertices (Points in 3D Space)
        // We sample a parametric representation of the implicit algebraic torus surface
        for (i in 0..uDivs) {
            val u = i * 2.0 * Math.PI / uDivs
            for (j in 0..vDivs) {
                val v = j * 2.0 * Math.PI / vDivs

                // Parametric translation of the implicit algebraic equation: 
                // (x² + y² + z² + R² - r²)² - 4R²(x² + y²) = 0
                val x = (outerR + innerR * cos(v)) * cos(u)
                val y = (outerR + innerR * cos(v)) * sin(u)
                val z = innerR * sin(v)

                mesh.points.addAll(x.toFloat(), y.toFloat(), z.toFloat())
            }
        }

        // 2. Generate Dummy Texture Coordinates (Required by JavaFX TriangleMesh)
        mesh.texCoords.addAll(0f, 0f)

        // 3. Generate Faces (Triangles connecting the points)
        for (i in 0 until uDivs) {
            for (j in 0 until vDivs) {
                // Get indices of a 4-point grid cell
                val p0 = i * (vDivs + 1) + j
                val p1 = p0 + 1
                val p2 = (i + 1) * (vDivs + 1) + j
                val p3 = p2 + 1

                // Construct two triangles for every grid rectangle
                // Format: Point1, TexCoord1, Point2, TexCoord2, Point3, TexCoord3
                mesh.faces.addAll(p0, 0, p2, 0, p1, 0)
                mesh.faces.addAll(p1, 0, p2, 0, p3, 0)
            }
        }

        return mesh
    }
}

fun main() {
    Application.launch(Implicit3DApp::class.java)
}