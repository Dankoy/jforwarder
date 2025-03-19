package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel;

import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;

public interface ChannelMaker {

  default Channel makeCorrectChannel() {

    return new Channel(0, "channel1", "permalink");
  }
}
