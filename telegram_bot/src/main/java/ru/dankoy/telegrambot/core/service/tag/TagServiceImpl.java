package ru.dankoy.telegrambot.core.service.tag;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public Optional<Tag> findTagByTitle(String title) {

    try {
      return Optional.of(subscriptionsHolderFeign.getTagByTitle(title));
    } catch (NotFound e) {
      return Optional.empty();
    }
  }

  @Override
  public TagSubscription subscribeByTag(TagSubscription tagSubscription) {

    return subscriptionsHolderFeign.subscribeByTag(tagSubscription);
  }

  @Override
  public void unsubscribeByTag(TagSubscription tagSubscription) {

    subscriptionsHolderFeign.unsubscribeByTag(tagSubscription);
  }

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public List<TagSubscription> getAllSubscriptionsByChat(long chatId) {

    return subscriptionsHolderFeign.getAllTagSubscriptionsByChatId(chatId);
  }

  @Override
  public List<TagSubscription> getAllSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {
    return subscriptionsHolderFeign.getAllTagSubscriptionsByChatIdAndMessageThreadId(
        chatId, messageThreadId);
  }

  @Override
  public Tag create(Tag tag) {
    return subscriptionsHolderFeign.createTag(tag);
  }
}
