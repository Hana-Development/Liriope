package com.cheonsa.liriope.command;

import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.border.BorderType;
import com.cheonsa.liriope.api.border.IBorder;
import com.cheonsa.liriope.command.sub.BandHeightCommand;
import com.cheonsa.liriope.command.sub.CenterCommand;
import com.cheonsa.liriope.command.sub.ColorCommand;
import com.cheonsa.liriope.command.sub.DamageCommand;
import com.cheonsa.liriope.command.sub.DrawIntervalCommand;
import com.cheonsa.liriope.command.sub.GetCommand;
import com.cheonsa.liriope.command.sub.ParticleSizeCommand;
import com.cheonsa.liriope.command.sub.PointSpacingCommand;
import com.cheonsa.liriope.command.sub.RemoveCommand;
import com.cheonsa.liriope.command.sub.RowSpacingCommand;
import com.cheonsa.liriope.command.sub.SetCommand;
import com.cheonsa.liriope.command.sub.StatusCommand;
import com.cheonsa.liriope.command.sub.StopCommand;
import com.cheonsa.liriope.command.sub.TickCommand;
import com.cheonsa.liriope.command.sub.TpCenterCommand;
import com.cheonsa.liriope.command.sub.TypeCommand;
import com.cheonsa.liriope.command.sub.ViewDistanceCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BorderCommands {

    private BorderCommands() {
        throw new IllegalStateException("Utility class cannot be initialized");
    }

    public static Object[] all(ILiriopeAPI api) {
        return new Object[]{
            new TypeCommand(api), new SetCommand(api), new CenterCommand(api), new StopCommand(api),
            new ColorCommand(api), new DamageCommand(api), new TickCommand(api),
            new ParticleSizeCommand(api), new ViewDistanceCommand(api), new PointSpacingCommand(api),
            new BandHeightCommand(api), new RowSpacingCommand(api), new DrawIntervalCommand(api),
            new StatusCommand(api), new GetCommand(api), new TpCenterCommand(api), new RemoveCommand(api)
        };
    }

    public static World world(BukkitCommandActor actor) {
        CommandSender sender = actor.sender();
        if (sender instanceof Entity entity) {
            return entity.getWorld();
        }
        if (sender instanceof BlockCommandSender block) {
            return block.getBlock().getWorld();
        }
        return Bukkit.getWorlds().getFirst();
    }

    public static IBorder border(ILiriopeAPI api, BukkitCommandActor actor) {
        return api.getBorder(world(actor));
    }

    public static IBorder existing(ILiriopeAPI api, World world) {
        if (!api.hasBorder(world)) {
            throw new CommandErrorException("There is no border in " + world.getName());
        }
        return api.getBorder(world);
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

    public static void reply(BukkitCommandActor actor, String message) {
        actor.sender().sendMessage(Component.text(message, NamedTextColor.GREEN));
    }
}
