package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static org.hanayori.liriope.command.BorderCommands.reply;
import static org.hanayori.liriope.command.BorderCommands.world;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record RemoveCommand(ILiriopeAPI api) {

    @Subcommand("remove")
    public void execute(CommandSender sender) {
        this.api.removeBorder(world(sender));
        reply(sender, "Border removed");
    }
}
