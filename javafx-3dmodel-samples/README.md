# javafx3d-samples
A collection of minimal hzt.sample code for JavaFX 3D

## LoadModelApp

Remember to add the Obj importer library from `lib/` (to my knowledge, no Maven version available).

Obj importer used under license: Copyright (c) 2013 August Lammersdorf, InteractiveMesh e.K..
Please see [LICENSE-ObjModelImporterJFX.txt](LICENSE-ObjModelImporterJFX.txt) for more details.

Scooter model used under license: CCA 3.0. Please see [LICENSE-Scooter.txt](LICENSE-Scooter.txt) for more details.

# Setup
Before you run the program, make sure you modify the following in Intellij:
- Go to File -->  hzt.Settings --> Appearance and Behaviour --> Path Variables
- Add a new path variable by clicking on the + sign and name it PATH_TO_FX and browse to the lib folder of the JavaFX SDK to set its value, and click apply.
- Go to Run --> Edit Configurations
- Type the following in the VM Options section: `--module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml`
- Click Apply and Run the application. It should work now.

[Source](https://openjfx.io/openjfx-docs/#IDE-Intellij)

## Sources
- [Interactive mesh](http://www.interactivemesh.com/)
- [Toroid class](http://www.lagers.org.uk/javafx/toroidclass.html)
