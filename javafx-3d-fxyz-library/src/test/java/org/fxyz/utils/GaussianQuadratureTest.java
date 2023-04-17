package org.fxyz.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GaussianQuadratureTest {

    @Test
    void testNIntegrate() {
        final var v = new GaussianQuadrature(7, 0, Math.PI).nIntegrate(Math::sin);
        assertEquals(2, v, 1e-10);
    }

}
