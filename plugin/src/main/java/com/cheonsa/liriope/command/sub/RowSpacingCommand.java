package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static com.cheonsa.liriope.command.BorderCommands.atLeast;
import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.reply;

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
