package com.edencoding.controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchBarTableViewController implements Initializable {

    @FXML
    private TableView<Order> exampleTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, String> stateColumn;
    @FXML
    private TableColumn<Order, String> cityColumn;
    @FXML
    private TextField searchBox;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        List<Order> orders = getOrders();
        FilteredList<Order> filteredData = new FilteredList<>(FXCollections.observableList(orders));

        exampleTable.setItems(filteredData);
        exampleTable.setRowFactory(tableView -> getOrderTableRow(filteredData));

        searchBox.textProperty().addListener((observable, oldValue, newValue) ->
                exampleTable.setItems(filterList(orders, newValue.toLowerCase())));
    }

    private static final String NEW_YORK = "New York";

    private List<Order> getOrders() {
        return List.of(
                new Order(1, NEW_YORK, NEW_YORK), //8,323,340
                new Order(2, "Colorado", "Denver"),
                new Order(3, "Missouri", "Kansas City"),
                new Order(4, "Nebraska", "Custer"),
                new Order(5, "Iowa", "Black Hawk"),
                new Order(6, "Nevada", "Las Vegas"),
                new Order(7, "California", "San Diego"), //1.54m
                new Order(8, "Illinois", "Chicago"), //2.2m
                new Order(9, "Massachusetts", "Boston"), //4.3m
                new Order(10, "Vermont", "Montpellier"),
                new Order(11, "Alberta", "Revelstoke"),
                new Order(12, "Manitoba", "Winnipeg"),
                new Order(13, "British Colombia", "Terrace"));
    }

    private TableRow<Order> getOrderTableRow(FilteredList<Order> filteredData) {
        TableRow<Order> row = new TableRow<>();
        row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"), false);
        row.itemProperty().addListener((obs, oldOrder, newOrder) -> changeRowAppearance(filteredData, row, newOrder));
        return row;
    }

    private void changeRowAppearance(FilteredList<Order> filteredData, TableRow<Order> row, Order newOrder) {
        boolean assignClass = filteredData.contains(newOrder) &&
                (newOrder.getCity().equals(NEW_YORK) ||
                        newOrder.getCity().equals("Boston"));

        row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"), assignClass);
    }

    private ObservableList<Order> filterList(List<Order> list, String searchText) {
        List<Order> filteredList = list.stream()
                .filter(order -> searchFindsOrder(order, searchText))
                .collect(Collectors.toList());

        return FXCollections.observableList(filteredList);
    }

    private boolean searchFindsOrder(Order order, String searchText) {
        return (order.getCity().toLowerCase().contains(searchText)) ||
                (order.getState().toLowerCase().contains(searchText)) ||
                Integer.toString(order.getId()).equals(searchText);
    }


    @FXML
    private void handleClearSearchText(ActionEvent event) {
        searchBox.setText("");
        event.consume();
    }

    @FXML
    private void handleExitButtonClicked(ActionEvent event) {
        Platform.exit();
        event.consume();
    }

    @FXML
    private void handleGitButtonClicked(ActionEvent event) {
        new Application() {
            @Override
            public void start(Stage stage) {
                //used top open browser
            }
        }.getHostServices().showDocument("https://github.com/edencoding/javafx-ui/");
        event.consume();
    }

    public static class Order {
        private final IntegerProperty id;
        private final StringProperty state;
        private final StringProperty city;

        public Order(Integer id, String state, String city) {
            this.id = new SimpleIntegerProperty(id);
            this.state = new SimpleStringProperty(state);
            this.city = new SimpleStringProperty(city);
        }

        public int getId() {
            return id.get();
        }

        public IntegerProperty idProperty() {
            return id;
        }

        public String getState() {
            return state.get();
        }

        public StringProperty stateProperty() {
            return state;
        }

        public String getCity() {
            return city.get();
        }

        public StringProperty cityProperty() {
            return city;
        }
    }
}
