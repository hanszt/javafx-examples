package hzt;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Comparator;
import java.util.Objects;

public class MultiListView<T> extends HBox {

    private static final int DEFAULT_SPACING = 10;
    private static final int DEFAULT_FONT_SIZE = 16;

    private final BooleanProperty sorting = new SimpleBooleanProperty(false);

    private final Label titleLeftListViewLabel = new Label();
    private final ListView<T> leftListView = new ListView<>();

    private final Button leftToRightButton = new Button("->");
    private final Button rightToLeftButton = new Button("<-");

    private final Label titleRightListViewLabel = new Label();
    private final ListView<T> rightListView = new ListView<>();

    private Comparator<T> comparator = Comparator.comparing(Objects::toString);

    public MultiListView() {
        this("Left listview", "right listview");
    }

    public MultiListView(String titleLeftListview, String titleRightListview) {
        this.titleLeftListViewLabel.setText(titleLeftListview);
        this.titleRightListViewLabel.setText(titleRightListview);

        VBox leftListviewVbox = new VBox(titleLeftListViewLabel, leftListView);
        leftListviewVbox.setSpacing(DEFAULT_SPACING);
        VBox buttonVBox = new VBox(leftToRightButton, rightToLeftButton);
        buttonVBox.setSpacing(DEFAULT_SPACING);
        buttonVBox.setPadding(new Insets(50, 10, 10, 10));
        VBox rightListviewVbox = new VBox(titleRightListViewLabel, rightListView);
        rightListviewVbox.setSpacing(DEFAULT_SPACING);

        addListenerToListview(leftListView, leftToRightButton);
        addListenerToListview(rightListView, rightToLeftButton);
        configureButton(leftToRightButton, leftListView, rightListView);
        configureButton(rightToLeftButton, rightListView, leftListView);
        configureTittleLabels();
        this.getChildren().addAll(leftListviewVbox, buttonVBox, rightListviewVbox);
    }

    private void configureTittleLabels() {
        titleLeftListViewLabel.setFont(Font.font(DEFAULT_FONT_SIZE));
        titleRightListViewLabel.setFont(Font.font(DEFAULT_FONT_SIZE));
    }

    private void addListenerToListview(ListView<T> listView, Button button) {
        ObservableList<T> selectedItems = listView.getSelectionModel().getSelectedItems();
        selectedItems.addListener((ListChangeListener<T>) change ->
                button.setDisable(selectedItems.isEmpty()));
    }

    private void configureButton(Button button, ListView<T> from, ListView<T> to) {
        button.setOnAction(e -> moveItems(from, to));
        button.setDisable(from.getItems().isEmpty());
        button.setMinWidth(30);
        button.setPrefHeight(200);
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        rightListView.getSelectionModel().setSelectionMode(selectionMode);
        leftListView.getSelectionModel().setSelectionMode(selectionMode);
    }

    public void addRightListviewListener(ListChangeListener<T> addedListViewListener) {
        rightListView.getItems().addListener(addedListViewListener);
    }

    private void moveItems(ListView<T> from, ListView<T> to) {
        to.getSelectionModel().clearSelection();
        ObservableList<T> selectedItems = from.getSelectionModel().getSelectedItems();
        to.getItems().addAll(selectedItems);
        if (sorting.get()) to.getItems().sort(comparator);
        selectedItems.forEach(i -> to.getSelectionModel().select(i));
        from.getItems().removeAll(selectedItems);
//        from.getSelectionModel().clearSelection();
    }

    public void addRightListView(T item) {
        rightListView.getItems().add(item);
    }

    public void addLeftListView(T item) {
        leftListView.getItems().add(item);
    }

    public void setListViewsTitles(String titleLeftListView, String titleRightListView) {
        titleLeftListViewLabel.setText(titleLeftListView);
        titleRightListViewLabel.setText(titleRightListView);
    }

    public void setComparatorListViews(Comparator<T> comparator, boolean sorting) {
        this.sorting.set(sorting);
        setComparatorListViews(comparator);
    }

    public void setComparatorListViews(Comparator<T> comparator) {
        this.comparator = comparator;
        if (sorting.get()) {
            rightListView.getItems().sort(comparator);
            leftListView.getItems().sort(comparator);
        }
    }

    @Override
    public String toString() {
        return "SwitchListView{" +
                "titleLeftListViewLabel=" + titleLeftListViewLabel +
                ", titleRightListViewLabel=" + titleRightListViewLabel +
                '}';
    }
}
