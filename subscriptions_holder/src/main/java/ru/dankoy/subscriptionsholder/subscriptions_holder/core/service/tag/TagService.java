package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.tag;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Tag;

public interface TagService {

  Tag getByTitle(String title);

  Tag create(Tag tag);

  Tag modify(Tag tag);

  void deleteByTitle(String title);
}
