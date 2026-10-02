package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.cheonsa.liriope.command.BorderCommands.reply;
import static com.cheonsa.liriope.command.BorderCommands.world;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record RemoveCommand(ILiriopeAPI api) {

    @Subcommand("remove")
    public void execute(BukkitCommandActor actor) {
        this.api.removeBorder(world(actor));
        reply(actor, "Border removed");
    }
}
