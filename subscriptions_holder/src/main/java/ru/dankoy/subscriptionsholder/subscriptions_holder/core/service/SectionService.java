package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.Optional;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Section;

public interface SectionService {

  Optional<Section> getSectionByName(String name);

}
