package hzt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class DatePickerSample extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        Label label = new Label();
        DatePicker datePicker = new DatePicker();
        datePicker.setOnAction(actionEvent -> displayPickedDate(label, datePicker));

        Label label1 = new Label();
        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setOnAction(actionEvent -> displayPickedDateTime(label1, dateTimePicker));

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10,10,10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(datePicker, label,dateTimePicker, label1);
        Scene scene = new Scene(vBox, 400, 400);
        stage.setScene(scene);
        stage.setTitle("PaginationSample");
        stage.show();

    }

    private void displayPickedDateTime(Label label1, DateTimePicker dateTimePicker) {
        label1.setText(dateTimePicker.getDateTimeValue().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    private void displayPickedDate(Label label, DatePicker datePicker) {
        label.setText(datePicker.getValue().toString());
        System.out.println(datePicker.getValue().toString());
    }
}
