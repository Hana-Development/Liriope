package org.hanayori.liriope.command.sub;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static org.hanayori.liriope.command.BorderCommands.atLeast;
import static org.hanayori.liriope.command.BorderCommands.border;
import static org.hanayori.liriope.command.BorderCommands.reply;

@Injectable
@Command({"worldborder", "border"})
@CommandPermission("liriope.command.worldborder")
public record ParticleSizeCommand(ILiriopeAPI api) {

    @Subcommand("particle_size")
    public void execute(CommandSender sender, float value) {
        atLeast(value, 0.01);
        border(this.api, sender).setParticleSize(value);
        reply(sender, "Border particle size set to " + value);
    }
}
