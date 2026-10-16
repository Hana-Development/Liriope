package org.hanayori.liriope.command.parameter;

import org.bukkit.command.CommandSender;
import org.hanayori.aster.command.CommandException;
import org.hanayori.aster.command.ParameterType;
import org.hanayori.aster.component.Injectable;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Injectable
public final class DurationType implements ParameterType<Duration> {

    private static final Pattern FORMAT = Pattern.compile("(\\d+[dhms])+");
    private static final Pattern PART = Pattern.compile("(\\d+)([dhms])");
    private static final String INVALID = "Invalid duration, use seconds or something like 1h30m10s";

    @Override
    public Duration parse(CommandSender sender, String input) {
        String text = input.toLowerCase(Locale.ROOT);
        try {
            if (!text.isEmpty() && text.chars().allMatch(Character::isDigit)) {
                return Duration.ofSeconds(Integer.parseInt(text));
            }
            if (!FORMAT.matcher(text).matches()) {
                throw new CommandException(INVALID);
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
            throw new CommandException(INVALID);
        }
    }

    // ponytail: static list, Aster's suggest() doesn't see the typed input so no "5" -> "5s/5m" completions
    @Override
    public Collection<String> suggest(CommandSender sender) {
        return List.of("30s", "1m", "5m", "10m", "1h");
    }
}
