module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires org.jetbrains.annotations;
    requires com.google.common;

    opens hzt to javafx.graphics;
    opens hzt.fxml.launcher to javafx.graphics;
    opens hzt.fxml.controller to javafx.fxml;

}
