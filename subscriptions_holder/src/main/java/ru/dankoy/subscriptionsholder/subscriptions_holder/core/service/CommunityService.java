package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import java.util.Set;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.community.Section;

public interface CommunityService {

    List<Community> getAll();

    Community getByName(String name);

    Community getByNameAndSectionName(String name, String sectionName);

    Community getByNameAndSectionIn(String name, Set<Section> sections);

    Community create(Community community);

    Community update(Community community);

    void delete(String name, String sectionName);

    void delete(String name);
}
