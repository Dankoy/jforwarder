package ru.dankoy.coubconnector.coub_connector.core.service.coub;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.CoubWrapper;
import ru.dankoy.coubconnector.coub_connector.core.feign.coub.CoubFeign;
import ru.dankoy.coubconnector.coub_connector.core.service.permalinkcreator.PermalinkCreatorService;

@Service
@RequiredArgsConstructor
public class CoubServiceImpl implements CoubService {

  private final CoubFeign coubFeign;
  private final PermalinkCreatorService permalinkCreatorService;

  @Override
  public CoubWrapper getCoubsWrapperForCommunityAndSection(String communityName, String sectionName,
      int page) {

    var wrapper = coubFeign.getCoubsForCommunityWrapperPageable(communityName, sectionName, page);

    wrapper.getCoubs()
        .forEach(permalinkCreatorService::createCoubPermalink);

    return wrapper;

  }

}
