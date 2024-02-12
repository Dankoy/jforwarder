package ru.dankoy.coubtagssearcher.core.service.channel;

import ru.dankoy.coubtagssearcher.core.domain.Channel;
import ru.dankoy.coubtagssearcher.core.domain.Tag;

public interface ChannelService {

  Channel findChannelByTitle(String title);

}
