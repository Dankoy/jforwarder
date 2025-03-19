package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.section;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.section.SectionRepository;

@RequiredArgsConstructor
@Service
public class SectionServiceImpl implements SectionService {

  private final SectionRepository sectionRepository;

  @Override
  public Optional<Section> getSectionByName(String name) {
    return sectionRepository.getByName(name);
  }

  @Override
  public List<Section> getBySectionNames(Set<Section> sections) {

    return sectionRepository.getByNameIsIn(
        sections.stream().map(Section::getName).collect(Collectors.toSet()));
  }
}
