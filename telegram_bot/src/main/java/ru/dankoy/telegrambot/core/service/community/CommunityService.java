package ru.dankoy.telegrambot.core.service.community;

import java.util.List;
import ru.dankoy.telegrambot.core.domain.subscription.Community;

public interface CommunityService {

  List<Community> getAll();

}
