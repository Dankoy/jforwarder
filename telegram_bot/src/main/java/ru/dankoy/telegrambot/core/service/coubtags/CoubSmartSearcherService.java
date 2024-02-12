package ru.dankoy.telegrambot.core.service.coubtags;

import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.channel.Channel;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;

public interface CoubSmartSearcherService {

  Optional<Tag> findTagByTitle(String title);

  Optional<Channel> findByChannelTitle(String title);
}
