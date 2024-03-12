package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

public interface SectionMaker {

  default Set<Section> makeCorrectSections() {

    return Stream.of(
            new Section(1L, "daily"),
            new Section(2L, "weekly"),
            new Section(3L, "monthly"),
            new Section(4L, "quarter"),
            new Section(5L, "half"),
            new Section(6L, "rising"),
            new Section(7L, "fresh"))
        .collect(Collectors.toSet());
  }
}
