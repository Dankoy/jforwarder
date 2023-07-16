package ru.dankoy.subscriptionsholder.subscriptions_holder.core.service;

import java.util.List;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Community;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TelegramChat;

public interface CommunityService {

  List<Community> getAll();

  Community getByName(String name);

  Community create(Community community);

  Community update(Community community);

  Community addChat(String communityName, TelegramChat chat);

  void delete(String name);

  void deleteChatFromCommunity(String communityName, TelegramChat chat);
}
