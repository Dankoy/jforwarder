package ru.dankoy.telegrambot.core.service.community;

import java.util.List;
import java.util.Optional;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;

public interface CommunityService {

    List<Community> getAll();

    Optional<Community> getByName(String communityName);
}
