module styled.tableview {

    requires javafx.controls;
    requires javafx.fxml;
    requires slf4j.api;

    opens com.edencoding to javafx.fxml;
    opens com.edencoding.controllers to javafx.fxml;

    exports com.edencoding;
}
