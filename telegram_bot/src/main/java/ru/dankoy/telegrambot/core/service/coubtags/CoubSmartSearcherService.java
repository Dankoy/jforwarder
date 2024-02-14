package ru.dankoy.telegrambot.core.service.coubtags;

import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;

public interface CoubSmartSearcherService {

    Optional<Tag> findTagByTitle(String title);

    Optional<Channel> findByChannelPermalink(String permalink);
}
