package ru.dankoy.telegrambot.core.service.community;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.feign.subscriptionsholder.SubscriptionsHolderFeign;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@Slf4j
@RequiredArgsConstructor
@Service("communityServiceImpl")
public class CommunityServiceImpl implements CommunityService {

  private final SubscriptionsHolderFeign subscriptionsHolderFeign;

  @Override
  public List<Community> getAll() {
    return subscriptionsHolderFeign.getAllCommunities();
  }

  @Override
  public Optional<Community> getByName(String communityName) {
    try {
      return Optional.of(subscriptionsHolderFeign.getCommunityByName(communityName));
    } catch (NotFound e) {
      return Optional.empty();
    }
  }
}
