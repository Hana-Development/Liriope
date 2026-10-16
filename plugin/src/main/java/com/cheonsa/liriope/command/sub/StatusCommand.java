package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static com.cheonsa.liriope.command.BorderCommands.centerText;
import static com.cheonsa.liriope.command.BorderCommands.existing;
import static com.cheonsa.liriope.command.BorderCommands.hex;
import static com.cheonsa.liriope.command.BorderCommands.reply;
import static com.cheonsa.liriope.command.BorderCommands.sizeText;
import static com.cheonsa.liriope.command.BorderCommands.world;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record StatusCommand(ILiriopeAPI api) {

    @Subcommand({"status", "info"})
    public void execute(CommandSender sender) {
        World world = world(sender);
        IBorder b = existing(this.api, world);
        reply(sender, "Border of " + world.getName() + ":"
            + "\n Shape: " + b.getBorderType()
            + "\n Center: " + centerText(b)
            + "\n Size: " + sizeText(b)
            + "\n Damage: " + b.getDamage() + " every " + b.getDamageTick() + " ticks"
            + "\n Color: " + hex(b.getColor()));
    }
}
