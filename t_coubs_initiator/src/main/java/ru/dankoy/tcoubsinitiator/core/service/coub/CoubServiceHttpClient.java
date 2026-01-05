package ru.dankoy.tcoubsinitiator.core.service.coub;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.CoubWrapper;
import ru.dankoy.tcoubsinitiator.core.httpservice.coub.CoubHttpService;
import ru.dankoy.tcoubsinitiator.core.service.permalinkcreator.PermalinkCreatorService;

@Primary
@Service
@RequiredArgsConstructor
public class CoubServiceHttpClient implements CoubService {

  private final CoubHttpService coubHttpService;
  private final PermalinkCreatorService permalinkCreatorService;

  @Override
  public CoubWrapper getCoubsWrapperForCommunityAndSection(
      String communityName, String sectionName, long page, int perPage) {

    var wrapper =
        coubHttpService.getCoubsForCommunityWrapperPageable(
            communityName, sectionName, page, perPage);

    wrapper.getCoubs().forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;
  }

  @Override
  public CoubWrapper getCoubsWrapperForTag(
      String tag, String orderBy, String type, String scope, long page, int perPage) {

    var wrapper =
        coubHttpService.getCoubsForTagWrapperPageable(tag, orderBy, type, scope, page, perPage);

    wrapper.getCoubs().forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;
  }

  @Override
  public CoubWrapper getCoubsWrapperForChannel(
      String channelPermalink, String orderBy, String type, String scope, long page, int perPage) {

    var wrapper =
        coubHttpService.getCoubsForChannelWrapperPageable(
            channelPermalink, orderBy, type, scope, page, perPage);

    wrapper.getCoubs().forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;
  }
}
