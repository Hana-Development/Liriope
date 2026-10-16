package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.hex;
import static com.cheonsa.liriope.command.BorderCommands.reply;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record ColorCommand(ILiriopeAPI api) {

    @Subcommand("color")
    public void execute(CommandSender sender, Color color) {
        border(this.api, sender).setColor(color);
        reply(sender, "Border color set to " + hex(color));
    }
}
