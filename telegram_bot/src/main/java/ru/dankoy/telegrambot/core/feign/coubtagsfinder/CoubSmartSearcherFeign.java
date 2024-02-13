package ru.dankoy.telegrambot.core.feign.coubtagsfinder;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.telegrambot.core.domain.channel.Channel;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;

@FeignClient(name = "coub-smart-searcher")
public interface CoubSmartSearcherFeign {

  @GetMapping(
      path = "/api/v1/tags",
      params = {"title"})
  Tag searchTagByTitle(@RequestParam("title") String title);

  @GetMapping(
      path = "/api/v1/channels",
      params = {"title"})
  Channel searchChannelByTitle(@RequestParam("title") String title);
}