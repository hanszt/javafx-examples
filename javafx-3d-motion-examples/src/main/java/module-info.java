module Main.app {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires org.apache.logging.log4j;

    opens hzt to javafx.graphics;

}
