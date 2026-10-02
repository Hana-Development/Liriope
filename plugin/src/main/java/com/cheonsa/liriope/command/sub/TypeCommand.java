package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.BorderType;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.reply;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record TypeCommand(ILiriopeAPI api) {

    @Subcommand("type")
    public void execute(BukkitCommandActor actor, BorderType type) {
        border(this.api, actor).setBorderType(type);
        reply(actor, "Border shape set to " + type);
    }
}
