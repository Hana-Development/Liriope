package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.BorderType;
import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandConfirmation;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Optional;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import java.time.Duration;

import static com.cheonsa.liriope.command.BorderCommands.atLeast;
import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.num;
import static com.cheonsa.liriope.command.BorderCommands.over;
import static com.cheonsa.liriope.command.BorderCommands.reply;
import static com.cheonsa.liriope.command.BorderCommands.seconds;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record SetCommand(ILiriopeAPI api) {

    @Subcommand("set")
    @CommandConfirmation(message = "Run the command again within 5s to confirm.")
    public void execute(CommandSender sender, double size, @Optional Duration time) {
        atLeast(size, 0);
        IBorder b = border(this.api, sender);
        boolean square = b.getBorderType() == BorderType.SQUARE;
        int seconds = seconds(time);
        b.setRadius(square ? size / 2 : size, seconds);
        reply(sender, "Border " + (square ? "width" : "radius") + " set to " + num(size) + over(seconds));
    }
}
