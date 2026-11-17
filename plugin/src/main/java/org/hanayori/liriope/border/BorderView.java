package org.hanayori.liriope.border;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import org.bukkit.Color;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

/** One player's packet-only panels of a border: diffs what is shown against what should be. */
public final class BorderView {

    /** What the client was last sent: the panel's target, and the interpolation it glides there with. */
    private record Shown(int id, Panel panel, double y, double height, Color color, int interpolation) {}

    private final IntSupplier ids;
    private final Consumer<PacketWrapper<?>> out;
    private Map<Integer, Shown> shown = new HashMap<>();
    private Outline outline;
    private boolean inward;

    public BorderView(IntSupplier ids, Consumer<PacketWrapper<?>> out) {
        this.ids = ids;
        this.out = out;
    }

    /**
     * Like vanilla: panels within {@code renderDistance} blocks, each spanning the player's Y ± renderDistance.
     * The pivot steps in 16 blocks (fewer teleports) and the bottom lands on an even Y, so the shader's 2-block
     * stripe tiles anchor to world Y.
     * <p>While the border moves, panels are sent where they will be at the next keyframe ({@link Border#getLead()})
     * with an interpolation that lands exactly then, so a moving border costs one update per panel per keyframe.
     */
    public void update(Border border, double px, double py, double pz, double renderDistance) {
        Outline outline = border.getOutline();
        if (!Objects.equals(outline, this.outline)) {
            this.clear();
            this.outline = outline;
        }

        double cx = border.getOriginX(), cz = border.getOriginZ(), radius = border.getCurrentRadius();
        double y = 16 * Math.floor(py / 16), height = 2 * (16 * Math.ceil(renderDistance / 16)) + 16;
        Color color = border.getDisplayColor();
        int lead = border.getLead();
        int interpolation = lead > 0 ? lead : border.getDrawInterval();
        double tx = border.originXIn(lead), tz = border.originZIn(lead), tRadius = border.radiusIn(lead);
        boolean inward = border.isInside(px, pz);  // face the viewer's side: back faces are culled
        if (inward != this.inward) {  // respawn rather than swing every panel 180 degrees over the interpolation
            this.clear();
            this.outline = outline;
            this.inward = inward;
        }

        Map<Integer, Shown> next = new HashMap<>();
        for (int i : outline.visible(cx, cz, radius, px, pz, renderDistance)) {
            Panel p = outline.panel(tx, tz, tRadius, i, inward);
            Shown old = this.shown.remove(i);
            int id = old == null ? this.ids.getAsInt() : old.id();

            if (old == null) {
                Panel here = outline.panel(cx, cz, radius, i, inward);
                this.out.accept(PanelPackets.spawn(id, UUID.randomUUID(), here, y));
                this.out.accept(PanelPackets.metadata(id, here, height, color, interpolation));
                this.out.accept(PanelPackets.teleport(id, here, y));  // spawn yaw is a byte angle; this sets it exactly
                old = new Shown(id, here, y, height, color, interpolation);
            }
            Panel o = old.panel();
            boolean move = o.x() != p.x() || o.z() != p.z() || o.yaw() != p.yaw() || old.y() != y;
            boolean meta = o.width() != p.width() || o.phase() != p.phase() || old.height() != height || !old.color().equals(color);
            boolean retime = (move || meta) && old.interpolation() != interpolation;
            if (meta || retime) {  // first: it carries the teleport duration the move below glides with
                this.out.accept(PanelPackets.metadata(id, p, height, color, interpolation));
            }
            if (move) {
                this.out.accept(PanelPackets.teleport(id, p, y));
            }
            next.put(i, move || meta ? new Shown(id, p, y, height, color, interpolation) : old);
        }

        destroy(this.shown.values());
        this.shown = next;
    }

    public void clear() {
        destroy(this.shown.values());
        this.shown = new HashMap<>();
        this.outline = null;
    }

    public Set<Integer> shownIds() {
        return this.shown.values().stream().map(Shown::id).collect(Collectors.toUnmodifiableSet());
    }

    /** Panel index to entity id, for tests. */
    Map<Integer, Integer> shownByIndex() {
        return this.shown.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> e.getValue().id()));
    }

    private void destroy(Collection<Shown> panels) {
        if (!panels.isEmpty()) {
            this.out.accept(new WrapperPlayServerDestroyEntities(panels.stream().mapToInt(Shown::id).toArray()));
        }
    }
}
