package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
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
public record StopCommand(ILiriopeAPI api) {

    @Subcommand({"stop", "pause", "freeze"})
    public void execute(CommandSender sender) {
        IBorder b = border(this.api, sender);
        b.setRadius(b.getCurrentRadius());
        b.setOrigin(b.getOriginX(), b.getOriginZ());
        reply(sender, "Border stopped");
    }
}
