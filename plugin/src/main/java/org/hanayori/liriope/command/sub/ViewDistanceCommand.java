package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static org.hanayori.liriope.command.BorderCommands.atLeast;
import static org.hanayori.liriope.command.BorderCommands.border;
import static org.hanayori.liriope.command.BorderCommands.reply;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record ViewDistanceCommand(ILiriopeAPI api) {

    @Subcommand("view_distance")
    public void execute(CommandSender sender, double value) {
        atLeast(value, 0);
        border(this.api, sender).setViewDistance(value);
        reply(sender, "Border view distance set to " + value);
    }
}
