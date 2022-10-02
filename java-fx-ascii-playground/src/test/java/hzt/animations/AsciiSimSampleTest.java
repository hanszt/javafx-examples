package hzt.animations;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class AsciiSimSampleTest {

    private AsciiSimSample asciiSimSample;

    @Start
    void onStartUp(Stage stage) {
        asciiSimSample = new AsciiSimSample();
        asciiSimSample.start(stage);
    }

    @Test
    void testAsciiSampleTest() {
        final var text = asciiSimSample.textArea.getText();
        assertFalse(text.isEmpty());
    }

}
