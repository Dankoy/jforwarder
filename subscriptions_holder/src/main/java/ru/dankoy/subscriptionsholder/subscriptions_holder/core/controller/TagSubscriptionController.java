package ru.dankoy.subscriptionsholder.subscriptions_holder.core.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TagSubscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.service.TagSubscriptionService;


@RequiredArgsConstructor
@RestController
public class TagSubscriptionController {

  private final TagSubscriptionService tagSubscriptionService;


  @GetMapping(value = "/api/v1/tag_subscriptions", params = {"active"})
  public List<TagSubscription> getAllByActiveChat(@RequestParam("active") boolean active) {

    return tagSubscriptionService.getAllByActiveTelegramChat(active);

  }


  @PostMapping(value = "/api/v1/tag_subscriptions")
  public TagSubscription create(@RequestBody TagSubscription tagSubscription) {

    return tagSubscriptionService.createSubscription(tagSubscription);

  }

  @DeleteMapping(value = "/api/v1/tag_subscriptions")
  public void delete(@RequestBody TagSubscription tagSubscription) {

    tagSubscriptionService.deleteSubscription(tagSubscription);

  }


  @PutMapping("/api/v1/tag_subscriptions")
  public TagSubscription updatePermalink(@RequestBody TagSubscription tagSubscription) {

    return tagSubscriptionService.updatePermalink(tagSubscription);

  }

}
