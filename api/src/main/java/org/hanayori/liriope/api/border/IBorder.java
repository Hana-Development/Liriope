package org.hanayori.liriope.api.border;

import org.bukkit.Color;
import org.bukkit.Location;

public interface IBorder {

    /**
     * Shape of the border. Default {@link BorderType#CIRCLE}.
     */
    BorderType getBorderType();

    void setBorderType(BorderType type);

    /**
     * X coordinate of the border's center right now, changes every tick while moving. Default the vanilla border's center.
     */
    double getOriginX();

    /**
     * Z coordinate of the border's center right now, changes every tick while moving. Default the vanilla border's center.
     */
    double getOriginZ();

    /**
     * X coordinate the center is moving to, equal to {@link #getOriginX()} when not moving.
     */
    double getTargetOriginX();

    /**
     * Z coordinate the center is moving to, equal to {@link #getOriginZ()} when not moving.
     */
    double getTargetOriginZ();

    /**
     * @return seconds left until the current move reaches the target center, 0 if not moving
     */
    int getRemainingMoveTime();

    /**
     * Moves the center of the border instantly, cancels any running move.
     */
    void setOrigin(double x, double z);

    /**
     * Moves the center smoothly from the current center over {@code time} seconds,
     * replacing any running move. {@code time <= 0} moves instantly.
     */
    void setOrigin(double x, double z, int time);

    /**
     * Moves the center of the border instantly. Only x/z are used, the world of the location is ignored.
     */
    void setOrigin(Location location);

    /**
     * Radius right now, changes every tick while resizing.
     */
    double getCurrentRadius();

    /**
     * Radius the border is resizing to, equal to {@link #getCurrentRadius()} when not resizing.
     */
    double getTargetRadius();

    /**
     * @return seconds left until the current resize reaches the target radius, 0 if not resizing
     */
    int getRemainingTime();

    /**
     * Sets the radius instantly, cancels any running resize.
     */
    void setRadius(double radius);

    /**
     * Resizes the border smoothly from the current radius over {@code time} seconds,
     * replacing any running resize. {@code time <= 0} resizes instantly.
     */
    void setRadius(double radius, int time);

    /**
     * Checks x/z only, the world of the location is ignored.
     *
     * @return true if the location is inside or exactly on the edge
     */
    boolean isInside(Location location);

    /**
     * Damage dealt to a player outside the border every {@link #getDamageTick()} ticks.
     * Default 1.0 (half a heart). Recommended 1.0 - 4.0.
     */
    double getDamage();

    void setDamage(double damage);

    /**
     * Ticks between damage hits (20 ticks = 1 second). Default 20. Recommended 10 - 40.
     * Values below 1 are raised to 1.
     */
    long getDamageTick();

    void setDamageTick(long damageTick);

    /**
     * Particle color. Default {@link Color#AQUA}.
     */
    Color getColor();

    void setColor(Color color);

    /**
     * Dust particle size. Default 1.5. Recommended 1.0 - 2.0, above 4.0 is clamped by the client.
     */
    float getParticleSize();

    void setParticleSize(float particleSize);

    /**
     * How close (in blocks) a player must be to the edge to see it. Default 16. Recommended 8 - 24.
     * The client hides particles further than about 32 blocks away, so higher values only cost performance.
     */
    double getViewDistance();

    void setViewDistance(double viewDistance);

    /**
     * Horizontal gap between particle columns along the border, in blocks. Default 2. Recommended 1 - 3.
     * Halving this doubles the particles sent. Values below 0.1 are raised to 0.1.
     */
    double getPointSpacing();

    void setPointSpacing(double pointSpacing);

    /**
     * Blocks drawn above and below the player's feet. Default 6. Recommended 3 - 8.
     */
    double getBandHeight();

    void setBandHeight(double bandHeight);

    /**
     * Vertical gap between particle rows, in blocks. Default 2. Recommended 1 - 3.
     * Halving this doubles the particles sent. Values below 0.1 are raised to 0.1.
     */
    double getRowSpacing();

    void setRowSpacing(double rowSpacing);

    /**
     * Ticks between redraws. Default 8. Recommended 4 - 10.
     * Lower looks smoother but sends more particles, above 10 the border starts to flicker.
     * Values below 1 are raised to 1.
     */
    int getDrawInterval();

    void setDrawInterval(int drawInterval);
}
