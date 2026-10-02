package com.cheonsa.liriope.border;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class BorderManager implements ILiriopeAPI {

    private final Map<UUID, Border> worldBorders = new HashMap<>();
    private final Map<UUID, Border> playerBorders = new HashMap<>();

    public void tick() {
        this.worldBorders.forEach((uid, border) -> {
            World world = Bukkit.getWorld(uid);
            if (world == null) {
                return;
            }

            border.tick(world.getPlayers().stream()
                .filter(player -> !this.playerBorders.containsKey(player.getUniqueId()))
                .toList());
        });

        this.playerBorders.forEach((uuid, border) -> {
            Player player = Bukkit.getPlayer(uuid);
            border.tick(player == null ? List.of() : List.of(player));
        });
    }

    public void clear() {
        this.worldBorders.clear();
        this.playerBorders.clear();
    }

    @Override
    public IBorder getBorder(Player player) {
        return this.playerBorders.computeIfAbsent(player.getUniqueId(), uuid -> new Border(player.getWorld()));
    }

    @Override
    public IBorder getBorder(World world) {
        return this.worldBorders.computeIfAbsent(world.getUID(), uid -> new Border(world));
    }

    @Override
    public boolean hasBorder(Player player) {
        return this.playerBorders.containsKey(player.getUniqueId());
    }

    @Override
    public boolean hasBorder(World world) {
        return this.worldBorders.containsKey(world.getUID());
    }

    @Override
    public void removeBorder(Player player) {
        this.playerBorders.remove(player.getUniqueId());
    }

    @Override
    public void removeBorder(World world) {
        this.worldBorders.remove(world.getUID());
    }
}
