package org.hanayori.liriope.border;

import java.util.Arrays;

/** N-gon inscribed in a circle whose sagitta stays within {@link #MAX_SAGITTA} for radii in the fit band. */
public record Ring(double baseRadius, int segments) implements Outline {
    public static final double MAX_SAGITTA = 1.0 / 16, MIN_WIDTH = 0.25, MAX_WIDTH = 8, STEP = 1.25;

    public static Ring forRadius(double radius) {
        return new Ring(radius, segmentsFor(radius * STEP));
    }

    public static int segmentsFor(double radius) {
        double s = MAX_SAGITTA;
        double w = radius <= s ? MIN_WIDTH
                : Math.clamp(2 * Math.sqrt(radius * radius - (radius - s) * (radius - s)), MIN_WIDTH, MAX_WIDTH);
        return Math.max(3, (int) Math.ceil(2 * Math.PI * radius / w));
    }

    @Override
    public boolean fits(double radius) {
        return radius >= baseRadius / STEP && radius <= baseRadius * STEP;
    }

    public double chord(double radius) {
        return 2 * radius * Math.sin(Math.PI / segments);
    }

    /**
     * Panel {@code i} facing the centre ({@code inward}) or away from it. Text back faces are culled,
     * so it must face the viewer's side. Facing out, localU runs opposite to {@code i}, so the phase
     * is taken from the panel's far end to keep stripes continuous across seams. The phase uses the
     * base chord: seams drift by under a stripe while the radius moves within the band, instead of
     * every panel's stripes jumping on each resize step.
     */
    @Override
    public Panel panel(double cx, double cz, double radius, int i, boolean inward) {
        double theta = (i + 0.5) * 2 * Math.PI / segments;
        double cos = Math.cos(theta), sin = Math.sin(theta);
        double mid = radius * Math.cos(Math.PI / segments);
        double chord = chord(radius);
        double c = chord(baseRadius) / 2;  // in 2-block tiles (vanilla); base chord so stripes don't strobe while resizing
        double phase = inward ? i * c : -(i + 1) * c;
        float yaw = (float) Math.toDegrees(inward ? Math.atan2(cos, -sin) : Math.atan2(-cos, sin));
        return new Panel(i, cx + mid * cos, cz + mid * sin, yaw, chord, phase - Math.floor(phase));
    }

    @Override
    public int[] visible(double cx, double cz, double radius, double px, double pz, double viewDistance) {
        double dist = Math.hypot(px - cx, pz - cz), mid = radius * Math.cos(Math.PI / segments);
        if (radius < 1 || Math.abs(dist - mid) > viewDistance) return new int[0];
        double seg = 2 * Math.PI / segments;
        double angle = Math.atan2(pz - cz, px - cx);
        // law of cosines on the panel midpoints (radius mid): in range iff cos(phi) >= rhs
        double half = Math.PI;
        if (dist >= 1e-9) {
            double rhs = (mid * mid + dist * dist - viewDistance * viewDistance) / (2 * mid * dist);
            if (rhs > 1) return new int[0];
            if (rhs > -1) half = Math.min(Math.PI, Math.acos(rhs) + seg);  // + one segment margin; exact filter below
        }
        long lo = (long) Math.floor((angle - half) / seg);
        long hi = Math.min((long) Math.ceil((angle + half) / seg), lo + segments - 1);  // dedupe: at most N indices
        int[] out = new int[(int) (hi - lo + 1)];
        int n = 0;
        for (long k = lo; k <= hi; k++) {
            int i = (int) Math.floorMod(k, (long) segments);
            Panel p = panel(cx, cz, radius, i, true);
            if (Math.hypot(p.x() - px, p.z() - pz) <= viewDistance) out[n++] = i;
        }
        return Arrays.copyOf(out, n);
    }
}
