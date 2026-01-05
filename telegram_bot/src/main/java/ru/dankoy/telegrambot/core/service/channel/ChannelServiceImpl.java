package ru.dankoy.telegrambot.core.service.channel;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.channel.ChannelSubscription;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public Optional<Channel> findChannelByPermalink(String permalink) {

    try {
      return Optional.of(subscriptionsHolderFeign.getChannelByPermalink(permalink));
    } catch (NotFound e) {
      return Optional.empty();
    }
  }

  @Override
  public ChannelSubscription subscribeByChannel(ChannelSubscription channelSubscription) {

    return subscriptionsHolderFeign.subscribeByChannel(channelSubscription);
  }

  @Override
  public void unsubscribeByChannel(ChannelSubscription channelSubscription) {

    subscriptionsHolderFeign.unsubscribeByChannel(channelSubscription);
  }

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public List<ChannelSubscription> getAllSubscriptionsByChat(long chatId) {

    return subscriptionsHolderFeign.getAllChannelSubscriptionsByChatId(chatId);
  }

  /**
   * @deprecated chat is in separate microservice and db
   */
  @Deprecated(since = "2025-02-28", forRemoval = false)
  @Override
  public List<ChannelSubscription> getAllSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {

    return subscriptionsHolderFeign.getAllChannelSubscriptionsByChatIdAndMessageThreadId(
        chatId, messageThreadId);
  }

  @Override
  public Channel create(Channel channel) {
    return subscriptionsHolderFeign.createChannel(channel);
  }
}
