package ru.dankoy.coubconnector.coub_connector.core.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.community.Community;
import ru.dankoy.coubconnector.coub_connector.core.service.community.CommunityService;


@RequiredArgsConstructor
@RestController
public class CommunityController {


  private final CommunityService communityService;


  @GetMapping("/api/v1/communities")
  public List<Community> getCommunities() {

    return communityService
        .getCommunitiesWrapper()
        .getCommunities();

  }


}
