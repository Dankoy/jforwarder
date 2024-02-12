package ru.dankoy.coubtagssearcher.core.feign;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.coubtagssearcher.core.domain.coubcom.TagsWrapper;

@Cacheable(cacheNames = "coubs-feign-cache", cacheManager = "caffeineCacheManager")
@FeignClient(name = "coubs", url = "${coub.connector.gatewayApiUrl}api/v2/smart_search/")
public interface CoubSmartSearchFeign {

  @GetMapping(
      value = "/tags",
      params = {"search_query", "page"})
  TagsWrapper getTags(
      @RequestParam("search_query") String searchQuery, @RequestParam("page") int page);
}
