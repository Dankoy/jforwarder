package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.tag.Tag;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.tag.TagRepository;

@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Override
  public Tag getByTitle(String title) {
    var optional = tagRepository.getByTitle(title);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Tag not found - '%s'", title)));
  }

  @Transactional
  @Override
  public Tag create(Tag tag) {

    var optional = tagRepository.getByTitle(tag.getTitle());

    optional.ifPresent(
        t -> {
          throw new ResourceConflictException(
              String.format("Tag already exists - '%s'", t.getTitle()));
        });

    return tagRepository.save(tag);
  }

  @Transactional
  @Override
  public Tag modify(Tag tag) {

    var optional = tagRepository.findById(tag.getId());

    optional.ifPresentOrElse(
        t -> tagRepository.save(tag),
        () -> {
          throw new ResourceNotFoundException(
              String.format("Tag not found - '%s'", tag.getTitle()));
        });

    return tag;
  }

  @Override
  public void deleteByTitle(String title) {

    var optional = tagRepository.getByTitle(title);

    optional.ifPresent(tagRepository::delete);
  }
}
