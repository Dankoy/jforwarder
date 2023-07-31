package ru.dankoy.telegrambot.core.service.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Slf4j
@Component
public class SubscribeToCommunityCommand extends BotCommand {

  public SubscribeToCommunityCommand() {
    super("subscribe_by_community",
        "subscribe to chosen community");
  }

}
