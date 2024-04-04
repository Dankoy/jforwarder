package ru.dankoy.telegrambot.core.service.channel;

import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;

public interface ChannelMaker {

  default Channel makeCorrectChannel() {

    return new Channel(0, "channel1", "permalink");
  }
}
