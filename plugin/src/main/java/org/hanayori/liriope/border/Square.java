package org.hanayori.liriope.border;

import java.util.stream.IntStream;

/**
 * Axis-aligned square, each side split into {@code perSide} equal panels at most {@link Ring#MAX_WIDTH} wide for
 * radii in the fit band. Panels run counterclockwise like {@link Ring}'s, so stripes stay continuous round corners.
 */
public record Square(double baseRadius, int perSide) implements Outline {

    /** Outward normal of each side, counterclockwise from +x. */
    private static final double[][] NORMALS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public static Square forRadius(double radius) {
        return new Square(radius, Math.max(1, (int) Math.ceil(2 * radius * Ring.STEP / Ring.MAX_WIDTH)));
    }

    @Override
    public boolean fits(double radius) {
        return radius >= baseRadius / Ring.STEP && radius <= baseRadius * Ring.STEP;
    }

    @Override
    public Panel panel(double cx, double cz, double radius, int i, boolean inward) {
        double nx = NORMALS[i / perSide][0], nz = NORMALS[i / perSide][1];
        double width = 2 * radius / perSide, along = -radius + (i % perSide + 0.5) * width;  // along the tangent (-nz, nx)
        double c = baseRadius / perSide;  // half the base width, in 2-block tiles: see Ring#panel
        double phase = inward ? i * c : -(i + 1) * c;
        float yaw = (float) Math.toDegrees(inward ? Math.atan2(nx, -nz) : Math.atan2(-nx, nz));
        return new Panel(i, cx + radius * nx - along * nz, cz + radius * nz + along * nx, yaw, width, phase - Math.floor(phase));
    }

    @Override
    public int[] visible(double cx, double cz, double radius, double px, double pz, double viewDistance) {
        if (radius < 1) return new int[0];
        double width = 2 * radius / perSide, dx = px - cx, dz = pz - cz;
        IntStream.Builder out = IntStream.builder();
        for (int side = 0; side < 4; side++) {
            double along = -dx * NORMALS[side][1] + dz * NORMALS[side][0];
            int lo = Math.max(0, (int) Math.floor((along - viewDistance + radius) / width));
            int hi = Math.min(perSide - 1, (int) Math.floor((along + viewDistance + radius) / width));
            for (int k = lo; k <= hi; k++) {
                Panel p = panel(cx, cz, radius, side * perSide + k, true);
                if (Math.hypot(p.x() - px, p.z() - pz) <= viewDistance) out.add(p.index());
            }
        }
        return out.build().toArray();
    }
}
