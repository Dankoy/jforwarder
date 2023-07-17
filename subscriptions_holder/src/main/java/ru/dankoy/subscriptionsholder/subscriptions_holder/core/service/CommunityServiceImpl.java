package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;


import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.aspects.FilterActiveSubscriptions;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceConflictException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.exceptions.ResourceNotFoundException;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.CommunityRepository;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

  private final CommunityRepository communityRepository;

  private final TelegramChatService telegramChatService;

  private final SectionService sectionService;

  @Override
  @FilterActiveSubscriptions
  public List<Community> getAll() {
    return communityRepository.findAll();
  }

  @Override
  public List<Community> getByName(String name) {
    return communityRepository.getByName(name);

  }

  @Override
  public Community getByNameAndSectionName(String name, String sectionName) {
    var optional = communityRepository.getByNameAndSectionName(name, sectionName);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Not found - %s", name)));

  }

  @Override
  @Transactional
  public Community create(Community community) {

    var existingOptional = communityRepository.getByNameAndSectionName(community.getName(),
        community.getSection().getName());

    var sectionOptional = sectionService.getSectionByName(community.getSection().getName());

    var section = sectionOptional.orElseThrow(() -> new ResourceNotFoundException(
        String.format("Not found - %s", community.getSection().getName())));

    if (existingOptional.isPresent()) {
      throw new ResourceConflictException(
          String.format("Already exists - %s", community.getName()));
    } else {
      community.setSection(section);
      return communityRepository.save(community);
    }

  }

  @Override
  @Transactional
  public Community update(Community community) {
    return communityRepository.save(community);
  }


  @Override
  @Transactional
  public void delete(String name, String sectionName) {

    var existingOptional = communityRepository.getByNameAndSectionName(name, sectionName);

    existingOptional.ifPresent(communityRepository::delete);

  }


}
