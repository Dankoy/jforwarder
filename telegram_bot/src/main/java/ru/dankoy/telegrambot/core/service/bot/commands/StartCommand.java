package ru.dankoy.telegrambot.core.service.bot.commands;

import feign.FeignException.NotFound;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.ChatWithUUID;
import ru.dankoy.telegrambot.core.service.chat.SubscriptionsHolderChatService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

@Slf4j
public class StartCommand extends BotCommand {

  private final transient TelegramChatService telegramChatService;
  private final SubscriptionsHolderChatService subscriptionsHolderChatService;

  public StartCommand(
      String command,
      String description,
      TelegramChatService telegramChatService,
      SubscriptionsHolderChatService subscriptionsHolderChatService) {
    super(command, description);
    this.telegramChatService = telegramChatService;
    this.subscriptionsHolderChatService = subscriptionsHolderChatService;
  }

  public Message start(Message inputMessage) {

    var tChat = inputMessage.getChat();
    var messageThreadId = inputMessage.getMessageThreadId();

    try {

      var found = telegramChatService.getChatByIdAndMessageThreadId(tChat.getId(), messageThreadId);
      var found2 =
          subscriptionsHolderChatService.getChatByIdAndMessageThreadId(
              tChat.getId(), messageThreadId);
      log.info("chat - {}", found);

      found.setActive(true);
      found2.setActive(true);
      telegramChatService.update(found);
      subscriptionsHolderChatService.update(found2);

    } catch (NotFound e) {

      var newChat =
          new ChatWithUUID(
              null,
              tChat.getId(),
              tChat.getType(),
              tChat.getTitle(),
              tChat.getFirstName(),
              tChat.getLastName(),
              tChat.getUserName(),
              true,
              messageThreadId);

      var newChat2 =
          new Chat(
              0L,
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
      subscriptionsHolderChatService.createChat(newChat2);
    }

    return inputMessage;
  }
}
