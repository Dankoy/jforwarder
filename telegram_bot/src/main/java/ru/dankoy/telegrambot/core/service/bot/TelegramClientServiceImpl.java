package ru.dankoy.telegrambot.core.service.bot;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfiguration;
import ru.dankoy.telegrambot.core.exceptions.BotSendMessageException;
import ru.dankoy.telegrambot.core.service.chat.SubscriptionsHolderChatService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

@Slf4j
@RequiredArgsConstructor
public class TelegramClientServiceImpl implements TelegramClientService {

  private final TelegramClient telegramClient;

  private final TelegramChatService telegramChatService;

  private final SubscriptionsHolderChatService subscriptionsHolderChatService;

  @Override
  public void sendMessage(SendMessage sendMessage) {
    send(sendMessage);
  }

  private void send(SendMessage sendMessage) {
    try {
      telegramClient.execute(sendMessage);
      log.info(
          "Message sent to '{}'-{} with message '{}'",
          sendMessage.getChatId(),
          sendMessage.getMessageThreadId(),
          StringUtils.normalizeSpace(sendMessage.getText()));
    } catch (TelegramApiRequestException e) {
      if (e.getErrorCode() == 403
          || e.getMessage().contains(TelegramBotApiErrorMessages.TOPIC_CLOSED.getMessage())
          || e.getMessage().contains(TelegramBotApiErrorMessages.TOPIC_DELETED.getMessage())) {

        log.warn("User blocked bot. Make it not active");

        blockChat(sendMessage);

      } else if (e.getErrorCode() == 400) {

        log.warn("User deleted chat. Disabling it.");

        blockChat(sendMessage);

      } else {
        // some other exception in api
        log.error("Something went wrong: {}", e.getMessage());
        throw new BotSendMessageException(e.getMessage(), e);
      }
    } catch (TelegramApiException e) {
      log.error("Error sending message - {}", e.getMessage());
      throw new BotSendMessageException(e.getMessage(), e);
    }
  }

  private void blockChat(SendMessage sendMessage) {
    var found =
        telegramChatService.getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());

    var found2 =
        subscriptionsHolderChatService.getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());

    if (found.isActive()) {
      found.setActive(false);
      found2.setActive(false);
      telegramChatService.update(found);
      subscriptionsHolderChatService.update(found2);
    }
  }

  @Override
  public void registerCommands(BotConfiguration botConfiguration) throws TelegramApiException {
    for (Entry<Locale, List<BotCommand>> entry :
        botConfiguration.commandsHolder().getCommands().entrySet()) {

      if (entry.getKey().equals(botConfiguration.fullBotProperties().getDefaultLocale())) {

        var setMyCommands =
            SetMyCommands.builder()
                .commands(entry.getValue())
                .scope(new BotCommandScopeDefault())
                .build();

        telegramClient.execute(setMyCommands);
      } else {
        var setMyCommands =
            new SetMyCommands(
                entry.getValue(), new BotCommandScopeDefault(), entry.getKey().getLanguage());

        telegramClient.execute(setMyCommands);
      }
    }
  }

  @Override
  public void deregisterCommands(BotConfiguration botConfiguration) throws TelegramApiException {
    // delete for default locale (without locale)

    var deleteMyCommands = new DeleteMyCommands();
    deleteMyCommands.setScope(new BotCommandScopeDefault());
    telegramClient.execute(deleteMyCommands);

    // then delete for every known locale
    for (Entry<Locale, List<BotCommand>> entry :
        botConfiguration.commandsHolder().getCommands().entrySet()) {

      deleteMyCommands =
          new DeleteMyCommands(new BotCommandScopeDefault(), entry.getKey().getLanguage());

      telegramClient.execute(deleteMyCommands);
    }
  }
}
