package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Optional;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import java.time.Duration;

import static org.hanayori.liriope.command.BorderCommands.border;
import static org.hanayori.liriope.command.BorderCommands.num;
import static org.hanayori.liriope.command.BorderCommands.over;
import static org.hanayori.liriope.command.BorderCommands.reply;
import static org.hanayori.liriope.command.BorderCommands.seconds;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record CenterCommand(ILiriopeAPI api) {

    // ponytail: plain x z, no ~ relative coords; add a column-position ParameterType if needed
    @Subcommand("center")
    public void execute(CommandSender sender, double x, double z, @Optional Duration time) {
        int seconds = seconds(time);
        border(this.api, sender).setOrigin(x, z, seconds);
        reply(sender, "Border center set to " + num(x) + ", " + num(z) + over(seconds));
    }
}
