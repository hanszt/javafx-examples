module Main.app {

    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires java.logging;
    requires javafx.web;
    requires org.apache.logging.log4j;
    requires javafx.utils;
    requires kotlin.stdlib;

    exports hzt.htmleditorsample;

    opens hzt; // needed for junit and javafx.graphics
    opens hzt.togglebuttonsample to javafx.graphics;
    opens hzt.textfieldsample to javafx.graphics;
    opens hzt.tooltipsample to javafx.graphics;
    opens hzt.titledpanesample to javafx.graphics;
    opens hzt.buttonsample to javafx.graphics;
    opens hzt.radiobuttonsample to javafx.graphics;
    opens hzt.scrollpanesample to javafx.graphics;
    opens hzt.htmleditorsample to javafx.graphics;
    opens hzt.hyperlinksample to javafx.graphics;
    opens hzt.hyperlinkwebviewsample to javafx.graphics;
    opens hzt.treeviewsample to javafx.graphics;
    opens hzt.comboboxsample to javafx.graphics;
    opens hzt.colorpickersample to javafx.graphics;
    opens hzt.checkboxsample to javafx.graphics;
    opens hzt.choiceboxsample to javafx.graphics;
    opens hzt.passwordfieldsample to javafx.graphics;
    opens hzt.progresssample to javafx.graphics;
    opens hzt.scrollbarsample to javafx.graphics;
    opens hzt.filechoosersample to javafx.graphics;
    opens hzt.tableviewsample to javafx.graphics, javafx.base;
    opens hzt.listviewsample to javafx.graphics;
    opens hzt.labelsample to javafx.graphics;
    opens hzt.slidersample to javafx.graphics;
    opens hzt.fxwebbrowser to javafx.graphics, javafx.fxml;
}
