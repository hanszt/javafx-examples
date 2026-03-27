module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.google.common;
    requires kotlin.stdlib;
    requires javafx.utils;
    requires org.jetbrains.annotations;
    requires org.slf4j;

    opens hzt to javafx.graphics;
    opens hzt.fxml.launcher to javafx.graphics;
    opens hzt.gaussian_chart to javafx.graphics;

    opens hzt.fxml.controller to javafx.fxml;

}
