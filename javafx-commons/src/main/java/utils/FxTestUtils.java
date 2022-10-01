package utils;

import javafx.util.Duration;

public final class FxTestUtils {

    private FxTestUtils() {
    }

    public static void sleep(Duration duration) {
        try {
            Thread.sleep((long) duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
