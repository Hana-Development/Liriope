package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.hanayori.liriope.api.border.IBorder;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static org.hanayori.liriope.command.BorderCommands.centerText;
import static org.hanayori.liriope.command.BorderCommands.existing;
import static org.hanayori.liriope.command.BorderCommands.hex;
import static org.hanayori.liriope.command.BorderCommands.reply;
import static org.hanayori.liriope.command.BorderCommands.sizeText;
import static org.hanayori.liriope.command.BorderCommands.world;

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
