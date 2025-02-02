package ru.dankoy.coubtagssearcher.core.feign;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.coubtagssearcher.core.domain.coubcom.ChannelsWrapper;

@RateLimiter(name = "coub")
@Cacheable(cacheNames = "coubs-feign-cache", cacheManager = "caffeineCacheManager")
@FeignClient(name = "coub-search", url = "${coub.connector.gatewayApiUrl}api/v2/search")
public interface CoubSearchFeign {

  @GetMapping(
      value = "/channels",
      params = {"q", "page", "per_page"})
  ChannelsWrapper getChannels(
      @RequestParam("q") String query,
      @RequestParam("page") int page,
      @RequestParam("per_page") int perPage);
}
