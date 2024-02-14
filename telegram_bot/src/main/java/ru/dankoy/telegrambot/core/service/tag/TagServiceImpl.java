package ru.dankoy.telegrambot.core.service.tag;


import feign.FeignException.NotFound;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;
import ru.dankoy.telegrambot.core.domain.subscription.tag.TagSubscription;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

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

  @Override
  public List<TagSubscription> getAllSubscriptionsByChat(long chatId) {

    return subscriptionsHolderFeign.getAllTagSubscriptionsByChatId(chatId);
  }

  @Override
  public Tag create(Tag tag) {
    return subscriptionsHolderFeign.createTag(tag);
  }

}
