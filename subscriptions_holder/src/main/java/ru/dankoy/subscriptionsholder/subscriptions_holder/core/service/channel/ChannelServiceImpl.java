package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.channel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.channel.Channel;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.channel.ChannelRepository;

@RequiredArgsConstructor
@Service
public class ChannelServiceImpl implements ChannelService {

  private final ChannelRepository channelRepository;

  @Override
  public Channel getByTitle(String title) {
    var optional = channelRepository.getByTitle(title);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Channel not found - '%s'", title)));
  }

  @Override
  public Channel getByPermalink(String title) {
    var optional = channelRepository.getByPermalink(title);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Channel not found - '%s'", title)));
  }

  @Transactional
  @Override
  public Channel create(Channel tag) {

    var optional = channelRepository.getByTitle(tag.getTitle());

    optional.ifPresent(
        t -> {
          throw new ResourceConflictException(
              String.format("Channel already exists - '%s'", t.getTitle()));
        });

    return channelRepository.save(tag);
  }

  @Transactional
  @Override
  public Channel modify(Channel tag) {

    var optional = channelRepository.getByTitle(tag.getTitle());

    optional.ifPresentOrElse(
        t -> channelRepository.save(tag),
        () -> {
          throw new ResourceNotFoundException(
              String.format("Channel not found - '%s'", tag.getTitle()));
        });

    return channelRepository.save(tag);
  }

  @Override
  @Transactional
  public void deleteByTitle(String title) {

    var optional = channelRepository.getByTitle(title);

    optional.ifPresent(channelRepository::delete);
  }

  @Override
  @Transactional
  public void deleteByPermalink(String permalink) {

    var optional = channelRepository.getByPermalink(permalink);

    optional.ifPresent(channelRepository::delete);
  }
}
