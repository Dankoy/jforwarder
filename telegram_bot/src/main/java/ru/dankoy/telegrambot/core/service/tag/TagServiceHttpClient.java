package ru.dankoy.telegrambot.core.service.tag;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderTagHttpService;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderTagSubHttpService;

@RequiredArgsConstructor
@Service
public class TagServiceHttpClient implements TagService {

  private final SubscriptionsHolderTagHttpService subscriptionsHolderTagHttpService;

  private final SubscriptionsHolderTagSubHttpService subscriptionsHolderTagSubHttpService;

  @Override
  public Optional<Tag> findTagByTitle(String title) {

    try {
      return Optional.of(subscriptionsHolderTagHttpService.getTagByTitle(title));
    } catch (NotFound e) {
      return Optional.empty();
    }
  }

  @Override
  public TagSubscription subscribeByTag(TagSubscription tagSubscription) {

    return subscriptionsHolderTagSubHttpService.subscribeByTag(tagSubscription);
  }

  @Override
  public void unsubscribeByTag(TagSubscription tagSubscription) {

    subscriptionsHolderTagSubHttpService.unsubscribeByTag(tagSubscription);
  }

  /**
   * @deprecated for topics support via messageThreadId
   */
  @Deprecated(since = "2024-04-05", forRemoval = true)
  @Override
  public List<TagSubscription> getAllSubscriptionsByChat(long chatId) {

    throw new UnsupportedOperationException("Deprecated method");
  }

  @Override
  public List<TagSubscription> getAllSubscriptionsByChatIdAndMessageThreadId(
      long chatId, Integer messageThreadId) {
    return subscriptionsHolderTagSubHttpService.getAllTagSubscriptionsByChatIdAndMessageThreadId(
        chatId, messageThreadId);
  }

  @Override
  public Tag create(Tag tag) {
    return subscriptionsHolderTagHttpService.createTag(tag);
  }
}
