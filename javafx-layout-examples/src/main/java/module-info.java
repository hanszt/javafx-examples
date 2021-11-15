module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires org.kordamp.bootstrapfx.core;

    opens hzt to javafx.graphics;
}