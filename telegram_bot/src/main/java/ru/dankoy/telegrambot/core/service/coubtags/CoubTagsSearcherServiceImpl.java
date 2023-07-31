package ru.dankoy.telegrambot.core.service.coubtags;

import feign.FeignException.NotFound;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;
import ru.dankoy.telegrambot.core.feign.coubtagsfinder.CoubTagsSearcherFeign;


@RequiredArgsConstructor
@Service
public class CoubTagsSearcherServiceImpl implements CoubTagsSearcherService {

  private final CoubTagsSearcherFeign coubTagsSearcherFeign;

  @Override
  public Optional<Tag> findByTitle(String title) {

    try {

      return Optional.of(coubTagsSearcherFeign.searchByTitle(title));

    } catch (NotFound e) {
      return Optional.empty();
    }

  }
}
