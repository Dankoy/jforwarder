package ru.dankoy.subscriptions_scheduler.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;
import ru.dankoy.subscriptions_scheduler.core.mapper.SubscriptionMapper;
import ru.dankoy.subscriptions_scheduler.core.service.subscriptions.SubscriptionService;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;
  private final SubscriptionMapper subscriptionMapper;

  @GetMapping("/api/v1/subscriptions")
  public PagedModel<SubscriptionDTO> getAllChats(Pageable pageable) {

    var page = subscriptionService.findAll(pageable);

    var dtoPage = page.map(subscriptionMapper::tDto);

    return new PagedModel<>(dtoPage);
  }
}
