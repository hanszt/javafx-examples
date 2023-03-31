
package com.asgteach.accelerometer;

import com.gluonhq.attach.util.Constants;
import com.asgteach.accelerometer.views.AppViewManager;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Optional;

public class TiltMazeLauncher {

    public static void main(String[] args) {
        TiltMaze.main(args);
    }

    public static class TiltMaze extends MobileApplication {

        private static final Logger LOGGER = LoggerFactory.getLogger(TiltMaze.class);

        @Override
        public void init() {
            System.setProperty(Constants.ATTACH_DEBUG, "true");
            AppViewManager.registerViews(this);
        }

        @Override
        public void postInit(Scene scene) {
            Swatch.YELLOW.assignTo(scene);
            scene.getStylesheets().add(Optional.ofNullable(TiltMaze.class.getResource("style.css"))
                    .map(URL::toExternalForm)
                    .orElseThrow());
            LOGGER.debug("height={}, width={}", scene.getHeight(), scene.getWidth());
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
}
