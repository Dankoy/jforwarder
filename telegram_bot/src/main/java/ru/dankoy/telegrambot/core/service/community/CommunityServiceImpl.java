package ru.dankoy.telegrambot.core.service.community;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.telegrambot.core.domain.subscription.Community;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

@RequiredArgsConstructor
@Service
public class CommunityServiceImpl implements CommunityService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public List<Community> getAll() {
    return subscriptionsHolderFeign.getAllCommunities();
  }
}
