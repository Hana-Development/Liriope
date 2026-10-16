package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.hanayori.liriope.api.border.BorderType;
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
public record TypeCommand(ILiriopeAPI api) {

    @Subcommand("type")
    public void execute(CommandSender sender, BorderType type) {
        border(this.api, sender).setBorderType(type);
        reply(sender, "Border shape set to " + type);
    }
}
