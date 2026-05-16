module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires kotlin.stdlib;
    requires commons.math3;
    requires javafx.utils;

    opens hzt to javafx.graphics;
    opens hzt.marbeling to javafx.graphics;
    opens hzt.implicit_surfaces to javafx.graphics;
}
