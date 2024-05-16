package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.dankoy.telegrambot.config.bot.properties.BotNameProvider;
import ru.dankoy.telegrambot.config.bot.properties.LocaleConfig;
import ru.dankoy.telegrambot.core.service.localeprovider.LocaleProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandsExtractorServiceImpl implements CommandsExtractorService {

  private final LocaleProvider localeProvider;

  private final CommandsHolder commandsHolder;

  private final BotNameProvider botNameProvider;

  private final LocaleConfig localeConfig;

  @Override
  public Optional<BotCommand> getCommand(String localeString, String commandText) {

    var locale = localeProvider.getLocale(localeString);

    // убираем все лишнее из команды
    var commandWord = commandText.split(" ")[0];

    Map<Locale, List<BotCommand>> commands = commandsHolder.getCommands();

    var optional = Optional.ofNullable(commands.get(locale));

    var commandsByLocale =
        optional.orElse(commandsHolder.getCommands().get(localeConfig.getDefaultLocale()));

    for (var command : commandsByLocale) {
      var groupCommand = "/" + command.getCommand() + getGroupChatBotName();
      var singleChatCommand = "/" + command.getCommand();
      if (groupCommand.equals(commandWord)) {
        return Optional.of(command);
      }
      if (singleChatCommand.equals(commandWord)) {
        return Optional.of(command);
      }
    }
    log.warn("Command {} not found", commandText);

    return Optional.empty();
  }

  @Override
  public BotCommand getCommand(Class<? extends BotCommand> commandClass) {

    var locale = localeConfig.getDefaultLocale();

    var commandsByLocale = commandsHolder.getCommands().get(locale);

    for (var command : commandsByLocale) {
      if (commandClass.equals(command.getClass())) {
        return command;
      }
    }

    throw new IllegalArgumentException(String.valueOf(commandClass));
  }

  private String getGroupChatBotName() {
    return "@" + botNameProvider.getName();
  }
}
