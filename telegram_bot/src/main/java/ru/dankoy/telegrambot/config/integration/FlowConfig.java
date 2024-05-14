package ru.dankoy.telegrambot.config.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;
import ru.dankoy.telegrambot.core.exceptions.BotCommandFlowException;
import ru.dankoy.telegrambot.core.exceptions.BotFlowException;
import ru.dankoy.telegrambot.core.service.bot.TelegramBot;
import ru.dankoy.telegrambot.core.service.bot.commands.CommandsExtractorService;
import ru.dankoy.telegrambot.core.service.bot.commands.CommunitiesCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.HelpCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.MySubscriptionsCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.OrdersCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.StartCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.SubscribeCommand;
import ru.dankoy.telegrambot.core.service.bot.commands.UnsubscribeCommand;
import ru.dankoy.telegrambot.core.service.flow.BotMessageRouter;
import ru.dankoy.telegrambot.core.service.flow.ChatFlowHandler;
import ru.dankoy.telegrambot.core.service.flow.CommandParserService;
import ru.dankoy.telegrambot.core.service.reply.ReplyCreatorService;

@Configuration
public class FlowConfig {

  // Поток в который поступают сообщения из чатов
  @Bean
  public MessageChannel inputMessageChannel() {
    return new QueueChannel(25);
  }

  @Bean
  public MessageChannel mySubscriptionsChannel() {
    return new QueueChannel(25);
  }

  @Bean
  public MessageChannel startChannel() {
    return new QueueChannel(25);
  }

  @Bean
  public MessageChannel helpChannel() {
    return new QueueChannel(25);
  }

  @Bean
  public MessageChannel subscribeChannel() {
    return new QueueChannel(50);
  }

  @Bean
  public MessageChannel messageSourceLocalizationChannel() {
    return new QueueChannel(25);
  }

  @Bean
  public MessageChannel freemarkerLocalizationChannel() {
    return new QueueChannel(25);
  }

  //  // общий выходной поток
  //  @Bean
  //  public MessageChannel outputMessageChannel() {
  //    return new PublishSubscribeChannel();
  //  }

  @Bean
  public HeaderValueRouter commandRouter() {
    HeaderValueRouter router = new HeaderValueRouter("command");
    router.setChannelMapping(
        MySubscriptionsCommand.class.getSimpleName(), "mySubscriptionsChannel");
    router.setChannelMapping(CommunitiesCommand.class.getSimpleName(), "communitiesChannel");
    router.setChannelMapping(OrdersCommand.class.getSimpleName(), "ordersChannel");
    router.setChannelMapping(StartCommand.class.getSimpleName(), "startChannel");
    router.setChannelMapping(SubscribeCommand.class.getSimpleName(), "subscribeChannel");
    router.setChannelMapping(UnsubscribeCommand.class.getSimpleName(), "unsubscribeChannel");
    router.setChannelMapping(HelpCommand.class.getSimpleName(), "helpChannel");
    return router;
  }

  @Bean
  public IntegrationFlow botExceptionsFlow() {
    return IntegrationFlow.from("errorChannel") // default error channel
        .<Object, Class<?>>route(
            Object::getClass,
            m ->
                m.channelMapping(BotFlowException.class, "messageSourceLocalizationChannel")
                    .channelMapping(BotCommandFlowException.class, "freemarkerLocalizationChannel"))
        .get();
  }

  @Bean
  public IntegrationFlow errorMessageSourceLocalizationFlow(
      ReplyCreatorService replyCreatorService, TelegramBot telegramBot) {
    return IntegrationFlow.from(messageSourceLocalizationChannel())
        .handle(replyCreatorService, "replyWithMessageSourceOnException")
        .handle(telegramBot, "sendMessage")
        //        .filter("#payload.failedMessage.headers['parsedCommand']")
        //        .handle(replyCreatorService, "createReplySubscriptionHelp")
        //        .handle(telegramBot, "sendMessage")
        .get();
  }

  @Bean
  public IntegrationFlow errorFreemarkerLocalizationFlow(
      ReplyCreatorService replyCreatorService, TelegramBot telegramBot) {
    return IntegrationFlow.from(freemarkerLocalizationChannel())
        .handle(replyCreatorService, "replyWithFreemarkerOnException")
        .handle(telegramBot, "sendMessage")
        .get();
  }

  // Flow for any input message from user in chats
  @Bean
  public IntegrationFlow inputMessageProcessingFlow(BotMessageRouter botMessageRouter) {
    return IntegrationFlow.from(inputMessageChannel())
        .handle(botMessageRouter, "commandRoute")
        .route(commandRouter())
        .get();
  }

