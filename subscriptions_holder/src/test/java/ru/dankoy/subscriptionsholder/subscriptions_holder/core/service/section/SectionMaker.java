package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.section;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

public interface SectionMaker {

  default Set<Section> makeCorrectSections() {

    return Stream.of(new Section(1L, "daily"), new Section(2L, "weekly"))
        .collect(Collectors.toSet());
  }
}
