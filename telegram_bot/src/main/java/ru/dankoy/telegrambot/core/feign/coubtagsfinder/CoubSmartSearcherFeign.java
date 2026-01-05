package ru.dankoy.telegrambot.core.feign.coubtagsfinder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;

/**
 * @deprecated since spring-boot 4.0.0 in favor {@link ChannelSubscriptionServiceHttpClient}
 */
@Deprecated(since = "2025-01-04", forRemoval = true)
@FeignClient(name = "coub-smart-searcher")
public interface CoubSmartSearcherFeign {

  @GetMapping(
      path = "/api/v1/tags",
      params = {"title"})
  Tag searchTagByTitle(@RequestParam("title") String title);

  @GetMapping(
      path = "/api/v1/channels",
      params = {"permalink"})
  Channel searchChannelByPermalink(@RequestParam("permalink") String permalink);
}
