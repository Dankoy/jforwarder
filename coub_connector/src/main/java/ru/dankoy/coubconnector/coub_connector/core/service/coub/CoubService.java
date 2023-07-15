package ru.dankoy.coubconnector.coub_connector.core.service.coub;

import ru.dankoy.coubconnector.coub_connector.core.domain.coubcom.coub.CoubWrapper;

public interface CoubService {

  CoubWrapper getCoubsWrapperForCommunityAndSection(String communityName, String sectionName,
      int page);
}
