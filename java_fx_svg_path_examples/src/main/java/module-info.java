module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires java.desktop;
    requires batik.transcoder;
    requires javafx.swing;

    opens hzt to javafx.graphics;
}
