package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Tag;

public interface TagMaker {

  default Tag makeCorrectTag(String title) {

    return new Tag(0L, title);
  }
}
