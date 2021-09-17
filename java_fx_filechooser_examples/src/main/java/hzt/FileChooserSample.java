package hzt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.*;

public final class FileChooserSample extends Application {

    private final Desktop desktop = Desktop.getDesktop();

    @Override
    public void start(final Stage stage) {
        stage.setTitle("File Chooser Sample");

        final FileChooser fileChooser = new FileChooser();

        final Button openButton = new Button("Open a Picture...");
        final Button openMultipleButton = new Button("Open Pictures...");
        final Button directoryButton = new Button("Choose directory...");
        final Button fileButton = new Button("Choose file...");

        openButton.setOnAction(e -> showDialogWindow(stage, fileChooser));
        openMultipleButton.setOnAction(e -> showDialogMultipleItems(stage, fileChooser));
        directoryButton.setOnAction(e -> chooseDirectory(stage));
        fileButton.setOnAction(e -> chooseFile(stage, fileChooser));

        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openButton, 0, 0);
        GridPane.setConstraints(openMultipleButton, 1, 0);
        GridPane.setConstraints(directoryButton, 2, 0);
        GridPane.setConstraints(fileButton, 3, 0);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, openMultipleButton, directoryButton, fileButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }

    private void chooseDirectory(Stage stage) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            String absolutePath = selectedDirectory.getAbsolutePath();
            out.println(absolutePath);
        }
    }


    private void chooseFile(Stage stage, FileChooser fileChooser) {
        final File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            String absolutePath = selectedFile.getAbsolutePath();
            out.println(absolutePath);
        }
    }

    private void showDialogWindow(Stage stage, FileChooser fileChooser) {
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            openFile(file);
        }
    }

    private void showDialogMultipleItems(Stage stage, FileChooser fileChooser) {
        configureFileChooser(fileChooser);
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) {
                openFile(file);
            }
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser){
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(new File(getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
