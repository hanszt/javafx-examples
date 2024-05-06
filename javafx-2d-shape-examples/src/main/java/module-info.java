module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;

    opens hzt to javafx.graphics;
    opens hzt.marbeling to javafx.graphics;
}
