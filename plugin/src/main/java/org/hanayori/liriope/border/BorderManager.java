package org.hanayori.liriope.border;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.hanayori.liriope.api.border.BorderStyle;
import org.hanayori.liriope.api.border.IBorder;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.hanayori.aster.component.DependsOn;
import org.hanayori.aster.component.Service;
import org.hanayori.aster.component.PostInit;
import org.hanayori.aster.component.PreDestroy;
import org.hanayori.aster.packet.PacketService;
import org.hanayori.aster.schedule.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@DependsOn(PacketService.class)
public final class BorderManager implements Listener {

    private final Map<UUID, Border> worldBorders = new HashMap<>();
    private final Map<UUID, Border> playerBorders = new HashMap<>();
    private final Map<UUID, BorderView> views = new HashMap<>();
    /** The border each view's panels were built for. */
    private final Map<UUID, Border> viewBorder = new HashMap<>();
    private ScheduledTask task;

    @PostInit
    public void setup() {
        this.task = Schedulers.getGlobalScheduler().runTimer(this::tick, 1, 1);
    }

    @PreDestroy
    public void teardown() {
        this.task.cancel();
        this.views.values().forEach(BorderView::clear);
        this.views.clear();
        this.viewBorder.clear();
        this.worldBorders.clear();
        this.playerBorders.clear();
    }

    private void tick() {
        this.worldBorders.values().forEach(Border::tick);
        this.playerBorders.values().forEach(Border::tick);

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            Border border = this.playerBorders.get(uuid);
            if (border == null) {
                border = this.worldBorders.get(player.getWorld().getUID());
            }
            if (this.viewBorder.containsKey(uuid) && (this.viewBorder.get(uuid) != border || border.getBorderStyle() != BorderStyle.TEXT_DISPLAY)) {
                this.clearView(uuid);
            }
            if (border == null) {
                continue;
            }

            border.tick(player);
            if (border.getBorderStyle() == BorderStyle.TEXT_DISPLAY && border.isRedrawTick()) {
                Location loc = player.getLocation();
                this.views.computeIfAbsent(uuid, u -> newView(player)).update(border, loc.getX(), loc.getY(), loc.getZ(), renderDistance(player));
                this.viewBorder.put(uuid, border);
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

    private static BorderView newView(Player player) {
        return new BorderView(SpigotReflectionUtil::generateEntityId, packet -> PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet));
    }

    /** Vanilla's effective render distance in blocks: the client setting, capped by the server's view distance. */
    private static double renderDistance(Player player) {
        return 16.0 * Math.min(player.getClientViewDistance(), player.getViewDistance());
    }

    private void clearView(UUID uuid) {
        BorderView view = this.views.get(uuid);
        if (view != null) {
            view.clear();
        }
        this.viewBorder.remove(uuid);
    }

    /**
     * Forget the view without sending anything: after quit the client is gone. Respawn and world change
     * clear first, because a same-dimension respawn keeps the client's level and its entities.
     */
    private void forget(PlayerEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        this.views.remove(uuid);
        this.viewBorder.remove(uuid);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.forget(event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        this.clearView(event.getPlayer().getUniqueId());
        this.forget(event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        this.clearView(event.getPlayer().getUniqueId());
        this.forget(event);
    }
}
