package utils;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorTests {

    @Test
    void testColorWebToOpacity() {
        final var web = Color.web("#000000", 0.6);

        assertEquals("0x000000ff", toHex(Color.web("#000000")));
        assertEquals("0x00000099", toHex(web));
    }

    /**
     * Copied from the {@link Color#toString}
     *
     * @param color The color to transform
     * @return The hex (web) representation
     */
    private String toHex(Color color) {
        int r = (int) Math.round(color.getRed() * 255.0);
        int g = (int) Math.round(color.getGreen() * 255.0);
        int b = (int) Math.round(color.getBlue() * 255.0);
        int o = (int) Math.round(color.getOpacity() * 255.0);
        return String.format("0x%02x%02x%02x%02x", r, g, b, o);
    }
}
