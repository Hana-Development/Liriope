package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.BorderType;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.reply;

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
