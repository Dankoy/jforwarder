package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

public interface CommunityMaker extends SectionMaker {

  default List<Community> correctCommunities() {

    Set<Section> sections = makeCorrectSections();

    return Stream.of(
            new Community(1L, 32L, "animals-pets", sections),
            new Community(2L, 113L, "blogging", sections),
            new Community(3L, 115L, "standup-jokes", sections),
            new Community(4L, 37L, "mashup", sections),
            new Community(5L, 36L, "anime", sections),
            new Community(6L, 19L, "movies", sections),
            new Community(7L, 17L, "gaming", sections),
            new Community(8L, 14L, "cartoons", sections),
            new Community(9L, 2L, "art", sections),
            new Community(10L, 114L, "live-pictures", sections),
            new Community(11L, 8L, "music", sections),
            new Community(12L, 12L, "sports", sections),
            new Community(13L, 76L, "science-technology", sections),
            new Community(14L, 112L, "food-kitchen", sections),
            new Community(15L, 39L, "celebrity", sections),
            new Community(16L, 9L, "nature-travel", sections),
            new Community(17L, 16L, "fashion", sections),
            new Community(18L, 17L, "dance", sections),
            new Community(19L, 75L, "cars", sections),
            new Community(20L, 19L, "memes", sections))
        .toList();
  }
}
