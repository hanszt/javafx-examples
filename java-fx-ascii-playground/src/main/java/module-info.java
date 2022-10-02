module java.fx.ascii.playground {

    requires javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.utils;

    opens hzt.animations to javafx.graphics;
}
