module styled.tableview {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;

    opens com.edencoding to javafx.fxml;
    opens com.edencoding.controllers to javafx.fxml;

    exports com.edencoding;
}
