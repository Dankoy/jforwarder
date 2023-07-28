package ru.dankoy.coubtagssearcher.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dankoy.coubtagssearcher.core.domain.Tag;
import ru.dankoy.coubtagssearcher.core.exceptions.ResourceNotFoundException;
import ru.dankoy.coubtagssearcher.core.feign.CoubSmartSearchFeign;


@Slf4j
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

  private final CoubSmartSearchFeign coubSmartSearchFeign;

  @Override
  public Tag findTagByTitle(String title) {

    int page = 1;
    var wrapper = coubSmartSearchFeign.getTags(title, page);
    var maxPage = wrapper.getMeta().getTotalPages();

    while (page <= maxPage) {

      var optionalFoundTag = wrapper.getData().stream()
          .filter(tag -> tag.getTitle().equals(title))
          .findFirst();

      log.info("Found coubs: {}", wrapper);

      if (optionalFoundTag.isEmpty()) {
        log.info("On page '{}' tag '{}' not found", page, title);
        page++;
        sleep(3_000);

        wrapper = coubSmartSearchFeign.getTags(title, page);
      } else {
        var foundTag = optionalFoundTag.get();
        log.info("Found tag: {}", foundTag);
        return foundTag;
      }

    }

    throw new ResourceNotFoundException(
        String.format("Couldn't find tag with title '%s'", title));

  }

  private void sleep(long millis) {

    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Interrupted while trying to get coubs", e);
    }

  }
}
