module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;

    exports hzt.chapt9 to javafx.graphics;
    opens hzt to javafx.graphics;

}
