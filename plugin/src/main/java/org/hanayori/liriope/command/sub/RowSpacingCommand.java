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
public record RowSpacingCommand(ILiriopeAPI api) {

    @Subcommand("row_spacing")
    public void execute(CommandSender sender, double value) {
        atLeast(value, 0.1);
        border(this.api, sender).setRowSpacing(value);
        reply(sender, "Border row spacing set to " + value);
    }
}
