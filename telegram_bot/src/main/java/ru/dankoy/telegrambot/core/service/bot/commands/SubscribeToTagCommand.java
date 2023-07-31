package ru.dankoy.telegrambot.core.service.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Slf4j
@Component
public class SubscribeToTagCommand extends BotCommand {

  public SubscribeToTagCommand() {
    super("subscribe_by_tag",
        "subscribe to chosen tag");
  }

}
