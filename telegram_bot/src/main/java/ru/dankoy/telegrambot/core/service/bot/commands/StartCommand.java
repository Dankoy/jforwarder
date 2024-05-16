package ru.dankoy.telegrambot.core.service.bot.commands;

import feign.FeignException.NotFound;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

@Slf4j
public class StartCommand extends BotCommand {

  private final transient TelegramChatService telegramChatService;

  public StartCommand(String command, String description, TelegramChatService telegramChatService) {
    super(command, description);
    this.telegramChatService = telegramChatService;
  }

  public Message start(Message inputMessage) {

    var tChat = inputMessage.getChat();
    var messageThreadId = inputMessage.getMessageThreadId();

    try {

      var found = telegramChatService.getChatByIdAndMessageThreadId(tChat.getId(), messageThreadId);
      log.info("chat - {}", found);
      found.setActive(true);
      telegramChatService.update(found);

    } catch (NotFound e) {

      var newChat =
          new Chat(
              0,
              tChat.getId(),
              tChat.getType(),
              tChat.getTitle(),
              tChat.getFirstName(),
              tChat.getLastName(),
              tChat.getUserName(),
              true,
              messageThreadId);
      log.info("New chat to create - {}", newChat);
      telegramChatService.createChat(newChat);
    }

    return inputMessage;
  }
}
