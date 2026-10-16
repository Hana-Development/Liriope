package org.hanayori.liriope.api.event;

import org.hanayori.liriope.api.border.IBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class LiriopePlayerEnterBorderEvent extends LiriopeBorderEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public LiriopePlayerEnterBorderEvent(@NotNull Player player, @NotNull IBorder border) {
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
