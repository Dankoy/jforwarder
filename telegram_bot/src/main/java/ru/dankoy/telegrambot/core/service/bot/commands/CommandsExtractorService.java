package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

public interface CommandsExtractorService {

  Optional<BotCommand> getCommand(String locale, String commandText);

  BotCommand getCommand(Class<? extends BotCommand> commandClass);
}
