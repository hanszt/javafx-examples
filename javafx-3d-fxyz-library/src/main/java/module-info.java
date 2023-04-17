module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires java.logging;
    requires java.desktop;
    requires javafx.swing;
    requires org.apache.logging.log4j;

    opens org.fxyz.tests to javafx.graphics;
    //necessary for testing
    opens org.fxyz.utils;

}
