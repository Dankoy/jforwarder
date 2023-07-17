package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;

public interface CommunityService {

  List<Community> getAll();

  List<Community> getByName(String name);

  Community getByNameAndSectionName(String name, String sectionName);

  Community create(Community community);

  Community update(Community community);

  void delete(String name, String sectionName);

}
