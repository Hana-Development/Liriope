package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.reply;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record StopCommand(ILiriopeAPI api) {

    @Subcommand({"stop", "pause", "freeze"})
    public void execute(BukkitCommandActor actor) {
        IBorder b = border(this.api, actor);
        b.setRadius(b.getCurrentRadius());
        b.setOrigin(b.getOriginX(), b.getOriginZ());
        reply(actor, "Border stopped");
    }
}
