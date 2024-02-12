package ru.dankoy.coubtagssearcher.core.service.channel;

import ru.dankoy.coubtagssearcher.core.domain.Channel;

public interface ChannelService {

  Channel findChannelByTitle(String title);
}
