module java.fx.bindings {

    requires javafx.controls;
    requires javafx.graphics;

    exports hzt.bindingsapp to javafx.graphics;
    opens hzt.bindingsapp to javafx.base;
}
