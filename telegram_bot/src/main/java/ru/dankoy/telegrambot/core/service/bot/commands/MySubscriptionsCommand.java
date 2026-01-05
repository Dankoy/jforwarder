package ru.dankoy.telegrambot.core.service.bot.commands;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.domain.subscription.community.CommunitySubscription;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.dto.flow.CreateReplyMySubscriptionsDto;
import ru.dankoy.telegrambot.core.dto.flow.MySubscriptionsDto;
import ru.dankoy.telegrambot.core.service.subscription.ChannelSubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.CommunitySubscriptionService;
import ru.dankoy.telegrambot.core.service.subscription.TagSubscriptionService;

@Slf4j
public class MySubscriptionsCommand extends BotCommand {

  @Qualifier("CommunitySubscriptionServiceHttpClient")
  private final transient CommunitySubscriptionService communitySubscriptionService;

  @Qualifier("TagSubscriptionServiceHttpClient")
  private final transient TagSubscriptionService tagSubscriptionService;

  @Qualifier("channelSubscriptionServiceHttpClient")
  private final transient ChannelSubscriptionService channelSubscriptionService;

  public MySubscriptionsCommand(
      String command,
      String description,
      CommunitySubscriptionService communitySubscriptionService,
      TagSubscriptionService tagSubscriptionService,
      ChannelSubscriptionService channelSubscriptionService) {
    super(command, description);
    this.channelSubscriptionService = channelSubscriptionService;
    this.communitySubscriptionService = communitySubscriptionService;
    this.tagSubscriptionService = tagSubscriptionService;
  }

  public CreateReplyMySubscriptionsDto mySubscriptions(Message inputMessage) {

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

    return new CreateReplyMySubscriptionsDto(
        inputMessage, new MySubscriptionsDto(subs, tagSubs, channelSubs));
  }
}
