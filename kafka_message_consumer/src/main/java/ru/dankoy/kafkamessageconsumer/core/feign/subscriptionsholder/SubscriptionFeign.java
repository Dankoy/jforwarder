package ru.dankoy.kafkamessageconsumer.core.feign.subscriptionsholder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dankoy.kafkamessageconsumer.core.domain.registry.SentCoubsRegistry;
import ru.dankoy.kafkamessageconsumer.core.domain.subscription.Subscription;

@FeignClient(name = "subscriptions-holder")
public interface SubscriptionFeign {

  @PutMapping(path = "/api/v1/subscriptions")
  Subscription updateSubscriptionPermalink(@RequestBody Subscription subscription);

  @PostMapping(path = "/api/v1/sent_coubs_registry")
  SentCoubsRegistry createRegistryEntry(@RequestBody SentCoubsRegistry sentCoubsRegistry);
}
