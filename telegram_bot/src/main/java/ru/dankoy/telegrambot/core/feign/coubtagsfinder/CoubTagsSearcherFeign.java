package ru.dankoy.telegrambot.core.feign.coubtagsfinder;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.telegrambot.core.domain.tagsubscription.Tag;

@FeignClient(name = "coub-tags-searcher")
public interface CoubTagsSearcherFeign {


  @GetMapping(path = "/api/v1/tags", params = {"title"})
  Tag searchByTitle(@RequestParam("title") String title);


}
