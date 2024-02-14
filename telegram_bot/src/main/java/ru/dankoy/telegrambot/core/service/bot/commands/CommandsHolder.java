package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Getter
public class CommandsHolder {

    private final Map<Locale, List<BotCommand>> commands = new HashMap<>();

    public void addCommand(Locale locale, BotCommand botCommand) {

        if (commands.containsKey(locale)) {

            commands.get(locale).add(botCommand);

        } else {
            List<BotCommand> commandList = new ArrayList<>();
            commandList.add(botCommand);
            commands.put(locale, commandList);
        }
    }

    public void addCommands(Locale locale, List<BotCommand> botCommands) {

        if (commands.containsKey(locale)) {

            commands.get(locale).addAll(botCommands);

        } else {
            commands.put(locale, botCommands);
        }
    }
}
