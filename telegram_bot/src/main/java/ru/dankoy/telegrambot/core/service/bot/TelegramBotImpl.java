package ru.dankoy.telegrambot.core.service.bot;

import feign.FeignException.Conflict;
import feign.FeignException.NotFound;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Community;
import ru.dankoy.telegrambot.core.domain.subscription.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Order;
import ru.dankoy.telegrambot.core.domain.tagsubscription.TagSubscription;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;


@Slf4j
@RequiredArgsConstructor
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {

  private final static String COMMAND_FIRST_FIELD = "first";
  private final static String COMMAND_SECOND_FIELD = "second";

  private final String botName;

  private final List<BotCommand> commands;

  private final CommunitySubscriptionService communitySubscriptionService;

  private final TelegramChatService telegramChatService;

  private final TemplateBuilder templateBuilder;

  private final CommunityService communityService;

  private final TagSubscriptionService tagSubscriptionService;

  private final OrderService orderService;


  public TelegramBotImpl(
      String botName,
      String token,
      List<BotCommand> commands,
      CommunitySubscriptionService communitySubscriptionService,
      TelegramChatService telegramChatService,
      TemplateBuilder templateBuilder,
      CommunityService communityService,
      TagSubscriptionService tagSubscriptionService,
      OrderService orderService
  ) {
    super(token);
    this.botName = botName;
    this.communitySubscriptionService = communitySubscriptionService;
    this.telegramChatService = telegramChatService;
    this.templateBuilder = templateBuilder;
    this.communityService = communityService;
    this.commands = commands;
    this.tagSubscriptionService = tagSubscriptionService;
    this.orderService = orderService;

    try {
      this.execute(
          new SetMyCommands(commands,
              new BotCommandScopeDefault(),
              "en")
      );
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
  }

  @Override
  public void onUpdateReceived(Update update) {

    if (update.hasMessage()) {

      Message message = update.getMessage();

      if (update.getMessage().hasText()) {
        log.info("Received message from '{}' with text '{}'", message.getChat().getId(),
            message.getText());
        botAnswerUtils(message);
      }
    }

  }


  private void botAnswerUtils(Message inputMessage) {

    var messageText = inputMessage.getText();
    var chatId = inputMessage.getChat().getId();

    var startFromGroup = "/start" + getGroupChatBotName();
    var subsFromGroup = "/my_subscriptions" + getGroupChatBotName();
    var helpFromGroup = "/help" + getGroupChatBotName();
    var subscribeCommunityFromGroup = "/subscribe_by_community" + getGroupChatBotName();
    var unsubscribeCommunityFromGroup = "/unsubscribe_from_community" + getGroupChatBotName();
    var subscribeTagFromGroup = "/subscribe_by_tag" + getGroupChatBotName();
    var unsubscribeTagFromGroup = "/unsubscribe_from_tag" + getGroupChatBotName();
    var communitiesFromGroup = "/communities" + getGroupChatBotName();
    var tagOrdersFromGroup = "/tag_orders" + getGroupChatBotName();

    if (messageText.equals("/my_subscriptions") || messageText.equals(subsFromGroup)) {
      mySubscriptions(chatId);
    } else if (messageText.equals("/start") || messageText.equals(startFromGroup)) {
      start(inputMessage);
    } else if (messageText.equals("/help") || messageText.equals(helpFromGroup)) {
      help(inputMessage);
    } else if (messageText.startsWith("/subscribe_by_community") || messageText.startsWith(
        subscribeCommunityFromGroup)) {
      checkChatStatus(inputMessage);
      subscribeToCommunity(inputMessage);
    } else if (messageText.startsWith("/unsubscribe_from_community") || messageText.startsWith(
        unsubscribeCommunityFromGroup)) {
      checkChatStatus(inputMessage);
      unsubscribeFromCommunity(inputMessage);
    } else if (messageText.startsWith("/subscribe_by_tag") || messageText.startsWith(
        subscribeTagFromGroup)) {
      checkChatStatus(inputMessage);
      subscribeByTag(inputMessage);
    } else if (messageText.startsWith("/unsubscribe_from_tag") || messageText.startsWith(
        unsubscribeTagFromGroup)) {
      checkChatStatus(inputMessage);
      unsubscribeFromTag(inputMessage);
    } else if (messageText.equals("/communities") || messageText.equals(communitiesFromGroup)) {
      communities(inputMessage);
    } else if (messageText.equals("/tag_orders") || messageText.equals(tagOrdersFromGroup)) {
      tagOrders(inputMessage);
    }
  }


  private void checkChatStatus(Message message) {

    var sendMessage = new SendMessage();
    var tChat = message.getChat();

    sendMessage.setChatId(message.getChat().getId());

    log.info("Check chat status - {}", tChat.getId());
    try {
      var found = telegramChatService.getChatById(tChat.getId());
      log.info("Found chat - {}", tChat.getId());

      if (!found.isActive()) {
        sendMessage.setText("Chat is not active. Please, first run /start command.");
        send(sendMessage);
      }

    } catch (NotFound e) {
      log.warn("Chat not found - {}", tChat.getId());
      sendMessage.setText("Please, first run /start command.");
      send(sendMessage);
      throw new IllegalStateException("Accessed subscribe command without start");
    }

  }

  private void mySubscriptions(long chatId) {

    List<CommunitySubscription> subs = communitySubscriptionService.getSubscriptionsByChatId(
        chatId);
    List<TagSubscription> tagSubs = tagSubscriptionService.getSubscriptionsByChatId(chatId);

    SendMessage message = new SendMessage();
    message.setChatId(chatId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communitySubscriptions", subs);
    templateData.put("tagSubscriptions", tagSubs);

    var text = templateBuilder.writeTemplate(templateData, "subscriptions.ftl");
    message.setText(text);

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
        "Now you can subscribe to coub.com communities or/and tags and receive updates directly in chat");

    send(message);
  }


  private void help(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());

    var text = templateBuilder.writeTemplate(new HashMap<>(), "help.ftl");
    message.setText(text);

    send(message);

  }

  private void subscribeToCommunity(Message inputMessage) {

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

      var s = communitySubscriptionService.subscribe(communityName, sectionName,
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

  private void unsubscribeFromCommunity(Message inputMessage) {

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
      communitySubscriptionService.unsubscribe(communityName,
          sectionName,
          inputMessage.getChat().getId());

      message.setText(String.format("Unsubscribed from %s %s", communityName, sectionName));
      send(message);

    } catch (Exception e) {
      message.setText("Something went wrong. Validate your command");
      send(message);
    }

  }

  private void subscribeByTag(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());

    var messageText = inputMessage.getText();
    // find community and section

    Map<String, String> command = parseCommandTagMultipleWords(messageText);

    var tagName = command.get(COMMAND_FIRST_FIELD);
    var orderName = command.get(COMMAND_SECOND_FIELD);

    try {

      var s = tagSubscriptionService.subscribe(
          tagName,
          orderName,
          "all",
          "",
          inputMessage.getChat().getId());

      message.setText(String.format("Subscribed to %s %s", s.getTag().getTitle(),
          s.getOrder().getName()));
      send(message);

    } catch (Conflict e) {
      message.setText(String.format("You are already subscribed to '%s - %s'",
          tagName,
          orderName));
      send(message);
    } catch (NotFoundException e) {
      message.setText(e.getMessage());
      send(message);
    } catch (Exception e) {
      message.setText("Something went wrong. Validate your command or check /help.");
      send(message);
    }

  }

  private void unsubscribeFromTag(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());

    var messageText = inputMessage.getText();
    // find community and section

    Map<String, String> command = parseCommandTagMultipleWords(messageText);

    var tagName = command.get(COMMAND_FIRST_FIELD);
    var orderName = command.get(COMMAND_SECOND_FIELD);

    try {

      tagSubscriptionService.unsubscribe(
          tagName,
          orderName,
          "all",
          "",
          inputMessage.getChat().getId());

      message.setText(String.format("Unsubscribed from %s %s", tagName, orderName));
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


  private void tagOrders(Message inputMessage) {

    SendMessage message = new SendMessage();
    message.setChatId(inputMessage.getChat().getId());
    message.setParseMode(ParseMode.MARKDOWN);
    List<Order> orders = orderService.findAll();

    // escape special chars in order names
    var updated = orders.stream()
        .map(order -> new Order(escapeMetaCharacters(order.getName())))
        .toList();

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("orders", updated);

    var text = templateBuilder.writeTemplate(templateData, "tag_orders.ftl");

    message.setText(text);

    send(message);

  }


  private void send(SendMessage sendMessage) {
    try {
      execute(sendMessage);
      log.info("Reply sent to '{}' with message '{}'", sendMessage.getChatId(),
          StringUtils.normalizeSpace(sendMessage.getText()));
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

  private Map<String, String> parseCommandTagMultipleWords(String messageText) {

    Map<String, String> result = new HashMap<>();

    String[] command = messageText.split(" ");

    // Get all words after 0 and last element and concat in one string
    var s = Arrays.stream(command, 1, command.length - 1)
        .collect(Collectors.joining(" "));

    result.put(COMMAND_FIRST_FIELD, s);
    result.put(COMMAND_SECOND_FIELD, command[command.length - 1]);

    return result;

  }

  @Override
  public String getBotUsername() {
    return botName;
  }


  private String getGroupChatBotName() {
    return "@" + botName;
  }

  @Override
  public void sendMessage(CommunitySubscriptionMessage message) {

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
    var text = templateBuilder.writeTemplate(templateData, "community_subscription_message.ftl");

    sendMessage.setText(text);

    try {

      log.info("Sent message to chat '{}' for subscription '{}' {} {}",
          message.chat(),
          message.id(),
          message.community().getName(),
          message.section().getName());

      execute(sendMessage);

      log.info("Message sent to '{}' with message '{}'", sendMessage.getChatId(),
          StringUtils.normalizeSpace(sendMessage.getText()));

    } catch (TelegramApiRequestException e) {
      if (e.getErrorCode() == 403) {

        log.warn("User blocked bot. Make it not active");

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

  @Override
  public void sendMessage(TagSubscriptionMessage message) {

    var sendMessage = new SendMessage();

    var tagName = message.tag().getTitle();
    var orderName = message.order().getName();
    var coubUrl = message.coub().getUrl();
    var chatId = message.chat().getChatId();

    sendMessage.setChatId(chatId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("tagName", tagName);
    templateData.put("orderName", orderName);
    templateData.put("url", coubUrl);
    var text = templateBuilder.writeTemplate(templateData, "tag_subscription_message.ftl");

    sendMessage.setText(text);

    try {

      log.info("Sent message to chat '{}' for tag subscription '{}' {}",
          message.chat().getChatId(),
          message.id(),
          message.tag().getTitle());

      execute(sendMessage);

      log.info("Message sent to '{}' with message '{}'", sendMessage.getChatId(),
          StringUtils.normalizeSpace(sendMessage.getText()));

    } catch (TelegramApiRequestException e) {
      if (e.getErrorCode() == 403) {

        log.warn("User blocked bot. Make it not active");

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

  private String escapeMetaCharacters(String inputString) {
    final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+",
        "?", "|", "<", ">", "-", "&", "%", "_"};

    for (String metaCharacter : metaCharacters) {
      if (inputString.contains(metaCharacter)) {
        inputString = inputString.replace(metaCharacter, "\\" + metaCharacter);
      }
    }
    return inputString;
  }


}