  // FLow for processing my_subscriptions command
  @Bean
  public IntegrationFlow mySubscriptionsFlow(
      CommandsExtractorService commandsExtractorService,
      ReplyCreatorService replyCreatorService,
      TelegramBot telegramBot,
      ChatFlowHandler chatFlowHandler) {

    var command = commandsExtractorService.getCommand(MySubscriptionsCommand.class);

    return IntegrationFlow.from(mySubscriptionsChannel())
        .handle(chatFlowHandler, "checkChatStatus")
        .handle(command, "mySubscriptions")
        .handle(replyCreatorService, "createReplyMySubscriptions")
        .handle(telegramBot, "sendMessage")
        .get();
  }

  // FLow for processing start command
  @Bean
  public IntegrationFlow startFlow(
      CommandsExtractorService commandsExtractorService,
      ReplyCreatorService replyCreatorService,
      TelegramBot telegramBot) {

    var command = commandsExtractorService.getCommand(StartCommand.class);

    return IntegrationFlow.from(startChannel())
        .handle(command, "start")
        .handle(replyCreatorService, "createReplyStart")
        .handle(telegramBot, "sendMessage")
        .get();
  }

  // FLow for processing help command
  @Bean
  public IntegrationFlow helpFlow(
      ReplyCreatorService replyCreatorService, TelegramBot telegramBot) {

    return IntegrationFlow.from(helpChannel())
        .handle(replyCreatorService, "createReplyHelp")
        .handle(telegramBot, "sendMessage")
        .get();
  }

  // FLow for processing subscribe command
  @Bean
  public IntegrationFlow subscribeFlow(
      ReplyCreatorService replyCreatorService,
      TelegramBot telegramBot,
      ChatFlowHandler chatFlowHandler,
      CommandsExtractorService commandsExtractorService,
      CommandParserService commandParserService) {

    var command = commandsExtractorService.getCommand(SubscribeCommand.class);

    return IntegrationFlow.from(subscribeChannel())
        .handle(chatFlowHandler, "checkChatStatus")
        .handle(commandParserService, "parseSubscribeCommand") // add headers for command
        .handle(command, "subscribe")
        .handle(replyCreatorService, "createReplyCommunitySubscriptionSuccessful")
        .handle(telegramBot, "sendMessage")
        .get();
  }

  //  @Bean
  //  public IntegrationFlow morphingFlow(
  //      XenomorphingService xenomorphingService,
  //      OvomorphSelector ovomorphSelector) {
  //    return IntegrationFlows
  //        .from(stage1Channel())
  //        .handle(xenomorphingService, "eggMorphing")
  //        // фильтр на заголовок, есть ли жертвы поблизости
  //        .filter(ovomorphSelector)
  //        // facehugger морфится в chestburster без дополнительных условий
  //        .handle(xenomorphingService, "facehuggerMorphing")
  //        // chestburster морфится в drone без дополнительных условий
  //        .handle(xenomorphingService, "chestbursterMorphing")
  //        // допустим, что жертв достаточно, поэтому меняем у объекта дрона готовность к
  // трансформации.
  //        // По-хорошему нужен отдельный сервис, который бы проверял внешние условия, что жертв
  //        // достаточно, в зависимости от, например, отдельного объекта, содержащего количество
  // жертв
  //        // с разными параметрами, которые, в свою очередь влияли на шкалу насыщенности дрона для
  // эволюции
  //        .<Drone, Drone>transform(drone -> Drone.builder()
  //            .age(drone.getAge())
  //            .name(drone.getName())
  //            .fedEnoughForEvolution(true)
  //            // добавляет еще и предрасположенность к типу эволюции
  //            .predispositionXenoType(XenomorphType.randomOfSentryOrWarrior())
  //            .build())
  //        // Добавить заголовок в зависимости от предрасположенности
  //        .enrichHeaders(h -> h.headerExpression(
  //            "predisposition",
  //            "#root.payload.predispositionXenoType.name()")
  //        )
  //        // В зависимости от заголовка сообщения, происходит роутинг в один из двух каналов по
  //        // эволюции дрона в преторианца или дробителя
  //        .route(droneRouter())
  //        .get();
  //  }
  //
  //
  //  // Флоу, описывающий трансформацию дрона в praetorian
  //  @Bean
  //  public IntegrationFlow morphingDroneToWarriorFlow(XenomorphingService xenomorphingService) {
  //    return IntegrationFlows
  //        .from(stage6WarriorChannel())
  //        .handle(xenomorphingService, "droneToWarriorMorphing")
  //        .handle(xenomorphingService, "warriorMorphing")
  //        .channel(xenoOutputChannel())
  //        .get();
  //  }

}
