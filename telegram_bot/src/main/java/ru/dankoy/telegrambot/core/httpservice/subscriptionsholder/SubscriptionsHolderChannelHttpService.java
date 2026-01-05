package ru.dankoy.telegrambot.core.httpservice.subscriptionsholder;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderChannelHttpService {

  @GetExchange(url = "/api/v1/channels")
  Channel getChannelByPermalink(@RequestParam("permalink") String permalink);

  @PostExchange(url = "/api/v1/channels")
  Channel createChannel(@RequestBody Channel channel);
}
