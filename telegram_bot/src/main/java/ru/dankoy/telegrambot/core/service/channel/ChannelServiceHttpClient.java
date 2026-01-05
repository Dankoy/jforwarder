package ru.dankoy.telegrambot.core.service.channel;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderChannelHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderChannelSubHttpService;

@RequiredArgsConstructor
@Service("channelServiceHttpClient")
public class ChannelServiceHttpClient implements ChannelService {

  private final SubscriptionsHolderChannelSubHttpService subHttpService;
  private final SubscriptionsHolderChannelHttpService channelHttpService;

  @Override
  public Optional<Channel> findChannelByPermalink(String permalink) {

    try {
      return Optional.of(channelHttpService.getChannelByPermalink(permalink));
    } catch (NotFound e) {
      return Optional.empty();
    }
  }

  @Override
  public ChannelSubscription subscribeByChannel(ChannelSubscription channelSubscription) {

    return subHttpService.subscribeByChannel(channelSubscription);
  }

  @Override
  public void unsubscribeByChannel(ChannelSubscription channelSubscription) {

    subHttpService.unsubscribeByChannel(channelSubscription);
  }

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public List<ChannelSubscription> getAllSubscriptionsByChat(long chatId) {

    throw new UnsupportedOperationException("This method is deprecated and should not be used");
  }

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @Override
  public List<ChannelSubscription> getAllSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {

    return subHttpService.getAllChannelSubscriptionsByChatIdAndMessageThreadId(
        chatId, messageThreadId);
  }

  @Override
  public Channel create(Channel channel) {
    return channelHttpService.createChannel(channel);
  }
}
