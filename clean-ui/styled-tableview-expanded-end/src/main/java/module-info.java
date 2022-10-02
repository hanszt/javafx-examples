module styled.tableview.expanded {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.utils;

    opens com.edencoding;
    opens com.edencoding.controllers to javafx.fxml;

    exports com.edencoding;
}
