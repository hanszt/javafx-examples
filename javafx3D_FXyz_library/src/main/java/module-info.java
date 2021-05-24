module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires java.logging;
    requires java.desktop;
    requires javafx.swing;

    opens org.fxyz.tests to javafx.graphics;

}
