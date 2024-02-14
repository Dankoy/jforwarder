package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

public interface SectionService {

    Optional<Section> getSectionByName(String name);

    List<Section> getBySectionNames(Set<Section> sections);
}
