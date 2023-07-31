package ru.dankoy.telegrambot.core.service.tag;

import java.util.List;
import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;
import ru.dankoy.telegrambot.core.domain.tagsubscription.TagSubscription;

public interface TagService {


  Optional<Tag> findTagByTitle(String title);

  TagSubscription subscribeByTag(TagSubscription tagSubscription);

  void unsubscribeByTag(TagSubscription tagSubscription);

  List<TagSubscription> getAllSubscriptionsByChat(long chatId);

  Tag create(Tag tag);
}
