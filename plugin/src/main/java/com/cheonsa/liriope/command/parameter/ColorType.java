package com.cheonsa.liriope.command.parameter;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.util.Locale;

public final class ColorType implements ParameterType<BukkitCommandActor, Color> {

    @Override
    public Color parse(MutableStringStream input, ExecutionContext<BukkitCommandActor> context) {
        String text = input.readUnquotedString();
        NamedTextColor named = NamedTextColor.NAMES.value(text.toLowerCase(Locale.ROOT));
        if (named != null) {
            return Color.fromRGB(named.value());
        }
        String hex = text.startsWith("#") ? text.substring(1) : text;
        if (!hex.matches("[0-9a-fA-F]{6}")) {
            throw new CommandErrorException("Invalid color, use a name or hex like FF0000");
        }
        return Color.fromRGB(Integer.parseInt(hex, 16));
    }

    @Override
    public SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return context -> NamedTextColor.NAMES.keys();
    }
}
