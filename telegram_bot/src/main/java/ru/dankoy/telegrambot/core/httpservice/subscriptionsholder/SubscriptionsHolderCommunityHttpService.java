package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import ru.dankoy.telegrambot.core.domain.subscription.community.Community;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderCommunityHttpService {

  @GetExchange(url = "/api/v1/communities/{name}")
  Community getCommunityByName(@PathVariable("name") String communityName);

  @GetExchange(url = "/api/v1/communities")
  List<Community> getAllCommunities();
}
