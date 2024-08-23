package ru.dankoy.telegrambot.config.integration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.advice.RateLimiterRequestHandlerAdvice;
import org.springframework.messaging.MessageChannel;
import ru.dankoy.telegrambot.core.domain.message.ChannelSubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.CommunitySubscriptionMessage;
import ru.dankoy.telegrambot.core.domain.message.CoubMessage;
import ru.dankoy.telegrambot.core.domain.message.TagSubscriptionMessage;
import ru.dankoy.telegrambot.core.service.bot.TelegramBot;
import ru.dankoy.telegrambot.core.service.reply.ReplyCreatorService;

/**
 * @author dankoy
 *     <p>Should work in sync mode. It was made to fix bug <a
 *     href="https://github.com/Dankoy/jforwarder/issues/126">...</a>
 */
@Slf4j
@Configuration
public class FlowSyncConfig {

  @Bean
  public MessageChannel subscriptionMessagesChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel sendMessageDirectChannel() {
    // this channel send messages as soon they created.
    //  With queue integration flow waited 5 seconds every time before handle message.
    return new DirectChannel();
  }

  @Bean
  public MessageChannel communitySubscriptionSendDirectChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel tagSubscriptionSendDirectChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel channelSubscriptionSendDirectChannel() {
    return new DirectChannel();
  }

  // rate limiter for one message in 2 second
  @Bean
  public RateLimiterRequestHandlerAdvice rateLimiterRequestOneInTwoSecondsHandlerAdvice() {
    return new RateLimiterRequestHandlerAdvice(
        RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(2))
            .limitForPeriod(1)
            .build());
  }

  @Bean
  public IntegrationFlow sendMessageSyncFlow(TelegramBot telegramBot) {
    return IntegrationFlow.from(sendMessageDirectChannel())
        .handle(
            telegramBot,
            "sendMessage",
            c -> c.advice(rateLimiterRequestOneInTwoSecondsHandlerAdvice()))
        .get();
  }

  @Bean
  public IntegrationFlow subscriptionMessagesFlow() {

    return IntegrationFlow.from(subscriptionMessagesChannel())
        .<CoubMessage, Class<?>>route(
            CoubMessage::getClass,
            m ->
                m.channelMapping(
                        CommunitySubscriptionMessage.class,
                        "communitySubscriptionSendDirectChannel")
                    .channelMapping(
                        TagSubscriptionMessage.class, "tagSubscriptionSendDirectChannel")
                    .channelMapping(
                        ChannelSubscriptionMessage.class, "channelSubscriptionSendDirectChannel"))
        .get();
  }

  @Bean
  public IntegrationFlow communitySubscriptionSendMessageFlow(
      ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(communitySubscriptionSendDirectChannel())
        .handle(replyCreatorService, "createCommunitySubscriptionMessage")
        .channel(sendMessageDirectChannel())
        .get();
  }

  @Bean
  public IntegrationFlow tagSubscriptionSendMessageFlow(ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(tagSubscriptionSendDirectChannel())
        .handle(replyCreatorService, "createTagSubscriptionMessage")
        .channel(sendMessageDirectChannel())
        .get();
  }

  @Bean
  public IntegrationFlow channelSubscriptionSendMessageFlow(
      ReplyCreatorService replyCreatorService) {

    return IntegrationFlow.from(channelSubscriptionSendDirectChannel())
        .handle(replyCreatorService, "createChannelSubscriptionMessage")
        .channel(sendMessageDirectChannel())
        .get();
  }
}
