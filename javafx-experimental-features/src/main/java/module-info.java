module Main.app {

    requires java.desktop;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.swing;

    opens shapethreedtest to javafx.graphics;
    opens strechygrid to javafx.graphics;
    opens fx_eight_three_d_features to javafx.graphics;
    opens industrial to javafx.graphics;
    opens dragdrop to javafx.graphics;
}
