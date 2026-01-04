package ru.dankoy.tcoubsinitiator.core.httpservice.coub;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.CoubWrapper;

@RateLimiter(name = "coub")
@Cacheable(cacheNames = "coubs-restclient-cache", cacheManager = "caffeineCacheManager")
@HttpExchange(url = "${coub.connector.gatewayApiUrl}api/v2/")
public interface CoubHttpService {

  @GetExchange(value = "timeline/community/{communityName}/{section}")
  CoubWrapper getCoubsForCommunityWrapperPageable(
      @PathVariable(value = "communityName") String community,
      @PathVariable(value = "section") String section,
      @RequestParam("page") long page,
      @RequestParam("per_page") int perPage);

  @GetExchange(value = "timeline/tag/{tag}")
  CoubWrapper getCoubsForTagWrapperPageable(
      @PathVariable(value = "tag") String tag,
      @RequestParam("order_by") String orderBy,
      @RequestParam("type") String type,
      @RequestParam("scope") String scope,
      @RequestParam("page") long page,
      @RequestParam("per_page") long perPage);

  @GetExchange(value = "timeline/channel/{permalink}")
  CoubWrapper getCoubsForChannelWrapperPageable(
      @PathVariable(value = "permalink") String permalink,
      @RequestParam("order_by") String orderBy,
      @RequestParam("type") String type,
      @RequestParam("scope") String scope,
      @RequestParam("page") long page,
      @RequestParam("per_page") long perPage);
}
