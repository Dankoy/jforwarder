package ru.dankoy.telegrambot.core.service.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Slf4j
public class SubscribeCommand extends BotCommand {

  public SubscribeCommand(String command, String description) {
    super(command, description);
  }
}
