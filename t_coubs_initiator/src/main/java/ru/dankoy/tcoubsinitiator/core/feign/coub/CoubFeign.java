package ru.dankoy.tcoubsinitiator.core.feign.coub;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.CoubWrapper;

// GET
//    https://coub.com/api/v2/timeline/community/anime/rising?page=1

@RateLimiter(name = "coub")
@Cacheable(cacheNames = "coubs-feign-cache", cacheManager = "caffeineCacheManager")
@FeignClient(name = "coubs", url = "${coub.connector.gatewayApiUrl}api/v2/")
public interface CoubFeign {

  @GetMapping(
      value = "timeline/community/{communityName}/{section}",
      params = {"page", "per_page"})
  CoubWrapper getCoubsForCommunityWrapperPageable(
      @PathVariable(value = "communityName") String community,
      @PathVariable(value = "section") String section,
      @RequestParam("page") long page,
      @RequestParam("per_page") int perPage);

  @GetMapping(
      value = "timeline/tag/{tag}",
      params = {"order_by", "type", "scope", "page", "per_page"})
  CoubWrapper getCoubsForTagWrapperPageable(
      @PathVariable(value = "tag") String tag,
      @RequestParam("order_by") String orderBy,
      @RequestParam("type") String type,
      @RequestParam("scope") String scope,
      @RequestParam("page") long page,
      @RequestParam("per_page") long perPage);

  @GetMapping(
      value = "timeline/channel/{permalink}",
      params = {"permalink", "order_by", "type", "scope", "page", "per_page"})
  CoubWrapper getCoubsForChannelWrapperPageable(
      @PathVariable(value = "permalink") @RequestParam("permalink") String permalink,
      @RequestParam("order_by") String orderBy,
      @RequestParam("type") String type,
      @RequestParam("scope") String scope,
      @RequestParam("page") long page,
      @RequestParam("per_page") long perPage);
}
