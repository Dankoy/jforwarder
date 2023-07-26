package ru.dankoy.telegrambot.core.service.bot;

import feign.FeignException.Conflict;
import feign.FeignException.NotFound;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.dankoy.telegrambot.core.domain.message.SubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Community;
import ru.dankoy.telegrambot.core.domain.subscription.Subscription;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.subscription.SubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;


@Slf4j
@RequiredArgsConstructor
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {

  private final String botName;

  private final SubscriptionService subscriptionService;

  private final TelegramChatService telegramChatService;

  private final TemplateBuilder templateBuilder;

  private final CommunityService communityService;


  public TelegramBotImpl(
      String botName,
      String token,
      List<BotCommand> commands,
      SubscriptionService subscriptionService,
      TelegramChatService telegramChatService,
      TemplateBuilder templateBuilder,
      CommunityService communityService
  ) {
    super(token);
    this.botName = botName;
    this.subscriptionService = subscriptionService;
    this.telegramChatService = telegramChatService;
    this.templateBuilder = templateBuilder;
    this.communityService = communityService;

    try {
      this.execute(
          new SetMyCommands(commands,
              new BotCommandScopeDefault(),
              null)
      );
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void onUpdateReceived(Update update) {

    log.info("{}", update.getMessage().getChat());

    if (update.hasMessage()) {
      Message message = update.getMessage();

      SendMessage sendMessage = new SendMessage();

      sendMessage.setChatId(message.getChatId());
      sendMessage.setText("Bot is in development");

      botAnswerUtils(message);

      try {
        execute(sendMessage);
      } catch (TelegramApiException e) {
        log.error("Error message send - {}", e.getMessage());
      }

      log.info("Chat - {}", message.getChat());

    }

  }


  private void botAnswerUtils(Message inputMessage) {

    var messageText = inputMessage.getText();
    var chatId = inputMessage.getChat().getId();

    var startFromGroup = "/start" + getGroupChatBotName();
    var subsFromGroup = "/my_subscriptions" + getGroupChatBotName();
    var helpFromGroup = "/help" + getGroupChatBotName();
    var subscribeFromGroup = "/subscribe" + getGroupChatBotName();
    var unsubscribeFromGroup = "/unsubscribe" + getGroupChatBotName();
    var communitiesFromGroup = "/communities" + getGroupChatBotName();

    if (messageText.equals("/my_subscriptions") || messageText.equals(subsFromGroup)) {
      mySubscriptions(chatId);
    } else if (messageText.equals("/start") || messageText.equals(startFromGroup)) {
      start(inputMessage);
    } else if (messageText.equals("/help") || messageText.equals(helpFromGroup)) {
      help(inputMessage);
    } else if (messageText.startsWith("/subscribe") || messageText.startsWith(subscribeFromGroup)) {
      activateChat(inputMessage);
      subscribe(inputMessage);
    } else if (messageText.startsWith("/unsubscribe") || messageText.startsWith(
        unsubscribeFromGroup)) {
      unsubscribe(inputMessage);
    } else if (messageText.equals("/communities") || messageText.equals(communitiesFromGroup)) {
      communities(inputMessage);
    }
  }

  private void activateChat(Message message) {

    var tChat = message.getChat();

    var found = telegramChatService.getChatById(tChat.getId());
    log.info("Activate chat - {}", found);
    found.setActive(true);
    telegramChatService.update(found);

  }

  private void mySubscriptions(long chatId) {

    List<Subscription> subs = subscriptionService.getSubscriptionsByChatId(chatId);

    SendMessage message = new SendMessage();
    message.setChatId(chatId);
    message.setParseMode(ParseMode.MARKDOWN);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("subscriptions", subs);
    var text = templateBuilder.writeTemplate(templateData, "subscriptions.ftl");

    if (text.isEmpty()) {
      message.setText("You are not subscribed to anything");
    } else {
      message.setText(text);
    }

    send(message);
  }

  // create chat in db
  private void start(Message inputMessage) {

    var tChat = inputMessage.getChat();

    try {

      var found = telegramChatService.getChatById(inputMessage.getChatId());
      log.info("chat - {}", found);
      found.setActive(true);
      telegramChatService.update(found);

    } catch (NotFound e) {

      var newChat = new Chat(0,
          tChat.getId(),
          tChat.getType(),
          tChat.getTitle(),
          tChat.getFirstName(),
          tChat.getLastName(),
          tChat.getUserName(),
          true
      );
      log.info("New chat to create - {}", newChat);
      telegramChatService.createChat(newChat);

    }

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());
    message.setText(
        "Here you can subscribe to coub.com communities and receive updates directly in chat");

    send(message);
  }


  private void help(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());

    var text = templateBuilder.writeTemplate(new HashMap<>(), "help.ftl");
    message.setText(text);

    send(message);

  }

  private void subscribe(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());

    var messageText = inputMessage.getText();
    // find community and section

    String[] command = new String[0];
    try {

      command = parseCommand(messageText);

    } catch (BotException e) {
      message.setText(e.getMessage());
      send(message);
      return;
    }

    var communityName = command[1];
    var sectionName = command[2];

    try {

      var s = subscriptionService.subscribe(communityName, sectionName,
          inputMessage.getChat().getId());

      message.setText(String.format("Subscribed to %s %s", s.getCommunity().getName(),
          s.getSection().getName()));
      send(message);

    } catch (Conflict e) {
      message.setText(String.format("You are already subscribed to '%s - %s'",
          communityName,
          sectionName));
      send(message);
    } catch (Exception e) {
      message.setText("Something went wrong. Validate your command");
      send(message);
    }

  }

  private void unsubscribe(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());

    var messageText = inputMessage.getText();
    // find community and section

    String[] command = new String[0];
    try {

      command = parseCommand(messageText);

    } catch (BotException e) {
      message.setText(e.getMessage());
      send(message);
      return;
    }

    var communityName = command[1];
    var sectionName = command[2];

    try {
      subscriptionService.unsubscribe(communityName,
          sectionName,
          inputMessage.getChat().getId());

      message.setText(String.format("Unsubscribed from %s %s", communityName, sectionName));
      send(message);

    } catch (Exception e) {
      message.setText("Something went wrong. Validate your command");
      send(message);
    }

  }

  private void communities(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());
    message.setParseMode(ParseMode.MARKDOWN);
    List<Community> communities = communityService.getAll();

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communities", communities);

    var text = templateBuilder.writeTemplate(templateData, "communities.ftl");
    message.setText(text);

    send(message);

  }

  private void send(SendMessage sendMessage) {
    try {
      execute(sendMessage);
      log.info("Reply sent");
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
  }

  private String[] parseCommand(String messageText) {

    String[] command = messageText.split(" ");

    if (command.length != 3) {
      log.info("Command can't be parsed. {}", Arrays.asList(command));
      throw new BotException("Command should have coub community name and section name");
    } else {
      return command;
    }

  }

  @Override
  public String getBotUsername() {
    return botName;
  }


  private String getGroupChatBotName() {
    return "@" + botName;
  }

  @Override
  public void sendMessage(SubscriptionMessage message) {

    var communityName = message.community().getName();
    var sectionName = message.section().getName();
    var coubUrl = message.coub().getUrl();
    var chatId = message.chat().getChatId();

    var sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communityName", communityName);
    templateData.put("sectionName", sectionName);
    templateData.put("url", coubUrl);
    var text = templateBuilder.writeTemplate(templateData, "message.ftl");

    sendMessage.setText(text);

    try {

      execute(sendMessage);
      log.info("Sent message to chat '{}' for subscription '{}' {} {}",
          message.chat(),
          message.id(),
          message.community().getName(),
          message.section().getName());
      log.info("Send - {}", sendMessage);

    } catch (TelegramApiRequestException e) {
      if (e.getErrorCode() == 403) {

        log.warn("User doesn't want to receive messages from bot. Make it not active");

        var found = telegramChatService.getChatById(chatId);

        if (found.isActive()) {
          found.setActive(false);
          telegramChatService.update(found);
        }
      }
    } catch (TelegramApiException e) {
      log.error("Error sending message - {}", e.getMessage());
    }

  }
}
