package hzt.comboboxsample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

public class ComboBoxSample extends Application {

    private static final String LOWEST = "Lowest";
    private static final String LOW = "Low";
    private static final String NORMAL = "Normal";
    private static final String HIGH = "High";
    private static final String HIGHEST = "Highest";

    private final Button sendButton = new Button("Send");
    private final Label notification = new Label();
    private final TextField subject = new TextField("");
    private final TextArea text = new TextArea("");

    private String address = " ";

    @Override
    public void start(Stage stage) {
        stage.setTitle("ComboBoxSample");
        Scene scene = new Scene(new Group(), 500, 300);

        final ComboBox<String> emailComboBox = buildEmailComboBox();

        final ComboBox<String> priorityComboBox = buildPriorityBox();

        sendButton.setOnAction(e -> sendButtonAction(emailComboBox, priorityComboBox));

        GridPane grid = buildGrid(emailComboBox, priorityComboBox);

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        stage.setScene(scene);
        stage.show();

    }

    @NotNull
    private ComboBox<String> buildEmailComboBox() {
        final ComboBox<String> emailComboBox = new ComboBox<>();
        emailComboBox.getItems().addAll(
                "jacob.smith@example.com",
                "isabella.johnson@example.com",
                "ethan.williams@example.com",
                "emma.jones@example.com",
                "michael.brown@example.com"
        );
        emailComboBox.setPromptText("Email address");
        emailComboBox.setEditable(true);
        emailComboBox.valueProperty().addListener((oldVal, curVal, newVal) -> address = newVal);
        return emailComboBox;
    }

    @NotNull
    private GridPane buildGrid(ComboBox<String> emailComboBox, ComboBox<String> priorityComboBox) {
        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("To: "), 0, 0);
        grid.add(emailComboBox, 1, 0);
        grid.add(new Label("Priority: "), 2, 0);
        grid.add(priorityComboBox, 3, 0);
        grid.add(new Label("Subject: "), 0, 1);
        grid.add(subject, 1, 1, 3, 1);
        grid.add(text, 0, 2, 4, 1);
        grid.add(sendButton, 0, 3);
        grid.add(notification, 1, 3, 3, 1);
        return grid;
    }

    @NotNull
    private ComboBox<String> buildPriorityBox() {
        final ComboBox<String> priorityComboBox = new ComboBox<>();
        priorityComboBox.getItems().addAll(HIGHEST,HIGH, NORMAL, LOW, LOWEST);
        priorityComboBox.setValue(NORMAL);
        priorityComboBox.setCellFactory(getCallback());
        return priorityComboBox;
    }

    private void sendButtonAction(ComboBox<String> emailComboBox, ComboBox<String> priorityComboBox) {
        if (emailComboBox.getValue() != null && !emailComboBox.getValue().isEmpty()) {
            notification.setText("Your message was successfully sent" + " to " + address);
            emailComboBox.setValue(null);
            if (priorityComboBox.getValue() != null && !priorityComboBox.getValue().isEmpty()) {
                priorityComboBox.setValue(NORMAL);
            }
            subject.clear();
            text.clear();
        } else {
            String message = "You have not selected a recipient!";
            notification.setText(message);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No recipient selected");
            alert.setHeaderText("No recipient selected");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    @NotNull
    private Callback<ListView<String>, ListCell<String>> getCallback() {
        return new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return getListCell();
            }

            @NotNull
            private ListCell<String> getListCell() {
                ListCell<String> listCell = new ListCell<>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            if (item.contains("High")) setTextFill(Color.RED);
                            else if (item.contains("Low")) setTextFill(Color.GREEN);
                            else setTextFill(Color.BLACK);
                        } else setText(null);
                    }
                };
                listCell.setPrefWidth(100);
                return listCell;
            }
        };
    }

}
