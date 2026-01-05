package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderTagHttpService {

  @GetExchange(url = "/api/v1/tags")
  Tag getTagByTitle(@RequestParam("title") String title);

  @PostExchange(url = "/api/v1/tags")
  Tag createTag(@RequestBody Tag tag);
}
