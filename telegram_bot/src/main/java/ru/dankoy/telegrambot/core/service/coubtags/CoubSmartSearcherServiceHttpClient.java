package ru.dankoy.telegrambot.core.service.coubtags;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;
import ru.dankoy.telegrambot.core.httpservice.coubsmartsearcher.CoubSmartSearcherHttpService;

@RequiredArgsConstructor
@Service("coubSmartSearcherServiceHttpClient")
public class CoubSmartSearcherServiceHttpClient implements CoubSmartSearcherService {

  private final CoubSmartSearcherHttpService coubSmartSearcherFeign;

  @Override
  public Optional<Tag> findTagByTitle(String title) {

    try {

      return Optional.of(coubSmartSearcherFeign.searchTagByTitle(title));

    } catch (NotFound e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Channel> findByChannelPermalink(String permalink) {

    try {

      return Optional.of(coubSmartSearcherFeign.searchChannelByPermalink(permalink));

    } catch (NotFound e) {
      return Optional.empty();
    }
  }
}
