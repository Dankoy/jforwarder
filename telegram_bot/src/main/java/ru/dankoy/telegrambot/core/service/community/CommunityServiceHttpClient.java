package ru.dankoy.telegrambot.core.service.community;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;
import ru.dankoy.telegrambot.core.httpservice.subscriptionsholder.SubscriptionsHolderCommunityHttpService;

@Slf4j
@RequiredArgsConstructor
@Service("communityServiceHttpClient")
public class CommunityServiceHttpClient implements CommunityService {

  private final SubscriptionsHolderCommunityHttpService subscriptionsHolderCommunityHttpService;

  @Override
  public List<Community> getAll() {
    return subscriptionsHolderCommunityHttpService.getAllCommunities();
  }

  @Override
  public Optional<Community> getByName(String communityName) {
    try {
      return Optional.of(subscriptionsHolderCommunityHttpService.getCommunityByName(communityName));
    } catch (NotFound e) {
      return Optional.empty();
    }
  }
}
