package ru.dankoy.telegrambot.core.service.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

@Slf4j
@Component
public class UnsubscribeFromCommunityCommand extends BotCommand {

  public UnsubscribeFromCommunityCommand() {
    super("unsubscribe_from_community", "unsubscribe from chosen community");
  }

}
