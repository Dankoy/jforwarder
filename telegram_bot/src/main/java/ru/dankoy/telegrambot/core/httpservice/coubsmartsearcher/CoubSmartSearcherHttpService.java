package ru.dankoy.telegrambot.core.httpservice.coubsmartsearcher;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import ru.dankoy.telegrambot.core.domain.subscription.channel.Channel;
import ru.dankoy.telegrambot.core.domain.subscription.tag.Tag;

@HttpExchange(url = "http://coub-smart-searcher")
public interface CoubSmartSearcherHttpService {

  @GetExchange(url = "/api/v1/tags")
  Tag searchTagByTitle(@RequestParam("title") String title);

  @GetExchange(url = "/api/v1/channels")
  Channel searchChannelByPermalink(@RequestParam("permalink") String permalink);
}
