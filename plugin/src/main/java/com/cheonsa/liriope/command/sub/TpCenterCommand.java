package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

import static com.cheonsa.liriope.command.BorderCommands.reply;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record TpCenterCommand(ILiriopeAPI api) {

    @Subcommand("tp_center")
    public void execute(BukkitCommandActor actor) {
        if (!(actor.sender() instanceof Entity entity)) {
            throw new CommandErrorException("Only players and entities can teleport");
        }
        World world = entity.getWorld();
        IBorder b = this.api.getBorder(world);
        int y = world.getHighestBlockYAt((int) Math.floor(b.getOriginX()), (int) Math.floor(b.getOriginZ())) + 1;
        entity.teleportAsync(new Location(world, b.getOriginX(), y, b.getOriginZ()));
        reply(actor, "Teleported to the border center");
    }
}
