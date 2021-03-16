package com.edencoding.controllers;

import com.edencoding.models.Courier;
import com.edencoding.models.Driver;
import com.edencoding.models.Order;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class MainViewController implements Initializable {

    @FXML
    private TableView<Order> exampleTable;
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    @FXML
    private TableColumn<Order, Boolean> shipTypeColumn;
    @FXML
    private TableColumn<Order, String> courierColumn;
    @FXML
    private TableColumn<Order, String> costColumn;
    @FXML
    private TableColumn<Order, Integer> driverColumn;

    private final DecimalFormat currency = new DecimalFormat("$##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        shipTypeColumn.setCellValueFactory(cellData -> cellData.getValue().priorityShippingProperty());
        driverColumn.setCellValueFactory(cellData -> cellData.getValue().driverIdProperty().asObject());

        costColumn.setCellValueFactory(cellData -> {
            String formattedCost = currency.format(calculateItemCost(cellData.getValue()));
            return new SimpleStringProperty(formattedCost);
        });

        courierColumn.setCellValueFactory(cellData -> {
            Courier courier = getCourier(cellData.getValue().getCourierId());
            return courier.nameProperty();
        });

        shipTypeColumn.setCellFactory(col -> {
            TableCell<Order, Boolean> cell = new TableCell<>();
            cell.itemProperty().addListener((obs, old, newVal) -> {
                if (newVal != null) {
                    Node centreBox = createPriorityGraphic(newVal);
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(centreBox));
                }
            });
            return cell;
        });

        driverColumn.setCellFactory(col -> {
            TableCell<Order, Integer> cell = new TableCell<>();

            cell.itemProperty().addListener((observableValue, o, newValue) -> {
                if (newValue != null) {
                    Node graphic = createDriverGraphic(newValue);
                    cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(graphic));
                }
            });
            return cell;
        });

        List<Order> orders = exampleOrders();
        exampleTable.setItems(FXCollections.observableList(orders));
    }

    private Node createDriverGraphic(Integer driverID) {
        Driver driver = getDriver(driverID);

        GridPane trackingDetailsHolder = new GridPane();
        trackingDetailsHolder.setHgap(5);
        ImageView driverPicture = new ImageView(new Image(getClass().getResourceAsStream(driver.getPhotoFileName())));
        driverPicture.setPreserveRatio(true);
        driverPicture.setFitHeight(30d);
        GridPane.setRowSpan(driverPicture, 2);
        trackingDetailsHolder.getChildren().add(driverPicture);

        Label driverStatus = new Label();
        driverStatus.textProperty().bind(driver.nameProperty());
        GridPane.setColumnIndex(driverStatus, 1);
        trackingDetailsHolder.getChildren().add(driverStatus);

        HBox rating = new HBox();
        for (int i = 0; i < driver.getRating(); i++) {
            rating.getChildren().add(
                    new ImageView(new Image(getClass().getResourceAsStream("/img/gold_star.png")))
            );
        }

        for (int i = 0; i < (5 - driver.getRating()); i++) {
            rating.getChildren().add(
                    new ImageView(new Image(getClass().getResourceAsStream("/img/black_star.png")))
            );
        }
        GridPane.setConstraints(rating, 1, 1);
        trackingDetailsHolder.getChildren().add(rating);

        return trackingDetailsHolder;
    }

    private Node createPriorityGraphic(boolean isPriority) {
        HBox graphicContainer = new HBox();
        graphicContainer.setAlignment(Pos.CENTER);
        if (isPriority) {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/img/important.png")));
            imageView.setFitHeight(25);
            imageView.setPreserveRatio(true);
            graphicContainer.getChildren().add(imageView);
        }
        return graphicContainer;
    }

    private double calculateItemCost(Order order) {
        double priorityMultiplier = order.isPriorityShipping() ? 1.1d : 1d;
        return order.getWeight() * priorityMultiplier;
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
                //user to open the browser
            }
        }.getHostServices().showDocument("https://github.com/edencoding/javafx-ui/");
        event.consume();
    }

    Driver getDriver(int id) {
        return exampleDrivers().get(id);
    }

    Courier getCourier(int id) {
        return exampleCouriers().get(id);
    }

    Map<Integer, Driver> exampleDrivers() {
        return Map.of(
        1, new Driver("Ellen Jowels", 4, "/img/driver1.png"),
        2, new Driver("Eric Northope", 2, "/img/driver2.png"),
        3, new Driver("Stella Gerrard", 3, "/img/driver3.png"),
        4, new Driver("Stanley Horbright", 3, "/img/driver4.png"));
    }

    Map<Integer, Courier> exampleCouriers() {
        return Map.of(
        0, new Courier(0, "FedEx"),
        1, new Courier(1, "ParcelForce"));
    }

    List<Order> exampleOrders() {
        return List.of(
                new Order(1, "New York", "New York", false, 0, 7d, 1),
                new Order(2, "Colorado", "Denver", true, 1, 7d, 2),
                new Order(3, "Missouri", "Kansas City", false, 0, 15d, 3),
                new Order(4, "Nebraska", "Custer", true, 0, 3d, 4));
    }

}
