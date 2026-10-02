package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.World;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static com.cheonsa.liriope.command.BorderCommands.centerText;
import static com.cheonsa.liriope.command.BorderCommands.existing;
import static com.cheonsa.liriope.command.BorderCommands.hex;
import static com.cheonsa.liriope.command.BorderCommands.reply;
import static com.cheonsa.liriope.command.BorderCommands.sizeText;
import static com.cheonsa.liriope.command.BorderCommands.world;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record StatusCommand(ILiriopeAPI api) {

    @Subcommand({"status", "info"})
    public void execute(BukkitCommandActor actor) {
        World world = world(actor);
        IBorder b = existing(this.api, world);
        reply(actor, "Border of " + world.getName() + ":"
            + "\n Shape: " + b.getBorderType()
            + "\n Center: " + centerText(b)
            + "\n Size: " + sizeText(b)
            + "\n Damage: " + b.getDamage() + " every " + b.getDamageTick() + " ticks"
            + "\n Color: " + hex(b.getColor()));
    }
}
