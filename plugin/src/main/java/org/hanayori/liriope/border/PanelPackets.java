package org.hanayori.liriope.border;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Color;

import java.util.List;
import java.util.UUID;

/** Packets for one packet-only TextDisplay panel. */
final class PanelPackets {

    /** Glyph offset in font units: the renderer spans the glyph over text-x [-4.5, 5.5], so shift back by 0.5 to centre it. */
    static final double GLYPH_OFFSET_X = -0.5, GLYPH_OFFSET_Y = 0;

    private PanelPackets() {}

    /** Packs the 6-bit texture phase into the low 2 bits of each colour channel. */
    static int encodeColor(Color tint, double phase) {
        int q = (int) Math.round(phase * 64) & 63;
        return (tint.asRGB() & 0xFCFCFC) | (q >> 4) << 16 | ((q >> 2) & 3) << 8 | (q & 3);
    }

    static WrapperPlayServerSpawnEntity spawn(int id, UUID uuid, Panel p, double y) {
        return new WrapperPlayServerSpawnEntity(id, uuid, EntityTypes.TEXT_DISPLAY, location(p, y), p.yaw(), 0, new Vector3d());
    }

    static WrapperPlayServerEntityMetadata metadata(int id, Panel p, double height, Color tint, int interpolationTicks) {
        float scaleX = (float) (p.width() * 4), scaleY = (float) (height * 4);
        Component text = Component.translatable("liriope.border.panel", "  ").color(TextColor.color(encodeColor(tint, p.phase())));
        return new WrapperPlayServerEntityMetadata(id, List.of(
            new EntityData<>(8, EntityDataTypes.INT, 0),
            new EntityData<>(9, EntityDataTypes.INT, interpolationTicks),
            new EntityData<>(10, EntityDataTypes.INT, Math.min(interpolationTicks, 59)),  // client caps teleport duration at 59
            new EntityData<>(11, EntityDataTypes.VECTOR3F,
                new Vector3f((float) (GLYPH_OFFSET_X * scaleX / 40), (float) (GLYPH_OFFSET_Y * scaleY / 40 - height / 2), 0)),  // pivot at mid-height
            new EntityData<>(12, EntityDataTypes.VECTOR3F, new Vector3f(scaleX, scaleY, 1)),
            new EntityData<>(15, EntityDataTypes.BYTE, (byte) 0),
            new EntityData<>(16, EntityDataTypes.INT, 15 << 4 | 15 << 20),
            new EntityData<>(17, EntityDataTypes.FLOAT, 16f),  // view range x 64 blocks: past 32 chunks even at 50% entity distance
            new EntityData<>(23, EntityDataTypes.ADV_COMPONENT, text),
            new EntityData<>(24, EntityDataTypes.INT, 1000),
            new EntityData<>(25, EntityDataTypes.INT, 0x3E << 24 | tint.asRGB()),
            new EntityData<>(26, EntityDataTypes.BYTE, (byte) -2),
            new EntityData<>(27, EntityDataTypes.BYTE, (byte) 0)));
    }

    static WrapperPlayServerEntityTeleport teleport(int id, Panel p, double y) {
        return new WrapperPlayServerEntityTeleport(id, location(p, y), false);
    }

    private static Location location(Panel p, double y) {
        return new Location(p.x(), y, p.z(), p.yaw(), 0);
    }
}
