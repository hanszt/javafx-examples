open module javafx.utils {

    requires javafx.controls;
    requires javafx.graphics;
    requires org.apache.logging.log4j;
    requires kotlin.stdlib;

    exports utils;
    exports fxutils;
    exports interfaces;
    exports services;
    exports fx_customs;
}
