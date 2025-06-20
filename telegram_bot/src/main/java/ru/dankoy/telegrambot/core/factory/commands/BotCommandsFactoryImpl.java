package ru.dankoy.telegrambot.core.factory.commands;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.CommunitiesCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.HelpCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.MySubscriptionsCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.OrdersCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.StartCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.SubscribeCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.UnsubscribeCommand;
import ru.dankoy.telegrambot.core.service.chat.SubscriptionsHolderChatService;
import ru.dankoy.telegrambot.core.service.chat.TelegramChatService;
import ru.dankoy.telegrambot.core.service.community.CommunityService;
import ru.dankoy.telegrambot.core.service.localization.LocalisationService;
import ru.dankoy.telegrambot.core.service.order.OrderService;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;

public record BotCommandsFactoryImpl(
    LocalisationService localisationService,
    Locale locale,
    CommunitySubscriptionService communitySubscriptionService,
    TagSubscriptionService tagSubscriptionService,
    ChannelSubscriptionService channelSubscriptionService,
    TelegramChatService telegramChatService,
    CommunityService communityService,
    OrderService orderService,
    SubscriptionsHolderChatService subscriptionsHolderChatService)
    implements BotCommandsFactory {

  // TODO: commands should not be used as logic container. Special services should be created

  @Override
  public List<BotCommand> allKnownCommands() {

    return Stream.of(
            communitiesCommand(),
            helpCommand(),
            mySubscriptionsCommand(),
            startCommand(),
            subscribeCommand(),
            tagOrdersCommand(),
            unsubscribeCommand())
        .toList();
  }

  @Override
  public BotCommand communitiesCommand() {

    var command = localisationService.getLocalizedMessage("communitiesCommand", null, locale);
    var description =
        localisationService.getLocalizedMessage("communitiesCommandDescription", null, locale);

    return new CommunitiesCommand(command, description, communityService);
  }

  @Override
  public BotCommand helpCommand() {

    var command = localisationService.getLocalizedMessage("helpCommand", null, locale);
    var description =
        localisationService.getLocalizedMessage("helpCommandDescription", null, locale);

    return new HelpCommand(command, description);
  }

  @Override
  public BotCommand mySubscriptionsCommand() {

    var command = localisationService.getLocalizedMessage("mySubscriptionsCommand", null, locale);
    var description =
        localisationService.getLocalizedMessage("mySubscriptionsCommandDescription", null, locale);

    return new MySubscriptionsCommand(
        command,
        description,
        communitySubscriptionService,
        tagSubscriptionService,
        channelSubscriptionService);
  }

  @Override
  public BotCommand startCommand() {

    var command = localisationService.getLocalizedMessage("startCommand", null, locale);
    var description =
        localisationService.getLocalizedMessage("startCommandDescription", null, locale);

    return new StartCommand(
        command, description, telegramChatService, subscriptionsHolderChatService);
  }

  @Override
  public BotCommand subscribeCommand() {

    var command = localisationService.getLocalizedMessage("subscribeCommand", null, locale);
    var description =
        localisationService.getLocalizedMessage("subscribeCommandDescription", null, locale);

    return new SubscribeCommand(
        command,
        description,
        communitySubscriptionService,
        tagSubscriptionService,
        channelSubscriptionService);
  }

  @Override
  public BotCommand unsubscribeCommand() {

    var command = localisationService.getLocalizedMessage("unsubscribeCommand", null, locale);
    var description =
        localisationService.getLocalizedMessage("unsubscribeCommandDescription", null, locale);

    return new UnsubscribeCommand(
        command,
        description,
        communitySubscriptionService,
        tagSubscriptionService,
        channelSubscriptionService);
  }

  @Override
  public BotCommand tagOrdersCommand() {

    var command = localisationService.getLocalizedMessage("ordersCommand", null, locale);
    var description =
        localisationService.getLocalizedMessage("ordersCommandDescription", null, locale);

    return new OrdersCommand(command, description, orderService);
  }
}
