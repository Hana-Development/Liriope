package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.BorderType;
import com.cheonsa.liriope.api.border.IBorder;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Values;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.exception.CommandErrorException;

import static com.cheonsa.liriope.command.BorderCommands.cap;
import static com.cheonsa.liriope.command.BorderCommands.centerText;
import static com.cheonsa.liriope.command.BorderCommands.duration;
import static com.cheonsa.liriope.command.BorderCommands.existing;
import static com.cheonsa.liriope.command.BorderCommands.hex;
import static com.cheonsa.liriope.command.BorderCommands.num;
import static com.cheonsa.liriope.command.BorderCommands.reply;
import static com.cheonsa.liriope.command.BorderCommands.sizeText;
import static com.cheonsa.liriope.command.BorderCommands.world;

@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record GetCommand(ILiriopeAPI api) {

    @Subcommand("get")
    public void execute(BukkitCommandActor actor,
                        @Values({"radius", "size", "width", "length", "center", "damage", "tick", "color", "origin"}) String key) {
        IBorder b = existing(this.api, world(actor));
        boolean square = b.getBorderType() == BorderType.SQUARE;
        if (key.equals("radius") && square) {
            throw new CommandErrorException("Radius only applies to round shapes");
        }
        if ((key.equals("width") || key.equals("length")) && !square) {
            throw new CommandErrorException(cap(key) + " only applies to square shapes");
        }
        reply(actor, switch (key) {
            case "radius", "width", "length", "size" -> cap(key) + ": " + sizeText(b);
            case "center" -> "Center: " + centerText(b);
            case "damage" -> "Damage: " + b.getDamage();
            case "tick" -> "Tick: every " + b.getDamageTick() + " ticks";
            case "color" -> "Color: " + hex(b.getColor());
            default -> b.getRemainingMoveTime() == 0
                ? "Not moving, center is " + num(b.getOriginX()) + ", " + num(b.getOriginZ())
                : "Heading to " + num(b.getTargetOriginX()) + ", " + num(b.getTargetOriginZ()) + ", arriving in " + duration(b.getRemainingMoveTime());
        });
    }
}
