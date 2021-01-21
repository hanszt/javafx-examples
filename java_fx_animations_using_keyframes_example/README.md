# Animation and effects Samples

## Setup
Before you run the program, make sure you modify the following in Intellij:
- Go to File -->  Settings --> Appearance and Behaviour --> Path Variables
- Add a new line variable by clicking active the + sign and name it PATH_TO_FX and browse to the lib folder of the JavaFX SDK to set its value, and click apply.
- Go to Run --> Edit Configurations
- Type the following in the VM Options section:

        --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml

- Click Apply and Run the application. It should work now.

source: [openjfx-docs IDE Intellij](https://openjfx.io/openjfx-docs/#IDE-Intellij)


## Source
[File chooser tutorial oracle docs](https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm)