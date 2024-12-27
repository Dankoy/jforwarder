package ru.dankoy.subscriptions_scheduler.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;

@FeignClient(contextId = "subscriptions", name = "subscriptions-holder")
public interface SubscriptionsFeign {

  @GetMapping(
      path = "/api/v1/subscriptions",
      params = {"page", "size", "sort"})
  Page<SubscriptionDTO> getAllChats(Pageable pageable);
}
