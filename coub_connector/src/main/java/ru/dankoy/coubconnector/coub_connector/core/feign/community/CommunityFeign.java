package ru.dankoy.coubconnector.coub_connector.core.feign.community;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.community.CommunityWrapper;

@FeignClient(name = "community", url = "${coub.connector.apiUrl}")
public interface CommunityFeign {

  @GetMapping(value = "/communities")
  CommunityWrapper getCommunitiesWrapper();

}
