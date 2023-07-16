package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Section;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.repository.SectionRepository;


@RequiredArgsConstructor
@Service
public class SectionServiceImpl implements SectionService {

  private final SectionRepository sectionRepository;

  @Override
  public Optional<Section> getSectionByName(String name) {
    return sectionRepository.getByName(name);
  }
}
