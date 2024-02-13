package ru.dankoy.coubtagssearcher.core.service.channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dankoy.coubtagssearcher.core.domain.Channel;
import ru.dankoy.coubtagssearcher.core.exceptions.ResourceNotFoundException;
import ru.dankoy.coubtagssearcher.core.feign.CoubSearchFeign;
import ru.dankoy.coubtagssearcher.core.service.Utils;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {

  private static final int PER_PAGE = 25;

  private final CoubSearchFeign searchFeign;

  @Override
  public Channel findChannelByTitle(String permalink) {

    int page = 1;
    long maxPage = Integer.MAX_VALUE;

    while (page <= maxPage) {

      var wrapper = searchFeign.getChannels(permalink, page, PER_PAGE);
      maxPage = wrapper.getTotalPages();

      log.info("wrapper {}", wrapper);

      var optionalFoundChannel =
          wrapper.getChannels().stream()
              .filter(channel -> channel.getPermalink().equals(permalink))
              .findFirst();

      log.info("Found channel: {}", optionalFoundChannel);

      if (optionalFoundChannel.isEmpty()) {

        log.info("On page '{}' channel '{}' not found", page, permalink);
        page++;
        Utils.sleep(3_000);

      } else {
        var foundChannel = optionalFoundChannel.get();
        log.info("Found channel: {}", foundChannel);
        return foundChannel;
      }
    }

    throw new ResourceNotFoundException(
        String.format("Couldn't find channel with permalink '%s'", permalink));
  }
}
