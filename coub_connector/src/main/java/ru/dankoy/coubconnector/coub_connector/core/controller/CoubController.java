package ru.dankoy.coubconnector.coub_connector.core.controller;


import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.Coub;
import ru.dankoy.coubconnector.coub_connector.core.service.coub.CoubService;


@RequiredArgsConstructor
@RestController
public class CoubController {

  private final CoubService coubService;

  /* todo: section cat be only rising and fresh
   * to make top section you have to order by likes_count
   */

  //todo: add validation of section name

  @GetMapping(
      value = "/api/v1/coubs/community/{communityName}/{section}",
      params = {"page"}
  )
  public List<Coub> getCommunities(
      @PathVariable(value = "communityName") String communityName,
      @PathVariable(value = "section") String section,
      @PathParam(value = "page") int page
  ) {

    return coubService
        .getCoubsWrapperForCommunityAndSection(communityName, section, page)
        .getCoubs();

  }


}
