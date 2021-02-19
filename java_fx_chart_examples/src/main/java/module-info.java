module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires com.google.common;

    opens hzt to javafx.graphics;

}
