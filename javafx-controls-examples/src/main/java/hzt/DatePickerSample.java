package hzt;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Clock;
import java.time.format.DateTimeFormatter;

public class DatePickerSample extends Application {

    private static final Logger LOGGER = LogManager.getLogger(DatePickerSample.class);

    @Override
    public void start(final Stage stage) throws Exception {
        LOGGER.info("Starting {}...", DatePickerSample.class.getSimpleName());
        Label label = new Label();
        DatePicker datePicker = new DatePicker();
        datePicker.setOnAction(actionEvent -> displayPickedDate(label, datePicker));

        Label label1 = new Label();
        DateTimePicker dateTimePicker = new DateTimePicker(Clock.systemDefaultZone());
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

    private static void displayPickedDateTime(Label label1, DateTimePicker dateTimePicker) {
        label1.setText(dateTimePicker.getDateTimeValue().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    private static void displayPickedDate(Label label, DatePicker datePicker) {
        label.setText(datePicker.getValue().toString());
        LOGGER.info(() -> datePicker.getValue().toString());
    }
}
