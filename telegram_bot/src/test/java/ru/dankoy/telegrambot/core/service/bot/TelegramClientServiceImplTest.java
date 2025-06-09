package ru.dankoy.telegrambot.core.service.bot;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfiguration;
import ru.dankoy.telegrambot.config.bot.configuration.botflow.BotConfigurationImpl;
import ru.dankoy.telegrambot.config.bot.properties.TelegramBotProperties;
import ru.dankoy.telegrambot.core.exceptions.BotSendMessageException;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsHolder;
import ru.dankoy.telegrambot.core.service.chat.SubscriptionsHolderChatService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;

@ExtendWith(MockitoExtension.class)
class TelegramClientServiceImplTest {

  @Mock private TelegramClient telegramClient;
  @Mock private SubscriptionsHolderChatService subscriptionsHolderChatService;

  @Mock private TelegramChatService telegramChatService;

  @InjectMocks private TelegramClientServiceImpl telegramClientService;

  private BotConfiguration botConfiguration;

  @BeforeEach
  void setup() {

    var fullBotProperties =
        new TelegramBotProperties(
            "name", "token", new Locale[] {Locale.US, Locale.GERMAN}, Locale.US);

    var commandsHolder = new CommandsHolder();

    commandsHolder.addCommands(
        Locale.US, Collections.singletonList(new BotCommand("test", "descr")));

    commandsHolder.addCommands(Locale.GERMAN, Collections.emptyList());

    botConfiguration = new BotConfigurationImpl(fullBotProperties, commandsHolder);
  }

  @Test
  void tegisterCommandsTest_expectCorrectResponse() throws TelegramApiException {
    telegramClientService.registerCommands(botConfiguration);

    verify(telegramClient, times(botConfiguration.commandsHolder().getCommands().size()))
        .execute(any(SetMyCommands.class));
  }

  @Test
  void registerCommandsTest_exceptionIsThrownWhenTelegramApiFails() throws TelegramApiException {
    TelegramApiException exception = new TelegramApiException("test");
    when(telegramClient.execute(any(SetMyCommands.class))).thenThrow(exception);

    assertThrows(
        TelegramApiException.class, () -> telegramClientService.registerCommands(botConfiguration));
  }

  @Test
  void deregisterCommandsTest_expectCorrectResponse() throws TelegramApiException {
    telegramClientService.deregisterCommands(botConfiguration);

    verify(telegramClient, times(botConfiguration.commandsHolder().getCommands().size() + 1))
        .execute(any(DeleteMyCommands.class));
  }

  @Test
  void deregisterCommandsTest_ExceptionIsThrownWhenTelegramApiFails() throws TelegramApiException {
    TelegramApiException exception = new TelegramApiException("test");
    when(telegramClient.execute(any(SetMyCommands.class))).thenThrow(exception);

    assertThrows(
        TelegramApiException.class, () -> telegramClientService.registerCommands(botConfiguration));
  }

  @Test
  void testSendMessage_Success() throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();

    telegramClientService.sendMessage(sendMessage);

