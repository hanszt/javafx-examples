package org.openjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ComboAppClass extends Application {

    private static final Pair<String, String> EMPTY_PAIR = new Pair<>("", "");

    private final ComboBox<Pair<String, String>> account = new ComboBox<>();

    private static ListCell<Pair<String, String>> buildListCell(ListView<Pair<String, String>> listView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Pair<String, String> item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getKey());
            }
        };
    }

    @Override
    public void start(Stage primaryStage) {

        Label accountsLabel = new Label("Account:");
        account.setPrefWidth(200);
        Button saveButton = new Button("Save");

        HBox hbox = new HBox(
                accountsLabel,
                account,
                saveButton);
        hbox.setSpacing(10.0D);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(40));

        Scene scene = new Scene(hbox);

        initCombo();

        saveButton.setOnAction((evt) -> {
            if (account.getValue().equals(EMPTY_PAIR)) {
                System.out.println("no save needed; no item selected");
            } else {
                System.out.println("saving " + account.getValue());
            }
        });

        primaryStage.setTitle("CombosApp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initCombo() {

        List<Pair<String, String>> accounts = new ArrayList<>();

        accounts.add(new Pair<>("Auto Expense", "60000"));
        accounts.add(new Pair<>("Interest Expense", "61000"));
        accounts.add(new Pair<>("Office Expense", "62000"));
        accounts.add(new Pair<>("Salaries Expense", "63000"));

        account.getItems().add(EMPTY_PAIR);
        account.getItems().addAll(accounts);
        account.setValue(EMPTY_PAIR);

        Callback<ListView<Pair<String, String>>, ListCell<Pair<String, String>>> factory = ComboAppClass::buildListCell;

        account.setCellFactory(factory);
        account.setButtonCell(factory.call(null));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
