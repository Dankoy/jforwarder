package ru.dankoy.tcoubsinitiator.core.service.permalinkcreator;

import ru.dankoy.tcoubsinitiator.core.domain.coubcom.coub.Permalink;

public interface PermalinkCreatorService {

  void createCommunityPermalink(Permalink permalink);

  void createCoubPermalink(Permalink permalink);
}
