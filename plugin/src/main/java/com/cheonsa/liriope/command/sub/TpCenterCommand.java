package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.hanayori.aster.command.CommandException;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static com.cheonsa.liriope.command.BorderCommands.reply;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record TpCenterCommand(ILiriopeAPI api) {

    @Subcommand("tp_center")
    public void execute(CommandSender sender) {
        if (!(sender instanceof Entity entity)) {
            throw new CommandException("Only players and entities can teleport");
        }
        World world = entity.getWorld();
        IBorder b = this.api.getBorder(world);
        int y = world.getHighestBlockYAt((int) Math.floor(b.getOriginX()), (int) Math.floor(b.getOriginZ())) + 1;
        entity.teleportAsync(new Location(world, b.getOriginX(), y, b.getOriginZ()));
        reply(sender, "Teleported to the border center");
    }
}
