package com.cheonsa.liriope.command.parameter;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.CommandException;
import org.hanayori.aster.command.ParameterType;
import org.hanayori.aster.component.Injectable;

import java.util.Collection;
import java.util.Locale;

@Injectable
public final class ColorType implements ParameterType<Color> {

    @Override
    public Color parse(CommandSender sender, String text) {
        NamedTextColor named = NamedTextColor.NAMES.value(text.toLowerCase(Locale.ROOT));
        if (named != null) {
            return Color.fromRGB(named.value());
        }
        String hex = text.startsWith("#") ? text.substring(1) : text;
        if (!hex.matches("[0-9a-fA-F]{6}")) {
            throw new CommandException("Invalid color, use a name or hex like FF0000");
        }
        return Color.fromRGB(Integer.parseInt(hex, 16));
    }

    @Override
    public Collection<String> suggest(CommandSender sender) {
        return NamedTextColor.NAMES.keys();
    }
}
