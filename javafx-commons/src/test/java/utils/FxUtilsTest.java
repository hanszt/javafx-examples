package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FxUtilsTest {

    @Test
    void testJunit() {
        var list = new ArrayList<>();
        list.add("Hallo");
        assertTrue(list.contains("Hallo"));
    }
}
