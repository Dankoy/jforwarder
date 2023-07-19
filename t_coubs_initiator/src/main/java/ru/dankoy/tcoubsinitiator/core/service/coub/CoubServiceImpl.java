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
  public CoubWrapper getCoubsWrapperForCommunityAndSection(String communityName, String sectionName,
      int page, int perPage) {

    var wrapper = coubFeign.getCoubsForCommunityWrapperPageable(communityName, sectionName, page,
        perPage);

    wrapper.getCoubs()
        .forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;

  }

}
