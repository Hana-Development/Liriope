package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.BorderType;
import com.cheonsa.liriope.api.border.IBorder;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Flag;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Switch;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

import java.time.Duration;

import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.num;
import static com.cheonsa.liriope.command.BorderCommands.over;
import static com.cheonsa.liriope.command.BorderCommands.reply;
import static com.cheonsa.liriope.command.BorderCommands.seconds;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record SetCommand(ILiriopeAPI api) {

    @Subcommand("set")
    public void execute(BukkitCommandActor actor, @Range(min = 0) double size,
                        @Switch("confirm") boolean confirm, @Flag(value = "time", shorthand = 't') @Optional Duration time) {
        if (!confirm) {
            throw new CommandErrorException("You need to confirm this action by add --confirm");
        }
        IBorder b = border(this.api, actor);
        boolean square = b.getBorderType() == BorderType.SQUARE;
        int seconds = seconds(time);
        b.setRadius(square ? size / 2 : size, seconds);
        reply(actor, "Border " + (square ? "width" : "radius") + " set to " + num(size) + over(seconds));
    }
}
