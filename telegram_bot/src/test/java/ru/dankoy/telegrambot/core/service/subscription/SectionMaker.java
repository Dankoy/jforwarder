package ru.dankoy.telegrambot.core.service.subscription;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.dankoy.telegrambot.core.domain.subscription.community.Section;

public interface SectionMaker {

  default Set<Section> makeCorrectSections() {

    return Stream.of(new Section(1L, "daily"), new Section(2L, "weekly"))
        .collect(Collectors.toSet());
  }
}
