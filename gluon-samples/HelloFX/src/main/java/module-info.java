module hellofx {

    requires javafx.controls;
    requires javafx.graphics;

    opens hellofx to javafx.graphics;

}