    verify(telegramClient).execute(sendMessage);
  }

  @Test
  void testSendMessage_expectsGenericException() throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();

    when(telegramClient.execute(any(SendMessage.class))).thenThrow(new TelegramApiException("t"));

    assertThatThrownBy(() -> telegramClientService.sendMessage(sendMessage))
        .isInstanceOf(BotSendMessageException.class);
  }

  @Test
  void testSendMessage_expectsTelegramApiRequestException() throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();

    var apiResponse = ApiResponse.builder().errorCode(000).build();

    var ex = new TelegramApiRequestException("err", apiResponse);

    when(telegramClient.execute(any(SendMessage.class))).thenThrow(ex);

    assertThatThrownBy(() -> telegramClientService.sendMessage(sendMessage))
        .isInstanceOf(BotSendMessageException.class);
  }

  @Test
  void testSendMessage_expectsTelegramApiRequestException400AndBlockChat()
      throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();
    var apiResponse = ApiResponse.builder().errorCode(400).build();
    var ex = new TelegramApiRequestException("err", apiResponse);

    var chat = ru.dankoy.telegrambot.core.domain.Chat.builder().active(true).build();

    var chat2 = new ru.dankoy.telegrambot.core.domain.ChatWithUUID();
    chat2.setActive(true);

    when(telegramChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat2);
    when(subscriptionsHolderChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat);

    when(telegramClient.execute(any(SendMessage.class))).thenThrow(ex);

    telegramClientService.sendMessage(sendMessage);

    verify(telegramClient, times(1)).execute(sendMessage);
    verify(telegramChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());
    verify(subscriptionsHolderChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());

    verify(telegramChatService, times(1)).update(chat2);
    verify(subscriptionsHolderChatService, times(1)).update(chat);
  }

  @Test
  void testSendMessage_expectsTelegramApiRequestException400ButChatIsNotActive()
      throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();
    var apiResponse = ApiResponse.builder().errorCode(400).build();
    var ex = new TelegramApiRequestException("err", apiResponse);

    var chat = ru.dankoy.telegrambot.core.domain.Chat.builder().build();

    var chat2 = new ru.dankoy.telegrambot.core.domain.ChatWithUUID();

    when(telegramChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat2);
    when(subscriptionsHolderChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat);

    when(telegramClient.execute(any(SendMessage.class))).thenThrow(ex);

    telegramClientService.sendMessage(sendMessage);

    verify(telegramClient, times(1)).execute(sendMessage);
    verify(telegramChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());
    verify(subscriptionsHolderChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());
  }

  @Test
  void testSendMessage_expectsTelegramApiRequestException403AndBlockChat()
      throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();
    var apiResponse = ApiResponse.builder().errorCode(403).build();
    var ex = new TelegramApiRequestException("any message", apiResponse);

    var chat = ru.dankoy.telegrambot.core.domain.Chat.builder().active(true).build();

    var chat2 = new ru.dankoy.telegrambot.core.domain.ChatWithUUID();
    chat2.setActive(true);

    when(telegramChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat2);
    when(subscriptionsHolderChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat);

    when(telegramClient.execute(any(SendMessage.class))).thenThrow(ex);

    telegramClientService.sendMessage(sendMessage);

    verify(telegramClient, times(1)).execute(sendMessage);
    verify(telegramChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());
    verify(subscriptionsHolderChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());

    verify(telegramChatService, times(1)).update(chat2);
    verify(subscriptionsHolderChatService, times(1)).update(chat);
  }

  @Test
  void testSendMessage_expectsTelegramApiRequestExceptionTopicClosedAndBlockChat()
      throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();
    var apiResponse = ApiResponse.builder().errorCode(000).build();
    var ex =
        new TelegramApiRequestException(
            TelegramBotApiErrorMessages.TOPIC_CLOSED.getMessage(), apiResponse);

    var chat = ru.dankoy.telegrambot.core.domain.Chat.builder().active(true).build();

    var chat2 = new ru.dankoy.telegrambot.core.domain.ChatWithUUID();
    chat2.setActive(true);

    when(telegramChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat2);
    when(subscriptionsHolderChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat);

    when(telegramClient.execute(any(SendMessage.class))).thenThrow(ex);

    telegramClientService.sendMessage(sendMessage);

    verify(telegramClient, times(1)).execute(sendMessage);
    verify(telegramChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());
    verify(subscriptionsHolderChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());

    verify(telegramChatService, times(1)).update(chat2);
    verify(subscriptionsHolderChatService, times(1)).update(chat);
  }

  @Test
  void testSendMessage_expectsTelegramApiRequestExceptionTopicDeletedAndBlockChat()
      throws TelegramApiException {

    SendMessage sendMessage =
        SendMessage.builder().chatId("123").messageThreadId(1).text("text").build();
    var apiResponse = ApiResponse.builder().errorCode(000).build();
    var ex =
        new TelegramApiRequestException(
            TelegramBotApiErrorMessages.TOPIC_DELETED.getMessage(), apiResponse);

    var chat = ru.dankoy.telegrambot.core.domain.Chat.builder().active(true).build();

    var chat2 = new ru.dankoy.telegrambot.core.domain.ChatWithUUID();
    chat2.setActive(true);

    when(telegramChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat2);
    when(subscriptionsHolderChatService.getChatByIdAndMessageThreadId(123, 1)).thenReturn(chat);

    when(telegramClient.execute(any(SendMessage.class))).thenThrow(ex);

    telegramClientService.sendMessage(sendMessage);

    verify(telegramClient, times(1)).execute(sendMessage);
    verify(telegramChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());
    verify(subscriptionsHolderChatService, times(1))
        .getChatByIdAndMessageThreadId(
            Long.parseLong(sendMessage.getChatId()), sendMessage.getMessageThreadId());

    verify(telegramChatService, times(1)).update(chat2);
    verify(subscriptionsHolderChatService, times(1)).update(chat);
  }
}
