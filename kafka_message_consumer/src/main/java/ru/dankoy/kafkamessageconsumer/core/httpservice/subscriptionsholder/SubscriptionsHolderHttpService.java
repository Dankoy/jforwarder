package ru.dankoy.kafkamessageconsumer.core.httpservice.subscriptionsholder;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import ru.dankoy.kafkamessageconsumer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;

@HttpExchange(url = "http://subscriptions-holder")
public interface SubscriptionsHolderHttpService {

  @PutExchange(url = "/api/v1/subscriptions")
  Subscription updateSubscriptionPermalink(@RequestBody Subscription subscription);

  @PostExchange(url = "/api/v1/sent_coubs_registry")
  SentCoubsRegistry createRegistryEntry(@RequestBody SentCoubsRegistry sentCoubsRegistry);
}
