package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;

public interface ChannelService {

  Channel getByTitle(String title);

  Channel getByPermalink(String permalink);

  Channel create(Channel tag);

  Channel modify(Channel tag);

  void deleteByTitle(String title);

  void deleteByPermalink(String title);
}
