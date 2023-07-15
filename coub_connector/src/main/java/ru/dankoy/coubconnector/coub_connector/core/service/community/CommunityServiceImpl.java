package ru.dankoy.coubconnector.coub_connector.core.service.community;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.community.CommunityWrapper;
import ru.dankoy.coubconnector.coub_connector.core.feign.community.CommunityFeign;
import ru.dankoy.coubconnector.coub_connector.core.service.permalinkcreator.PermalinkCreatorService;


@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

  private final CommunityFeign communityFeign;
  private final PermalinkCreatorService permalinkCreatorService;

  @Override
  public CommunityWrapper getCommunitiesWrapper() {

    var wrapper = communityFeign.getCommunitiesWrapper();

    wrapper.getCommunities()
        .forEach(permalinkCreatorService::createCommunityPermalink);

    return wrapper;

  }
}
