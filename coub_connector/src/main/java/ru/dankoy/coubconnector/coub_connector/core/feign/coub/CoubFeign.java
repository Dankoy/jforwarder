package ru.dankoy.coubconnector.coub_connector.core.feign.coub;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.CoubWrapper;

//GET
//    https://coub.com/api/v2/timeline/community/anime/rising?page=1

@FeignClient(name = "coubs", url = "${coub.connector.apiUrl}timeline/community/")
public interface CoubFeign {


  @GetMapping(value = "{communityName}/{section}")
  CoubWrapper getCoubsForCommunityWrapperPageable(
      @PathVariable(value = "communityName") String community,
      @PathVariable(value = "section") String section,
      @RequestParam("page") int page);


}
