package ru.dankoy.telegrambot.core.service.subscription;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.domain.subscription.community.Section;

public interface CommunityMaker extends SectionMaker {

  default List<Community> correctCommunities() {

    Set<Section> sections = makeCorrectSections();

    return Stream.of(
            new Community(1L, 75L, "cars", sections), new Community(2L, 19L, "memes", sections))
        .toList();
  }

  default Community findCorrectCommunityByName(String name) {

    Optional<Community> expectedOptional =
        correctCommunities().stream().filter(c -> c.getName().equals(name)).findFirst();

    return expectedOptional.orElseThrow(() -> new RuntimeException("Not found " + name));
  }

  default Community findCorrectCommunityByNameAndSectionName(String name, String sectionName) {

    Optional<Community> expectedOptional =
        correctCommunities().stream().filter(c -> c.getName().equals(name)).findFirst();

    expectedOptional.ifPresentOrElse(
        c ->
            c.setSections(
                c.getSections().stream()
                    .filter(s -> s.getName().equals(sectionName))
                    .collect(Collectors.toSet())),
        () -> {
          throw new RuntimeException("Not found " + name);
        });

    return expectedOptional.orElseThrow();
  }
}
