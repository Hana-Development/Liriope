package org.hanayori.liriope.command;

import org.hanayori.liriope.api.ILiriopeAPI;
import org.hanayori.liriope.api.border.BorderType;
import org.hanayori.liriope.api.border.IBorder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.hanayori.aster.command.CommandException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BorderCommands {

    private BorderCommands() {
        throw new IllegalStateException("Utility class cannot be initialized");
    }

    public static World world(CommandSender sender) {
        if (sender instanceof Entity entity) {
            return entity.getWorld();
        }
        if (sender instanceof BlockCommandSender block) {
            return block.getBlock().getWorld();
        }
        return Bukkit.getWorlds().getFirst();
    }

    public static IBorder border(ILiriopeAPI api, CommandSender sender) {
        return api.getBorder(world(sender));
    }

    public static IBorder existing(ILiriopeAPI api, World world) {
        if (!api.hasBorder(world)) {
            throw new CommandException("There is no border in " + world.getName());
        }
        return api.getBorder(world);
    }

    public static void atLeast(double value, double min) {
        if (value < min) {
            throw new CommandException("Value must be at least " + num(min));
        }
    }

    public static int seconds(Duration time) {
        return time == null ? 0 : Math.toIntExact(time.toSeconds());
    }

    public static String over(int seconds) {
        return seconds > 0 ? " over " + duration(seconds) : "";
    }

    private static double size(IBorder border, double radius) {
        return border.getBorderType() == BorderType.SQUARE ? radius * 2 : radius;
    }

    public static String sizeText(IBorder border) {
        double now = size(border, border.getCurrentRadius());
        String text = border.getBorderType() == BorderType.SQUARE ? num(now) + "x" + num(now) : "radius " + num(now);
        if (border.getRemainingTime() > 0) {
            text += ", will be " + num(size(border, border.getTargetRadius())) + " block(s) "
                + (border.getBorderType() == BorderType.SQUARE ? "wide" : "in radius") + " in " + duration(border.getRemainingTime());
        }
        return text;
    }

    public static String centerText(IBorder border) {
        String text = num(border.getOriginX()) + ", " + num(border.getOriginZ());
        if (border.getRemainingMoveTime() > 0) {
            text += ", will be at " + num(border.getTargetOriginX()) + ", " + num(border.getTargetOriginZ()) + " in " + duration(border.getRemainingMoveTime());
        }
        return text;
    }

    public static String duration(int seconds) {
        int[] values = {seconds / 86400, seconds / 3600 % 24, seconds / 60 % 60, seconds % 60};
        String[] units = {"day", "hour", "minute", "second"};
        List<String> parts = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                parts.add(values[i] + " " + units[i] + (values[i] == 1 ? "" : "s"));
            }
        }
        return parts.isEmpty() ? "0 seconds" : String.join(", ", parts);
    }

    public static String num(double value) {
        return value == Math.rint(value) ? String.valueOf((long) value) : String.format(Locale.ROOT, "%.2f", value);
    }

    public static String hex(Color color) {
        return String.format("#%06X", color.asRGB());
    }

    public static String cap(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    public static void reply(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message, NamedTextColor.GREEN));
    }
}
