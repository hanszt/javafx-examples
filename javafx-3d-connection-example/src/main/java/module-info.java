module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;
    requires org.jetbrains.annotations;

    opens hzt to javafx.graphics;

}
