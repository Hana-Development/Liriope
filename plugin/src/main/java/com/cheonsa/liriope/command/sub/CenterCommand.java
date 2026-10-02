package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.time.Duration;

import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.num;
import static com.cheonsa.liriope.command.BorderCommands.over;
import static com.cheonsa.liriope.command.BorderCommands.reply;
import static com.cheonsa.liriope.command.BorderCommands.seconds;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record CenterCommand(ILiriopeAPI api) {

    // ponytail: plain x z, no ~ relative coords; add a column-position ParameterType if needed
    @Subcommand("center")
    public void execute(BukkitCommandActor actor, double x, double z, @Flag(value = "time", shorthand = 't') @Optional Duration time) {
        int seconds = seconds(time);
        border(this.api, actor).setOrigin(x, z, seconds);
        reply(actor, "Border center set to " + num(x) + ", " + num(z) + over(seconds));
    }
}
