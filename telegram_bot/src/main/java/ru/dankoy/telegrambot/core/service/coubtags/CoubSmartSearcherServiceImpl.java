package ru.dankoy.telegrambot.core.service.coubtags;

import feign.FeignException.NotFound;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.channel.Channel;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;
import ru.dankoy.telegrambot.core.feign.coubtagsfinder.CoubSmartSearcherFeign;

@RequiredArgsConstructor
@Service
public class CoubSmartSearcherServiceImpl implements CoubSmartSearcherService {

  private final CoubSmartSearcherFeign coubSmartSearcherFeign;

  @Override
  public Optional<Tag> findTagByTitle(String title) {

    try {

      return Optional.of(coubSmartSearcherFeign.searchTagByTitle(title));

    } catch (NotFound e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Channel> findByChannelTitle(String title) {

    try {

      return Optional.of(coubSmartSearcherFeign.searchChannelByTitle(title));

    } catch (NotFound e) {
      return Optional.empty();
    }
  }
}
