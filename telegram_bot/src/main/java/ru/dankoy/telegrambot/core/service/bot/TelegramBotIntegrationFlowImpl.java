package ru.dankoy.telegrambot.core.service.bot;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfiguration;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.exceptions.BotSendMessageException;
import ru.dankoy.telegrambot.core.gateway.BotMessageGateway;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotIntegrationFlowImpl extends TelegramLongPollingBot implements TelegramBot {

  private final String botName;

  private final TelegramChatService telegramChatService;

  private final BotMessageGateway botMessageGateway;

  public TelegramBotIntegrationFlowImpl(BotConfiguration botConfiguration) {

    super(botConfiguration.fullBotProperties().getToken());

    this.botName = botConfiguration.fullBotProperties().getName();
    this.telegramChatService = botConfiguration.telegramChatService();
    this.botMessageGateway = botConfiguration.botMessageGateway();

    try {

      unregisterCommands(botConfiguration);
      registerCommands(botConfiguration);

    } catch (TelegramApiException e) {
      log.error(e.getMessage());
      throw new BotException("Exception while bot initialization", e);
    }
  }

  @Override
  public void onUpdateReceived(Update update) {

    if (update.hasMessage()) {

      Message message = update.getMessage();

      if (update.getMessage().hasText()) {

        log.info(
            "Received message from '{}' with text '{}'",
            message.getChat().getId(),
            message.getText());

        botMessageGateway.process(message);
      }
    }
  }

  @Override
  public String getBotUsername() {
    return botName;
  }

  private void registerCommands(BotConfiguration botConfiguration) throws TelegramApiException {

    for (Entry<Locale, List<BotCommand>> entry :
        botConfiguration.commandsHolder().getCommands().entrySet()) {

      if (entry.getKey().equals(botConfiguration.fullBotProperties().getDefaultLocale())) {

        var setMyCommands = new SetMyCommands();
        setMyCommands.setScope(new BotCommandScopeDefault());
        setMyCommands.setCommands(entry.getValue());

        this.execute(setMyCommands);
      } else {
        var setMyCommands =
            new SetMyCommands(
                entry.getValue(), new BotCommandScopeDefault(), entry.getKey().getLanguage());

        this.execute(setMyCommands);
      }
    }
  }

  private void unregisterCommands(BotConfiguration botConfiguration) throws TelegramApiException {

    // delete for default locale (without locale)

    var deleteMyCommands = new DeleteMyCommands();
    deleteMyCommands.setScope(new BotCommandScopeDefault());
    this.execute(deleteMyCommands);

    // then delete for every known locale
    for (Entry<Locale, List<BotCommand>> entry :
        botConfiguration.commandsHolder().getCommands().entrySet()) {

      deleteMyCommands =
          new DeleteMyCommands(new BotCommandScopeDefault(), entry.getKey().getLanguage());

      this.execute(deleteMyCommands);
    }
  }

  @Override
  public void sendMessage(SendMessage sendMessage) {
    send(sendMessage);
  }

  // the send message with chat service can't be integrated flawlessly in spring integration.
  // I can throw here 'exceptions' which are going to propagate in errorChannel (are they?) and then
  // do logic in services.
  // But then bot is going to be highly coupled with spring integration, which is not that good
  private void send(SendMessage sendMessage) {
    try {
      execute(sendMessage);
      log.info(
          "Message sent to '{}'-{} with message '{}'",
          sendMessage.getChatId(),
          sendMessage.getMessageThreadId(),
          StringUtils.normalizeSpace(sendMessage.getText()));
    } catch (TelegramApiRequestException e) {
      if (e.getErrorCode() == 403
          || e.getMessage().contains(TelegramBotApiErrorMessages.TOPIC_CLOSED.getMessage())) {

        log.warn("User blocked bot. Make it not active");

        var found =
            telegramChatService.getChatByIdAndMessageThreadId(
                Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());

        if (found.isActive()) {
          found.setActive(false);
          telegramChatService.update(found);
        }
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
}
