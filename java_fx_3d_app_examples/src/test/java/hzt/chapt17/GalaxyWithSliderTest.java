package hzt.chapt17;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import utils.FxUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ApplicationExtension.class)
class GalaxyWithSliderTest {

    private final GalaxyWithSlider galaxyWithSlider = new GalaxyWithSlider();

    private Stage stage;

    @SuppressWarnings("unused")
    @Start
    private void start(Stage stage) {
        galaxyWithSlider.start(stage);
        this.stage = stage;
    }

    @Test
    void testShouldContainSlider() {
        final var root = stage.getScene().getRoot();
        assertTrue(FxUtils.allDescendents(root).contains(galaxyWithSlider.slider));
    }

    @Test
    void testShouldContainWorld() {
        final var root = stage.getScene().getRoot();
        assertTrue(FxUtils.allDescendents(root).contains(galaxyWithSlider.world));
    }

    @Test
    void testWhenSliderMovedGalaxyMovesInZDirection(FxRobot robot) {
        final var slider = galaxyWithSlider.slider;
        final var world = galaxyWithSlider.world;

        final var TARGET_SLIDER_VALUE_1 = 100;
        robot.drag(slider).interact(() -> slider.setValue(TARGET_SLIDER_VALUE_1));
        assertEquals(TARGET_SLIDER_VALUE_1, world.getTranslateZ());

        final var TARGET_SLIDER_VALUE_2 = -100;
        robot.drag(slider).interact(() -> slider.setValue(TARGET_SLIDER_VALUE_2));
        assertEquals(TARGET_SLIDER_VALUE_2, world.getTranslateZ());
    }
}
