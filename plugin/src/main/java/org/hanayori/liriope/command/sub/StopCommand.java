package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.hanayori.liriope.api.border.IBorder;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static org.hanayori.liriope.command.BorderCommands.border;
import static org.hanayori.liriope.command.BorderCommands.reply;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record StopCommand(ILiriopeAPI api) {

    @Subcommand({"stop", "pause", "freeze"})
    public void execute(CommandSender sender) {
        IBorder b = border(this.api, sender);
        b.setRadius(b.getCurrentRadius());
        b.setOrigin(b.getOriginX(), b.getOriginZ());
        reply(sender, "Border stopped");
    }
}
