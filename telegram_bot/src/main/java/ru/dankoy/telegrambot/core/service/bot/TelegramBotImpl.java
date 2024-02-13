package ru.dankoy.telegrambot.core.service.bot;

import feign.FeignException.Conflict;
import feign.FeignException.NotFound;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
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
import ru.dankoy.telegrambot.config.configuration.BotConfiguration;
import ru.dankoy.telegrambot.core.domain.SubscriptionType;
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.subscription.Chat;
import ru.dankoy.telegrambot.core.domain.subscription.Community;
import ru.dankoy.telegrambot.core.domain.subscription.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.Order;
import ru.dankoy.telegrambot.core.domain.tagsubscription.TagSubscription;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.localeprovider.LocaleProvider;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {

  private static final String COMMAND_FIRST_FIELD = "first";
  private static final String COMMAND_SECOND_FIELD = "second";
  private static final String COMMAND_SUBSCRIPTION_TYPE_FIELD = "subscription_type";
  private static final String COMMAND = "command";

  private final String botName;

  private final CommunitySubscriptionService communitySubscriptionService;

  private final TelegramChatService telegramChatService;

  private final TemplateBuilder templateBuilder;

  private final CommunityService communityService;

  private final TagSubscriptionService tagSubscriptionService;

  private final OrderService orderService;

  private final LocalisationService localisationService;

  private final LocaleProvider localeProvider;

  public TelegramBotImpl(BotConfiguration botConfiguration) {

    super(botConfiguration.fullBotProperties().getToken());

    this.botName = botConfiguration.fullBotProperties().getName();
    this.communitySubscriptionService = botConfiguration.communitySubscriptionService();
    this.telegramChatService = botConfiguration.telegramChatService();
    this.templateBuilder = botConfiguration.templateBuilder();
    this.communityService = botConfiguration.communityService();
    this.tagSubscriptionService = botConfiguration.tagSubscriptionService();
    this.orderService = botConfiguration.orderService();
    this.localisationService = botConfiguration.localisationService();
    this.localeProvider = botConfiguration.localeProvider();

    try {

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
        botAnswerUtils(message);
      }
    }
  }

  private void botAnswerUtils(Message inputMessage) {

    var messageText = inputMessage.getText();

    var startFromGroup = "/start" + getGroupChatBotName();
    var subsFromGroup = "/my_subscriptions" + getGroupChatBotName();
    var helpFromGroup = "/help" + getGroupChatBotName();
    var subscribeCommunityFromGroup = "/subscribe" + getGroupChatBotName();
    var unsubscribeCommunityFromGroup = "/unsubscribe" + getGroupChatBotName();
    var communitiesFromGroup = "/communities" + getGroupChatBotName();
    var tagOrdersFromGroup = "/tag_orders" + getGroupChatBotName();

    if (messageText.equals("/my_subscriptions") || messageText.equals(subsFromGroup)) {
      mySubscriptions(inputMessage);
    } else if (messageText.equals("/start") || messageText.equals(startFromGroup)) {
      start(inputMessage);
    } else if (messageText.equals("/help") || messageText.equals(helpFromGroup)) {
      help(inputMessage);
    } else if (messageText.startsWith("/subscribe")
        || messageText.startsWith(subscribeCommunityFromGroup)) {
      checkChatStatus(inputMessage);
      subscribeUtils(inputMessage);
    } else if (messageText.startsWith("/unsubscribe")
        || messageText.startsWith(unsubscribeCommunityFromGroup)) {
      checkChatStatus(inputMessage);
      unsubscribeUtils(inputMessage);
    } else if (messageText.equals("/communities") || messageText.equals(communitiesFromGroup)) {
      communities(inputMessage);
    } else if (messageText.equals("/tag_orders") || messageText.equals(tagOrdersFromGroup)) {
      tagOrders(inputMessage);
    } else {
      help(inputMessage);
    }
  }

  private void checkChatStatus(Message message) {

    var sendMessage = createReply(message);

    var tChat = message.getChat();

    log.info("Check chat status - {}", tChat.getId());
    try {
      var found = telegramChatService.getChatById(tChat.getId());
      log.info("Found chat - {}", tChat.getId());

      if (!found.isActive()) {
        sendMessage.setText(
            localisationService.getLocalizedMessage(
                "chatNotActive", null, localeProvider.getLocale(message)));
        send(sendMessage);
      }

    } catch (NotFound e) {
      log.warn("Chat not found - {}", tChat.getId());
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              "chatNotFound", null, localeProvider.getLocale(message)));
      send(sendMessage);
      throw new IllegalStateException("Accessed subscribe command without start");
    }
  }

  private void mySubscriptions(Message inputMessage) {

    long chatId = inputMessage.getChat().getId();

    List<CommunitySubscription> subs =
        communitySubscriptionService.getSubscriptionsByChatId(chatId);
    List<TagSubscription> tagSubs = tagSubscriptionService.getSubscriptionsByChatId(chatId);

    var sendMessage = createReply(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communitySubscriptions", subs);
    templateData.put("tagSubscriptions", tagSubs);

    var text =
        templateBuilder.writeTemplate(
            templateData, "subscriptions.ftl", localeProvider.getLocale(inputMessage));
    sendMessage.setText(text);

    send(sendMessage);
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

      var newChat =
          new Chat(
              0,
              tChat.getId(),
              tChat.getType(),
              tChat.getTitle(),
              tChat.getFirstName(),
              tChat.getLastName(),
              tChat.getUserName(),
              true);
      log.info("New chat to create - {}", newChat);
      telegramChatService.createChat(newChat);
    }

    var sendMessage = createReply(inputMessage);
    sendMessage.setText(
        localisationService.getLocalizedMessage(
            "startFinish", null, localeProvider.getLocale(inputMessage)));

    send(sendMessage);
  }

  private void help(Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("subscription_types", Arrays.toString(SubscriptionType.values()));

    var text =
        templateBuilder.writeTemplate(
            templateData, "help.ftl", localeProvider.getLocale(inputMessage));
    sendMessage.setText(text);

    send(sendMessage);
  }

  private void subscribeToCommunity(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var communityName = command.get(COMMAND_FIRST_FIELD);
    var sectionName = command.get(COMMAND_SECOND_FIELD);

    try {

      var s =
          communitySubscriptionService.subscribe(
              communityName, sectionName, inputMessage.getChat().getId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              "subscriptionCompleted",
              new Object[] {s.getCommunity().getName(), s.getSection().getName()},
              localeProvider.getLocale(inputMessage)));

      send(sendMessage);

    } catch (Conflict e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              "alreadySubscribed",
              new Object[] {communityName, sectionName},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);
    } catch (NotFoundException e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              e.getExceptionObjectType().getType(),
              new Object[] {e.getValue()},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);
      send(buildSubscriptionHelpMessage(inputMessage, command.get(COMMAND)));
    } catch (BotException e) {
      send(buildSubscriptionHelpMessage(inputMessage, command.get(COMMAND)));
    }
  }

  private void unsubscribeFromCommunity(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var communityName = command.get(COMMAND_FIRST_FIELD);
    var sectionName = command.get(COMMAND_SECOND_FIELD);

    try {
      communitySubscriptionService.unsubscribe(
          communityName, sectionName, inputMessage.getChat().getId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              "unsubscriptionCompleted",
              new Object[] {communityName, sectionName},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);

    } catch (BotException e) {
      send(buildSubscriptionHelpMessage(inputMessage, command.get(COMMAND)));
    }
  }

  private void subscribeUtils(Message inputMessage) {

    // find route by first word after command

    try {
      Map<String, String> command = parseCommandTagMultipleWords(inputMessage);

      // route
      if (command.get(COMMAND_SUBSCRIPTION_TYPE_FIELD).equals(SubscriptionType.TAG.getType())) {
        subscribeByTag(command, inputMessage);
      } else if (command
          .get(COMMAND_SUBSCRIPTION_TYPE_FIELD)
          .equals(SubscriptionType.COMMUNITY.getType())) {
        subscribeToCommunity(command, inputMessage);
      }
    } catch (BotException e) {
      var sendMessage = createReply(inputMessage);
      sendMessage.setText(e.getMessage());
      sendMessage.setParseMode(ParseMode.MARKDOWN);
      send(sendMessage);
    }
  }

  private void unsubscribeUtils(Message inputMessage) {

    // find route by first word after command

    try {
      Map<String, String> command = parseCommandTagMultipleWords(inputMessage);

      // route
      if (command
          .get(COMMAND_SUBSCRIPTION_TYPE_FIELD)
          .equals(SubscriptionType.COMMUNITY.getType())) {
        unsubscribeFromCommunity(command, inputMessage);
      } else if (command
          .get(COMMAND_SUBSCRIPTION_TYPE_FIELD)
          .equals(SubscriptionType.TAG.getType())) {
        unsubscribeFromTag(command, inputMessage);
      }

    } catch (BotException e) {
      var sendMessage = createReply(inputMessage);
      sendMessage.setText(e.getMessage());
      sendMessage.setParseMode(ParseMode.MARKDOWN);
      send(sendMessage);
    }
  }

  private void subscribeByTag(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var tagName = command.get(COMMAND_FIRST_FIELD);
    var orderValue = command.get(COMMAND_SECOND_FIELD);

    try {

      var s =
          tagSubscriptionService.subscribe(
              tagName, orderValue, "all", "", inputMessage.getChat().getId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              "subscriptionCompleted",
              new Object[] {s.getTag().getTitle(), s.getOrder().getValue()},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);

    } catch (Conflict e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              "alreadySubscribed",
              new Object[] {tagName, orderValue},
              localeProvider.getLocale(inputMessage)));

      send(sendMessage);
    } catch (NotFoundException e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              e.getExceptionObjectType().getType(),
              new Object[] {e.getValue()},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);
      send(buildSubscriptionHelpMessage(inputMessage, command.get(COMMAND)));
    } catch (BotException e) {
      send(buildSubscriptionHelpMessage(inputMessage, command.get(COMMAND)));
    }
  }

  private void unsubscribeFromTag(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var tagName = command.get(COMMAND_FIRST_FIELD);
    var orderValue = command.get(COMMAND_SECOND_FIELD);

    try {

      tagSubscriptionService.unsubscribe(
          tagName, orderValue, "all", "", inputMessage.getChat().getId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              "unsubscriptionCompleted",
              new Object[] {tagName, orderValue},
              localeProvider.getLocale(inputMessage)));

      send(sendMessage);

    } catch (BotException e) {
      send(buildSubscriptionHelpMessage(inputMessage, command.get(COMMAND)));
    }
  }

  private void communities(Message inputMessage) {

    var sendMessage = createReply(inputMessage);
    sendMessage.setParseMode(ParseMode.MARKDOWN);
    List<Community> communities = communityService.getAll();

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communities", communities);

    var text =
        templateBuilder.writeTemplate(
            templateData, "communities.ftl", localeProvider.getLocale(inputMessage));
    sendMessage.setText(text);

    send(sendMessage);
  }

  private void tagOrders(Message inputMessage) {

    var sendMessage = createReply(inputMessage);
    sendMessage.setParseMode(ParseMode.MARKDOWN);
    List<Order> orders = orderService.findAll();

    // escape special chars in order names
    var updated =
        orders.stream().map(order -> new Order(escapeMetaCharacters(order.getValue()))).toList();

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("orders", updated);

    var text =
        templateBuilder.writeTemplate(
            templateData, "tag_orders.ftl", localeProvider.getLocale(inputMessage));

    sendMessage.setText(text);

    send(sendMessage);
  }

  private void send(SendMessage sendMessage) {
    try {
      execute(sendMessage);
      log.info(
          "Reply sent to '{}' with message '{}'",
          sendMessage.getChatId(),
          StringUtils.normalizeSpace(sendMessage.getText()));
    } catch (TelegramApiException e) {
      log.error(e.getMessage());
    }
  }

  private Map<String, String> parseCommandTagMultipleWords(Message inputMessage) {

    Map<String, String> result = new HashMap<>();

    String[] command = inputMessage.getText().split(" ");

    if (command.length < 4) {
      log.error("Expected valid command but got - {}", Arrays.asList(command));
      throwSubscriptionException(inputMessage, command[0]);
    }

    try {
      SubscriptionType.valueOf(command[1].toUpperCase());
    } catch (IllegalArgumentException e) {
      log.error(
          "Expected one of {}, but got {}", Arrays.asList(SubscriptionType.values()), command[1]);
      throw new BotException(
          localisationService.getLocalizedMessage(
              "illegalBotCommand",
              new Object[] {Arrays.asList(SubscriptionType.values()).toString().toLowerCase()},
              localeProvider.getLocale(inputMessage)));
    }

    // Get all words after 0 and last element and concat in one string
    var s = Arrays.stream(command, 2, command.length - 1).collect(Collectors.joining(" "));

    result.put(COMMAND, command[0]);
    result.put(COMMAND_SUBSCRIPTION_TYPE_FIELD, command[1]);
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

    var communityName = message.getCommunity().getName();
    var sectionName = message.getSection().getName();
    var coubUrl = message.getCoub().getUrl();
    var chatId = message.getChat().getChatId();

    var sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communityName", communityName);
    templateData.put("sectionName", sectionName);
    templateData.put("url", coubUrl);
    var text = templateBuilder.writeTemplate(templateData, "community_subscription_message.ftl");

    sendMessage.setText(text);

    try {

      log.info(
          "Sent message to chat '{}' for subscription '{}' {} {}",
          message.getChat(),
          message.getId(),
          message.getCommunity().getName(),
          message.getSection().getName());

      execute(sendMessage);

      log.info(
          "Message sent to '{}' with message '{}'",
          sendMessage.getChatId(),
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

    var tagName = message.getTag().getTitle();
    var orderValue = message.getOrder().getValue();
    var coubUrl = message.getCoub().getUrl();
    var chatId = message.getChat().getChatId();

    sendMessage.setChatId(chatId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("tagName", tagName);
    templateData.put("orderValue", orderValue);
    templateData.put("url", coubUrl);
    var text = templateBuilder.writeTemplate(templateData, "tag_subscription_message.ftl");

    sendMessage.setText(text);

    try {

      log.info(
          "Sent message to chat '{}' for tag subscription '{}' {}",
          message.getChat().getChatId(),
          message.getId(),
          message.getTag().getTitle());

      execute(sendMessage);

      log.info(
          "Message sent to '{}' with message '{}'",
          sendMessage.getChatId(),
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
    final String[] metaCharacters = {
      "\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&",
      "%", "_"
    };

    for (String metaCharacter : metaCharacters) {
      if (inputString.contains(metaCharacter)) {
        inputString = inputString.replace(metaCharacter, "\\" + metaCharacter);
      }
    }
    return inputString;
  }

  private SendMessage buildSubscriptionHelpMessage(Message message, String command) {

    return SendMessage.builder()
        .chatId(message.getChatId())
        .text(getSubscriptionCommandHelp(message, command))
        .parseMode(ParseMode.MARKDOWN)
        .build();
  }

  private String getSubscriptionCommandHelp(Message message, String command) {

    Map<String, Object> templateData = new HashMap<>();
    templateData.put(COMMAND, command);

    return templateBuilder.writeTemplate(
        templateData, "subscription_exception.ftl", localeProvider.getLocale(message));
  }

  private void throwSubscriptionException(Message message, String command) {

    throw new BotException(getSubscriptionCommandHelp(message, command));
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

  @Override
  public void sendMessage(SendMessage sendMessage) {
    send(sendMessage);
  }

  private SendMessage createReply(Message inputMessage) {

    var sendMessage = new SendMessage();
    sendMessage.setChatId(inputMessage.getChat().getId());
    sendMessage.setReplyToMessageId(inputMessage.getMessageId());

    return sendMessage;
  }
}
