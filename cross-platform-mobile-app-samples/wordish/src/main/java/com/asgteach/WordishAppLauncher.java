package com.asgteach;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class WordishAppLauncher extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        new WordishApp(stage);
    }
}
