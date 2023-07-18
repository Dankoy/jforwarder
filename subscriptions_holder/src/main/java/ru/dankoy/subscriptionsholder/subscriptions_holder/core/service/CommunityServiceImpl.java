package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;


import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Section;
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
  public List<Community> getAll() {
    return communityRepository.findAll();
  }

  @Override
  public Community getByName(String name) {
    var optional = communityRepository.getByName(name);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Not found - %s", name)));

  }

  @Override
  public Community getByNameAndSectionName(String name, String sectionName) {
    var optional = communityRepository.getByNameAndSectionsName(name, sectionName);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(String.format("Not found - %s", name)));

  }

  @Override
  public Community getByNameAndSectionIn(String name, Set<Section> sections) {

    var sectionNames = sections.stream()
        .map(Section::getName)
        .collect(Collectors.toSet());

    var optional = communityRepository.getByNameAndSections(name, sectionNames);

    return optional.orElseThrow(
        () -> new ResourceNotFoundException(
            String.format(
                "Not found community %s with sections '%s'",
                name,
                sectionNames)));
  }

  @Override
  @Transactional
  public Community create(Community community) {

    var sectionNames = community.getSections().stream()
        .map(Section::getName)
        .collect(Collectors.toSet());

    var existingOptional = communityRepository
        .getByNameAndSections(
            community.getName(),
            sectionNames);

    List<Section> foundSections = sectionService.getBySectionNames(community.getSections());
    List<Section> mySections = new ArrayList<>(community.getSections());

    foundSections.sort((c1, c2) -> c2.getName().compareTo(c1.getName()));
    mySections.sort((c1, c2) -> c2.getName().compareTo(c1.getName()));

    if (!mySections.equals(foundSections)) {
      throw new ResourceNotFoundException(
          String.format("Expected sections to be found '%s', but got '%s'",
              mySections.stream()
                  .map(s -> s.getName() + ", ")
                  .toList(),
              foundSections.stream()
                  .map(s -> s.getName() + ", ")
                  .toList()
          )
      );
    }

    if (existingOptional.isPresent()) {
      throw new ResourceConflictException(
          String.format("Already exists - %s and '%s'", community.getName(),
              community.getSections().stream()
                  .map(s -> s.getName() + ", ")
                  .toList()));
    } else {

      Set<Section> foundSectionsSet = new HashSet<>(foundSections);

      community.setSections(foundSectionsSet);
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

    var existingOptional = communityRepository.getByNameAndSectionsName(name, sectionName);

    existingOptional.ifPresent(communityRepository::delete);

  }

  @Override
  @Transactional
  public void delete(String name) {

    var optional = communityRepository.getByName(name);

    optional.ifPresent(communityRepository::delete);

  }


}
