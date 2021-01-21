package hzt.listviewsample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ListViewComboBoxSample extends Application {

    private static final ObservableList<String> NAMES = FXCollections.observableArrayList();
    private static final ObservableList<String> DATA = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("List View Sample");

        final ListView<String> listView = new ListView<>(DATA);
        listView.setPrefSize(200, 250);
        listView.setEditable(true);

        NAMES.addAll(
                "Adam", "Alex", "Alfred", "Albert",
                "Brenda", "Connie", "Derek", "Donny",
                "Lynne", "Myrtle", "Rose", "Rudolph",
                "Tony", "Trudy", "Williams", "Zach"
        );

        for (int i = 0; i < 18; i++) {
            DATA.add("Anonymous");
        }

        listView.setItems(DATA);
        listView.setCellFactory(ComboBoxListCell.forListView(NAMES));

        StackPane root = new StackPane();
        root.getChildren().add(listView);
        primaryStage.setScene(new Scene(root, 200, 250));
        primaryStage.show();
    }
}