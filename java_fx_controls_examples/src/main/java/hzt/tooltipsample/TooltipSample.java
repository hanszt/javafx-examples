package hzt.tooltipsample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class TooltipSample extends Application {

    private static final String[] rooms = {
            "Accommodation (BB)",
            "Half Board",
            "Late Check-out",
            "Extra Bed"
    };

    private static final Integer[] rates = {100, 20, 10, 30};

    final CheckBox[] cbs = new CheckBox[rooms.length];
    final Label total = new Label("Total: $0");
    Integer sum = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Tooltip Sample");
        stage.setWidth(300);
        stage.setHeight(150);

        total.setFont(new Font("Arial", 20));

        for (int i = 0; i < rooms.length; i++) {
            final CheckBox checkBox = cbs[i] = new CheckBox(rooms[i]);
            final Integer rate = rates[i];
            final Tooltip tooltip = new Tooltip("$" + rates[i].toString());
            tooltip.setFont(new Font("Arial", 16));
            checkBox.setTooltip(tooltip);
            checkBox.selectedProperty().addListener((ov, oldVal, newVal) -> updateSum(checkBox, rate));
        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(cbs);
        vbox.setSpacing(5);
        HBox root = new HBox();
        root.getChildren().add(vbox);
        root.getChildren().add(total);
        root.setSpacing(40);
        root.setPadding(new Insets(20, 10, 10, 20));

        ((Group) scene.getRoot()).getChildren().add(root);

        stage.setScene(scene);
        stage.show();
    }

    private void updateSum(CheckBox cb, Integer rate) {
        sum = cb.isSelected() ? sum + rate : sum - rate;
        total.setText("Total: $" + sum);
    }
}