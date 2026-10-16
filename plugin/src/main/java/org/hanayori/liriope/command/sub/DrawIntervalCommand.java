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
public record DrawIntervalCommand(ILiriopeAPI api) {

    @Subcommand("draw_interval")
    public void execute(CommandSender sender, int value) {
        atLeast(value, 1);
        border(this.api, sender).setDrawInterval(value);
        reply(sender, "Border draw interval set to " + value);
    }
}
