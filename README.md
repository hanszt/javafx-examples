# Java fx examples

---

This is a JavaFX examples package. It's intended to get familiar with the different components of javaFX.

---

## Running tests

To run all the tests in this project simply run `mvn clean test`

---

## Optional Java Fx Setup

When your project runs on the classpath instead of the module path, add the following in the run configuration

- Go to File -->  Settings --> Appearance and Behaviour --> Path Variables
- Add a new line variable by clicking the + sign and name it `PATH_TO_FX` and browse to the lib folder of the
  JavaFX SDK to set its value, and click apply.
- Go to Run --> Edit Configurations
- Type the following in the VM Options section: `--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml`
- Click Apply and Run the application. It should work now.

source: [openjfx Intellij](https://openjfx.io/openjfx-docs/#IDE-Intellij)

---

### Source

- [Molecule hzt.sample app](https://docs.oracle.com/javafx/8/3d_graphics/sampleapp.htm#BABCGEAJ)
- [Picking3D](https://docs.oracle.com/javase/8/javafx/graphics-tutorial/picking.htm)

---
