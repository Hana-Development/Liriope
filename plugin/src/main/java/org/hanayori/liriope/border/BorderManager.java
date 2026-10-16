package org.hanayori.liriope.border;

import org.hanayori.liriope.api.border.IBorder;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.hanayori.aster.component.Injectable;
import org.hanayori.aster.component.PostInit;
import org.hanayori.aster.component.PreDestroy;
import org.hanayori.aster.schedule.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Injectable
public final class BorderManager {

    private final Map<UUID, Border> worldBorders = new HashMap<>();
    private final Map<UUID, Border> playerBorders = new HashMap<>();
    private ScheduledTask task;

    @PostInit
    void onPostInit() {
        this.task = Schedulers.getGlobalScheduler().runTimer(this::tick, 1, 1);
    }

    @PreDestroy
    void onPreDestroy() {
        this.task.cancel();
        this.worldBorders.clear();
        this.playerBorders.clear();
    }

    private void tick() {
        this.worldBorders.values().forEach(Border::tick);
        this.playerBorders.values().forEach(Border::tick);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Border border = this.playerBorders.get(player.getUniqueId());
            if (border == null) {
                border = this.worldBorders.get(player.getWorld().getUID());
            }
            if (border != null) {
                border.tick(player);
            }
        }
    }

    public IBorder getBorder(Player player) {
        return this.playerBorders.computeIfAbsent(player.getUniqueId(), uuid -> new Border(player.getWorld()));
    }

    public IBorder getBorder(World world) {
        return this.worldBorders.computeIfAbsent(world.getUID(), uid -> new Border(world));
    }

    public boolean hasBorder(Player player) {
        return this.playerBorders.containsKey(player.getUniqueId());
    }

    public boolean hasBorder(World world) {
        return this.worldBorders.containsKey(world.getUID());
    }

    public void removeBorder(Player player) {
        this.playerBorders.remove(player.getUniqueId());
    }

    public void removeBorder(World world) {
        this.worldBorders.remove(world.getUID());
    }
}
