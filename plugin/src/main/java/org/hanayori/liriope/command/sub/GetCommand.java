package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.hanayori.liriope.api.border.BorderType;
import org.hanayori.liriope.api.border.IBorder;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.CommandException;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.command.annotation.Suggest;
import org.hanayori.aster.component.Injectable;

import static org.hanayori.liriope.command.BorderCommands.cap;
import static org.hanayori.liriope.command.BorderCommands.centerText;
import static org.hanayori.liriope.command.BorderCommands.duration;
import static org.hanayori.liriope.command.BorderCommands.existing;
import static org.hanayori.liriope.command.BorderCommands.hex;
import static org.hanayori.liriope.command.BorderCommands.num;
import static org.hanayori.liriope.command.BorderCommands.reply;
import static org.hanayori.liriope.command.BorderCommands.sizeText;
import static org.hanayori.liriope.command.BorderCommands.world;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record GetCommand(ILiriopeAPI api) {

    @Subcommand("get")
    public void execute(CommandSender sender,
                        @Suggest({"radius", "size", "width", "length", "center", "damage", "tick", "color", "origin"}) String key) {
        IBorder b = existing(this.api, world(sender));
        boolean square = b.getBorderType() == BorderType.SQUARE;
        if (key.equals("radius") && square) {
            throw new CommandException("Radius only applies to round shapes");
        }
        if ((key.equals("width") || key.equals("length")) && !square) {
            throw new CommandException(cap(key) + " only applies to square shapes");
        }
        reply(sender, switch (key) {
            case "radius", "width", "length", "size" -> cap(key) + ": " + sizeText(b);
            case "center" -> "Center: " + centerText(b);
            case "damage" -> "Damage: " + b.getDamage();
            case "tick" -> "Tick: every " + b.getDamageTick() + " ticks";
            case "color" -> "Color: " + hex(b.getColor());
            case "origin" -> b.getRemainingMoveTime() == 0
                ? "Not moving, center is " + num(b.getOriginX()) + ", " + num(b.getOriginZ())
                : "Heading to " + num(b.getTargetOriginX()) + ", " + num(b.getTargetOriginZ()) + ", arriving in " + duration(b.getRemainingMoveTime());
            default -> throw new CommandException("Unknown key " + key);
        });
    }
}
