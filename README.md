# Java fx examples

---

This is a JavaFX examples project. It's intended to get familiar with the different components of javaFX.

---

## Tests

Some tests make use of TestFx

To let TestFX manage Mouse and Keyboard on macOS, you have to authorized it.

* Go to System Settings > Privacy & Security > Accessibility
    * If you want to execute the tests from your IDE, check your IDE in the list
    * If you want to execute the tests from maven/gradle in your terminal, check the Terminal
    * If you don't find the Terminal or your IDE, click on + and add it to the list
        * Terminal should be there /Applications/Utilities/Terminal.app

See also [Fix with documentation](https://github.com/bjalon/TestFX/commit/13cdd07e5e6091a5f067edc1a80b8b126de3fd0b)

### Running the tests

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

### Clearing local mvn repo

`mvn dependency:purge-local-repository -DactTransitively=false -DreResolve=false`

Also see: [Clearing the Maven Cache](https://www.baeldung.com/maven-clear-cache)

---

### Source

- [Molecule hzt.sample app](https://docs.oracle.com/javafx/8/3d_graphics/sampleapp.htm#BABCGEAJ)
- [Picking3D](https://docs.oracle.com/javase/8/javafx/graphics-tutorial/picking.htm)

---
