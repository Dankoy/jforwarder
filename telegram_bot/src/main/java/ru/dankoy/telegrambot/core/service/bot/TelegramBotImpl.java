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
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.dankoy.telegrambot.config.configuration.BotConfiguration;
import ru.dankoy.telegrambot.core.domain.Chat;
import ru.dankoy.telegrambot.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.subscription.Order;
import ru.dankoy.telegrambot.core.domain.subscription.SubscriptionType;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.exceptions.BotException;
import ru.dankoy.telegrambot.core.exceptions.NotFoundException;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.localeprovider.LocaleProvider;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;
import ru.dankoy.telegrambot.core.service.template.TemplateBuilder;

@Slf4j
@RequiredArgsConstructor
public class TelegramBotImpl extends TelegramLongPollingBot implements TelegramBot {

  private static final String TEMPLATE_SUBSCRIPTION_SUCCESS = "subscriptionCompleted";
  private static final String TEMPLATE_UNSUBSCRIBE_SUCCESS = "unsubscriptionCompleted";
  private static final String TEMPLATE_SUBSCRIPTION_EXISTS = "alreadySubscribed";
  private static final String TEMPLATE_SUBSCRIPTION_EXCEPTION = "subscription_exception.ftl";

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

  private final ChannelSubscriptionService channelSubscriptionService;

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
    this.channelSubscriptionService = botConfiguration.channelSubscriptionService();
    this.orderService = botConfiguration.orderService();
    this.localisationService = botConfiguration.localisationService();
    this.localeProvider = botConfiguration.localeProvider();

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
    var tagOrdersFromGroup = "/orders" + getGroupChatBotName();

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
    } else if (messageText.startsWith("/orders") || messageText.equals(tagOrdersFromGroup)) {
      orders(inputMessage);
    } else {
      help(inputMessage);
    }
  }

  private void checkChatStatus(Message message) {

    var sendMessage = createReply(message);

    var tChat = message.getChat();
    var messageThreadId = message.getMessageThreadId();

    log.info("Check chat status - {}", tChat.getId());
    try {
      var found = telegramChatService.getChatByIdAndMessageThreadId(tChat.getId(), messageThreadId);
      log.info("Found chat - {}-{}", tChat.getId(), messageThreadId);

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
    Integer messageThreadId = inputMessage.getMessageThreadId();

    List<CommunitySubscription> subs =
        communitySubscriptionService.getSubscriptionsByChatIdAndMessageThreadId(
            chatId, messageThreadId);
    List<TagSubscription> tagSubs =
        tagSubscriptionService.getSubscriptionsByChatIdAndMessageThreadId(chatId, messageThreadId);
    List<ChannelSubscription> channelSubs =
        channelSubscriptionService.getSubscriptionsByChatIdAndMessageThreadId(
            chatId, messageThreadId);

    var sendMessage = createReply(inputMessage);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communitySubscriptions", subs);
    templateData.put("tagSubscriptions", tagSubs);
    templateData.put("channelSubscriptions", channelSubs);

    var text =
        templateBuilder.writeTemplate(
            templateData, "subscriptions.ftl", localeProvider.getLocale(inputMessage));
    sendMessage.setText(text);

    send(sendMessage);
  }

  // create chat in db
  private void start(Message inputMessage) {

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
              communityName,
              sectionName,
              inputMessage.getChat().getId(),
              inputMessage.getMessageThreadId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_SUBSCRIPTION_SUCCESS,
              new Object[] {s.getCommunity().getName(), s.getSection().getName()},
              localeProvider.getLocale(inputMessage)));

      send(sendMessage);

    } catch (Conflict e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_SUBSCRIPTION_EXISTS,
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
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
    } catch (BotException e) {
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
    }
  }

  private void unsubscribeFromCommunity(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var communityName = command.get(COMMAND_FIRST_FIELD);
    var sectionName = command.get(COMMAND_SECOND_FIELD);

    try {
      communitySubscriptionService.unsubscribe(
          communityName,
          sectionName,
          inputMessage.getChat().getId(),
          inputMessage.getMessageThreadId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_UNSUBSCRIBE_SUCCESS,
              new Object[] {communityName, sectionName},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);

    } catch (BotException e) {
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
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
      } else if (command
          .get(COMMAND_SUBSCRIPTION_TYPE_FIELD)
          .equals(SubscriptionType.CHANNEL.getType())) {
        subscribeByChannel(command, inputMessage);
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
      } else if (command
          .get(COMMAND_SUBSCRIPTION_TYPE_FIELD)
          .equals(SubscriptionType.CHANNEL.getType())) {
        unsubscribeFromChannel(command, inputMessage);
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
              tagName,
              orderValue,
              "all",
              "",
              inputMessage.getChat().getId(),
              inputMessage.getMessageThreadId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_SUBSCRIPTION_SUCCESS,
              new Object[] {s.getTag().getTitle(), s.getOrder().getValue()},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);

    } catch (Conflict e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_SUBSCRIPTION_EXISTS,
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
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
    } catch (BotException e) {
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
    }
  }

  private void unsubscribeFromTag(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var tagName = command.get(COMMAND_FIRST_FIELD);
    var orderValue = command.get(COMMAND_SECOND_FIELD);

    try {

      tagSubscriptionService.unsubscribe(
          tagName,
          orderValue,
          "all",
          "",
          inputMessage.getChat().getId(),
          inputMessage.getMessageThreadId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_UNSUBSCRIBE_SUCCESS,
              new Object[] {tagName, orderValue},
              localeProvider.getLocale(inputMessage)));

      send(sendMessage);

    } catch (BotException e) {
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
    }
  }

  private void subscribeByChannel(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var channelPermalink = command.get(COMMAND_FIRST_FIELD);
    var orderValue = command.get(COMMAND_SECOND_FIELD);

    try {

      var s =
          channelSubscriptionService.subscribe(
              channelPermalink,
              orderValue,
              "all",
              "",
              inputMessage.getChat().getId(),
              inputMessage.getMessageThreadId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_SUBSCRIPTION_SUCCESS,
              new Object[] {s.getChannel().getPermalink(), s.getOrder().getValue()},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);

    } catch (Conflict e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_SUBSCRIPTION_EXISTS,
              new Object[] {channelPermalink, orderValue},
              localeProvider.getLocale(inputMessage)));

      send(sendMessage);
    } catch (NotFoundException e) {
      sendMessage.setText(
          localisationService.getLocalizedMessage(
              e.getExceptionObjectType().getType(),
              new Object[] {e.getValue()},
              localeProvider.getLocale(inputMessage)));
      send(sendMessage);
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
    } catch (BotException e) {
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
    }
  }

  private void unsubscribeFromChannel(Map<String, String> command, Message inputMessage) {

    var sendMessage = createReply(inputMessage);

    var channelPermalink = command.get(COMMAND_FIRST_FIELD);
    var orderValue = command.get(COMMAND_SECOND_FIELD);

    try {

      channelSubscriptionService.unsubscribe(
          channelPermalink,
          orderValue,
          "all",
          "",
          inputMessage.getChat().getId(),
          inputMessage.getMessageThreadId());

      sendMessage.setText(
          localisationService.getLocalizedMessage(
              TEMPLATE_UNSUBSCRIBE_SUCCESS,
              new Object[] {channelPermalink, orderValue},
              localeProvider.getLocale(inputMessage)));

      send(sendMessage);

    } catch (BotException e) {
      send(buildHelpMessage(inputMessage, command.get(COMMAND), TEMPLATE_SUBSCRIPTION_EXCEPTION));
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

  private void orders(Message inputMessage) {

    try {

      Map<String, SubscriptionType> command = parseOrderCommandMultipleWords(inputMessage);

      var sendMessage = createReply(inputMessage);
      sendMessage.setParseMode(ParseMode.MARKDOWN);
      List<Order> orders = orderService.findAllByType(command.get(COMMAND_SUBSCRIPTION_TYPE_FIELD));

      // escape special chars in order names
      var updated =
          orders.stream().map(order -> new Order(escapeMetaCharacters(order.getValue()))).toList();

      Map<String, Object> templateData = new HashMap<>();
      templateData.put("orders", updated);

      var text =
          templateBuilder.writeTemplate(
              templateData, "orders.ftl", localeProvider.getLocale(inputMessage));

      sendMessage.setText(text);

      send(sendMessage);

    } catch (BotException e) {
      var sendMessage = createReply(inputMessage);
      sendMessage.setText(e.getMessage());
      sendMessage.setParseMode(ParseMode.MARKDOWN);
      send(sendMessage);
    }
  }

  private void send(SendMessage sendMessage) {
    try {
      execute(sendMessage);
      log.info(
          "Reply sent to '{}'-{} with message '{}'",
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
      }
    } catch (TelegramApiException e) {
      log.error("Error sending message - {}", e.getMessage());
    }
  }

  private Map<String, String> parseCommandTagMultipleWords(Message inputMessage) {

    Map<String, String> result = new HashMap<>();

    String[] command = inputMessage.getText().split(" ");

    if (command.length < 4) {
      log.error("Expected valid command but got - {}", Arrays.asList(command));
      throwSubscriptionException(inputMessage, command[0], TEMPLATE_SUBSCRIPTION_EXCEPTION);
    }

    checkSubscriptionTypeInCommand(inputMessage, command);

    // Get all words after 0 and last element and concat in one string
    var s = Arrays.stream(command, 2, command.length - 1).collect(Collectors.joining(" "));

    result.put(COMMAND, command[0]);
    result.put(COMMAND_SUBSCRIPTION_TYPE_FIELD, command[1]);
    result.put(COMMAND_FIRST_FIELD, s);
    result.put(COMMAND_SECOND_FIELD, command[command.length - 1]);

    return result;
  }

  private Map<String, SubscriptionType> parseOrderCommandMultipleWords(Message inputMessage) {

    Map<String, SubscriptionType> result = new HashMap<>();

    String[] command = inputMessage.getText().split(" ");

    if (command.length != 2) {
      log.error("Expected valid command but got - {}", Arrays.asList(command));
      throwSubscriptionException(inputMessage, command[0], "orders_exception.ftl");
    }

    checkSubscriptionTypeInCommand(inputMessage, command);

    result.put(COMMAND_SUBSCRIPTION_TYPE_FIELD, SubscriptionType.valueOf(command[1].toUpperCase()));

    return result;
  }

  private void checkSubscriptionTypeInCommand(Message inputMessage, String[] command) {
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
    var messageThreadId = message.getChat().getMessageThreadId();

    var sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setMessageThreadId(messageThreadId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("communityName", communityName);
    templateData.put("sectionName", sectionName);
    templateData.put("url", coubUrl);
    var text = templateBuilder.writeTemplate(templateData, "community_subscription_message.ftl");

    sendMessage.setText(text);

    send(sendMessage);

    log.info(
        "Sent message to chat '{}'-{} for subscription '{}' {} {}",
        message.getChat(),
        message.getChat().getMessageThreadId(),
        message.getId(),
        message.getCommunity().getName(),
        message.getSection().getName());
  }

  @Override
  public void sendMessage(TagSubscriptionMessage message) {

    var sendMessage = new SendMessage();

    var tagName = message.getTag().getTitle();
    var orderValue = message.getOrder().getValue();
    var coubUrl = message.getCoub().getUrl();
    var chatId = message.getChat().getChatId();
    var messageThreadId = message.getChat().getMessageThreadId();

    sendMessage.setChatId(chatId);
    sendMessage.setMessageThreadId(messageThreadId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("tagName", tagName);
    templateData.put("orderValue", orderValue);
    templateData.put("url", coubUrl);
    var text = templateBuilder.writeTemplate(templateData, "tag_subscription_message.ftl");

    sendMessage.setText(text);

    send(sendMessage);

    log.info(
        "Sent message to chat '{}'-{} for tag subscription '{}' {}",
        message.getChat().getChatId(),
        message.getChat().getMessageThreadId(),
        message.getId(),
        message.getTag().getTitle());
  }

  @Override
  public void sendMessage(ChannelSubscriptionMessage message) {

    var sendMessage = new SendMessage();

    var channelTitle = message.getChannel().getTitle();
    var orderValue = message.getOrder().getValue();
    var coubUrl = message.getCoub().getUrl();
    var chatId = message.getChat().getChatId();
    var messageThreadId = message.getChat().getMessageThreadId();

    sendMessage.setChatId(chatId);
    sendMessage.setMessageThreadId(messageThreadId);

    Map<String, Object> templateData = new HashMap<>();
    templateData.put("channelTitle", channelTitle);
    templateData.put("orderValue", orderValue);
    templateData.put("url", coubUrl);
    var text = templateBuilder.writeTemplate(templateData, "channel_subscription_message.ftl");

    sendMessage.setText(text);

    send(sendMessage);

    log.info(
        "Sent message to chat '{}'-{} for channel subscription '{}' {}",
        message.getChat().getChatId(),
        message.getChat().getMessageThreadId(),
        message.getId(),
        message.getChannel().getPermalink());
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

  private SendMessage buildHelpMessage(Message message, String command, String helpType) {

    return SendMessage.builder()
        .chatId(message.getChatId())
        .messageThreadId(message.getMessageThreadId())
        .text(getCommandHelp(message, command, helpType))
        .parseMode(ParseMode.MARKDOWN)
        .build();
  }

  private String getCommandHelp(Message message, String command, String helpType) {

    Map<String, Object> templateData = new HashMap<>();
    templateData.put(COMMAND, command);

    return templateBuilder.writeTemplate(templateData, helpType, localeProvider.getLocale(message));
  }

  private void throwSubscriptionException(Message message, String command, String helpType) {

    throw new BotException(getCommandHelp(message, command, helpType));
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

    // then delete for every knows locale
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

  private SendMessage createReply(Message inputMessage) {

    var sendMessage = new SendMessage();
    sendMessage.setChatId(inputMessage.getChat().getId());
    sendMessage.setMessageThreadId(inputMessage.getMessageThreadId());
    sendMessage.setReplyToMessageId(inputMessage.getMessageId());

    return sendMessage;
  }
}
