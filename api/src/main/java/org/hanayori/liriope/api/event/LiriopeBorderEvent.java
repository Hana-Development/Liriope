package org.hanayori.liriope.api.event;

import org.hanayori.liriope.api.border.IBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public abstract class LiriopeBorderEvent extends PlayerEvent {

    private final IBorder border;

    protected LiriopeBorderEvent(@NotNull Player player, @NotNull IBorder border) {
        super(player);
        this.border = border;
    }

    public @NotNull IBorder getBorder() {
        return this.border;
    }
}
