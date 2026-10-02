package com.cheonsa.liriope.command.parameter;

import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DurationType implements ParameterType<BukkitCommandActor, Duration> {

    private static final Pattern FORMAT = Pattern.compile("(\\d+[dhms])+");
    private static final Pattern PART = Pattern.compile("(\\d+)([dhms])");
    private static final String INVALID = "Invalid duration, use seconds or something like 1h30m10s";

    @Override
    public Duration parse(MutableStringStream input, ExecutionContext<BukkitCommandActor> context) {
        String text = input.readUnquotedString().toLowerCase(Locale.ROOT);
        try {
            if (!text.isEmpty() && text.chars().allMatch(Character::isDigit)) {
                return Duration.ofSeconds(Integer.parseInt(text));
            }
            if (!FORMAT.matcher(text).matches()) {
                throw new CommandErrorException(INVALID);
            }
            long seconds = 0;
            Matcher matcher = PART.matcher(text);
            while (matcher.find()) {
                seconds += Long.parseLong(matcher.group(1)) * switch (matcher.group(2)) {
                    case "d" -> 86400;
                    case "h" -> 3600;
                    case "m" -> 60;
                    default -> 1;
                };
            }
            return Duration.ofSeconds(Math.toIntExact(seconds));
        } catch (NumberFormatException | ArithmeticException e) {
            throw new CommandErrorException(INVALID);
        }
    }

    @Override
    public SuggestionProvider<BukkitCommandActor> defaultSuggestions() {
        return context -> {
            String source = context.input().source();
            String typed = source.substring(source.lastIndexOf(' ') + 1).toLowerCase(Locale.ROOT);
            if (typed.isEmpty()) {
                return List.of("30s", "1m", "5m", "10m", "1h");
            }
            if (Character.isDigit(typed.charAt(typed.length() - 1))) {
                return List.of(typed + "s", typed + "m", typed + "h", typed + "d");
            }
            return List.of();
        };
    }
}
