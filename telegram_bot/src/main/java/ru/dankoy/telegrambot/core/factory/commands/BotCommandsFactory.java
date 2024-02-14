package ru.dankoy.telegrambot.core.factory.commands;

import java.util.List;
import java.util.Locale;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;

public interface BotCommandsFactory {

  LocalisationService localisationService();

  Locale locale();

  List<BotCommand> allKnownCommands();

  BotCommand communitiesCommand();

  BotCommand helpCommand();

  BotCommand mySubscriptionsCommand();

  BotCommand startCommand();

  BotCommand subscribeCommand();

  BotCommand unsubscribeCommand();

  BotCommand ordersCommand();
}
