package ru.dankoy.tcoubsinitiator.core.service.coub;

import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.CoubWrapper;

public interface CoubService {

  CoubWrapper getCoubsWrapperForCommunityAndSection(String communityName, String sectionName,
      int page, int perPage);
}
