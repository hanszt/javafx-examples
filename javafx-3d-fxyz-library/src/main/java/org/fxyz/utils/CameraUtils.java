package org.fxyz.utils;

import javafx.scene.PerspectiveCamera;
import javafx.scene.input.KeyEvent;

public final class CameraUtils {

    private CameraUtils() {
    }

    public static void moveInXZPlane(PerspectiveCamera camera, KeyEvent event) {
        final double change = event.isShiftDown() ? 50.0 : 10.0;
        switch (event.getCode()) {
            case W -> camera.setTranslateZ(camera.getTranslateZ() + change);
            case S -> camera.setTranslateZ(camera.getTranslateZ() - change);
            case A -> camera.setTranslateX(camera.getTranslateX() - change);
            case D -> camera.setTranslateX(camera.getTranslateX() + change);
            default -> {
                //do nothing
            }
        }
    }
}
