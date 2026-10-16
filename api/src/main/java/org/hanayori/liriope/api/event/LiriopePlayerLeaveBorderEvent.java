package org.hanayori.liriope.api.event;

import org.hanayori.liriope.api.border.IBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LiriopePlayerLeaveBorderEvent extends LiriopeBorderEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public LiriopePlayerLeaveBorderEvent(@NotNull Player player, @NotNull IBorder border) {
        super(player, border);
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
