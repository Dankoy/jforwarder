package ru.dankoy.tcoubsinitiator.core.service.coub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.CoubWrapper;
import ru.dankoy.tcoubsinitiator.core.feign.coub.CoubFeign;
import ru.dankoy.tcoubsinitiator.core.service.permalinkcreator.PermalinkCreatorService;

@Service
@RequiredArgsConstructor
public class CoubServiceImpl implements CoubService {

  private final CoubFeign coubFeign;
  private final PermalinkCreatorService permalinkCreatorService;

  @Override
  public CoubWrapper getCoubsWrapperForCommunityAndSection(
      String communityName, String sectionName, long page, int perPage) {

    var wrapper =
        coubFeign.getCoubsForCommunityWrapperPageable(communityName, sectionName, page, perPage);

    wrapper.getCoubs().forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;
  }

  @Override
  public CoubWrapper getCoubsWrapperForTag(
      String tag, String orderBy, String type, String scope, long page, int perPage) {

    var wrapper = coubFeign.getCoubsForTagWrapperPageable(tag, orderBy, type, scope, page, perPage);

    wrapper.getCoubs().forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;
  }

  @Override
  public CoubWrapper getCoubsWrapperForChannel(
      String channelPermalink, String orderBy, String type, String scope, long page, int perPage) {

    var wrapper =
        coubFeign.getCoubsForChannelWrapperPageable(
            channelPermalink, orderBy, type, scope, page, perPage);

    wrapper.getCoubs().forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;
  }
}
