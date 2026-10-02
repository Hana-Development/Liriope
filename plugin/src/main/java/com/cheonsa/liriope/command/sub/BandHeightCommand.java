package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.reply;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record BandHeightCommand(ILiriopeAPI api) {

    @Subcommand("band_height")
    public void execute(BukkitCommandActor actor, @Range(min = 0) double value) {
        border(this.api, actor).setBandHeight(value);
        reply(actor, "Border band height set to " + value);
    }
}
