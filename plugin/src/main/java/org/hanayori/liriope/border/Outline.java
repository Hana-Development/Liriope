package org.hanayori.liriope.border;

import org.hanayori.liriope.api.border.BorderType;

/** A border's shape split into flat panels for {@link BorderView}. */
public sealed interface Outline permits Ring, Square {

    static Outline of(BorderType type, double radius) {
        return switch (type) {
            case CIRCLE -> Ring.forRadius(radius);
            case SQUARE -> Square.forRadius(radius);
        };
    }

    /** Whether this outline's panel count still suits {@code radius}; rebuild when false. */
    boolean fits(double radius);

    Panel panel(double cx, double cz, double radius, int i, boolean inward);

    /** Indices of the panels whose midpoint is within {@code viewDistance} of the player. */
    int[] visible(double cx, double cz, double radius, double px, double pz, double viewDistance);
}
