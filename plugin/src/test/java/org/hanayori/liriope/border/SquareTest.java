package org.hanayori.liriope.border;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SquareTest {

    @Test
    void panelsTileEdgesWithinMaxWidth() {
        Square sq = Square.forRadius(37);
        for (int i = 0; i < 4 * sq.perSide(); i++) {
            Panel p = sq.panel(10, -5, 37, i, true);
            assertTrue(p.width() <= Ring.MAX_WIDTH);
            assertEquals(37, Math.max(Math.abs(p.x() - 10), Math.abs(p.z() + 5)), 1e-9);  // on the edge
        }
        // consecutive panels touch: midpoints one width apart along a side
        Panel a = sq.panel(0, 0, 37, 0, true), b = sq.panel(0, 0, 37, 1, true);
        assertEquals(a.width(), Math.hypot(a.x() - b.x(), a.z() - b.z()), 1e-9);
    }

    @Test
    void facesMatchRing() {  // east side faces like the ring at angle 0
        Square sq = Square.forRadius(100);
        Ring ring = Ring.forRadius(100);
        for (boolean inward : new boolean[]{true, false}) {
            float sy = sq.panel(0, 0, 100, sq.perSide() / 2, inward).yaw();
            float ry = ring.panel(0, 0, 100, 0, inward).yaw();
            assertEquals(ry, sy, 180.0 / ring.segments() + 1e-3);  // ring panel 0 sits half a segment past angle 0
        }
    }

    @Test
    void visibleOnlyNearPlayer() {
        Square sq = Square.forRadius(100);
        int[] v = sq.visible(0, 0, 100, 100, 100, 16);  // player at a corner
        assertTrue(v.length > 0);
        int sides = 0;
        for (int side = 0; side < 4; side++) {
            for (int i : v) if (i / sq.perSide() == side) { sides++; break; }
        }
        assertEquals(2, sides);
        int all = 0;
        for (int i = 0; i < 4 * sq.perSide(); i++) {
            Panel p = sq.panel(0, 0, 100, i, true);
            if (Math.hypot(p.x() - 100, p.z() - 100) <= 16) all++;
        }
        assertEquals(all, v.length);
        assertEquals(0, sq.visible(0, 0, 100, 0, 0, 16).length);  // far from every side
    }
}
