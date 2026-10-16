package com.cheonsa.liriope.command.sub;

import com.cheonsa.liriope.api.ILiriopeAPI;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.annotation.Command;
import org.hanayori.aster.command.annotation.CommandPermission;
import org.hanayori.aster.command.annotation.Subcommand;
import org.hanayori.aster.component.Injectable;

import static com.cheonsa.liriope.command.BorderCommands.atLeast;
import static com.cheonsa.liriope.command.BorderCommands.border;
import static com.cheonsa.liriope.command.BorderCommands.reply;

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
