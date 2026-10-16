package com.cheonsa.liriope.border;

import com.cheonsa.liriope.api.border.BorderType;
import com.cheonsa.liriope.api.border.IBorder;
import com.cheonsa.liriope.api.event.LiriopePlayerDamageByBorderEvent;
import com.cheonsa.liriope.api.event.LiriopePlayerEnterBorderEvent;
import com.cheonsa.liriope.api.event.LiriopePlayerLeaveBorderEvent;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Border implements IBorder {

    private static final DamageSource DAMAGE_SOURCE = DamageSource.builder(DamageType.OUTSIDE_BORDER).build();

    private BorderType type = BorderType.CIRCLE;
    private double originX;
    private double originZ;
    private double startX;
    private double startZ;
    private double targetX;
    private double targetZ;
    private int moveTicks;
    private int moveElapsed;

    private double radius;
    private double startRadius;
    private double targetRadius;
    private int durationTicks;
    private int elapsedTicks;

    private double damage = 1.0;
    private long damageTick = 20;

    private Color color = Color.AQUA;
    private float particleSize = 1.5f;
    private Particle.DustOptions dust = new Particle.DustOptions(this.color, this.particleSize);
    private double viewDistance = 16.0;
    private double pointSpacing = 2.0;
    private double bandHeight = 6.0;
    private double rowSpacing = 2.0;
    private int drawInterval = 8;

    private int tickCount;
    private boolean drawing;
    private boolean damaging;
    private final Set<UUID> outside = new HashSet<>();

    public Border(World world) {
        WorldBorder vanilla = world.getWorldBorder();
        this.originX = vanilla.getCenter().getX();
        this.originZ = vanilla.getCenter().getZ();
        this.targetX = this.originX;
        this.targetZ = this.originZ;
        this.radius = vanilla.getSize() / 2;
        this.targetRadius = this.radius;
    }

    public void tick() {
        this.updateRadius();
        this.updateOrigin();

        this.tickCount++;
        this.drawing = this.tickCount % this.drawInterval == 0 && this.radius >= 1.0;
        this.damaging = this.tickCount % this.damageTick == 0;
    }

    public void tick(Player player) {
        if (this.drawing) {
            this.draw(player);
        }

        if (this.isInside(player.getX(), player.getZ())) {
            if (this.outside.remove(player.getUniqueId())) {
                new LiriopePlayerEnterBorderEvent(player, this).callEvent();
            }
            return;
        }

        if (this.outside.add(player.getUniqueId())) {
            new LiriopePlayerLeaveBorderEvent(player, this).callEvent();
        }

        if (this.damaging && new LiriopePlayerDamageByBorderEvent(player, this).callEvent()) {
            player.damage(this.damage, DAMAGE_SOURCE);
        }
    }

    private void updateRadius() {
        if (this.radius == this.targetRadius) {
            return;
        }

        this.elapsedTicks++;
        if (this.elapsedTicks >= this.durationTicks) {
            this.radius = this.targetRadius;
        } else {
            double progress = (double) this.elapsedTicks / this.durationTicks;
            this.radius = this.startRadius + (this.targetRadius - this.startRadius) * progress;
        }
    }

    private void updateOrigin() {
        if (this.originX == this.targetX && this.originZ == this.targetZ) {
            return;
        }

        this.moveElapsed++;
        if (this.moveElapsed >= this.moveTicks) {
            this.originX = this.targetX;
            this.originZ = this.targetZ;
        } else {
            double progress = (double) this.moveElapsed / this.moveTicks;
            this.originX = this.startX + (this.targetX - this.startX) * progress;
            this.originZ = this.startZ + (this.targetZ - this.startZ) * progress;
        }
    }

    private boolean isInside(double x, double z) {
        double dx = x - this.originX;
        double dz = z - this.originZ;
        return switch (this.type) {
            case CIRCLE -> dx * dx + dz * dz <= this.radius * this.radius;
            case SQUARE -> Math.max(Math.abs(dx), Math.abs(dz)) <= this.radius;
        };
    }

    private void draw(Player player) {
        switch (this.type) {
            case CIRCLE -> this.drawCircle(player);
            case SQUARE -> this.drawSquare(player);
        }
    }

    private void drawCircle(Player player) {
        double dx = player.getX() - this.originX;
        double dz = player.getZ() - this.originZ;
        if (Math.abs(Math.hypot(dx, dz) - this.radius) > this.viewDistance) {
            return;
        }

        double playerAngle = Math.atan2(dz, dx);
        double halfArc = Math.min(Math.PI, this.viewDistance / this.radius);
        double step = this.pointSpacing / this.radius;

        for (double angle = playerAngle - halfArc; angle <= playerAngle + halfArc; angle += step) {
            this.drawColumn(player, this.originX + this.radius * Math.cos(angle), this.originZ + this.radius * Math.sin(angle));
        }
    }

    private void drawSquare(Player player) {
        double dx = player.getX() - this.originX;
        double dz = player.getZ() - this.originZ;

        for (double side : new double[]{-this.radius, this.radius}) {
            this.drawSquareEdge(player, side, dx, dz, true);
            this.drawSquareEdge(player, side, dz, dx, false);
        }
    }

    private void drawSquareEdge(Player player, double side, double across, double along, boolean xEdge) {
        if (Math.abs(across - side) > this.viewDistance) {
            return;
        }

        double from = Math.max(-this.radius, along - this.viewDistance);
        double to = Math.min(this.radius, along + this.viewDistance);
        for (double t = from; t <= to; t += this.pointSpacing) {
            if (xEdge) {
                this.drawColumn(player, this.originX + side, this.originZ + t);
            } else {
                this.drawColumn(player, this.originX + t, this.originZ + side);
            }
        }
    }

    private void drawColumn(Player player, double x, double z) {
        if (Math.hypot(x - player.getX(), z - player.getZ()) > this.viewDistance) {
            return;
        }

        double baseY = Math.floor(player.getY());
        for (double dy = -this.bandHeight; dy <= this.bandHeight; dy += this.rowSpacing) {
            player.spawnParticle(Particle.DUST, x, baseY + dy, z, 1, 0, 0, 0, 0, this.dust);
        }
    }

    @Override
    public BorderType getBorderType() {
        return this.type;
    }

    @Override
    public void setBorderType(BorderType type) {
        this.type = type;
    }

    @Override
    public double getOriginX() {
        return this.originX;
    }

    @Override
    public double getOriginZ() {
        return this.originZ;
    }

    @Override
    public double getTargetOriginX() {
        return this.targetX;
    }

    @Override
    public double getTargetOriginZ() {
        return this.targetZ;
    }

    @Override
    public int getRemainingMoveTime() {
        if (this.originX == this.targetX && this.originZ == this.targetZ) {
            return 0;
        }
        return (int) Math.ceil((this.moveTicks - this.moveElapsed) / 20.0);
    }

    @Override
    public void setOrigin(double x, double z) {
        this.setOrigin(x, z, 0);
    }

    @Override
    public void setOrigin(double x, double z, int time) {
        this.startX = this.originX;
        this.startZ = this.originZ;
        this.targetX = x;
        this.targetZ = z;
        this.moveTicks = time * 20;
        this.moveElapsed = 0;

        if (time <= 0) {
            this.originX = x;
            this.originZ = z;
        }
    }

    @Override
    public void setOrigin(Location location) {
        this.setOrigin(location.getX(), location.getZ());
    }

    @Override
    public double getCurrentRadius() {
        return this.radius;
    }

    @Override
    public double getTargetRadius() {
        return this.targetRadius;
    }

    @Override
    public int getRemainingTime() {
        if (this.radius == this.targetRadius) {
            return 0;
        }
        return (int) Math.ceil((this.durationTicks - this.elapsedTicks) / 20.0);
    }

    @Override
    public void setRadius(double radius) {
        this.setRadius(radius, 0);
    }

    @Override
    public void setRadius(double radius, int time) {
        this.startRadius = this.radius;
        this.targetRadius = radius;
        this.durationTicks = time * 20;
        this.elapsedTicks = 0;

        if (time <= 0) {
            this.radius = radius;
        }
    }

    @Override
    public boolean isInside(Location location) {
        return this.isInside(location.getX(), location.getZ());
    }

    @Override
    public double getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public long getDamageTick() {
        return this.damageTick;
    }

    @Override
    public void setDamageTick(long damageTick) {
        this.damageTick = Math.max(1, damageTick);
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        this.dust = new Particle.DustOptions(color, this.particleSize);
    }

    @Override
    public float getParticleSize() {
        return this.particleSize;
    }

    @Override
    public void setParticleSize(float particleSize) {
        this.particleSize = Math.max(0.01f, particleSize);
        this.dust = new Particle.DustOptions(this.color, this.particleSize);
    }

    @Override
    public double getViewDistance() {
        return this.viewDistance;
    }

    @Override
    public void setViewDistance(double viewDistance) {
        this.viewDistance = Math.max(0, viewDistance);
    }

    @Override
    public double getPointSpacing() {
        return this.pointSpacing;
    }

    @Override
    public void setPointSpacing(double pointSpacing) {
        this.pointSpacing = Math.max(0.1, pointSpacing);
    }

    @Override
    public double getBandHeight() {
        return this.bandHeight;
    }

    @Override
    public void setBandHeight(double bandHeight) {
        this.bandHeight = Math.max(0, bandHeight);
    }

    @Override
    public double getRowSpacing() {
        return this.rowSpacing;
    }

    @Override
    public void setRowSpacing(double rowSpacing) {
        this.rowSpacing = Math.max(0.1, rowSpacing);
    }

    @Override
    public int getDrawInterval() {
        return this.drawInterval;
    }

    @Override
    public void setDrawInterval(int drawInterval) {
        this.drawInterval = Math.max(1, drawInterval);
    }
}
