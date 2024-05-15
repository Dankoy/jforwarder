package ru.dankoy.telegrambot.config.integration;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplySubscribeDto;
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
import ru.dankoy.telegrambot.core.service.flow.MessageTransformer;
import ru.dankoy.telegrambot.core.service.reply.ReplyCreatorService;

@Slf4j
@Configuration
public class FlowConfig {

  // Поток в который поступают сообщения из чатов
  @Bean
  public MessageChannel inputMessageChannel() {
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel mySubscriptionsChannel() {
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel startChannel() {
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel helpChannel() {
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel subscribeChannel() {
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel messageSourceLocalizationChannel() {
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel freemarkerLocalizationChannel() {
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel sendMessageChannel() {
    // this channel send messages as soon they created.
    //  With queue integration flow waited 5 seconds every time before handle message.
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel commandHelpChannel() {
    // this channel send messages as soon they created.
    //  With queue integration flow waited 5 seconds every time before handle message.
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel communitySubscriptionSuccessChannel() {
    // this channel send messages as soon they created.
    //  With queue integration flow waited 5 seconds every time before handle message.
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel tagSubscriptionSuccessChannel() {
    // this channel send messages as soon they created.
    //  With queue integration flow waited 5 seconds every time before handle message.
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public MessageChannel channelSubscriptionSuccessChannel() {
    // this channel send messages as soon they created.
    //  With queue integration flow waited 5 seconds every time before handle message.
    return new PublishSubscribeChannel(executor());
  }

  @Bean
  public ThreadPoolTaskExecutor executor() {
    // Make pub-sub channels async. Otherwise all work happens in caller thread.
    ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
    pool.setCorePoolSize(10);
    pool.setMaxPoolSize(10);
    pool.setWaitForTasksToCompleteOnShutdown(true);
    return pool;
  }

  //  @Bean(name = PollerMetadata.DEFAULT_POLLER)
  //  public PollerMetadata defaultPoller() {
  //
  //    PollerMetadata pollerMetadata = new PollerMetadata();
  //    pollerMetadata.setTrigger(new PeriodicTrigger(Duration.ofMillis(300)));
  //    return pollerMetadata;
  //  }

  //  @Bean(name = PollerMetadata.DEFAULT_POLLER)
  //  public PollerMetadata poller() {
  //    return Pollers.fixedDelay(200).getObject();
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

  // дефолтный обработчик событий
  @Bean
  public IntegrationFlow botExceptionsFlow() {
    return IntegrationFlow.from("errorChannel") // default error channel
        .<MessageHandlingException, Class<?>>route(
            errorMessage -> errorMessage.getCause().getClass(),
            m ->
                m.channelMapping(BotFlowException.class, messageSourceLocalizationChannel())
                    .channelMapping(BotCommandFlowException.class, freemarkerLocalizationChannel()))
        .get();
  }

  @Bean
  public IntegrationFlow errorMessageSourceLocalizationFlow(
      ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(messageSourceLocalizationChannel())
        .handle(replyCreatorService, "replyWithMessageSourceOnException")
        .channel(sendMessageChannel())
        .get();
  }

  @Bean
  public IntegrationFlow errorFreemarkerLocalizationFlow(ReplyCreatorService replyCreatorService) {
    return IntegrationFlow.from(freemarkerLocalizationChannel())
        .handle(replyCreatorService, "replyWithFreemarkerOnException")
        .channel(sendMessageChannel())
        .get();
  }

  @Bean
  public IntegrationFlow commandHelpFlow(ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(freemarkerLocalizationChannel())
        .filter(
            MessagingException.class,
            m -> Objects.nonNull(m.getFailedMessage().getHeaders().get("commandString")))
        .handle(replyCreatorService, "replyWithHelpOnBotCommandFlowException")
        .channel(sendMessageChannel())
        .get();
  }

  @Bean
  public IntegrationFlow sendMessageFlow(TelegramBot telegramBot) {
    return IntegrationFlow.from(sendMessageChannel()).handle(telegramBot, "sendMessage").get();
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
      ChatFlowHandler chatFlowHandler) {

    var command = commandsExtractorService.getCommand(MySubscriptionsCommand.class);

    return IntegrationFlow.from(mySubscriptionsChannel())
        .handle(chatFlowHandler, "checkChatStatus")
        .handle(command, "mySubscriptions")
        .handle(replyCreatorService, "createReplyMySubscriptions")
        .channel(sendMessageChannel())
        .get();
  }

  // FLow for processing start command
  @Bean
  public IntegrationFlow startFlow(
      CommandsExtractorService commandsExtractorService, ReplyCreatorService replyCreatorService) {

    var command = commandsExtractorService.getCommand(StartCommand.class);

    return IntegrationFlow.from(startChannel())
        .handle(command, "start")
        .handle(replyCreatorService, "createReplyStart")
        .channel(sendMessageChannel())
        .get();
  }

  // FLow for processing help command
  @Bean
  public IntegrationFlow helpFlow(ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(helpChannel())
        .handle(replyCreatorService, "createReplyHelp")
        .channel(sendMessageChannel())
        .get();
  }

  // FLow for processing subscribe command
  @Bean
  public IntegrationFlow subscribeFlow(
      ChatFlowHandler chatFlowHandler,
      CommandsExtractorService commandsExtractorService,
      CommandParserService commandParserService,
      MessageTransformer messageTransformer) {

    var command = commandsExtractorService.getCommand(SubscribeCommand.class);

    return IntegrationFlow.from(subscribeChannel())
        .handle(chatFlowHandler, "checkChatStatus")
        .handle(commandParserService, "parseSubscribeCommand") // add headers for command
        // добавляет строку команды по которой, если произошла ошибка можно отправить help по
        // команде
        .transform(messageTransformer, "addCommandStringToHeaders")
        .handle(command, "subscribe")
        .<CreateReplySubscribeDto, Class<?>>route(
            m -> m.subscriptionDto().subscription().getClass()

            //                Objects.requireNonNull(
            //                        m.getHeaders()
            //                            .get(
            //
            // CommandConstraints.COMMAND_SUBSCRIPTION_TYPE_FIELD.getConstraint()))
            //                    .toString()
            ,
            m ->
                m.channelMapping(CommunitySubscription.class, "communitySubscriptionSuccessChannel")
                    .channelMapping(TagSubscription.class, "tagSubscriptionSuccessChannel")
                    .channelMapping(ChannelSubscription.class, "channelSubscriptionSuccessChannel")
            //                m.channelMapping(
            //                        SubscriptionType.COMMUNITY.getType(),
            // "communitySubscriptionSuccessChannel")
            //                    .channelMapping(SubscriptionType.TAG.getType(),
            // "tagSubscriptionSuccessChannel")
            //                    .channelMapping(
            //                        SubscriptionType.CHANNEL.getType(),
            // "channelSubscriptionSuccessChannel")
            )
        .get();
  }

  @Bean
  public IntegrationFlow communitySubscriptionSuccessFlow(ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(communitySubscriptionSuccessChannel())
        .handle(replyCreatorService, "createReplyCommunitySubscriptionSuccessful")
        .channel(sendMessageChannel())
        .get();
  }

  @Bean
  public IntegrationFlow tagSubscriptionSuccessFlow(ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(tagSubscriptionSuccessChannel())
        .handle(replyCreatorService, "createReplyTagSubscriptionSuccessful")
        .channel(sendMessageChannel())
        .get();
  }

  @Bean
  public IntegrationFlow channelSubscriptionSuccessFlow(ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(channelSubscriptionSuccessChannel())
        .handle(replyCreatorService, "createReplyChannelSubscriptionSuccessful")
        .channel(sendMessageChannel())
        .get();
  }
}
