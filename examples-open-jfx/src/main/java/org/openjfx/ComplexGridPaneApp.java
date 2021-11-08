package org.openjfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ComplexGridPaneApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox vbox = new VBox();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(4);
        gridPane.setVgap(10);

        VBox.setVgrow(gridPane, Priority.ALWAYS);

        Label tittleLabel = new Label("Support Ticket");

        Label lblEmail = new Label("Email");
        TextField emailTextField = new TextField();

        Label lblContract = new Label("Contract");
        TextField tfContract = new TextField();

        Label priorityLabel = new Label("Priority");
        ComboBox<String> priorityCombobox = new ComboBox<>(FXCollections.observableArrayList("Medium", "High", "Low"));

        Label severityLabel = new Label("Severity");
        ComboBox<String> severityCombobox = new ComboBox<>(FXCollections.observableArrayList("Blocker", "Workaround", "N/A"));

        Label categoryLabel = new Label("Category");
        ComboBox<String> categoryCombobox = new ComboBox<>(FXCollections.observableArrayList("Bug", "Feature"));

        Label lblProblem = new Label("Problem");
        TextField problemTextField = new TextField();

        Label lblDescription = new Label("Description");
        TextArea descriptionTextArea = new TextArea();

        gridPane.add(tittleLabel, 1, 0);  // empty item at 0,0

        gridPane.add(lblEmail, 0, 1);
        gridPane.add(emailTextField, 1, 1);
        gridPane.add(lblContract, 4, 1);
        gridPane.add(tfContract, 5, 1);

        gridPane.add(priorityLabel, 0, 2);
        gridPane.add(priorityCombobox, 1, 2);
        gridPane.add(severityLabel, 2, 2);
        gridPane.add(severityCombobox, 3, 2);
        gridPane.add(categoryLabel, 4, 2);
        gridPane.add(categoryCombobox, 5, 2);

        gridPane.add(lblProblem, 0, 3);
        gridPane.add(problemTextField, 1, 3);
        gridPane.add(lblDescription, 0, 4);
        gridPane.add(descriptionTextArea, 1, 4);

        GridPane.setColumnSpan(tittleLabel, 5);
        GridPane.setColumnSpan(emailTextField, 3);
        GridPane.setColumnSpan(problemTextField, 5);
        GridPane.setColumnSpan(descriptionTextArea, 5);

        Separator sep = new Separator(); // hr

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(10));

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        ButtonBar.setButtonData(saveButton, ButtonBar.ButtonData.OK_DONE);
        ButtonBar.setButtonData(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);

        buttonBar.getButtons().addAll(saveButton, cancelButton);

        vbox.getChildren().addAll(gridPane, sep, buttonBar);

        Scene scene = new Scene(vbox);

        primaryStage.setTitle("Grid Pane App");
        primaryStage.setScene(scene);
        primaryStage.setWidth(736);
        primaryStage.setHeight(414);
        primaryStage.show();
    }

}
